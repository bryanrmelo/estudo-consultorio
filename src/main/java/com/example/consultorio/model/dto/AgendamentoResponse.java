package com.example.consultorio.model.dto;

import com.example.consultorio.model.enums.AgendamentoStatus;
import com.example.consultorio.model.Dentista;
import com.example.consultorio.model.Agendamento;
import com.example.consultorio.model.Paciente;

import java.time.LocalDateTime;

public record AgendamentoResponse(
        Long id,
        Paciente paciente,
        Dentista dentista,
        LocalDateTime inicio,
        LocalDateTime fim,
        String observacao,
        AgendamentoStatus status,
        LocalDateTime criadoEm
) {
    public static AgendamentoResponse from(Agendamento agendamento) {
        return new AgendamentoResponse(
                agendamento.getId(),
                agendamento.getPaciente(),
                agendamento.getDentista(),
                agendamento.getInicio(),
                agendamento.getFim(),
                agendamento.getObservacao(),
                agendamento.getStatus(),
                agendamento.getCriadoEm()
        );
    }

}
