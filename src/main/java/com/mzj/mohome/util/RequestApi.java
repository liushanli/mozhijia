package com.mzj.mohome.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author admin
 * @ClassName RequestApi
 * @Description RestTemplate请求API
 * @Date 2023/10/24 10:41
 */
@Service
@Slf4j
public class RequestApi {

    //    private RestTemplate restTemplate = new RestTemplate();//RestTemplate默认不处理HTTP响应码为400、500这类响应结果，直接抛异常
    //注入restTemplate bean
    @Resource
    private RestTemplate myRestTemplate;

    public JSONObject getApi(String url, Map<String, String> parameters) {
        try {
            log.info("Start to get coverage api,url is {}，parameters：{} ", url,JSONObject.toJSONString(parameters));
            //Set url, restTemplate请求的url后面要有占位符
            //url=url+"?";
            StringBuffer sb = new StringBuffer(url);
            if(parameters != null && parameters.size()>0){
                sb = sb.append("?");
                //sb=sb.append("?Prameter1={pam1}&Name={Name}&Type={Type}");//get请求得参数，需要先占位 才能请求成功
                for (String key : parameters.keySet()) {
                    sb.append(key + "={" + key + "}&");
                }
                //去掉最后的&符号
                sb = new StringBuffer(sb.substring(0, sb.lastIndexOf("&")));
            }
            //获取资源的地址，get请求
            //返回类型设为String
            String response = myRestTemplate.getForObject(sb.toString(),
                    String.class,
                    parameters
            );
            log.info("请求返回信息为："+ JSON.toJSONString(response));
            //获取respons
            JSONObject body = JSONObject.parseObject(response);
            return body;
        } catch (Exception e) {
            log.error("Invoke  apifailed!exception is {}",e);
            return null;
        }
    }
}
