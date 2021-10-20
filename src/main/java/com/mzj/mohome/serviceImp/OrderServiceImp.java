package com.mzj.mohome.serviceImp;

import com.mzj.mohome.entity.Order;
import com.mzj.mohome.entity.PayRecord;

import com.mzj.mohome.mapper.OrderMapper;
import com.mzj.mohome.mapper.ProductMapper;
import com.mzj.mohome.mapper.UserMapper;
import com.mzj.mohome.mapper.WorkersMapper;
import com.mzj.mohome.service.OrderService;
import com.mzj.mohome.service.ProductService;
import com.mzj.mohome.service.WorkerService;
import com.mzj.mohome.util.ToolsUtil;
import com.mzj.mohome.vo.OrderDto;
import com.mzj.mohome.vo.OrderVo;
import com.mzj.mohome.vo.PageUtil;
import com.winnerlook.model.VoiceResponseResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        Integer page = StringUtils.isNotEmpty(pageStr)?0:Integer.parseInt(pageStr);
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
        Map<String,Object>  map_2 = new HashMap<String,Object>();
        Map<String,Object>  map_1 = new HashMap<String,Object>();
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
            Float f = orderMapper.findSecorndPrice(order.getProductId());
            if(f!=null){
                order.setBargainPrice(f);
            }else{
                order.setBargainPrice(0f);
            }
            num = orderMapper.addOrderInfo(order);

            if(num>0){

                PayRecord payRecord = new PayRecord();
                payRecord.setOrderId(order.getOrderId());
                payRecord.setBody(String.valueOf(map.get("body")));
                payRecord.setSubject(String.valueOf(map.get("subject")));
                payRecord.setBuyType(1);
                payRecord.setOnlinePay(order.getPayOnline());
                payRecord.setUserId(order.getUserId());
                userMapper.addPayRecordInfoCard(payRecord);
                map_2.put("orderId",order.getOrderId());
            }
        }catch (Exception e){
            System.out.println("e==="+e.getMessage());
            return null;
        }
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
           objectMap.put("aboutTime",sdf_1.format(objectMap.get("aboutTime")));
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
            objectMap.put("aboutTime",sdf_1.format(objectMap.get("aboutTime")));
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
            order.setWorkerName(ToolsUtil.getString(map.get("workerName")));
            order.setOrderReviceId(ToolsUtil.getString(map.get("workerId")));
            order.setOrderReviceName(ToolsUtil.getString(map.get("workerName")));
            order.setWorkerPhone(ToolsUtil.getString(map.get("workerPhone")));
            String status = ToolsUtil.getString(map.get("status"));
            int numer = 0;
            if(status!=null){
                List<Map<String,Object>> mapList = orderMapper.findWorkerOrderList(order.getOrderId(),status);

                List<Map<String,Object>> mapList_1 = orderMapper.findWorkerOrderListById(order.getOrderId());
                Map<String,Object> map1 = mapList_1.get(0);
                String workerId = ToolsUtil.getString(map1.get("workerId"));
                String date = ToolsUtil.getString(map1.get("aboutTime"));
                logger.info("该订单"+order.getOrderId()+"，状态修改为"+status);
                if(mapList == null || mapList.size()<=0 ){
                    order.setStatus(Integer.parseInt(status));
                    if(Integer.parseInt(status)>=3 && Integer.parseInt(status)<=5){
                        if(Integer.parseInt(status)==3){
                            workersMapper.updateWorkTimeById(date,workerId,"1");
                        }
                         numer = orderMapper.updateOrderInfo(order);
                    }
                    else if(status.equals("6")|| status.equals("8") || status.equals("-1") || status.equals("7")){
                        numer = orderMapper.updateOrderInfoStatus(order);
                    }
                    else if(status.equals("9")){
                        numer = orderMapper.updateOrderStatusTime(order);
                    }
                }else{
                    logger.info("该订单"+order.getOrderId()+"，已被技师或者商家修改状态");
                }
            }


            System.out.println("修改成功");
            return numer;
        }catch (Exception e){
            System.out.println("修改失败："+e.getMessage());
            return 0;
        }
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
}

