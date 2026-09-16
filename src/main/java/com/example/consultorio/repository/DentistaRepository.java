package com.example.consultorio.repository;

import com.example.consultorio.model.Dentista;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DentistaRepository extends JpaRepository<Dentista, Long> {
    boolean existsByCro(String cro);
}
