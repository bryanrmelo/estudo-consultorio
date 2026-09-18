package com.example.consultorio.repository;

import com.example.consultorio.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long>, JpaSpecificationExecutor<Agendamento> {
    Optional<Agendamento> findByIdempotencyKeyAndCriadoEmBetween(String idempotencyKey, LocalDateTime inicioDoDia, LocalDateTime fimDoDia);

    Optional<Agendamento> findByDentistaAndInicio(Long dentista, LocalDateTime inicio);

    @Query("SELECT a FROM Agendamento a " +
            "WHERE a.dentista.id = :dentistaId " +
            "  AND a.inicio < :novoFim " +      // equivalente a existenteInicio.isBefore(novoFim)
            "  AND a.fim > :novoInicio" +
            "  AND a.status <> 'CANCELADO'")
    List<Agendamento> findConflitosDeHorario(
            @Param("dentistaId") Long dentistaId,
            @Param("novoInicio") LocalDateTime novoInicio,
            @Param("novoFim") LocalDateTime novoFim
    );
}
