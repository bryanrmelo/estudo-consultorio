package com.example.consultorio.model.dto.responses;

import com.example.consultorio.model.Paciente;

import java.time.LocalDate;

public record PacienteResponse(
        Long id,
        String nome,
        String email,
        String telefone,
        LocalDate dataNascimento
) {
    public static PacienteResponse from(Paciente paciente) {
        return new PacienteResponse(
                paciente.getId(),
                paciente.getNome(),
                paciente.getEmail(),
                paciente.getTelefone(),
                paciente.getDataNascimento()
        );
    }

}
