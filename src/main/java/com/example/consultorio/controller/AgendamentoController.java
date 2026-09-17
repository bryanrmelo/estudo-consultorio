package com.example.consultorio.controller;

import com.example.consultorio.model.dto.filtros.FiltroAgendamento;
import com.example.consultorio.model.dto.requests.AgendamentoRequest;
import com.example.consultorio.model.dto.responses.AgendamentoResponse;
import com.example.consultorio.model.enums.AgendamentoStatus;
import com.example.consultorio.service.AgendamentoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    public AgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @PostMapping
    public ResponseEntity<AgendamentoService.ResultadoCriacaoAgendamento> criar(
            @RequestBody @Valid AgendamentoRequest request,
            @RequestHeader("Idempotency-Key") String idempotencyKey) {
        AgendamentoService.ResultadoCriacaoAgendamento response = agendamentoService.criar(request, idempotencyKey);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<AgendamentoResponse>> listar(
            @ModelAttribute FiltroAgendamento filtro,
            @RequestParam @Min(0) int pagina,
            @RequestParam @Min(1) @Max(100) int limite)
    {
        Page<AgendamentoResponse> page = agendamentoService.listar(filtro, pagina, limite);
        return ResponseEntity.ok(page);

    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AgendamentoResponse> atualizarStatus(@PathVariable Long id, @RequestParam AgendamentoStatus status) {
        return ResponseEntity.ok(null);
    }
}
