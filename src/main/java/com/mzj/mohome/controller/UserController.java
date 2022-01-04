package com.mzj.mohome.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.mzj.mohome.entity.*;
import com.mzj.mohome.service.UserService;

import com.mzj.mohome.util.AlipayConfig;
import com.mzj.mohome.util.AppleUtil;
import com.mzj.mohome.util.ToolsUtil;
import lombok.extern.slf4j.XSlf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/user")
public class UserController {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;


    @ResponseBody
    @RequestMapping("/findAllUser")
    public Map<String,Object> findAllUser(String id){
        Map<String,Object> map = new HashMap<>();
        try{
            logger.info("=====findAllUser=====");
            if(StringUtils.isNotEmpty(id)){
                List<User> userList = userService.getUser(Integer.parseInt(id));
                map.put("userList",userList);
            }
            /*String jsonData = "{employees:[{'firstName':'Bill','lastName':'Gills'},{'firstName':'Bill2','lastName':'Gills2'},{'firstName':'Bill3','lastName':'Gills3'}]}";
            JSONObject jsonObject = JSON.parseObject(jsonData);*/
        }catch (Exception e){
            logger.error("findAllUser报错=="+e.getMessage());
        }

        return map;
    }

    @ResponseBody
    @GetMapping("/findUserByPhone")
    public Map<String,Object> findUserByPhone(String phone,String sendCode,String openId,String appleData){
        Map<String,Object> map = new HashMap<>();
        try{
            logger.info("=====findUserByPhone====");
            map.put("success",true);
            map.put("msg","");
            List<User> userList = userService.getByUserExist(phone,sendCode,openId,appleData);
           if(userList != null && userList.size()>0){
                User userVo = userList.get(0);
                map.put("userVo",userVo);
            }else{
                map.put("userVo","");
            }
        }catch (Exception e){
            map.put("msg","系统出现异常，请稍后重试");
            map.put("success","false");
            map.put("userVo","");
            logger.error("findUserByPhone=="+e.getMessage());
        }

        return map;
    }


    @ResponseBody
    @RequestMapping(value="/findUserByUserId",method=RequestMethod.POST)
    public Map<String,Object> findUserByUserId(@RequestBody Map<String,Object> map_1){
        Map<String,Object> map = new HashMap<>();
        try{
            logger.info("=======findUserByUserId=======");
            String userId = map_1.get("userId").toString();
            //String version = map_1.get("version").toString();
            String version = "";
            if(map_1.get("version")!=null){
                version = String.valueOf(map_1.get("version"));
            }
            map.put("success",true);
            map.put("msg","");
            List<User> userList = userService.getByUserInfoById(userId,version);
            int count = userService.findCouponCount(userId);
            map.put("count",count);
            if(userList != null && userList.size()>0){
                User userVo = userList.get(0);
                map.put("userVo",userVo);
            }else {
                map.put("userVo", "");
            }
        }catch (Exception e){
            logger.error("findUserByUserId==="+e.getMessage());
            map.put("msg","系统出现异常，请稍后重试");
            map.put("success","false");
            map.put("userVo","");
        }
        return map;
    }


    @ResponseBody
    @RequestMapping("/findUserByPhoneCount")
    public Map<String,Object> findUserByPhoneCount(String phone){
        Map<String,Object> map = new HashMap<>();
        try{
            logger.error("==-findUserByPhoneCount-===");
            List<User> users = userService.findUserPhone(phone);
            map.put("success",true);
            map.put("msg","");
            map.put("userList",users);
        }catch (Exception e){
            logger.error("==--findUserByPhoneCount==="+e.getMessage());
            map.put("msg","系统出现异常，请稍后重试");
            map.put("success","false");
            map.put("userVo","");
            map.put("userList",false);
        }
        return map;
    }

    @ResponseBody
    @RequestMapping(value="/SmsSendMsg",method=RequestMethod.POST)
    public Map<String,Object> SmsSendMsg(String phone){
        Map<String,Object> map = new HashMap<String,Object>();
        try{
            logger.info("====SmsSendMsg====");
            String str = userService.SmsSendCode(phone);
            map.put("success",true);
            map.put("msg","");
            if(StringUtils.isNotEmpty(str)){
                map.put("success",false);
                map.put("msg",str);
            }
        }catch (Exception e){
            logger.info("SmsSendMsg=="+e.getMessage());
            map.put("success",false);
            map.put("msg",e.getMessage());
        }
       /* map.put("success",false);
        map.put("msg","版本过低，请更新最新版本");*/
        return map;
    }

    @ResponseBody
    @RequestMapping(value="/SmsSendMsgNew",method=RequestMethod.POST)
    public Map<String,Object> SmsSendMsgNew(String phone){
        Map<String,Object> map = new HashMap<String,Object>();
        try{
            logger.info("====SmsSendMsgNew====");
            String str = userService.SmsSendCode(phone);
            map.put("success",true);
            map.put("msg","");
            if(StringUtils.isNotEmpty(str)){
                map.put("success",false);
                map.put("msg",str);
            }
        }catch (Exception e){
            logger.info("SmsSendMsg=="+e.getMessage());
            map.put("success",false);
            map.put("msg",e.getMessage());
        }
        return map;
    }

    @ResponseBody
    @RequestMapping(value="/SmsSendMsgOrder",method=RequestMethod.POST)
    public Map<String,Object> SmsSendMsgOrder(@RequestBody Map<String,Object> map_1){

        Map<String,Object> map = new HashMap<String,Object>();
        try{
            logger.info("====SmsSendMsgOrder===");
            String orderId = map_1.get("orderId").toString();
            Map<String,Object> stringObjectMap = userService.SmsSendCodeJishi(orderId);
            map.put("success",true);
            map.put("status",stringObjectMap.get("status"));
            map.put("payTime",stringObjectMap.get("payTime"));
            map.put("surplusMoney",stringObjectMap.get("surplusMoney"));
        }catch (Exception e){
            logger.info("--SmsSendMsgOrder-===="+e.getMessage());
            map.put("success",false);
            map.put("status",e.getMessage());
            map.put("payTime",null);
            map.put("surplusMoney",null);
        }
        return map;
    }

    @ResponseBody
    @RequestMapping("findProvinceInfo")
    public  Map<String,Object> findProvinceInfo(@RequestBody Map<String,Object> map){
        logger.info("=====findProvinceInfo=======");
        String level = (String) map.get("level");
        String pid = String.valueOf(map.get("pid"));
        Map<String,Object>  objectMap = userService.findProvinceInfo(level,pid);
        return objectMap;
    }

    @ResponseBody
    @RequestMapping("findBannerList")
    public  Map<String,Object> findBannerList(){
        Map<String,Object> objectMap = new HashMap<>();
        try {
            logger.info("====findBannerList====");
            List<AppBanner> list = userService.findBannerList();
            objectMap.put("bannerList",list);
            objectMap.put("success",true);
            logger.info("bannerList="+list.size());
        }catch (Exception e){
            logger.error("====findBannerList="+e.getMessage());
        }
       return objectMap;
    }
    @ResponseBody
    @RequestMapping("findRepayList")
    public Map<String,Object> findRepayList(@RequestBody Map<String,Object> map){
        Map<String,Object> objectMap = new HashMap<String,Object>();
        String out_trade_no=new SimpleDateFormat("yyyymmddmmhhss").format(new Date());//产生一组随机的订单号
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL,AlipayConfig.APPID,AlipayConfig.RSA_PRIVATE_KEY,AlipayConfig.FORMAT,AlipayConfig.CHARSET,AlipayConfig.ALIPAY_PUBLIC_KEY,AlipayConfig.SIGNTYPE);
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody(map.get("content")!=null?String.valueOf(map.get("content")):"");
        model.setSubject(map.get("subject")!=null?String.valueOf(map.get("subject")):"");
        model.setOutTradeNo(out_trade_no);
        model.setTimeoutExpress("30m");
        model.setTotalAmount(map.get("totalAmount")!=null?String.valueOf(map.get("totalAmount")):"");
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        //request.setNotifyUrl("商户外网可以访问的异步地址");
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
            objectMap.put("order",response.getBody());
            objectMap.put("out_trade_no",out_trade_no);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return objectMap;
    }

    //添加企业定制
    @ResponseBody
    @PostMapping("/addCompanyMade")
    public Map<String,Object> addCompanyMade(@RequestBody Map<String,Object> map){
        Map<String,Object> result_map = new HashMap<String,Object>();
        try {
            int count = userService.addCompanyInfo(map);
            if (count > 0) {
                result_map.put("success", true);
                result_map.put("message", "");
            } else {
                result_map.put("success", false);
                result_map.put("message", "保存错误");
            }
        }catch (Exception e){
            result_map.put("success", false);
            result_map.put("message", "系统出现错误");
        }
        return result_map;
    }

    @ResponseBody
    @PostMapping("/updUserInfo")
    public Map<String,Object> updUserInfo(@RequestBody Map<String,Object> map){
        Map<String,Object> result_map = new HashMap<String,Object>();
        try {
            int count = userService.updUser(map);
            if (count > 0) {
                result_map.put("success", true);
                result_map.put("message", "");
            } else {
                result_map.put("success", false);
                result_map.put("message", "更改错误");
            }
        }catch (Exception e){
            result_map.put("success", false);
            result_map.put("message", "系统出现错误");
        }
        return result_map;
    }



    @ResponseBody
    @PostMapping("/updUserInfoCard")
    public Map<String,Object> updUserInfoCard(@RequestBody Map<String,Object> map){
        Map<String,Object> result_map = new HashMap<String,Object>();
        try {
            String orderId = userService.updUserInfo(map);
            if (StringUtils.isNotEmpty(orderId)) {
                result_map.put("success", true);
                result_map.put("message", "");
                result_map.put("orderId",orderId);
            } else {
                result_map.put("success", false);
                result_map.put("message", "更改错误");
                result_map.put("orderId",null);
            }
        }catch (Exception e){
            result_map.put("success", false);
            result_map.put("message", "系统出现错误");
            result_map.put("orderId",null);
        }
        return result_map;
    }



    @ResponseBody
    @PostMapping("/findCardList")
    public Map<String,Object> findCardList(){
        Map<String,Object> result_map = new HashMap<String,Object>();
        try {
            List<Card> cardList= userService.findCardList();
            result_map.put("success", true);
            result_map.put("cardList", cardList);
        }catch (Exception e){
            result_map.put("success", false);
            result_map.put("cardList", "null");
        }
        return result_map;
    }


    @ResponseBody
    @PostMapping("/addUpUserAddress")
    public Map<String,Object> addUpUserAddress(@RequestBody Map<String,Object> map){
        Map<String,Object> result_map = new HashMap<String,Object>();
        try {
            int count = userService.addUPDTbAddress(map);
            if (count > 0) {
                result_map.put("success", true);
                result_map.put("message", "");
            } else {
                result_map.put("success", false);
                result_map.put("message", "修改失败");
            }
        }catch (Exception e){
            result_map.put("success", false);
            result_map.put("message", "系统出现错误");
        }
        return result_map;
    }

    @ResponseBody
    @PostMapping("/findUserAddress")
    public Map<String,Object> findUserAddress(@RequestBody Map<String,Object> map){
        Map<String,Object> result_map = new HashMap<String,Object>();
        try {
            List<Address> addressList= userService.findAddress(map);
            if (addressList!=null && addressList.size() > 0) {
                result_map.put("success", true);
                result_map.put("message", "");
                result_map.put("addressList",addressList);
            } else {
                result_map.put("success", false);
                result_map.put("message", "查询失败");
                result_map.put("addressList",null);
            }
        }catch (Exception e){
            result_map.put("success", false);
            result_map.put("message", "系统出现问题");
            result_map.put("addressList",null);
        }
        return result_map;
    }

    @ResponseBody
    @PostMapping("/addEvaluate")
    public Map<String,Object> addEvaluate(@RequestBody Map<String,Object> map){
        Map<String,Object> result_map = new HashMap<String,Object>();
        try {
            int count = userService.addEvaluate(map);
            if (count > 0) {
                result_map.put("success", true);
                result_map.put("message", "");
            } else {
                result_map.put("success", false);
                result_map.put("message", "保存错误");
            }
        }catch (Exception e){
            result_map.put("success", false);
            result_map.put("message", "系统出现错误");
        }
        return result_map;
    }


    @ResponseBody
    @RequestMapping(value = "/addPartnet",method = RequestMethod.POST)
    public Map<String,Object> addPartnet(@RequestBody Map<String,Object> map){
        Map<String,Object> result_map = new HashMap<String,Object>();
        try {
            int count = userService.addPartnet(map);
            if (count > 0) {
                result_map.put("success", true);
                result_map.put("message", "");
            } else {
                result_map.put("success", false);
                result_map.put("message", "保存错误");
            }
        }catch (Exception e){
            result_map.put("success", false);
            result_map.put("message", "系统出现错误");
        }
        return result_map;
    }

    @ResponseBody
    @RequestMapping(value = "/addSuggest",method = RequestMethod.POST)
    public Map<String,Object> addSuggest(@RequestBody Map<String,Object> map){
        Map<String,Object> result_map = new HashMap<String,Object>();
        try {
            int count = userService.addSuggest(map);
            if (count > 0) {
                result_map.put("success", true);
                result_map.put("message", "");
            } else {
                result_map.put("success", false);
                result_map.put("message", "保存错误");
            }
        }catch (Exception e){
            result_map.put("success", false);
            result_map.put("message", "系统出现错误");
        }
        return result_map;
    }

    @ResponseBody
    @RequestMapping(value = "/updAddressStatus",method = RequestMethod.POST)
    public Map<String,Object> updAddressStatus(@RequestBody Map<String,Object> map){
        Map<String,Object> result_map = new HashMap<String,Object>();
        try {
            int count = userService.updAddressById(map);
            if (count > 0) {
                result_map.put("success", true);
                result_map.put("message", "");
            } else {
                result_map.put("success", false);
                result_map.put("message", "保存错误");
            }
        }catch (Exception e){
            result_map.put("success", false);
            result_map.put("message", "系统出现错误");
        }
        return result_map;
    }

    @ResponseBody
    @RequestMapping(value = "/delAddressStatus",method = RequestMethod.POST)
    public Map<String,Object> delAddressStatus(@RequestBody Map<String,Object> map){
        Map<String,Object> result_map = new HashMap<String,Object>();
        try {
            int count = userService.delAddressById((Integer)(map.get("id")));
            if (count > 0) {
                result_map.put("success", true);
                result_map.put("message", "");
            } else {
                result_map.put("success", false);
                result_map.put("message", "保存错误");
            }
        }catch (Exception e){
            result_map.put("success", false);
            result_map.put("message", "系统出现错误");
        }
        return result_map;
    }


    @ResponseBody
    @RequestMapping(value = "/addRecruit",method = RequestMethod.POST)
    public Map<String,Object> addRecruit(@RequestBody Recurit recurit){
        Map<String,Object> result_map = new HashMap<String,Object>();
        try {
            int count = userService.addRecurit(recurit);
            if (count > 0) {
                result_map.put("success", true);
                result_map.put("message", "");
            } else {
                result_map.put("success", false);
                result_map.put("message", "保存错误");
            }
        }catch (Exception e){
            result_map.put("success", false);
            result_map.put("message", "系统出现错误");
        }
        return result_map;
    }

    @ResponseBody
    @RequestMapping(value = "/findEvalListInfo",method = RequestMethod.POST)
    public Map<String,Object> findEvalListInfo(@RequestBody Map<String,Object> map){
        Map<String,Object> result_map = new HashMap<String,Object>();
        try {
            String userId = String.valueOf(map.get("userId"));
            List<Map<String,Object>> resultList = userService.findEvaluateListByUserId(userId);
            if (resultList.size() > 0) {
                result_map.put("success", true);
                result_map.put("resultList",resultList);
            } else {
                result_map.put("success", false);
                result_map.put("resultList", null);
            }
        }catch (Exception e){
            result_map.put("success", false);
            result_map.put("resultList", null);
        }
        return result_map;
    }

    @ResponseBody
    @RequestMapping(value = "/findEvalListInfoCon",method = RequestMethod.POST)
    public Map<String,Object> findEvalListInfoCon(@RequestBody Map<String,Object> map){
        Map<String,Object> result_map = new HashMap<>();
        try {
            List<Map<String,Object>> resultList = userService.findEvaluateListByUserIdCon(map);
            if (resultList.size() > 0) {
                result_map.put("success", true);
                result_map.put("resultList",resultList);
            } else {
                result_map.put("success", false);
                result_map.put("resultList", null);
            }
        }catch (Exception e){
            result_map.put("success", false);
            result_map.put("resultList", null);
        }
        return result_map;
    }

    @ResponseBody
    @PostMapping("/updEvalInfo")
    public Map<String,Object> updEvalInfo(@RequestBody Evaluate evaluate){
        Map<String,Object> result_map = new HashMap<String,Object>();
        try {
            int count = userService.updEvalById(evaluate);
            if (count > 0) {
                result_map.put("success", true);
                result_map.put("message", "");
            } else {
                result_map.put("success", false);
                result_map.put("message", "更改失败");
            }
        }catch (Exception e){
            result_map.put("success", false);
            result_map.put("message", "系统出现错误");
        }
        return result_map;
    }

    @ResponseBody
    @PostMapping("/delEvalInfo")
    public Map<String,Object> delEvalInfo(String id){
        Map<String,Object> result_map = new HashMap<String,Object>();
        try {
            int count = userService.deleEvaluateListById(id);
            if (count > 0) {
                result_map.put("success", true);
                result_map.put("message", "");
            }else{
                result_map.put("success", false);
                result_map.put("message", "删除失败");
            }
        }catch (Exception e){
            result_map.put("success", false);
            result_map.put("message", "系统出现错误");
        }
        return result_map;
    }

    @ResponseBody
    @PostMapping("/findEvalInfoById")
    public Map<String,Object> findEvalInfoById(String id){
        Map<String,Object> result_map = new HashMap<String,Object>();
        try {
            Map<String,Object> evaluate = userService.findEvalById(id);
            if (evaluate != null) {
                result_map.put("success", true);
                result_map.put("evaluate",evaluate);
            }else{
                result_map.put("success", false);
                result_map.put("evaluate",null);
            }
        }catch (Exception e){
            result_map.put("success", false);
            result_map.put("evaluate",null);
            System.out.println("查询="+e.getMessage());
        }
        return result_map;
    }

    @ResponseBody
        @PostMapping("/getUserInfo")
    public Map<String,Object> getUserInfo(@RequestBody Map<String,Object> map){
        Map<String,Object> result_map = new HashMap<String,Object>();
        try {
            //对identityToken解码
            String identityToken = map.get("identityToken").toString();
            JSONObject json = AppleUtil.parserIdentityToken(identityToken);

            result_map.put("json",json);
            result_map.put("success", true);
            result_map.put("message", "");
           /* if (count > 0) {
                String openId = ToolsUtil.getString(map.get("openId"));
                String phone = ToolsUtil.getString(map.get("phone"));
                String appleData = ToolsUtil.getString(map.get("appleData"));
                List<User> userList = userService.getByUserExist(phone,"",openId,appleData);
                User userVo = userList.get(0);
                result_map.put("userVo",userVo);
                result_map.put("success", true);
                result_map.put("message", "");
            } else {
                result_map.put("success", false);
                result_map.put("message", "更改失败");
            }*/
        }catch (Exception e){
            result_map.put("success", false);
            result_map.put("message", "系统出现错误");
        }
        return result_map;
    }

    @ResponseBody
    @PostMapping("/addWhatUserInfo")
    public Map<String,Object> addWhatUserInfo(@RequestBody Map<String,Object> map){
        Map<String,Object> result_map = new HashMap<String,Object>();
        try {
            int count = userService.addWhatUserInfo(map);
            if (count > 0) {
                String openId = ToolsUtil.getString(map.get("openId"));
                String phone = ToolsUtil.getString(map.get("phone"));
                String appleData = ToolsUtil.getString(map.get("appleData"));
                List<User> userList = userService.getByUserExist(phone,"",openId,appleData);
                User userVo = userList.get(0);
                result_map.put("userVo",userVo);
                result_map.put("success", true);
                result_map.put("message", "");
            } else {
                result_map.put("success", false);
                result_map.put("message", "更改失败");
            }
        }catch (Exception e){
            result_map.put("success", false);
            result_map.put("message", "系统出现错误");
        }
        return result_map;
    }

    @ResponseBody
    @PostMapping("/updWhatUserInfo")
    public Map<String,Object> updWhatUserInfo(@RequestBody Map<String,Object> map){
        Map<String,Object> result_map = new HashMap<String,Object>();
        try {
            int count = userService.updUserInfoByPhone(map);
            if (count > 0) {
                result_map.put("success", true);
                result_map.put("message", "");
            } else {
                result_map.put("success", false);
                result_map.put("message", "更改失败");
            }
        }catch (Exception e){
            result_map.put("success", false);
            result_map.put("message", "系统出现错误");
        }
        return result_map;
    }

    @ResponseBody
    @PostMapping("/findAccountInfo")
    public Map<String,Object> findAccountInfo(){
        Map<String,Object> result_map = new HashMap<String,Object>();
        try {
            List<Map<String,Object>> list = userService.findAccountInfo();
            if (list.size() > 0 && list != null) {
                result_map.put("success", true);
                result_map.put("message", "");
                result_map.put("list",list);
            } else {
                result_map.put("list",null);
                result_map.put("success", false);
                result_map.put("message", "查询失败");
            }
        }catch (Exception e){
            result_map.put("success", false);
            result_map.put("message", "系统出现错误");
            result_map.put("list",null);
        }
        return result_map;
    }

    @ResponseBody
    @PostMapping("/findAccountInfoRecord")
    public Map<String,Object> findAccountInfoRecord(@RequestBody Map<String,Object> map){
        Map<String,Object> result_map = new HashMap<String,Object>();
        try {
        List<Map<String,Object>> list = userService.findAccountInfoRecord(map);
        if (list.size() > 0 && list != null) {
            result_map.put("success", true);
            result_map.put("message", "");
            result_map.put("list",list);
        } else {
            result_map.put("list",null);
            result_map.put("success", false);
            result_map.put("message", "查询失败");
        }
        }catch (Exception e){
            result_map.put("success", false);
            result_map.put("message", "系统出现错误");
            result_map.put("list",null);
        }
        return result_map;
    }

    @ResponseBody
    @PostMapping("/addSchoolInfo")
    public Map<String,Object> addSchoolInfo(@RequestBody Map<String,Object> map){
        Map<String,Object> result_map = new HashMap<String,Object>();
        try {
            int count = userService.addSchoolInfo(map);
            if (count > 0) {
                result_map.put("success", true);
                result_map.put("message", "");
            } else {
                result_map.put("success", false);
                result_map.put("message", "添加失败");
            }
        }catch (Exception e){
            result_map.put("success", false);
            result_map.put("message", "系统出现错误");
        }
        return result_map;
    }


    @ResponseBody
    @PostMapping("/addPayRecordInfo")
    public Map<String,Object> addPayRecordInfo(@RequestBody Map<String,Object> map){
        Map<String,Object> result_map = new HashMap<String,Object>();
        try {

            String orderId = userService.addPayRecordInfo(map);
            if (StringUtils.isNotEmpty(orderId)) {
                result_map.put("success", true);
                result_map.put("orderId",orderId);
                result_map.put("message", "");
            } else {
                result_map.put("success", false);
                result_map.put("message", "添加失败");
                result_map.put("orderId",null);
            }
        }catch (Exception e){
            result_map.put("success", false);
            result_map.put("message", "系统出现错误");
            result_map.put("orderId",null);
        }
        return result_map;
    }

    @ResponseBody
    @PostMapping("/findCityInfo")
    public Map<String,Object> findCityInfo(@RequestBody Map<String,Object> map){
        Map<String,Object> result_map = new HashMap<String,Object>();
        try {
            List<ProvinceCityArea> cityAreas= userService.findProvinceByNameInfo(map);
            if (cityAreas!=null && cityAreas.size()>0) {
                result_map.put("success", true);
                result_map.put("cityAreas",cityAreas);
                result_map.put("message", "");
            } else {
                result_map.put("success", false);
                result_map.put("message", "添加失败");
                result_map.put("cityAreas",null);
            }
        }catch (Exception e){
            result_map.put("success", false);
            result_map.put("message", "系统出现错误");
            result_map.put("cityAreas",null);
            System.out.println(e.getMessage());
        }
        return result_map;
    }

    //查询手机是否是国外的苹果手机来访问的
    @ResponseBody
    @RequestMapping("/findIPhoneByStatus")
    public Map<String,Object> findIPhoneByStatus(){
        Map<String,Object> result_map = new HashMap<String,Object>();
        try {
            logger.info("=======findIPhoneByStatus=====");
            result_map.put("jd","121.472999999999999");
            result_map.put("wd","31.2317");
            result_map.put("cityName","");
            //result_map.put("cityName","上海市");
        }catch (Exception e){
            logger.error("===findIPhoneByStatus==="+e.getMessage());
            System.out.println(e.getMessage());
        }
        return result_map;
    }
    @ResponseBody
    @GetMapping("/addPhoneUserInfo")
    public Map<String,Object> addPhoneUserInfo(String phone){
        Map<String,Object> map = new HashMap<>();
        try{
            logger.info("=====findUserByPhone====");
            map.put("success",true);
            map.put("msg","");
            List<User> userList = userService.getByUserExistPhone(phone);
            if(userList != null && userList.size()>0){
                User userVo = userList.get(0);
                map.put("userVo",userVo);
            }else{
                map.put("userVo","");
            }
        }catch (Exception e){
            map.put("msg","系统出现异常，请稍后重试");
            map.put("success","false");
            map.put("userVo","");
            logger.error("findUserByPhone=="+e.getMessage());
        }

        return map;
    }

    @CrossOrigin
    @ResponseBody
    @GetMapping("/findByPhone")
    public Map<String,Object> findByPhone(String phone){
        Map<String,Object> map = new HashMap<>();
        try{
            logger.info("=====findByPhone====");
            map.put("success",true);
            map.put("msg","");
            List<User> userList = userService.getByUserPhone(phone);
            if(userList != null && userList.size()>0){
                User userVo = userList.get(0);
                map.put("userVo",userVo);
            }else{
                map.put("userVo","");
                map.put("success",false);
            }
        }catch (Exception e){
            map.put("msg","系统出现异常，请稍后重试");
            map.put("success","false");
            map.put("userVo","");
            logger.error("findByPhone=="+e.getMessage());
        }

        return map;
    }




    //添加优惠券
    @CrossOrigin
    @ResponseBody
    @PostMapping("/addCouponByUserId")
    public Map<String,Object> addCouponByUserId(String userId,String parentId){
        Map<String,Object> result_map = new HashMap<String,Object>();
        try {
            logger.info("=====addCouponByUserId start=====");
            int  num = userService.addCouponUserId(userId,parentId);
            if (num >0) {
                result_map.put("success", true);
                result_map.put("message", "");
            } else {
                result_map.put("success", false);
                result_map.put("message", "添加失败");
            }
            logger.info("=====addCouponByUserId end=====");
        }catch (Exception e){
            logger.info("=====addCouponByUserId 异常====="+e.getMessage());
            result_map.put("success", false);
            result_map.put("message", "系统出现错误");
        }
        return result_map;
    }


    @CrossOrigin
    @ResponseBody
    @GetMapping("/addUserByInfo")
    public Map<String,Object> addUserByInfo(String phone){
        Map<String,Object> map = new HashMap<>();
        try{
            logger.info("=====addUserByInfo====");
            map.put("success",true);
            map.put("msg","");
            String userId = userService.addUserPhone(phone);
            if(StringUtils.isNotEmpty(userId)){
                map.put("userId",userId);
            }else{
                map.put("userId","");
                map.put("success",false);
                map.put("msg","查询失败");
                logger.info("查询失败");
            }
        }catch (Exception e){
            map.put("msg","系统出现异常，请稍后重试");
            map.put("success","false");
            map.put("userVo","");
            logger.error("addUserByInfo=="+e.getMessage());
        }

        return map;
    }


    @ResponseBody
    @GetMapping("/findUserByInfo")
    public Map<String,Object> findUserByInfo(String userId){
        Map<String,Object> map = new HashMap<>();
        try{
            logger.info("=====findUserByInfo====");
            map.put("success",true);
            map.put("msg","");
            List<Map<String,Object>> mapList = userService.findCouponByUserId(userId);
            if(mapList.size()>0 && mapList != null){
                map.put("couponList",mapList);
            }else{
                map.put("couponList",null);
                map.put("success",false);
                map.put("msg","没有优惠券");
                logger.info("没有优惠券");
            }
        }catch (Exception e){
            map.put("msg","系统出现异常，请稍后重试");
            map.put("success","false");
            map.put("couponList",null);
            logger.error("findUserByInfo=="+e.getMessage());
        }
        return map;
    }

    @ResponseBody
    @GetMapping("/updateTbCoupon")
    public Map<String,Object> updateTbCoupon(String id,String orderId){
        Map<String,Object> map = new HashMap<>();
        try{
            logger.info("=====updateTbCoupon====");
            map.put("success",true);
            map.put("msg","");
            int num = userService.updateTbCoupon(id,orderId);
            if(num <=0 ){
                logger.info("=====修改失败====");
                map.put("success",false);
                map.put("msg","修改失败");
            }
        }catch (Exception e){
            map.put("msg","系统出现异常，请稍后重试");
            map.put("success","false");
            logger.error("updateTbCoupon=="+e.getMessage());
        }
        return map;
    }

    @ResponseBody
    @GetMapping("/findContentUser")
    public Map<String,Object> findContentUser(){
        Map<String,Object> map = new HashMap<>();
        try{
            logger.info("=====findContentUser====");
            map.put("success",true);
            map.put("msg","");
            int content = userService.getByUserInfoByIds();
            if(content>0){
                logger.info("=====修改失败====");
                map.put("success",false);
                map.put("msg","修改失败");
                map.put("content","");
            }else{
                map.put("content",content);
            }
        }catch (Exception e){
            map.put("content","");
            map.put("msg","系统出现异常，请稍后重试");
            map.put("success","false");
            logger.error("findContent=="+e.getMessage());
        }
        return map;
    }


    @ResponseBody
    @GetMapping("/findContent")
    public Map<String,Object> findContent(){
        Map<String,Object> map = new HashMap<>();
        try{
            logger.info("=====findContent====");
            map.put("success",true);
            map.put("msg","");
            String content = userService.findContent();
            if(StringUtils.isEmpty(content) ){
                logger.info("=====修改失败====");
                map.put("success",false);
                map.put("msg","修改失败");
                map.put("content","");
            }else{
                map.put("content",content);
            }
        }catch (Exception e){
            map.put("content","");
            map.put("msg","系统出现异常，请稍后重试");
            map.put("success","false");
            logger.error("findContent=="+e.getMessage());
        }
        return map;
    }

    @ResponseBody
    @GetMapping("/findVersion")
    public Map<String,Object> findVersion(String type){
        Map<String,Object> map = new HashMap<>();
        try{
            logger.info("=====findVersion====");
            map.put("success",true);
            map.put("msg","");
            List<Map<String,Object>> mapList = userService.findVersionList(type);
            if(null == mapList || mapList.size() <=0 ){
                logger.info("=====暂无版本信息====");
                map.put("success",false);
                map.put("msg","暂无版本信息");
            }
            else{
                map.put("versionInfo",mapList.get(0));
            }
        }catch (Exception e){
            map.put("msg","系统出现异常，请稍后重试");
            map.put("success","false");
            logger.error("findVersion=="+e.getMessage());
        }
        return map;
    }


}
