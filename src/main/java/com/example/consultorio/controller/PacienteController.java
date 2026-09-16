package com.example.consultorio.controller;

import com.example.consultorio.model.dto.requests.PacienteRequest;
import com.example.consultorio.model.dto.responses.PacienteResponse;
import com.example.consultorio.model.enums.PacienteSort;
import com.example.consultorio.service.PacienteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
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
    public ResponseEntity<Page<PacienteResponse>> listar(
            @RequestParam(defaultValue = "1") int pagina,
            @RequestParam(defaultValue = "20") @Max(100) int limite,
            @RequestParam(defaultValue = "cpf") PacienteSort sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direcao) {
        Page<PacienteResponse> page = pacienteService.listar(pagina, limite, sortBy, direcao);
        return ResponseEntity.ok(page);
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
