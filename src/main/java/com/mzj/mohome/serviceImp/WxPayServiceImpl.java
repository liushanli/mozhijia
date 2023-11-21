package com.mzj.mohome.serviceImp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mzj.mohome.mapper.UserMapper;
import com.mzj.mohome.service.WxPayService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mzj.mohome.util.WxPayConfig;
import com.mzj.mohome.util.WxPayUtils;
import com.mzj.mohome.vo.OrderInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class WxPayServiceImpl implements WxPayService {

    @Resource
    private WxPayConfig wxPayConfig;

    @Resource
    private CloseableHttpClient httpClient;
    @Autowired
    private UserMapper userMapper;

    /**
     * jsapi用户下单
     * @param orderInfo 订单信息
     * @return 预支付标识
     */
    @Override
    public Map<String, Object> jsApiPay(OrderInfo orderInfo) {
        Map<String,Object> map = new HashMap<>();
        map.put("success",true);
        map.put("msg","");
        Integer totalFee = orderInfo.getTotalFee()*100;
        Integer num = userMapper.fineUserWhite(orderInfo.getUserId());
        if(num != null){
            totalFee = num;
        }
        String tradeNo =  WxPayUtils.generateNonceStr();
        // 构建POST请求
        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi");
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Content-type", "application/json; charset=utf-8");
        // 使用JSON库，构建请求参数对象
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();
        JSONObject attachObj = new JSONObject();
        rootNode
                // 直连商户号
                .put("mchid", wxPayConfig.getMchId())
                // 应用ID
                .put("appid", wxPayConfig.getAppid())
                // 商品描述
                .put("description", orderInfo.getTitle())
                // 商户订单号
                .put("out_trade_no", tradeNo)
                // 通知地址
                .put("notify_url", "http://wx.mzjsh.com:8080/mohome_test")
                .put("attach", attachObj.toJSONString());
        // 订单金额对象
        rootNode.putObject("amount")
                // 总金额，单位为分
                .put("total",totalFee);
        //支付者
        rootNode.putObject("payer")
                //用户标识
                .put("openid", orderInfo.getOpenId());
        try {
            objectMapper.writeValue(bos, rootNode);
            httpPost.setEntity(new StringEntity(bos.toString("UTF-8"), "UTF-8"));
            // 执行请求
            CloseableHttpResponse response = httpClient.execute(httpPost);
            log.info("请求返回参数为：{}",JSON.toJSONString(response));
            // 获取响应
            JSONObject prepay_id_obj = JSON.parseObject(EntityUtils.toString(response.getEntity()));
            System.out.println("【下单返回参数】" + prepay_id_obj);
            if (prepay_id_obj.get("prepay_id") == null) {
                map.put("msg",prepay_id_obj.getString("message"));
                map.put("success",false);
                return map;
            }
            //返回签名
            String prepay_id = prepay_id_obj.getString("prepay_id");
            log.info("prepay_i===="+prepay_id);
            JSONObject tokenWeiXin = WxPayUtils.getTokenWeiXin(wxPayConfig.getAppid(), prepay_id, wxPayConfig.getPrivateKeyPath());
            map.put("result",tokenWeiXin);
            map.put("tradeNo",tradeNo);
            map.put("openId",orderInfo.getOpenId());
            return map;
        } catch (IOException e) {
            e.printStackTrace();
            log.error("获取调用信息失败{}",e);
            return null;
        }
    }

    /** 获取微信支付参数信息 */
    private Map<String,Object> getPayment(String prepayId, String appId, PrivateKey privateKey) {
        Map<String,Object> map = new HashMap<>();
        String nonceStr = UUID.randomUUID().toString().toUpperCase();
        long timeStamp = System.currentTimeMillis()/1000;
        String source=appId+"\n"+timeStamp+"\n"+nonceStr+"\n"+prepayId+"\n";
        String sign = getSign(source.getBytes(StandardCharsets.UTF_8), privateKey);
        map.put("appId", appId);
        map.put("timeStamp", timeStamp);
        map.put("nonceStr", nonceStr);
        map.put("package", prepayId);
        map.put("signType", "RSA");
        map.put("paySign", sign);
        return map;
    }

    /** 获取微信微信支付签名 */
    private String getSign(byte[] message, PrivateKey yourPrivateKey) {
        try {
            // 签名方式（固定SHA256withRSA）
            Signature sign = Signature.getInstance("SHA256withRSA");
            // 使用私钥进行初始化签名（私钥需要从私钥文件【证书】中读取）
            sign.initSign(yourPrivateKey);
            // 签名更新
            sign.update(message);
            // 对签名结果进行Base64编码
            return Base64.getEncoder().encodeToString(sign.sign());
        } catch (Exception e) {
            log.error("获取微信支付签名失败，错误信息为：{}",e);
            throw new RuntimeException("获取微信支付签名失败");
        }
    }

}
