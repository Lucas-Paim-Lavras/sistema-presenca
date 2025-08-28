package com.controlepresenca.service;

import com.controlepresenca.dto.MentorDTO;
import com.controlepresenca.entity.Mentor;
import com.controlepresenca.repository.MentorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para operações de negócio relacionadas a Mentores
 */
@Service
@Transactional
public class MentorService {
    
    @Autowired
    private MentorRepository mentorRepository;
    
    
    /**
     * Listar todos os mentores
     */
    @Transactional(readOnly = true)
    public List<MentorDTO> listarTodos() {
        List<Mentor> mentores = mentorRepository.findAllByOrderByNome();
        return mentores.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Listar mentores ativos
     */
    @Transactional(readOnly = true)
    public List<MentorDTO> listarAtivos() {
        List<Mentor> mentores = mentorRepository.findByAtivoTrueOrderByNome();
        return mentores.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Listar mentores por tipo
     */
    @Transactional(readOnly = true)
    public List<MentorDTO> listarPorTipo(String tipoMentor) {
        try {
            Mentor.TipoMentor tipo = Mentor.TipoMentor.valueOf(tipoMentor.toUpperCase());
            List<Mentor> mentores = mentorRepository.findByTipoMentorAndAtivoTrueOrderByNome(tipo);
            return mentores.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Tipo de mentor inválido: " + tipoMentor);
        }
    }
    
    /**
     * Buscar mentor por ID
     */
    @Transactional(readOnly = true)
    public MentorDTO buscarPorId(Long id) {
        Mentor mentor = mentorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mentor não encontrado"));
        return convertToDTO(mentor);
    }
    
    /**
     * Buscar mentor por email
     */
    @Transactional(readOnly = true)
    public MentorDTO buscarPorEmail(String email) {
        Mentor mentor = mentorRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Mentor não encontrado"));
        return convertToDTO(mentor);
    }
    
    /**
     * Criar novo mentor
     */
    public MentorDTO criar(MentorDTO mentorDTO) {
        // Verificar se já existe mentor com o mesmo email
        if (mentorRepository.existsByEmail(mentorDTO.getEmail())) {
            throw new RuntimeException("Já existe um mentor com o email: " + mentorDTO.getEmail());
        }
        
        Mentor mentor = new Mentor();
        mentor.setNome(mentorDTO.getNome());
        mentor.setEmail(mentorDTO.getEmail());
        
        try {
            mentor.setTipoMentor(Mentor.TipoMentor.valueOf(mentorDTO.getTipoMentor().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Tipo de mentor inválido: " + mentorDTO.getTipoMentor());
        }
        
        mentor = mentorRepository.save(mentor);
        return convertToDTO(mentor);
    }
    
    /**
     * Atualizar mentor
     */
    public MentorDTO atualizar(Long id, MentorDTO mentorDTO) {
        Mentor mentor = mentorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mentor não encontrado"));
        
        // Verificar se o email já existe em outro mentor
        if (!mentor.getEmail().equals(mentorDTO.getEmail()) && 
            mentorRepository.existsByEmail(mentorDTO.getEmail())) {
            throw new RuntimeException("Já existe um mentor com o email: " + mentorDTO.getEmail());
        }
        
        mentor.setNome(mentorDTO.getNome());
        mentor.setEmail(mentorDTO.getEmail());
        
        try {
            mentor.setTipoMentor(Mentor.TipoMentor.valueOf(mentorDTO.getTipoMentor().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Tipo de mentor inválido: " + mentorDTO.getTipoMentor());
        }
        
        mentor.setAtivo(mentorDTO.getAtivo());
        
        mentor = mentorRepository.save(mentor);
        return convertToDTO(mentor);
    }
    
    /**
     * Remover mentor (soft delete)
     */
    public void remover(Long id) {
        Mentor mentor = mentorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mentor não encontrado"));
        
        mentor.setAtivo(false);
        mentorRepository.save(mentor);
    }
    
    /**
     * Reativar mentor
     */
    public MentorDTO reativar(Long id) {
        Mentor mentor = mentorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mentor não encontrado"));
        
        mentor.setAtivo(true);
        mentor = mentorRepository.save(mentor);
        return convertToDTO(mentor);
    }
    
    /**
     * Buscar mentores por nome
     */
    @Transactional(readOnly = true)
    public List<MentorDTO> buscarPorNome(String nome) {
        List<Mentor> mentores = mentorRepository.findByNomeContainingIgnoreCaseAndAtivoTrue(nome);
        return mentores.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Obter estatísticas de mentores
     */
    @Transactional(readOnly = true)
    public MentorEstatisticasDTO obterEstatisticas() {
        MentorEstatisticasDTO stats = new MentorEstatisticasDTO();
        
        stats.setTotalMentores((int) mentorRepository.countByAtivoTrue());
        stats.setTotalMentores((int) mentorRepository.countByTipoMentorAndAtivoTrue(Mentor.TipoMentor.MENTOR));
        stats.setTotalMentorTrainees((int) mentorRepository.countByTipoMentorAndAtivoTrue(Mentor.TipoMentor.MENTOR_TRAINEE));
        stats.setTotalMentorCoordenadores((int) mentorRepository.countByTipoMentorAndAtivoTrue(Mentor.TipoMentor.MENTOR_COORDENADOR));
        
        return stats;
    }
    
    /**
     * Converter entidade Mentor para DTO
     */
    private MentorDTO convertToDTO(Mentor mentor) {
        MentorDTO dto = new MentorDTO(mentor);
        
        return dto;
    }
    
    /**
     * DTO para estatísticas de mentores
     */
    public static class MentorEstatisticasDTO {
        private Integer totalMentores;
        private Integer totalMentorTrainees;
        private Integer totalMentorCoordenadores;
        
        // Getters e Setters
        public Integer getTotalMentores() {
            return totalMentores;
        }
        
        public void setTotalMentores(Integer totalMentores) {
            this.totalMentores = totalMentores;
        }
        
        public Integer getTotalMentorTrainees() {
            return totalMentorTrainees;
        }
        
        public void setTotalMentorTrainees(Integer totalMentorTrainees) {
            this.totalMentorTrainees = totalMentorTrainees;
        }
        
        public Integer getTotalMentorCoordenadores() {
            return totalMentorCoordenadores;
        }
        
        public void setTotalMentorCoordenadores(Integer totalMentorCoordenadores) {
            this.totalMentorCoordenadores = totalMentorCoordenadores;
        }
    }
}

