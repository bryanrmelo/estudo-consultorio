package com.example.consultorio.model.dto.responses;

import com.example.consultorio.model.Procedimento;

import java.math.BigDecimal;

public record ProcedimentoResponse(
        Long id,
        String nome,
        BigDecimal valor,
        int duracaoMinutos
) {

    public static ProcedimentoResponse from(Procedimento p) {
        return new ProcedimentoResponse(
                p.getId(),
                p.getNome(),
                p.getValor(),
                p.getDuracaoMinutos()
        );
    }
}
