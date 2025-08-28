package com.controlepresenca.dto;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO para criação/atualização de chamada de mentor
 */
public class CriarChamadaMentorDTO {
    
    private LocalDate dataChamada;
    private String observacoes;
    private List<ParticipanteChamadaMentorDTO> participantes;
    
    // Construtor padrão
    public CriarChamadaMentorDTO() {}
    
    // Construtor com parâmetros
    public CriarChamadaMentorDTO(LocalDate dataChamada, String observacoes, List<ParticipanteChamadaMentorDTO> participantes) {
        this.dataChamada = dataChamada;
        this.observacoes = observacoes;
        this.participantes = participantes;
    }
    
    // Getters e Setters
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
    
    public List<ParticipanteChamadaMentorDTO> getParticipantes() {
        return participantes;
    }
    
    public void setParticipantes(List<ParticipanteChamadaMentorDTO> participantes) {
        this.participantes = participantes;
    }
    
    /**
     * DTO interno para representar a participação de um mentor na chamada
     */
    public static class ParticipanteChamadaMentorDTO {
        private Long mentorId;
        private Boolean presente;
        
        // Construtor padrão
        public ParticipanteChamadaMentorDTO() {}
        
        // Construtor com parâmetros
        public ParticipanteChamadaMentorDTO(Long mentorId, Boolean presente) {
            this.mentorId = mentorId;
            this.presente = presente != null ? presente : false;
        }
        
        // Getters e Setters
        public Long getMentorId() {
            return mentorId;
        }
        
        public void setMentorId(Long mentorId) {
            this.mentorId = mentorId;
        }
        
        public Boolean getPresente() {
            return presente;
        }
        
        public void setPresente(Boolean presente) {
            this.presente = presente != null ? presente : false;
        }
        
        @Override
        public String toString() {
            return "ParticipanteChamadaMentorDTO{" +
                    "mentorId=" + mentorId +
                    ", presente=" + presente +
                    '}';
        }
    }
    
    @Override
    public String toString() {
        return "CriarChamadaMentorDTO{" +
                "dataChamada=" + dataChamada +
                ", observacoes='" + observacoes + '\'' +
                ", participantes=" + participantes +
                '}';
    }
}

