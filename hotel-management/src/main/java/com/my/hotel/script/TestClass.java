package com.my.hotel.script;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TestClass {
    public static void main(String[] args) {
        try (InputStream excelFile = new FileInputStream("SqlScriptMatrix.xlsx");
             Workbook workbook = new XSSFWorkbook(excelFile)) {

            // Specify the sheet name or index where the table is located
            Sheet sheet = workbook.getSheet("Sheet1");

            // Search for the table's starting cell based on a dynamic condition
            Cell startCell = findTableStartCell(sheet);

            // Read the table's data
            List<List<String>> tableData = readTableData(sheet, startCell);

            // Process the table data as needed
            for (List<String> row : tableData) {
                for (String cellValue : row) {
                    System.out.print(cellValue + "\t");
                }
                System.out.println();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Implement a method to find the starting cell of the table based on your criteria
    private static Cell findTableStartCell(Sheet sheet) {
        // Iterate through rows and columns to find the cell with "CMND"
        for (Row row : sheet) {
            for (Cell cell : row) {
                if (cell.getCellType() == CellType.STRING && "CMND".equals(cell.getStringCellValue())) {
                    return cell; // Found the starting cell
                }
            }
        }
        return null; // "CMND" not found
    }

    // Read the table's data based on the starting cell
    private static List<List<String>> readTableData(Sheet sheet, Cell startCell) {
        List<List<String>> tableData = new ArrayList<>();
        if (startCell != null) {
            int startRow = startCell.getRowIndex();
            int startCol = startCell.getColumnIndex();

            // Iterate through rows and columns to read the table data
            for (int rowIndex = startRow; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                List<String> rowData = new ArrayList<>();

                if (row != null) {
                    for (int colIndex = startCol; colIndex < row.getLastCellNum(); colIndex++) {
                        Cell cell = row.getCell(colIndex);
                        if (cell != null) {
                            // Read cell value and add it to the row data
                            rowData.add(getCellValueAsString(cell));
                        } else {
                            // Handle empty cells if needed
                            rowData.add(""); // Or another default value
                        }
                    }
                }

                // Add the row data to the table data
                tableData.add(rowData);
            }
        }
        return tableData;
    }

    // Helper method to get the cell value as a string
    private static String getCellValueAsString(Cell cell) {
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell);
//        if (cell.getCellType() == CellType.STRING) {
//            return cell.getStringCellValue();
//        } else if (cell.getCellType() == CellType.NUMERIC) {
//            return String.valueOf(cell.getNumericCellValue());
//        } else {
//            // Handle other cell types as needed
//            return "";
//        }
    }
}
