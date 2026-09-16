package com.example.consultorio.repository;

import com.example.consultorio.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);
}
