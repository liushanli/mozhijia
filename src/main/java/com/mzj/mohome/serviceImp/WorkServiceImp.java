package com.mzj.mohome.serviceImp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mzj.mohome.entity.Worker;
import com.mzj.mohome.entity.WorkerPic;
import com.mzj.mohome.mapper.OrderMapper;
import com.mzj.mohome.mapper.ShopMapper;
import com.mzj.mohome.mapper.WorkersMapper;
import com.mzj.mohome.service.UserService;
import com.mzj.mohome.service.WorkerService;
import com.mzj.mohome.util.*;
import com.mzj.mohome.vo.PageUtil;
import com.mzj.mohome.vo.WorkerVo;
import com.mzj.mohome.vo.WorkerWxInfo;
import com.winnerlook.model.PrivacyBindBodyAxb;
import com.winnerlook.model.PrivacyUnbindBody;
import com.winnerlook.model.VoiceResponseResult;
import com.winnerlook.util.MD5Util;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("workerService")
public class WorkServiceImp implements WorkerService {
    private final static Logger logger = LoggerFactory.getLogger(WorkServiceImp.class);
    @Autowired
    private WorkersMapper workersMapper;
    @Autowired
    private ShopServiceImp shopService;

    @Autowired
    private UserService userService;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Value("${middleNumber}")
    private String middleNumber;

    @Value("${appId}")
    private String appId;

    @Value("${token}")
    private String token;


    @Value("${filePath}")
    private String filePath;

    @Value("${videoImageIIS}")
    private String iisPath;

    @Value("${logoUrl}")
    private String logoUrl;

    @Value("${distanceUrl}")
    private String distanceUrl;
    @Value("${clientAk}")
    private String clientAk;
    @Value("${serverAK}")
    private String serverAk;
    @Value("${addressUrl}")
    private String addressUrl;

    @Value("${video_path}")
    private String video_path;
    @Autowired
    private RequestApi requestApi;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat simpleDateF = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public List<Map<String,Object>> findWorkerList(PageUtil pageUtil) {
        DecimalFormat df = new DecimalFormat("0%");
        if(pageUtil!=null){
            String shopId = ToolsUtil.getString(pageUtil.getShopId());
            Integer page = pageUtil.getPage();
            Integer size = pageUtil.getSize();
            String workId = ToolsUtil.getString(pageUtil.getWorkerId());
            String userName = ToolsUtil.getString(pageUtil.getUserName());
            String shopName = ToolsUtil.getString(pageUtil.getShopName());
            String genderDesc = ToolsUtil.getString(pageUtil.getGenderDesc());
            String evalNmsDesc = ToolsUtil.getString(pageUtil.getEvalNmsDesc());
            if(StringUtils.isNotEmpty(evalNmsDesc)){
                evalNmsDesc = evalNmsDesc.equals("asc")?"1":"2";
            }
            String onLine = ToolsUtil.getString(pageUtil.getOnLine());
            String city = ToolsUtil.getString(pageUtil.getCity());
            String productId = ToolsUtil.getString(pageUtil.getProductId());
            if(page == null || page == 0) page = 1;
            if(size == null || size == 0) size = 4;
            Integer pages = (page-1)*size;
            List<Map<String,Object>> workerList = null;
            logger.info("findWorkerList===city==="+city);
            if(ToolsUtil.getCityFlag(city)){
                workerList  = workersMapper.findWorkerList(city,shopName,userName,workId,shopId,pages,size,genderDesc,onLine,productId,evalNmsDesc);
            }else if(!ToolsUtil.getCityFlag(city)){
                workerList  = workersMapper.findWorkerList_1(city,shopName,userName,workId,shopId,pages,size,genderDesc,onLine,productId,evalNmsDesc);
            }
            logger.info("====ToolsUtil.getCityFlag(city)=="+ToolsUtil.getCityFlag(city));
            logger.info("findWorkerList===workerList==="+workerList.size());
            for(Map<String,Object> worker : workerList){
                String radius = ToolsUtil.getString(worker.get("radius"));
                String distance = ToolsUtil.getString(worker.get("distance"));

                if(StringUtils.isNotEmpty(radius)){
                    Float radius_1 = Float.parseFloat(radius);
                    radius_1 = radius_1/1000;
                    float b = (float)(Math.round(radius_1*100))/100;
                    worker.put("radius",b);
                }
                if(distance!=null){
                    Float distanc = Float.parseFloat(distance);
                    if(distanc>1000){
                        distanc = distanc/1000;
                        float b = (float)(Math.round(distanc*100))/100;
                        worker.put("distance",b+"km");
                    }else{
                        worker.put("distance",distanc+"m");
                    }
                }
               /* String dateHHMM = workersMapper.getDateHHM(worker.get("workerId").toString());
                worker.put("dateHHmm",dateHHMM);*/
                List<Map<String,Object>> mapList = workersMapper.findWorkEvalStatus(worker.get("workerId").toString());
                if(mapList!=null && mapList.size()>0){
                    for(int i=0;i<mapList.size();i++){
                        Map<String,Object> map = mapList.get(i);
                        if(i==0)
                            worker.put("evalStatus_1",map.get("name").toString());
                        else if(i==1)
                            worker.put("evalStatus_2",map.get("name").toString());
                        else if(i==2)
                            worker.put("evalStatus_3",map.get("name").toString());
                    }
                }
                int number = 0;
                if(worker.get("sellSum")!=null){
                    number = (int) worker.get("sellSum")+(int) worker.get("sellNum");
                    worker.put("sellNum",number);
                }
                worker.put("sellSum",worker.get("sellSum")!=null?worker.get("sellSum"):0);
                List<Map<String,Object>> list =  workersMapper.findEvaluate(worker.get("workerId").toString());
                if(list!=null && list.size()>0){
                    Map<String,Object> map1 = list.get(0);
                    float maxNum = Float.parseFloat(map1.get("maxNum").toString());
                    float minNum =Float.parseFloat(map1.get("minNum").toString());
                    if (maxNum > 0) {
                        if (minNum/maxNum <= 0.2) {
                            worker.put("star", 1);
                        } else if (minNum/maxNum <= 0.4) {
                            worker.put("star", 2);
                        } else if (minNum/maxNum <= 0.6) {
                            worker.put("star", 3);
                        } else if (minNum/maxNum <= 0.8) {
                            worker.put("star", 4);
                        } else if (minNum/maxNum <= 1) {
                            worker.put("star", 5);
                        }
                    } else {
                        worker.put("star", 5);
                    }
                    worker.put("evaluateNumLv", minNum > 0 ? df.format(minNum / maxNum) : "100%");
                }else{
                    worker.put("evaluateNumLv","100%");
                    worker.put("star",5);
                }

            }
            //logger.info(JSON.toJSONString(workerList));
            logger.info("======");
            return workerList;
        }
        return null;
    }

    @Override
    public List<WorkerPic> findWorkerPicById(PageUtil pageUtil) {
        String workerId = "";
        if(pageUtil!=null){
             workerId = pageUtil.getWorkerId();
            if(StringUtils.isNotEmpty(workerId)){
                List<WorkerPic> workerPicList = workersMapper.findWorkerPicById(workerId);
                return workerPicList;
            }
        }
        return null;
    }

    //根据用户手机号，验证码来判断用户是否存在
    public Map<String,Object> findWorkerInfo(String phone,String sendCode){
        Map<String,Object> objectMap = new HashMap<>();
        try {
            objectMap.put("success",true);
            objectMap.put("msg","");
            objectMap.put("workVo",null);
            Map<String,Object> map1 =  shopMapper.findShopBySend(phone,sendCode);
            if(map1 == null){
                objectMap.put("success",false);
                objectMap.put("msg","手机号/验证码错误");
                objectMap.put("workVo",null);
            }else{
                //shopStatus为1的话，是店员登陆,为2时，是商家登录
                List<Worker> workerList = workersMapper.findWorkInfoExist(phone);

                if(workerList!=null && workerList.size()>0){
                    Worker worker = workerList.get(0);
                    workersMapper.updateLoginTime(worker.getWorkerId());
                    objectMap.put("success",true);
                    objectMap.put("msg","");
                    objectMap.put("workVo",worker);
                    objectMap.put("shopStatus",1);
                }else{
                    Map<String,Object> map2 = shopMapper.findShopByPhone(phone);
                    if(map2 != null){
                        objectMap.put("msg","");
                        objectMap.put("workVo",map2);
                        objectMap.put("success",true);
                        objectMap.put("shopStatus",2);
                    }else if(StringUtils.isNotEmpty(phone)){
                        logger.info("添加用户");
                        String uuid = UUID.randomUUID().toString();
                        String workerId = uuid.replaceAll("-","").toUpperCase();
                        shopMapper.addWorkInfo(workerId,phone);
                        Map<String, Object> map_3 = new HashMap<>();
                        map_3.put("shopStatus","1");
                        map_3.put("userName",phone);
                        map_3.put("phone",phone);
                        map_3.put("workerId",workerId);
                        map_3.put("imgUrl","");
                        objectMap.put("msg", "添加用户");
                        objectMap.put("success", true);
                        objectMap.put("workVo", map_3);
                    }else{
                        objectMap.put("msg", "不存在该账号");
                        objectMap.put("workVo", null);
                        objectMap.put("success", false);
                        objectMap.put("shopStatus", 1);
                    }
                }
            }
            return objectMap;
        } catch (Exception e) {
            e.printStackTrace();
            objectMap.put("success",false);
            objectMap.put("msg","服务异常");
            objectMap.put("workVo",null);
            return objectMap;
        }
    }

    //根据用户手机号，验证码来判断用户是否存在
    public Map<String,Object> findWorkerInfoWx(String phone,String sendCode,String openId){
        Map<String,Object> objectMap = new HashMap<>();
        try {
            objectMap.put("success",true);
            objectMap.put("msg","");
            objectMap.put("workVo",null);
            Map<String,Object> map1 =  shopMapper.findShopBySend(phone,sendCode);
            if(map1 == null){
                objectMap.put("success",false);
                objectMap.put("msg","手机号/验证码错误");
                objectMap.put("workVo",null);
            }else{
                //shopStatus为1的话，是店员登陆,为2时，是商家登录
                List<Worker> workerList = workersMapper.findWorkInfoExist(phone);

                if(workerList!=null && workerList.size()>0){
                    Worker worker = workerList.get(0);
                    logger.info("findWorkerInfoWx===获取到的技师信息为：{}",JSON.toJSONString(worker));
                    workersMapper.updateLoginTime(worker.getWorkerId());
                    objectMap.put("success",true);
                    objectMap.put("msg","");
                    objectMap.put("workVo",worker);
                    objectMap.put("shopStatus",1);
                    if(StringUtils.isNotBlank(openId)){
                        int count = workersMapper.findWorkerOpenInfoWx(worker.getWorkerId(),openId);
                        if(count>0){
                            objectMap.put("success",false);
                            objectMap.put("msg","微信已绑定："+worker.getUserName()+",请确认后重新绑定");
                        }else{
                            workersMapper.addWorkerOpenInfos(openId,worker.getWorkerId());
                        }
                    }
                }else{
                    Map<String,Object> map2 = shopMapper.findShopByPhone(phone);
                    if(map2 != null){
                        logger.info("findWorkerInfoWx===获取到的商家信息为：{}",JSON.toJSONString(map2));
                        objectMap.put("msg","");
                        objectMap.put("workVo",map2);
                        objectMap.put("success",true);
                        objectMap.put("shopStatus",2);
                        if(StringUtils.isNotBlank(openId)){
                            int count = workersMapper.findWorkerOpenInfo(map2.get("shopId").toString(),openId);
                            if(count>0){
                                objectMap.put("success",false);
                                objectMap.put("msg","微信已绑定："+map2.get("shopName").toString()+",请确认后重新绑定");
                            }else{
                                workersMapper.addWorkerOpenInfos(openId,map2.get("shopId").toString());
                            }
                        }
                    }else{
                        objectMap.put("msg","该手机号没有注册");
                        objectMap.put("success",false);
                        objectMap.put("workVo",null);
                        objectMap.put("shopStatus",1);
                    }
                }
            }
            return objectMap;
        } catch (Exception e) {
            e.printStackTrace();
            objectMap.put("success",false);
            objectMap.put("msg","服务异常");
            objectMap.put("workVo",null);
            return objectMap;
        }
    }

    //根据用户手机号，验证码来判断用户是否存在
    public Map<String,Object> findWorkerInfoBy(String phone,String version){
        Map<String,Object> objectMap = new HashMap<>();
        try {
            objectMap.put("success",true);
            objectMap.put("msg","");
            objectMap.put("workVo",null);

            //shopStatus为1的话，是店员登陆,为2时，是商家登录
            List<Worker> workerList = workersMapper.findWorkInfoExist(phone);
            if(StringUtils.isNotEmpty(version)){
                workersMapper.updateVersion(version,phone);
            }

            if(workerList!=null && workerList.size()>0){
                Worker worker = workerList.get(0);
                workersMapper.updateLoginTime(worker.getWorkerId());
                objectMap.put("bangFlag",false);
                int count = workersMapper.findOpenIdCount(worker.getWorkerId());
                if(count>0){
                    objectMap.put("bangFlag",true);
                }
                objectMap.put("success",true);
                objectMap.put("msg","");
                objectMap.put("workVo",worker);
                objectMap.put("shopStatus",1);
            }else {
                Map<String, Object> map2 = shopMapper.findShopByPhone(phone);
                if (map2 != null) {
                    objectMap.put("bangFlag",false);
                    int count = workersMapper.findOpenIdCount(map2.get("shopId").toString());
                    if(count>0){
                        objectMap.put("bangFlag",true);
                    }
                    objectMap.put("msg", "");
                    objectMap.put("workVo", map2);
                    objectMap.put("success", true);
                    objectMap.put("shopStatus", 2);
                } else{
                    objectMap.put("bangFlag",false);
                    objectMap.put("msg", "不存在该账号");
                    objectMap.put("workVo", null);
                    objectMap.put("success", false);
                    objectMap.put("shopStatus", 1);
                }
            }
            return objectMap;
        } catch (Exception e) {
            e.printStackTrace();
            objectMap.put("success",false);
            objectMap.put("msg","服务异常");
            objectMap.put("workVo",null);
            return objectMap;
        }
    }


    public Map<String,Object> findWorkInfo(Map<String,Object> map){

        Map<String,Object> objectMap = new HashMap<>();
        try {

            String phone = map.get("phone").toString();
            List<Worker> workerList_1 = workersMapper.findWorkInfoExist(phone);

            if(workerList_1!=null && workerList_1.size()>0){
                Worker worker = workerList_1.get(0);
                workersMapper.updateLoginTime(worker.getWorkerId());
                objectMap.put("success",true);
                objectMap.put("msg","");
                objectMap.put("workVo",worker);
                objectMap.put("shopStatus",1);
            }
        }catch (Exception e){
            logger.info("查询失败");
        }
       return objectMap;
    }

    public String updateLoginImg(Map<String,Object> map){
        try {
            String workerId = (map.get("workerId")!=null)?map.get("workerId").toString():"";
            String filePath = (map.get("filePath")!=null)?map.get("filePath").toString():"";
            String videoUrl = (map.get("videoUrl")!=null)?map.get("videoUrl").toString():"";
            logger.info("workerId="+workerId+"==filePath="+(iisPath+"/"+filePath)+"---"+videoUrl);
            int num = workersMapper.updateLoginImg(workerId,(iisPath+"/"+videoUrl),filePath);
            if(num>0){
                return (iisPath+"/"+videoUrl);
            }else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    @Override
    public List<Map<String, Object>> findWorkTime(Map<String, Object> map) {

        String workId = String.valueOf(map.get("workerId"));
        Integer cur = (Integer)map.get("cur");
        String workerId =workId;
        String beginTime = "";
        String endTime = "";

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

        List<Map<String,Object>> map1 = workersMapper.findWorkTime(beginTime,endTime,workerId);
        map.put("beginTime",beginTime);
        map.put("endTime",endTime);
        List<Map<String,Object>> map2 = shopService.findShopTime(map);
        List<Map<String,Object>> map3 = new ArrayList<>();

        if(map1!=null && map1.size()>0 && map2.size()>0 && map2!=null){
            for(int i=0;i<map1.size();i++){
                Map<String,Object> map4 = map1.get(i);
                if(map4.get("isBusy")==map2.get(i).get("isBusy")){
                        map4.put("busy",map4.get("isBusy"));
                }else{
                    map4.put("busy","1");
                }
                map3.add(map4);
            }
            return map3;
        }else {
            return null;
        }
    }

    public String getEndTime(Date start,Integer cur){
        Calendar c = Calendar.getInstance();
        c.setTime(start);
        c.add(Calendar.DAY_OF_MONTH, cur);// 今天+3天
        Date end = c.getTime();
       return simpleDateF.format(end);
    }

    public List<Map<String,Object>> findEvaluate(Map<String,Object> map){
       String id = String.valueOf(map.get("id"));
       List<Map<String,Object>> mapList = workersMapper.findEvaluateInfo(id);
       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       if(mapList!=null && mapList.size()>0){
           for(Map<String,Object> mapInfo : mapList){
               if(mapInfo.get("updateTime")!=null && mapInfo.get("updateTime")!=""){
                   mapInfo.put("updateTime",sdf.format(mapInfo.get("updateTime")));
               }
               List<Map<String,Object>> mapList1 = workersMapper.findWorkEvalBiao(mapInfo.get("id").toString());
               if(mapList1!=null && mapList1.size()>0){
                   mapInfo.put("list",mapList1);
               }else{
                   mapInfo.put("list",null);
               }
           }
       }

       return mapList;
    }

    public List<Map<String,Object>> findEvaluateByProductId(Map<String,Object> map){
        String id = String.valueOf(map.get("productId"));
        Integer row = 5;
        Integer page = map.get("page")!=null && map.get("page")!=""?Integer.parseInt(map.get("page").toString()):1;
        Integer start = (page-1)*row;
        Integer end = page*row;
        List<Map<String,Object>> mapList = workersMapper.findEvaluateByProductId(id,start,end);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(mapList!=null && mapList.size()>0){
            for(Map<String,Object> mapInfo : mapList){
                if(mapInfo.get("updateTime")!=null && mapInfo.get("updateTime")!=""){
                    mapInfo.put("updateTime",sdf.format(mapInfo.get("updateTime")));
                }
                List<Map<String,Object>> mapList1 = workersMapper.findWorkEvalBiao(mapInfo.get("id").toString());
                if(mapList1!=null && mapList1.size()>0){
                    mapInfo.put("list",mapList1);
                }else{
                    mapInfo.put("list",null);
                }
            }
        }

        return mapList;
    }

    //删除和员工关联图片
    public int delWorkPic(Map<String,Object> map){
        String workerId = ToolsUtil.getString(map.get("workerId"));
        return workersMapper.delWorkPic(workerId);
    }

    //添加和员工关联图片
    public int addWorkPic(Map<String,Object> map){
        List<String> list = (List<String>)map.get("imgUrlList");
        String workerId = ToolsUtil.getString(map.get("workerId"));
        if(list!=null){
            int orderNum = 0;
            for(String imgUrl:list){
                workersMapper.addWorkPic(workerId,imgUrl,"0",orderNum);
                orderNum = orderNum + 1;
            }
        }
      return 0;
    }

    //添加健康证，资格证，身份证等关联图片
    public int addWorkConPic(Map<String,Object> map){
        String imgUrl = ToolsUtil.getString(map.get("imgUrl"));
        String workerId = ToolsUtil.getString(map.get("workerId"));
        String imgType = ToolsUtil.getString(map.get("imgType"));
        workersMapper.addWorkPic(workerId,imgUrl,imgType,1);
        return 0;
    }


    //根据员工id，来查询员工图片
    public List<Map<String,Object>> findWorkPicList(Map<String,Object> map){
        String workerId = ToolsUtil.getString(map.get("workerId"));
        return workersMapper.findWorkPicList(workerId);
    }

    //根据员工id，来查询员工图片
    public List<Map<String,Object>> findWorkTypePicList(Map<String,Object> map){
        String workerId = ToolsUtil.getString(map.get("workerId"));
        List<Map<String,Object>> mapList = workersMapper.findWorkPicTypeList(workerId);
        if(mapList.size()<=0 || mapList == null){
            return null;
        }
        return mapList;
    }

    //修改员工信息
    public int updateWorkInfo(Map<String,Object> map){
        String userName = ToolsUtil.getString(map.get("userName"));
        String nickName = ToolsUtil.getString(map.get("nickName"));
        String gender = ToolsUtil.getString(map.get("gender"));
        String imgUrl = ToolsUtil.getString(map.get("imgUrl"));
        String introduce = ToolsUtil.getString(map.get("introduce"));
        String workerId = ToolsUtil.getString(map.get("workerId"));
        String idCard = ToolsUtil.getString(map.get("idcard"));
        Worker worker = new Worker();
        worker.setUserName(userName);
        worker.setNickName(nickName);
        worker.setGender(gender);
        worker.setImgUrl(imgUrl);
        worker.setIntroduce(introduce);
        worker.setWorkerId(workerId);
        worker.setIdcard(idCard);
        return workersMapper.updateWorkInfo(worker);
    }

    public int updateWorkBusy(Map<String,Object> map){
        String id = ToolsUtil.getString(map.get("id"));
        Integer isBusy = (Integer)map.get("isBusy");
        String dateStr = ToolsUtil.getString(map.get("dateStr"));
        String workerId = ToolsUtil.getString(map.get("workerId"));
        return workersMapper.updateWorkBusy(isBusy,dateStr,id,workerId);
    }

    //修改工作图片
    public int updWorkPic(Map<String,Object> map){
        String imgUrl = ToolsUtil.getString(map.get("imgUrl"));
        String workerId = ToolsUtil.getString(map.get("workerId"));
        String imgType = ToolsUtil.getString(map.get("imgType"));
        return workersMapper.updWorkPic(imgUrl,workerId,imgType);
    }

    public VoiceResponseResult httpsPrivacyBindAxb(Map<String,String> map){
        try{
            String cellPhoneA = String.valueOf(map.get("cellPhoneA"));
            String cellPhoneB = String.valueOf(map.get("cellPhoneB"));
            String cellPhone = String.valueOf(map.get("phone"));
            /** AXB模式小号绑定接口地址*/
            String url = "https://101.37.133.245:11008/voice/1.0.0/middleNumberAXB";

            PrivacyBindBodyAxb bindBodyAxb = new PrivacyBindBodyAxb();

            /** 设定绑定的隐私小号*/
            bindBodyAxb.setMiddleNumber(cellPhone);
            /** 设定与该隐私小号绑定的号码A*/
            bindBodyAxb.setBindNumberA(cellPhoneA);
            /** 设定与该隐私小号绑定的号码B*/
            bindBodyAxb.setBindNumberB(cellPhoneB);
            /** 设置是否开启通话录音  1:开启，0:关闭*/
            bindBodyAxb.setCallRec(0);
            /** 设置绑定关系有效时长 ,为空表示绑定关系永久，单位:秒*/
            bindBodyAxb.setMaxBindingTime(300);
            /** 设置是否透传主叫的号码到A  0:不透传; 1: 透传，不填默认不透传*/
            bindBodyAxb.setPassthroughCallerToA(0);
            /** 设置是否透传主叫的号码到B  0:不透传; 1: 透传，不填默认不透传*/
            bindBodyAxb.setPassthroughCallerToB(0);
            /** 设置用于接收呼叫结果的服务地址*/
            bindBodyAxb.setCallbackUrl("http://myip../...");

            /** 获取当前系统时间戳*/
            long timestamp = System.currentTimeMillis();
            /** 生成Base64转码后的参数authorization*/
            String authorization = Base64Util.encodeBase64(appId+":"+timestamp);
            /** 生成加密参数sig*/
            String sig = MD5Util.getMD5(appId + token + timestamp);
            /** 生成最终接口访问地址*/
            url = url + "/" + appId + "/" + sig;

            String body = JSON.toJSONString(bindBodyAxb);

            /** 调用接口*/
            HttpsService httpsService = new HttpsService();
            String result = httpsService.doPost(url, authorization, body, "UTF-8");
            VoiceResponseResult voiceResponseResult = new VoiceResponseResult();
            voiceResponseResult.setResult(result);
            logger.info("绑定成功："+JSON.toJSONString(voiceResponseResult));
            return voiceResponseResult;
        }catch (Exception e){
            logger.error("绑定小号："+e);
            return null;
        }
    }


    public VoiceResponseResult httpsPrivacyUnbind(Map<String,String> map){
        try{
            String cellPhoneA = String.valueOf(map.get("cellPhoneA"));
            String cellPhoneB = String.valueOf(map.get("cellPhoneB"));
            String cellPhone = String.valueOf(map.get("phone"));
            logger.info("解绑的手机号：cellPhoneA="+cellPhoneA+",cellPhoneB="+cellPhoneB+",cellPhone"+cellPhone);
            /** 小号解绑接口地址*/
            String url = "https://101.37.133.245:11008/voice/1.0.0/middleNumberUnbind";

            PrivacyUnbindBody unbindBody = new PrivacyUnbindBody();
            /** 获取当前系统时间戳*/
            long timestamp = System.currentTimeMillis();

            /** 设置需要解绑的小号*/
            unbindBody.setMiddleNumber(cellPhone);
            /** 设置与该小号绑定的号码A*/
            unbindBody.setBindNumberA(cellPhoneA);
            unbindBody.setBindNumberB(cellPhoneB);
            /** 生成Base64转码后的参数authorization*/
            String authorization = Base64Util.encodeBase64(appId+":"+timestamp);
            /** 生成加密参数sig*/
            String sig = MD5Util.getMD5(appId + token + timestamp);

            /** 生成最终接口访问地址*/
            url = url + "/" + appId + "/" + sig;

            String body = JSON.toJSONString(unbindBody);

            /** 调用接口解绑小号*/
            HttpsService httpsService = new HttpsService();
            String result = httpsService.doPost(url, authorization, body, "UTF-8");
            VoiceResponseResult voiceResponseResult = new VoiceResponseResult();
            voiceResponseResult.setResult(result);
            return voiceResponseResult;
        }catch (Exception e){
            logger.error("解绑错误与："+e);
            return null;
        }
    }

    public Map<String,Object> uploadWorkerVideo(Map<String,Object> map){
        Map<String,Object> map1 = new HashMap<String,Object>();
        String mp4Url = map.get("video").toString();
        String workerId = map.get("workerId").toString();
        mp4Url = filePath+mp4Url.substring(31);
        /*Map<String, Object> screenshot = ToolVideo.getScreenshot(mp4Url);
        String imageName = ImageUtil.exportImg2(logoUrl,screenshot.get("imgPath").toString(),video_path);
*/
        List<String> list = ToolsUtil.processImg(mp4Url,"D:\\work\\ffmpeg.exe");
        System.out.println(list.size());
        if(list.size()==13){
            //String imageName = ImageUtil.exportImg2(logoUrl,list.get(12),path+"/"+prePath+"/"+format);
            //封面图片
            String imgUrl = list.get(12);
            System.out.println(imgUrl);

            //String videoImages = iisPath+"/"+imageName;
            //String videoImages = imgUrl;
            //String imageName = ImageUtil.exportImg2(logoUrl,imgUrl,path+"/"+prePath+"/"+format);
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            String f = format.format(new Date());
            String imageName = ImageUtil.exportImg2(logoUrl,imgUrl,"D:/work/Admin/Upload/worker_ImgUrl/"+f);
            //封面图片
            map.put("success",true);
            String videoImages = iisPath+"/worker_ImgUrl/"+f+"/"+imageName;
            workersMapper.updateLoginImg(workerId,videoImages,map.get("video").toString());
            map1.put("videoImage",videoImages);
        }else{
            //map.put("videoImage","");
        }
        return  map1;
    }

    public List<Map<String,Object>> findEvaluateByWorkId(Map<String,Object> map){
        String workerId = ToolsUtil.getString(map.get("workerId"));
        String shopId = ToolsUtil.getString(map.get("shopId"));
       List<Map<String,Object>> mapList = workersMapper.findWorkEval(workerId,shopId);
        return mapList;
    }

    public Map<String,Object> findWorkOrderNum(Map<String,Object> map){
        String workerId = ToolsUtil.getString(map.get("workerId"));
        String shopId = ToolsUtil.getString(map.get("shopId"));
        List<Map<String,Object>> mapList = workersMapper.findWorkOrderNum(workerId,shopId);
        Map<String,Object> map1 = new HashMap<>();
        if(mapList.size()>0 && mapList != null){
            for(Map<String,Object> map2 : mapList){
                map1.put(map2.get("status").toString(),map2.get("orderNum"));
            }
            //成功接单数：
            map1.put("successOrder",map1.get("1"));
            //加单率
            map1.put("addOrder","0%");
            //退款率
            String returnNum = ToolsUtil.getNumberFormat(Integer.parseInt(ToolsUtil.getString(map1.get("2"))),Integer.parseInt(ToolsUtil.getString(map1.get("1")))) ;
            map1.put("returnNum",returnNum);

            //好评率
            Integer goodNum = Integer.parseInt(ToolsUtil.getString(map1.get("3")));
            Integer badNum = Integer.parseInt(ToolsUtil.getString(map1.get("4")));
            Integer totalNum = goodNum+badNum;
            if(goodNum>0){
                map1.put("goodNum",ToolsUtil.getNumberFormat(goodNum,totalNum));
            }else{
                map1.put("goodNum","100%");
            }
        }
        return map1;
    }


    public Map<String,Object> findWorkOrderNumInfo(Map<String,Object> map){
        String workerId = ToolsUtil.getString(map.get("workerId"));
        String shopId = ToolsUtil.getString(map.get("shopId"));
        //判断是近三天，近一周，近一月，还是全部
        String msgFlag = ToolsUtil.getString(map.get("msgFlag"));
        List<Map<String,Object>> mapList = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Map<String,Object>> mapList1 = null;
        if(msgFlag.equals("0")){
            mapList = workersMapper.findWorkOrderNumberAll(workerId,shopId);
            mapList1 = workersMapper.findWorkerOrderDetail(workerId,shopId,"","",Integer.parseInt(msgFlag));
        }else if (msgFlag.equals("1")){
            Date date = ToolsUtil.getBeforeOrAfterDate(new Date(),-3);
            mapList = workersMapper.findWorkOrderNumber(workerId,shopId,sdf.format(date),sdf.format(new Date()));
            mapList1 = workersMapper.findWorkerOrderDetail(workerId,shopId,sdf.format(date),sdf.format(new Date()),Integer.parseInt(msgFlag));
        }else if (msgFlag.equals("2")){
            Date date = ToolsUtil.getBeforeOrAfterDate(new Date(),-7);
            mapList = workersMapper.findWorkOrderNumber(workerId,shopId,sdf.format(date),sdf.format(new Date()));
            mapList1 = workersMapper.findWorkerOrderDetail(workerId,shopId,sdf.format(date),sdf.format(new Date()),Integer.parseInt(msgFlag));
        }else if (msgFlag.equals("3")){
            Date date = ToolsUtil.getBeforeOrAfterMontg(new Date(),-1);
            mapList = workersMapper.findWorkOrderNumber(workerId,shopId,sdf.format(date),sdf.format(new Date()));
            mapList1 = workersMapper.findWorkerOrderDetail(workerId,shopId,sdf.format(date),sdf.format(new Date()),Integer.parseInt(msgFlag));
        }
        Map<String,Object> map1 = new HashMap<>();
        if(mapList.size()>0 && mapList != null){
            for(Map<String,Object> map2 : mapList){
                map1.put("OrderNum"+map2.get("status").toString(),map2.get("orderNum"));
                map1.put("orderPrice"+map2.get("status").toString(),map2.get("payOnLine"));
            }
        }
        map1.put("detailList",mapList1);
        return map1;
    }

    public int findUpdateOrder(Map<String,Object> map){
        String orderId = map.get("orderId").toString();
        Map<String,Object> orderInfo = orderMapper.findOrder(orderId);
        String workerId = orderInfo.get("workerId").toString();
        String date = orderInfo.get("aboutTime").toString();
        List<Map<String,Object>> orderList = workersMapper.findInfo(workerId,orderId,date);
        for(Map<String,Object> map1 : orderList){
            workersMapper.updateWorkBusy(1,null,map1.get("id").toString(),null);
        }
        return 0;
    }

    public Map<String,Object> findShopByCode(Map<String,Object> map){
        String code = ToolsUtil.getString(map.get("code"));
        String shopId = ToolsUtil.getString(map.get("shopId"));
        Map<String,Object> map1 = workersMapper.findShopByCode(code,shopId);
        if(map1 != null){
            map1.put("success",true);
            map1.put("msg","");
        }else{
            map1.put("success",false);
            map1.put("msg","邀请码输入错误，请您查证后再输入");
        }
        return map1;
    }

    public Map<String,Object> updShopByCode(Map<String,Object> map) {
        String wokerId = ToolsUtil.getString(map.get("workerId"));
        String shopCodeStatus = ToolsUtil.getString(map.get("shopCodeStatus"));
        String shopId = ToolsUtil.getString(map.get("shopId"));
        int nums = workersMapper.updShopByCode(Integer.parseInt(shopCodeStatus), wokerId,shopId);
        Map<String, Object> map1 = new HashMap<>();
        if (nums > 0) {
            map1.put("success", true);
            map1.put("msg", "");
        } else {
            map1.put("success", false);
            map1.put("msg", "申请失败");
        }
        return map1;
    }


    @Override
    public List<Map<String,Object>> findWorkerList_1(PageUtil pageUtil) {
        DecimalFormat df = new DecimalFormat("0%");
        if(pageUtil!=null){
            String shopId = ToolsUtil.getString(pageUtil.getShopId());
            Integer page = (pageUtil.getPage()-1)*5;
            Integer size = pageUtil.getPage()*5;
            String workId = ToolsUtil.getString(pageUtil.getWorkerId());
            String userName = ToolsUtil.getString(pageUtil.getUserName());
            String shopName = ToolsUtil.getString(pageUtil.getShopName());
            String genderDesc = ToolsUtil.getString(pageUtil.getGenderDesc());

            String evalNmsDesc = ToolsUtil.getString(pageUtil.getEvalNmsDesc());
            if(StringUtils.isNotEmpty(evalNmsDesc)){
                evalNmsDesc = evalNmsDesc.equals("asc")?"1":"2";
            }
            String onLine = ToolsUtil.getString(pageUtil.getOnLine());
            String city = ToolsUtil.getString(pageUtil.getCity());
            String productId = ToolsUtil.getString(pageUtil.getProductId());
            if(onLine==null){
                onLine = "1";
            }
            logger.info("findWorkerList===city==="+city);

            String jd = ToolsUtil.getString(pageUtil.getJd());
            String wd = ToolsUtil.getString(pageUtil.getWd());
            logger.info("经纬度：jd={},wd={}",jd,wd);
            List<Map<String,Object>> workerList  = workersMapper.findWorkerList_2(jd,wd,city,shopName,userName,workId,shopId,page,size,genderDesc,onLine,productId,evalNmsDesc);
            List<Map<String,Object>> workerList_1  = new ArrayList<>();
            logger.info("====ToolsUtil.getCityFlag(city)=="+ToolsUtil.getCityFlag(city));
            logger.info("findWorkerList===workerList==="+workerList.size());
           if(workerList!=null && workerList.size()>0){

               logger.info("-------start------");
               Long oldtime=System.currentTimeMillis();
                for(Map<String,Object> worker : workerList) {

                    String radius = ToolsUtil.getString(worker.get("radius"));
                    String workerId = worker.get("workerId").toString();
                   /* String dateMM = workersMapper.getDateMM(workerId);
                    worker.put("dateHHmm",dateMM);*/
                    Long distance = Long.parseLong(worker.get("distance").toString()) ;
                    if(StringUtils.isNotEmpty(radius)){
                        Float radius_1 = Float.parseFloat(radius);
                        radius_1 = radius_1/1000;
                        float b = (float)(Math.round(radius_1*100))/100;
                        worker.put("workerRadius",b);
                    }

                    if (distance != null) {
                        Float distanc = (float)distance;
                        if (distanc > 1000) {
                            distanc = distanc / 1000;
                            float b = (float) (Math.round(distanc * 100)) / 100;
                            worker.put("distance", b + "km");
                        } else {
                            worker.put("distance", distanc + "m");
                        }
                    }
                    //获取技师的标签
                    List<Map<String, Object>> mapList = workersMapper.findWorkEvalStatus(workerId);
                    logger.info("技师的标签：{}",mapList!=null?JSON.toJSONString(mapList):"");
                    if (mapList != null && mapList.size() > 0) {
                        for (int i = 0; i < mapList.size(); i++) {
                            Map<String, Object> map = mapList.get(i);
                            if (i == 0) {
                                worker.put("evalStatus_1", map.get("name").toString());
                            } else if (i == 1) {
                                worker.put("evalStatus_2", map.get("name").toString());
                            } else if (i == 2) {
                                worker.put("evalStatus_3", map.get("name").toString());
                            }
                        }
                    }
                    int number = 0;
                    if (worker.get("sellSum") != null) {
                        number = (int) worker.get("sellSum") + (int) worker.get("sellNum");
                        worker.put("sellNum", number);
                    }
                    logger.info("获取技师的订单数量：{}",number);
                    worker.put("sellSum", worker.get("sellSum") != null ? worker.get("sellSum") : 0);

                    String quality = ToolsUtil.getStringValue(worker.get("quality"));
                    String percentage = ToolsUtil.getStringValue(worker.get("percentage"));
                    if(StringUtils.isNotEmpty(quality)){
                        worker.put("star",quality);
                    }else{
                        worker.put("star",5);
                    }

                    if(StringUtils.isNotEmpty(percentage)){
                        worker.put("evaluateNumLv",percentage);
                    }else{
                        worker.put("evaluateNumLv","100%");
                    }
                  /*  List<Map<String, Object>> list = workersMapper.findEvaluate(worker.get("workerId").toString());
                    if (list != null && list.size() > 0) {
                        Map<String,Object> map1 = list.get(0);
                        float maxNum = Float.parseFloat(map1.get("maxNum").toString());
                        float minNum =Float.parseFloat(map1.get("minNum").toString());
                        logger.info("获取用户的总数{}，成功数量：{}",maxNum,minNum);
                        if (maxNum > 0) {
                            if (minNum/maxNum <= 0.2) {
                                worker.put("star", 1);
                            } else if (minNum/maxNum <= 0.4) {
                                worker.put("star", 2);
                            } else if (minNum/maxNum <= 0.6) {
                                worker.put("star", 3);
                            } else if (minNum/maxNum <= 0.8) {
                                worker.put("star", 4);
                            } else if (minNum/maxNum <= 1) {
                                worker.put("star", 5);
                            }
                        } else {
                            worker.put("star", 5);
                        }
                        worker.put("evaluateNumLv", minNum > 0 ? df.format(minNum / maxNum) : "100%");
                    } else {
                        worker.put("evaluateNumLv", "100%");
                        worker.put("star", 5);
                    }*/
                    workerList_1.add(worker);
                }
               Long systime=System.currentTimeMillis();
               Long time = (systime - oldtime);//相差毫秒数
                logger.info("jieshu的毫秒为：{}",time);
            }
           logger.info("===findWorkerList_1 end==");
            return workerList;
        }
        return null;
    }

    @Override
    public List<Map<String,Object>> findWorkerList_2(PageUtil pageUtil) {
        logger.info("===================findWorkerList_2====start===============请求参数为：{}",JSON.toJSONString(pageUtil));
        if(pageUtil!=null){
            String shopId = ToolsUtil.getString(pageUtil.getShopId());
            Integer page = (pageUtil.getPage()-1)*4;
            Integer size = 4;
            String workId = ToolsUtil.getString(pageUtil.getWorkerId());
            String userName = ToolsUtil.getString(pageUtil.getUserName());
            String shopName = ToolsUtil.getString(pageUtil.getShopName());
            String genderDesc = ToolsUtil.getString(pageUtil.getGenderDesc());

            String evalNmsDesc = ToolsUtil.getString(pageUtil.getEvalNmsDesc());
            if(StringUtils.isNotEmpty(evalNmsDesc)){
                evalNmsDesc = evalNmsDesc.equals("asc")?"1":"2";
            }
            String onLine = ToolsUtil.getString(pageUtil.getOnLine());
            String city = ToolsUtil.getString(pageUtil.getCity());
            String productId = ToolsUtil.getString(pageUtil.getProductId());
            if(onLine==null){
                onLine = "1";
            }
            logger.info("findWorkerList_2===city==="+city);

            String jd = ToolsUtil.getString(pageUtil.getJd());
            String wd = ToolsUtil.getString(pageUtil.getWd());
            logger.info("findWorkerList_2==经纬度：jd={},wd={}",jd,wd);
            List<Map<String,Object>> workerList  = workersMapper.findWorkerList_4(jd,wd,city,shopName,userName,workId,shopId,page,size,genderDesc,onLine,productId,evalNmsDesc);
            List<Map<String,Object>> workerList_1  = new ArrayList<>();
            logger.info("====ToolsUtil.getCityFlag(city)=="+ToolsUtil.getCityFlag(city));
            logger.info("findWorkerList===workerList==="+workerList.size());
            if(workerList!=null && workerList.size()>0){

                logger.info("-------start------");
                Long oldtime=System.currentTimeMillis();
                for(Map<String,Object> worker : workerList) {
                    String radius = ToolsUtil.getString(worker.get("radius"));
                    String workerId = worker.get("workerId").toString();
                    Float radius_1 = Float.parseFloat(radius);

                    Integer distance = getDistances(wd+","+jd,worker.get("wd")+","+worker.get("jd"));
                   
                    if(radius_1<distance){
                        worker.put("distance", "超出范围");
                    }else{
                        if(distance<1000){
                            worker.put("distance", distance + "m");
                        }else{
                            BigDecimal bd = new BigDecimal(distance);
                            BigDecimal distanceLen = bd.divide(new BigDecimal(1000), 2, BigDecimal.ROUND_HALF_UP);
                            worker.put("distance", distanceLen + "km");
                        }
                    }

                    //好评百分比
                    worker.put("evaluateNumLv",worker.get("evaluateNumLv").toString()+"%");

                    //标签
                    if(worker.get("evalStatus_one")!=null) {
                        worker.put("evalStatus_1", worker.get("evalStatus_one").toString());
                    }
                    if(worker.get("evalStatus_two")!=null) {
                        worker.put("evalStatus_2", worker.get("evalStatus_two").toString());
                    }
                    if(worker.get("evalStatus_three")!=null){
                        worker.put("evalStatus_3", worker.get("evalStatus_three").toString());
                    }
                    int sellSum = 0;
                    int sellNum = 0;
                    if (worker.get("sellSum") != null) {
                        sellSum = (int) worker.get("sellSum");
                    }
                    if (worker.get("sellNum") != null) {
                        sellNum = (int) worker.get("sellNum");
                    }
                    worker.put("sellSum",sellSum+sellNum);

                    workerList_1.add(worker);
                }
                Long systime=System.currentTimeMillis();
                Long time = (systime - oldtime);//相差毫秒数
                logger.info("结束的毫秒为：{}",time);
            }
            logger.info("===================findWorkerList_2====end===============:"+workerList_1.size());
            return workerList_1;
        }
        return null;
    }



    public List<Map<String,Object>> workerInfoListByInfo(String shopId,String city){
        List<Map<String,Object>> workList =  workersMapper.workerInfoListByInfo(shopId,city);
        if(workList!=null && workList.size()>0){
            for(Map<String,Object> map : workList){
                String sellSum = ToolsUtil.getString(map.get("sellSum"));
                String sellNum = ToolsUtil.getString(map.get("sellNum"));

                Integer sell_num = sellNum!=null?Integer.parseInt(sellNum):0;
                Integer sell_sum = sellSum!=null?Integer.parseInt(sellSum):0;
                map.put("sellNum",sell_num+sell_sum);
            }
        }
        logger.info("明星有=="+workList.size()+"个");
        return workList;
    }

    public Map<String,Object> queryWorkerInfo(Map<String,Object> map){
        Map<String,Object> objectMap = new HashMap<>();
        objectMap.put("success",true);
        objectMap.put("msg","");
        try {
            String workerId = map.get("workerId").toString();
            String orderJd = map.get("jd").toString();
            String orderWd = map.get("wd").toString();
            Worker worker = workersMapper.queryTbWorkerInfo(workerId);

            Integer dis = getDistances(orderWd+","+orderJd,worker.getWd()+","+worker.getJd());
            logger.info("dis:{},worker=={}",dis,JSON.toJSONString(worker));
            if(dis>worker.getRadius()){
                objectMap.put("success",false);
                objectMap.put("msg","距离太远，技师无法到达");
            }
            return objectMap;
        }catch (Exception e){
            logger.error("错误信息为：{}",e);
            objectMap.put("success",false);
            objectMap.put("msg","服务异常");
            return objectMap;
        }
    }

    //获取两者之间的距离
    public Integer getDistances(String start, String end){
        Map<String,String> map_1 = new HashMap<>();
        map_1.put("origin",start);
        map_1.put("destination",end);
        map_1.put("steps_info","0");
        map_1.put("ak",serverAk);
        JSONObject jsonObject = requestApi.getApi(distanceUrl,map_1);

        //数据解析
        JSONObject result = jsonObject.getJSONObject("result");
        Integer distance = 0;
        if(result != null){
            JSONArray jsonArray = (JSONArray) result.get("routes");
            distance = (Integer) ((JSONObject) jsonArray.get(0)).get("distance");//获取举例米
        }
        logger.info("请求返回两者的距离为："+distance);
        return distance;
    }



    public int updWorkerInfoJW(Map<String,Object> paramMap){
        WorkerVo workerVo = new WorkerVo();
        workerVo.setJd(Double.parseDouble(ToolsUtil.getString(paramMap.get("jd"))));
        workerVo.setWd(Double.parseDouble(ToolsUtil.getString(paramMap.get("wd"))));
        workerVo.setWorkerId(ToolsUtil.getString(paramMap.get("workerId")));
        workerVo.setProvince(ToolsUtil.getString(paramMap.get("province")));
        workerVo.setCity(ToolsUtil.getString(paramMap.get("city")));
        workerVo.setArea(ToolsUtil.getString(paramMap.get("area")));
        //status为1的时候,根据姓名查询id,否则，不查询
        if(ToolsUtil.getString(paramMap.get("status")).equals("1")){
            String provinceId = workersMapper.findProvinceInfo(ToolsUtil.getString(paramMap.get("province")));
            String cityId = workersMapper.findProvinceInfo(ToolsUtil.getString(paramMap.get("city")));
            String areaId = workersMapper.findProvinceInfo(ToolsUtil.getString(paramMap.get("area")));
            workerVo.setProvinceId(provinceId);
            workerVo.setCityId(cityId);
            workerVo.setAreaId(areaId);
        }else{
            workerVo.setProvinceId(ToolsUtil.getString(paramMap.get("provinceId")));
            workerVo.setCityId(ToolsUtil.getString(paramMap.get("cityId")));
            workerVo.setAreaId(ToolsUtil.getString(paramMap.get("areaId")));
        }
        logger.info("====updWorkerInfoJW===修改信息为：{}",JSON.toJSONString(workerVo));
        return workersMapper.updWorkInfo(workerVo);
    }

    public WorkerVo findWorkLocation(String workerId){
        return workersMapper.findWorkerLocation(workerId);
    }



    /*@Scheduled(cron = "0 0/5 * * * ?")
    @Async*/
    public void updateWorkDateHHmm(){
        Long oldtime=System.currentTimeMillis();
        logger.info("updateWorkDateHHmm=====修改时间日期定时开始："+(new Date()));
        List<Map<String,String>> workerList = workersMapper.queryWorkerList();

        if(workerList.size()>0 && workerList != null){
            updateWorkerInfoLabel(workerList);
            for(Map<String,String> worker : workerList ){
                String workerId = worker.get("workerId");
                String dateHHmm = workersMapper.getDateMM(workerId);
                if(StringUtils.isEmpty(worker.get("dateHHmm")) || !dateHHmm.equals(worker.get("dateHHmm"))){
                    workersMapper.updWorkerInfo(workerId,dateHHmm);
                }
            }
        }
        Long systime=System.currentTimeMillis();
        //相差毫秒数
        Long time = systime - oldtime;
        logger.info("updateWorkDateHHmm=====修改时间日期定时结束==结束的毫秒为：{}",time);
        logger.info("updateWorkDateHHmm=====修改时间日期定时结束："+(new Date()));
    }
    //获取标签，进行修改信息
    private void updateWorkerInfoLabel(List<Map<String,String>> workerList){
        Long oldtime=System.currentTimeMillis();
        logger.info("updateWorkerInfoLabel===start==技师标签修改："+(new Date()));
        for(Map<String,String> worker : workerList ){
            String evalStatus_1 = "";
            String evalStatus_2 = "";
            String evalStatus_3 = "";
            String workerId = worker.get("workerId");
            List<Map<String, Object>> mapList = workersMapper.findWorkEvalStatus(workerId);
            logger.info("技师:"+workerId+"的标签："+mapList!=null?JSON.toJSONString(mapList):"");
            if (mapList != null && mapList.size() > 0) {
                for (int i = 0; i < mapList.size(); i++) {
                    Map<String, Object> map = mapList.get(i);
                    if (i == 0) {
                        evalStatus_1 = map.get("name").toString();
                    } else if (i == 1) {
                        evalStatus_2 = map.get("name").toString();
                    } else if (i == 2) {
                        evalStatus_3 = map.get("name").toString();
                    }
                }
                workersMapper.updWorkerInfoLabel(workerId,evalStatus_1,evalStatus_2,evalStatus_3);
            }
        }
        Long systime=System.currentTimeMillis();
        //相差毫秒数
        Long time = systime - oldtime;
        logger.info("updateWorkerInfoLabel===end==技师标签修改==结束的毫秒为：{}",time);
        logger.info("updateWorkerInfoLabel===end==技师标签修改："+(new Date()));
    }
}
