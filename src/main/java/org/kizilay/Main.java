package org.kizilay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
  public static void main(String[] args) throws IOException {
    String excelFilePath=null;
    if (connFile.workType.contains("test")) {
      excelFilePath = "Envanter.xlsx";
    }
    else {
      if (args.length == 0) {
        System.out.println("Lütfen Excel dosya yolunu parametre olarak geçin.");
        return;
      }
      excelFilePath = args[0]; // Excel dosya yolunu parametre olarak al
    }




    GetHostFromZabbix getHostFromZabbix = new GetHostFromZabbix();
    GetHostFromKizilay getHostFromKizilay = new GetHostFromKizilay();
    List<Host> ZabbixHostsList = new ArrayList<>();
    List<Host> KizilayHostsList = new ArrayList<>();
    List<Host> enabledHostList = new ArrayList<>();
    try {
      ZabbixHostsList = GetHostFromZabbix.getHostsList();
      KizilayHostsList = getHostFromKizilay.getHostsList(excelFilePath);
      enabledHostList= getHostFromKizilay.MakeEnableHostFromExcel(KizilayHostsList);
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
    System.out.println("Kizilay Host count: " + KizilayHostsList.size() + " Zabbix Host count: " + ZabbixHostsList.size());

    List<Host> zabbixDifference = GetDifference.findDifferenceZabbix(ZabbixHostsList);
    List<Host> difference = GetDifference.findDifference(ZabbixHostsList, KizilayHostsList);

    System.out.println("difference is :" + difference.size());

    try {
      List<Host> hostList = ZabbixTools.createHostFromList(difference);
      ExcelWriter.writeExcel(hostList);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    String filePath = WriteCSV.writeDifferenceToCSV(difference, "Excel_Kizilay_zabbix_difference");
    List<Host> ZabbixHostsListAfterInserting = GetHostFromZabbix.getHostsList();
    List<Host> zabbixDifferenceAfterInserting = GetDifference.findDifferenceZabbix(ZabbixHostsListAfterInserting);
    WriteCSV.writeDifferenceToCSV(zabbixDifferenceAfterInserting, "zabbixDifference");
    WriteCSV.writeDifferenceToCSV(enabledHostList, "enabledHostList");

    System.out.println("Succesfully Saved at " + filePath);
  }
}
