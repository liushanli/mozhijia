package com.mzj.mohome.util;

import com.alipay.api.domain.DataEntry;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Random;

public class SmsSendUtil {
        public static String sendSmsByPost(String path, String postContent) {
            URL url = null;
            try {
                url = new URL(path);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setConnectTimeout(10000);
                httpURLConnection.setReadTimeout(10000);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestProperty("Charset", "UTF-8");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.connect();
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(postContent.getBytes("UTF-8"));
                os.flush();
                StringBuilder sb = new StringBuilder();
                int httpRspCode = httpURLConnection.getResponseCode();
                if (httpRspCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    br.close();
                    return sb.toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    /**
     * 建议使用
     */
    public String randomCode() {
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }
}
