package com.controlepresenca.service;

import com.controlepresenca.dto.PresencaDTO;
import com.controlepresenca.dto.AlunoDTO;
import com.controlepresenca.dto.TurmaDTO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Service para geração de relatórios em CSV e Excel
 * 
 * Fornece funcionalidades para exportar dados do sistema
 */
@Service
public class RelatorioService {

    @Autowired
    private PresencaService presencaService;

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private TurmaService turmaService;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Gera relatório de presenças em formato CSV
     */
    public String gerarRelatorioPresencasCSV(Long turmaId, LocalDate dataInicio, LocalDate dataFim) throws IOException {
        List<PresencaDTO> presencas = presencaService.gerarRelatorio(turmaId, dataInicio, dataFim);

        StringWriter stringWriter = new StringWriter();
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader("Data", "Hora", "Turma", "Código Turma", "Aluno", "Matrícula", "Observações")
                .build();

        try (CSVPrinter csvPrinter = new CSVPrinter(stringWriter, csvFormat)) {
            for (PresencaDTO presenca : presencas) {
                csvPrinter.printRecord(
                        presenca.getDataPresenca().format(dateFormatter),
                        presenca.getHoraPresenca().format(timeFormatter),
                        presenca.getTurmaNome(),
                        presenca.getTurmaCodigo(),
                        presenca.getAlunoNome(),
                        presenca.getAlunoMatricula(),
                        presenca.getObservacoes() != null ? presenca.getObservacoes() : ""
                );
            }
        }

        return stringWriter.toString();
    }

    /**
     * Gera relatório de presenças em formato Excel
     */
    public byte[] gerarRelatorioPresencasExcel(Long turmaId, LocalDate dataInicio, LocalDate dataFim) throws IOException {
        List<PresencaDTO> presencas = presencaService.gerarRelatorio(turmaId, dataInicio, dataFim);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Relatório de Presenças");

            // Criar estilo para cabeçalho
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Criar cabeçalho
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Data", "Hora", "Turma", "Código Turma", "Aluno", "Matrícula", "Observações"};
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Preencher dados
            int rowNum = 1;
            for (PresencaDTO presenca : presencas) {
                Row row = sheet.createRow(rowNum++);
                
                row.createCell(0).setCellValue(presenca.getDataPresenca().format(dateFormatter));
                row.createCell(1).setCellValue(presenca.getHoraPresenca().format(timeFormatter));
                row.createCell(2).setCellValue(presenca.getTurmaNome());
                row.createCell(3).setCellValue(presenca.getTurmaCodigo());
                row.createCell(4).setCellValue(presenca.getAlunoNome());
                row.createCell(5).setCellValue(presenca.getAlunoMatricula());
                row.createCell(6).setCellValue(presenca.getObservacoes() != null ? presenca.getObservacoes() : "");
            }

            // Ajustar largura das colunas
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Converter para bytes
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    /**
     * Gera relatório de alunos em formato CSV
     */
    public String gerarRelatorioAlunosCSV(Long turmaId) throws IOException {
        List<AlunoDTO> alunos = turmaId != null ? 
                alunoService.listarAlunosPorTurma(turmaId) : 
                alunoService.listarAlunosAtivos();

        StringWriter stringWriter = new StringWriter();
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader("Nome", "Matrícula", "Email", "Turma", "Código Turma", "Total Presenças", "Status")
                .build();

        try (CSVPrinter csvPrinter = new CSVPrinter(stringWriter, csvFormat)) {
            for (AlunoDTO aluno : alunos) {
                csvPrinter.printRecord(
                        aluno.getNome(),
                        aluno.getMatricula(),
                        aluno.getEmail(),
                        aluno.getTurmaNome(),
                        aluno.getTurmaCodigo(),
                        aluno.getTotalPresencas(),
                        aluno.getAtivo() ? "Ativo" : "Inativo"
                );
            }
        }

        return stringWriter.toString();
    }

    /**
     * Gera relatório de alunos em formato Excel
     */
    public byte[] gerarRelatorioAlunosExcel(Long turmaId) throws IOException {
        List<AlunoDTO> alunos = turmaId != null ? 
                alunoService.listarAlunosPorTurma(turmaId) : 
                alunoService.listarAlunosAtivos();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Relatório de Alunos");

            // Criar estilo para cabeçalho
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Criar cabeçalho
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Nome", "Matrícula", "Email", "Turma", "Código Turma", "Total Presenças", "Status"};
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Preencher dados
            int rowNum = 1;
            for (AlunoDTO aluno : alunos) {
                Row row = sheet.createRow(rowNum++);
                
                row.createCell(0).setCellValue(aluno.getNome());
                row.createCell(1).setCellValue(aluno.getMatricula());
                row.createCell(2).setCellValue(aluno.getEmail());
                row.createCell(3).setCellValue(aluno.getTurmaNome());
                row.createCell(4).setCellValue(aluno.getTurmaCodigo());
                row.createCell(5).setCellValue(aluno.getTotalPresencas());
                row.createCell(6).setCellValue(aluno.getAtivo() ? "Ativo" : "Inativo");
            }

            // Ajustar largura das colunas
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Converter para bytes
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    /**
     * Gera relatório de turmas em formato CSV
     */
    public String gerarRelatorioTurmasCSV() throws IOException {
        List<TurmaDTO> turmas = turmaService.listarTurmasAtivas();

        StringWriter stringWriter = new StringWriter();
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader("Nome", "Código", "Descrição", "Total Alunos", "Total Presenças", "Status")
                .build();

        try (CSVPrinter csvPrinter = new CSVPrinter(stringWriter, csvFormat)) {
            for (TurmaDTO turma : turmas) {
                csvPrinter.printRecord(
                        turma.getNome(),
                        turma.getCodigo(),
                        turma.getDescricao() != null ? turma.getDescricao() : "",
                        turma.getTotalAlunos(),
                        turma.getTotalPresencas(),
                        turma.getAtiva() ? "Ativa" : "Inativa"
                );
            }
        }

        return stringWriter.toString();
    }

    /**
     * Gera relatório de turmas em formato Excel
     */
    public byte[] gerarRelatorioTurmasExcel() throws IOException {
        List<TurmaDTO> turmas = turmaService.listarTurmasAtivas();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Relatório de Turmas");

            // Criar estilo para cabeçalho
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Criar cabeçalho
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Nome", "Código", "Descrição", "Total Alunos", "Total Presenças", "Status"};
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Preencher dados
            int rowNum = 1;
            for (TurmaDTO turma : turmas) {
                Row row = sheet.createRow(rowNum++);
                
                row.createCell(0).setCellValue(turma.getNome());
                row.createCell(1).setCellValue(turma.getCodigo());
                row.createCell(2).setCellValue(turma.getDescricao() != null ? turma.getDescricao() : "");
                row.createCell(3).setCellValue(turma.getTotalAlunos());
                row.createCell(4).setCellValue(turma.getTotalPresencas());
                row.createCell(5).setCellValue(turma.getAtiva() ? "Ativa" : "Inativa");
            }

            // Ajustar largura das colunas
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Converter para bytes
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
}

