package com.example.consultorio.model.dto.filtros;

import com.example.consultorio.model.Agendamento;
import com.example.consultorio.model.enums.AgendamentoStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record FiltroAgendamento(Long dentistaId, Long pacienteId, LocalDate data, AgendamentoStatus status) {
    public Specification<Agendamento> toSpecification() {
        Specification<Agendamento> spec = Specification.unrestricted();

        if (dentistaId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("dentista").get("id"), dentistaId));
        }
        if (pacienteId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("paciente").get("id"), pacienteId));
        }
        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }
        if (data != null) {
            LocalDateTime inicioDoDia = data.atStartOfDay();
            spec = spec.and((root, query, cb) -> cb.between(root.get("inicio"), inicioDoDia, inicioDoDia.plusDays(1).minusNanos(1)));
        }

        return spec;
    }
}