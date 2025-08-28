package com.controlepresenca.repository;

import com.controlepresenca.entity.Turma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para a entidade Turma
 * 
 * Fornece métodos para operações CRUD e consultas customizadas
 */
@Repository
public interface TurmaRepository extends JpaRepository<Turma, Long> {

    /**
     * Busca turma pelo código
     */
    Optional<Turma> findByCodigo(String codigo);

    /**
     * Busca turmas ativas
     */
    List<Turma> findByAtivaTrue();

    /**
     * Busca turmas por nome (case insensitive)
     */
    List<Turma> findByNomeContainingIgnoreCase(String nome);

    /**
     * Verifica se existe turma com o código especificado
     */
    boolean existsByCodigo(String codigo);

    /**
     * Verifica se existe turma com o código especificado, excluindo um ID
     */
    boolean existsByCodigoAndIdNot(String codigo, Long id);

    /**
     * Busca turmas com estatísticas (total de alunos e presenças)
     */
    @Query("SELECT t FROM Turma t LEFT JOIN FETCH t.alunos WHERE t.ativa = true")
    List<Turma> findTurmasComAlunos();

    /**
     * Conta total de alunos por turma
     */
    @Query("SELECT COUNT(a) FROM Aluno a WHERE a.turma.id = :turmaId AND a.ativo = true")
    Long countAlunosByTurmaId(@Param("turmaId") Long turmaId);

    /**
     * Conta total de presenças por turma
     */
    @Query("SELECT COUNT(p) FROM Presenca p WHERE p.turma.id = :turmaId")
    Long countPresencasByTurmaId(@Param("turmaId") Long turmaId);

    /**
     * Busca turmas ordenadas por nome
     */
    List<Turma> findByAtivaOrderByNome(Boolean ativa);
}

