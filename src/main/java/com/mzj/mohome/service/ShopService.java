package com.mzj.mohome.service;

import com.mzj.mohome.entity.Shop;

import java.util.List;
import java.util.Map;

public interface ShopService {
    List<Shop> findShopList(Map<String, Object> map);

    //查询店铺的工作时间
    List<Map<String,Object>> findShopTime(Map<String, Object> map);

    //根据条件查询商家信息
    List<Shop> findShopInfoByCon(Map<String, Object> map);

    //根据手机号和验证码来查询商家是否存在
    Map<String,Object> findShopBySend(Map<String, Object> map);

    List<Map<String,Object>> findShopTimeByCon(Map<String, Object> map);

    List<Map<String,Object>> findShopTimeConByWorkId(Map<String, Object> map);

    int updateShopBusy(Map<String, Object> map);
    //根据条件查询该店铺下的技师信息
    List<Map<String,Object>> findShopWorkInfoByCons(Map<String, Object> map);


    int updateJishiOnline(Map<String, Object> map);

    int updateWorkerJd(Map<String, Object> map);
}
