package com.example.consultorio.service;

import com.example.consultorio.common.EmailJaCadastradoException;
import com.example.consultorio.model.Paciente;
import com.example.consultorio.model.dto.PacienteRequest;
import com.example.consultorio.model.dto.PacienteResponse;
import com.example.consultorio.repository.PacienteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @Transactional
    public PacienteResponse criar(PacienteRequest pacienteRequest) {
        if(pacienteRepository.existsByEmail(pacienteRequest.email())) {
            throw new EmailJaCadastradoException(
                    "Já existe paciente cadastrado com o email " + pacienteRequest.email()
            );
        }

        Paciente paciente = new Paciente(pacienteRequest.nome(), pacienteRequest.email(), pacienteRequest.telefone(), pacienteRequest.dataNascimento());

        pacienteRepository.save(paciente);

        return PacienteResponse.from(paciente);
    }

    @Transactional(readOnly = true)
    public List<PacienteResponse> listar() {
        return pacienteRepository.findAll().stream().map(PacienteResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public PacienteResponse buscarPorId(Long id) {
        return PacienteResponse.from(pacienteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado")));
    }

}
