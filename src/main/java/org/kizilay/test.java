package test;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class test {
  private static void executeRequest(HttpPost httpPost) {
    try {
      HttpClient client = new DefaultHttpClient();
      HttpResponse response = client.execute(httpPost);
      System.out.println("Response Code:  " + response.getStatusLine().getStatusCode());
    }  catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (IllegalStateException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void executeMultiPartRequest(String urlString, File file) throws IOException {
    HttpPost postRequest = new HttpPost(urlString);
    postRequest = addHeader(postRequest, "Access Token");
    try {
      postRequest.setEntity(new FileEntity(file));
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    executeRequest(postRequest);
  }

  private static HttpPost addHeader(HttpPost httpPost, String accessToken) {
    httpPost.addHeader("Accept", "application/json;odata=verbose");
    httpPost.setHeader("Authorization", "Bearer " + accessToken);
    return httpPost;
  }

  public static void main(String args[]) throws IOException {
    test fileUpload = new test();
    File file = new File("zabbixDifference-zabbixDifference.csv");
    fileUpload.executeMultiPartRequest(
            "Here Goes the URL", file);

  }
}