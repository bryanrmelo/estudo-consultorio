package com.example.consultorio.model.dto.requests;

import jakarta.validation.constraints.NotNull;

public record AtualizarDentistaStatusRequest(
        @NotNull Boolean ativo
) {}
