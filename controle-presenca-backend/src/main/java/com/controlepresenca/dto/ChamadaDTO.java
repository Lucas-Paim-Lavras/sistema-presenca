package com.controlepresenca.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para transferência de dados de Chamada
 */
public class ChamadaDTO {
    
    private Long id;
    private Long turmaId;
    private String turmaNome;
    private String turmaCodigo;
    private LocalDate dataChamada;
    private String observacoes;
    private LocalDateTime dataCriacao;
    private List<ChamadaAlunoDTO> alunos;
    private Integer totalAlunos;
    private Integer totalPresentes;
    private Integer totalFaltas;
    
    // Construtor padrão
    public ChamadaDTO() {}
    
    // Construtor com parâmetros principais
    public ChamadaDTO(Long id, Long turmaId, String turmaNome, String turmaCodigo, 
                      LocalDate dataChamada, String observacoes, LocalDateTime dataCriacao) {
        this.id = id;
        this.turmaId = turmaId;
        this.turmaNome = turmaNome;
        this.turmaCodigo = turmaCodigo;
        this.dataChamada = dataChamada;
        this.observacoes = observacoes;
        this.dataCriacao = dataCriacao;
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getTurmaId() {
        return turmaId;
    }
    
    public void setTurmaId(Long turmaId) {
        this.turmaId = turmaId;
    }
    
    public String getTurmaNome() {
        return turmaNome;
    }
    
    public void setTurmaNome(String turmaNome) {
        this.turmaNome = turmaNome;
    }
    
    public String getTurmaCodigo() {
        return turmaCodigo;
    }
    
    public void setTurmaCodigo(String turmaCodigo) {
        this.turmaCodigo = turmaCodigo;
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
    
    public List<ChamadaAlunoDTO> getAlunos() {
        return alunos;
    }
    
    public void setAlunos(List<ChamadaAlunoDTO> alunos) {
        this.alunos = alunos;
    }
    
    public Integer getTotalAlunos() {
        return totalAlunos;
    }
    
    public void setTotalAlunos(Integer totalAlunos) {
        this.totalAlunos = totalAlunos;
    }
    
    public Integer getTotalPresentes() {
        return totalPresentes;
    }
    
    public void setTotalPresentes(Integer totalPresentes) {
        this.totalPresentes = totalPresentes;
    }
    
    public Integer getTotalFaltas() {
        return totalFaltas;
    }
    
    public void setTotalFaltas(Integer totalFaltas) {
        this.totalFaltas = totalFaltas;
    }
    
    @Override
    public String toString() {
        return "ChamadaDTO{" +
                "id=" + id +
                ", turmaId=" + turmaId +
                ", turmaNome='" + turmaNome + '\'' +
                ", turmaCodigo='" + turmaCodigo + '\'' +
                ", dataChamada=" + dataChamada +
                ", observacoes='" + observacoes + '\'' +
                ", dataCriacao=" + dataCriacao +
                ", totalAlunos=" + totalAlunos +
                ", totalPresentes=" + totalPresentes +
                ", totalFaltas=" + totalFaltas +
                '}';
    }
}

