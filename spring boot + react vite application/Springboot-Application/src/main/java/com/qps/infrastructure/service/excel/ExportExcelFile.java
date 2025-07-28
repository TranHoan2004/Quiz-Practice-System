package com.qps.infrastructure.service.excel;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Component
public class ExportExcelFile<T> {
    private final XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private final List<T> listData;

    public ExportExcelFile(List<T> listData) {
        this.listData = listData;
        workbook = new XSSFWorkbook();
    }

    public ExportExcelFile<T> writeHeaderLine(String[] headers) {
        sheet = workbook.createSheet("data export");
        var row = sheet.createRow(0);
        var style = workbook.createCellStyle();
        var font = workbook.createFont();

        font.setBold(true);
        font.setFontHeight(12);
        font.setFontName("Times New Roman");
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);

        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        for (int i = 0; i < headers.length; i++) {
            createCell(row, i, headers[i], style);
        }

        return this;
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        var cell = row.createCell(columnCount);
        switch (value) {
            case Integer i -> cell.setCellValue(i);
            case Boolean b -> cell.setCellValue(b);
            case Long l -> cell.setCellValue(l);
            case LocalDate localDate -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                cell.setCellValue(localDate.format(formatter));
            }
            case LocalDateTime localDateTime -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                cell.setCellValue(localDateTime.format(formatter));
            }
            case null, default -> cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    public ExportExcelFile<T> writeDataLines(String[] fields, Class<?> clazz) {
        int rowCount = 1;
        var style = workbook.createCellStyle();
        var font = workbook.createFont();

        style.setFont(font);
        font.setFontHeight(12);
        font.setFontName("Times New Roman");
        font.setBold(true);

        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        for (var data : this.listData) {
            var row = sheet.createRow(rowCount++);
            int columnCount = 0;

            for (String fieldName : fields) {
                try {
                    var field = clazz.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    var value = field.get(data);
                    createCell(row, columnCount, value, style);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    log.error(e.getMessage(), e);
                }
                columnCount++;
            }
        }
        return this;
    }

    public void export(HttpServletResponse response) throws IOException {
        var outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

}
