package com.example.consultorio.service;

import com.example.consultorio.common.exception.ConflitoException;
import com.example.consultorio.common.exception.IdempotencyConflictException;
import com.example.consultorio.common.exception.RecursoNaoEncontradoException;
import com.example.consultorio.common.exception.ValidacaoException;
import com.example.consultorio.config.HorarioClinicaProperties;
import com.example.consultorio.model.Agendamento;
import com.example.consultorio.model.Dentista;
import com.example.consultorio.model.Paciente;
import com.example.consultorio.model.Procedimento;
import com.example.consultorio.model.dto.filtros.FiltroAgendamento;
import com.example.consultorio.model.dto.requests.AgendamentoRequest;
import com.example.consultorio.model.dto.responses.AgendamentoResponse;
import com.example.consultorio.model.enums.AgendamentoStatus;
import com.example.consultorio.repository.AgendamentoRepository;
import com.example.consultorio.repository.DentistaRepository;
import com.example.consultorio.repository.PacienteRepository;
import com.example.consultorio.repository.ProcedimentoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

@Service
public class AgendamentoService {
    private static final DateTimeFormatter FORMATO_DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final AgendamentoRepository agendamentoRepository;
    private final PacienteRepository pacienteRepository;
    private final DentistaRepository dentistaRepository;
    private final ProcedimentoRepository procedimentoRepository;
    private final HorarioClinicaProperties horarioClinicaProperties;

    public AgendamentoService(AgendamentoRepository agendamentoRepository, PacienteRepository pacienteRepository, DentistaRepository dentistaRepository, ProcedimentoRepository procedimentoRepository, HorarioClinicaProperties horarioClinicaProperties) {
        this.agendamentoRepository = agendamentoRepository;
        this.pacienteRepository = pacienteRepository;
        this.dentistaRepository = dentistaRepository;
        this.procedimentoRepository = procedimentoRepository;
        this.horarioClinicaProperties = horarioClinicaProperties;
    }

    @Transactional
    public ResultadoCriacaoAgendamento criar(AgendamentoRequest request, String idempotencyKey) {
        // valida a chave de idempotência no timeframe especificado e se representa o objeto correto
        LocalDateTime agora = LocalDateTime.now();
        Optional<Agendamento> existente = agendamentoRepository
                .findByIdempotencyKeyAndCriadoEmBetween(idempotencyKey, agora.minusHours(24), agora);

        if (existente.isPresent()) {
            Agendamento a = existente.get();
            boolean mesmoCorpo = a.getPaciente().getId().equals(request.pacienteId())
                    && a.getDentista().getId().equals(request.dentistaId())
                    && a.getProcedimento().getId().equals(request.procedimentoId())
                    && a.getInicio().equals(request.inicio())
                    && Objects.equals(a.getObservacao(), request.observacao());
            if (mesmoCorpo) {
                // mata o processo aqui
                return new ResultadoCriacaoAgendamento(AgendamentoResponse.from(a), false);
            }
            throw new IdempotencyConflictException("A Idempotency-Key foi usada com um corpo de requisição diferente.");
        }

        // valida se os horario de ínicio segue as regras de negócio (RN04)
        this.validaHorarioInicio(request.inicio());

        // valida se os itens existem
        Paciente paciente = pacienteRepository.findById(request.pacienteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Paciente"));
        Dentista dentista = dentistaRepository.findById(request.dentistaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Dentista"));
        Procedimento procedimento = procedimentoRepository.findById(request.procedimentoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Procedimento"));

        // caso o dentista esteja inativo é impossível criar um novo agendamento para ele
        if (!dentista.isAtivo()) {
            throw new ValidacaoException("Não é possível fazer um agendamento com um dentista inativo");
        }

        // RN01
        LocalDateTime horarioFim = request.inicio().plusMinutes(procedimento.getDuracaoMinutos());

        // RN02
        if(this.existeConflitoDeHorario(dentista, request.inicio(), horarioFim)) {
            throw new ConflitoException("O dentista %s está ocupado no horário requisitado (%s)".formatted(dentista.getNome(), request.inicio().format(FORMATO_DATA_HORA)));
        }

        //RN03
        this.validaHorarioAgendamentoHorarioClinica(request.inicio(), horarioFim);

        Agendamento agendamento = new Agendamento(paciente, dentista, procedimento, request.inicio(), horarioFim, AgendamentoStatus.AGENDADO, request.observacao());
        agendamento.setIdempotencyKey(idempotencyKey);

        try {
            agendamentoRepository.save(agendamento);
            return new ResultadoCriacaoAgendamento(AgendamentoResponse.from(agendamento), true);
        } catch (DataIntegrityViolationException e) {
            throw new ConflitoException("Já existe um agendamento criado para essa Idempotency-Key.");
        }
    }

    //RN02 Dois agendamentos do mesmo dentista não podem se sobrepor no tempo.
    private boolean existeConflitoDeHorario(Dentista dentista, LocalDateTime horarioInicio, LocalDateTime horarioFim) {
        // busca se não existe um agendamento no horário de início
        Long dentistaId = dentista.getId();
        // valida se adicionando o horário final não tem uma interseção com outro agendamento
        return agendamentoRepository.findConflitosDeHorario(dentistaId, horarioInicio, horarioFim).isEmpty();
    }

    //RN03 O agendamento inteiro precisa caber dentro do horário de atendimento da clínica:
    // segunda a sexta das 08:00 às 18:00, sábado das 08:00 às 12:00.
    private void validaHorarioAgendamentoHorarioClinica(LocalDateTime inicio, LocalDateTime fim) {
        validaDentroDoExpediente(inicio);
        validaDentroDoExpediente(fim);
    }

    private void validaDentroDoExpediente(LocalDateTime momento) {
        DayOfWeek diaDaSemana = momento.getDayOfWeek();
        LocalTime hora = momento.toLocalTime();

        LocalTime aberturaExpediente;
        LocalTime fechamentoExpediente;

        if (diaDaSemana == DayOfWeek.SATURDAY) {
            aberturaExpediente = horarioClinicaProperties.sabadoInicio();
            fechamentoExpediente = horarioClinicaProperties.sabadoFim();
        } else if (diaDaSemana == DayOfWeek.SUNDAY) {
            throw new ValidacaoException("A clínica não funciona no domingo.");
        } else {
            aberturaExpediente = horarioClinicaProperties.segASexInicio();
            fechamentoExpediente = horarioClinicaProperties.segASexFim();
        }

        if (hora.isBefore(aberturaExpediente) || hora.isAfter(fechamentoExpediente)) {
            throw new ValidacaoException(
                    "O agendamento precisa estar dentro do horário de atendimento da clínica.");
        }

    }


    //RN04 Não se agenda no passado, e o início precisa estar a pelo menos 30 minutos de distância do momento da requisição.
    private void validaHorarioInicio(LocalDateTime inicio) {
        var agora = LocalDateTime.now();
        // valida se tem está na antecedência mínima
        var horarioMinimo = agora.plus(horarioClinicaProperties.antecedenciaMinima());
        if (inicio.isBefore(horarioMinimo)) {
            throw new ValidacaoException(
                    "O agendamento deve ser feito com no mínimo %d minutos de antecedência."
                            .formatted(horarioClinicaProperties.antecedenciaMinima().toMinutes()));
        }
        // valida se não está no passado explicitamente
        if (inicio.isBefore(agora)) {
            throw new ValidacaoException(
                    "O agendamento não pode ser feito no passado.");
        }
    }

    public Page<AgendamentoResponse> listar(FiltroAgendamento filtro, int pagina, int limite) {
        Pageable pageable = PageRequest.of(pagina, limite, Sort.by(Sort.Direction.ASC, "inicio"));
        return agendamentoRepository.findAll(filtro.toSpecification(), pageable).map(AgendamentoResponse::from);
    }

    public record ResultadoCriacaoAgendamento(AgendamentoResponse agendamento, boolean novo) {
    }
}
