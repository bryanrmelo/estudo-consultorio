package com.example.consultorio.common.exception;

import org.springframework.http.HttpStatus;

public class CpfJaCadastradoException extends NegocioException {
    public CpfJaCadastradoException(String cpf) {
        super(HttpStatus.CONFLICT, "CPF já cadastrado",
                "O CPF '%s' já está em uso".formatted(cpf));
    }
}