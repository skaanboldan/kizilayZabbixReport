package org.kizilay;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GetHostFromKizilay {
  public List<Host> getHostsList(String excelFilePath) {
    // Eğer excelFilePath null veya boşsa varsayılan dosya adını kullan
    if (excelFilePath == null || excelFilePath.trim().isEmpty()) {
      excelFilePath = "Envanter.xlsx";
    }

    // Excel dosyasını oku
    List<Host> hosts = ExcelReader.readHostsFromExcel(excelFilePath);

    // Filtrelenmiş host listesini döndür
    return filterHost(hosts);
  }


  private List<Host> filterHost(List<Host> hosts) {
    Iterator<Host> iterator = hosts.iterator();
    while (iterator.hasNext()) {
      Host host = iterator.next();
      if ((host.getPower() != null && !host.getPower().booleanValue()) && !"Prod".equals(host.getEnvironment()))
        iterator.remove(); 
    } 
    return hosts;
  }


  public List<Host> MakeEnableHostFromExcel(List<Host> kizilayHostsList) throws Exception {

    List<Host> enabledHostList = new ArrayList<>();

    ZabbixTools zabbixTools=new ZabbixTools();

    Iterator<Host> iterator = kizilayHostsList.iterator();
    while (iterator.hasNext()) {
      Host host = iterator.next();
      if ((host.getPower() != null && !host.getPower().booleanValue()) && !"Prod".equals(host.getEnvironment())){
        try {
          String zabbixHostID=zabbixTools.getHostId(host.getName(), host.getIp());
          zabbixTools.enableHost(zabbixHostID);
          enabledHostList.add(host);
        }
        catch (Exception e ) {
        throw e;
        }
        }
      }
    return enabledHostList;
  }



  }






