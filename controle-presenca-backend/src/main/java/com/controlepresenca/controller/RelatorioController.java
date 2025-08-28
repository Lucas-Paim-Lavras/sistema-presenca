package com.controlepresenca.controller;

import com.controlepresenca.service.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Controller REST para geração de relatórios
 * 
 * Endpoints para exportar dados em formato CSV e Excel:
 * GET /relatorios/presencas/csv - Exporta presenças em CSV
 * GET /relatorios/presencas/excel - Exporta presenças em Excel
 * GET /relatorios/alunos/csv - Exporta alunos em CSV
 * GET /relatorios/alunos/excel - Exporta alunos em Excel
 * GET /relatorios/turmas/csv - Exporta turmas em CSV
 * GET /relatorios/turmas/excel - Exporta turmas em Excel
 */
@RestController
@RequestMapping("/relatorios")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000"})
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * Exporta relatório de presenças em formato CSV
     */
    @GetMapping("/presencas/csv")
    public ResponseEntity<?> exportarPresencasCSV(
            @RequestParam(required = false) Long turmaId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        try {
            String csvContent = relatorioService.gerarRelatorioPresencasCSV(turmaId, dataInicio, dataFim);
            
            String filename = "relatorio-presencas";
            if (dataInicio != null && dataFim != null) {
                filename += "_" + dataInicio.format(dateFormatter) + "_" + dataFim.format(dateFormatter);
            }
            filename += ".csv";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/csv"));
            headers.setContentDispositionFormData("attachment", filename);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(csvContent);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao gerar relatório CSV: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor");
        }
    }

    /**
     * Exporta relatório de presenças em formato Excel
     */
    @GetMapping("/presencas/excel")
    public ResponseEntity<?> exportarPresencasExcel(
            @RequestParam(required = false) Long turmaId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        try {
            byte[] excelContent = relatorioService.gerarRelatorioPresencasExcel(turmaId, dataInicio, dataFim);
            
            String filename = "relatorio-presencas";
            if (dataInicio != null && dataFim != null) {
                filename += "_" + dataInicio.format(dateFormatter) + "_" + dataFim.format(dateFormatter);
            }
            filename += ".xlsx";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", filename);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelContent);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao gerar relatório Excel: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor");
        }
    }

    /**
     * Exporta relatório de alunos em formato CSV
     */
    @GetMapping("/alunos/csv")
    public ResponseEntity<?> exportarAlunosCSV(@RequestParam(required = false) Long turmaId) {
        try {
            String csvContent = relatorioService.gerarRelatorioAlunosCSV(turmaId);
            
            String filename = "relatorio-alunos";
            if (turmaId != null) {
                filename += "_turma-" + turmaId;
            }
            filename += ".csv";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/csv"));
            headers.setContentDispositionFormData("attachment", filename);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(csvContent);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao gerar relatório CSV: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor");
        }
    }

    /**
     * Exporta relatório de alunos em formato Excel
     */
    @GetMapping("/alunos/excel")
    public ResponseEntity<?> exportarAlunosExcel(@RequestParam(required = false) Long turmaId) {
        try {
            byte[] excelContent = relatorioService.gerarRelatorioAlunosExcel(turmaId);
            
            String filename = "relatorio-alunos";
            if (turmaId != null) {
                filename += "_turma-" + turmaId;
            }
            filename += ".xlsx";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", filename);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelContent);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao gerar relatório Excel: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor");
        }
    }

    /**
     * Exporta relatório de turmas em formato CSV
     */
    @GetMapping("/turmas/csv")
    public ResponseEntity<?> exportarTurmasCSV() {
        try {
            String csvContent = relatorioService.gerarRelatorioTurmasCSV();
            
            String filename = "relatorio-turmas.csv";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/csv"));
            headers.setContentDispositionFormData("attachment", filename);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(csvContent);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao gerar relatório CSV: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor");
        }
    }

    /**
     * Exporta relatório de turmas em formato Excel
     */
    @GetMapping("/turmas/excel")
    public ResponseEntity<?> exportarTurmasExcel() {
        try {
            byte[] excelContent = relatorioService.gerarRelatorioTurmasExcel();
            
            String filename = "relatorio-turmas.xlsx";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", filename);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelContent);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao gerar relatório Excel: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor");
        }
    }
}

