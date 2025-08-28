package com.controlepresenca.service;

import com.controlepresenca.dto.*;
import com.controlepresenca.entity.*;
import com.controlepresenca.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service para operações de negócio relacionadas a Chamadas
 */
@Service
@Transactional
public class ChamadaService {
    
    @Autowired
    private ChamadaRepository chamadaRepository;
    
    @Autowired
    private ChamadaAlunoRepository chamadaAlunoRepository;
    
    @Autowired
    private TurmaRepository turmaRepository;
    
    @Autowired
    private AlunoRepository alunoRepository;
    
    /**
     * Criar uma nova chamada com os status de presença dos alunos
     */
    public ChamadaDTO criarChamada(CriarChamadaDTO criarChamadaDTO) {
        // Verificar se a turma existe
        Turma turma = turmaRepository.findById(criarChamadaDTO.getTurmaId())
                .orElseThrow(() -> new RuntimeException("Turma não encontrada"));
        
        // Verificar se já existe uma chamada para esta turma nesta data
        if (chamadaRepository.existsByTurmaIdAndDataChamada(criarChamadaDTO.getTurmaId(), criarChamadaDTO.getDataChamada())) {
            throw new RuntimeException("Já existe uma chamada para esta turma na data " + criarChamadaDTO.getDataChamada());
        }
        
        // Criar a chamada
        Chamada chamada = new Chamada();
        chamada.setTurma(turma);
        chamada.setDataChamada(criarChamadaDTO.getDataChamada());
        chamada.setObservacoes(criarChamadaDTO.getObservacoes());
        
        chamada = chamadaRepository.save(chamada);
        
        // Criar os registros de presença/falta para cada aluno
        List<ChamadaAluno> chamadaAlunos = new ArrayList<>();
        
        for (CriarChamadaDTO.StatusAlunoDTO statusAluno : criarChamadaDTO.getAlunos()) {
            Aluno aluno = alunoRepository.findById(statusAluno.getAlunoId())
                    .orElseThrow(() -> new RuntimeException("Aluno não encontrado: " + statusAluno.getAlunoId()));
            
            // Verificar se o aluno pertence à turma
            if (!aluno.getTurma().getId().equals(criarChamadaDTO.getTurmaId())) {
                throw new RuntimeException("Aluno " + aluno.getNome() + " não pertence à turma selecionada");
            }
            
            ChamadaAluno chamadaAluno = new ChamadaAluno();
            chamadaAluno.setChamada(chamada);
            chamadaAluno.setAluno(aluno);
            chamadaAluno.setPresente(statusAluno.getPresente());
            
            chamadaAlunos.add(chamadaAluno);
        }
        
        chamadaAlunoRepository.saveAll(chamadaAlunos);
        
        // Retornar o DTO da chamada criada
        return buscarChamadaPorId(chamada.getId());
    }
    
    /**
     * Buscar chamada por ID com todos os detalhes
     */
    @Transactional(readOnly = true)
    public ChamadaDTO buscarChamadaPorId(Long id) {
        Chamada chamada = chamadaRepository.findByIdWithTurma(id)
                .orElseThrow(() -> new RuntimeException("Chamada não encontrada"));
        
        // Buscar os registros de presença/falta dos alunos
        List<ChamadaAluno> chamadaAlunos = chamadaAlunoRepository.findByChamadaIdWithAluno(id);
        
        return convertToDTO(chamada, chamadaAlunos);
    }
    
    /**
     * Listar todas as chamadas
     */
    @Transactional(readOnly = true)
    public List<ChamadaDTO> listarChamadas() {
        List<Chamada> chamadas = chamadaRepository.findAllByOrderByDataChamadaDesc();
        return chamadas.stream()
                .map(this::convertToDTOSimples)
                .collect(Collectors.toList());
    }
    
    /**
     * Listar chamadas por turma
     */
    @Transactional(readOnly = true)
    public List<ChamadaDTO> listarChamadasPorTurma(Long turmaId) {
        List<Chamada> chamadas = chamadaRepository.findByTurmaIdWithTurma(turmaId);
        return chamadas.stream()
                .map(this::convertToDTOSimples)
                .collect(Collectors.toList());
    }
    
    /**
     * Listar chamadas por período
     */
    @Transactional(readOnly = true)
    public List<ChamadaDTO> listarChamadasPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        List<Chamada> chamadas = chamadaRepository.findByDataChamadaBetweenOrderByDataChamadaDesc(dataInicio, dataFim);
        return chamadas.stream()
                .map(this::convertToDTOSimples)
                .collect(Collectors.toList());
    }
    
    /**
     * Listar chamadas por turma e período
     */
    @Transactional(readOnly = true)
    public List<ChamadaDTO> listarChamadasPorTurmaEPeriodo(Long turmaId, LocalDate dataInicio, LocalDate dataFim) {
        List<Chamada> chamadas = chamadaRepository.findByTurmaIdAndDataChamadaBetweenOrderByDataChamadaDesc(turmaId, dataInicio, dataFim);
        return chamadas.stream()
                .map(this::convertToDTOSimples)
                .collect(Collectors.toList());
    }
    
    /**
     * Atualizar uma chamada existente
     */
    public ChamadaDTO atualizarChamada(Long id, CriarChamadaDTO atualizarChamadaDTO) {
        Chamada chamada = chamadaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chamada não encontrada"));
        
        // Atualizar dados básicos da chamada
        chamada.setObservacoes(atualizarChamadaDTO.getObservacoes());
        chamada = chamadaRepository.save(chamada);
        
        // Atualizar status de presença dos alunos
        for (CriarChamadaDTO.StatusAlunoDTO statusAluno : atualizarChamadaDTO.getAlunos()) {
            Optional<ChamadaAluno> chamadaAlunoOpt = chamadaAlunoRepository.findByChamadaIdAndAlunoId(id, statusAluno.getAlunoId());
            
            if (chamadaAlunoOpt.isPresent()) {
                ChamadaAluno chamadaAluno = chamadaAlunoOpt.get();
                chamadaAluno.setPresente(statusAluno.getPresente());
                chamadaAlunoRepository.save(chamadaAluno);
            }
        }
        
        return buscarChamadaPorId(id);
    }
    
    /**
     * Remover uma chamada
     */
    public void removerChamada(Long id) {
        if (!chamadaRepository.existsById(id)) {
            throw new RuntimeException("Chamada não encontrada");
        }
        
        // Os registros de ChamadaAluno serão removidos automaticamente devido ao CASCADE
        chamadaRepository.deleteById(id);
    }
    
    /**
     * Buscar chamada por turma e data
     */
    @Transactional(readOnly = true)
    public Optional<ChamadaDTO> buscarChamadaPorTurmaEData(Long turmaId, LocalDate dataChamada) {
        Optional<Chamada> chamadaOpt = chamadaRepository.findByTurmaIdAndDataChamada(turmaId, dataChamada);
        
        if (chamadaOpt.isPresent()) {
            return Optional.of(buscarChamadaPorId(chamadaOpt.get().getId()));
        }
        
        return Optional.empty();
    }
    
    /**
     * Converter entidade Chamada para DTO com detalhes completos
     */
    private ChamadaDTO convertToDTO(Chamada chamada, List<ChamadaAluno> chamadaAlunos) {
        ChamadaDTO dto = new ChamadaDTO();
        dto.setId(chamada.getId());
        dto.setTurmaId(chamada.getTurma().getId());
        dto.setTurmaNome(chamada.getTurma().getNome());
        dto.setTurmaCodigo(chamada.getTurma().getCodigo());
        dto.setDataChamada(chamada.getDataChamada());
        dto.setObservacoes(chamada.getObservacoes());
        dto.setDataCriacao(chamada.getDataCriacao());
        
        // Converter lista de ChamadaAluno para DTO
        List<ChamadaAlunoDTO> alunosDTO = chamadaAlunos.stream()
                .map(this::convertChamadaAlunoToDTO)
                .collect(Collectors.toList());
        
        dto.setAlunos(alunosDTO);
        dto.setTotalAlunos(alunosDTO.size());
        dto.setTotalPresentes((int) alunosDTO.stream().filter(ChamadaAlunoDTO::getPresente).count());
        dto.setTotalFaltas(dto.getTotalAlunos() - dto.getTotalPresentes());
        
        return dto;
    }
    
    /**
     * Converter entidade Chamada para DTO simples (sem lista de alunos)
     */
    private ChamadaDTO convertToDTOSimples(Chamada chamada) {
        ChamadaDTO dto = new ChamadaDTO();
        dto.setId(chamada.getId());
        dto.setTurmaId(chamada.getTurma().getId());
        dto.setTurmaNome(chamada.getTurma().getNome());
        dto.setTurmaCodigo(chamada.getTurma().getCodigo());
        dto.setDataChamada(chamada.getDataChamada());
        dto.setObservacoes(chamada.getObservacoes());
        dto.setDataCriacao(chamada.getDataCriacao());
        
        // Calcular estatísticas básicas
        long totalPresentes = chamadaAlunoRepository.countByChamadaIdAndPresenteTrue(chamada.getId());
        long totalFaltas = chamadaAlunoRepository.countByChamadaIdAndPresenteFalse(chamada.getId());
        
        dto.setTotalPresentes((int) totalPresentes);
        dto.setTotalFaltas((int) totalFaltas);
        dto.setTotalAlunos((int) (totalPresentes + totalFaltas));
        
        return dto;
    }
    
    /**
     * Converter entidade ChamadaAluno para DTO
     */
    private ChamadaAlunoDTO convertChamadaAlunoToDTO(ChamadaAluno chamadaAluno) {
        ChamadaAlunoDTO dto = new ChamadaAlunoDTO();
        dto.setId(chamadaAluno.getId());
        dto.setChamadaId(chamadaAluno.getChamada().getId());
        dto.setAlunoId(chamadaAluno.getAluno().getId());
        dto.setAlunoNome(chamadaAluno.getAluno().getNome());
        dto.setAlunoMatricula(chamadaAluno.getAluno().getMatricula());
        dto.setAlunoEmail(chamadaAluno.getAluno().getEmail());
        dto.setPresente(chamadaAluno.getPresente());
        dto.setDataRegistro(chamadaAluno.getDataRegistro());
        
        return dto;
    }
}

