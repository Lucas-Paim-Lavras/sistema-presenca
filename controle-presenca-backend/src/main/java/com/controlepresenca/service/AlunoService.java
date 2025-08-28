package com.controlepresenca.service;

import com.controlepresenca.dto.AlunoDTO;
import com.controlepresenca.entity.Aluno;
import com.controlepresenca.entity.Turma;
import com.controlepresenca.repository.AlunoRepository;
import com.controlepresenca.repository.TurmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service para gerenciar operações relacionadas a Alunos
 * 
 * Contém a lógica de negócio para CRUD de alunos
 */
@Service
@Transactional
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private TurmaRepository turmaRepository;

    /**
     * Lista todos os alunos ativos
     */
    public List<AlunoDTO> listarAlunosAtivos() {
        List<Aluno> alunos = alunoRepository.findByAtivoTrue();
        return alunos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lista todos os alunos
     */
    public List<AlunoDTO> listarTodosAlunos() {
        List<Aluno> alunos = alunoRepository.findAll();
        return alunos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lista alunos por turma
     */
    public List<AlunoDTO> listarAlunosPorTurma(Long turmaId) {
        List<Aluno> alunos = alunoRepository.findByTurmaIdAndAtivoTrue(turmaId);
        return alunos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca aluno por ID
     */
    public Optional<AlunoDTO> buscarPorId(Long id) {
        Optional<Aluno> aluno = alunoRepository.findById(id);
        return aluno.map(this::convertToDTO);
    }

    /**
     * Busca aluno por matrícula
     */
    public Optional<AlunoDTO> buscarPorMatricula(String matricula) {
        Optional<Aluno> aluno = alunoRepository.findByMatricula(matricula);
        return aluno.map(this::convertToDTO);
    }

    /**
     * Busca aluno por email
     */
    public Optional<AlunoDTO> buscarPorEmail(String email) {
        Optional<Aluno> aluno = alunoRepository.findByEmail(email);
        return aluno.map(this::convertToDTO);
    }

    /**
     * Cria um novo aluno
     */
    public AlunoDTO criarAluno(AlunoDTO alunoDTO) {
        // Validar se a turma existe
        Optional<Turma> turma = turmaRepository.findById(alunoDTO.getTurmaId());
        if (!turma.isPresent()) {
            throw new RuntimeException("Turma não encontrada com ID: " + alunoDTO.getTurmaId());
        }

        // Validar se a matrícula já existe
        if (alunoRepository.existsByMatricula(alunoDTO.getMatricula())) {
            throw new RuntimeException("Já existe um aluno com a matrícula: " + alunoDTO.getMatricula());
        }

        // Validar se o email já existe
        if (alunoRepository.existsByEmail(alunoDTO.getEmail())) {
            throw new RuntimeException("Já existe um aluno com o email: " + alunoDTO.getEmail());
        }

        Aluno aluno = convertToEntity(alunoDTO, turma.get());
        aluno = alunoRepository.save(aluno);
        return convertToDTO(aluno);
    }

    /**
     * Atualiza um aluno existente
     */
    public AlunoDTO atualizarAluno(Long id, AlunoDTO alunoDTO) {
        Optional<Aluno> alunoExistente = alunoRepository.findById(id);
        
        if (!alunoExistente.isPresent()) {
            throw new RuntimeException("Aluno não encontrado com ID: " + id);
        }

        // Validar se a turma existe
        Optional<Turma> turma = turmaRepository.findById(alunoDTO.getTurmaId());
        if (!turma.isPresent()) {
            throw new RuntimeException("Turma não encontrada com ID: " + alunoDTO.getTurmaId());
        }

        // Validar se a matrícula já existe (excluindo o próprio aluno)
        if (alunoRepository.existsByMatriculaAndIdNot(alunoDTO.getMatricula(), id)) {
            throw new RuntimeException("Já existe um aluno com a matrícula: " + alunoDTO.getMatricula());
        }

        // Validar se o email já existe (excluindo o próprio aluno)
        if (alunoRepository.existsByEmailAndIdNot(alunoDTO.getEmail(), id)) {
            throw new RuntimeException("Já existe um aluno com o email: " + alunoDTO.getEmail());
        }

        Aluno aluno = alunoExistente.get();
        aluno.setNome(alunoDTO.getNome());
        aluno.setMatricula(alunoDTO.getMatricula());
        aluno.setEmail(alunoDTO.getEmail());
        aluno.setTurma(turma.get());
        aluno.setAtivo(alunoDTO.getAtivo());

        aluno = alunoRepository.save(aluno);
        return convertToDTO(aluno);
    }

    /**
     * Remove um aluno (soft delete)
     */
    public void removerAluno(Long id) {
        Optional<Aluno> aluno = alunoRepository.findById(id);
        
        if (!aluno.isPresent()) {
            throw new RuntimeException("Aluno não encontrado com ID: " + id);
        }

        Aluno alunoEntity = aluno.get();
        alunoEntity.setAtivo(false);
        alunoRepository.save(alunoEntity);
    }

    /**
     * Remove um aluno permanentemente
     */
    public void excluirAluno(Long id) {
        if (!alunoRepository.existsById(id)) {
            throw new RuntimeException("Aluno não encontrado com ID: " + id);
        }
        alunoRepository.deleteById(id);
    }

    /**
     * Busca alunos por nome
     */
    public List<AlunoDTO> buscarPorNome(String nome) {
        List<Aluno> alunos = alunoRepository.findByNomeContainingIgnoreCase(nome);
        return alunos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca alunos por turma e nome
     */
    public List<AlunoDTO> buscarPorTurmaENome(Long turmaId, String nome) {
        List<Aluno> alunos = alunoRepository.findByTurmaIdAndNomeContainingIgnoreCase(turmaId, nome);
        return alunos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Converte Entity para DTO
     */
    private AlunoDTO convertToDTO(Aluno aluno) {
        AlunoDTO dto = new AlunoDTO();
        dto.setId(aluno.getId());
        dto.setNome(aluno.getNome());
        dto.setMatricula(aluno.getMatricula());
        dto.setEmail(aluno.getEmail());
        dto.setTurmaId(aluno.getTurma().getId());
        dto.setTurmaNome(aluno.getTurma().getNome());
        dto.setTurmaCodigo(aluno.getTurma().getCodigo());
        dto.setDataCadastro(aluno.getDataCadastro());
        dto.setAtivo(aluno.getAtivo());
        
        // Adicionar estatísticas
        dto.setTotalPresencas(alunoRepository.countPresencasByAlunoId(aluno.getId()).intValue());
        
        return dto;
    }

    /**
     * Converte DTO para Entity
     */
    private Aluno convertToEntity(AlunoDTO dto, Turma turma) {
        Aluno aluno = new Aluno();
        aluno.setNome(dto.getNome());
        aluno.setMatricula(dto.getMatricula());
        aluno.setEmail(dto.getEmail());
        aluno.setTurma(turma);
        aluno.setAtivo(dto.getAtivo() != null ? dto.getAtivo() : true);
        return aluno;
    }
}

