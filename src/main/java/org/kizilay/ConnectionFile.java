package org.kizilay;

import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ConnectionFile {


    public static URL getZabbixURL() {
        try {
            if(connFile.workType.contains("kizilay"))
            return new URL(connFile.KIZILAY_ZABBIX_API_URL);
            else if (connFile.workType.contains("test")) {
                return new URL(connFile.test_ZABBIX_API_URL);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    public static String getToken() {
        String authToken;
        if(connFile.workType.contains("kizilay"))
         return connFile.KIZILAY_ZABBIX_API_TOKEN;
        else if (connFile.workType.contains("test")) {
            return connFile.test_ZABBIX_API_TOKEN;

        }
        return null;
    }
}
