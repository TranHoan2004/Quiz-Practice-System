package com.qps.infrastructure.service.excel;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ImportExcelFile {
    public List<List<String>> readExcelFile(InputStream inputStream) throws IOException {
        List<List<String>> data = new ArrayList<>();
        try (var workbook = new XSSFWorkbook(inputStream)) {
            var sheet = workbook.getSheetAt(0); // Lấy sheet đầu tiên
            for (var row : sheet) {
                List<String> rowData = getStrings(row);
                data.add(rowData);
            }
        }
        return data;
    }

    private List<String> getStrings(Row row) {
        List<String> rowData = new ArrayList<>();
        for (var cell : row) {
            switch (cell.getCellType()) {
                case STRING -> rowData.add(cell.getStringCellValue());
                case NUMERIC -> rowData.add(String.valueOf(cell.getNumericCellValue()));
                case BOOLEAN -> rowData.add(String.valueOf(cell.getBooleanCellValue()));
                case FORMULA -> rowData.add(cell.getCellFormula());
                default -> rowData.add("");
            }
        }
        return rowData;
    }

    public List<List<String>> getData(MultipartFile file) throws IOException {
        return readExcelFile(file.getInputStream());
    }
}
