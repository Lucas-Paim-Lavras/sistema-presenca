package com.controlepresenca.service;

import com.controlepresenca.dto.TurmaDTO;
import com.controlepresenca.entity.Turma;
import com.controlepresenca.repository.TurmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service para gerenciar operações relacionadas a Turmas
 * 
 * Contém a lógica de negócio para CRUD de turmas
 */
@Service
@Transactional
public class TurmaService {

    @Autowired
    private TurmaRepository turmaRepository;

    /**
     * Lista todas as turmas ativas
     */
    public List<TurmaDTO> listarTurmasAtivas() {
        List<Turma> turmas = turmaRepository.findByAtivaTrue();
        return turmas.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lista todas as turmas
     */
    public List<TurmaDTO> listarTodasTurmas() {
        List<Turma> turmas = turmaRepository.findAll();
        return turmas.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca turma por ID
     */
    public Optional<TurmaDTO> buscarPorId(Long id) {
        Optional<Turma> turma = turmaRepository.findById(id);
        return turma.map(this::convertToDTO);
    }

    /**
     * Busca turma por código
     */
    public Optional<TurmaDTO> buscarPorCodigo(String codigo) {
        Optional<Turma> turma = turmaRepository.findByCodigo(codigo);
        return turma.map(this::convertToDTO);
    }

    /**
     * Cria uma nova turma
     */
    public TurmaDTO criarTurma(TurmaDTO turmaDTO) {
        // Validar se o código já existe
        if (turmaRepository.existsByCodigo(turmaDTO.getCodigo())) {
            throw new RuntimeException("Já existe uma turma com o código: " + turmaDTO.getCodigo());
        }

        Turma turma = convertToEntity(turmaDTO);
        turma = turmaRepository.save(turma);
        return convertToDTO(turma);
    }

    /**
     * Atualiza uma turma existente
     */
    public TurmaDTO atualizarTurma(Long id, TurmaDTO turmaDTO) {
        Optional<Turma> turmaExistente = turmaRepository.findById(id);
        
        if (!turmaExistente.isPresent()) {
            throw new RuntimeException("Turma não encontrada com ID: " + id);
        }

        // Validar se o código já existe (excluindo a própria turma)
        if (turmaRepository.existsByCodigoAndIdNot(turmaDTO.getCodigo(), id)) {
            throw new RuntimeException("Já existe uma turma com o código: " + turmaDTO.getCodigo());
        }

        Turma turma = turmaExistente.get();
        turma.setNome(turmaDTO.getNome());
        turma.setCodigo(turmaDTO.getCodigo());
        turma.setDescricao(turmaDTO.getDescricao());
        turma.setAtiva(turmaDTO.getAtiva());

        turma = turmaRepository.save(turma);
        return convertToDTO(turma);
    }

    /**
     * Remove uma turma (soft delete)
     */
    public void removerTurma(Long id) {
        Optional<Turma> turma = turmaRepository.findById(id);
        
        if (!turma.isPresent()) {
            throw new RuntimeException("Turma não encontrada com ID: " + id);
        }

        Turma turmaEntity = turma.get();
        turmaEntity.setAtiva(false);
        turmaRepository.save(turmaEntity);
    }

    /**
     * Remove uma turma permanentemente
     */
    public void excluirTurma(Long id) {
        if (!turmaRepository.existsById(id)) {
            throw new RuntimeException("Turma não encontrada com ID: " + id);
        }
        turmaRepository.deleteById(id);
    }

    /**
     * Busca turmas por nome
     */
    public List<TurmaDTO> buscarPorNome(String nome) {
        List<Turma> turmas = turmaRepository.findByNomeContainingIgnoreCase(nome);
        return turmas.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Converte Entity para DTO
     */
    private TurmaDTO convertToDTO(Turma turma) {
        TurmaDTO dto = new TurmaDTO();
        dto.setId(turma.getId());
        dto.setNome(turma.getNome());
        dto.setCodigo(turma.getCodigo());
        dto.setDescricao(turma.getDescricao());
        dto.setDataCriacao(turma.getDataCriacao());
        dto.setAtiva(turma.getAtiva());
        
        // Adicionar estatísticas
        dto.setTotalAlunos(turmaRepository.countAlunosByTurmaId(turma.getId()).intValue());
        dto.setTotalPresencas(turmaRepository.countPresencasByTurmaId(turma.getId()).intValue());
        
        return dto;
    }

    /**
     * Converte DTO para Entity
     */
    private Turma convertToEntity(TurmaDTO dto) {
        Turma turma = new Turma();
        turma.setNome(dto.getNome());
        turma.setCodigo(dto.getCodigo());
        turma.setDescricao(dto.getDescricao());
        turma.setAtiva(dto.getAtiva() != null ? dto.getAtiva() : true);
        return turma;
    }
}

