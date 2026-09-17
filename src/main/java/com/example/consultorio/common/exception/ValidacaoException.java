package com.example.consultorio.common.exception;

import org.springframework.http.HttpStatus;

public class ValidacaoException extends NegocioException {
    public ValidacaoException(String message) {
        super(HttpStatus.BAD_REQUEST, "Erro de validação", message);
    }
}
