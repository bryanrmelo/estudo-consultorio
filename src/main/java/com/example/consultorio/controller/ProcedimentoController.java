package com.example.consultorio.controller;

import com.example.consultorio.model.dto.requests.ProcedimentoRequest;
import com.example.consultorio.model.dto.responses.ProcedimentoResponse;
import com.example.consultorio.service.ProcedimentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/procedimentos")
public class ProcedimentoController {

    private final ProcedimentoService procedimentoService;

    public ProcedimentoController(ProcedimentoService procedimentoService) {
        this.procedimentoService = procedimentoService;
    }

    @PostMapping
    public ResponseEntity<ProcedimentoResponse> criar(@RequestBody @Valid ProcedimentoRequest request) {
        ProcedimentoResponse procedimentoResponse = procedimentoService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(procedimentoResponse);
    }
}
