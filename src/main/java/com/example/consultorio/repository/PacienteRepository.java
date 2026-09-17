package com.example.consultorio.repository;

import com.example.consultorio.model.Paciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);

    Optional<Paciente> findByCpf(String cpf);

    Page<Paciente> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}
