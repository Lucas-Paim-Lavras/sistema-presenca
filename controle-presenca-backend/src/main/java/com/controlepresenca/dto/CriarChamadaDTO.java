package com.controlepresenca.dto;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO para criação de uma nova chamada com os status de presença dos alunos
 */
public class CriarChamadaDTO {
    
    private Long turmaId;
    private LocalDate dataChamada;
    private String observacoes;
    private List<StatusAlunoDTO> alunos;
    
    // Construtor padrão
    public CriarChamadaDTO() {}
    
    // Construtor com parâmetros
    public CriarChamadaDTO(Long turmaId, LocalDate dataChamada, String observacoes, List<StatusAlunoDTO> alunos) {
        this.turmaId = turmaId;
        this.dataChamada = dataChamada;
        this.observacoes = observacoes;
        this.alunos = alunos;
    }
    
    // Getters e Setters
    public Long getTurmaId() {
        return turmaId;
    }
    
    public void setTurmaId(Long turmaId) {
        this.turmaId = turmaId;
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
    
    public List<StatusAlunoDTO> getAlunos() {
        return alunos;
    }
    
    public void setAlunos(List<StatusAlunoDTO> alunos) {
        this.alunos = alunos;
    }
    
    /**
     * DTO interno para representar o status de presença de um aluno
     */
    public static class StatusAlunoDTO {
        private Long alunoId;
        private Boolean presente;
        
        public StatusAlunoDTO() {}
        
        public StatusAlunoDTO(Long alunoId, Boolean presente) {
            this.alunoId = alunoId;
            this.presente = presente;
        }
        
        public Long getAlunoId() {
            return alunoId;
        }
        
        public void setAlunoId(Long alunoId) {
            this.alunoId = alunoId;
        }
        
        public Boolean getPresente() {
            return presente;
        }
        
        public void setPresente(Boolean presente) {
            this.presente = presente;
        }
        
        @Override
        public String toString() {
            return "StatusAlunoDTO{" +
                    "alunoId=" + alunoId +
                    ", presente=" + presente +
                    '}';
        }
    }
    
    @Override
    public String toString() {
        return "CriarChamadaDTO{" +
                "turmaId=" + turmaId +
                ", dataChamada=" + dataChamada +
                ", observacoes='" + observacoes + '\'' +
                ", alunos=" + alunos +
                '}';
    }
}

