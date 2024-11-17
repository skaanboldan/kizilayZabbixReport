package org.kizilay;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {
  public static List<Host> readHostsFromExcel(String filePath) {
    List<Host> hosts = new ArrayList<>();
    try (FileInputStream fis = new FileInputStream(filePath);
         XSSFWorkbook xSSFWorkbook = new XSSFWorkbook(fis)) {

      Sheet sheet = xSSFWorkbook.getSheetAt(0);

      // Başlık satırını atlamak için for döngüsü 1. satırdan başlıyor
      for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
        Row row = sheet.getRow(i);
        if (row == null) continue; // Satır boşsa atla

        Host host = new Host();
        try {
          String powerValue = row.getCell(0).getStringCellValue();
          String environment = row.getCell(5).getStringCellValue();

          // Sadece "A" ve "Prod" kriterlerine uygun olanları ekle
          if (powerValue.contains("A") && environment.contains("Prod")) {
            host.setPower(Boolean.valueOf("A".equalsIgnoreCase(powerValue)));
            host.setName(row.getCell(1).getStringCellValue());
            host.setIp(row.getCell(2).getStringCellValue());
            host.setOs(row.getCell(3).getStringCellValue());
            host.setRole(row.getCell(4).getStringCellValue());
            host.setEnvironment(environment);
            hosts.add(host); // Uygun host'u listeye ekle
          }
        } catch (Exception exception) {
          // Hata durumunda loglama yapılabilir
          exception.printStackTrace();
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return hosts;
  }

  
  public static void main(String[] args) {
    String excelFilePath = "Envanter.xlsx";
    List<Host> hosts = readHostsFromExcel(excelFilePath);
    System.out.println(hosts.size());
    for (Host host : hosts) {
      System.out.println("Power: " + host.getPower());
      System.out.println("Name: " + host.getName());
      System.out.println("IP: " + host.getIp());
      System.out.println("OS: " + host.getOs());
      System.out.println("Role: " + host.getRole());
      System.out.println("Environment: " + host.getEnvironment());
      System.out.println("---------------");
    } 
  }
}
