package org.example;

import org.apache.poi.ss.usermodel.*;
import java.io.FileInputStream;
import java.sql.*;

public class ExcelToClob {

    public static void main(String[] args) throws Exception {

        String excelFile = "C:/DATA/data.xls";
        String url = "jdbc:oracle:thin:@//localhost:1521/xe";
        String user = "HR";
        String pass = "hr";
        String url1 = "jdbc:oracle:thin:@10.40.228.52:1523:DELMTNL";
        String user1 = "paybill";
        String pass1 = "Wfmspb_2019";

        StringBuilder fullCsv = new StringBuilder();

        try (FileInputStream fis = new FileInputStream(excelFile);
             Workbook wb = WorkbookFactory.create(fis)) {

            Sheet sheet = wb.getSheetAt(0);
            boolean firstRow = true;

            for (Row row : sheet) {

                if (firstRow) {
                    firstRow = false;
                    continue; // skip header
                }

                StringBuilder rowCsv = new StringBuilder();

                for (Cell cell : row) {
                    switch (cell.getCellType()) {

                        case STRING:
                            rowCsv.append(cell.getStringCellValue());
                            break;

                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                                rowCsv.append(cell.getDateCellValue());
                            } else {
                                rowCsv.append((long) cell.getNumericCellValue());
                            }
                            break;

                        case BOOLEAN:
                            rowCsv.append(cell.getBooleanCellValue());
                            break;

                        case BLANK:
                            rowCsv.append("");
                            break;

                        default:
                            rowCsv.append(cell.toString());
                    }
                    rowCsv.append(",");
                }

                rowCsv.setLength(rowCsv.length() - 1); // remove last comma
                fullCsv.append(rowCsv).append("\n");

                System.out.println("--> " + rowCsv);
            }
        }

        String clobData = fullCsv.toString();

        // ===== ORACLE INSERT =====ssss
        Class.forName("oracle.jdbc.OracleDriver");

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps =
                     conn.prepareStatement("INSERT INTO EXCEL_CLOB (DATA) VALUES (?)")) {

            conn.setAutoCommit(false);

            ps.setString(1, clobData);
            ps.executeUpdate();

            conn.commit();
            System.out.println("SUCCESS: " + clobData.length() + " chars inserted");
        }
    }
}
