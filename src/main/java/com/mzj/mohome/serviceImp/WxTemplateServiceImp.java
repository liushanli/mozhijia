package com.mzj.mohome.serviceImp;

import com.alibaba.fastjson.JSON;
import com.mzj.mohome.mapper.OrderMapper;
import com.mzj.mohome.mapper.UserMapper;
import com.mzj.mohome.service.WxTemplateService;
import com.mzj.mohome.util.ToolsUtil;
import com.mzj.mohome.util.WxPayConfig;
import com.mzj.mohome.vo.OrderVo;
import com.mzj.mohome.vo.WeChatTemplateMsg;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.DefaultHttpParams;
import java.io.IOException;
import java.security.MessageDigest;

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

    @Autowired
    private OrderMapper orderMapper;


    /**
     * 每两个小时获取token一次
     */
    /*@Scheduled(cron = "0 0 0/2 * * ?")
    @Async*/
    public Map<String,String> getAccessToken(){
        Map<String,String> map = new HashMap<>();
        String accessToken = "";
        try {
            map.put("token","");
            map.put("Ticket","");
            //token保留两个小时，如果已存在，则直接可以用，否，则重新调用获取
            String requestUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+ wxPayConfig.getWeChatAppId() +"&secret=" + wxPayConfig.getWeChatAppsecret();
            log.info(requestUrl);
            String result=myRestTemplate.exchange(requestUrl, HttpMethod.GET,null,String.class).getBody();
            log.info("getAccessToken：===返回信息为：{}", result);
            JSONObject jsonObject = JSONObject.fromObject(result);
            accessToken = jsonObject.getString("access_token");
            log.info("accessToken：{}", accessToken);
            if(StringUtils.isNotEmpty(accessToken)){
                userMapper.updTokenInfo(wxPayConfig.getWeChatAppId(),accessToken);
                //获取jsTicket信息
                String ticket = getJsapiTicket(accessToken);
                map.put("token",accessToken);
                map.put("ticket",ticket);
            }
        }catch (Exception e){
            log.error("获取token信息失败，错误信息为：{}",e);
        }
        return map;
    }
    /**
     * 每天晚上十一点获取一次公众号
     * @return
     */
    /*@Scheduled(cron = "0 0 23 * * ?")
    @Async*/
    public void getUserList(){
        log.info("getUserList====获取公众号用户信息===start");
        Map<String,Object> map = new HashMap<>();
        map.put("success",false);
        map.put("msg","发送失败");
        RestTemplate restTemplate = new RestTemplate();
        String accessToken = userMapper.getToken(wxPayConfig.getWeChatAppId());
        if(StringUtils.isEmpty(accessToken)){
            return;
        }
        Integer total = 0;
        Integer count = 0;
        String next_openid = "";
        boolean flag = false;
        int status = 0;
        String requestUrl =  "https://api.weixin.qq.com/cgi-bin/user/get?access_token="+ accessToken;
        do{
            String result=restTemplate.exchange(requestUrl, HttpMethod.POST,null,String.class).getBody();
            log.info("getUserList===获取用户列表结果是: {}",result);
            JSONObject jsonObject = JSONObject.fromObject(result);
            log.info("errcode===="+jsonObject.get("errcode"));
            if(jsonObject.get("errcode") == null) {
                if(status == 0){
                    userMapper.delWxOpenInfo();
                    status = 1;
                }
                JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("openid");
                total = Integer.parseInt(jsonObject.getString("total"));
                count += Integer.parseInt(jsonObject.getString("count"));
                next_openid = jsonObject.getString("next_openid");
                if (total > count) {
                    requestUrl += "&next_openid=" + next_openid;
                    flag = true;
                }
                Iterator iterator = jsonArray.iterator();
                //获取
                while (iterator.hasNext()) {
                    try {
                        userMapper.addWxOpenInfo(String.valueOf(iterator.next()));
                    }catch (Exception e){
                        log.info("getUserList===获取公众号用户信息失败，错误信息为：{}",e);
                    }
                }
            }
        }while (flag);
        log.info("getUserList====获取公众号用户信息===end");
    }

    /**
     * 每五分钟查询是否有接单成功消息
     */
    /*@Scheduled(cron = "0 0/3 * * * ?")
    @Async*/
    public Map<String,Object> sendMessage(String orderId){
        Map<String,Object> resultMap = new HashMap<>();
        Map<String, Object> mapperPayRecordById = userMapper.findPayRecordById(orderId);

        resultMap.put("success",false);
        resultMap.put("msg","发送失败，请稍后重试");
        log.info("=sendMessage=开始发送接单通知===:{}",orderId);
        //第一步先查询数据库
        String accessToken = userMapper.getToken(wxPayConfig.getWeChatAppId());
        //第二步为空的时候,则进行查询接口,如果还没有的话,则不往下进行执行
        if(StringUtils.isEmpty(accessToken)){
            log.info("sendMessage====token为空，则不能进行发送接单成功消息,重新调用ticket");
            Map<String,String> map = getAccessToken();
            accessToken = map.get("token");
        }
        if(StringUtils.isEmpty(accessToken)){
            log.info("sendMessage====token为空，获取用户token失败");
            return resultMap;
        }
        List<OrderVo> list = new ArrayList<>();
        List<OrderVo> orderVoList = orderMapper.findUserOpenId(orderId);
        if(orderVoList == null || orderVoList.size()==0){
            log.info("sendMessage==技师==获取不到发送模板消息，则不能进行发送接单成功消息");
        }else{
            list.addAll(orderVoList);
        }
        List<OrderVo> shopList = orderMapper.sendShop(orderId);
        if(shopList == null || shopList.size()==0){
            log.info("sendMessage==商家==获取不到发送商家模板消息，则不能进行发送接单成功消息");
            return resultMap;
        }else{
            list.addAll(shopList);
        }
        log.info("发送消息为：{}",JSON.toJSONString(shopList));
        /*if(StringUtils.isNotEmpty(openId)){
            //判断商家是否有信息，如果有，则进行添加
            log.info("openId=={}",openId);
            orderVoInfo.setOpenId(openId);
            orderVoList.add(orderVoInfo);
            log.info("sendMessage======商家信息为：{}",JSON.toJSONString(orderVoInfo));
        }*/
        // 公众号的模板id(也有相应的接口可以查询到)
        String templateId = "GkJihpsGXUYsvaMytBwndX2TngVZBOEppY9zY7BdBWg";
        //List<OrderVo>  orderVoList1 = new ArrayList<>();
        // 微信的基础accessToken
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accessToken;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (OrderVo orderVo : list) {
                // 模板参数
                Map<String, WeChatTemplateMsg> sendMag = new HashMap<>();

                //商品名称
                //{{thing15.DATA}}
                sendMag.put("thing15", new WeChatTemplateMsg(orderVo.getProductName()));
                //预约时间
                //{{time14.DATA}}
                sendMag.put("time14", new WeChatTemplateMsg(simpleDateFormat.format(orderVo.getAboutTime())));
                String sourceType = "APP";
                if (StringUtils.isNotEmpty(orderVo.getSourceType()) && orderVo.getSourceType().equals("1")) {
                    sourceType = "公众号";
                }
                //订单渠道
                //{{thing12.DATA}}
                sendMag.put("thing12", new WeChatTemplateMsg(sourceType));

                String phone = orderVo.getPhone().substring(0,3)+"****"+orderVo.getPhone().substring(7,11);
                //客户电话
                //{{phone_number3.DATA}}
                sendMag.put("phone_number3", new WeChatTemplateMsg(phone));
                String address = orderVo.getAddress().replace(" ", "");
                if (address.length() >= 20) {
                    address = address.substring(0, 15) + "...";
                }
                //下单地址
                //{{thing4.DATA}}
                sendMag.put("thing4", new WeChatTemplateMsg(address));

                //拼接base参数
                Map<String, Object> sendBody = new HashMap<>();
                sendBody.put("touser", orderVo.getOpenId());               // openId
                sendBody.put("url", "http://wx.mzjsh.com:9999/pages/jishi_order_detail/jishi_order_detail?orderId=" + orderVo.getOrderId());  //跳转网页url
                sendBody.put("data", sendMag);                   // 模板参数
                sendBody.put("template_id", templateId);      // 模板Id
                log.info("sendBody=====" + JSON.toJSONString(sendBody));
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                // 请求体设置
                HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(sendBody,
                        httpHeaders);
                ResponseEntity<JSONObject> result = myRestTemplate.exchange(requestUrl, HttpMethod.POST, httpEntity, JSONObject.class);
                log.info("sendMessage==该订单orderId：{}====推送模板结果是: {}", orderVo.getOrderId(), JSON.toJSONString(result));
                JSONObject body = result.getBody();
                if (body.get("errcode").equals(0)) {
                    orderMapper.updateInfoNew(orderVo.getWorkerId());
                    resultMap.put("success", true);
                    resultMap.put("msg", "发送成功");
                    //推送成功，则进行修改数据库的消息，已发送
                    orderMapper.updOpenId(orderVo.getOrderId());
                }
            }
        log.info("=sendMessage=结束发送接单通知===");
        resultMap.put("success",true);
        resultMap.put("status",mapperPayRecordById.get("status"));
        resultMap.put("payTime",mapperPayRecordById.get("payTime"));
        resultMap.put("surplusMoney",mapperPayRecordById.get("surplusMoney"));
        return resultMap;
    }

    //第二步获取JsapiTicket
    public String getJsapiTicket(String accessToken) {
        String result = "";
        String ticket = "";
        try {
            String JSAPI_TICKET="https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=@accessToken&type=jsapi";
            HttpClient client = new HttpClient();
            String ticketURL = JSAPI_TICKET.replace("@accessToken",accessToken);
            GetMethod getMethod = new GetMethod(ticketURL);
            DefaultHttpParams.getDefaultParams().setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);

            client.executeMethod(getMethod);
            result = new String(getMethod.getResponseBodyAsString().getBytes("gbk"));
            log.info("getJsapiTicket==获取到的信息为：===result===="+result);
            // 将数据转换成json
            JSONObject jasonObject = JSONObject.fromObject(result);
            result = (String) jasonObject.get("ticket");
            getMethod.releaseConnection();
            if(ToolsUtil.getString(jasonObject.get("errcode")) != null && ToolsUtil.getString(jasonObject.get("errcode")).equals("0")){
                userMapper.updJsTicket(wxPayConfig.getWeChatAppId(),ToolsUtil.getString(jasonObject.get("ticket")));
                ticket = ToolsUtil.getString(jasonObject.get("ticket"));
            }
        } catch (IOException e) {
            log.error("getJsapiTicket==失败，异常信息为：{}",e);
        }
        return ticket;
    }

    //第三步:通过SHA1加密方式 获取signature 及其他参数信息
    public Map<String,Object> fn_GetShareData(String url){
        Map<String,Object> jsonMap=new HashMap<>();
        try {
            jsonMap.put("success",false);
            jsonMap.put("msg","获取信息失败");
            String jsapiTicket="";//ticket
            String nonceStr=create_nonce_str();//生成签名的随机串
            String signature="";//签名
            String timestamp=String.valueOf(create_timestamp());//时间戳
            String accessToken = userMapper.getToken(wxPayConfig.getWeChatAppId());
            jsapiTicket = userMapper.getJsTicket(wxPayConfig.getWeChatAppId());
            if(StringUtils.isEmpty(accessToken) || StringUtils.isEmpty(jsapiTicket)){
                Map<String,String> map = getAccessToken();
                accessToken = map.get("token");
                jsapiTicket = map.get("ticket");
            }
            if(StringUtils.isNotEmpty(accessToken) && StringUtils.isNotEmpty(jsapiTicket)){
                String signatureSha="jsapi_ticket=@ticket&noncestr=@noncestr&timestamp=@timestamp&url=@url";
                signatureSha=signatureSha.replace("@ticket",jsapiTicket).replace("@noncestr",nonceStr)
                        .replace("@timestamp",timestamp).replace("@url",url);

                MessageDigest crypt = MessageDigest.getInstance("SHA-1");
                crypt.reset();
                crypt.update(signatureSha.getBytes("UTF-8"));
                signature = byteToHex(crypt.digest());
                jsonMap.put("appId",wxPayConfig.getWeChatAppId());
                jsonMap.put("timestamp",timestamp);
                jsonMap.put("nonceStr",nonceStr);
                jsonMap.put("signature",signature);
                jsonMap.put("success",true);
                jsonMap.put("msg","");
            }
            log.info("fn_GetShareData====:{}", JSON.toJSONString(jsonMap));
        }catch (Exception e){
            jsonMap.put("success",false);
            jsonMap.put("msg","获取信息失败");
            log.error("fn_GetShareData=========错误信息为：{}",e);
        }
        return jsonMap;
    }




    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    private static Long create_timestamp() {
        return (System.currentTimeMillis() / 1000);
    }
}
