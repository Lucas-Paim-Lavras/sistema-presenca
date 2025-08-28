package com.controlepresenca.controller;

import com.controlepresenca.dto.ChamadaDTO;
import com.controlepresenca.dto.CriarChamadaDTO;
import com.controlepresenca.service.ChamadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Controller REST para operações relacionadas a Chamadas
 */
@RestController
@RequestMapping("/api/chamadas")
@CrossOrigin(origins = "*")
public class ChamadaController {
    
    @Autowired
    private ChamadaService chamadaService;
    
    /**
     * Criar uma nova chamada
     * POST /api/chamadas
     */
    @PostMapping
    public ResponseEntity<ChamadaDTO> criarChamada(@RequestBody CriarChamadaDTO criarChamadaDTO) {
        try {
            ChamadaDTO chamadaDTO = chamadaService.criarChamada(criarChamadaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(chamadaDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Listar todas as chamadas
     * GET /api/chamadas
     */
    @GetMapping
    public ResponseEntity<List<ChamadaDTO>> listarChamadas() {
        try {
            List<ChamadaDTO> chamadas = chamadaService.listarChamadas();
            return ResponseEntity.ok(chamadas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Buscar chamada por ID
     * GET /api/chamadas/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ChamadaDTO> buscarChamadaPorId(@PathVariable Long id) {
        try {
            ChamadaDTO chamadaDTO = chamadaService.buscarChamadaPorId(id);
            return ResponseEntity.ok(chamadaDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Listar chamadas por turma
     * GET /api/chamadas/turma/{turmaId}
     */
    @GetMapping("/turma/{turmaId}")
    public ResponseEntity<List<ChamadaDTO>> listarChamadasPorTurma(@PathVariable Long turmaId) {
        try {
            List<ChamadaDTO> chamadas = chamadaService.listarChamadasPorTurma(turmaId);
            return ResponseEntity.ok(chamadas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Listar chamadas por período
     * GET /api/chamadas/periodo?dataInicio=2024-01-01&dataFim=2024-01-31
     */
    @GetMapping("/periodo")
    public ResponseEntity<List<ChamadaDTO>> listarChamadasPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        try {
            List<ChamadaDTO> chamadas = chamadaService.listarChamadasPorPeriodo(dataInicio, dataFim);
            return ResponseEntity.ok(chamadas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Listar chamadas por turma e período
     * GET /api/chamadas/turma/{turmaId}/periodo?dataInicio=2024-01-01&dataFim=2024-01-31
     */
    @GetMapping("/turma/{turmaId}/periodo")
    public ResponseEntity<List<ChamadaDTO>> listarChamadasPorTurmaEPeriodo(
            @PathVariable Long turmaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        try {
            List<ChamadaDTO> chamadas = chamadaService.listarChamadasPorTurmaEPeriodo(turmaId, dataInicio, dataFim);
            return ResponseEntity.ok(chamadas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Buscar chamada por turma e data
     * GET /api/chamadas/turma/{turmaId}/data/{data}
     */
    @GetMapping("/turma/{turmaId}/data/{data}")
    public ResponseEntity<ChamadaDTO> buscarChamadaPorTurmaEData(
            @PathVariable Long turmaId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        try {
            Optional<ChamadaDTO> chamadaOpt = chamadaService.buscarChamadaPorTurmaEData(turmaId, data);
            
            if (chamadaOpt.isPresent()) {
                return ResponseEntity.ok(chamadaOpt.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Atualizar uma chamada existente
     * PUT /api/chamadas/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ChamadaDTO> atualizarChamada(
            @PathVariable Long id, 
            @RequestBody CriarChamadaDTO atualizarChamadaDTO) {
        try {
            ChamadaDTO chamadaDTO = chamadaService.atualizarChamada(id, atualizarChamadaDTO);
            return ResponseEntity.ok(chamadaDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Remover uma chamada
     * DELETE /api/chamadas/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerChamada(@PathVariable Long id) {
        try {
            chamadaService.removerChamada(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

