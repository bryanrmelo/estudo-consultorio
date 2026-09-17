package com.example.consultorio.repository;

import com.example.consultorio.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.Optional;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long>, JpaSpecificationExecutor<Agendamento> {
    Optional<Agendamento> findByIdempotencyKeyAndCriadoEmBetween(
            String idempotencyKey, LocalDateTime inicioDoDia, LocalDateTime fimDoDia);}
