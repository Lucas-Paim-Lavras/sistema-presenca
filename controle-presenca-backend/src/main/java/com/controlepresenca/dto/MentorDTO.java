package com.controlepresenca.dto;

import com.controlepresenca.entity.Mentor;
import java.time.LocalDateTime;

/**
 * DTO para transferência de dados de Mentor
 */
public class MentorDTO {
    
    private Long id;
    private String nome;
    private String email;
    private String tipoMentor;
    private String tipoMentorDescricao;
    private LocalDateTime dataCadastro;
    private Boolean ativo;
    private Integer totalPresencas;
    
    // Construtor padrão
    public MentorDTO() {}
    
    // Construtor com parâmetros principais
    public MentorDTO(Long id, String nome, String email, String tipoMentor, 
                     String tipoMentorDescricao, LocalDateTime dataCadastro, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.tipoMentor = tipoMentor;
        this.tipoMentorDescricao = tipoMentorDescricao;
        this.dataCadastro = dataCadastro;
        this.ativo = ativo;
    }
    
    // Construtor a partir da entidade
    public MentorDTO(Mentor mentor) {
        this.id = mentor.getId();
        this.nome = mentor.getNome();
        this.email = mentor.getEmail();
        this.tipoMentor = mentor.getTipoMentor().name();
        this.tipoMentorDescricao = mentor.getTipoMentor().getDescricao();
        this.dataCadastro = mentor.getDataCadastro();
        this.ativo = mentor.getAtivo();
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getTipoMentor() {
        return tipoMentor;
    }
    
    public void setTipoMentor(String tipoMentor) {
        this.tipoMentor = tipoMentor;
    }
    
    public String getTipoMentorDescricao() {
        return tipoMentorDescricao;
    }
    
    public void setTipoMentorDescricao(String tipoMentorDescricao) {
        this.tipoMentorDescricao = tipoMentorDescricao;
    }
    
    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }
    
    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
    
    public Boolean getAtivo() {
        return ativo;
    }
    
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
    
    public Integer getTotalPresencas() {
        return totalPresencas;
    }
    
    public void setTotalPresencas(Integer totalPresencas) {
        this.totalPresencas = totalPresencas;
    }
    
    @Override
    public String toString() {
        return "MentorDTO{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", tipoMentor='" + tipoMentor + '\'' +
                ", tipoMentorDescricao='" + tipoMentorDescricao + '\'' +
                ", dataCadastro=" + dataCadastro +
                ", ativo=" + ativo +
                ", totalPresencas=" + totalPresencas +
                '}';
    }
}

