package com.controlepresenca.dto;

import java.time.LocalDateTime;

/**
 * DTO para transferência de dados de ChamadaAluno
 */
public class ChamadaAlunoDTO {
    
    private Long id;
    private Long chamadaId;
    private Long alunoId;
    private String alunoNome;
    private String alunoMatricula;
    private String alunoEmail;
    private Boolean presente;
    private LocalDateTime dataRegistro;
    
    // Construtor padrão
    public ChamadaAlunoDTO() {}
    
    // Construtor com parâmetros principais
    public ChamadaAlunoDTO(Long id, Long chamadaId, Long alunoId, String alunoNome, 
                           String alunoMatricula, String alunoEmail, Boolean presente, 
                           LocalDateTime dataRegistro) {
        this.id = id;
        this.chamadaId = chamadaId;
        this.alunoId = alunoId;
        this.alunoNome = alunoNome;
        this.alunoMatricula = alunoMatricula;
        this.alunoEmail = alunoEmail;
        this.presente = presente;
        this.dataRegistro = dataRegistro;
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getChamadaId() {
        return chamadaId;
    }
    
    public void setChamadaId(Long chamadaId) {
        this.chamadaId = chamadaId;
    }
    
    public Long getAlunoId() {
        return alunoId;
    }
    
    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }
    
    public String getAlunoNome() {
        return alunoNome;
    }
    
    public void setAlunoNome(String alunoNome) {
        this.alunoNome = alunoNome;
    }
    
    public String getAlunoMatricula() {
        return alunoMatricula;
    }
    
    public void setAlunoMatricula(String alunoMatricula) {
        this.alunoMatricula = alunoMatricula;
    }
    
    public String getAlunoEmail() {
        return alunoEmail;
    }
    
    public void setAlunoEmail(String alunoEmail) {
        this.alunoEmail = alunoEmail;
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
        return "ChamadaAlunoDTO{" +
                "id=" + id +
                ", chamadaId=" + chamadaId +
                ", alunoId=" + alunoId +
                ", alunoNome='" + alunoNome + '\'' +
                ", alunoMatricula='" + alunoMatricula + '\'' +
                ", alunoEmail='" + alunoEmail + '\'' +
                ", presente=" + presente +
                ", dataRegistro=" + dataRegistro +
                '}';
    }
}

