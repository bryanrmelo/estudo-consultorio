package com.example.consultorio.service;

import com.example.consultorio.model.dto.AgendamentoRequest;
import com.example.consultorio.model.enums.AgendamentoStatus;
import com.example.consultorio.model.Dentista;
import com.example.consultorio.repository.DentistaRepository;
import com.example.consultorio.model.Agendamento;
import com.example.consultorio.model.dto.AgendamentoResponse;
import com.example.consultorio.model.Paciente;
import com.example.consultorio.repository.PacienteRepository;
import com.example.consultorio.repository.AgendamentoRepository;
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
