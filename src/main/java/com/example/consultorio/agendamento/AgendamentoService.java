package com.example.consultorio.agendamento;

import com.example.consultorio.dentista.Dentista;
import com.example.consultorio.dentista.DentistaRepository;
import com.example.consultorio.paciente.Paciente;
import com.example.consultorio.paciente.PacienteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final PacienteRepository pacienteRepository;
    private final DentistaRepository dentistaRepository;

    public AgendamentoService(AgendamentoRepository agendamentoRepository, PacienteRepository pacienteRepository, DentistaRepository dentistaRepository) {
        this.agendamentoRepository = agendamentoRepository;
        this.pacienteRepository = pacienteRepository;
        this.dentistaRepository = dentistaRepository;
    }

    public AgendamentoResponse criar(AgendamentoRequest request) {
        Paciente paciente = pacienteRepository.findById(request.pacienteId()).orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));
        Dentista dentista = dentistaRepository.findById(request.dentistaId()).orElseThrow(() - new EntityNotFoundException("Dentista não encontrado"));

        Agendamento agendamento = new Agendamento(paciente, dentista, request.inicio(), request.fim(), AgendamentoStatus.AGENDADO, request.observacao());
        agendamentoRepository.save(agendamento);

        return new AgendamentoResponse.from(agendamento);
    }
}
