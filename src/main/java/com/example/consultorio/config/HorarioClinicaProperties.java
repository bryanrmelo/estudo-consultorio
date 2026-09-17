package com.example.consultorio.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.time.LocalTime;

@ConfigurationProperties(prefix = "clinica.horario")
public record HorarioClinicaProperties(
        LocalTime segASexInicio,
        LocalTime segASexFim,
        LocalTime sabadoInicio,
        LocalTime sabadoFim,
        Duration antecedenciaMinima
) {
}
