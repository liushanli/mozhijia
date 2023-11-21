package com.mzj.mohome.util;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.Amount;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayResponse;
import org.springframework.beans.factory.annotation.Value;

/** Native 支付下单为例 */
public class WePayConfig {
    /** 商户号 */
    @Value("${wechatShopId}")
    public static String merchantId="1603263487";
    /** 商户API私钥路径 */
    public static String privateKeyPath="D:/work/HYPay/cert/1603263487_20210401_cert/apiclient_key.pem";
    /** 商户证书序列号 */
    public static String merchantSerialNumber="25FFAC52B3D459A8505C72C406B1F9870B96EAF1";
    /** 商户APIV3密钥 */
    @Value("${apiV3key}")
    public static String apiV3key="6A039158631944B181628652C4BD943B";
    @Value("${weChatAppId}")
    public static String weChatAppId="wx0e98389493b8b0a9";
    public static void main(String[] args) {
        // 使用自动更新平台证书的RSA配置
        // 建议将 config 作为单例或全局静态对象，避免重复的下载浪费系统资源
        Config config =
                new RSAAutoCertificateConfig.Builder()
                        .merchantId(merchantId)
                        .privateKeyFromPath(privateKeyPath)
                        .merchantSerialNumber(merchantSerialNumber)
                        .apiV3Key(apiV3key)
                        .build();
        // 构建service
        NativePayService service = new NativePayService.Builder().config(config).build();
        // request.setXxx(val)设置所需参数，具体参数可见Request定义
        PrepayRequest request = new PrepayRequest();
        Amount amount = new Amount();
        amount.setTotal(100);
        request.setAmount(amount);
        request.setAppid(weChatAppId);
        request.setMchid(merchantId);
        request.setDescription("测试商品标题");
        request.setNotifyUrl("https://notify_url");
        request.setOutTradeNo("out_trade_no_001");
        // 调用下单方法，得到应答
        PrepayResponse response = service.prepay(request);
        // 使用微信扫描 code_url 对应的二维码，即可体验Native支付
        System.out.println(response.getCodeUrl());
    }
}
