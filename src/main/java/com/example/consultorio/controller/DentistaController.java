package com.example.consultorio.controller;

import com.example.consultorio.model.dto.requests.AtualizarDentistaStatusRequest;
import com.example.consultorio.model.dto.requests.DentistaRequest;
import com.example.consultorio.model.dto.responses.DentistaResponse;
import com.example.consultorio.service.DentistaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dentistas")
public class DentistaController {

    private final DentistaService dentistaService;

    public DentistaController(DentistaService dentistaService) {
        this.dentistaService = dentistaService;
    }

    @PostMapping
    public ResponseEntity<DentistaResponse> criar(@RequestBody @Valid DentistaRequest request) {
        DentistaResponse dentistaResponse = dentistaService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dentistaResponse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DentistaResponse> atualizarStatus(@PathVariable Long id, @RequestBody @Valid AtualizarDentistaStatusRequest request) {
        DentistaResponse dentistaResponse = dentistaService.atualizarStatus(id, request);
        return ResponseEntity.ok(dentistaResponse);
    }
}
