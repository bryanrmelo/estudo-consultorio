package com.example.consultorio.controller;

import com.example.consultorio.model.dto.PacienteRequest;
import com.example.consultorio.model.dto.PacienteResponse;
import com.example.consultorio.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @GetMapping
    public ResponseEntity<List<PacienteResponse>> listar() {
        List<PacienteResponse> pacienteResponseList = pacienteService.listar();
        return ResponseEntity.ok(pacienteResponseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponse> buscarPorId(@PathVariable Long id) {
        PacienteResponse pacienteResponse = pacienteService.buscarPorId(id);
        return ResponseEntity.ok(pacienteResponse);
    }

    @PostMapping
    public ResponseEntity<PacienteResponse> criar(@RequestBody @Valid PacienteRequest request, UriComponentsBuilder uriBuilder) {
        PacienteResponse pacienteResponse = pacienteService.criar(request);

        URI uri = uriBuilder.path("/pacientes/{id}")
                .buildAndExpand(pacienteResponse.id())
                .toUri();

        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteResponse);
    }
}
