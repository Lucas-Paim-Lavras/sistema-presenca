package com.controlepresenca.repository;

import com.controlepresenca.entity.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para operações de banco de dados da entidade Mentor
 */
@Repository
public interface MentorRepository extends JpaRepository<Mentor, Long> {
    
    /**
     * Buscar mentor por email
     */
    Optional<Mentor> findByEmail(String email);
    
    /**
     * Verificar se existe mentor com o email especificado
     */
    boolean existsByEmail(String email);
    
    /**
     * Listar mentores ativos
     */
    List<Mentor> findByAtivoTrueOrderByNome();
    
    /**
     * Listar mentores por tipo
     */
    List<Mentor> findByTipoMentorAndAtivoTrueOrderByNome(Mentor.TipoMentor tipoMentor);
    
    /**
     * Listar todos os mentores ordenados por nome
     */
    List<Mentor> findAllByOrderByNome();
    
    /**
     * Buscar mentores por nome (busca parcial, case insensitive)
     */
    @Query("SELECT m FROM Mentor m WHERE LOWER(m.nome) LIKE LOWER(CONCAT('%', :nome, '%')) AND m.ativo = true ORDER BY m.nome")
    List<Mentor> findByNomeContainingIgnoreCaseAndAtivoTrue(@Param("nome") String nome);
    
    /**
     * Contar mentores por tipo
     */
    long countByTipoMentorAndAtivoTrue(Mentor.TipoMentor tipoMentor);
    
    /**
     * Contar total de mentores ativos
     */
    long countByAtivoTrue();
    

}
