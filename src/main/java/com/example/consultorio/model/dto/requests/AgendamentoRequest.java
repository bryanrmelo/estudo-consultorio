package com.example.consultorio.model.dto.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record AgendamentoRequest(

        @NotNull(message = "ID do paciente é obrigatório")
        Long pacienteId,

        @NotNull(message = "ID do dentista é obrigatório")
        Long dentistaId,

        @NotNull(message = "ID do procedimento é obrigatório")
        Long procedimentoId,

        @NotNull(message = "Data ínicial é obrigatório")
        LocalDateTime inicio,

        @Size(max = 500)
        String observacao
) {
}
