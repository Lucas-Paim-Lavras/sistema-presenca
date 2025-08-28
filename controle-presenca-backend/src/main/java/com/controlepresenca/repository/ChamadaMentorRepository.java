package com.controlepresenca.repository;

import com.controlepresenca.entity.ChamadaMentor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository para operações de banco de dados da entidade ChamadaMentor
 */
@Repository
public interface ChamadaMentorRepository extends JpaRepository<ChamadaMentor, Long> {
    
    /**
     * Buscar chamada de mentor por data
     */
    Optional<ChamadaMentor> findByDataChamada(LocalDate dataChamada);
    
    /**
     * Verificar se já existe chamada de mentor para uma data específica
     */
    boolean existsByDataChamada(LocalDate dataChamada);
    
    /**
     * Listar chamadas de mentor por período
     */
    List<ChamadaMentor> findByDataChamadaBetweenOrderByDataChamadaDesc(LocalDate dataInicio, LocalDate dataFim);
    
    /**
     * Listar todas as chamadas de mentor ordenadas por data decrescente
     */
    List<ChamadaMentor> findAllByOrderByDataChamadaDesc();
    
    /**
     * Buscar chamadas de mentor do mês atual
     */
    @Query("SELECT cm FROM ChamadaMentor cm " +
           "WHERE YEAR(cm.dataChamada) = YEAR(CURRENT_DATE) " +
           "AND MONTH(cm.dataChamada) = MONTH(CURRENT_DATE) " +
           "ORDER BY cm.dataChamada DESC")
    List<ChamadaMentor> findChamadasMesAtual();
    
    /**
     * Buscar chamadas de mentor do ano atual
     */
    @Query("SELECT cm FROM ChamadaMentor cm " +
           "WHERE YEAR(cm.dataChamada) = YEAR(CURRENT_DATE) " +
           "ORDER BY cm.dataChamada DESC")
    List<ChamadaMentor> findChamadasAnoAtual();
    
    /**
     * Contar chamadas de mentor por período
     */
    long countByDataChamadaBetween(LocalDate dataInicio, LocalDate dataFim);
    
    /**
     * Contar total de chamadas de mentor
     */
    long count();
    
    /**
     * Buscar chamada de mentor por ID com participantes
     */
    @Query("SELECT cm FROM ChamadaMentor cm " +
           "LEFT JOIN FETCH cm.participantes p " +
           "LEFT JOIN FETCH p.mentor m " +
           "WHERE cm.id = :id")
    Optional<ChamadaMentor> findByIdWithParticipantes(@Param("id") Long id);
    
    /**
     * Buscar chamada de mentor por data com participantes
     */
    @Query("SELECT cm FROM ChamadaMentor cm " +
           "LEFT JOIN FETCH cm.participantes p " +
           "LEFT JOIN FETCH p.mentor m " +
           "WHERE cm.dataChamada = :dataChamada")
    Optional<ChamadaMentor> findByDataChamadaWithParticipantes(@Param("dataChamada") LocalDate dataChamada);
    
    /**
     * Listar chamadas de mentor com participantes por período
     */
    @Query("SELECT DISTINCT cm FROM ChamadaMentor cm " +
           "LEFT JOIN FETCH cm.participantes p " +
           "LEFT JOIN FETCH p.mentor m " +
           "WHERE cm.dataChamada BETWEEN :dataInicio AND :dataFim " +
           "ORDER BY cm.dataChamada DESC")
    List<ChamadaMentor> findByPeriodoWithParticipantes(
            @Param("dataInicio") LocalDate dataInicio, 
            @Param("dataFim") LocalDate dataFim);
    
    /**
     * Listar todas as chamadas de mentor com participantes
     */
    @Query("SELECT DISTINCT cm FROM ChamadaMentor cm " +
           "LEFT JOIN FETCH cm.participantes p " +
           "LEFT JOIN FETCH p.mentor m " +
           "ORDER BY cm.dataChamada DESC")
    List<ChamadaMentor> findAllWithParticipantes();
    
    /**
     * Buscar últimas N chamadas de mentor
     */
    @Query("SELECT cm FROM ChamadaMentor cm " +
           "ORDER BY cm.dataChamada DESC")
    List<ChamadaMentor> findTopNChamadas(@Param("limit") int limit);
}

