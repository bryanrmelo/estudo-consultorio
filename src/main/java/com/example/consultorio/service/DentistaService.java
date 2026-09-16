package com.example.consultorio.service;

import com.example.consultorio.common.exception.CroJaCadastradoException;
import com.example.consultorio.common.exception.RecursoNaoEncontradoException;
import com.example.consultorio.model.Dentista;
import com.example.consultorio.model.dto.requests.AtualizarDentistaStatusRequest;
import com.example.consultorio.model.dto.requests.DentistaRequest;
import com.example.consultorio.model.dto.responses.DentistaResponse;
import com.example.consultorio.repository.DentistaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DentistaService {

    private final DentistaRepository dentistaRepository;

    public DentistaService(DentistaRepository dentistaRepository) {
        this.dentistaRepository = dentistaRepository;
    }

    @Transactional
    public DentistaResponse criar(DentistaRequest request) {
        if (dentistaRepository.existsByCro(request.cro())) {
            throw new CroJaCadastradoException(request.cro());
        }

        Dentista dentista = new Dentista(
                request.nome(),
                request.cro(),
                request.especialidade()
        );

        dentistaRepository.save(dentista);

        return DentistaResponse.from(dentista);
    }

    @Transactional
    public DentistaResponse atualizarStatus(Long id, AtualizarDentistaStatusRequest request) {
        Dentista dentista = dentistaRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Dentista não encontrado"));
        dentista.setAtivo(request.ativo());

        dentistaRepository.save(dentista);
        return DentistaResponse.from(dentista);
    }
}
