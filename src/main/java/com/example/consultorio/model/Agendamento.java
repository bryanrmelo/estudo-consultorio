package com.example.consultorio.model;

import com.example.consultorio.model.enums.AgendamentoStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

import java.time.LocalDateTime;

@Entity
@Table(name = "agendamento")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dentista_id", nullable = false)
    private Dentista dentista;

    @Column(nullable = false)
    private LocalDateTime inicio;

    @Column(nullable = false)
    private LocalDateTime fim;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private AgendamentoStatus status;

    @Column(length = 500)
    private String observacao;

    @Generated(event = EventType.INSERT)
    @Column(name = "criado_em", nullable = false, insertable = false, updatable = false)
    private LocalDateTime criadoEm;

    protected Agendamento() {}

    public Agendamento(Paciente paciente, Dentista dentista, LocalDateTime inicio, LocalDateTime fim, AgendamentoStatus status, String observacao) {
        this.paciente = paciente;
        this.dentista = dentista;
        this.inicio = inicio;
        this.fim = fim;
        this.status = status;
        this.observacao = observacao;
    }

    public Long getId() {
        return id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public Dentista getDentista() {
        return dentista;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public LocalDateTime getFim() {
        return fim;
    }

    public void setFim(LocalDateTime fim) {
        this.fim = fim;
    }

    public AgendamentoStatus getStatus() {
        return status;
    }

    public void setStatus(AgendamentoStatus status) {
        this.status = status;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }
}
