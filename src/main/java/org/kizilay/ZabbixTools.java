package org.kizilay;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class ZabbixTools {
  private static final Map<String, String> templateCache = new HashMap<>();
  
  public static List<Host> createHostFromList(List<Host> hostList) throws Exception {
    List<Host> returnHostList = new ArrayList<>();
    for (Host host : hostList) {
      Host add = createHost(ConnectionFile.getToken(), host);
      System.out.println(add.getIp());
      returnHostList.add(add);
    } 
    return returnHostList;
  }
  
  private static Host createHost(String authToken, Host host) throws Exception {
    URL url = ConnectionFile.getZabbixURL();
    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
    conn.setRequestMethod("POST");
    conn.setRequestProperty("Content-Type", "application/json");
    conn.setDoOutput(true);
    JSONObject request = new JSONObject();
    request.put("jsonrpc", "2.0");
    request.put("method", connFile.HOST_CREATE_METHOD);
    int templateId = connFile.LINUX_BY_ZABBIX_AGENT_ACTIVE_TEMPLATE_ID;
    int groupId = connFile.LINUX_SERVERS_GROUP_ID;
    host.setPort("20050");
    if (host.getOs().contains("Windows")) {
      groupId = connFile.WINDOWS_GROUP_ID;
      templateId = connFile.WINDOWS_BY_ZABBIX_AGENT_TEMPLATE_ID;
    } 
    request.put("params", (new JSONObject())
        .put("host", host.getName())
        .put("interfaces", (new JSONArray())
          .put((new JSONObject())
            .put("type", 1)
            .put("main", 1)
            .put("useip", 1)
            .put("dns", "")
            .put("ip", host.getIp().trim())
            .put("port", host.getPort())))
        .put("groups", (new JSONArray())
          .put((new JSONObject())
            .put("groupid", groupId)))
        .put("templates", (new JSONArray())
          .put((new JSONObject())
            .put("templateid", templateId))));
    request.put("auth", authToken);
    request.put("id", 1);
    System.out.println("Request JSON: " + request.toString(2));
    OutputStream os = conn.getOutputStream();
    try {
      os.write(request.toString().getBytes(StandardCharsets.UTF_8));
      os.flush();
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
    try {
      InputStream is = conn.getInputStream();
      try {
        String responseBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        JSONObject response = new JSONObject(responseBody);
        System.out.println("Response JSON: " + response.toString(2));
        if (response.has("error")) {
          System.out.println("Error: " + response.getJSONObject("error").getString("data"));
          host.setStatus("Fail");
          host.setMessage(response.getJSONObject("error").getString("data"));
          System.out.println(host.toString());
          Host host2 = host;
          if (is != null)
            is.close(); 
          return host2;
        } 
        System.out.println("Host created successfully.");
        host.setStatus("Success");
        Host host1 = host;
        if (is != null)
          is.close(); 
        return host1;
      } catch (Throwable throwable) {
        if (is != null)
          try {
            is.close();
          } catch (Throwable throwable1) {
            throwable.addSuppressed(throwable1);
          }  
        throw throwable;
      } 
    } catch (IOException e) {
      InputStream errorStream = conn.getErrorStream();
      try {
        if (errorStream != null) {
          String errorResponse = new String(errorStream.readAllBytes(), StandardCharsets.UTF_8);
          System.out.println("Error response: " + errorResponse);
        } else {
          System.out.println("Error response: " + e.getMessage());
        } 
        if (errorStream != null)
          errorStream.close(); 
      } catch (Throwable throwable) {
        if (errorStream != null)
          try {
            errorStream.close();
          } catch (Throwable throwable1) {
            throwable.addSuppressed(throwable1);
          }  
        throw throwable;
      } 
      throw e;
    } finally {}
  }
  
  private static String getTemplateId(String authToken, String templateName) throws Exception {
    if (templateCache.containsKey(templateName))
      return templateCache.get(templateName); 
    URL url = ConnectionFile.getZabbixURL();
    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
    conn.setRequestMethod("POST");
    conn.setRequestProperty("Content-Type", "application/json");
    conn.setDoOutput(true);
    JSONObject request = new JSONObject();
    request.put("jsonrpc", "2.0");
    request.put("method", connFile.TEMPLATE_QUERY_METHOD);
    request.put("params", (new JSONObject())
        .put("output", (new JSONArray()).put("templateid"))
        .put("filter", (new JSONObject()).put("host", (new JSONArray()).put(templateName))));
    request.put("auth", authToken);
    request.put("id", 1);
    System.out.println("Template Query JSON: " + request.toString(2));
    OutputStream os = conn.getOutputStream();
    try {
      os.write(request.toString().getBytes(StandardCharsets.UTF_8));
      os.flush();
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
    try {
      InputStream is = conn.getInputStream();
      try {
        String responseBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        JSONObject response = new JSONObject(responseBody);
        System.out.println("Template Response JSON: " + response.toString(2));
        if (response.has("error"))
          throw new RuntimeException("Error querying template: " + response.getJSONObject("error").toString(2)); 
        JSONArray result = response.getJSONArray("result");
        if (result.length() > 0) {
          JSONObject template = result.getJSONObject(0);
          String templateId = template.getString("templateid");
          templateCache.put(templateName, templateId);
          String str1 = templateId;
          if (is != null)
            is.close(); 
          return str1;
        } 
        throw new RuntimeException("Template not found: " + templateName);
      } catch (Throwable throwable) {
        if (is != null)
          try {
            is.close();
          } catch (Throwable throwable1) {
            throwable.addSuppressed(throwable1);
          }  
        throw throwable;
      } 
    } catch (IOException e) {
      InputStream errorStream = conn.getErrorStream();
      try {
        if (errorStream != null) {
          String errorResponse = new String(errorStream.readAllBytes(), StandardCharsets.UTF_8);
          System.out.println("Error response: " + errorResponse);
        } else {
          System.out.println("Error response: " + e.getMessage());
        } 
        if (errorStream != null)
          errorStream.close(); 
      } catch (Throwable throwable) {
        if (errorStream != null)
          try {
            errorStream.close();
          } catch (Throwable throwable1) {
            throwable.addSuppressed(throwable1);
          }  
        throw throwable;
      } 
      throw e;
    } finally {}
  }
  static String getHostId(String hostName, String hostIp) throws Exception {
    JSONObject json = new JSONObject();
    json.put("jsonrpc", "2.0");
    json.put("method", "host.get");
    json.put("params", new JSONObject()
            .put("output", "hostid")
            .put("filter", new JSONObject()
                    .put("host", hostName))
            .put("search", new JSONObject()
                    .put("ip", hostIp)));
    json.put("auth", ConnectionFile.getToken());
    json.put("id", 2);

    JSONArray hosts = sendRequest(json).getJSONArray("result");
    return hosts.length() > 0 ? hosts.getJSONObject(0).getString("hostid") : null;
  }

  static void enableHost(String hostId) throws Exception {
    JSONObject json = new JSONObject();
    json.put("jsonrpc", "2.0");
    json.put("method", "host.update");
    json.put("params", new JSONObject()
            .put("hostid", hostId)
            .put("status", 0)); // 0 = enabled, 1 = disabled
    json.put("auth", ConnectionFile.getToken());
    json.put("id", 3);

    sendRequest(json);
  }

  private static JSONObject sendRequest(JSONObject json) throws Exception {
    URL url = ConnectionFile.getZabbixURL(); 
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("POST");
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setDoOutput(true);

    try (OutputStream os = connection.getOutputStream()) {
      byte[] input = json.toString().getBytes(StandardCharsets.UTF_8);
      os.write(input, 0, input.length);
    }

    StringBuilder response = new StringBuilder();
    try (var reader = new java.io.BufferedReader(new java.io.InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
      String responseLine;
      while ((responseLine = reader.readLine()) != null) {
        response.append(responseLine.trim());
      }
    }

    return new JSONObject(response.toString());
  }


}
