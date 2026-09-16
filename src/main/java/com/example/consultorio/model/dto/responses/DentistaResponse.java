package com.example.consultorio.model.dto.responses;

import com.example.consultorio.model.Dentista;

public record DentistaResponse(
        Long id,
        String nome,
        String cro,
        String especialidade,
        boolean ativo
) {
    public static DentistaResponse from(Dentista d) {
        return new DentistaResponse(
                d.getId(),
                d.getCro(),
                d.getNome(),
                d.getEspecialidade(),
                d.isAtivo()
        );
    }
}
