package com.mzj.mohome.util;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mzj.mohome.vo.MapDto;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import static com.mzj.mohome.util.HttpsService.httpGet;

/**
 * 1.这个需要客户申请个高德API上面的key,也可以自己申请(免费的，很快)
 * */
@Slf4j
public class GetDistance {

    public static MapDto getJW(String address){
        try {
            MapDto mapDto = new MapDto();
            String jsonStr = httpGet("https://api.map.baidu.com/geocoding/v3/?address="+address+"&output=json&ak=Dw4VqR2Z5ygmDxfEVlaz0j2cI3wx9DGn");
            net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(jsonStr);
            String result = jsonObject.get("result").toString();
            net.sf.json.JSONObject jsonObjectResult = net.sf.json.JSONObject.fromObject(result);
            String location = jsonObjectResult.get("location").toString();
            net.sf.json.JSONObject location1 = net.sf.json.JSONObject.fromObject(location);
            String lng = location1.get("lng").toString();
            String lat = location1.get("lat").toString();
            mapDto.setLat(lat);
            mapDto.setLng(lng);
            return mapDto;
        }catch (Exception e){
            log.error("获取地图地址错误为：{}",e);
            return null;
        }
    }

    /**
     * 根据两点的经纬度来获取信息
     * start，end由纬度,经度组成
     * @param start
     * @param end
     * @return
     */
   /* public static Long getDistance(String start,String end){
        try {
            String url = "https://api.map.baidu.com/directionlite/v1/driving";
            Map<String,String> map_1 = new HashMap<>();
            map_1.put("origin",start);
            map_1.put("destination",end);
            map_1.put("steps_info","0");
            map_1.put("ak","zdv9i6SuZtRFCVfXey4u49xXRkBWl5FY");
            JSONObject jsonObject = requestApi.getApi(url,map_1);
            //数据解析
            JSONObject result = jsonObject.getJSONObject("result");
            JSONArray jsonArray = (JSONArray) result.get("routes");
            Integer distance1 = (Integer) ((JSONObject) jsonArray.get(0)).get("distance");//获取举例米

            return Long.parseLong(location2.get("distance").toString());
        }catch (Exception e){
            log.error("获取地图距离错误为：{}",e);
            return 0l;
        }
    }*/

    /*public static void main(String str[]){
       System.out.println(getJW("上海市"));
        System.out.println(getJW("上海市浦东新区万德路"));
        //https://api.map.baidu.com/directionlite/v1/driving?origin=40.01116,116.339303&destination=39.936404,116.452562&ak=Dw4VqR2Z5ygmDxfEVlaz0j2cI3wx9DGn
        //System.out.println(getDistince("40.01116,116.339303","31.244907000000001,121.468913000000001"));
    }*/
}
