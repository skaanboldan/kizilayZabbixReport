package org.kizilay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class GetHostFromZabbix {
  public static List<Host> getHostsList() throws IOException {
    String authToken=ConnectionFile.getToken();
      List<Host> hostDetails = getHostNamesAndIp(authToken);
      return hostDetails;

  }
  

  
  private static List<Host> getHostNamesAndIp(String authToken) throws IOException {
    List<Host> hostDetails = new ArrayList<>();
    JSONObject request = new JSONObject();
    request.put("jsonrpc", "2.0");
    request.put("method", "host.get");
    request.put("id", 2);
    request.put("auth", authToken);
    JSONObject params = new JSONObject();
    params.put("output", new JSONArray(Arrays.asList(new String[] { "hostid", "name" })));
    params.put("selectInterfaces", new JSONArray(Arrays.asList(new String[] { "ip", "port" })));
    params.put("selectTemplates", new JSONArray(Arrays.asList(new String[] { "templateid" })));
    request.put("params", params);
    JSONObject response = sendPostRequest(request);
    JSONArray result = response.optJSONArray("result");
    for (int i = 0; i < result.length(); i++) {
      JSONObject host = result.getJSONObject(i);
      String hostName = host.optString("name");
      JSONArray interfaces = host.optJSONArray("interfaces");
      JSONArray templates = host.optJSONArray("templates");
      String ip = "No IP found";
      String port = "No port found";
      if (interfaces != null && interfaces.length() > 0) {
        JSONObject firstInterface = interfaces.getJSONObject(0);
        ip = firstInterface.optString("ip", "No IP found");
        port = firstInterface.optString("port", "No port found");
      } 
      String templateName = "No Template found";
      if (templates != null && templates.length() > 0) {
        String templateId = templates.getJSONObject(0).optString("templateid");
        templateName = getTemplateName(authToken, templateId);
      } 
      hostDetails.add(new Host(hostName, ip, port, templateName));
    } 
    return hostDetails;
  }
  
  private static String getTemplateName(String authToken, String templateId) throws IOException {
    JSONObject request = new JSONObject();
    request.put("jsonrpc", "2.0");
    request.put("method", "template.get");
    request.put("id", 3);
    request.put("auth", authToken);
    JSONObject params = new JSONObject();
    params.put("output", new JSONArray(Arrays.asList(new String[] { "host" })));
    params.put("templateids", new JSONArray(Arrays.asList(new String[] { templateId })));
    request.put("params", params);
    JSONObject response = sendPostRequest(request);
    JSONArray result = response.optJSONArray("result");
    if (result != null && result.length() > 0)
      return result.getJSONObject(0).optString("host"); 
    return "No Template found";
  }
  
  private static JSONObject sendPostRequest(JSONObject request) throws IOException {

    URL url = ConnectionFile.getZabbixURL();
    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
    connection.setRequestMethod("POST");
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setDoOutput(true);
    OutputStream os = connection.getOutputStream();
    try {
      byte[] input = request.toString().getBytes("utf-8");
      os.write(input, 0, input.length);
      if (os != null)
        os.close(); 
    } catch (Throwable throwable) {
      if (os != null)
        try {
          os.close();
        } catch (Throwable throwable1) {
          throwable.addSuppressed(throwable1);
        }  
      throw throwable;
    } 
    StringBuilder response = new StringBuilder();
    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
    try {
      String responseLine;
      while ((responseLine = br.readLine()) != null)
        response.append(responseLine.trim()); 
      br.close();
    } catch (Throwable throwable) {
      try {
        br.close();
      } catch (Throwable throwable1) {
        throwable.addSuppressed(throwable1);
      } 
      throw throwable;
    } 
    return new JSONObject(response.toString());
  }
}
