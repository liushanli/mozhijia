package com.mzj.mohome.util;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.*;
import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import lombok.Data;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;


@Configuration
@Data
public class WxPayConfig {

    /**
     * 商户号
     */
    @Value("${wechatShopId}")
    private String mchId;

    /**
     * 商户API证书序列号
     */
    @Value("${merchantSerialNumber}")
    private String mchSerialNo;

    /**
     * 商户私钥文件地址
     */
    @Value("${privateKeyPath}")
    private String privateKeyPath;

    /**
     * APIv3密钥
     */
    @Value("${apiV3key}")
    private String apiV3Key;

    /**
     * APPID
     */
    @Value("${weChatAppId}")
    private String appid;

    /**
     * apiclient_cert.pem证书文件
     */
    @Value("${privateCertPath}")
    private String privateCertPath;

    /*
     * privateCertificatePath
     * */
    @Value("${privateCertificatePath}")
    private String privateCertificatePath;
    @Value("${weChatAppId}")
    private String weChatAppId;
    @Value("${weChatAppsecret}")
    private String weChatAppsecret;

    /**
     *获取商户秘钥
     *@return PrivateKey
     */
    public PrivateKey getPrivateKey(String filename){
        try{
            return PemUtil.loadPrivateKey(new FileInputStream(filename));
        } catch (FileNotFoundException e){
            throw new RuntimeException("秘钥文件不存在",e);
        }
    }

    /**
     *获取微信平台签名认证器
     *@return Verifier
     */
    @Bean
    public Verifier getVerifier() throws Exception {
        //获取商户私钥
        PrivateKey privateKey = getPrivateKey(privateKeyPath);
        //获取签名对象
        PrivateKeySigner privateKeySigner = new PrivateKeySigner(mchSerialNo,privateKey);
        //身份认证对象
        WechatPay2Credentials wechatPay2Credentials =new WechatPay2Credentials(mchId ,privateKeySigner);
        //定时刷新微信平台的签名认证
        CertificatesManager certificatesManager = CertificatesManager.getInstance();
        // 向证书管理器增加需要自动更新平台证书的商户信息
        certificatesManager.putMerchant(mchId, wechatPay2Credentials, apiV3Key.getBytes(StandardCharsets.UTF_8));
        // 从证书管理器中获取verifier
        return certificatesManager.getVerifier(mchId);
    }

    /**
     *获取http请求对象
     *@return CloseableHttpClient
     */
    @Bean
    public CloseableHttpClient getWxpayClient(Verifier verifier){
        //获取商户私钥
        PrivateKey privateKey = getPrivateKey(privateKeyPath);
        WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
                .withMerchant(mchId, mchSerialNo, privateKey)
                .withValidator(new WechatPay2Validator(verifier));
        // ... 接下来，你仍然可以通过builder设置各种参数，来配置你的HttpClient
        // 通过WechatPayHttpClientBuilder构造的HttpClient，会自动的处理签名和验签
        //CloseableHttpResponse response = httpClient.execute();
        return builder.build();
    }
}