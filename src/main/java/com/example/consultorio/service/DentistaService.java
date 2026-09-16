package com.example.consultorio.service;

import com.example.consultorio.model.Dentista;
import com.example.consultorio.model.dto.responses.DentistaResponse;
import com.example.consultorio.repository.DentistaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DentistaService {

    private final DentistaRepository dentistaRepository;

    public DentistaService(DentistaRepository dentistaRepository) {
        this.dentistaRepository = dentistaRepository;
    }

    @Transactional(readOnly = true)
    public Page<DentistaResponse> listar(int pagina, int limite) {

        // essa paginação utiliza LIMIT e OFFSET
        Pageable pageable = PageRequest.of(pagina, limite, Sort.by(Sort.Direction.ASC, "nome"));
        Page<Dentista> dentistasPage = dentistaRepository.findAll(pageable);
        return dentistasPage.map(DentistaResponse::from);
    }



}
