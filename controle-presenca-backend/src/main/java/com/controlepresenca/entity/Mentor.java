package com.controlepresenca.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidade que representa um Mentor do YouX Lab
 */
@Entity
@Table(name = "mentores")
public class Mentor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nome", nullable = false, length = 150)
    private String nome;
    
    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_mentor", nullable = false, length = 50)
    private TipoMentor tipoMentor;
    
    @Column(name = "data_cadastro", nullable = false)
    private LocalDateTime dataCadastro;
    
    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;
    
    
    // Enum para tipos de mentor
    public enum TipoMentor {
        MENTOR("Mentor"),
        MENTOR_TRAINEE("Mentor-trainee"),
        MENTOR_COORDENADOR("Mentor Coordenador");
        
        private final String descricao;
        
        TipoMentor(String descricao) {
            this.descricao = descricao;
        }
        
        public String getDescricao() {
            return descricao;
        }
    }
    
    // Construtor padrão
    public Mentor() {
        this.dataCadastro = LocalDateTime.now();
        this.ativo = true;
    }
    
    // Construtor com parâmetros
    public Mentor(String nome, String email, TipoMentor tipoMentor) {
        this();
        this.nome = nome;
        this.email = email;
        this.tipoMentor = tipoMentor;
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
    
    public TipoMentor getTipoMentor() {
        return tipoMentor;
    }
    
    public void setTipoMentor(TipoMentor tipoMentor) {
        this.tipoMentor = tipoMentor;
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
    
    
    @PrePersist
    protected void onCreate() {
        if (dataCadastro == null) {
            dataCadastro = LocalDateTime.now();
        }
        if (ativo == null) {
            ativo = true;
        }
    }
    
    @Override
    public String toString() {
        return "Mentor{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", tipoMentor=" + tipoMentor +
                ", dataCadastro=" + dataCadastro +
                ", ativo=" + ativo +
                '}';
    }
}
