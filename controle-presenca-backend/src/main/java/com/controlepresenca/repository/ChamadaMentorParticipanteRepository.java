package com.controlepresenca.repository;

import com.controlepresenca.entity.ChamadaMentorParticipante;
import com.controlepresenca.entity.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository para operações de banco de dados da entidade ChamadaMentorParticipante
 */
@Repository
public interface ChamadaMentorParticipanteRepository extends JpaRepository<ChamadaMentorParticipante, Long> {
    
    /**
     * Buscar participação por chamada de mentor e mentor
     */
    Optional<ChamadaMentorParticipante> findByChamadaMentorIdAndMentorId(Long chamadaMentorId, Long mentorId);
    
    /**
     * Verificar se já existe participação para um mentor em uma chamada específica
     */
    boolean existsByChamadaMentorIdAndMentorId(Long chamadaMentorId, Long mentorId);
    
    /**
     * Listar participações por chamada de mentor
     */
    List<ChamadaMentorParticipante> findByChamadaMentorIdOrderByMentorNome(Long chamadaMentorId);
    
    /**
     * Listar participações por mentor
     */
    List<ChamadaMentorParticipante> findByMentorIdOrderByChamadaMentorDataChamadaDesc(Long mentorId);
    
    /**
     * Listar participações por mentor e período
     */
    @Query("SELECT cmp FROM ChamadaMentorParticipante cmp " +
           "JOIN cmp.chamadaMentor cm " +
           "WHERE cmp.mentor.id = :mentorId " +
           "AND cm.dataChamada BETWEEN :dataInicio AND :dataFim " +
           "ORDER BY cm.dataChamada DESC")
    List<ChamadaMentorParticipante> findByMentorIdAndPeriodo(
            @Param("mentorId") Long mentorId,
            @Param("dataInicio") LocalDate dataInicio, 
            @Param("dataFim") LocalDate dataFim);
    
    /**
     * Listar participações por tipo de mentor
     */
    List<ChamadaMentorParticipante> findByMentorTipoMentorOrderByChamadaMentorDataChamadaDescMentorNome(
            Mentor.TipoMentor tipoMentor);
    
    /**
     * Listar participações por tipo de mentor e período
     */
    @Query("SELECT cmp FROM ChamadaMentorParticipante cmp " +
           "JOIN cmp.chamadaMentor cm " +
           "WHERE cmp.mentor.tipoMentor = :tipoMentor " +
           "AND cm.dataChamada BETWEEN :dataInicio AND :dataFim " +
           "ORDER BY cm.dataChamada DESC, cmp.mentor.nome")
    List<ChamadaMentorParticipante> findByTipoMentorAndPeriodo(
            @Param("tipoMentor") Mentor.TipoMentor tipoMentor,
            @Param("dataInicio") LocalDate dataInicio, 
            @Param("dataFim") LocalDate dataFim);
    
    /**
     * Contar participações de um mentor
     */
    long countByMentorId(Long mentorId);
    
    /**
     * Contar presenças de um mentor
     */
    long countByMentorIdAndPresenteTrue(Long mentorId);
    
    /**
     * Contar faltas de um mentor
     */
    long countByMentorIdAndPresenteFalse(Long mentorId);
    
    /**
     * Contar participações por tipo de mentor
     */
    long countByMentorTipoMentor(Mentor.TipoMentor tipoMentor);
    
    /**
     * Contar presenças por tipo de mentor
     */
    long countByMentorTipoMentorAndPresenteTrue(Mentor.TipoMentor tipoMentor);
    
    /**
     * Contar participações em uma chamada específica
     */
    long countByChamadaMentorId(Long chamadaMentorId);
    
    /**
     * Contar presenças em uma chamada específica
     */
    long countByChamadaMentorIdAndPresenteTrue(Long chamadaMentorId);
    
    /**
     * Contar faltas em uma chamada específica
     */
    long countByChamadaMentorIdAndPresenteFalse(Long chamadaMentorId);
    
    /**
     * Listar participações com informações do mentor e da chamada
     */
    @Query("SELECT cmp FROM ChamadaMentorParticipante cmp " +
           "JOIN FETCH cmp.mentor m " +
           "JOIN FETCH cmp.chamadaMentor cm " +
           "WHERE cmp.chamadaMentor.id = :chamadaMentorId " +
           "ORDER BY m.nome")
    List<ChamadaMentorParticipante> findByChamadaMentorIdWithMentorAndChamada(@Param("chamadaMentorId") Long chamadaMentorId);
    
    /**
     * Listar participações por mentor com informações da chamada
     */
    @Query("SELECT cmp FROM ChamadaMentorParticipante cmp " +
           "JOIN FETCH cmp.mentor m " +
           "JOIN FETCH cmp.chamadaMentor cm " +
           "WHERE cmp.mentor.id = :mentorId " +
           "ORDER BY cm.dataChamada DESC")
    List<ChamadaMentorParticipante> findByMentorIdWithChamada(@Param("mentorId") Long mentorId);
    
    /**
     * Listar participações por período com informações do mentor e da chamada
     */
    @Query("SELECT cmp FROM ChamadaMentorParticipante cmp " +
           "JOIN FETCH cmp.mentor m " +
           "JOIN FETCH cmp.chamadaMentor cm " +
           "WHERE cm.dataChamada BETWEEN :dataInicio AND :dataFim " +
           "ORDER BY cm.dataChamada DESC, m.nome")
    List<ChamadaMentorParticipante> findByPeriodoWithMentorAndChamada(
            @Param("dataInicio") LocalDate dataInicio, 
            @Param("dataFim") LocalDate dataFim);
    
    /**
     * Estatísticas de participação por mentor
     */
    @Query("SELECT cmp.mentor.id, cmp.mentor.nome, cmp.mentor.email, cmp.mentor.tipoMentor, " +
           "COUNT(cmp.id) as totalParticipacoes, " +
           "COUNT(CASE WHEN cmp.presente = true THEN 1 END) as totalPresencas, " +
           "COUNT(CASE WHEN cmp.presente = false THEN 1 END) as totalFaltas " +
           "FROM ChamadaMentorParticipante cmp " +
           "GROUP BY cmp.mentor.id, cmp.mentor.nome, cmp.mentor.email, cmp.mentor.tipoMentor " +
           "ORDER BY cmp.mentor.nome")
    List<Object[]> findEstatisticasParticipacaoPorMentor();
    
    /**
     * Estatísticas de participação por tipo de mentor
     */
    @Query("SELECT cmp.mentor.tipoMentor, " +
           "COUNT(cmp.id) as totalParticipacoes, " +
           "COUNT(CASE WHEN cmp.presente = true THEN 1 END) as totalPresencas, " +
           "COUNT(CASE WHEN cmp.presente = false THEN 1 END) as totalFaltas " +
           "FROM ChamadaMentorParticipante cmp " +
           "GROUP BY cmp.mentor.tipoMentor " +
           "ORDER BY cmp.mentor.tipoMentor")
    List<Object[]> findEstatisticasParticipacaoPorTipoMentor();
}

