package com.mzj.mohome.serviceImp;

import com.alibaba.fastjson.JSON;
import com.mzj.mohome.mapper.UserMapper;
import com.mzj.mohome.service.WxTemplateService;
import com.mzj.mohome.util.RequestApi;
import com.mzj.mohome.util.WxPayConfig;
import com.mzj.mohome.vo.WeChatTemplateMsg;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *微信公众号推送模板信息
 * @date 2023-11-19
 */
@Slf4j
@Service
public class WxTemplateServiceImp implements WxTemplateService {
    @Resource
    private WxPayConfig wxPayConfig;
    @Resource
    private UserMapper userMapper;
    @Resource
    private RestTemplate myRestTemplate;
    public String getAccessToken(){
        //token保留两个小时，如果已存在，则直接可以用，否，则重新调用获取
        String accessToken = userMapper.getToken(wxPayConfig.getWeChatAppId());
        if(StringUtils.isEmpty(accessToken)){
            String requestUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+ wxPayConfig.getWeChatAppId() +"&secret=" + wxPayConfig.getWeChatAppsecret();
            log.info(requestUrl);
            String result=myRestTemplate.exchange(requestUrl, HttpMethod.GET,null,String.class).getBody();
            log.info("getAccessToken：===返回信息为：{}", result);
            JSONObject jsonObject = JSONObject.fromObject(result);
            accessToken = jsonObject.getString("access_token");
            log.info("accessToken：{}", accessToken);
            if(StringUtils.isNotEmpty(accessToken)){
                userMapper.updTokenInfo(wxPayConfig.getWeChatAppId(),accessToken);
            }
        }
        return accessToken;
    }

    public Map<String,Object> getUserList(){
        Map<String,Object> map = new HashMap<>();
        map.put("success",false);
        map.put("msg","发送失败");
        RestTemplate restTemplate = new RestTemplate();
        String accessToken = getAccessToken();
        String requestUrl =  "https://api.weixin.qq.com/cgi-bin/user/get?access_token="+ accessToken;
        String result=restTemplate.exchange(requestUrl, HttpMethod.POST,null,String.class).getBody();
        log.info("getUserList===获取用户列表结果是: {}",result);
        JSONObject jsonObject = JSONObject.fromObject(result);
        JSONArray jsonArray =  jsonObject.getJSONObject("data").getJSONArray("openid");
        Iterator iterator = jsonArray.iterator();
        while (iterator.hasNext()){
            log.info("用户openid："+iterator.next());
        }
       /* com.alibaba.fastjson.JSONObject result = com.alibaba.fastjson.JSONObject.parseObject(response.getBody());
        com.alibaba.fastjson.JSONArray openIdJsonArray = result.getJSONObject("data").getJSONArray("openid");
        Iterator iterator = openIdJsonArray.iterator();
        if (iterator.hasNext()){
            log.debug("用户openid："+iterator.next());
        }*/
        // openId代表一个唯一微信用户，即微信消息的接收人,为空的话，则调用失败，或者该用户未关注公众号
       String openId = "oQr3g6o9bk0c0PsvKRqmT4439I7M";
       if(StringUtils.isNotEmpty(openId)){
           if(sendMessage(openId,accessToken)){
               map.put("success",true);
               map.put("msg","发送成功");
           }
       }
        return map;
    }

    public boolean sendMessage(String openId,String accessToken){
        // 模板参数
        Map<String,WeChatTemplateMsg> sendMag = new HashMap<>();
        // 公众号的模板id(也有相应的接口可以查询到)
        String templateId = "_n6zAW4Tpa-862c9vlEY_pZvWjrHyOy_ktyvCuDS_4k";
        // 微信的基础accessToken
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accessToken;
        sendMag.put("thing3", new WeChatTemplateMsg("美甲"));
        sendMag.put("thing2", new WeChatTemplateMsg("张三"));
        sendMag.put("time4", new WeChatTemplateMsg("2022-11-24 10:37"));
        sendMag.put("phone_number8", new WeChatTemplateMsg("18598658655"));
        sendMag.put("thing5", new WeChatTemplateMsg("云南省昆明市金鼎科技园"));


        //拼接base参数
        Map<String, Object> sendBody = new HashMap<>();
        sendBody.put("touser", openId);               // openId
        sendBody.put("url", "http://wx.mzjsh.com");  //跳转网页url
        sendBody.put("data", sendMag);                   // 模板参数
        sendBody.put("template_id", templateId);      // 模板Id
        log.info("sendBody====="+ JSON.toJSONString(sendBody));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        // 请求体设置
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(sendBody,
                httpHeaders);
        ResponseEntity<JSONObject> result=myRestTemplate.exchange(requestUrl, HttpMethod.POST,httpEntity,JSONObject.class);
        log.info("sendMessage===推送模板结果是: {}",JSON.toJSONString(result));
        JSONObject body = result.getBody();
        if(body.get("errcode").toString() == "0"){
            return true;
       }
        return false;
    }

}
