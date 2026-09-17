package com.example.consultorio.common.exception;

public class IdempotencyConflictException extends ConflitoException {
    public IdempotencyConflictException(String message) {
        super("Idempotência conflitante", message);
    }
}
