package org.kizilay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GetDifference {
  public static List<Host> findDifference(List<Host> list1, List<Host> list2) {
    Set<String> set1 = new HashSet<>();
    for (Host host : list1)
      set1.add(host.getIp() + "-" + host.getIp()); 
    List<Host> result = new ArrayList<>();
    for (Host host : list2) {
      String identifier = host.getIp() + "-" + host.getIp();
      if (!set1.contains(identifier))
        result.add(host); 
    } 
    return result;
  }
  
  public static List<Host> findDifferenceZabbix(List<Host> zabbixHostsList) {
    List<Host> differentHosts = new ArrayList<>();
    Map<String, List<Host>> ipToHostsMap = new HashMap<>();
    for (Host host : zabbixHostsList)
      ((List<Host>)ipToHostsMap.computeIfAbsent(host.getIp(), k -> new ArrayList())).add(host); 
    for (Map.Entry<String, List<Host>> entry : ipToHostsMap.entrySet()) {
      List<Host> hostsWithSameIp = entry.getValue();
      if (hostsWithSameIp.size() > 1) {
        String firstHostName = ((Host)hostsWithSameIp.get(0)).getName();
        boolean hasDifferentNames = false;
        boolean allEnabled = true;
        for (Host host : hostsWithSameIp) {
          if (!host.getName().equals(firstHostName))
            hasDifferentNames = true; 
          if (host.getPower()!=null&&!host.getPower().booleanValue()) {
            allEnabled = false;
            break;
          } 
        } 
        if (hasDifferentNames && allEnabled)
          differentHosts.addAll(hostsWithSameIp); 
      } 
    } 
    return differentHosts;
  }
}
