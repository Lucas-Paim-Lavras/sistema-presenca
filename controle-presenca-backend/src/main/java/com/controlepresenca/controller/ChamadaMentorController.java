package com.controlepresenca.controller;

import com.controlepresenca.dto.ChamadaMentorDTO;
import com.controlepresenca.dto.CriarChamadaMentorDTO;
import com.controlepresenca.service.ChamadaMentorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Controller REST para operações relacionadas a Chamadas de Mentores
 */
@RestController
@RequestMapping("/api/chamadas-mentores")
@CrossOrigin(origins = "*")
public class ChamadaMentorController {
    
    @Autowired
    private ChamadaMentorService chamadaMentorService;
    
    /**
     * Criar nova chamada de mentor
     * POST /api/chamadas-mentores
     */
    @PostMapping
    public ResponseEntity<ChamadaMentorDTO> criarChamada(@RequestBody CriarChamadaMentorDTO criarChamadaDTO) {
        try {
            ChamadaMentorDTO chamada = chamadaMentorService.criarChamada(criarChamadaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(chamada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Listar todas as chamadas de mentor
     * GET /api/chamadas-mentores
     */
    @GetMapping
    public ResponseEntity<List<ChamadaMentorDTO>> listarTodas() {
        try {
            List<ChamadaMentorDTO> chamadas = chamadaMentorService.listarTodas();
            return ResponseEntity.ok(chamadas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Buscar chamada de mentor por ID
     * GET /api/chamadas-mentores/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ChamadaMentorDTO> buscarPorId(@PathVariable Long id) {
        try {
            ChamadaMentorDTO chamada = chamadaMentorService.buscarPorId(id);
            return ResponseEntity.ok(chamada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Buscar chamada de mentor por data
     * GET /api/chamadas-mentores/data/{data}
     */
    @GetMapping("/data/{data}")
    public ResponseEntity<ChamadaMentorDTO> buscarPorData(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        try {
            Optional<ChamadaMentorDTO> chamadaOpt = chamadaMentorService.buscarPorData(data);
            
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
     * Listar chamadas de mentor por período
     * GET /api/chamadas-mentores/periodo?dataInicio=2024-01-01&dataFim=2024-01-31
     */
    @GetMapping("/periodo")
    public ResponseEntity<List<ChamadaMentorDTO>> listarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        try {
            List<ChamadaMentorDTO> chamadas = chamadaMentorService.listarPorPeriodo(dataInicio, dataFim);
            return ResponseEntity.ok(chamadas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Atualizar chamada de mentor
     * PUT /api/chamadas-mentores/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ChamadaMentorDTO> atualizar(@PathVariable Long id, @RequestBody CriarChamadaMentorDTO criarChamadaDTO) {
        try {
            ChamadaMentorDTO chamadaAtualizada = chamadaMentorService.atualizar(id, criarChamadaDTO);
            return ResponseEntity.ok(chamadaAtualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Remover chamada de mentor
     * DELETE /api/chamadas-mentores/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        try {
            chamadaMentorService.remover(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Obter estatísticas de chamadas de mentor
     * GET /api/chamadas-mentores/estatisticas
     */
    @GetMapping("/estatisticas")
    public ResponseEntity<ChamadaMentorService.ChamadaMentorEstatisticasDTO> obterEstatisticas() {
        try {
            ChamadaMentorService.ChamadaMentorEstatisticasDTO stats = chamadaMentorService.obterEstatisticas();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

