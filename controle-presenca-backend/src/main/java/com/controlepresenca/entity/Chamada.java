package com.controlepresenca.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidade que representa uma Chamada de Aula
 * Uma chamada é feita para uma turma em uma data específica
 */
@Entity
@Table(name = "chamadas", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"turma_id", "data_chamada"}))
public class Chamada {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "turma_id", nullable = false)
    private Turma turma;
    
    @Column(name = "data_chamada", nullable = false)
    private LocalDate dataChamada;
    
    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;
    
    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;
    
    @OneToMany(mappedBy = "chamada", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChamadaAluno> chamadaAlunos;
    
    // Construtor padrão
    public Chamada() {
        this.dataCriacao = LocalDateTime.now();
    }
    
    // Construtor com parâmetros
    public Chamada(Turma turma, LocalDate dataChamada, String observacoes) {
        this();
        this.turma = turma;
        this.dataChamada = dataChamada;
        this.observacoes = observacoes;
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Turma getTurma() {
        return turma;
    }
    
    public void setTurma(Turma turma) {
        this.turma = turma;
    }
    
    public LocalDate getDataChamada() {
        return dataChamada;
    }
    
    public void setDataChamada(LocalDate dataChamada) {
        this.dataChamada = dataChamada;
    }
    
    public String getObservacoes() {
        return observacoes;
    }
    
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
    
    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
    
    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
    
    public List<ChamadaAluno> getChamadaAlunos() {
        return chamadaAlunos;
    }
    
    public void setChamadaAlunos(List<ChamadaAluno> chamadaAlunos) {
        this.chamadaAlunos = chamadaAlunos;
    }
    
    @PrePersist
    protected void onCreate() {
        if (dataCriacao == null) {
            dataCriacao = LocalDateTime.now();
        }
    }
    
    @Override
    public String toString() {
        return "Chamada{" +
                "id=" + id +
                ", turma=" + (turma != null ? turma.getNome() : null) +
                ", dataChamada=" + dataChamada +
                ", observacoes='" + observacoes + '\'' +
                ", dataCriacao=" + dataCriacao +
                '}';
    }
}

