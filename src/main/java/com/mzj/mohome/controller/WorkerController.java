package com.mzj.mohome.controller;

import com.alibaba.fastjson.JSON;
import com.mzj.mohome.entity.WorkerPic;
import com.mzj.mohome.service.WorkerService;
import com.mzj.mohome.util.ImageUtil;
import com.mzj.mohome.util.ToolsUtil;
import com.mzj.mohome.vo.PageUtil;
import com.mzj.mohome.vo.WorkerVo;
import com.winnerlook.model.VoiceResponseResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/worker")
public class WorkerController {
    private final static Logger logger = LoggerFactory.getLogger(WorkerController.class);

    @Autowired
    private WorkerService workerService;

    @Value("${filePath}")
    private String path;

    @Value("${iisPath}")
    private String iisPath;

    @Value("${logoUrl}")
    private String logoUrl;


    @ResponseBody
    @RequestMapping(value = "/findWorkByPhone",method = RequestMethod.POST)
    public Map<String,Object> findWorkByPhone(@RequestBody Map<String,Object> map_1){
        Map<String,Object> map = new HashMap<>();
        try{
            logger.info("WorkerController.findWorkByPhone====请求信息为：{}",JSON.toJSONString(map_1));
            String phone = String.valueOf(map_1.get("phone"));
            String sendCode = String.valueOf(map_1.get("sendCode"));
            if(StringUtils.isNotEmpty(phone) && StringUtils.isNotEmpty(sendCode)){
                map = workerService.findWorkerInfo(phone,sendCode);
            }
        }catch (Exception e){
            map.put("msg","系统出现异常，请稍后重试");
            map.put("success","false");
            map.put("workVo",null);
        }
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/findWorkInfoByPhone",method = RequestMethod.POST)
    public Map<String,Object> findWorkInfoByPhone(@RequestBody Map<String,Object> map_1){
        Map<String,Object> map = new HashMap<>();
        try{
            String phone = String.valueOf(map_1.get("phone"));
            String version = "";
            if(map_1.get("version")!=null){
                version = String.valueOf(map_1.get("version"));
            }
            if(StringUtils.isNotEmpty(phone)){
                map = workerService.findWorkerInfoBy(phone,version);
            }
        }catch (Exception e){
            map.put("msg","系统出现异常，请稍后重试");
            map.put("success","false");
            map.put("workVo",null);
        }
        return map;
    }

    @ResponseBody
    @PostMapping("/findWorkListByShop")
    public Map<String,Object> findWorkListByShop(@RequestBody PageUtil pageUtil){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success",true);
        map.put("msg","");
        try {
            List<Map<String,Object>>  workerList = workerService.findWorkerList(pageUtil);
            map.put("workerList",workerList);
            List<WorkerPic>  workerPicList = workerService.findWorkerPicById(pageUtil);
            map.put("workePicrList",workerPicList);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success",false);
            map.put("msg","查询错误，请稍后重试");
        }
        return map;
    }


    @ResponseBody
    @PostMapping("/findWorkListByShop_1")
    public Map<String,Object> findWorkListByShop_1(@RequestBody PageUtil pageUtil){
        Map<String, Object> map = new HashMap<>();
        map.put("success",true);
        map.put("msg","");
        try {
            List<Map<String,Object>>  workerList = workerService.findWorkerList_1(pageUtil);
            map.put("workerList",workerList);
            List<WorkerPic>  workerPicList = workerService.findWorkerPicById(pageUtil);
            map.put("workePicrList",workerPicList);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success",false);
            map.put("msg","查询错误，请稍后重试");
        }
        return map;
    }

    @ResponseBody
    @PostMapping("/findWorkPicList")
    public Map<String,Object> findWorkPicList(@RequestBody PageUtil pageUtil){
        Map<String, Object> map = new HashMap<>();
        map.put("success",true);
        map.put("msg","");
        try {
            List<WorkerPic>  workerPicList = workerService.findWorkerPicById(pageUtil);
            map.put("workePicrList",workerPicList);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success",false);
            map.put("msg","查询错误，请稍后重试");
        }
        return map;
    }

    @ResponseBody
    @PostMapping("/findWorkListByShop_2")
    public Map<String,Object> findWorkListByShop_2(@RequestBody PageUtil pageUtil){
        Map<String, Object> map = new HashMap<>();
        map.put("success",true);
        map.put("msg","");
        try {
            List<Map<String,Object>>  workerList = workerService.findWorkerList_2(pageUtil);
            map.put("workerList",workerList);
            if(StringUtils.isNotBlank(pageUtil.getWorkerId())){
                List<WorkerPic>  workerPicList = workerService.findWorkerPicById(pageUtil);
                map.put("workePicrList",workerPicList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success",false);
            map.put("msg","查询错误，请稍后重试");
        }
        return map;
    }



    @ResponseBody
    @PostMapping("/findWorkEval")
    public Map<String,Object> findWorkEval(@RequestBody Map<String,Object> objectMap){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success",true);
        map.put("msg","");
        try {
            List<Map<String,Object>> evalList = workerService.findEvaluate(objectMap);
            map.put("evalList",evalList);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success",false);
            map.put("msg","查询错误，请稍后重试");
            map.put("evalList",null);
        }

        return map;
    }

    @ResponseBody
    @PostMapping("/findWorkEvalByProduct")
    public Map<String,Object> findWorkEvalByProduct(@RequestBody Map<String,Object> objectMap){
        logger.info("请求参数：{}",JSON.toJSONString(objectMap));
        Map<String, Object> map = new HashMap<>();
        map.put("success",true);
        map.put("msg","");
        try {
            List<Map<String,Object>> evalList = workerService.findEvaluateByProductId(objectMap);
            map.put("evalList",evalList);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success",false);
            map.put("msg","查询错误，请稍后重试");
            map.put("evalList",null);
        }

        return map;
    }

    @ResponseBody
    @PostMapping("/findWorkPicListByWork")
    public Map<String,Object> findWorkPicListByWork(@RequestBody PageUtil pageUtil){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success",true);
        map.put("msg","");
        try {
            List<WorkerPic>  workerPicList = workerService.findWorkerPicById(pageUtil);
            map.put("workePicrList",workerPicList);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success",false);
            map.put("msg","查询错误，请稍后重试");
        }
        return map;
    }


    //获取时间
    @ResponseBody
    @RequestMapping(value = "/findWorkTimeById",method = RequestMethod.POST)
    public Map<String,Object> findWorkTimeById(@RequestBody Map<String,Object> map_1){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success",true);
        map.put("msg","");
        try {
            List<Map<String,Object>>  workerTimeList = workerService.findWorkTime(map_1);
            map.put("workerTimeList",workerTimeList);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success",false);
            map.put("msg","查询错误，请稍后重试");
        }
        return map;
    }

    //获取最近三天的时间
    @ResponseBody
    @RequestMapping(value = "/getWorkNearTime",method = RequestMethod.POST)
    public Map<String,Object> getWorkNearTime(@RequestBody Map<String,Object> map_1){
        Map<String, Object> map = new HashMap<String, Object>();
        SimpleDateFormat simpleDateF = new SimpleDateFormat("yyyy-MM-dd");
        map.put("success",true);
        map.put("msg","");
        try {
            Date d1 = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(d1);
            cal.add(Calendar.MINUTE, 30);
            Date start = cal.getTime();
            Calendar c = Calendar.getInstance();
            c.setTime(start);
            c.add(Calendar.DAY_OF_MONTH, 0);
            Date tody = c.getTime();

            c.add(Calendar.DAY_OF_MONTH, 1);
            Date tommow = c.getTime();
            c.add(Calendar.DAY_OF_MONTH, 1);
            Date after = c.getTime();

            map.put("today",simpleDateF.format(tody));
            map.put("tomorrow",simpleDateF.format(tommow));
            map.put("after",simpleDateF.format(after));

        } catch (Exception e) {
            e.printStackTrace();
            map.put("success",false);
            map.put("msg","查询错误，请稍后重试");
        }
        return map;
    }

    /**
     * 员工上传图片
     * @param uploadFile
     * @param req
     * @return
     */
    @ResponseBody
    @PostMapping("/uploadImg")
    public Map<String,Object> uploadImg(MultipartFile uploadFile, HttpServletRequest req,String prePath){
        Map<String,Object> map = new HashMap<>();
        logger.info("======uploadImg======");
        //path += prePath; //保存到硬盘路径
        //iisPath += prePath; //保存到数据表中的路径
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
        //获取文件名
        String fileName = uploadFile.getOriginalFilename();
        //获取文件后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //重新生成文件名
        //fileName = UUID.randomUUID()+suffixName;
        //添加日期目录
        String format = sd.format(new Date());
        try {
            //指定本地文件夹存储图片
            String filePath = path+"/"+prePath+"/"+format;
            String iisImage = iisPath+"/"+prePath+"/"+format;
            System.out.println(filePath);
            File file = new File(filePath,fileName);
            if (!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            file.createNewFile();

            //第一步：上传文件
            uploadFile.transferTo(new File(filePath+"/"+fileName));
            map.put("imgUrl",iisImage+"/"+fileName);
            map.put("saveImgUrl",filePath+"/"+fileName);
            map.put("success",true);
            //第二步判断是否是视频文件
            if((suffixName.toUpperCase()).equals(".MP4")){

               List<String> list = ToolsUtil.processImg(filePath+"/"+fileName,"D:\\work\\ffmpeg.exe");
                if(list.size()==13){
                  //String imageName = ImageUtil.exportImg2(logoUrl,list.get(12),path+"/"+prePath+"/"+format);
                    //封面图片
                    String imgUrl = list.get(12);

                    map.put("videoImage",list.get(12));
                }else{
                    map.put("videoImage","");
                }
            }
            return map;
        } catch (Exception e) {
            logger.error("上传图片失败");
            e.printStackTrace();
            map.put("imgUrl","");
            map.put("saveImgUrl","");
            map.put("success",false);
            return map;
        }
    }
    /**
     * 员工添加水印
     * @return
     */
    @ResponseBody
    @PostMapping("/uploadPhoto")
    public Map<String,Object> uploadPhoto(String inPath){
        Map<String,Object> map = new HashMap<>();
        logger.info("======uploadPhoto======");
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            String f = format.format(new Date());
            String imageName = ImageUtil.exportImg2(logoUrl,inPath,"D:/work/Admin/Upload/worker_ImgUrl/"+f);
            //封面图片
            map.put("success",true);
            map.put("videoImage",iisPath+"/worker_ImgUrl/"+f+"/"+imageName);
            logger.info("===水印成功==="+imageName);
            return map;
        } catch (Exception e) {
            logger.error("上传水印失败");
            e.printStackTrace();
            map.put("videoImage","");
            map.put("success",false);
            return map;
        }
    }



    @ResponseBody
    @RequestMapping(value = "/delWorkImgUrl",method = RequestMethod.POST)
    public Map<String,Object> delWorkImgUrl(@RequestBody Map<String,Object> map){
        Map<String, Object> objectMap = new HashMap<String, Object>();
        try {
            workerService.delWorkPic(map);
            objectMap.put("success",true);
            objectMap.put("msg","");
        } catch (Exception e) {
            e.printStackTrace();
            objectMap.put("success",false);
            objectMap.put("msg","查询错误，请稍后重试");
        }
        return objectMap;
    }

    @ResponseBody
    @RequestMapping(value = "/addWorkPic",method = RequestMethod.POST)
    public Map<String,Object> addWorkPic(@RequestBody Map<String,Object> map){
        Map<String, Object> objectMap = new HashMap<String, Object>();
        try {
            workerService.addWorkPic(map);
            objectMap.put("success",true);
            objectMap.put("msg","");
        } catch (Exception e) {
            e.printStackTrace();
            objectMap.put("success",false);
            objectMap.put("msg","查询错误，请稍后重试");
        }
        return objectMap;
    }

    @ResponseBody
    @RequestMapping(value = "/updWorkInfo",method = RequestMethod.POST)
    public Map<String,Object> updWorkInfo(@RequestBody Map<String,Object> map){
        Map<String, Object> objectMap = new HashMap<String, Object>();
        try {
            workerService.updateWorkInfo(map);
            objectMap.put("success",true);
            objectMap.put("msg","");
        } catch (Exception e) {
            e.printStackTrace();
            objectMap.put("success",false);
            objectMap.put("msg","查询错误，请稍后重试");
        }
        return objectMap;
    }

    /**
     * 注册工作人员信息
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/registerWorkInfo",method = RequestMethod.POST)
    public Map<String,Object> registerWorkInfo(@RequestBody Map<String,Object> map){
        Map<String, Object> objectMap = new HashMap<>();
        try {
            workerService.addWorkInfo(map);
            objectMap.put("success",true);
            objectMap.put("msg","");
        } catch (Exception e) {
            e.printStackTrace();
            objectMap.put("success",false);
            objectMap.put("msg","查询错误，请稍后重试");
        }
        return objectMap;
    }


    //获取评价内容
    @ResponseBody
    @RequestMapping(value = "/findWorkPic",method = RequestMethod.POST)
    public Map<String,Object> findWorkPic(@RequestBody Map<String,Object> map){
        Map<String, Object> objectMap = new HashMap<String, Object>();
        objectMap.put("success",true);
        objectMap.put("msg","");
        try {
            List<Map<String,Object>>  workPicList = workerService.findWorkPicList(map);
            objectMap.put("workPicList",workPicList);
        } catch (Exception e) {
            e.printStackTrace();
            objectMap.put("success",false);
            objectMap.put("msg","查询错误，请稍后重试");
            objectMap.put("workPicList",null);
        }
        return objectMap;
    }

    @ResponseBody
    @RequestMapping(value = "/updWorkTime",method = RequestMethod.POST)
    public Map<String,Object> updWorkTime(@RequestBody Map<String,Object> map){
        Map<String, Object> objectMap = new HashMap<String, Object>();
        objectMap.put("success",true);
        objectMap.put("msg","");
        try {
            int  num = workerService.updateWorkBusy(map);
            objectMap.put("num",num);
        } catch (Exception e) {
            e.printStackTrace();
            objectMap.put("success",false);
            objectMap.put("msg","修改错误");
            objectMap.put("num",0);
        }
        return objectMap;
    }


    //修改员工图片
    @ResponseBody
    @RequestMapping(value = "/updWorkPicType",method = RequestMethod.POST)
    public Map<String,Object> updWorkPicType(@RequestBody Map<String,Object> map){
        Map<String, Object> objectMap = new HashMap<String, Object>();
        objectMap.put("success",true);
        objectMap.put("msg","");
        try {
            int  num = workerService.updWorkPic(map);
            objectMap.put("num",num);
        } catch (Exception e) {
            e.printStackTrace();
            objectMap.put("success",false);
            objectMap.put("msg","修改错误");
            objectMap.put("num",0);
        }
        return objectMap;
    }

    //修改员工图片
    @ResponseBody
    @RequestMapping(value = "/addWorkPicType",method = RequestMethod.POST)
    public Map<String,Object> addWorkPicType(@RequestBody Map<String,Object> map){
        Map<String, Object> objectMap = new HashMap<String, Object>();
        objectMap.put("success",true);
        objectMap.put("msg","");
        try {
            int  num = workerService.addWorkConPic(map);
            objectMap.put("num",num);
        } catch (Exception e) {
            e.printStackTrace();
            objectMap.put("success",false);
            objectMap.put("msg","修改错误");
            objectMap.put("num",0);
        }
        return objectMap;
    }

    //修改员工图片
    @ResponseBody
    @RequestMapping(value = "/findWorkPicType",method = RequestMethod.POST)
    public Map<String,Object> findWorkPicType(@RequestBody Map<String,Object> map){
        Map<String, Object> objectMap = new HashMap<String, Object>();
        objectMap.put("success",true);
        objectMap.put("msg","");
        try {
            List<Map<String,Object>> picTypeList = workerService.findWorkTypePicList(map);
            objectMap.put("picTypeList",picTypeList);
        } catch (Exception e) {
            e.printStackTrace();
            objectMap.put("success",false);
            objectMap.put("msg","查询错误");
            objectMap.put("picTypeList",null);
        }
        return objectMap;
    }


    //绑定技师和用户
    @ResponseBody
    @RequestMapping(value = "/bindCellPhone",method = RequestMethod.POST)
    public Map<String,Object> bindCellPhone(@RequestBody Map<String,String> map){
        Map<String, Object> objectMap = new HashMap<String, Object>();
        objectMap.put("success",true);
        objectMap.put("msg","");
        try {
            VoiceResponseResult v = workerService.httpsPrivacyBindAxb(map);
            objectMap.put("vs",v);
        } catch (Exception e) {
            e.printStackTrace();
            objectMap.put("success",false);
            objectMap.put("msg","查询错误");
            objectMap.put("picTypeList",null);
        }
        return objectMap;
    }


    //解绑技师和用户
    @ResponseBody
    @RequestMapping(value = "/unbindCellPhone",method = RequestMethod.POST)
    public Map<String,Object> unbindCellPhone(@RequestBody Map<String,String> map){
        Map<String, Object> objectMap = new HashMap<String, Object>();
        objectMap.put("success",true);
        objectMap.put("msg","");
        try {
            VoiceResponseResult v = workerService.httpsPrivacyUnbind(map);

            objectMap.put("vs",v);
        } catch (Exception e) {
            e.printStackTrace();
            objectMap.put("success",false);
            objectMap.put("msg","查询错误");
            objectMap.put("picTypeList",null);
        }
        return objectMap;
    }

    @ResponseBody
    @PostMapping("/updateWorkByWorkId")
    public Map<String,Object> updateWorkByWorkId(@RequestBody Map<String,Object> objectMap){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success",true);
        map.put("msg","");
        try {
            String msg = workerService.updateLoginImg(objectMap);
            map.put("url",msg);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success",false);
            map.put("msg","查询错误，请稍后重试");
            map.put("count",0);
        }
        return map;
    }

    @ResponseBody
    @RequestMapping("/uploadVideoImageInfo")
    public Map<String,Object> uploadVideoImageInfo(@RequestBody Map<String,Object> objectMap){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success",true);
        map.put("msg","");
        try {
           objectMap.put("logo",logoUrl);
           objectMap.put("iisPath",iisPath);
           map = workerService.uploadWorkerVideo(objectMap);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success",false);
            map.put("msg","查询错误，请稍后重试");
        }
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/findEvalListInfoWorkInfo",method = RequestMethod.POST)
    public Map<String,Object> findEvalListInfoWorkInfo(@RequestBody Map<String,Object> map){
        Map<String,Object> result_map = new HashMap<String,Object>();
        try {
            List<Map<String,Object>> resultList = workerService.findEvaluateByWorkId(map);
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
    @RequestMapping(value = "/findEvalListInfoWorkOrderNum",method = RequestMethod.POST)
    public Map<String,Object> findEvalListInfoWorkOrderNum(@RequestBody Map<String,Object> map){
        Map<String,Object> result_map = new HashMap<String,Object>();
        try {
            Map<String,Object> resultMap = workerService.findWorkOrderNum(map);
            if (resultMap != null) {
                result_map.put("success", true);
                result_map.put("resultMap",resultMap);
            } else {
                result_map.put("success", false);
                result_map.put("resultMap", null);
            }
        }catch (Exception e){
            result_map.put("success", false);
            result_map.put("resultMap", null);
        }
        return result_map;
    }


    @ResponseBody
    @RequestMapping(value = "/findOrderNumInfo",method = RequestMethod.POST)
    public Map<String,Object> findOrderNumInfo(@RequestBody Map<String,Object> map){
        Map<String,Object> result_map = new HashMap<String,Object>();
        try {
            Map<String,Object> resultMap = workerService.findWorkOrderNumInfo(map);
            if (resultMap != null) {
                result_map.put("success", true);
                result_map.put("resultMap",resultMap);
            } else {
                result_map.put("success", false);
                result_map.put("resultMap", null);
            }
        }catch (Exception e){
            result_map.put("success", false);
            result_map.put("resultMap", null);
        }
        return result_map;
    }


    @ResponseBody
    @RequestMapping(value = "/findUpdateOrder",method = RequestMethod.POST)
    public Map<String,Object> findUpdateOrder(@RequestBody Map<String,Object> map){
        Map<String,Object> result_map = new HashMap<String,Object>();
        try {
            workerService.findUpdateOrder(map);
            result_map.put("success", true);
            result_map.put("msg","");
        }catch (Exception e){
            result_map.put("success", false);
            result_map.put("msg", null);
        }
        return result_map;
    }

    @ResponseBody
    @RequestMapping(value = "/findShopCode",method = RequestMethod.POST)
    public Map<String,Object> findShopCode(@RequestBody Map<String,Object> map){
        Map<String,Object> result_map = new HashMap<>();
        try {
            result_map = workerService.findShopByCode(map);

        }catch (Exception e){
            result_map.put("success", false);
            result_map.put("msg", null);
        }
        return result_map;
    }

    @ResponseBody
    @RequestMapping(value = "/updateWorkCode",method = RequestMethod.POST)
    public Map<String,Object> updateWorkCode(@RequestBody Map<String,Object> map){
        Map<String,Object> result_map = new HashMap<String,Object>();
        try {
            result_map = workerService.updShopByCode(map);
        }catch (Exception e){
            result_map.put("success", false);
            result_map.put("msg", "服务错误");
        }
        return result_map;
    }


    @ResponseBody
    @RequestMapping(value = "/findWorkInfo",method = RequestMethod.POST)
    public Map<String,Object> findWorkInfo(@RequestBody Map<String,Object> map){
        Map<String,Object> result_map = new HashMap<String,Object>();
        try {
            result_map = workerService.findWorkInfo(map);
            result_map.put("success", true);
            result_map.put("msg", "");

        }catch (Exception e){
            result_map.put("success", false);
            result_map.put("msg", "服务错误");
        }
        return result_map;
    }


    @ResponseBody
    @PostMapping("/getWorkerInfo_1")
    public Map<String,Object> getWorkerInfo_1(@RequestBody Map<String,Object> map_1){

        Map<String, Object> map = new HashMap<>();
        map.put("success",true);
        map.put("msg","");
        try {
            String shopId = null;
            if(map_1.get("shopId") != null){
                shopId = ToolsUtil.getString(map_1.get("shopId"));
            }
            String city = null;
            if(map_1.get("city") != null){
                city = ToolsUtil.getString(map_1.get("city"));
            }
            List<Map<String,Object>>  workerList = workerService.workerInfoListByInfo(shopId,city);
            map.put("workerList",workerList);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success",false);
            map.put("msg","查询错误，请稍后重试");
        }
        return map;
    }

    @ResponseBody
    @PostMapping("/queryWorkDis")
    public Map<String,Object> queryWorkDis(@RequestBody Map<String,Object> map_1){

        logger.info("请求信息为：{}",JSON.toJSONString(map_1));
       return workerService.queryWorkerInfo(map_1);
    }

    /**
     * 根据两个经纬度获取相距几公里
     * @param map_1
     * @return
     */
    @ResponseBody
    @PostMapping("/queryWorkDisInfo")
    public Map<String,Object> queryWorkDisInfo(@RequestBody Map<String,Object> map_1){

        logger.info("请求信息为：{}",JSON.toJSONString(map_1));
        String start = ToolsUtil.getString(map_1.get("start"));
        String end = ToolsUtil.getString(map_1.get("end"));
        Map<String,Object> map = new HashMap<>();
        try {
            Integer dis = workerService.getDistances(start,end);
            map.put("success",true);
            map.put("msg","");
            map.put("distinct",dis);
        }catch (Exception e){
            logger.error("获取经纬度之间的距离失败：{}",e);
            map.put("success",false);
            map.put("msg","获取位置失败");
            map.put("distinct",0);
        }
        return map;
    }

    @ResponseBody
    @PostMapping(value = "/findWorkByPhoneWx")
    public Map<String,Object> findWorkByPhoneWx(@RequestBody Map<String,Object> map_1){
        Map<String,Object> map = new HashMap<>();
        try{
            logger.info("findWorkByPhoneWx===:请求参数为：{}",JSON.toJSONString(map_1));
            String phone = String.valueOf(map_1.get("phone"));
            String sendCode = String.valueOf(map_1.get("sendCode"));
            String openId = String.valueOf(map_1.get("openId"));
            map = workerService.findWorkerInfoWx(phone,sendCode,openId);
        }catch (Exception e){
            map.put("msg","系统出现异常，请稍后重试");
            map.put("success","false");
            map.put("workVo",null);
        }
        return map;
    }

    @ResponseBody
    @PostMapping(value = "/updWorkerInfo")
    public Map<String,Object> updWorkerInfo(@RequestBody Map<String,Object> stringObjectMap){
        Map<String,Object> map = new HashMap<>();
        try{
            workerService.updWorkerInfoJW(stringObjectMap);
            map.put("success",true);
            map.put("msg","");
        }catch (Exception e){
            map.put("msg","系统出现异常，请稍后重试");
            map.put("success","false");
            map.put("workVo",null);
        }
        return map;
    }

    @ResponseBody
    @GetMapping(value = "/findWorkLocation")
    public Map<String,Object> findWorkLocation(String workerId){
        Map<String,Object> map = new HashMap<>();
        try{
            WorkerVo workerVo = workerService.findWorkLocation(workerId);
            map.put("success",true);
            map.put("msg","");
            map.put("workerVo",workerVo);
        }catch (Exception e){
            map.put("msg","系统出现异常，请稍后重试");
            map.put("success","false");
            map.put("workVo",null);
        }
        return map;
    }

    @ResponseBody
    @GetMapping(value = "/addWorkWxInfo")
    public Map<String,Object> addWorkWxInfo(String workerId){
        Map<String,Object> map = new HashMap<>();
        try{
            WorkerVo workerVo = workerService.findWorkLocation(workerId);
            map.put("success",true);
            map.put("msg","");
            map.put("workerVo",workerVo);
        }catch (Exception e){
            map.put("msg","系统出现异常，请稍后重试");
            map.put("success","false");
            map.put("workVo",null);
        }
        return map;
    }

    @ResponseBody
    @GetMapping(value = "/updWorkWxInfo")
    public Map<String,Object> updWorkWxInfo(String workerId){
        Map<String,Object> map = new HashMap<>();
        try{
            WorkerVo workerVo = workerService.findWorkLocation(workerId);
            map.put("success",true);
            map.put("msg","");
            map.put("workerVo",workerVo);
        }catch (Exception e){
            map.put("msg","系统出现异常，请稍后重试");
            map.put("success","false");
            map.put("workVo",null);
        }
        return map;
    }

    @ResponseBody
    @GetMapping(value = "/delWorkWxInfo")
    public Map<String,Object> delWorkWxInfo(String workerId){
        Map<String,Object> map = new HashMap<>();
        try{
            WorkerVo workerVo = workerService.findWorkLocation(workerId);
            map.put("success",true);
            map.put("msg","");
            map.put("workerVo",workerVo);
        }catch (Exception e){
            map.put("msg","系统出现异常，请稍后重试");
            map.put("success","false");
            map.put("workVo",null);
        }
        return map;
    }

    @ResponseBody
    @GetMapping(value = "/findWorkWxInfo")
    public Map<String,Object> findWorkWxInfo(String workerId){
        Map<String,Object> map = new HashMap<>();
        try{
            WorkerVo workerVo = workerService.findWorkLocation(workerId);
            map.put("success",true);
            map.put("msg","");
            map.put("workerVo",workerVo);
        }catch (Exception e){
            map.put("msg","系统出现异常，请稍后重试");
            map.put("success","false");
            map.put("workVo",null);
        }
        return map;
    }

    @ResponseBody
    @PostMapping(value = "/registerWorkerInfo")
    public Map<String,Object> registerWorkerInfo(@RequestBody WorkerVo workerVo){
        Map<String,Object> map = new HashMap<>();
        try{
            logger.info("addWorkerInfo==添加技师信息为：{}",JSON.toJSONString(workerVo));
            map = workerService.registerWorkerInfo(workerVo);
        }catch (Exception e){
            map.put("msg","系统出现异常，请稍后重试");
            map.put("success","false");
        }
        return map;
    }
}

