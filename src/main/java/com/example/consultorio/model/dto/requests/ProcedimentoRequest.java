package com.example.consultorio.model.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProcedimentoRequest(

        @NotBlank(message = "nome é necessário")
        @Size(max = 120)
        String nome,

        @NotNull(message = "valor é necessário")
        BigDecimal valor,

        @NotNull(message = "duracaoMinutos é necessário")
        int duracaoMinutos
) {


}
