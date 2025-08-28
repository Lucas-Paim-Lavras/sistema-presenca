package com.controlepresenca.repository;

import com.controlepresenca.entity.Presenca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository para a entidade Presenca
 * 
 * Fornece métodos para operações CRUD e consultas customizadas
 */
@Repository
public interface PresencaRepository extends JpaRepository<Presenca, Long> {

    /**
     * Busca presenças por aluno
     */
    List<Presenca> findByAlunoId(Long alunoId);

    /**
     * Busca presenças por turma
     */
    List<Presenca> findByTurmaId(Long turmaId);

    /**
     * Busca presenças por data
     */
    List<Presenca> findByDataPresenca(LocalDate dataPresenca);

    /**
     * Busca presenças por turma e data
     */
    List<Presenca> findByTurmaIdAndDataPresenca(Long turmaId, LocalDate dataPresenca);

    /**
     * Busca presenças por aluno e data
     */
    List<Presenca> findByAlunoIdAndDataPresenca(Long alunoId, LocalDate dataPresenca);

    /**
     * Busca presença específica por aluno e data
     */
    Optional<Presenca> findByAlunoIdAndDataPresencaAndTurmaId(Long alunoId, LocalDate dataPresenca, Long turmaId);

    /**
     * Busca presenças por período
     */
    List<Presenca> findByDataPresencaBetween(LocalDate dataInicio, LocalDate dataFim);

    /**
     * Busca presenças por turma e período
     */
    List<Presenca> findByTurmaIdAndDataPresencaBetween(Long turmaId, LocalDate dataInicio, LocalDate dataFim);

    /**
     * Busca presenças por aluno e período
     */
    List<Presenca> findByAlunoIdAndDataPresencaBetween(Long alunoId, LocalDate dataInicio, LocalDate dataFim);

    /**
     * Verifica se existe presença para aluno em uma data específica
     */
    boolean existsByAlunoIdAndDataPresenca(Long alunoId, LocalDate dataPresenca);

    /**
     * Busca presenças com informações de aluno e turma
     */
    @Query("SELECT p FROM Presenca p JOIN FETCH p.aluno JOIN FETCH p.turma ORDER BY p.dataPresenca DESC, p.horaPresenca DESC")
    List<Presenca> findPresencasComAlunoETurma();

    /**
     * Busca presenças por turma com informações de aluno
     */
    @Query("SELECT p FROM Presenca p JOIN FETCH p.aluno WHERE p.turma.id = :turmaId ORDER BY p.dataPresenca DESC, p.horaPresenca DESC")
    List<Presenca> findByTurmaIdComAluno(@Param("turmaId") Long turmaId);

    /**
     * Conta presenças por turma
     */
    Long countByTurmaId(Long turmaId);

    /**
     * Conta presenças por aluno
     */
    Long countByAlunoId(Long alunoId);

    /**
     * Conta presenças por turma e data
     */
    Long countByTurmaIdAndDataPresenca(Long turmaId, LocalDate dataPresenca);

    /**
     * Busca presenças ordenadas por data e hora (mais recentes primeiro)
     */
    List<Presenca> findAllByOrderByDataPresencaDescHoraPresencaDesc();

    /**
     * Busca presenças por turma ordenadas por data e hora
     */
    List<Presenca> findByTurmaIdOrderByDataPresencaDescHoraPresencaDesc(Long turmaId);

    /**
     * Relatório de presenças com informações completas
     */
    @Query("SELECT p FROM Presenca p " +
           "JOIN FETCH p.aluno a " +
           "JOIN FETCH p.turma t " +
           "WHERE (:turmaId IS NULL OR p.turma.id = :turmaId) " +
           "AND (:dataInicio IS NULL OR p.dataPresenca >= :dataInicio) " +
           "AND (:dataFim IS NULL OR p.dataPresenca <= :dataFim) " +
           "ORDER BY p.dataPresenca DESC, p.horaPresenca DESC")
    List<Presenca> findRelatorioPresencas(@Param("turmaId") Long turmaId, 
                                         @Param("dataInicio") LocalDate dataInicio, 
                                         @Param("dataFim") LocalDate dataFim);
}

