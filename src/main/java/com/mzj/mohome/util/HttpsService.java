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
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.URI;
import java.util.List;


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
    /**
     * 文件名：  ${file_name}
     * 版权：    Copyright by ljm
     * 描述：
     * 修改人：  HuamingChen
     * 修改时间：2018/10/24
     * 跟踪单号：
     * 修改单号：
     * 修改内容：
     */
        public static String httpGet(String url){
            RestTemplate restTemplate=new RestTemplate();
            String result=restTemplate.exchange(url, HttpMethod.GET,null,String.class).getBody();
            return result;
        }

        public static String httpPost(String url,String name){
            RestTemplate restTemplate=new RestTemplate();
            return restTemplate.postForEntity(url,name,String.class).getBody();
        }

       /* public static void main(String str[]){
            //System.out.println(HttpTemplate.httpGet("http://localhost:8080/test"));
            String jsonStr = httpGet("https://api.map.baidu.com/geocoding/v3/?address=北京市海淀区上地十街10号&output=json&ak=Dw4VqR2Z5ygmDxfEVlaz0j2cI3wx9DGn");
            String jsonStr_1 = httpGet("https://api.map.baidu.com/directionlite/v1/driving?origin=40.01116,116.339303&destination=39.936404,116.452562&ak=Dw4VqR2Z5ygmDxfEVlaz0j2cI3wx9DGn");
            net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(jsonStr);

            String result = jsonObject.get("result").toString();
            net.sf.json.JSONObject jsonObjectResult = net.sf.json.JSONObject.fromObject(result);
            String location = jsonObjectResult.get("location").toString();

            net.sf.json.JSONObject location1 = net.sf.json.JSONObject.fromObject(location);


            net.sf.json.JSONObject jsonObject1 = net.sf.json.JSONObject.fromObject(jsonStr_1);

            String result1 = jsonObject1.get("result").toString();
            net.sf.json.JSONObject jsonObjectResult1 = net.sf.json.JSONObject.fromObject(result1);
            List<String> routes = (List<String>)jsonObjectResult1.get("routes");

            net.sf.json.JSONObject location2 = net.sf.json.JSONObject.fromObject(routes.get(0));


            //net.sf.json.JSONObject jsonObject_1 = net.sf.json.JSONObject.fromObject(jsonStr);
            System.out.println(location2.get("distance"));
            //System.out.println();
        }*/
}
