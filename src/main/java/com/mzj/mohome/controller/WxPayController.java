package com.mzj.mohome.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.mzj.mohome.service.WxPayService;
import com.mzj.mohome.util.RequestApi;
import com.mzj.mohome.util.WxPayConfig;
import com.mzj.mohome.vo.OrderInfo;
import com.mzj.mohome.vo.R;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.notification.Notification;
import com.wechat.pay.contrib.apache.httpclient.notification.NotificationHandler;
import com.wechat.pay.contrib.apache.httpclient.notification.NotificationRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


@CrossOrigin
@RestController
@RequestMapping("/api/wx-pay")
@Api(tags = "网站微信支付API")
@Slf4j
public class WxPayController{

    @Resource
    private WxPayService wxPayService;

    @Resource
    private WxPayConfig wxPayConfig;

    @Resource
    private Verifier verifier;

    @Autowired
    private RequestApi requestApi;

    @ApiOperation("调用统一下单API，生成预支付交易会话标识")
    @PostMapping("/jsapi")
    public R jsapiPay(@RequestBody OrderInfo orderInfo) throws Exception{
        log.info("jsapiPay===请求参数为:"+ JSON.toJSONString(orderInfo));
        Map<String,Object> map = new HashMap<>();
        String openId = orderInfo.getOpenId();
        //获取微信openId
        if(StringUtils.isNotEmpty(openId)){
            log.info("jsapiPay===发起支付请求:"+ JSON.toJSONString(orderInfo));
            map = wxPayService.jsApiPay(orderInfo);
        }else{
            String appId = "wx0e98389493b8b0a9";
            String secret = "df2af6daefc5c45f636ff4f2b67b6d58";
            Map<String,String> map_1 = new HashMap<>();
            map_1.put("appid",appId);
            map_1.put("secret",secret);
            map_1.put("code",orderInfo.getCode());
            map_1.put("grant_type","authorization_code");
            String url = "https://api.weixin.qq.com/sns/oauth2/access_token";
            JSONObject jsonObject = requestApi.getApi(url,map_1);
            log.info("jsapiPay===微信获取token信息======"+jsonObject.toJSONString());
            if(jsonObject.getString("openid") !=null){
                openId = jsonObject.getString("openid");
                orderInfo.setOpenId(openId);
                log.info("jsapiPay===发起支付请求:"+ JSON.toJSONString(orderInfo));
                map = wxPayService.jsApiPay(orderInfo);
            }else{
                map.put("success",false);
                map.put("msg",jsonObject.get("errmsg"));
            }
        }
        log.info("jsapiPay===返回参数为:"+ JSON.toJSONString(map));
        return R.ok().setData(map);
    }

    @ApiOperation("微信支付回调")
    @PostMapping("/jsapi/notify")
    public R jsapiNotify(HttpServletRequest req, HttpServletResponse res){
        try {
            String body = readData(req);
            // 构建request，传入必要参数
            NotificationRequest request = new NotificationRequest.Builder().withSerialNumber(req.getHeader("Wechatpay-Serial"))
                    .withNonce(req.getHeader("Wechatpay-Nonce"))
                    .withTimestamp(req.getHeader("Wechatpay-Timestamp"))
                    .withSignature(req.getHeader("Wechatpay-Signature"))
                    .withBody(body)
                    .build();
            NotificationHandler handler = new NotificationHandler(verifier, wxPayConfig.getApiV3Key().getBytes(StandardCharsets.UTF_8));
            // 验签和解析请求体
            Notification notification = handler.parse(request);
            // 从notification中获取解密报文
            String decryptData = notification.getDecryptData();
            //开始你的业务逻辑

            res.setStatus(200);
            return R.ok();
        }catch (Exception e){
            throw new RuntimeException("解密失败===>"+e.getMessage());
        }
    }

    /**
     * 读取微信支付信息
     * @param request 信息
     * @return 结果
     */
    private static String readData(HttpServletRequest request) {
        BufferedReader br = null;
        try {
            StringBuilder result = new StringBuilder();
            br = request.getReader();
            for (String line; (line = br.readLine()) != null; ) {
                if (result.length() > 0) {
                    result.append("\n");
                }
                result.append(line);
            }
            return result.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
