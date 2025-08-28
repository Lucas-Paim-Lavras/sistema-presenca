package com.controlepresenca.controller;

import com.controlepresenca.dto.PresencaDTO;
import com.controlepresenca.service.PresencaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Controller REST para gerenciar Presenças
 * 
 * Endpoints:
 * GET /presencas - Lista todas as presenças
 * GET /presencas/{id} - Busca presença por ID
 * GET /presencas/turma/{turmaId} - Lista presenças por turma
 * GET /presencas/aluno/{alunoId} - Lista presenças por aluno
 * POST /presencas - Registra nova presença
 * POST /presencas/rapida - Registra presença rápida (data/hora atual)
 * PUT /presencas/{id} - Atualiza presença
 * DELETE /presencas/{id} - Remove presença
 */
@RestController
@RequestMapping("/presencas")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000"})
public class PresencaController {

    @Autowired
    private PresencaService presencaService;

    /**
     * Lista todas as presenças
     */
    @GetMapping
    public ResponseEntity<List<PresencaDTO>> listarPresencas() {
        try {
            List<PresencaDTO> presencas = presencaService.listarTodasPresencas();
            return ResponseEntity.ok(presencas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Lista presenças por turma
     */
    @GetMapping("/turma/{turmaId}")
    public ResponseEntity<List<PresencaDTO>> listarPresencasPorTurma(@PathVariable Long turmaId) {
        try {
            List<PresencaDTO> presencas = presencaService.listarPresencasPorTurma(turmaId);
            return ResponseEntity.ok(presencas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Lista presenças por aluno
     */
    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<PresencaDTO>> listarPresencasPorAluno(@PathVariable Long alunoId) {
        try {
            List<PresencaDTO> presencas = presencaService.listarPresencasPorAluno(alunoId);
            return ResponseEntity.ok(presencas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Lista presenças por data
     */
    @GetMapping("/data/{data}")
    public ResponseEntity<List<PresencaDTO>> listarPresencasPorData(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        try {
            List<PresencaDTO> presencas = presencaService.listarPresencasPorData(data);
            return ResponseEntity.ok(presencas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Lista presenças por turma e data
     */
    @GetMapping("/turma/{turmaId}/data/{data}")
    public ResponseEntity<List<PresencaDTO>> listarPresencasPorTurmaEData(
            @PathVariable Long turmaId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        try {
            List<PresencaDTO> presencas = presencaService.listarPresencasPorTurmaEData(turmaId, data);
            return ResponseEntity.ok(presencas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Lista presenças por período
     */
    @GetMapping("/periodo")
    public ResponseEntity<List<PresencaDTO>> listarPresencasPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        try {
            List<PresencaDTO> presencas = presencaService.listarPresencasPorPeriodo(dataInicio, dataFim);
            return ResponseEntity.ok(presencas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Busca presença por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<PresencaDTO> buscarPresencaPorId(@PathVariable Long id) {
        try {
            Optional<PresencaDTO> presenca = presencaService.buscarPorId(id);
            return presenca.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Registra uma nova presença
     */
    @PostMapping
    public ResponseEntity<?> registrarPresenca(@Valid @RequestBody PresencaDTO presencaDTO) {
        try {
            PresencaDTO presencaRegistrada = presencaService.registrarPresenca(presencaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(presencaRegistrada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Erro interno do servidor");
        }
    }

    /**
     * Registra presença rápida (data e hora atuais)
     */
    @PostMapping("/rapida")
    public ResponseEntity<?> registrarPresencaRapida(@RequestBody PresencaDTO presencaDTO) {
        try {
            PresencaDTO presencaRegistrada = presencaService.registrarPresencaRapida(
                    presencaDTO.getAlunoId(), 
                    presencaDTO.getTurmaId()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(presencaRegistrada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Erro interno do servidor");
        }
    }

    /**
     * Atualiza uma presença existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarPresenca(@PathVariable Long id, 
                                              @Valid @RequestBody PresencaDTO presencaDTO) {
        try {
            PresencaDTO presencaAtualizada = presencaService.atualizarPresenca(id, presencaDTO);
            return ResponseEntity.ok(presencaAtualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Erro interno do servidor");
        }
    }

    /**
     * Remove uma presença
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removerPresenca(@PathVariable Long id) {
        try {
            presencaService.removerPresenca(id);
            return ResponseEntity.ok().body("Presença removida com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Erro interno do servidor");
        }
    }

    /**
     * Gera relatório de presenças
     */
    @GetMapping("/relatorio")
    public ResponseEntity<List<PresencaDTO>> gerarRelatorio(
            @RequestParam(required = false) Long turmaId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        try {
            List<PresencaDTO> presencas = presencaService.gerarRelatorio(turmaId, dataInicio, dataFim);
            return ResponseEntity.ok(presencas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Conta presenças por turma
     */
    @GetMapping("/turma/{turmaId}/contar")
    public ResponseEntity<Long> contarPresencasPorTurma(@PathVariable Long turmaId) {
        try {
            Long total = presencaService.contarPresencasPorTurma(turmaId);
            return ResponseEntity.ok(total);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Conta presenças por aluno
     */
    @GetMapping("/aluno/{alunoId}/contar")
    public ResponseEntity<Long> contarPresencasPorAluno(@PathVariable Long alunoId) {
        try {
            Long total = presencaService.contarPresencasPorAluno(alunoId);
            return ResponseEntity.ok(total);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

