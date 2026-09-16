package com.example.consultorio.model.dto.requests;

import jakarta.validation.constraints.*;

public record DentistaRequest(
        @NotBlank(message = "CRO é necessário")
        @Size(min = 7, max = 10)
        String cro,

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 120)
        String nome,

        String especialidade
) {
}
