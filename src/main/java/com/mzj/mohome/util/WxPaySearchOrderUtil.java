package com.mzj.mohome.util;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.mzj.mohome.service.WxTemplateService;
import com.mzj.mohome.vo.WxchatCallbackSuccessData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.Base64;
import java.util.HashMap;
import okhttp3.HttpUrl;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.Signature;
import java.util.Base64;
import java.util.Map;

@Slf4j
public class WxPaySearchOrderUtil {

    // Authorization: <schema> <token>
// GET - getToken("GET", httpurl, "")
// POST - getToken("POST", httpurl, json)
    /**
     * V3  SHA256withRSA 签名.
     *
     * @param method       请求方法  GET  POST PUT DELETE 等
     * @param timestamp    当前时间戳   因为要配置到TOKEN 中所以 签名中的要跟TOKEN 保持一致
     * @param nonceStr     随机字符串  要和TOKEN中的保持一致
     * @param body         请求体 GET 为 "" POST 为JSON
     * @return the string
     */
    public static String getToken(String method, HttpUrl url, String body,String mchId,String certNo,
                                  String nonceStr,PrivateKey privateKey,long timestamp,String signature) {
        try {
            //long timestamp = System.currentTimeMillis() / 1000;
            /*String message = buildMessage(method, url, timestamp, nonceStr, body);
            String signature = sign(message.getBytes("utf-8"),privateKey);*/

            return "mchid=\"" + mchId + "\","
                    + "nonce_str=\"" + nonceStr + "\","
                    + "timestamp=\"" + timestamp + "\","
                    + "serial_no=\"" + certNo + "\","
                    + "signature=\"" + signature + "\"";
        }catch (Exception e){
            return "";
        }

    }

    public static String sign(byte[] message,PrivateKey privateKey) {
        try{
            Signature sign = Signature.getInstance("SHA256withRSA");
            sign.initSign(privateKey);
            sign.update(message);

            return Base64.getEncoder().encodeToString(sign.sign());
        }catch (Exception e){
            log.error("e===={}",e);
            return "";
        }

    }

    public static String buildMessage(String method, HttpUrl url, long timestamp, String nonceStr, String body) {
        String canonicalUrl = url.encodedPath();
        if (url.encodedQuery() != null) {
            canonicalUrl += "?" + url.encodedQuery();
        }

        return method + "\n"
                + canonicalUrl + "\n"
                + timestamp + "\n"
                + nonceStr + "\n"
                + body + "\n";
    }


    /**
     * 根据微信支付系统生成的订单号查询订单详情
     * @param wxPayConfig 微信支付配置信息
     * @param transactionId 微信支付系统生成的订单号
     * @param wxPayClient 微信支付客户端请求对象
     * @return 微信订单对象
     */
    public static WxchatCallbackSuccessData searchByTransactionId(WxPayConfig wxPayConfig, String transactionId, CloseableHttpClient wxPayClient) {
        // 1.请求路径和对象
        String url = wxPayConfig.getDoMain().concat("/v3/pay/transactions/id/").concat(transactionId).concat("?mchid=").concat(wxPayConfig.getMchId());
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Accept", "application/json");

        // 2.完成签名并执行请求
        CloseableHttpResponse response = null;
        try {
            response = wxPayClient.execute(httpGet);
        } catch (IOException e) {

            log.error("微信支付请求失败");
        }

        // 3.解析返回的数据
        WxchatCallbackSuccessData callbackData = resolverResponse(response);
        log.info("callbackData:{}",callbackData);
        return callbackData;
    }

    /**
     * 根据微信支付系统生成的订单号查询订单详情
     * @param wxPayConfig 微信支付配置信息
     * @param orderId 我们自己的订单id
     * @param wxPayClient 微信支付客户端请求对象
     * @return 微信订单对象
     */
    /*public static WxchatCallbackSuccessData searchByOrderId(WxPayConfig wxPayConfig, String orderId,
                                                            CloseableHttpClient wxPayClient,Map<String,Object> map,
        HttpUrl url,PrivateKey privateKey)*/

    public static WxchatCallbackSuccessData searchByOrderId(WxPayConfig wxPayConfig, String orderId,
                                                            CloseableHttpClient wxPayClient) {
        // 1.请求路径和对象
        String url_1 = wxPayConfig.getDoMain().concat("v3/pay/transactions/out-trade-no/").concat(orderId).concat("?mchid=").concat(wxPayConfig.getMchId());
        log.info("token11====="+url_1);
        HttpGet httpGet = new HttpGet(url_1);
  /*      String token = getToken("GET",url,"",wxPayConfig.getMchId(),wxPayConfig.getMchSerialNo(),
                ToolsUtil.getString(map.get("nonceStr")),privateKey,Long.parseLong(ToolsUtil.getString(map.get("timestamp"))),ToolsUtil.getString(map.get("signature")));
          log.info("token====="+token);
          httpGet.setHeader("Authorization", "WECHATPAY2-SHA256-RSA2048 "+token);
*/

        httpGet.setHeader("Accept", "application/json");
        httpGet.addHeader("Content-type", "application/json; charset=utf-8");
        // 2.完成签名并执行请求
        CloseableHttpResponse response = null;
        try {
            response = wxPayClient.execute(httpGet);
        } catch (IOException e) {
           log.error("请求签名失败：{}",e);
        }

        // 3.解析返回的数据
        WxchatCallbackSuccessData callbackData = resolverResponse(response);
        log.info("callbackData====================:{}",callbackData);
        return callbackData;
    }


    /**
     * 解析响应数据
     * @param response 发送请求成功后，返回的数据
     * @return 微信返回的参数
     */
    private static WxchatCallbackSuccessData resolverResponse(CloseableHttpResponse response) {
        try {
            // 1.获取请求码
            int statusCode = response.getStatusLine().getStatusCode();
            // 2.获取返回值 String 格式
            final String bodyAsString = EntityUtils.toString(response.getEntity());

            Gson gson = new Gson();
            if (statusCode == 200) {
                // 3.如果请求成功则解析成Map对象返回
                HashMap<String, String> resultMap = gson.fromJson(bodyAsString, HashMap.class);

                // 4.封装成我们需要的数据
                WxchatCallbackSuccessData callbackData = new WxchatCallbackSuccessData();

                callbackData.setOrderId(ToolsUtil.getString(resultMap.get("out_trade_no")));
                callbackData.setTransactionId(ToolsUtil.getString(resultMap.get("transaction_id")));
                callbackData.setTradestate(ToolsUtil.getString(resultMap.get("trade_state")));
                callbackData.setTradetype(ToolsUtil.getString(resultMap.get("trade_type")));
                String amount = ToolsUtil.getString(resultMap.get("amount"));
                HashMap<String,Object> amountMap = gson.fromJson(amount, HashMap.class);
                String total = String.valueOf(amountMap.get("total"));
                callbackData.setTotalMoney(new BigDecimal(total).movePointLeft(2));
                return callbackData;
            } else {
                if (StringUtils.isNoneBlank(bodyAsString)) {
                    log.error("微信支付请求失败，提示信息:{}", bodyAsString);
                    // 4.请求码显示失败，则尝试获取提示信息
                    HashMap<String, String> resultMap = gson.fromJson(bodyAsString, HashMap.class);
                    log.info("请求签名失败：{}", JSON.toJSONString(resultMap));
                }
                log.error("微信支付请求失败，未查询到原因，提示信息:{}", response);
                // 其他异常，微信也没有返回数据，这就需要具体排查了
                throw new IOException("request failed");
            }
        } catch (Exception e) {
            log.error("WxchatCallbackSuccessData===Exception=错误信息为：{}",e);
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                log.error("WxchatCallbackSuccessData==finally===IOException==错误信息为：{}",e);
            }
        }
        return null;
    }

    }
