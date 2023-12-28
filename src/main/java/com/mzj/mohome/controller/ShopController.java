package com.mzj.mohome.controller;

import com.alibaba.fastjson.JSON;
import com.mzj.mohome.entity.Shop;
import com.mzj.mohome.entity.WorkerPic;
import com.mzj.mohome.service.ShopService;
import com.mzj.mohome.service.WorkerService;
import com.mzj.mohome.util.ToolsUtil;
import com.mzj.mohome.vo.PageUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/shop")
public class ShopController {
    private final static Logger logger = LoggerFactory.getLogger(ShopController.class);
    @Autowired
    private ShopService shopService;
    @Autowired
    private WorkerService workerService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat simpleDateF = new SimpleDateFormat("yyyy-MM-dd");

    @ResponseBody
    @PostMapping("/findShopList")
    public Map<String,Object> findShopList(@RequestBody Map<String,Object> map_1){
        Map<String,Object> map = new HashMap<String,Object>();
        List<Shop> shopList = shopService.findShopList(map_1);
        map.put("shopList",shopList);
        map.put("success",true);
        return map;
    }

    @ResponseBody
    @PostMapping("/findShopInfo")
    public Map<String,Object> findShopInfo(@RequestBody Map<String,Object> map){
        Map<String,Object> objectMap = new HashMap<String,Object>();
        List<Shop> shopInfo = shopService.findShopInfoByCon(map);
        objectMap.put("shopList",shopInfo);
        objectMap.put("success",true);
        return objectMap;
    }

    @ResponseBody
    @PostMapping("/findShopBySendCode")
    public Map<String,Object> findShopBySendCode(@RequestBody Map<String,Object> map){
        Map<String,Object> objectMap = new HashMap<String,Object>();
        try{
           objectMap = shopService.findShopBySend(map);
        }catch (Exception e){
            objectMap.put("shopList",null);
            objectMap.put("success",false);
            objectMap.put("msg","网络错误");
            System.out.println(e.getMessage());
        }
        return objectMap;
    }

    //获取时间
    @ResponseBody
    @RequestMapping(value = "/findShopTimeById",method = RequestMethod.POST)
    public Map<String,Object> findShopTimeById(@RequestBody Map<String,Object> map_1){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success",true);
        map.put("msg","");
        try {
            Map<String,Object> map2 = findShopInfoMap(map_1);
            List<Map<String,Object>>  workerTimeList = shopService.findShopTimeByCon(map2);
            map.put("shopTimeList",workerTimeList);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success",false);
            map.put("msg","查询错误，请稍后重试");
        }
        return map;
    }

    private Map<String,Object> findShopInfoMap(Map<String,Object> objectMap){
        String beginTime = "";
        String endTime = "";
        Integer cur = Integer.parseInt(objectMap.get("cur").toString());
        Date d1 = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(d1);
        cal.add(Calendar.MINUTE, 30);
        Date start = cal.getTime();
        if(cur==0){
            beginTime = sdf.format(start);
            endTime = getEndTimeInfo(start,cur+1);
        }else{
            beginTime = getEndTimeInfo(start,cur);
            endTime = getEndTimeInfo(start,cur+1);
        }
        objectMap.put("beginTime",beginTime);
        objectMap.put("endTime",endTime);
        return objectMap;
    }

    private String getEndTimeInfo(Date start,Integer cur){
        Calendar c = Calendar.getInstance();
        c.setTime(start);
        c.add(Calendar.DAY_OF_MONTH, cur);// 今天+3天
        Date end = c.getTime();
        return simpleDateF.format(end);
    }

    //根据员工id来获取店铺信息
    @ResponseBody
    @RequestMapping(value = "/findShopInfoByWorker",method = RequestMethod.POST)
    public Map<String,Object> findShopInfoByWorker(@RequestBody Map<String,Object> map_1){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success",true);
        map.put("msg","");
        try {
            //Map<String,Object> map2 = findShopInfoMap(map_1);
            List<Map<String,Object>>  shopList = shopService.findShopTimeConByWorkId(map_1);
            map.put("shopInfo",shopList.get(0));
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success",false);
            map.put("msg","查询错误，请稍后重试");
            map.put("shopInfo",null);
        }
        return map;
    }


    //根据员工id来获取店铺信息
    @ResponseBody
    @RequestMapping(value = "/findShopWorkInfoByCons",method = RequestMethod.POST)
    public Map<String,Object> findShopWorkInfoByCons(@RequestBody Map<String,Object> map_1){
        Map<String, Object> map = new HashMap<>();
        map.put("success",true);
        map.put("msg","");
        try {
            List<Map<String,Object>>  workList = shopService.findShopWorkInfoByCons(map_1);
            map.put("workList",workList);
            String workerId = ToolsUtil.getString(map_1.get("workerId"));
            if(StringUtils.isNotBlank(workerId)){
                PageUtil pageUtil = new PageUtil();
                pageUtil.setWorkerId(workerId);
                List<WorkerPic>  workerPicList = workerService.findWorkerPicById(pageUtil);
                map.put("workePicrList",workerPicList);
                logger.info("====workePicrList="+ JSON.toJSONString(workerPicList));
            }

        } catch (Exception e) {
            e.printStackTrace();
            map.put("success",false);
            map.put("msg","查询错误，请稍后重试");
            map.put("shopInfo",null);
        }
        return map;
    }

    //根据员工id来获取店铺信息
    @ResponseBody
    @RequestMapping(value = "/updateJishiOnline",method = RequestMethod.POST)
    public Map<String,Object> updateJishiOnline(@RequestBody Map<String,Object> map_1){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success",true);
        map.put("msg","");
        try {
            int  num = shopService.updateJishiOnline(map_1);
            if(num<0){
                map.put("success",false);
                map.put("msg","");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success",false);
            map.put("msg","查询错误，请稍后重试");
        }
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/updShopTime",method = RequestMethod.POST)
    public Map<String,Object> updShopTime(@RequestBody Map<String,Object> map){
        Map<String, Object> objectMap = new HashMap<String, Object>();
        objectMap.put("success",true);
        objectMap.put("msg","");
        try {
            int  num = shopService.updateShopBusy(map);
            objectMap.put("num",num);
        } catch (Exception e) {
            e.printStackTrace();
            objectMap.put("success",false);
            objectMap.put("msg","修改错误");
            objectMap.put("num",0);
        }
        return objectMap;
    }


    @ResponseBody
    @RequestMapping(value = "/updWorkPoint",method = RequestMethod.POST)
    public Map<String,Object> updWorkPoint(@RequestBody Map<String,Object> map){
        Map<String, Object> objectMap = new HashMap<String, Object>();
        objectMap.put("success",true);
        objectMap.put("msg","");
        try {
            logger.info("===updWorkPoint==start==");
            int  num = shopService.updateWorkerJd(map);
            if(num>0){
                objectMap.put("success",true);
                objectMap.put("msg","");
            }else{
                objectMap.put("success",false);
                objectMap.put("msg","修改失败");
            }
        } catch (Exception e) {
            logger.info("updWorkPoint===error==="+e.getMessage());
            e.printStackTrace();
            objectMap.put("success",false);
            objectMap.put("msg","修改错误");
            objectMap.put("num",0);
        }
        logger.info("===updWorkPoint==end==");
        return objectMap;
    }

}
