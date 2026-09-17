package com.example.consultorio.common.exception;

import org.springframework.http.HttpStatus;

public class ConflitoException extends NegocioException {
    public ConflitoException(String message) {
        super(HttpStatus.CONFLICT, "Conflito", message);
    }

    protected ConflitoException(String title, String message) {
        super(HttpStatus.CONFLICT, title, message);
    }
}
