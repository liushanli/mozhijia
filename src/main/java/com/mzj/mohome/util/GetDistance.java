package com.mzj.mohome.util;
import com.mzj.mohome.vo.MapDto;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
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
    public static Long getDistance(String start,String end){
        try {
            String jsonStr_1 = httpGet("https://api.map.baidu.com/directionlite/v1/driving?origin="+start+"&destination="+end+"&ak=Dw4VqR2Z5ygmDxfEVlaz0j2cI3wx9DGn");
            net.sf.json.JSONObject jsonObject1 = net.sf.json.JSONObject.fromObject(jsonStr_1);
            String result1 = jsonObject1.get("result").toString();
            net.sf.json.JSONObject jsonObjectResult1 = net.sf.json.JSONObject.fromObject(result1);
            List<String> routes = (List<String>)jsonObjectResult1.get("routes");

            net.sf.json.JSONObject location2 = net.sf.json.JSONObject.fromObject(routes.get(0));
            return Long.parseLong(location2.get("distance").toString());
        }catch (Exception e){
            log.error("获取地图距离错误为：{}",e);
            return 0l;
        }
    }

    /*public static void main(String str[]){
       System.out.println(getJW("上海市"));
        System.out.println(getJW("上海市浦东新区万德路"));
        //https://api.map.baidu.com/directionlite/v1/driving?origin=40.01116,116.339303&destination=39.936404,116.452562&ak=Dw4VqR2Z5ygmDxfEVlaz0j2cI3wx9DGn
        //System.out.println(getDistince("40.01116,116.339303","31.244907000000001,121.468913000000001"));
    }*/
}
