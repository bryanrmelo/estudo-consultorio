package com.example.consultorio.service;

import com.example.consultorio.model.Procedimento;
import com.example.consultorio.model.dto.requests.ProcedimentoRequest;
import com.example.consultorio.model.dto.responses.ProcedimentoResponse;
import com.example.consultorio.repository.ProcedimentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProcedimentoService {

    private final ProcedimentoRepository procedimentoRepository;

    public ProcedimentoService(ProcedimentoRepository procedimentoRepository) {
        this.procedimentoRepository = procedimentoRepository;
    }

    @Transactional
    public ProcedimentoResponse criar(ProcedimentoRequest request) {
        Procedimento procedimento = new Procedimento(request.nome(), request.duracaoMinutos(), request.valor());
        procedimentoRepository.save(procedimento);

        return ProcedimentoResponse.from(procedimento);
    }
}
