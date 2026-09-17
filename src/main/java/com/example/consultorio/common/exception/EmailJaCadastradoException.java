package com.example.consultorio.common.exception;

public class EmailJaCadastradoException extends ConflitoException {
    public EmailJaCadastradoException(String email) {
        super("Email já cadastrado", "O email '%s' já está em uso".formatted(email));
    }
}