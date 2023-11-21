package com.mzj.mohome.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.UUID;

public class WxPayUtils {

    private static Logger logger = LoggerFactory.getLogger(WxPayUtils.class);

    /**
     * 生成支付所需的参数
     *
     * @param appId 公众号的APP_ID
     * @param prepay_id 下单接口返回的参数 预支付交易会话标识
     * @param privateKeyPath 私钥地址
     * @return JSONObject
     * @throws Exception e
     */
    public static JSONObject getTokenWeiXin(String appId, String prepay_id, String privateKeyPath) {
        // 获取随机字符串
        String nonceStr = getNonceStr();
        // 获取微信小程序支付package
        String packagestr = "prepay_id=" + prepay_id;
        long timestamp = System.currentTimeMillis() / 1000;
        //签名，使用字段appId、timeStamp、nonceStr、package计算得出的签名值
        String message = buildMessageTwo(appId, timestamp, nonceStr, packagestr);
        //获取对应的签名
        String signature = sign(message.getBytes(StandardCharsets.UTF_8), privateKeyPath);
        // 组装返回
        JSONObject json = new JSONObject(new JSONObject());
        json.put("appId", appId);
        json.put("timeStamp", String.valueOf(timestamp));
        json.put("nonceStr", nonceStr);
        json.put("package", packagestr);
        json.put("signType", "RSA");
        json.put("paySign", signature);
        return json;
    }

    /**
     * @return
     * @Author 神传子
     * @MethodName decryptToString
     * @Description //TODO 解密支付回调参数
     * @Date 11:27 2023/5/23
     * @Param aesKey ApiV3Key
     * @Param associatedData 返回参数的【resource.associated_data】
     * @Param nonce 返回参数的【resource.ciphertext】
     * @Param ciphertext 返回参数的【resource.nonce】
     */
    public static String decryptToString(byte[] aesKey, byte[] associatedData, byte[] nonce, String ciphertext)
            throws GeneralSecurityException, IOException {
        final int KEY_LENGTH_BYTE = 32;
        final int TAG_LENGTH_BIT = 128;

        if (aesKey.length != KEY_LENGTH_BYTE){
            logger.info("无效的ApiV3Key，长度必须为32个字节");
            return "";
        }
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

            SecretKeySpec key = new SecretKeySpec(aesKey, "AES");
            GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, nonce);

            cipher.init(Cipher.DECRYPT_MODE, key, spec);
            cipher.updateAAD(associatedData);

            return new String(cipher.doFinal(Base64.getDecoder().decode(ciphertext)), "utf-8");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new IllegalStateException(e);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static JSONObject doGet(String url) {
        //获取DefaultHttpClient请求
        HttpClient client = HttpClientBuilder.create().build();

        HttpGet get = new HttpGet(url);
        JSONObject response = null;
        try {
            HttpResponse res = client.execute(get);
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = res.getEntity();
                // 返回json格式
                String result = EntityUtils.toString(res.getEntity());
                response = JSON.parseObject(result);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    //    获取随机字符串
    public static String getNonceStr() {
        return UUID.randomUUID().toString()
                .replaceAll("-", "")
                .substring(0, 32);
    }

    /**
     * 随机字符串 订单号
     *
     * @return
     */
    public static String generateNonceStr() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }

    // 签名连接成一串
    private static String buildMessageTwo(String appId, long timestamp, String nonceStr, String packag) {
        return appId + "\n"
                + timestamp + "\n"
                + nonceStr + "\n"
                + packag + "\n";
    }


    /**
     * 生成签名
     *
     * @param message message
     * @return String
     */
    private static String sign(byte[] message, String privateKeyPath) {
        PrivateKey merchantPrivateKey = null;
        X509Certificate wechatPayCertificate = null;
        try {
            merchantPrivateKey = PemUtil.loadPrivateKey(
                    new FileInputStream(privateKeyPath));
            /*wechatPayCertificate = PemUtil.loadCertificate(
                    new FileInputStream(wechatPayProperties.getPrivateCertificatePath()));*/

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Signature sign = null;
        try {
            sign = Signature.getInstance("SHA256withRSA");
            //这里需要一个PrivateKey类型的参数，就是商户的私钥。
            sign.initSign(merchantPrivateKey);
            sign.update(message);
            return Base64.getEncoder().encodeToString(sign.sign());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}

