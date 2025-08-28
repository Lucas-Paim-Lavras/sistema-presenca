package com.controlepresenca.controller;

import com.controlepresenca.dto.AlunoDTO;
import com.controlepresenca.service.AlunoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller REST para gerenciar Alunos
 * 
 * Endpoints:
 * GET /alunos - Lista todos os alunos ativos
 * GET /alunos/{id} - Busca aluno por ID
 * GET /alunos/matricula/{matricula} - Busca aluno por matrícula
 * GET /alunos/turma/{turmaId} - Lista alunos por turma
 * POST /alunos - Cria novo aluno
 * PUT /alunos/{id} - Atualiza aluno
 * DELETE /alunos/{id} - Remove aluno (soft delete)
 */
@RestController
@RequestMapping("/alunos")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000"})
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    /**
     * Lista todos os alunos ativos
     */
    @GetMapping
    public ResponseEntity<List<AlunoDTO>> listarAlunos() {
        try {
            List<AlunoDTO> alunos = alunoService.listarAlunosAtivos();
            return ResponseEntity.ok(alunos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Lista todos os alunos (incluindo inativos)
     */
    @GetMapping("/todos")
    public ResponseEntity<List<AlunoDTO>> listarTodosAlunos() {
        try {
            List<AlunoDTO> alunos = alunoService.listarTodosAlunos();
            return ResponseEntity.ok(alunos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Lista alunos por turma
     */
    @GetMapping("/turma/{turmaId}")
    public ResponseEntity<List<AlunoDTO>> listarAlunosPorTurma(@PathVariable Long turmaId) {
        try {
            List<AlunoDTO> alunos = alunoService.listarAlunosPorTurma(turmaId);
            return ResponseEntity.ok(alunos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Busca aluno por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlunoDTO> buscarAlunoPorId(@PathVariable Long id) {
        try {
            Optional<AlunoDTO> aluno = alunoService.buscarPorId(id);
            return aluno.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Busca aluno por matrícula
     */
    @GetMapping("/matricula/{matricula}")
    public ResponseEntity<AlunoDTO> buscarAlunoPorMatricula(@PathVariable String matricula) {
        try {
            Optional<AlunoDTO> aluno = alunoService.buscarPorMatricula(matricula);
            return aluno.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Busca aluno por email
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<AlunoDTO> buscarAlunoPorEmail(@PathVariable String email) {
        try {
            Optional<AlunoDTO> aluno = alunoService.buscarPorEmail(email);
            return aluno.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Busca alunos por nome
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<AlunoDTO>> buscarAlunosPorNome(@RequestParam String nome) {
        try {
            List<AlunoDTO> alunos = alunoService.buscarPorNome(nome);
            return ResponseEntity.ok(alunos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Busca alunos por turma e nome
     */
    @GetMapping("/turma/{turmaId}/buscar")
    public ResponseEntity<List<AlunoDTO>> buscarAlunosPorTurmaENome(
            @PathVariable Long turmaId, 
            @RequestParam String nome) {
        try {
            List<AlunoDTO> alunos = alunoService.buscarPorTurmaENome(turmaId, nome);
            return ResponseEntity.ok(alunos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Cria um novo aluno
     */
    @PostMapping
    public ResponseEntity<?> criarAluno(@Valid @RequestBody AlunoDTO alunoDTO) {
        try {
            AlunoDTO alunoCriado = alunoService.criarAluno(alunoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(alunoCriado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Erro interno do servidor");
        }
    }

    /**
     * Atualiza um aluno existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarAluno(@PathVariable Long id, 
                                           @Valid @RequestBody AlunoDTO alunoDTO) {
        try {
            AlunoDTO alunoAtualizado = alunoService.atualizarAluno(id, alunoDTO);
            return ResponseEntity.ok(alunoAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Erro interno do servidor");
        }
    }

    /**
     * Remove um aluno (soft delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removerAluno(@PathVariable Long id) {
        try {
            alunoService.removerAluno(id);
            return ResponseEntity.ok().body("Aluno removido com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Erro interno do servidor");
        }
    }

    /**
     * Exclui um aluno permanentemente
     */
    @DeleteMapping("/{id}/permanente")
    public ResponseEntity<?> excluirAluno(@PathVariable Long id) {
        try {
            alunoService.excluirAluno(id);
            return ResponseEntity.ok().body("Aluno excluído permanentemente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Erro interno do servidor");
        }
    }
}

