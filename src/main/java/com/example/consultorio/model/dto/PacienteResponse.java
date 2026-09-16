package com.example.consultorio.model.dto;

import com.example.consultorio.model.Paciente;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PacienteResponse(
        Long id,
        String nome,
        String email,
        String telefone,
        LocalDate dataNascimento,
        LocalDateTime criadoEm
) {
    public static PacienteResponse from(Paciente paciente) {
        return new PacienteResponse(
                paciente.getId(),
                paciente.getNome(),
                paciente.getEmail(),
                paciente.getTelefone(),
                paciente.getDataNascimento(),
                paciente.getCriadoEm()
        );
    }

}
