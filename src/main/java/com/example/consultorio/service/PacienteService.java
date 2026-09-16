package com.example.consultorio.service;

import com.example.consultorio.common.exception.CpfJaCadastradoException;
import com.example.consultorio.common.exception.EmailJaCadastradoException;
import com.example.consultorio.common.exception.RecursoNaoEncontradoException;
import com.example.consultorio.model.Paciente;
import com.example.consultorio.model.dto.requests.PacienteRequest;
import com.example.consultorio.model.dto.responses.PacienteResponse;
import com.example.consultorio.model.enums.PacienteSort;
import com.example.consultorio.repository.PacienteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @Transactional
    public PacienteResponse criar(PacienteRequest pacienteRequest) {
        if (pacienteRepository.existsByEmail(pacienteRequest.email())) {
            throw new EmailJaCadastradoException(pacienteRequest.email());
        }

        if (pacienteRepository.existsByCpf(pacienteRequest.cpf())) {
            throw new CpfJaCadastradoException(pacienteRequest.cpf());
        }

        Paciente paciente = new Paciente(
                pacienteRequest.cpf(),
                pacienteRequest.nome(),
                pacienteRequest.email(),
                pacienteRequest.telefone(),
                pacienteRequest.dataNascimento()
        );

        pacienteRepository.save(paciente);

        return PacienteResponse.from(paciente);
    }

    @Transactional(readOnly = true)
    public Page<PacienteResponse> listar(int pagina, int limite, PacienteSort sortBy, Sort.Direction direcao) {

        // essa paginação utiliza LIMIT e OFFSET
        Pageable pageable = PageRequest.of(pagina, limite,
                Sort.by(direcao, sortBy.getProperty()).and(Sort.by(Sort.Direction.ASC, "id")));
        Page<Paciente> page = pacienteRepository.findAll(pageable);
        return page.map(PacienteResponse::from);
    }

    @Transactional(readOnly = true)
    public PacienteResponse buscarPorId(Long id) {
        return PacienteResponse.from(pacienteRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Paciente não encontrado")));
    }

}
