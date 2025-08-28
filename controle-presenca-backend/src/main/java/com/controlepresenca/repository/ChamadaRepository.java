package com.controlepresenca.repository;

import com.controlepresenca.entity.Chamada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository para operações de banco de dados da entidade Chamada
 */
@Repository
public interface ChamadaRepository extends JpaRepository<Chamada, Long> {
    
    /**
     * Buscar chamada por turma e data
     */
    Optional<Chamada> findByTurmaIdAndDataChamada(Long turmaId, LocalDate dataChamada);
    
    /**
     * Listar todas as chamadas de uma turma específica
     */
    List<Chamada> findByTurmaIdOrderByDataChamadaDesc(Long turmaId);
    
    /**
     * Listar chamadas por período de datas
     */
    List<Chamada> findByDataChamadaBetweenOrderByDataChamadaDesc(LocalDate dataInicio, LocalDate dataFim);
    
    /**
     * Listar chamadas de uma turma por período de datas
     */
    List<Chamada> findByTurmaIdAndDataChamadaBetweenOrderByDataChamadaDesc(
            Long turmaId, LocalDate dataInicio, LocalDate dataFim);
    
    /**
     * Listar todas as chamadas ordenadas por data decrescente
     */
    List<Chamada> findAllByOrderByDataChamadaDesc();
    
    /**
     * Verificar se já existe uma chamada para uma turma em uma data específica
     */
    boolean existsByTurmaIdAndDataChamada(Long turmaId, LocalDate dataChamada);
    
    /**
     * Contar o total de chamadas de uma turma
     */
    long countByTurmaId(Long turmaId);
    
    /**
     * Buscar chamadas com informações da turma (usando JOIN FETCH)
     */
    @Query("SELECT c FROM Chamada c JOIN FETCH c.turma WHERE c.id = :id")
    Optional<Chamada> findByIdWithTurma(@Param("id") Long id);
    
    /**
     * Buscar chamadas com informações da turma e alunos (usando JOIN FETCH)
     */
    @Query("SELECT c FROM Chamada c " +
           "JOIN FETCH c.turma " +
           "LEFT JOIN FETCH c.chamadaAlunos ca " +
           "LEFT JOIN FETCH ca.aluno " +
           "WHERE c.id = :id")
    Optional<Chamada> findByIdWithTurmaAndAlunos(@Param("id") Long id);
    
    /**
     * Listar chamadas de uma turma com informações da turma
     */
    @Query("SELECT c FROM Chamada c JOIN FETCH c.turma WHERE c.turma.id = :turmaId ORDER BY c.dataChamada DESC")
    List<Chamada> findByTurmaIdWithTurma(@Param("turmaId") Long turmaId);
    
    /**
     * Buscar chamadas recentes (últimos 30 dias)
     */
    @Query("SELECT c FROM Chamada c JOIN FETCH c.turma " +
           "WHERE c.dataChamada >= :dataInicio " +
           "ORDER BY c.dataChamada DESC")
    List<Chamada> findRecentChamadas(@Param("dataInicio") LocalDate dataInicio);
}

