package com.mzj.mohome.serviceImp;

import com.alibaba.fastjson.JSON;
import com.mzj.mohome.entity.Shop;
import com.mzj.mohome.entity.Worker;
import com.mzj.mohome.mapper.ShopMapper;
import com.mzj.mohome.mapper.WorkersMapper;
import com.mzj.mohome.service.ShopService;
import com.mzj.mohome.service.WorkerService;
import com.mzj.mohome.util.ToolsUtil;
import com.mzj.mohome.util.getLngAndLat;
import com.mzj.mohome.vo.WorkerVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.tools.Tool;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("shopService")
public class ShopServiceImp implements ShopService {
    private final static Logger logger = LoggerFactory.getLogger(ShopServiceImp.class);

    @Autowired
    private WorkersMapper workersMapper;

    @Autowired
    private ShopMapper shopMapper;
    @Autowired
    private WorkerService workerService;
    @Override
    public List<Shop> findShopList(Map<String,Object> map) {

        String city = map.get("city").toString();
        if(ToolsUtil.getCityFlag(city)){
            return shopMapper.findShopList(city);
        }else{
            return shopMapper.findShopList_1(city);
        }
    }

    @Override
    public List<Map<String,Object>> findShopTime(Map<String, Object> map) {
        String beginTime = ToolsUtil.getString(map.get("beginTime"));
        String endTime = ToolsUtil.getString(map.get("endTime"));
        String shopId =  ToolsUtil.getString(map.get("shopId"));
        String workerId = ToolsUtil.getString(map.get("workerId"));
        List<Map<String,Object>> map1 = shopMapper.findShopTime(beginTime,endTime,shopId,workerId);
        return map1;
    }
    //根据条件查询商家信息
    public List<Shop> findShopInfoByCon(Map<String,Object> map){
        String shopId = ToolsUtil.getString(map.get("shopId"));

        return shopMapper.findShopInfoByCon(shopId);
    }

         //根据手机号和验证码来查询商家是否存在
    public Map<String,Object> findShopBySend(Map<String,Object> map){
        String phone =  ToolsUtil.getString(map.get("phone"));
        String sendCode = ToolsUtil.getString(map.get("sendCode"));
        Map<String,Object> map1 =  shopMapper.findShopBySend(phone,sendCode);
        Map<String,Object> objectMap = new HashMap<>();
        if(map1 != null){
            Map<String,Object> map2 = shopMapper.findShopByPhone(phone);
            if(map2 != null){
                objectMap.put("msg","");
                objectMap.put("shopList",map2);
                objectMap.put("success",true);
            }else{
                objectMap.put("msg","该手机号没有注册店铺");
                objectMap.put("shopList",null);
                objectMap.put("success",false);
            }
        }else{
            objectMap.put("msg","该手机号/验证码错误");
            objectMap.put("shopList",null);
            objectMap.put("success",false);
        }
        return objectMap;
    }

    public List<Map<String,Object>> findShopTimeByCon(Map<String,Object> map){
        try{
            String shopId = String.valueOf(map.get("shopId"));
            Integer cur = (Integer)map.get("cur");
            String beginTime = "";
            String endTime = "";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d1 = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(d1);
            cal.add(Calendar.MINUTE, 30);
            Date start = cal.getTime();
            if(cur==0){
                beginTime = sdf.format(start);
                endTime = getEndTime(start,cur+1);
            }else{
                beginTime = getEndTime(start,cur);
                endTime = getEndTime(start,cur+1);
            }

            List<Map<String,Object>> map1 = shopMapper.findShopTimeCon(beginTime,endTime,shopId);
            map.put("beginTime",beginTime);
            map.put("endTime",endTime);
            return map1;
        }catch (Exception e){
            return null;
        }
    }


    public String getEndTime(Date start,Integer cur){
        SimpleDateFormat simpleDateF = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(start);
        c.add(Calendar.DAY_OF_MONTH, cur);// 今天+3天
        Date end = c.getTime();
        return simpleDateF.format(end);
    }

    public List<Map<String,Object>> findShopTimeConByWorkId(Map<String,Object> map){
        String workerId =  ToolsUtil.getString(map.get("workerId"));
        List<Map<String,Object>> map1 = shopMapper.findShopTimeConByWorkId(workerId);
        return map1;
    }

    public List<Map<String,Object>> findShopWorkInfoByCons(Map<String,Object> map){
        String shopId = ToolsUtil.getString(map.get("shopId"));
        String city = ToolsUtil.getString(map.get("city"));
        String onLine = ToolsUtil.getString(map.get("onLine"));
        String workerName = ToolsUtil.getString(map.get("workerName"));
        Integer page = ToolsUtil.getString(map.get("page"))!=null?(Integer)map.get("page"):1;
        page = (page-1)*10;
        List<Map<String,Object>> mapList_1 = shopMapper.findWorkerListByShopId(city,shopId,onLine,page,workerName);
        DecimalFormat df = new DecimalFormat("0%");
        if(mapList_1!=null){
            logger.info("==========start=======");
            Long oldtime=new Date().getTime();
           for(Map<String,Object> worker : mapList_1) {

               if(StringUtils.isNotBlank(ToolsUtil.getString(worker.get("evalStatus_one")))){
                   worker.put("evalStatus_1", ToolsUtil.getString(worker.get("evalStatus_one")));
               }
               if(StringUtils.isNotBlank(ToolsUtil.getString(worker.get("evalStatus_two")))){
                   worker.put("evalStatus_2",ToolsUtil.getString(worker.get("evalStatus_two")));
               }
               if(StringUtils.isNotBlank(ToolsUtil.getString(worker.get("evalStatus_three")))){
                   worker.put("evalStatus_3",ToolsUtil.getString(worker.get("evalStatus_three")));
               }

               worker.put("sellSum", worker.get("sellSum") != null ? worker.get("sellSum") : 0);
               if(StringUtils.isNotBlank(ToolsUtil.getString(worker.get("quality")))){
                   worker.put("star",ToolsUtil.getString(worker.get("quality")));
               }else{
                   worker.put("star", 5);
               }
               String address = "";
               if(StringUtils.isNotEmpty(ToolsUtil.getString(worker.get("province")))){
                   address += ToolsUtil.getString(worker.get("province"));
               }
               if(StringUtils.isNotEmpty(ToolsUtil.getString(worker.get("city")))){
                   address += ToolsUtil.getString(worker.get("city"));
               }
               if(StringUtils.isNotEmpty(ToolsUtil.getString(worker.get("area")))){
                   address += ToolsUtil.getString(worker.get("area"));
               }
               if(StringUtils.isEmpty(ToolsUtil.getString(worker.get("address")))){
                    worker.put("address",address);
                }
           }

            Long systime=new Date().getTime();
            Long time = (systime - oldtime);//相差毫秒数
            logger.info("==========end=======耗时：{}",time);
        }
        return  mapList_1;
    }

    public int updateJishiOnline(Map<String,Object> map){
        String online = map.get("online").toString();
        String workerId = map.get("workerId").toString();
        return shopMapper.updateJishiOnline(workerId,online);
    }

    //修改店铺的忙时时间
    public int updateShopBusy(Map<String,Object> map){
        String id = ToolsUtil.getString(map.get("id"));
        Integer isBusy = (Integer)map.get("isBusy");
        String dateStr = ToolsUtil.getString(map.get("dateStr"));
        String shopId = ToolsUtil.getString(map.get("shopId"));
        return shopMapper.updateShopBusy(isBusy,dateStr,id,shopId);
    }

    public int updateWorkerJd(Map<String,Object> map){
        String jd = ToolsUtil.getString(map.get("jd"));
        String wd = ToolsUtil.getString(map.get("wd"));
        String workerId = ToolsUtil.getString(map.get("workerId"));
        logger.info("jd=="+jd+"==wd=="+wd+"==workerId==="+workerId);
        Map<String,Object> addressMap = getLngAndLat.getAddressInfo(wd+","+jd);
        addressMap.put("status","1");
        addressMap.put("area",addressMap.get("district").toString());
        addressMap.put("jd",jd);
        addressMap.put("wd",wd);
        addressMap.put("workerId",workerId);
        logger.info("updateWorkerJd===请求信息为：{}", JSON.toJSONString(addressMap));
        int num = workerService.updWorkerInfoJW(addressMap);

        //int num = shopMapper.updWorkerPoint(jd,wd,workerId);
        return num;
    }
}
