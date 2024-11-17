package org.kizilay;

public class Host {
  Boolean power;
  
  String name;
  
  String ip;
  
  String port;
  
  String os;
  
  String role;
  
  String environment;
  
  String status;
  
  String message;
  
  public String getStatus() {
    return this.status;
  }
  
  public void setStatus(String status) {
    this.status = status;
  }
  
  public String getMessage() {
    return this.message;
  }
  
  public void setMessage(String message) {
    this.message = message;
  }
  
  public Host() {}
  
  public Host(String hostName, String ip, String port, String templateName) {
    this.name = hostName;
    this.ip = ip;
    this.port = port;
    this.os = templateName;
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getIp() {
    return this.ip;
  }
  
  public void setIp(String ip) {
    this.ip = ip.trim();
  }
  
  public String getPort() {
    return this.port;
  }
  
  public void setPort(String port) {
    this.port = port;
  }
  
  public String getOs() {
    return this.os;
  }
  
  public void setOs(String os) {
    this.os = os;
  }
  
  public Boolean getPower() {
    return this.power;
  }
  
  public void setPower(Boolean power) {
    this.power = power;
  }
  
  public String getRole() {
    return this.role;
  }
  
  public void setRole(String role) {
    this.role = role;
  }
  
  public String getEnvironment() {
    return this.environment;
  }
  
  public void setEnvironment(String environment) {
    this.environment = environment;
  }
  
  public Host(Boolean power, String name, String ip, String port, String os, String role, String environment) {
    this.power = power;
    this.name = name;
    this.ip = ip;
    this.port = port;
    this.os = os;
    this.role = role;
    this.environment = environment;
  }
  
  public String toString() {
    return "Host{power=" + this.power + ", name='" + this.name + "', ip='" + this.ip + "', port='" + this.port + "', os='" + this.os + "', role='" + this.role + "', environment='" + this.environment + "', status='" + this.status + "', message='" + this.message + "'}";
  }
}
