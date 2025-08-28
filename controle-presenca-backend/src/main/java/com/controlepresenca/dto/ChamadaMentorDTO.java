package com.controlepresenca.dto;

import com.controlepresenca.entity.ChamadaMentor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para transferência de dados de ChamadaMentor
 */
public class ChamadaMentorDTO {
    
    private Long id;
    private LocalDate dataChamada;
    private String observacoes;
    private LocalDateTime dataCriacao;
    private List<ChamadaMentorParticipanteDTO> participantes;
    private Integer totalMentores;
    private Integer totalPresentes;
    private Integer totalAusentes;
    
    // Construtor padrão
    public ChamadaMentorDTO() {}
    
    // Construtor com parâmetros principais
    public ChamadaMentorDTO(Long id, LocalDate dataChamada, String observacoes, LocalDateTime dataCriacao) {
        this.id = id;
        this.dataChamada = dataChamada;
        this.observacoes = observacoes;
        this.dataCriacao = dataCriacao;
    }
    
    // Construtor a partir da entidade
    public ChamadaMentorDTO(ChamadaMentor chamadaMentor) {
        this.id = chamadaMentor.getId();
        this.dataChamada = chamadaMentor.getDataChamada();
        this.observacoes = chamadaMentor.getObservacoes();
        this.dataCriacao = chamadaMentor.getDataCriacao();
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
    
    public List<ChamadaMentorParticipanteDTO> getParticipantes() {
        return participantes;
    }
    
    public void setParticipantes(List<ChamadaMentorParticipanteDTO> participantes) {
        this.participantes = participantes;
    }
    
    public Integer getTotalMentores() {
        return totalMentores;
    }
    
    public void setTotalMentores(Integer totalMentores) {
        this.totalMentores = totalMentores;
    }
    
    public Integer getTotalPresentes() {
        return totalPresentes;
    }
    
    public void setTotalPresentes(Integer totalPresentes) {
        this.totalPresentes = totalPresentes;
    }
    
    public Integer getTotalAusentes() {
        return totalAusentes;
    }
    
    public void setTotalAusentes(Integer totalAusentes) {
        this.totalAusentes = totalAusentes;
    }
    
    @Override
    public String toString() {
        return "ChamadaMentorDTO{" +
                "id=" + id +
                ", dataChamada=" + dataChamada +
                ", observacoes='" + observacoes + '\'' +
                ", dataCriacao=" + dataCriacao +
                ", totalMentores=" + totalMentores +
                ", totalPresentes=" + totalPresentes +
                ", totalAusentes=" + totalAusentes +
                '}';
    }
}

