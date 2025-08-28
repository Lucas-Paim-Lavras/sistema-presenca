package com.controlepresenca.service;

import com.controlepresenca.dto.*;
import com.controlepresenca.entity.ChamadaMentor;
import com.controlepresenca.entity.ChamadaMentorParticipante;
import com.controlepresenca.entity.Mentor;
import com.controlepresenca.repository.ChamadaMentorParticipanteRepository;
import com.controlepresenca.repository.ChamadaMentorRepository;
import com.controlepresenca.repository.MentorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service para operações de negócio relacionadas a Chamadas de Mentores
 */
@Service
@Transactional
public class ChamadaMentorService {
    
    @Autowired
    private ChamadaMentorRepository chamadaMentorRepository;
    
    @Autowired
    private ChamadaMentorParticipanteRepository participanteRepository;
    
    @Autowired
    private MentorRepository mentorRepository;
    
    /**
     * Criar nova chamada de mentor
     */
    public ChamadaMentorDTO criarChamada(CriarChamadaMentorDTO criarChamadaDTO) {
        // Verificar se já existe chamada para esta data
        if (chamadaMentorRepository.existsByDataChamada(criarChamadaDTO.getDataChamada())) {
            throw new RuntimeException("Já existe uma chamada de mentor para a data " + criarChamadaDTO.getDataChamada());
        }
        
        // Criar a chamada
        ChamadaMentor chamada = new ChamadaMentor();
        chamada.setDataChamada(criarChamadaDTO.getDataChamada());
        chamada.setObservacoes(criarChamadaDTO.getObservacoes());
        
        chamada = chamadaMentorRepository.save(chamada);
        
        // Criar os participantes
        if (criarChamadaDTO.getParticipantes() != null && !criarChamadaDTO.getParticipantes().isEmpty()) {
            for (CriarChamadaMentorDTO.ParticipanteChamadaMentorDTO participanteDTO : criarChamadaDTO.getParticipantes()) {
                Mentor mentor = mentorRepository.findById(participanteDTO.getMentorId())
                        .orElseThrow(() -> new RuntimeException("Mentor não encontrado: " + participanteDTO.getMentorId()));
                
                ChamadaMentorParticipante participante = new ChamadaMentorParticipante();
                participante.setChamadaMentor(chamada);
                participante.setMentor(mentor);
                participante.setPresente(participanteDTO.getPresente());
                
                participanteRepository.save(participante);
            }
        }
        
        return buscarPorId(chamada.getId());
    }
    
    /**
     * Listar todas as chamadas de mentor
     */
    @Transactional(readOnly = true)
    public List<ChamadaMentorDTO> listarTodas() {
        List<ChamadaMentor> chamadas = chamadaMentorRepository.findAllByOrderByDataChamadaDesc();
        return chamadas.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Buscar chamada de mentor por ID
     */
    @Transactional(readOnly = true)
    public ChamadaMentorDTO buscarPorId(Long id) {
        Optional<ChamadaMentor> chamadaOpt = chamadaMentorRepository.findByIdWithParticipantes(id);
        if (chamadaOpt.isEmpty()) {
            throw new RuntimeException("Chamada de mentor não encontrada");
        }
        
        return convertToDTOWithParticipantes(chamadaOpt.get());
    }
    
    /**
     * Buscar chamada de mentor por data
     */
    @Transactional(readOnly = true)
    public Optional<ChamadaMentorDTO> buscarPorData(LocalDate dataChamada) {
        Optional<ChamadaMentor> chamadaOpt = chamadaMentorRepository.findByDataChamadaWithParticipantes(dataChamada);
        return chamadaOpt.map(this::convertToDTOWithParticipantes);
    }
    
    /**
     * Listar chamadas de mentor por período
     */
    @Transactional(readOnly = true)
    public List<ChamadaMentorDTO> listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        List<ChamadaMentor> chamadas = chamadaMentorRepository.findByDataChamadaBetweenOrderByDataChamadaDesc(dataInicio, dataFim);
        return chamadas.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Atualizar chamada de mentor
     */
    public ChamadaMentorDTO atualizar(Long id, CriarChamadaMentorDTO criarChamadaDTO) {
        ChamadaMentor chamada = chamadaMentorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chamada de mentor não encontrada"));
        
        // Verificar se a mudança de data não conflita com outra chamada
        if (!chamada.getDataChamada().equals(criarChamadaDTO.getDataChamada()) &&
            chamadaMentorRepository.existsByDataChamada(criarChamadaDTO.getDataChamada())) {
            throw new RuntimeException("Já existe uma chamada de mentor para a data " + criarChamadaDTO.getDataChamada());
        }
        
        // Atualizar dados da chamada
        chamada.setDataChamada(criarChamadaDTO.getDataChamada());
        chamada.setObservacoes(criarChamadaDTO.getObservacoes());
        
        chamada = chamadaMentorRepository.save(chamada);
        
        // Remover participantes existentes
        List<ChamadaMentorParticipante> participantesExistentes = participanteRepository.findByChamadaMentorIdOrderByMentorNome(id);
        participanteRepository.deleteAll(participantesExistentes);
        
        // Criar novos participantes
        if (criarChamadaDTO.getParticipantes() != null && !criarChamadaDTO.getParticipantes().isEmpty()) {
            for (CriarChamadaMentorDTO.ParticipanteChamadaMentorDTO participanteDTO : criarChamadaDTO.getParticipantes()) {
                Mentor mentor = mentorRepository.findById(participanteDTO.getMentorId())
                        .orElseThrow(() -> new RuntimeException("Mentor não encontrado: " + participanteDTO.getMentorId()));
                
                ChamadaMentorParticipante participante = new ChamadaMentorParticipante();
                participante.setChamadaMentor(chamada);
                participante.setMentor(mentor);
                participante.setPresente(participanteDTO.getPresente());
                
                participanteRepository.save(participante);
            }
        }
        
        return buscarPorId(chamada.getId());
    }
    
    /**
     * Remover chamada de mentor
     */
    public void remover(Long id) {
        if (!chamadaMentorRepository.existsById(id)) {
            throw new RuntimeException("Chamada de mentor não encontrada");
        }
        chamadaMentorRepository.deleteById(id);
    }
    
    /**
     * Obter estatísticas de chamadas de mentor
     */
    @Transactional(readOnly = true)
    public ChamadaMentorEstatisticasDTO obterEstatisticas() {
        ChamadaMentorEstatisticasDTO stats = new ChamadaMentorEstatisticasDTO();
        
        LocalDate hoje = LocalDate.now();
        LocalDate inicioMes = hoje.withDayOfMonth(1);
        LocalDate fimMes = hoje.withDayOfMonth(hoje.lengthOfMonth());
        
        stats.setTotalChamadas((int) chamadaMentorRepository.count());
        stats.setTotalChamadasMesAtual((int) chamadaMentorRepository.countByDataChamadaBetween(inicioMes, fimMes));
        
        // Verificar se existe chamada hoje
        Optional<ChamadaMentor> chamadaHoje = chamadaMentorRepository.findByDataChamada(hoje);
        if (chamadaHoje.isPresent()) {
            long totalPresentes = participanteRepository.countByChamadaMentorIdAndPresenteTrue(chamadaHoje.get().getId());
            long totalAusentes = participanteRepository.countByChamadaMentorIdAndPresenteFalse(chamadaHoje.get().getId());
            
            stats.setTotalPresentesHoje((int) totalPresentes);
            stats.setTotalAusentesHoje((int) totalAusentes);
        } else {
            stats.setTotalPresentesHoje(0);
            stats.setTotalAusentesHoje(0);
        }
        
        return stats;
    }
    
    /**
     * Converter entidade ChamadaMentor para DTO
     */
    private ChamadaMentorDTO convertToDTO(ChamadaMentor chamada) {
        ChamadaMentorDTO dto = new ChamadaMentorDTO(chamada);
        
        // Adicionar estatísticas básicas
        long totalParticipantes = participanteRepository.countByChamadaMentorId(chamada.getId());
        long totalPresentes = participanteRepository.countByChamadaMentorIdAndPresenteTrue(chamada.getId());
        long totalAusentes = participanteRepository.countByChamadaMentorIdAndPresenteFalse(chamada.getId());
        
        dto.setTotalMentores((int) totalParticipantes);
        dto.setTotalPresentes((int) totalPresentes);
        dto.setTotalAusentes((int) totalAusentes);
        
        return dto;
    }
    
    /**
     * Converter entidade ChamadaMentor para DTO com participantes
     */
    private ChamadaMentorDTO convertToDTOWithParticipantes(ChamadaMentor chamada) {
        ChamadaMentorDTO dto = convertToDTO(chamada);
        
        // Adicionar participantes
        List<ChamadaMentorParticipante> participantes = participanteRepository.findByChamadaMentorIdWithMentorAndChamada(chamada.getId());
        List<ChamadaMentorParticipanteDTO> participantesDTO = participantes.stream()
                .map(ChamadaMentorParticipanteDTO::new)
                .collect(Collectors.toList());
        
        dto.setParticipantes(participantesDTO);
        
        return dto;
    }
    
    /**
     * DTO para estatísticas de chamadas de mentor
     */
    public static class ChamadaMentorEstatisticasDTO {
        private Integer totalChamadas;
        private Integer totalChamadasMesAtual;
        private Integer totalPresentesHoje;
        private Integer totalAusentesHoje;
        
        // Getters e Setters
        public Integer getTotalChamadas() {
            return totalChamadas;
        }
        
        public void setTotalChamadas(Integer totalChamadas) {
            this.totalChamadas = totalChamadas;
        }
        
        public Integer getTotalChamadasMesAtual() {
            return totalChamadasMesAtual;
        }
        
        public void setTotalChamadasMesAtual(Integer totalChamadasMesAtual) {
            this.totalChamadasMesAtual = totalChamadasMesAtual;
        }
        
        public Integer getTotalPresentesHoje() {
            return totalPresentesHoje;
        }
        
        public void setTotalPresentesHoje(Integer totalPresentesHoje) {
            this.totalPresentesHoje = totalPresentesHoje;
        }
        
        public Integer getTotalAusentesHoje() {
            return totalAusentesHoje;
        }
        
        public void setTotalAusentesHoje(Integer totalAusentesHoje) {
            this.totalAusentesHoje = totalAusentesHoje;
        }
    }
}

