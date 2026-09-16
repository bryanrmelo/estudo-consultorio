package com.example.consultorio.controller;

import com.example.consultorio.model.dto.responses.DentistaResponse;
import com.example.consultorio.service.DentistaService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dentistas")
public class DentistaController {

    private final DentistaService dentistaService;

    public DentistaController(DentistaService dentistaService) { this.dentistaService = dentistaService; }

    @GetMapping
    public ResponseEntity<Page<DentistaResponse>> listar(
            @RequestParam(defaultValue = "1") int pagina,
            @RequestParam(defaultValue = "20") int limite) {
        Page<DentistaResponse> list = dentistaService.listar(pagina, limite);
        return ResponseEntity.ok(list);
    }
}
