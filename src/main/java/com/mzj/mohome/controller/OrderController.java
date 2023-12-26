package com.mzj.mohome.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mzj.mohome.entity.Order;
import com.mzj.mohome.entity.PayRecord;
import com.mzj.mohome.service.OrderService;
import com.mzj.mohome.service.UserService;
import com.mzj.mohome.util.RequestApi;
import com.mzj.mohome.util.ToolsUtil;
import com.mzj.mohome.util.WxPayConfig;
import com.mzj.mohome.vo.OrderVo;
import com.mzj.mohome.vo.ReturnOrderStatusVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单控制类
 */
@Slf4j
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private RequestApi requestApi;
    @Resource
    private WxPayConfig wxPayConfig;
    @ResponseBody
    @PostMapping("/findOrderInfo")
    public Map<String,Object> findOrderInfo(@RequestBody Map<String,Object> map){
        Map<String,Object> mapList = new HashMap<>();
        try{
            List<OrderVo> orderList = orderService.findOrerList(map);
            mapList.put("orderList",orderList);
        }catch (Exception e){
            System.out.println("错误信息=="+e.getMessage());
        }
        return mapList;
    }

    @ResponseBody
    @RequestMapping(value = "/addUserOrder",method = RequestMethod.POST)
    public Map<String,Object> addUserOrder(@RequestBody Map<String,Object> map){
        Map<String,Object> resultMap = new HashMap<>();
        try {
            resultMap.put("success",true);
            resultMap.put("msg","");
            Map<String,Object> stringObjectMap = orderService.addOrderInfo(map);
            if(stringObjectMap==null){
                resultMap.put("success",false);
                resultMap.put("msg","添加失败");
                resultMap.put("orderId","");
            }else{
                resultMap.put("orderId",stringObjectMap.get("orderId"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("success",false);
            resultMap.put("msg","添加失败");
            resultMap.put("orderId","");
        }
        return resultMap;
    }
    @ResponseBody
    @RequestMapping(value = "/addUserOrderWx",method = RequestMethod.POST)
    public Map<String,Object> addUserOrderWx(@RequestBody Map<String,Object> map){
        Map<String,Object> resultMap = new HashMap<>();
        try {
            log.info("请求信息为：{}",JSON.toJSONString(map));
            resultMap.put("success",true);
            resultMap.put("msg","");
            resultMap = orderService.addOrderInfoWx(map);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("success",false);
            resultMap.put("msg","添加失败");
            resultMap.put("orderId","");
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "/updaSerOrder",method = RequestMethod.POST)
    public Map<String,Object> updaSerOrder(@RequestBody Map<String,Object> map){
        Map<String,Object> resultMap = new HashMap<>();
        try {
            log.info("updaSerOrder====请求参数：{}",JSON.toJSONString(map));
            resultMap.put("success",true);
            resultMap.put("msg","");
            int result = orderService.updateOrderInfo(map);
            if(result<=0){
                resultMap.put("success",false);
                resultMap.put("msg","修改失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "/findOrderStatusMsg",method = RequestMethod.POST)
    public Map<String,Object> findOrderStatusMsg(@RequestBody Map<String,Object> map){
        Map<String,Object> resultMap = new HashMap<>();
        try {
            resultMap.put("success",true);
            resultMap.put("msg","");
            Map<String,Object> result = orderService.findOrderStatusMsg(map);
            resultMap.put("result",result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }


    @ResponseBody
    @PostMapping("/findOrderInfoByStatus")
    public Map<String,Object> findOrderInfoByStatus(@RequestBody Map<String,Object> map){
        Order order = new Order();

        Map<String,Object> mapList = new HashMap<>();
        try{
            List<Map<String,Object>>  orderList = orderService.findOrerListInfo(map);
            mapList.put("orderList",orderList);
        }catch (Exception e){
            System.out.println("错误信息=="+e.getMessage());
        }
        return mapList;
    }

    @ResponseBody
    @PostMapping("/findOrderInfoByStatusByCon")
    public Map<String,Object> findOrderInfoByStatusByCon(@RequestBody Map<String,Object> map){
        Order order = new Order();

        Map<String,Object> mapList = new HashMap<String,Object>();
        try{
            List<Map<String,Object>>  orderList = orderService.findOrerListInfoByCon(map);
            mapList.put("orderList",orderList);
        }catch (Exception e){
            System.out.println("错误信息=="+e.getMessage());
        }
        return mapList;
    }


    @ResponseBody
    @RequestMapping(value = "/updReturnOrder",method = RequestMethod.POST)
    public Map<String,Object> updReturnOrder(@RequestBody Map<String,Object> map){
        Map<String,Object> resultMap = new HashMap<>();
        try {
            log.info("请求参数为：{}",JSON.toJSONString(map));

            //添加记录
            String orderId = ToolsUtil.getString(map.get("orderId"));
            OrderVo order = orderService.findOrderDetail(orderId);
            userService.addReturnOrderHistory(orderId,order.getStatus());

            resultMap.put("success",true);
            resultMap.put("msg","");
            map.put("status",10);
            map.put("returnType","0");
            int result = orderService.updateReturnOrder(map);
            if(result<=0){
                resultMap.put("success",false);
                resultMap.put("msg","修改失败");
            }
        } catch (Exception e) {
            log.error("updReturnOrder===修改状态失败，错误信息为：{}",e);
            e.printStackTrace();
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "/updReturnOrderBeforeStatus",method = RequestMethod.POST)
    public Map<String,Object> updReturnOrderBeforeStatus(@RequestBody Map<String,Object> map){
        Map<String,Object> resultMap = new HashMap<>();
        try {
            log.info("请求参数为：{}",JSON.toJSONString(map));
            resultMap.put("success",true);
            resultMap.put("msg","");
            String orderId = ToolsUtil.getString(map.get("orderId"));
            List<ReturnOrderStatusVo> returnOrderStatusVos = userService.queryReturnInfoList(orderId);
            map.put("status",returnOrderStatusVos.get(0).getStatus());

            int result = orderService.updateReturnOrder(map);
            if(result<=0){
                resultMap.put("success",false);
                resultMap.put("msg","修改失败");
            }
        } catch (Exception e) {
            log.error("updReturnOrderBeforeStatus===修改状态失败，错误信息为：{}",e);
            e.printStackTrace();
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "/orderStatusInfo",method = RequestMethod.POST)
    public Map<String,Object> orderStatusInfo(@RequestBody Map<String,Object> map){
        Map<String,Object> resultMap = new HashMap<>();
        try {
            resultMap.put("success",true);
            resultMap.put("msg","");
            Map<String,Object> resultList = orderService.orderStatusList(map);
            resultMap.put("orderStatusList",resultList);
            if(resultList.size()<=0){
                resultMap.put("success",false);
                resultMap.put("msg","查询失败");
                resultMap.put("orderStatusList",null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("orderStatusList",null);
            resultMap.put("success",false);
            resultMap.put("msg","查询失败");
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "/findEvalDetail",method = RequestMethod.POST)
    public Map<String,Object> findEvalDetail(){
        Map<String,Object> resultMap = new HashMap<>();
        try {
            resultMap.put("success",true);
            resultMap.put("msg","");
            List<Map<String,Object>> resultList = orderService.findEvalChooseDetail();
            resultMap.put("resultList",resultList);
            if(resultList.size()<=0){
                resultMap.put("success",false);
                resultMap.put("msg","查询失败");
                resultMap.put("orderStatusList",null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("orderStatusList",null);
            resultMap.put("success",false);
            resultMap.put("msg","查询失败");
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "/findAddPhoneUp",method = RequestMethod.POST)
    public Map<String,Object> findAddPhoneUp(@RequestBody Map<String,Object> map){
        Map<String,Object> resultMap = new HashMap<>();
        log.info("拨打小号的信息：{}", JSON.toJSONString(map));
        try {
            resultMap.put("success",true);
            resultMap.put("msg","");
            Map<String,Object> result_1 = orderService.addPhoneUp(map);
            resultMap.put("phone",result_1.get("trumpet"));
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("success",false);
            resultMap.put("msg","查询失败");
            resultMap.put("phone",null);
        }
        return resultMap;
    }


    @ResponseBody
    @RequestMapping(value = "/findPayOrder",method = RequestMethod.POST)
    public Map<String,Object> findPayOrder(@RequestBody Map<String,Object> map){
        Map<String,Object> resultMap = new HashMap<>();
        try {
            resultMap.put("success",true);
            resultMap.put("msg","");
            List<Map<String,Object>>  result_1 = orderService.findOrderList(map);
            resultMap.put("payRecordList",result_1);
        } catch (Exception e) {
            resultMap.put("payRecordList",null);
            e.printStackTrace();
            resultMap.put("success",false);
            resultMap.put("msg","查询失败");
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "/delOrderInfo",method = RequestMethod.POST)
    public Map<String,Object> delOrderInfo(@RequestBody Map<String,Object> map){
        Map<String,Object> resultMap = new HashMap<>();
        String orderId = map.get("orderId").toString();
        try {

            resultMap.put("success",true);
            resultMap.put("msg","");
            int count = orderService.delOrderInfo(orderId);
            if(count>0){
                resultMap.put("success",false);
                resultMap.put("msg","删除失败");
            }
        } catch (Exception e) {
            resultMap.put("success",false);
            resultMap.put("msg","删除失败");
            log.error("该订单id：{}删除订单失败：{}",orderId,e);
        }
        return resultMap;
    }
    @ResponseBody
    @RequestMapping(value = "/updOrderPayInfo",method = RequestMethod.POST)
    public Map<String,Object> updOrderPayInfo(@RequestBody PayRecord payRecord){
        Map<String,Object> resultMap = new HashMap<>();
        try {
            resultMap.put("success",true);
            resultMap.put("msg","");
            if(StringUtils.isNotEmpty(payRecord.getTradeNo())){
                Map<String,String> map_1 = new HashMap<>();
                map_1.put("mchid",wxPayConfig.getMchId());
                String url = "https://api.mch.weixin.qq.com/v3/pay/transactions/out-trade-no/"+payRecord.getOrderId();
                log.info("updOrderPayInfo==支付成功，根据商户订单号查询相关信息请求链接：{},商户号为：{}",url,wxPayConfig.getMchId());
                JSONObject jsonObject = requestApi.getApi(url,map_1);
                log.info("updOrderPayInfo==支付成功，根据商户订单号查询相关信息：{}",jsonObject.toJSONString());
                payRecord.setTradeNo(ToolsUtil.getString(jsonObject.get("transaction_id")));
            }
            int count = orderService.updOrderInfo(payRecord);
            if(count>0){
                resultMap.put("success",true);
                resultMap.put("msg","修改订单记录成功");
            }
        } catch (Exception e) {
            resultMap.put("success",false);
            resultMap.put("msg","修改订单记录失败");
            log.error("该订单id：{}修改订单记录失败：{}",payRecord.getOrderId(),e);
        }
        return resultMap;
    }

    public static void main(String[] args) {
        String str = "{\n" +
                "\t\"amount\": {\n" +
                "\t\t\"currency\": \"CNY\",\n" +
                "\t\t\"payer_currency\": \"CNY\",\n" +
                "\t\t\"payer_total\": 1,\n" +
                "\t\t\"total\": 1\n" +
                "\t},\n" +
                "\t\"appid\": \"wxdace645e0bc2cXXX\",\n" +
                "\t\"attach\": \"\",\n" +
                "\t\"bank_type\": \"OTHERS\",\n" +
                "\t\"mchid\": \"1900006XXX\",\n" +
                "\t\"out_trade_no\": \"44_2126281063_5504\",\n" +
                "\t\"payer\": {\n" +
                "\t\t\"openid\": \"o4GgauJP_mgWEWictzA15WT15XXX\"\n" +
                "\t},\n" +
                "\t\"promotion_detail\": [],\n" +
                "\t\"success_time\": \"2021-03-22T10:29:05+08:00\",\n" +
                "\t\"trade_state\": \"SUCCESS\",\n" +
                "\t\"trade_state_desc\": \"支付成功\",\n" +
                "\t\"trade_type\": \"JSAPI\",\n" +
                "\t\"transaction_id\": \"4200000891202103228088184743\"\n" +
                "}";
        JSONObject jsonObject = JSONObject.parseObject(str);
        System.out.println(jsonObject.get("transaction_id"));

        System.out.println(jsonObject.toJSONString());
    }


    @ResponseBody
    @RequestMapping(value = "/findOrderDetailInfo",method = RequestMethod.POST)
    public Map<String,Object> findOrderDetailInfo(String orderId){
        Map<String,Object> resultMap = new HashMap<>();
        try {
            OrderVo orderVo = orderService.findOrderDetail(orderId);
            resultMap.put("success",true);
            resultMap.put("msg","");
            resultMap.put("result",orderVo);
        } catch (Exception e) {
            resultMap.put("success",false);
            resultMap.put("msg","查询订单失败");
            log.error("该订单id：{}失败，错误信息为：{}",orderId,e);
        }
        return resultMap;
    }
}
