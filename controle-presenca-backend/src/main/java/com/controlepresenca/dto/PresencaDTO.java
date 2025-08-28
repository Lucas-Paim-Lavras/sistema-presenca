package com.controlepresenca.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) para Presença
 * 
 * Usado para transferir dados de presença entre o frontend e backend
 */
public class PresencaDTO {

    private Long id;

    @NotNull(message = "ID do aluno é obrigatório")
    private Long alunoId;

    @NotNull(message = "ID da turma é obrigatório")
    private Long turmaId;

    private String alunoNome;
    private String alunoMatricula;
    private String turmaNome;
    private String turmaCodigo;

    @NotNull(message = "Data da presença é obrigatória")
    private LocalDate dataPresenca;

    @NotNull(message = "Hora da presença é obrigatória")
    private LocalTime horaPresenca;

    private LocalDateTime dataHoraRegistro;
    private String observacoes;

    // Construtor padrão
    public PresencaDTO() {
        this.dataPresenca = LocalDate.now();
        this.horaPresenca = LocalTime.now();
    }

    // Construtor com parâmetros principais
    public PresencaDTO(Long alunoId, Long turmaId) {
        this();
        this.alunoId = alunoId;
        this.turmaId = turmaId;
    }

    // Construtor completo
    public PresencaDTO(Long id, Long alunoId, Long turmaId, String alunoNome, String alunoMatricula,
                      String turmaNome, String turmaCodigo, LocalDate dataPresenca, LocalTime horaPresenca,
                      LocalDateTime dataHoraRegistro, String observacoes) {
        this.id = id;
        this.alunoId = alunoId;
        this.turmaId = turmaId;
        this.alunoNome = alunoNome;
        this.alunoMatricula = alunoMatricula;
        this.turmaNome = turmaNome;
        this.turmaCodigo = turmaCodigo;
        this.dataPresenca = dataPresenca;
        this.horaPresenca = horaPresenca;
        this.dataHoraRegistro = dataHoraRegistro;
        this.observacoes = observacoes;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }

    public Long getTurmaId() {
        return turmaId;
    }

    public void setTurmaId(Long turmaId) {
        this.turmaId = turmaId;
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

    public LocalDate getDataPresenca() {
        return dataPresenca;
    }

    public void setDataPresenca(LocalDate dataPresenca) {
        this.dataPresenca = dataPresenca;
    }

    public LocalTime getHoraPresenca() {
        return horaPresenca;
    }

    public void setHoraPresenca(LocalTime horaPresenca) {
        this.horaPresenca = horaPresenca;
    }

    public LocalDateTime getDataHoraRegistro() {
        return dataHoraRegistro;
    }

    public void setDataHoraRegistro(LocalDateTime dataHoraRegistro) {
        this.dataHoraRegistro = dataHoraRegistro;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    @Override
    public String toString() {
        return "PresencaDTO{" +
                "id=" + id +
                ", alunoId=" + alunoId +
                ", turmaId=" + turmaId +
                ", alunoNome='" + alunoNome + '\'' +
                ", alunoMatricula='" + alunoMatricula + '\'' +
                ", turmaNome='" + turmaNome + '\'' +
                ", turmaCodigo='" + turmaCodigo + '\'' +
                ", dataPresenca=" + dataPresenca +
                ", horaPresenca=" + horaPresenca +
                ", dataHoraRegistro=" + dataHoraRegistro +
                ", observacoes='" + observacoes + '\'' +
                '}';
    }
}

