package com.example.consultorio.common.exception;

public class CroJaCadastradoException extends ConflitoException {
    public CroJaCadastradoException(String cro) {
        super("CRO já cadastrado", "O CRO '%s' já está em uso".formatted(cro));
    }
}