package com.my.hotel.bl;

import com.my.hotel.common.Constants;
import com.my.hotel.dto.CustomerDto;
import com.my.hotel.service.CustomerService;
import com.my.hotel.utils.Utilities;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ImportCustomerBL {

    private final CustomerService customerService;

    public boolean importCustomer(List<CustomerDto> customers) {
        return customerService.saveCustomers(customers);
    }

    public List<CustomerDto> previewFile(InputStream excelFile, String sheetName) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(excelFile)) {

            // Specify the sheet name or index where the table is located
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new IOException("Invalid file format: missing the sheet with name: " + sheetName);
            }

            // Search for the table's starting cell based on a dynamic condition
            Row startRow = findTableStartRow(sheet);
            if (startRow == null) {
                throw new IOException("Invalid file format: missing the cell with value: 'HO'!");
            }
            // Mapping the header of table
            Map<String, Integer> headerMap = getHeaderMap(startRow);
            if (!isValidHeaderMap(headerMap)) {
                throw new IOException("Invalid file format: invalid Header map, it should be "
                        + Constants.CUSTOMER_IMPORT_HEADER.CUSTOMER_IMPORT_HEADER_SET);
            }
            // Search for the table data's end row
            Integer tableEndRowIndex = findTableEndRow(sheet, startRow, headerMap.get(Constants.CUSTOMER_IMPORT_HEADER.FIRST_NAME));
            if (tableEndRowIndex == null) {
                throw new IOException("Invalid file format: missing end row at column 'HO'!");
            }

            // Read the table's data
            return readTableData(sheet, startRow, tableEndRowIndex, headerMap);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // Implement a method to find the starting cell of the table
    private static Row findTableStartRow(Sheet sheet) {
        // Iterate through rows and columns to find the cell with "HO"
        for (Row row : sheet) {
            for (Cell cell : row) {
                if (cell.getCellType() == CellType.STRING &&
                        Constants.CUSTOMER_IMPORT_HEADER.FIRST_NAME.equals(cell.getStringCellValue())) {
                    return row;
                }
            }
        }
        return null; // "HO" not found
    }

    private static Integer findTableEndRow(Sheet sheet, Row startRow, Integer indexFirstName) {
        int startRowNum = startRow.getRowNum();
        for (int rowIndex = startRowNum + 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            Cell cell = row.getCell(indexFirstName);
            if (Utilities.isEmptyString(getCellValueAsString(cell))) {
                return rowIndex;
            }
        }
        return null;
    }

    private static Map<String, Integer> getHeaderMap(Row row) {
        Map<String, Integer> headerMap = new HashMap<>();

        for (int colIndex = 0; colIndex < row.getLastCellNum(); colIndex++) {
            Cell cell = row.getCell(colIndex);
            if (cell != null) {
                // Read cell value and add it to the row data
                String cellValueAsString = getCellValueAsString(cell);
                if (Constants.CUSTOMER_IMPORT_HEADER.CUSTOMER_IMPORT_HEADER_SET.contains(cellValueAsString)) {
                    headerMap.put(cellValueAsString, colIndex);
                }
            }
        }
        return headerMap;
    }


    // Read the table's data based on the starting cell
    private static List<CustomerDto> readTableData(Sheet sheet, Row startRow, Integer endRowIndex, Map<String, Integer> headerMap) {
        List<CustomerDto> tableData = new ArrayList<>();
        if (startRow != null) {
            int startRowIndex = startRow.getRowNum();
//            int startCol = startRow.getColumnIndex();

            // Iterate through rows and columns to read the table data
            for (int rowIndex = startRowIndex + 1; rowIndex < endRowIndex; rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                CustomerDto rowData = new CustomerDto();
                // If invalid ID then skip the row
                Cell cellId = row.getCell(headerMap.get(Constants.CUSTOMER_IMPORT_HEADER.ID));
                String cellIdValue = getCellValueAsString(cellId);
                if (Utilities.isEmptyString(cellIdValue) || !isValidId(cellIdValue)) {
                    continue;
                }
                // Set the value for Customer based on header
                for (Map.Entry<String, Integer> entry : headerMap.entrySet()) {
                    Cell cell = row.getCell(entry.getValue());
                    String cellValue = getCellValueAsString(cell);
                    switch (entry.getKey()) {
                        case Constants.CUSTOMER_IMPORT_HEADER.ID:
                            rowData.setId(cellValue);
                            break;
                        case Constants.CUSTOMER_IMPORT_HEADER.FIRST_NAME:
                            rowData.setFirstName(cellValue);
                            break;
                        case Constants.CUSTOMER_IMPORT_HEADER.LAST_NAME:
                            rowData.setLastName(cellValue);
                            break;
                        case Constants.CUSTOMER_IMPORT_HEADER.PHONE_NUMBER:
                            rowData.setPhoneNumber(cellValue);
                            break;
                        case Constants.CUSTOMER_IMPORT_HEADER.EMAIL:
                            rowData.setEmail(cellValue);
                            break;
                        case Constants.CUSTOMER_IMPORT_HEADER.ADDRESS:
                            rowData.setAddress(cellValue);
                            break;
                        case Constants.CUSTOMER_IMPORT_HEADER.TAX_NUMBER:
                            rowData.setTaxNumber(cellValue);
                            break;
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
    }

    // Check valid Header Map
    private static boolean isValidHeaderMap(Map<String, Integer> headerMap) {
        Set<String> keySet = headerMap.keySet();
        return keySet.equals(Constants.CUSTOMER_IMPORT_HEADER.CUSTOMER_IMPORT_HEADER_SET);
    }

    // Check the valid ID
    private static boolean isValidId(String input) {
        // Check if the input is not null and contains only digits
        if (input == null || !input.matches("\\d+")) {
            return false;
        }
        int length = input.length();
        // Check the length of the input
        return length >= 9 && length <= 12;
    }
}
