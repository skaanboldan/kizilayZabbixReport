package org.kizilay;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelWriter {
    public static void writeExcel(List<Host> hostList) {
        XSSFWorkbook xSSFWorkbook = new XSSFWorkbook();
        Sheet sheet = xSSFWorkbook.createSheet("Hosts");
        Row headerRow = sheet.createRow(0);
        String[] columns = { "Power", "VM Ad", "VM IP Adresi", "Port", "Sistemi", "Sunucu Rol", "Ortam", "Durum", "Message" };
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }
        int rowNum = 1;
        for (Host host : hostList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(host.getPower().booleanValue() ? "On" : "Off");
            row.createCell(1).setCellValue(host.getName());
            row.createCell(2).setCellValue(host.getIp());
            row.createCell(3).setCellValue(host.getPort());
            row.createCell(4).setCellValue(host.getOs());
            row.createCell(5).setCellValue(host.getRole());
            row.createCell(6).setCellValue(host.getEnvironment());
            row.createCell(7).setCellValue(host.getStatus());
            row.createCell(8).setCellValue(host.getMessage());
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
        Date date = new Date();
        try {
            FileOutputStream fileOut = new FileOutputStream("Hosts-" + formatter.format(date) + ".xlsx");
            try {
                xSSFWorkbook.write(fileOut);
                fileOut.close();
            } catch (Throwable throwable) {
                try {
                    fileOut.close();
                } catch (Throwable throwable1) {
                    throwable.addSuppressed(throwable1);
                }
                throw throwable;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                xSSFWorkbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
