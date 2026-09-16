package com.example.consultorio.common.exception;

import org.springframework.http.HttpStatus;

public class RecursoNaoEncontradoException extends NegocioException {
    public RecursoNaoEncontradoException(String recurso) {
        super(HttpStatus.NOT_FOUND, "Recurso não encontrado",
                "O recurso '%s' não foi encontrado".formatted(recurso));
    }
}
