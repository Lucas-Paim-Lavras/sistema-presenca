package com.controlepresenca.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

/**
 * Entidade que representa um registro de Presença no sistema
 * 
 * Um registro de presença está associado a um aluno e uma turma
 */
@Entity
@Table(name = "presencas", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"aluno_id", "data_presenca"}))
public class Presenca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_presenca", nullable = false)
    @NotNull(message = "Data da presença é obrigatória")
    private LocalDate dataPresenca;

    @Column(name = "hora_presenca", nullable = false)
    @NotNull(message = "Hora da presença é obrigatória")
    private LocalTime horaPresenca;

    @Column(name = "data_hora_registro")
    private LocalDateTime dataHoraRegistro;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    // Relacionamento Many-to-One com Aluno
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    @NotNull(message = "Aluno é obrigatório")
    @JsonBackReference("aluno-presencas")
    private Aluno aluno;

    // Relacionamento Many-to-One com Turma
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "turma_id", nullable = false)
    @NotNull(message = "Turma é obrigatória")
    @JsonBackReference("turma-presencas")
    private Turma turma;

    // Construtor padrão
    public Presenca() {
        this.dataHoraRegistro = LocalDateTime.now();
        this.dataPresenca = LocalDate.now();
        this.horaPresenca = LocalTime.now();
    }

    // Construtor com parâmetros
    public Presenca(Aluno aluno, Turma turma) {
        this();
        this.aluno = aluno;
        this.turma = turma;
    }

    // Construtor completo
    public Presenca(Aluno aluno, Turma turma, LocalDate dataPresenca, LocalTime horaPresenca, String observacoes) {
        this();
        this.aluno = aluno;
        this.turma = turma;
        this.dataPresenca = dataPresenca;
        this.horaPresenca = horaPresenca;
        this.observacoes = observacoes;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataPresenca() {
        return dataPresenca;
    }

    public void setDataPresenca(LocalDate dataPresenca) {
        this.dataPresenca = dataPresenca;
    }

    public LocalTime getHoraPresenca() {
        return horaPresenca;
    }

    public void setHoraPresenca(LocalTime horaPresenca) {
        this.horaPresenca = horaPresenca;
    }

    public LocalDateTime getDataHoraRegistro() {
        return dataHoraRegistro;
    }

    public void setDataHoraRegistro(LocalDateTime dataHoraRegistro) {
        this.dataHoraRegistro = dataHoraRegistro;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    @PrePersist
    protected void onCreate() {
        dataHoraRegistro = LocalDateTime.now();
        if (dataPresenca == null) {
            dataPresenca = LocalDate.now();
        }
        if (horaPresenca == null) {
            horaPresenca = LocalTime.now();
        }
    }

    @Override
    public String toString() {
        return "Presenca{" +
                "id=" + id +
                ", dataPresenca=" + dataPresenca +
                ", horaPresenca=" + horaPresenca +
                ", dataHoraRegistro=" + dataHoraRegistro +
                ", observacoes='" + observacoes + '\'' +
                '}';
    }
}

