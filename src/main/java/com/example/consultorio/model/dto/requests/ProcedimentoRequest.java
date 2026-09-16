package com.example.consultorio.model.dto.requests;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProcedimentoRequest(

        @NotBlank(message = "nome é necessário")
        @Size(max = 120)
        String nome,

        @NotNull(message = "valor é necessário")
        @PositiveOrZero
        @Digits(integer = 8, fraction = 2)
        BigDecimal valor,

        @NotNull(message = "duracaoMinutos é necessário")
        @Positive
        Integer duracaoMinutos
) {


}
