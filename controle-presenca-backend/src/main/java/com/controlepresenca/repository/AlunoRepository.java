package com.controlepresenca.repository;

import com.controlepresenca.entity.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para a entidade Aluno
 * 
 * Fornece métodos para operações CRUD e consultas customizadas
 */
@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    /**
     * Busca aluno pela matrícula
     */
    Optional<Aluno> findByMatricula(String matricula);

    /**
     * Busca aluno pelo email
     */
    Optional<Aluno> findByEmail(String email);

    /**
     * Busca alunos por turma
     */
    List<Aluno> findByTurmaId(Long turmaId);

    /**
     * Busca alunos ativos por turma
     */
    List<Aluno> findByTurmaIdAndAtivoTrue(Long turmaId);

    /**
     * Busca alunos ativos
     */
    List<Aluno> findByAtivoTrue();

    /**
     * Busca alunos por nome (case insensitive)
     */
    List<Aluno> findByNomeContainingIgnoreCase(String nome);

    /**
     * Busca alunos por turma e nome
     */
    List<Aluno> findByTurmaIdAndNomeContainingIgnoreCase(Long turmaId, String nome);

    /**
     * Verifica se existe aluno com a matrícula especificada
     */
    boolean existsByMatricula(String matricula);

    /**
     * Verifica se existe aluno com o email especificado
     */
    boolean existsByEmail(String email);

    /**
     * Verifica se existe aluno com a matrícula especificada, excluindo um ID
     */
    boolean existsByMatriculaAndIdNot(String matricula, Long id);

    /**
     * Verifica se existe aluno com o email especificado, excluindo um ID
     */
    boolean existsByEmailAndIdNot(String email, Long id);

    /**
     * Conta total de presenças por aluno
     */
    @Query("SELECT COUNT(p) FROM Presenca p WHERE p.aluno.id = :alunoId")
    Long countPresencasByAlunoId(@Param("alunoId") Long alunoId);

    /**
     * Busca alunos com informações da turma
     */
    @Query("SELECT a FROM Aluno a JOIN FETCH a.turma WHERE a.ativo = true ORDER BY a.nome")
    List<Aluno> findAlunosComTurma();

    /**
     * Busca alunos por turma ordenados por nome
     */
    List<Aluno> findByTurmaIdAndAtivoOrderByNome(Long turmaId, Boolean ativo);

    /**
     * Conta alunos por turma
     */
    Long countByTurmaIdAndAtivo(Long turmaId, Boolean ativo);
}

