package com.controlepresenca.dto;

import com.controlepresenca.entity.ChamadaMentorParticipante;
import java.time.LocalDateTime;

/**
 * DTO para transferência de dados de ChamadaMentorParticipante
 */
public class ChamadaMentorParticipanteDTO {
    
    private Long id;
    private Long chamadaMentorId;
    private Long mentorId;
    private String mentorNome;
    private String mentorEmail;
    private String mentorTipo;
    private String mentorTipoDescricao;
    private Boolean presente;
    private LocalDateTime dataRegistro;
    
    // Construtor padrão
    public ChamadaMentorParticipanteDTO() {}
    
    // Construtor com parâmetros principais
    public ChamadaMentorParticipanteDTO(Long id, Long chamadaMentorId, Long mentorId, 
                                       String mentorNome, String mentorEmail, String mentorTipo,
                                       String mentorTipoDescricao, Boolean presente, LocalDateTime dataRegistro) {
        this.id = id;
        this.chamadaMentorId = chamadaMentorId;
        this.mentorId = mentorId;
        this.mentorNome = mentorNome;
        this.mentorEmail = mentorEmail;
        this.mentorTipo = mentorTipo;
        this.mentorTipoDescricao = mentorTipoDescricao;
        this.presente = presente;
        this.dataRegistro = dataRegistro;
    }
    
    // Construtor a partir da entidade
    public ChamadaMentorParticipanteDTO(ChamadaMentorParticipante participante) {
        this.id = participante.getId();
        this.chamadaMentorId = participante.getChamadaMentor().getId();
        this.mentorId = participante.getMentor().getId();
        this.mentorNome = participante.getMentor().getNome();
        this.mentorEmail = participante.getMentor().getEmail();
        this.mentorTipo = participante.getMentor().getTipoMentor().name();
        this.mentorTipoDescricao = participante.getMentor().getTipoMentor().getDescricao();
        this.presente = participante.getPresente();
        this.dataRegistro = participante.getDataRegistro();
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getChamadaMentorId() {
        return chamadaMentorId;
    }
    
    public void setChamadaMentorId(Long chamadaMentorId) {
        this.chamadaMentorId = chamadaMentorId;
    }
    
    public Long getMentorId() {
        return mentorId;
    }
    
    public void setMentorId(Long mentorId) {
        this.mentorId = mentorId;
    }
    
    public String getMentorNome() {
        return mentorNome;
    }
    
    public void setMentorNome(String mentorNome) {
        this.mentorNome = mentorNome;
    }
    
    public String getMentorEmail() {
        return mentorEmail;
    }
    
    public void setMentorEmail(String mentorEmail) {
        this.mentorEmail = mentorEmail;
    }
    
    public String getMentorTipo() {
        return mentorTipo;
    }
    
    public void setMentorTipo(String mentorTipo) {
        this.mentorTipo = mentorTipo;
    }
    
    public String getMentorTipoDescricao() {
        return mentorTipoDescricao;
    }
    
    public void setMentorTipoDescricao(String mentorTipoDescricao) {
        this.mentorTipoDescricao = mentorTipoDescricao;
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
    
    @Override
    public String toString() {
        return "ChamadaMentorParticipanteDTO{" +
                "id=" + id +
                ", chamadaMentorId=" + chamadaMentorId +
                ", mentorId=" + mentorId +
                ", mentorNome='" + mentorNome + '\'' +
                ", mentorEmail='" + mentorEmail + '\'' +
                ", mentorTipo='" + mentorTipo + '\'' +
                ", mentorTipoDescricao='" + mentorTipoDescricao + '\'' +
                ", presente=" + presente +
                ", dataRegistro=" + dataRegistro +
                '}';
    }
}

