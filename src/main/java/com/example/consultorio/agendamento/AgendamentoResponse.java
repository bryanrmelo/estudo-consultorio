package com.example.consultorio.agendamento;

import com.example.consultorio.dentista.Dentista;
import com.example.consultorio.paciente.Paciente;

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
