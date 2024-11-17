package org.kizilay;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class SharePointDownloader {
    public static void main(String[] args) {
        String fileUrl = "https://gizdanismanlik-my.sharepoint.com/:x:/g/personal/kaan_boldan_gizdanismanlik_com_tr/Ef4sm2aPz1pInqrAfPEHehoB4AWJzA-JNP2NPTDKeZARHA?e=WgEA2r";
        String saveFilePath = "downloaded_file.xlsx";
        String username = "kaan.boldan@gizdanismanlik.com.tr";
        String password = "nyhqy7-Kowtyb-hykvor";

        try {
            URL url = new URL(fileUrl);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("GET");

            // Basic Authentication
            String auth = username + ":" + password;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            httpConn.setRequestProperty("Authorization", "Basic " + encodedAuth);

            int responseCode = httpConn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = httpConn.getInputStream();
                FileOutputStream outputStream = new FileOutputStream(saveFilePath);

                byte[] buffer = new byte[4096];
                int bytesRead = -1;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.close();
                inputStream.close();

                System.out.println("Dosya başarıyla indirildi: " + saveFilePath);
            } else {
                System.out.println("Dosya indirilemedi. Sunucu yanıt kodu: " + responseCode);
            }
            httpConn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
