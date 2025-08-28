package com.controlepresenca.repository;

import com.controlepresenca.entity.ChamadaAluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository para operações de banco de dados da entidade ChamadaAluno
 */
@Repository
public interface ChamadaAlunoRepository extends JpaRepository<ChamadaAluno, Long> {
    
    /**
     * Buscar registros de presença por chamada
     */
    List<ChamadaAluno> findByChamadaIdOrderByAlunoNome(Long chamadaId);
    
    /**
     * Buscar registros de presença por aluno
     */
    List<ChamadaAluno> findByAlunoIdOrderByChamadaDataChamadaDesc(Long alunoId);
    
    /**
     * Buscar registro específico de um aluno em uma chamada
     */
    Optional<ChamadaAluno> findByChamadaIdAndAlunoId(Long chamadaId, Long alunoId);
    
    /**
     * Contar presenças de um aluno
     */
    long countByAlunoIdAndPresenteTrue(Long alunoId);
    
    /**
     * Contar faltas de um aluno
     */
    long countByAlunoIdAndPresenteFalse(Long alunoId);
    
    /**
     * Contar total de registros de um aluno (presenças + faltas)
     */
    long countByAlunoId(Long alunoId);
    
    /**
     * Contar presenças em uma chamada específica
     */
    long countByChamadaIdAndPresenteTrue(Long chamadaId);
    
    /**
     * Contar faltas em uma chamada específica
     */
    long countByChamadaIdAndPresenteFalse(Long chamadaId);
    
    /**
     * Buscar registros de presença por aluno com informações da chamada e turma
     */
    @Query("SELECT ca FROM ChamadaAluno ca " +
           "JOIN FETCH ca.chamada c " +
           "JOIN FETCH c.turma " +
           "WHERE ca.aluno.id = :alunoId " +
           "ORDER BY c.dataChamada DESC")
    List<ChamadaAluno> findByAlunoIdWithChamadaAndTurma(@Param("alunoId") Long alunoId);
    
    /**
     * Buscar registros de presença por chamada com informações do aluno
     */
    @Query("SELECT ca FROM ChamadaAluno ca " +
           "JOIN FETCH ca.aluno " +
           "WHERE ca.chamada.id = :chamadaId " +
           "ORDER BY ca.aluno.nome")
    List<ChamadaAluno> findByChamadaIdWithAluno(@Param("chamadaId") Long chamadaId);
    
    /**
     * Buscar registros de presença por turma e período
     */
    @Query("SELECT ca FROM ChamadaAluno ca " +
           "JOIN FETCH ca.chamada c " +
           "JOIN FETCH ca.aluno a " +
           "WHERE c.turma.id = :turmaId " +
           "AND c.dataChamada BETWEEN :dataInicio AND :dataFim " +
           "ORDER BY c.dataChamada DESC, a.nome")
    List<ChamadaAluno> findByTurmaAndPeriodo(
            @Param("turmaId") Long turmaId, 
            @Param("dataInicio") LocalDate dataInicio, 
            @Param("dataFim") LocalDate dataFim);
    
    /**
     * Buscar registros de presença por aluno e período
     */
    @Query("SELECT ca FROM ChamadaAluno ca " +
           "JOIN FETCH ca.chamada c " +
           "JOIN FETCH c.turma " +
           "WHERE ca.aluno.id = :alunoId " +
           "AND c.dataChamada BETWEEN :dataInicio AND :dataFim " +
           "ORDER BY c.dataChamada DESC")
    List<ChamadaAluno> findByAlunoAndPeriodo(
            @Param("alunoId") Long alunoId, 
            @Param("dataInicio") LocalDate dataInicio, 
            @Param("dataFim") LocalDate dataFim);
    
    /**
     * Estatísticas de presença por aluno
     */
    @Query("SELECT ca.aluno.id, ca.aluno.nome, ca.aluno.matricula, " +
           "COUNT(CASE WHEN ca.presente = true THEN 1 END) as presencas, " +
           "COUNT(CASE WHEN ca.presente = false THEN 1 END) as faltas, " +
           "COUNT(ca.id) as total " +
           "FROM ChamadaAluno ca " +
           "WHERE ca.aluno.turma.id = :turmaId " +
           "GROUP BY ca.aluno.id, ca.aluno.nome, ca.aluno.matricula " +
           "ORDER BY ca.aluno.nome")
    List<Object[]> findEstatisticasByTurma(@Param("turmaId") Long turmaId);
    
    /**
     * Verificar se um aluno já tem registro em uma chamada
     */
    boolean existsByChamadaIdAndAlunoId(Long chamadaId, Long alunoId);
}

