package com.example.consultorio.model.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record PacienteRequest(

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 120)
    String nome,

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    @Size(max = 150)
    String email,

    @Size(max = 20)
    String telefone,

    @NotNull(message = "Data de nascimento é obrigatória")
    @Past(message = "Data de nascimento deve estar no passado")
    LocalDate dataNascimento
) {}
