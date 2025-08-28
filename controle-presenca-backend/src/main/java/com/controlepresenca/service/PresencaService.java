package com.controlepresenca.service;

import com.controlepresenca.dto.PresencaDTO;
import com.controlepresenca.entity.Aluno;
import com.controlepresenca.entity.Presenca;
import com.controlepresenca.entity.Turma;
import com.controlepresenca.repository.AlunoRepository;
import com.controlepresenca.repository.PresencaRepository;
import com.controlepresenca.repository.TurmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service para gerenciar operações relacionadas a Presenças
 * 
 * Contém a lógica de negócio para CRUD de presenças
 */
@Service
@Transactional
public class PresencaService {

    @Autowired
    private PresencaRepository presencaRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private TurmaRepository turmaRepository;

    /**
     * Lista todas as presenças
     */
    public List<PresencaDTO> listarTodasPresencas() {
        List<Presenca> presencas = presencaRepository.findAllByOrderByDataPresencaDescHoraPresencaDesc();
        return presencas.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lista presenças por turma
     */
    public List<PresencaDTO> listarPresencasPorTurma(Long turmaId) {
        List<Presenca> presencas = presencaRepository.findByTurmaIdOrderByDataPresencaDescHoraPresencaDesc(turmaId);
        return presencas.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lista presenças por aluno
     */
    public List<PresencaDTO> listarPresencasPorAluno(Long alunoId) {
        List<Presenca> presencas = presencaRepository.findByAlunoId(alunoId);
        return presencas.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lista presenças por data
     */
    public List<PresencaDTO> listarPresencasPorData(LocalDate data) {
        List<Presenca> presencas = presencaRepository.findByDataPresenca(data);
        return presencas.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lista presenças por turma e data
     */
    public List<PresencaDTO> listarPresencasPorTurmaEData(Long turmaId, LocalDate data) {
        List<Presenca> presencas = presencaRepository.findByTurmaIdAndDataPresenca(turmaId, data);
        return presencas.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lista presenças por período
     */
    public List<PresencaDTO> listarPresencasPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        List<Presenca> presencas = presencaRepository.findByDataPresencaBetween(dataInicio, dataFim);
        return presencas.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca presença por ID
     */
    public Optional<PresencaDTO> buscarPorId(Long id) {
        Optional<Presenca> presenca = presencaRepository.findById(id);
        return presenca.map(this::convertToDTO);
    }

    /**
     * Registra uma nova presença
     */
    public PresencaDTO registrarPresenca(PresencaDTO presencaDTO) {
        // Validar se o aluno existe
        Optional<Aluno> aluno = alunoRepository.findById(presencaDTO.getAlunoId());
        if (!aluno.isPresent()) {
            throw new RuntimeException("Aluno não encontrado com ID: " + presencaDTO.getAlunoId());
        }

        // Validar se a turma existe
        Optional<Turma> turma = turmaRepository.findById(presencaDTO.getTurmaId());
        if (!turma.isPresent()) {
            throw new RuntimeException("Turma não encontrada com ID: " + presencaDTO.getTurmaId());
        }

        // Validar se o aluno pertence à turma
        if (!aluno.get().getTurma().getId().equals(presencaDTO.getTurmaId())) {
            throw new RuntimeException("Aluno não pertence à turma especificada");
        }

        // Verificar se já existe presença para este aluno na data especificada
        LocalDate dataPresenca = presencaDTO.getDataPresenca() != null ? 
                                presencaDTO.getDataPresenca() : LocalDate.now();
        
        if (presencaRepository.existsByAlunoIdAndDataPresenca(presencaDTO.getAlunoId(), dataPresenca)) {
            throw new RuntimeException("Já existe presença registrada para este aluno na data: " + dataPresenca);
        }

        Presenca presenca = convertToEntity(presencaDTO, aluno.get(), turma.get());
        presenca = presencaRepository.save(presenca);
        return convertToDTO(presenca);
    }

    /**
     * Atualiza uma presença existente
     */
    public PresencaDTO atualizarPresenca(Long id, PresencaDTO presencaDTO) {
        Optional<Presenca> presencaExistente = presencaRepository.findById(id);
        
        if (!presencaExistente.isPresent()) {
            throw new RuntimeException("Presença não encontrada com ID: " + id);
        }

        // Validar se o aluno existe
        Optional<Aluno> aluno = alunoRepository.findById(presencaDTO.getAlunoId());
        if (!aluno.isPresent()) {
            throw new RuntimeException("Aluno não encontrado com ID: " + presencaDTO.getAlunoId());
        }

        // Validar se a turma existe
        Optional<Turma> turma = turmaRepository.findById(presencaDTO.getTurmaId());
        if (!turma.isPresent()) {
            throw new RuntimeException("Turma não encontrada com ID: " + presencaDTO.getTurmaId());
        }

        Presenca presenca = presencaExistente.get();
        presenca.setAluno(aluno.get());
        presenca.setTurma(turma.get());
        presenca.setDataPresenca(presencaDTO.getDataPresenca());
        presenca.setHoraPresenca(presencaDTO.getHoraPresenca());
        presenca.setObservacoes(presencaDTO.getObservacoes());

        presenca = presencaRepository.save(presenca);
        return convertToDTO(presenca);
    }

    /**
     * Remove uma presença
     */
    public void removerPresenca(Long id) {
        if (!presencaRepository.existsById(id)) {
            throw new RuntimeException("Presença não encontrada com ID: " + id);
        }
        presencaRepository.deleteById(id);
    }

    /**
     * Registra presença rápida (data e hora atuais)
     */
    public PresencaDTO registrarPresencaRapida(Long alunoId, Long turmaId) {
        PresencaDTO presencaDTO = new PresencaDTO();
        presencaDTO.setAlunoId(alunoId);
        presencaDTO.setTurmaId(turmaId);
        presencaDTO.setDataPresenca(LocalDate.now());
        presencaDTO.setHoraPresenca(LocalTime.now());
        
        return registrarPresenca(presencaDTO);
    }

    /**
     * Gera relatório de presenças
     */
    public List<PresencaDTO> gerarRelatorio(Long turmaId, LocalDate dataInicio, LocalDate dataFim) {
        List<Presenca> presencas = presencaRepository.findRelatorioPresencas(turmaId, dataInicio, dataFim);
        return presencas.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Conta presenças por turma
     */
    public Long contarPresencasPorTurma(Long turmaId) {
        return presencaRepository.countByTurmaId(turmaId);
    }

    /**
     * Conta presenças por aluno
     */
    public Long contarPresencasPorAluno(Long alunoId) {
        return presencaRepository.countByAlunoId(alunoId);
    }

    /**
     * Converte Entity para DTO
     */
    private PresencaDTO convertToDTO(Presenca presenca) {
        PresencaDTO dto = new PresencaDTO();
        dto.setId(presenca.getId());
        dto.setAlunoId(presenca.getAluno().getId());
        dto.setTurmaId(presenca.getTurma().getId());
        dto.setAlunoNome(presenca.getAluno().getNome());
        dto.setAlunoMatricula(presenca.getAluno().getMatricula());
        dto.setTurmaNome(presenca.getTurma().getNome());
        dto.setTurmaCodigo(presenca.getTurma().getCodigo());
        dto.setDataPresenca(presenca.getDataPresenca());
        dto.setHoraPresenca(presenca.getHoraPresenca());
        dto.setDataHoraRegistro(presenca.getDataHoraRegistro());
        dto.setObservacoes(presenca.getObservacoes());
        
        return dto;
    }

    /**
     * Converte DTO para Entity
     */
    private Presenca convertToEntity(PresencaDTO dto, Aluno aluno, Turma turma) {
        Presenca presenca = new Presenca();
        presenca.setAluno(aluno);
        presenca.setTurma(turma);
        presenca.setDataPresenca(dto.getDataPresenca() != null ? dto.getDataPresenca() : LocalDate.now());
        presenca.setHoraPresenca(dto.getHoraPresenca() != null ? dto.getHoraPresenca() : LocalTime.now());
        presenca.setObservacoes(dto.getObservacoes());
        return presenca;
    }
}

