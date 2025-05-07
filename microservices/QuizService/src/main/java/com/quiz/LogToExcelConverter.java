package com.quiz;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import org.apache.poi.ss.usermodel.Row;

public class LogToExcelConverter {

    public static void main(String[] args) {
        String logFilePath = "/Users/rishabhsharma/Downloads/payload_stats.txt";  // Path to your uploaded log file
        String excelFilePath = "payload_stats2.xlsx"; // Output Excel file

        List<Map<String, String>> records = parseLogFile(logFilePath);
        if (!records.isEmpty()) {
            writeToExcel(records, excelFilePath);
            System.out.println("Excel file created successfully: " + excelFilePath);
        } else {
            System.out.println("No valid data found in the log file.");
        }
    }

    private static List<Map<String, String>> parseLogFile(String filePath) {
        List<Map<String, String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            Map<String, String> currentRecord = new LinkedHashMap<>();
            Pattern pattern = Pattern.compile("^(.*?):\\s*([\\d.]+)$|^(xmlPerPayloadTransformationTime|perPayloadDbExecutionTime)\\s*([\\d.]+)$");

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("----")) {
                    if (!currentRecord.isEmpty()) {
                        records.add(new LinkedHashMap<>(currentRecord));
                        currentRecord.clear();
                    }
                } else if (line.startsWith("Captured Payload Stats:")) {
                    currentRecord = new LinkedHashMap<>();
                } else {
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.matches()) {
                        String key = matcher.group(1) != null ? matcher.group(1).trim() : matcher.group(3).trim();
                        String value = matcher.group(2) != null ? matcher.group(2).trim() : matcher.group(4).trim();
                        currentRecord.put(key, value);
                    }
                }
            }
            if (!currentRecord.isEmpty()) {
                records.add(currentRecord);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }

    private static void writeToExcel(List<Map<String, String>> records, String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Payload Stats");
            int rowNum = 0;

            // Create header row
            Row headerRow = sheet.createRow(rowNum++);
            Map<String, String> firstRecord = records.get(0);
            List<String> headers = new ArrayList<>(firstRecord.keySet());
            for (int i = 0; i < headers.size(); i++) {
                headerRow.createCell(i).setCellValue(headers.get(i));
            }

            // Write data rows
            for (Map<String, String> record : records) {
                Row row = sheet.createRow(rowNum++);
                for (int i = 0; i < headers.size(); i++) {
                    row.createCell(i).setCellValue(record.getOrDefault(headers.get(i), ""));
                }
            }

            // Save to Excel file
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
