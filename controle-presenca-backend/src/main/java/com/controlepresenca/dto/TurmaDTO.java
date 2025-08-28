package com.controlepresenca.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) para Turma
 * 
 * Usado para transferir dados de turma entre o frontend e backend
 */
public class TurmaDTO {

    private Long id;

    @NotBlank(message = "Nome da turma é obrigatório")
    @Size(max = 100, message = "Nome da turma deve ter no máximo 100 caracteres")
    private String nome;

    @NotBlank(message = "Código da turma é obrigatório")
    @Size(max = 20, message = "Código da turma deve ter no máximo 20 caracteres")
    private String codigo;

    private String descricao;
    private LocalDateTime dataCriacao;
    private Boolean ativa;
    private Integer totalAlunos;
    private Integer totalPresencas;

    // Construtor padrão
    public TurmaDTO() {}

    // Construtor com parâmetros principais
    public TurmaDTO(String nome, String codigo, String descricao) {
        this.nome = nome;
        this.codigo = codigo;
        this.descricao = descricao;
        this.ativa = true;
    }

    // Construtor completo
    public TurmaDTO(Long id, String nome, String codigo, String descricao, 
                   LocalDateTime dataCriacao, Boolean ativa, Integer totalAlunos, Integer totalPresencas) {
        this.id = id;
        this.nome = nome;
        this.codigo = codigo;
        this.descricao = descricao;
        this.dataCriacao = dataCriacao;
        this.ativa = ativa;
        this.totalAlunos = totalAlunos;
        this.totalPresencas = totalPresencas;
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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }

    public Integer getTotalAlunos() {
        return totalAlunos;
    }

    public void setTotalAlunos(Integer totalAlunos) {
        this.totalAlunos = totalAlunos;
    }

    public Integer getTotalPresencas() {
        return totalPresencas;
    }

    public void setTotalPresencas(Integer totalPresencas) {
        this.totalPresencas = totalPresencas;
    }

    @Override
    public String toString() {
        return "TurmaDTO{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", codigo='" + codigo + '\'' +
                ", descricao='" + descricao + '\'' +
                ", dataCriacao=" + dataCriacao +
                ", ativa=" + ativa +
                ", totalAlunos=" + totalAlunos +
                ", totalPresencas=" + totalPresencas +
                '}';
    }
}

