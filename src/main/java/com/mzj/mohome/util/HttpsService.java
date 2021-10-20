package com.mzj.mohome.util;

import com.winnerlook.service.https.MyVerifyHostname;
import com.winnerlook.service.https.MyX509TrustManager;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.URI;


public class HttpsService  {

    private SSLContext sslContext;
    private X509TrustManager tm;
    private CloseableHttpClient httpClient;

    public HttpsService()
            throws Exception {
        sslContext = SSLContext.getInstance("TLS");
        tm = new MyX509TrustManager();
        sslContext.init(null, new TrustManager[]{tm},
                new java.security.SecureRandom());
        httpClient = HttpClients.custom()
                .setSSLHostnameVerifier(new MyVerifyHostname())
                .setSSLContext(sslContext).build();
    }



    public String doPost(String path, String headerStr,
                         String requestBody, String encoding) throws Exception {
        String resultString = "";
        CloseableHttpResponse response = null;
        HttpPost httpPost = null;
        try {
            URI uri = new URI(path);
            httpPost = new HttpPost(uri);
            httpPost.setHeader("Content-type", "application/json; charset=utf-8");

            /** 报文头中设置Authorization参数*/
            httpPost.setHeader("Authorization", headerStr);

            /** 设置请求报文体*/
            httpPost.setEntity(new StringEntity(requestBody, encoding));

            response = httpClient.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            httpEntity = new BufferedHttpEntity(httpEntity);
            resultString = EntityUtils.toString(httpEntity);
        } catch (Exception e) {
            throw e;
        } finally { //释放连接

            if (null != response)
                response.close();
            if (httpPost != null)
                httpPost.releaseConnection();

        }
        return resultString;
    }
}
