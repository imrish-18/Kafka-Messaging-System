package com.quiz.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class LogToExcelExporter {

    static class PayloadStat {
        String csVersion;
        String targetCsVersion;
        String propagationTime;
        String integrationTime;
        String pickupTime;
        String xmlTime;
        String dbTime;
        String totalVcpTime;
    }

    public static void main(String[] args) {
        String inputFile = "/Users/rishabhsharma/Downloads/payload_stats.txt"; // <-- replace with full path if needed
        String outputFile = "payload_stats.xlsx";

        List<PayloadStat> statsList = readPayloadStats(inputFile);
        System.out.println("Parsed entries: " + statsList.size());

        if (!statsList.isEmpty()) {
            writeToExcel(statsList, outputFile);
            System.out.println("Excel file created: " + outputFile);
        } else {
            System.out.println("No valid data parsed. Excel file not created.");
        }
    }

    private static List<PayloadStat> readPayloadStats(String fileName) {
        List<PayloadStat> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_16LE))) {
            String line;
            PayloadStat current = null;
            int lineCount = 0;

            while ((line = br.readLine()) != null) {
                lineCount++;
                line = line.trim();
                System.out.println("Line " + lineCount + ": " + line);

                if (line.startsWith("Captured Payload Stats")) {
                    current = new PayloadStat();
                } else if (line.startsWith("Change Sequence Version (CS Version):")) {
                    if (current != null) current.csVersion = extractValue(line);
                } else if (line.startsWith("Target Change Sequence Version")) {
                    if (current != null) current.targetCsVersion = extractValue(line);
                } else if (line.startsWith("Propagation Time per Payload")) {
                    if (current != null) current.propagationTime = extractValue(line);
                } else if (line.startsWith("Integration Time per Payload")) {
                    if (current != null) current.integrationTime = extractValue(line);
                } else if (line.startsWith("Pick-Up Time per Payload")) {
                    if (current != null) current.pickupTime = extractValue(line);
                } else if (line.startsWith("XML Transformation Time per Payload")) {
                    if (current != null) current.xmlTime = extractValue(line);
                } else if (line.startsWith("DB Execution Time per Payload")) {
                    if (current != null) current.dbTime = extractValue(line);
                } else if (line.startsWith("Total VCP time")) {
                    if (current != null) {
                        current.totalVcpTime = extractValue(line);
                        list.add(current);
                        current = null;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }



    private static String extractValue(String line) {
        return line.substring(line.indexOf(":") + 1).trim();
    }

    private static void writeToExcel(List<PayloadStat> stats, String outputFile) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Payload Stats");

            String[] headers = {
                    "CS Version", "Target CS Version", "Propagation Time (s)",
                    "Integration Time (s)", "Pickup Time (s)", "XML Transformation Time (s)",
                    "DB Execution Time (s)", "Total VCP Time (s)"
            };

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            int rowIdx = 1;
            for (PayloadStat ps : stats) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(ps.csVersion);
                row.createCell(1).setCellValue(ps.targetCsVersion);
                row.createCell(2).setCellValue(ps.propagationTime);
                row.createCell(3).setCellValue(ps.integrationTime);
                row.createCell(4).setCellValue(ps.pickupTime);
                row.createCell(5).setCellValue(ps.xmlTime);
                row.createCell(6).setCellValue(ps.dbTime);
                row.createCell(7).setCellValue(ps.totalVcpTime);
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fileOut = new FileOutputStream(outputFile)) {
                workbook.write(fileOut);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
