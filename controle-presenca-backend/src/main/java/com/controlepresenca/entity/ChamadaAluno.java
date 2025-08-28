package com.controlepresenca.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidade que representa o status de presença/falta de um aluno em uma chamada específica
 */
@Entity
@Table(name = "chamada_alunos", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"chamada_id", "aluno_id"}))
public class ChamadaAluno {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chamada_id", nullable = false)
    private Chamada chamada;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;
    
    @Column(name = "presente", nullable = false)
    private Boolean presente = false;
    
    @Column(name = "data_registro", nullable = false)
    private LocalDateTime dataRegistro;
    
    // Construtor padrão
    public ChamadaAluno() {
        this.dataRegistro = LocalDateTime.now();
    }
    
    // Construtor com parâmetros
    public ChamadaAluno(Chamada chamada, Aluno aluno, Boolean presente) {
        this();
        this.chamada = chamada;
        this.aluno = aluno;
        this.presente = presente;
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Chamada getChamada() {
        return chamada;
    }
    
    public void setChamada(Chamada chamada) {
        this.chamada = chamada;
    }
    
    public Aluno getAluno() {
        return aluno;
    }
    
    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }
    
    public Boolean getPresente() {
        return presente;
    }
    
    public void setPresente(Boolean presente) {
        this.presente = presente;
    }
    
    public LocalDateTime getDataRegistro() {
        return dataRegistro;
    }
    
    public void setDataRegistro(LocalDateTime dataRegistro) {
        this.dataRegistro = dataRegistro;
    }
    
    @PrePersist
    protected void onCreate() {
        if (dataRegistro == null) {
            dataRegistro = LocalDateTime.now();
        }
    }
    
    @Override
    public String toString() {
        return "ChamadaAluno{" +
                "id=" + id +
                ", chamada=" + (chamada != null ? chamada.getId() : null) +
                ", aluno=" + (aluno != null ? aluno.getNome() : null) +
                ", presente=" + presente +
                ", dataRegistro=" + dataRegistro +
                '}';
    }
}

