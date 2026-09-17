package com.example.consultorio.service;

import com.example.consultorio.common.exception.RecursoNaoEncontradoException;
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
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final PacienteRepository pacienteRepository;
    private final DentistaRepository dentistaRepository;
    private final ProcedimentoRepository procedimentoRepository;

    public AgendamentoService(AgendamentoRepository agendamentoRepository, PacienteRepository pacienteRepository, DentistaRepository dentistaRepository, ProcedimentoRepository procedimentoRepository) {
        this.agendamentoRepository = agendamentoRepository;
        this.pacienteRepository = pacienteRepository;
        this.dentistaRepository = dentistaRepository;
        this.procedimentoRepository = procedimentoRepository;
    }

    //RN01 O fim do agendamento é calculado no servidor, somando a duração do procedimento ao início.
    //RN02 Dois agendamentos do mesmo dentista não podem se sobrepor no tempo.
    //RN03 O agendamento inteiro precisa caber dentro do horário de atendimento da clínica: segunda a sexta das 08:00 às 18:00, sábado das 08:00 às 12:00.
    //RN04 Não se agenda no passado, e o início precisa estar a pelo menos 30 minutos de distância do momento da requisição.
    //RN05 Dentista inativo não recebe agendamento novo, mas os agendamentos que ele já tinha continuam válidos e visíveis.

    @Transactional
    public ResultadoCriacaoAgendamento  criar(AgendamentoRequest request, String idempotencyKey) {
        LocalDateTime inicioDoDia = LocalDate.now().atStartOfDay();
        LocalDateTime fimDoDia = inicioDoDia.plusDays(1);
        Optional<Agendamento> existente = agendamentoRepository
                .findByIdempotencyKeyAndCriadoEmBetween(idempotencyKey, inicioDoDia, fimDoDia);
        if (existente.isPresent()) {
            return new ResultadoCriacaoAgendamento(AgendamentoResponse.from(existente.get()), false);
        }

        // valida se os itens existem
        Paciente paciente = pacienteRepository.findById(request.pacienteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Paciente"));
        Dentista dentista = dentistaRepository.findById(request.dentistaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Dentista"));
        Procedimento procedimento = procedimentoRepository.findById(request.procedimentoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Procedimento"));


        // checa se o dentista está disponível (é ativo) ou possui disponibilidade naquele horário
        if(!this.checaDisponibilidadeDentista(dentista, request.inicio())) {
            //throw new DentistaNaoDisponivelException();
        }

        // RN01
        // adiciona o tempo do procedimento com o tempo do início
        LocalDateTime horarioFim = request.inicio().plusMinutes(procedimento.getDuracaoMinutos());



        Agendamento agendamento = new Agendamento(paciente, dentista, procedimento, request.inicio(), horarioFim, AgendamentoStatus.AGENDADO, request.observacao());
        agendamento.setIdempotencyKey(idempotencyKey);
        agendamentoRepository.save(agendamento);
        return new ResultadoCriacaoAgendamento(AgendamentoResponse.from(agendamento), true);
    }

    private boolean checaDisponibilidadeDentista(Dentista dentista, LocalDateTime horarioInicio) {
        return false;
    }

    public Page<AgendamentoResponse> listar(FiltroAgendamento filtro, int pagina, int limite) {
        Pageable pageable = PageRequest.of(pagina, limite, Sort.by(Sort.Direction.ASC, "inicio"));
        return agendamentoRepository.findAll(filtro.toSpecification(), pageable).map(AgendamentoResponse::from);
    }

    public record ResultadoCriacaoAgendamento(AgendamentoResponse agendamento, boolean novo) {}
}
