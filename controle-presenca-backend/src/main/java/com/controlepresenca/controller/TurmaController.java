package com.controlepresenca.controller;

import com.controlepresenca.dto.TurmaDTO;
import com.controlepresenca.service.TurmaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller REST para gerenciar Turmas
 * 
 * Endpoints:
 * GET /turmas - Lista todas as turmas ativas
 * GET /turmas/{id} - Busca turma por ID
 * GET /turmas/codigo/{codigo} - Busca turma por código
 * POST /turmas - Cria nova turma
 * PUT /turmas/{id} - Atualiza turma
 * DELETE /turmas/{id} - Remove turma (soft delete)
 */
@RestController
@RequestMapping("/turmas")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000"})
public class TurmaController {

    @Autowired
    private TurmaService turmaService;

    /**
     * Lista todas as turmas ativas
     */
    @GetMapping
    public ResponseEntity<List<TurmaDTO>> listarTurmas() {
        try {
            List<TurmaDTO> turmas = turmaService.listarTurmasAtivas();
            return ResponseEntity.ok(turmas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Lista todas as turmas (incluindo inativas)
     */
    @GetMapping("/todas")
    public ResponseEntity<List<TurmaDTO>> listarTodasTurmas() {
        try {
            List<TurmaDTO> turmas = turmaService.listarTodasTurmas();
            return ResponseEntity.ok(turmas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Busca turma por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<TurmaDTO> buscarTurmaPorId(@PathVariable Long id) {
        try {
            Optional<TurmaDTO> turma = turmaService.buscarPorId(id);
            return turma.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Busca turma por código
     */
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<TurmaDTO> buscarTurmaPorCodigo(@PathVariable String codigo) {
        try {
            Optional<TurmaDTO> turma = turmaService.buscarPorCodigo(codigo);
            return turma.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Busca turmas por nome
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<TurmaDTO>> buscarTurmasPorNome(@RequestParam String nome) {
        try {
            List<TurmaDTO> turmas = turmaService.buscarPorNome(nome);
            return ResponseEntity.ok(turmas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Cria uma nova turma
     */
    @PostMapping
    public ResponseEntity<?> criarTurma(@Valid @RequestBody TurmaDTO turmaDTO) {
        try {
            TurmaDTO turmaCriada = turmaService.criarTurma(turmaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(turmaCriada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Erro interno do servidor");
        }
    }

    /**
     * Atualiza uma turma existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarTurma(@PathVariable Long id, 
                                           @Valid @RequestBody TurmaDTO turmaDTO) {
        try {
            TurmaDTO turmaAtualizada = turmaService.atualizarTurma(id, turmaDTO);
            return ResponseEntity.ok(turmaAtualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Erro interno do servidor");
        }
    }

    /**
     * Remove uma turma (soft delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removerTurma(@PathVariable Long id) {
        try {
            turmaService.removerTurma(id);
            return ResponseEntity.ok().body("Turma removida com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Erro interno do servidor");
        }
    }

    /**
     * Exclui uma turma permanentemente
     */
    @DeleteMapping("/{id}/permanente")
    public ResponseEntity<?> excluirTurma(@PathVariable Long id) {
        try {
            turmaService.excluirTurma(id);
            return ResponseEntity.ok().body("Turma excluída permanentemente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Erro interno do servidor");
        }
    }
}

