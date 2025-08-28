package com.controlepresenca.controller;

import com.controlepresenca.dto.MentorDTO;
import com.controlepresenca.service.MentorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para operações relacionadas a Mentores
 */
@RestController
@RequestMapping("/api/mentores")
@CrossOrigin(origins = "*")
public class MentorController {
    
    @Autowired
    private MentorService mentorService;
    
    /**
     * Listar todos os mentores
     * GET /api/mentores
     */
    @GetMapping
    public ResponseEntity<List<MentorDTO>> listarTodos() {
        try {
            List<MentorDTO> mentores = mentorService.listarTodos();
            return ResponseEntity.ok(mentores);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Listar mentores ativos
     * GET /api/mentores/ativos
     */
    @GetMapping("/ativos")
    public ResponseEntity<List<MentorDTO>> listarAtivos() {
        try {
            List<MentorDTO> mentores = mentorService.listarAtivos();
            return ResponseEntity.ok(mentores);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Listar mentores por tipo
     * GET /api/mentores/tipo/{tipo}
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<MentorDTO>> listarPorTipo(@PathVariable String tipo) {
        try {
            List<MentorDTO> mentores = mentorService.listarPorTipo(tipo);
            return ResponseEntity.ok(mentores);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Buscar mentor por ID
     * GET /api/mentores/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<MentorDTO> buscarPorId(@PathVariable Long id) {
        try {
            MentorDTO mentor = mentorService.buscarPorId(id);
            return ResponseEntity.ok(mentor);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Buscar mentor por email
     * GET /api/mentores/email/{email}
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<MentorDTO> buscarPorEmail(@PathVariable String email) {
        try {
            MentorDTO mentor = mentorService.buscarPorEmail(email);
            return ResponseEntity.ok(mentor);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Buscar mentores por nome
     * GET /api/mentores/buscar?nome=João
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<MentorDTO>> buscarPorNome(@RequestParam String nome) {
        try {
            List<MentorDTO> mentores = mentorService.buscarPorNome(nome);
            return ResponseEntity.ok(mentores);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Criar novo mentor
     * POST /api/mentores
     */
    @PostMapping
    public ResponseEntity<MentorDTO> criar(@RequestBody MentorDTO mentorDTO) {
        try {
            MentorDTO novoMentor = mentorService.criar(mentorDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoMentor);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Atualizar mentor
     * PUT /api/mentores/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<MentorDTO> atualizar(@PathVariable Long id, @RequestBody MentorDTO mentorDTO) {
        try {
            MentorDTO mentorAtualizado = mentorService.atualizar(id, mentorDTO);
            return ResponseEntity.ok(mentorAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Remover mentor (soft delete)
     * DELETE /api/mentores/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        try {
            mentorService.remover(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Reativar mentor
     * PUT /api/mentores/{id}/reativar
     */
    @PutMapping("/{id}/reativar")
    public ResponseEntity<MentorDTO> reativar(@PathVariable Long id) {
        try {
            MentorDTO mentor = mentorService.reativar(id);
            return ResponseEntity.ok(mentor);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Obter estatísticas de mentores
     * GET /api/mentores/estatisticas
     */
    @GetMapping("/estatisticas")
    public ResponseEntity<MentorService.MentorEstatisticasDTO> obterEstatisticas() {
        try {
            MentorService.MentorEstatisticasDTO stats = mentorService.obterEstatisticas();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

