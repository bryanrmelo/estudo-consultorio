package com.example.consultorio.common.exception;

import org.springframework.http.HttpStatus;

public class EmailJaCadastradoException extends NegocioException {
    public EmailJaCadastradoException(String email) {
        super(HttpStatus.CONFLICT, "Email já cadastrado",
                "O email '%s' já está em uso".formatted(email));
    }
}