package com.example.consultorio.common.exception;

public class CpfJaCadastradoException extends ConflitoException {
    public CpfJaCadastradoException(String cpf) {
        super("CPF já cadastrado", "O CPF '%s' já está em uso".formatted(cpf));
    }
}