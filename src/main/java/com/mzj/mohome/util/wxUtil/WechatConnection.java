package com.mzj.mohome.util.wxUtil;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WechatConnection {

    // 成功标志
    public static final String SUCCESS_CODE = "SUCCESS";

    /**
     * 建立微信连接，并返回结果
     *
     * @param url
     * @param info
     * @param object
     * @return
     * @throws IOException
     */
    public static Object connect(String url, String info, Class<?> object) throws IOException {
        // 建立连接
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setConnectTimeout(8000);
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        // 发送数据
        BufferedOutputStream bos = new BufferedOutputStream(conn.getOutputStream());
        bos.write(info.getBytes());
        bos.flush();
        bos.close();

        // 获取数据
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        // 接收数据
        String line;
        StringBuffer str = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            str.append(line);
        }

        return WechatUtil.truncateDataFromXML(object, str.toString());
    }

}
