package com.controlepresenca.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) para Aluno
 * 
 * Usado para transferir dados de aluno entre o frontend e backend
 */
public class AlunoDTO {

    private Long id;

    @NotBlank(message = "Nome do aluno é obrigatório")
    @Size(max = 150, message = "Nome do aluno deve ter no máximo 150 caracteres")
    private String nome;

    @NotBlank(message = "Matrícula é obrigatória")
    @Size(max = 20, message = "Matrícula deve ter no máximo 20 caracteres")
    private String matricula;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ter um formato válido")
    @Size(max = 100, message = "Email deve ter no máximo 100 caracteres")
    private String email;

    @NotNull(message = "ID da turma é obrigatório")
    private Long turmaId;

    private String turmaNome;
    private String turmaCodigo;
    private LocalDateTime dataCadastro;
    private Boolean ativo;
    private Integer totalPresencas;

    // Construtor padrão
    public AlunoDTO() {}

    // Construtor com parâmetros principais
    public AlunoDTO(String nome, String matricula, String email, Long turmaId) {
        this.nome = nome;
        this.matricula = matricula;
        this.email = email;
        this.turmaId = turmaId;
        this.ativo = true;
    }

    // Construtor completo
    public AlunoDTO(Long id, String nome, String matricula, String email, Long turmaId,
                   String turmaNome, String turmaCodigo, LocalDateTime dataCadastro, 
                   Boolean ativo, Integer totalPresencas) {
        this.id = id;
        this.nome = nome;
        this.matricula = matricula;
        this.email = email;
        this.turmaId = turmaId;
        this.turmaNome = turmaNome;
        this.turmaCodigo = turmaCodigo;
        this.dataCadastro = dataCadastro;
        this.ativo = ativo;
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

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        return "AlunoDTO{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", matricula='" + matricula + '\'' +
                ", email='" + email + '\'' +
                ", turmaId=" + turmaId +
                ", turmaNome='" + turmaNome + '\'' +
                ", turmaCodigo='" + turmaCodigo + '\'' +
                ", dataCadastro=" + dataCadastro +
                ", ativo=" + ativo +
                ", totalPresencas=" + totalPresencas +
                '}';
    }
}

