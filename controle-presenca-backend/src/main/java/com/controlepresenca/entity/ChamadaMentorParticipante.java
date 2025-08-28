package com.controlepresenca.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidade que representa a participação de um mentor em uma chamada (presente/ausente)
 */
@Entity
@Table(name = "chamada_mentores_participantes", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"chamada_mentor_id", "mentor_id"}))
public class ChamadaMentorParticipante {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chamada_mentor_id", nullable = false)
    private ChamadaMentor chamadaMentor;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_id", nullable = false)
    private Mentor mentor;
    
    @Column(name = "presente", nullable = false)
    private Boolean presente = false;
    
    @Column(name = "data_registro", nullable = false)
    private LocalDateTime dataRegistro;
    
    // Construtor padrão
    public ChamadaMentorParticipante() {
        this.dataRegistro = LocalDateTime.now();
        this.presente = false;
    }
    
    // Construtor com parâmetros
    public ChamadaMentorParticipante(ChamadaMentor chamadaMentor, Mentor mentor, Boolean presente) {
        this();
        this.chamadaMentor = chamadaMentor;
        this.mentor = mentor;
        this.presente = presente != null ? presente : false;
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public ChamadaMentor getChamadaMentor() {
        return chamadaMentor;
    }
    
    public void setChamadaMentor(ChamadaMentor chamadaMentor) {
        this.chamadaMentor = chamadaMentor;
    }
    
    public Mentor getMentor() {
        return mentor;
    }
    
    public void setMentor(Mentor mentor) {
        this.mentor = mentor;
    }
    
    public Boolean getPresente() {
        return presente;
    }
    
    public void setPresente(Boolean presente) {
        this.presente = presente != null ? presente : false;
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
        if (presente == null) {
            presente = false;
        }
    }
    
    @Override
    public String toString() {
        return "ChamadaMentorParticipante{" +
                "id=" + id +
                ", chamadaMentor=" + (chamadaMentor != null ? chamadaMentor.getId() : null) +
                ", mentor=" + (mentor != null ? mentor.getNome() : null) +
                ", presente=" + presente +
                ", dataRegistro=" + dataRegistro +
                '}';
    }
}

