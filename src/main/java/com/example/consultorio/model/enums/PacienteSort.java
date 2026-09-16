package com.example.consultorio.model.enums;

public enum PacienteSort {
    cpf("cpf"),
    nome("nome");

    private final String property;

    PacienteSort(String property) {
        this.property = property;
    }

    public String getProperty() {
        return property;
    }
}