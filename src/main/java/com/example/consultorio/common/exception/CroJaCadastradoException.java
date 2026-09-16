package com.example.consultorio.common.exception;

import org.springframework.http.HttpStatus;

public class CroJaCadastradoException extends NegocioException {
    public CroJaCadastradoException(String cro) {
        super(HttpStatus.CONFLICT, "CRO já cadastrado",
                "O CRO '%s' já está em uso".formatted(cro));
    }
}