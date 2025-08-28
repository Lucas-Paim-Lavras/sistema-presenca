package com.controlepresenca.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidade que representa uma chamada de mentores em uma data específica
 */
@Entity
@Table(name = "chamadas_mentores", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"data_chamada"}))
public class ChamadaMentor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "data_chamada", nullable = false)
    private LocalDate dataChamada;
    
    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;
    
    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;
    
    @OneToMany(mappedBy = "chamadaMentor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChamadaMentorParticipante> participantes;
    
    // Construtor padrão
    public ChamadaMentor() {
        this.dataCriacao = LocalDateTime.now();
    }
    
    // Construtor com parâmetros
    public ChamadaMentor(LocalDate dataChamada, String observacoes) {
        this();
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
    
    public List<ChamadaMentorParticipante> getParticipantes() {
        return participantes;
    }
    
    public void setParticipantes(List<ChamadaMentorParticipante> participantes) {
        this.participantes = participantes;
    }
    
    @PrePersist
    protected void onCreate() {
        if (dataCriacao == null) {
            dataCriacao = LocalDateTime.now();
        }
    }
    
    @Override
    public String toString() {
        return "ChamadaMentor{" +
                "id=" + id +
                ", dataChamada=" + dataChamada +
                ", observacoes='" + observacoes + '\'' +
                ", dataCriacao=" + dataCriacao +
                '}';
    }
}

