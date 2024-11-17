package org.kizilay;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WriteCSV {
  public static String writeDifferenceToCSV(List<Host> difference, String outputFileName) {
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
    Date date = new Date();
    String fileName = outputFileName + "-" + outputFileName + ".csv";
    try {
      PrintWriter writer = new PrintWriter(new File(fileName));
      try {
        StringBuilder sb = new StringBuilder();
        sb.append("Name,IP");
        sb.append('\n');
        for (Host host : difference) {
          sb.append(host.getName());
          sb.append(',');
          sb.append(host.getIp());
          sb.append('\n');
        } 
        writer.write(sb.toString());
        System.out.println("CSV file created successfully.");
        String str = (new File(fileName)).getAbsolutePath();
        writer.close();
        return str;
      } catch (Throwable throwable) {
        try {
          writer.close();
        } catch (Throwable throwable1) {
          throwable.addSuppressed(throwable1);
        } 
        throw throwable;
      } 
    } catch (FileNotFoundException e) {
      System.out.println("Error creating CSV file: " + e.getMessage());
      return null;
    } 
  }
}
