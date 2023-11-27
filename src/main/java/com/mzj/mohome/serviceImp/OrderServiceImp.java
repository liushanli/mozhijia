package com.mzj.mohome.serviceImp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mzj.mohome.entity.Order;
import com.mzj.mohome.entity.PayRecord;

import com.mzj.mohome.mapper.OrderMapper;
import com.mzj.mohome.mapper.ProductMapper;
import com.mzj.mohome.mapper.UserMapper;
import com.mzj.mohome.mapper.WorkersMapper;
import com.mzj.mohome.service.OrderService;
import com.mzj.mohome.service.ProductService;
import com.mzj.mohome.service.WorkerService;
import com.mzj.mohome.util.SmsSendUtil;
import com.mzj.mohome.util.ToolsUtil;
import com.mzj.mohome.vo.OrderDto;
import com.mzj.mohome.vo.OrderVo;
import com.winnerlook.model.VoiceResponseResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("orderService")
public class OrderServiceImp implements OrderService {

    private final static Logger logger = LoggerFactory.getLogger(OrderServiceImp.class);
    @Autowired
    private WorkerService workService;
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WorkersMapper workersMapper;

    @Autowired
    private ProductMapper productMapper;


    @Autowired
    private ProductService productService;



    @Override
    public List<OrderVo> findOrerList(Map<String,Object> map) {
        String userId = ToolsUtil.getString(map.get("userId"));
        String status = ToolsUtil.getString(map.get("status"));
        String orderId = ToolsUtil.getString(map.get("orderId"));
        String workerId = ToolsUtil.getString(map.get("workerId"));
        String shopId = ToolsUtil.getString(map.get("shopId"));
        String pageStr = ToolsUtil.getString(map.get("page"));
        Integer page = StringUtils.isNotEmpty(pageStr)?Integer.parseInt(pageStr):1;
        List<OrderVo> orderList = null;
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(orderId);
        orderDto.setStatusDesc(status);
        orderDto.setUserId(userId);
        orderDto.setWorkerId(workerId);
        orderDto.setShopId(shopId);
        orderDto.setPage((page-1)*10);
        orderList = orderMapper.findOrerList_1(orderDto);
        if(orderList != null && orderList.size()>0){
            for(OrderVo orderVo : orderList){
                orderVo.setAddTimeStr("");
                /**
                 * 0：待付款，1：已付款，2：付款失败，3：已接单，
                 * 4：服务人员出发 5：服务人员到达 6：开始服务
                 * 7：用户确认完成  8：技师确认完成服务  9：用户评价
                 * 10：申请退款，11：退款中，12：退款成功，-1：取消订单
                 */
                orderVo.setWorkconfirmTimeStr("");
                orderVo.setShopReceiveTimeStr("");
                orderVo.setServiceCompleteTimeStr("");
                orderVo.setOrderPayTimeStr("");
                if(orderVo.getStatus()==0){
                    orderVo.setStatusDesc("订单待付款");
                }else if(orderVo.getStatus()==1){
                    orderVo.setStatusDesc("订单已付款");
                }else if(orderVo.getStatus()==2){
                    orderVo.setStatusDesc("付款失败");
                }else if(orderVo.getStatus()==3){
                    orderVo.setStatusDesc("订单已接单");
                }else if(orderVo.getStatus()==4){
                    orderVo.setStatusDesc("服务人员出发");
                }else if(orderVo.getStatus()==5){
                    orderVo.setStatusDesc("服务人员到达");
                }else if(orderVo.getStatus()==6){
                    orderVo.setStatusDesc("开始服务");
                }else if(orderVo.getStatus()==7){
                    orderVo.setStatusDesc("用户确认完成");
                } else if(orderVo.getStatus()==9){
                    orderVo.setStatusDesc("用户评价");
                }else if(orderVo.getStatus()==8){
                    orderVo.setStatusDesc("技师确认完成服务");
                } else if(orderVo.getStatus()==10){
                    orderVo.setStatusDesc("申请退款中");
                }else if(orderVo.getStatus()==11){
                    orderVo.setStatusDesc("退款中");
                }else if(orderVo.getStatus()==12){
                    orderVo.setStatusDesc("退款成功");
                } else if(orderVo.getStatus()==-1){
                    orderVo.setStatusDesc("订单待退款");
                }
            }
        }
        return orderList;
    }


    /**
     * 添加订单信息
     * @return
     */
    public Map<String,Object> addOrderInfo(Map<String,Object> map){
        Order order = new Order();
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int num = 0;
        String sourceType = "";
        Map<String,Object>  map_2 = new HashMap<>();
        Map<String,Object>  map_1 = new HashMap<>();
        try {
            map_1 = (Map<String, Object>) map.get("json");
            order.setWorkerId(String.valueOf(map.get("workerId")));
            order.setProductId(String.valueOf(map.get("productId")));
            System.out.println(map);

            Map<String,Object> worker = workersMapper.findWorkerById(order.getWorkerId());
            Map<String,Object> product = productMapper.findProductInfoByIdInfo(order.getProductId());
            order.setShopId(worker.get("shopId").toString());
            order.setProductName(product.get("productName").toString());
            order.setOldPrice(Float.parseFloat(product.get("oldPrice").toString()));
            order.setUserId(String.valueOf(map.get("userId")));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");

            order.setOrderId("A"+simpleDateFormat.format(new Date())+ToolsUtil.getFourRandom());
            order.setPrice(Float.parseFloat(product.get("price").toString()));
            order.setMemberPrice(Float.parseFloat(product.get("memberPrice").toString()));

            order.setPayOnline(Float.parseFloat(String.valueOf(map.get("payOnline"))));
            order.setStatus(Integer.parseInt(String.valueOf(map.get("status"))));
            order.setUserName(String.valueOf(map_1.get("userName")));

            order.setProvince(String.valueOf(map_1.get("provice")));
            order.setCity(String.valueOf(map_1.get("city")));
            order.setArea(String.valueOf(map_1.get("area")));
            order.setAddress(String.valueOf(map_1.get("address")));
            order.setPhone(String.valueOf(map_1.get("userPhone")));
            order.setJd(Float.parseFloat(String.valueOf(map_1.get("jd"))));
            order.setWd(Float.parseFloat(String.valueOf(map_1.get("wd"))));
            order.setDetail(String.valueOf(map_1.get("detail")!=null?map_1.get("detail"):""));
            order.setRemarks(map_1.get("remarks").toString());
            order.setTrafficPrice((map.get("trafficPrice")!=null && map.get("trafficPrice")!="")?Integer.parseInt(String.valueOf(map.get("trafficPrice"))):0);
            order.setServiceNumber(Integer.parseInt(String.valueOf(map.get("serviceNumber"))));
            SimpleDateFormat sdf_1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            order.setAboutTime((map.get("aboutTime")!=null && map.get("aboutTime")!="")?sdf_1.parse(String.valueOf(map.get("aboutTime"))):null);
            order.setOrderPayTime((map.get("orderPayTime")!=null && map.get("orderPayTime")!="") ?sdf.parse(String.valueOf(map.get("orderPayTime"))):null);
            order.setAddTime(new Date());
            order.setProductImgUrl(ToolsUtil.getString(map.get("productImg")));
            Float f = orderMapper.findSecorndPrice(order.getProductId());
            if(f!=null){
                order.setBargainPrice(f);
            }else{
                order.setBargainPrice(0f);
            }
            //来源类型
            if(map_1.get("sourceType")!=null){
                sourceType = String.valueOf(map_1.get("sourceType"));
            }
            order.setSourceType(sourceType);
            num = orderMapper.addOrderInfo(order);
            if(num>0){
                PayRecord payRecord = new PayRecord();
                payRecord.setOrderId(order.getOrderId());
                payRecord.setBody(String.valueOf(map.get("body")));
                payRecord.setSubject(String.valueOf(map.get("subject")));
                payRecord.setBuyType(1);
                payRecord.setOnlinePay(order.getPayOnline());
                payRecord.setUserId(order.getUserId());
                payRecord.setSourceType(sourceType);
                userMapper.addPayRecordInfoCard(payRecord);
                map_2.put("orderId",order.getOrderId());
            }
        }catch (Exception e){
            logger.error("添加订单异常：{}",e);
            return null;
        }
        return map_2;
    }

    public Map<String,Object> addOrderInfoWx(Map<String,Object> map){
        Order order = new Order();
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int num = 0;
        String sourceType = "";
        Map<String,Object>  map_2 = new HashMap<>();
        map_2.put("success",true);
        map_2.put("msg","");
        Map<String,Object>  map_1 = new HashMap<>();
        try {
            map_1 = (Map<String, Object>) map.get("json");
            order.setWorkerId(String.valueOf(map.get("workerId")));
            order.setProductId(String.valueOf(map.get("productId")));
            System.out.println(map);

            Map<String,Object> worker = workersMapper.findWorkerById(order.getWorkerId());
            Map<String,Object> product = productMapper.findProductInfoByIdInfo(order.getProductId());
            order.setShopId(worker.get("shopId").toString());
            order.setProductName(product.get("productName").toString());
            order.setOldPrice(Float.parseFloat(product.get("oldPrice").toString()));
            order.setUserId(String.valueOf(map.get("userId")));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");

            order.setOrderId("A"+simpleDateFormat.format(new Date())+ToolsUtil.getFourRandom());
            order.setPrice(Float.parseFloat(product.get("price").toString()));
            order.setMemberPrice(Float.parseFloat(product.get("memberPrice").toString()));

            order.setPayOnline(Float.parseFloat(String.valueOf(map.get("payOnline"))));
            order.setStatus(Integer.parseInt(String.valueOf(map.get("status"))));
            order.setUserName(String.valueOf(map_1.get("userName")));

            order.setProvince(String.valueOf(map_1.get("provice")));
            order.setCity(String.valueOf(map_1.get("city")));
            order.setArea(String.valueOf(map_1.get("area")));
            order.setAddress(String.valueOf(map_1.get("address")));
            order.setPhone(String.valueOf(map_1.get("userPhone")));
            order.setJd(Float.parseFloat(String.valueOf(map_1.get("jd"))));
            order.setWd(Float.parseFloat(String.valueOf(map_1.get("wd"))));
            order.setDetail(String.valueOf(map_1.get("detail")!=null?map_1.get("detail"):""));
            order.setRemarks(map_1.get("remarks").toString());
            order.setTrafficPrice((map.get("trafficPrice")!=null && map.get("trafficPrice")!="")?Integer.parseInt(String.valueOf(map.get("trafficPrice"))):0);
            order.setServiceNumber(Integer.parseInt(String.valueOf(map.get("serviceNumber"))));
            SimpleDateFormat sdf_1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            order.setAboutTime((map.get("aboutTime")!=null && map.get("aboutTime")!="")?sdf_1.parse(String.valueOf(map.get("aboutTime"))):null);
            order.setOrderPayTime((map.get("orderPayTime")!=null && map.get("orderPayTime")!="") ?sdf.parse(String.valueOf(map.get("orderPayTime"))):null);
            order.setProductImgUrl(ToolsUtil.getString(map.get("productImg")));
            order.setAddTime(new Date());
            Float f = orderMapper.findSecorndPrice(order.getProductId());
            if(f!=null){
                order.setBargainPrice(f);
            }else{
                order.setBargainPrice(0f);
            }
            //来源类型
            if(map_1.get("sourceType")!=null){
                sourceType = String.valueOf(map_1.get("sourceType"));
            }
            order.setSourceType(sourceType);
            num = orderMapper.addOrderInfo(order);
            if(num>0){
                PayRecord payRecord = new PayRecord();
                payRecord.setOrderId(order.getOrderId());
                payRecord.setBody(String.valueOf(map.get("body")));
                payRecord.setSubject(String.valueOf(map.get("subject")));
                payRecord.setBuyType(1);
                payRecord.setOnlinePay(order.getPayOnline());
                payRecord.setUserId(order.getUserId());
                payRecord.setSourceType(sourceType);
                userMapper.addPayRecordInfoCard(payRecord);
                map_2.put("orderId",order.getOrderId());
                map_2.put("subject",payRecord.getSubject());
                map_2.put("money",payRecord.getOnlinePay());
                map_2.put("image",order.getProductImgUrl());
                map_2.put("userMoney",userMapper.findUserMoney(order.getUserId()));
            }
        }catch (Exception e){
            logger.error("添加订单异常：{}",e);
            map_2.put("success",false);
            map_2.put("msg","添加订单异常");
            return null;
        }
        logger.info("返回信息为：{}",JSON.toJSONString(map_2));
        return map_2;
    }



    //根据状态查询
   public List<Map<String,Object>> findOrerListInfo(Map<String,Object> map){
        String userId = ToolsUtil.getString(map.get("userId"));
        String stats = ToolsUtil.getString(map.get("status"));
        Integer page = ToolsUtil.getString(map.get("page"))!=null?(Integer)map.get("page"):1;
        String orderId = ToolsUtil.getString(map.get("orderId"));
        String workerId = ToolsUtil.getString(map.get("workerId"));
       List<Map<String,Object>> orderVos = orderMapper.findOrerListInfo(page,stats,userId,orderId,workerId);
       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
       SimpleDateFormat sdf_1 = new SimpleDateFormat("HH:mm");
       List<Map<String,Object>> mapList = new ArrayList<>();
       String status = "";
       for(Map<String,Object> objectMap:orderVos){
           if(objectMap.get("shopReceiveTime")!=null && objectMap.get("shopReceiveTime")!=""){
               objectMap.put("shopReceiveTime",sdf.format(objectMap.get("shopReceiveTime")));
           }
           if(objectMap.get("serviceCompleteTime")!=null && objectMap.get("serviceCompleteTime")!=""){
               objectMap.put("serviceCompleteTime",sdf.format(objectMap.get("serviceCompleteTime")));
           }
           if(objectMap.get("workconfirmTime")!=null && objectMap.get("workconfirmTime")!=""){
               objectMap.put("workconfirmTime",sdf.format(objectMap.get("workconfirmTime")));
           }
           objectMap.put("addTime",sdf.format(objectMap.get("addTime")));
           objectMap.put("aboutTime",sdf.format(objectMap.get("aboutTime")));
           stats = String.valueOf(objectMap.get("status"));
           objectMap.put("newOrdefindWorkEvalrUrl","../jishi-detail/jishi-detail?workerId="+objectMap.get("workerId"));
           objectMap.put("evalHide","none");


           if(stats.equals("0")){
               objectMap.put("statusDesc","订单待付款");
           }else if(stats.equals("1")){
               objectMap.put("statusDesc","订单已付款");
           }else if(stats.equals("2")){
               objectMap.put("statusDesc","付款失败");
           } else if(stats.equals("3")){
               objectMap.put("statusDesc","订单已接单");
           } else if(stats.equals("4")){
               objectMap.put("statusDesc","服务人员出发");
               objectMap.put("evalHide","");
           }else if(stats.equals("5")){
               objectMap.put("statusDesc","服务人员到达");
           }else if(stats.equals("6")){
               objectMap.put("statusDesc","开始服务");
               objectMap.put("evalHide","");
           }else if(stats.equals("7")){
               objectMap.put("statusDesc","用户待评价");
           }else if(stats.equals("8")){
               objectMap.put("statusDesc","用户待评价");
           }else if(stats.equals("9")){
               objectMap.put("statusDesc","用户已评价");
           }else if(stats.equals("10")){
               objectMap.put("statusDesc","申请退款中");
           }else if(stats.equals("11")){
               objectMap.put("statusDesc","退款中");
           }else if(stats.equals("12")){
               objectMap.put("statusDesc","退款成功");
           }else if(stats.equals("-1")){
               objectMap.put("statusDesc","订单待退款");
           }
           int count = orderMapper.findCouponId(orderId);
           objectMap.put("coupon",count);
           mapList.add(objectMap);
       }
        return mapList;
    }



    //根据状态查询进行中
    public List<Map<String,Object>> findOrerListInfoByCon(Map<String,Object> map){
        String userId = ToolsUtil.getString(map.get("userId"));
        String stats = ToolsUtil.getString(map.get("status"));
        Integer page = ToolsUtil.getString(map.get("page"))!=null?(Integer)map.get("page"):1;
        String orderId = ToolsUtil.getString(map.get("orderId"));
        String workerId = ToolsUtil.getString(map.get("workerId"));
        List<Map<String,Object>> orderVos = orderMapper.findOrerListInfoByOn(page,stats,userId,orderId,workerId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat sdf_1 = new SimpleDateFormat("HH:mm");
        List<Map<String,Object>> mapList = new ArrayList<>();
        String status = "";
        for(Map<String,Object> objectMap:orderVos){
            objectMap.put("addTime",sdf.format(objectMap.get("addTime")));
            objectMap.put("aboutTime",sdf.format(objectMap.get("aboutTime")));
            stats = String.valueOf(objectMap.get("status"));
            objectMap.put("newOrdefindWorkEvalrUrl","../jishi-detail/jishi-detail?workerId="+objectMap.get("workerId"));
            objectMap.put("evalHide","none");


            if(stats.equals("0")){
                objectMap.put("statusDesc","订单待付款");
            }else if(stats.equals("1")){
                objectMap.put("statusDesc","订单已付款");
            }else if(stats.equals("2")){
                objectMap.put("statusDesc","付款失败");
            } else if(stats.equals("3")){
                objectMap.put("statusDesc","订单已接单");
            } else if(stats.equals("4")){
                objectMap.put("statusDesc","服务人员出发");
                objectMap.put("evalHide","");
            }else if(stats.equals("5")){
                objectMap.put("statusDesc","服务人员到达");
            }else if(stats.equals("6")){
                objectMap.put("statusDesc","开始服务");
                objectMap.put("evalHide","");
            }else if(stats.equals("7")){
                objectMap.put("statusDesc","确认完成");
            }else if(stats.equals("8")){
                objectMap.put("statusDesc","完成服务");
            }else if(stats.equals("9")){
                objectMap.put("statusDesc","用户待评价");
            }else if(stats.equals("10")){
                objectMap.put("statusDesc","申请退款中");
            }else if(stats.equals("11")){
                objectMap.put("statusDesc","退款中");
            }else if(stats.equals("12")){
                objectMap.put("statusDesc","退款成功");
            }else if(stats.equals("-1")){
                objectMap.put("statusDesc","订单待退款");
            }
            mapList.add(objectMap);
        }
        return mapList;
    }






    public int updateOrderInfo(Map<String,Object> map){
        try{
            Order order = new Order();
            order.setOrderId(ToolsUtil.getString(map.get("orderId")));
            order.setShopReceiveTime(new Date());
            String workerIdDes = ToolsUtil.getString(map.get("workerId"));
            Map<String,Object> objectMap = orderMapper.findWorkerInfo(order.getOrderId());
            String status = ToolsUtil.getString(map.get("status"));
            int numer = 0;
            if(status!=null){
                List<Map<String,Object>> mapList = orderMapper.findWorkerOrderList(order.getOrderId(),status);
                String workerId = ToolsUtil.getString(objectMap.get("workerId"));
                List<Map<String,Object>> mapList_1 = orderMapper.findWorkerOrderListById(order.getOrderId());
                Map<String,Object> map1 = mapList_1.get(0);

                String date = ToolsUtil.getString(map1.get("aboutTime"));
                logger.info("该订单"+order.getOrderId()+"，状态修改为"+status);
                if(mapList == null || mapList.size()<=0 ){
                    order.setStatus(Integer.parseInt(status));
                    if(Integer.parseInt(status)>=3 && Integer.parseInt(status)<=5){
                        if(Integer.parseInt(status)==3){
                            if(StringUtils.isEmpty(workerIdDes)){
                                order.setWorkerName(ToolsUtil.getString(objectMap.get("userName")));
                                order.setWorkerPhone(ToolsUtil.getString(objectMap.get("phone")));
                                order.setOrderReviceId(ToolsUtil.getString(objectMap.get("shopId")));
                                order.setOrderReviceName(ToolsUtil.getString(objectMap.get("shopName")));
                            }else{
                                order.setWorkerName(ToolsUtil.getString(objectMap.get("userName")));
                                order.setWorkerPhone(ToolsUtil.getString(objectMap.get("phone")));
                                order.setOrderReviceId(ToolsUtil.getString(objectMap.get("workerId")));
                                order.setOrderReviceName(ToolsUtil.getString(objectMap.get("userName")));
                            }
                            workersMapper.updateWorkTimeById(date,workerId,"1");
                        }
                         numer = orderMapper.updateOrderInfo(order);
                    }
                    else if(status.equals("6")|| status.equals("8") || status.equals("-1") || status.equals("7")){
                        numer = orderMapper.updateOrderInfoStatus(order);
                        if(status.equals("8") || status.equals("7")){
                            orderMapper.updateOrdersTimes(workerId,order.getOrderId());
                        }
                    }
                    else if(status.equals("9")){
                        numer = orderMapper.updateOrderStatusTime(order);
                    }
                }else{
                    logger.info("该订单"+order.getOrderId()+"，已被技师或者商家修改状态");
                }
            }


            logger.info("修改成功");
            return numer;
        }catch (Exception e){
            logger.info("修改失败："+e.getMessage());
            return 0;
        }
    }

    /**
     * 0：待付款，1：已付款，2：付款失败，3：已接单，
     * 4：服务人员出发 5：服务人员到达 6：开始服务
     * 7：用户确认完成  8：技师确认完成服务  9：用户评价
     * 10：申请退款，11：退款失败，12：退款成功，-1：取消订单
     * @param map
     * @return
     */
   public Map<String,Object> findOrderStatusMsg(Map<String,Object> map){
       List<Map<String,Object>> mapList_1 = orderMapper.findWorkerOrderListById(map.get("orderId").toString());
       Map<String,Object> map1 = mapList_1.get(0);
       logger.info("查询订单信息：{}", JSON.toJSONString(mapList_1));
       String statusDesc = "";
       String status = "";
       switch (map1.get("status").toString()){
           case "0":
               statusDesc = "该订单待付款";
               break;
           case "1":
               statusDesc = "该订单已付款，是否进行下一步";
               status = "3";
               break;
           case "2":
               statusDesc = "该订单付款失败";
               break;
           case "3":
               statusDesc = "该订单已接单，是否进行下一步";
               status = "4";
               break;
           case "4":
               statusDesc = "该订单服务人员出发，是否进行下一步";
               status = "5";
               break;
           case "5":
               statusDesc = "该订单服务人员到达，是否进行下一步";
               status = "6";
               break;
           case "6":
               statusDesc = "该订单已开始服务，是否进行下一步";
               status = "8";
               break;
           case "7":
               statusDesc = "该订单用户确认完成";
               break;
           case "8":
               statusDesc = "该订单技师确认完成";
               status = "8";
               break;
           case "9":
               statusDesc = "该订单用户已评价";
               break;
           case "10":
               statusDesc = "该订单已申请退款";
               break;
           case "11":
               statusDesc = "该订单已退款失败";
               break;
           case "12":
               statusDesc = "该订单已退款成功";
               break;
           case "-1":
               statusDesc = "该订单已取消订单";
               break;
           default:
                   break;
       }
       Map<String,Object> map2 = new HashMap<>();
       map2.put("status",status);
       map2.put("statusDesc",statusDesc);
       return map2;
   }

    //修改订单退款
    public int updateReturnOrder(Map<String,Object> map){
        try{
            Order order = new Order();
            order.setOrderId(ToolsUtil.getString(map.get("orderId")));
            order.setReturnReason(ToolsUtil.getString(map.get("returnReason")));
            order.setReturnMoney(ToolsUtil.getString(map.get("returnMoney"))!=null?Double.parseDouble(ToolsUtil.getString(map.get("returnMoney"))):0);
            logger.info("该订单："+order.getOrderId()+"修改为10");
            int numer = orderMapper.updateReturnOrder(order);
            System.out.println("修改成功");
            return numer;
        }catch (Exception e){
            System.out.println("修改失败："+e.getMessage());
            return 0;
        }
    }

    public Map<String,Object> orderStatusList(Map<String,Object> map){
        String orderId = ToolsUtil.getString(map.get("orderId"));
        List<Map<String,Object>> list = orderMapper.orderStatusList(orderId);
        Map<String,Object> resultMap = new HashMap<>();
        if(list!=null && list.size()>0){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Map<String,Object> map_1 = list.get(0);

                map_1.put("addTime",sdf.format((Date) map_1.get("addTime")));
                resultMap.put("orderId",map_1.get("orderId"));
                resultMap.put("time_1",map_1.get("orderPayTime")!=null?sdf.format((Date)map_1.get("orderPayTime")):"");
                resultMap.put("time_2","");
                resultMap.put("time_3","");
                resultMap.put("time_4","");
                resultMap.put("time_5","");
                resultMap.put("time_6",map_1.get("serviceCompleteTime")!=null?sdf.format((Date)map_1.get("serviceCompleteTime")):"");
                resultMap.put("time_7",map_1.get("workconfirmTime")!=null?sdf.format((Date)map_1.get("workconfirmTime")):"");
                resultMap.put("time_8",map_1.get("workconfirmTime")!=null?sdf.format((Date)map_1.get("workconfirmTime")):"");
                resultMap.put("time_9","");
        }

        return resultMap;
    }

   public List<Map<String,Object>> findEvalChooseDetail(){
       List<Map<String,Object>> mapList = orderMapper.findEvalChooseDetail();
       if(mapList!=null){
           for(Map<String,Object> map : mapList){
               map.put("cur",false);
           }
       }
       return mapList;
   }


    public Map<String,Object> addPhoneUp(Map<String,Object> map){

        Random rand = new Random();

        String phoneA = map.get("phoneA").toString();
        String phoneB = map.get("phoneB").toString();

        //第一步进行查询
        List<Map<String,Object>> list = orderMapper.findTrum();
        int num = rand.nextInt(list.size());
        Map<String,Object> map1 = list.get(num);

        //第二步，根据小号和手机号A来进行查询，该手机号是否绑定
        List<Map<String,Object>> mapList = orderMapper.findPhoneAAndB(map1.get("trumpet").toString(),phoneA);
        Map<String,String> mapStr = new HashMap<>();
        //如果绑定则进行修改，然后再添加，否则直接添加绑定
        if(mapList.size()>0 && mapList != null){
            for(Map<String,Object> map2 : mapList){
                orderMapper.UpPhoneStatus(map2.get("id").toString());
                mapStr.put("phone",map1.get("trumpet").toString());
                mapStr.put("cellPhoneA",map2.get("phoneA").toString());
                mapStr.put("cellPhoneB",map2.get("phoneB").toString());
                VoiceResponseResult v = workService.httpsPrivacyUnbind(mapStr);
            }
            orderMapper.addPhoneInfo(map1.get("trumpet").toString(),phoneA,phoneB);
            mapStr.put("phone",map1.get("trumpet").toString());
            mapStr.put("cellPhoneA",phoneA);
            mapStr.put("cellPhoneB",phoneB);

            VoiceResponseResult v = workService.httpsPrivacyBindAxb(mapStr);
        }else{
            orderMapper.addPhoneInfo(map1.get("trumpet").toString(),phoneA,phoneB);
            mapStr.put("phone",map1.get("trumpet").toString());
            mapStr.put("cellPhoneA",phoneA);
            mapStr.put("cellPhoneB",phoneB);
            VoiceResponseResult v = workService.httpsPrivacyBindAxb(mapStr);
        }
        return map1;
    }

    public List<Map<String,Object>> findOrderList(Map<String,Object> map){

        String userId = map.get("userId").toString();
        List<Map<String,Object>> mapList = orderMapper.findOrderList(userId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(mapList.size()>0 && mapList != null){
            for(Map<String, Object> map1 : mapList){
                if(map1.get("payTime")!=null && map1.get("payTime")!=""){
                    String payTime = sdf.format(map1.get("payTime"));
                    map1.put("payTime",payTime);
                }
                if(map1.get("pay_status").toString().equals("1")){
                    if(map1.get("payType").toString().equals("1")){
                        map1.put("onlinePay","-"+map1.get("onlinePay").toString());
                    }else{
                        map1.put("onlinePay","+"+map1.get("onlinePay").toString());
                    }
                }else{
                    map1.put("onlinePay","+"+map1.get("onlinePay").toString());
                }

            }
        }
        return  mapList;
    }

    public int delOrderInfo(String orderId){
       return orderMapper.updDelOrderInfo(orderId);
    }

    public int updOrderInfo(PayRecord payRecord){
        if(payRecord.getOrderId().contains("A") && StringUtils.isNotEmpty(payRecord.getMessage()) && payRecord.getMessage().contains("成功")){
            Order order = new Order();
            order.setStatus(1);
            order.setOrderId(payRecord.getOrderId());
            order.setOrderPayType(payRecord.getOrderPayType());
            logger.info("该订单"+order.getOrderId()+"修改为1,已付款");
            orderMapper.updateOrderInfoById(order);
        }
        return userMapper.updPayRecordOrderInfo(payRecord);

    }

    /**
     * 每一分钟执行一次，给每个技师发送信息
     */
    /*@Scheduled(cron = "0 0/1 * * * ?")
    @Async*/
    public void jobCron() {
        Thread.currentThread().setName("cron表达式执行");
        List<Map<String,Object>> list =  orderMapper.findWorkerInfoNew();
        if(list!= null && list.size()>0){

            logger.info("开始修改这些订单状态");
            List<String> stringList = new ArrayList<String>();
            for(Map<String,Object> map : list){
                stringList.add(map.get("phone").toString());
                orderMapper.updateInfoNew(map.get("workerId").toString());
            }
            String listToStr = String.join(",", stringList);
            logger.info("发送给的号码为=="+listToStr);
            OrderServiceImp orderServiceImp = new OrderServiceImp();
            orderServiceImp.SmsSendCode(listToStr);
        }
    }







    public String SmsSendCode(String phone){
       /* List<Map<String,Object>> mapList = userMapper.findSmsCode(phone);
        String message = "";
        if(mapList!=null && mapList.size()<=5){*/
        SmsSendUtil smsSendUtil = new SmsSendUtil();
        String random = smsSendUtil.randomCode();
        //短信下发
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("account","YZM2505206");//API账号
        map.put("password","sV6pFg5QHX1bda");//API密码
        map.put("msg","【摩之家】您好，你有新订单，请及时查收");//短信内容
        map.put("phone",phone);//手机号findWorkListByShop
        map.put("report",true);//是否需要状态报告
        map.put("extend",123);//自定义扩展码
        JSONObject js = (JSONObject) JSONObject.toJSON(map);
        System.out.println("js===="+js);
        logger.info("js===="+js);
        String jsonStr = smsSendUtil.sendSmsByPost("http://smssh1.253.com/msg/send/json",js.toString());
        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(jsonStr);
           /* message = (String)jsonObject.get("errorMsg");
            if(StringUtils.isEmpty(message)){
                userMapper.addSmsSendInfo(phone,random);
            }
            logger.info("message==="+message);
            System.out.println("message==="+message);*/
        System.out.println(jsonObject);
      /*  }else{
            logger.info("已发送五次");
            message = "验证码每天只能发送五次，请您明天再试";
        }*/

        return null;
    }

    /**
     * 每1分钟查询一次，是否有订单十五分钟之类没有付款执行一次
     */
    /*@Scheduled(cron = "0 0/1 * * * ?")
    @Async*/
    public void jobCronCouponId() {
        logger.info("每1分钟执行一次，修改优惠券");
        List<String> userList =  userMapper.findCouponDate();
        if(userList!=null && userList.size()>0){
           for (String id : userList)
           {
               userMapper.updateTbCoupon("0",id,"");
           }
        }
    }


    /**
     * 每一分钟执行一次
     */
    /*@Scheduled(cron = "0 0/5 * * * ?")
    @Async*/
    public void jobCronStat() {
        logger.info("每五分钟执行一次，星级评价");
        updateStat();
    }



    private void updateStat(){
        logger.info("updateStat===修改技师星级");
        //进行修改每个技师的评价星级
        List<Map<String,Object>> mapList = workersMapper.findEvaluateInfo_1();
        DecimalFormat df = new DecimalFormat("0%");
        //查询技师的订单数
        if(mapList != null && mapList.size() > 0){
            for(Map<String,Object> map : mapList){
                float maxNum = Float.parseFloat(map.get("maxNum").toString());
                float minNum =Float.parseFloat(map.get("minNum").toString());
                int star = 5;
                String evaluateNumLv = "100%";
                if (maxNum > 0) {
                    if (minNum/maxNum <= 0.2) {
                        star = 1;
                    } else if (minNum/maxNum <= 0.4) {
                        star = 2;
                    } else if (minNum/maxNum <= 0.6) {
                        star = 3;
                    } else if (minNum/maxNum <= 0.8) {
                        star = 4;
                    } else if (minNum/maxNum <= 1) {
                        star = 5;
                    }
                    evaluateNumLv = minNum > 0 ? df.format(minNum / maxNum) : "100%";
                    map.put("evaluateNumLv", evaluateNumLv);
                } else {
                    map.put("evaluateNumLv", evaluateNumLv);
                    map.put("star", star);
                }
                workersMapper.updateWorkerStar(map.get("workerId").toString(),star,evaluateNumLv);
            }
        }
    }
}

