package com.mzj.mohome.service;

import com.mzj.mohome.entity.Product;
import com.mzj.mohome.entity.ProductType;

import java.util.List;
import java.util.Map;

/**
 * 项目接口服务
 */
public interface ProductService {
    /**
     * 查询服务类别
     * @return
     */
    public List<ProductType> findProductTypeList(Map<String,Object> map);

    List<ProductType> findProductListByCon(Map<String,Object> map);


    /**
     * 查询服务类别,根据店铺id
     * @return
     */
    List<Map<String,Object>> findProductWorkTypeList(Map<String,Object> map);

    //查询属于某个技师的项目
    List<Map<String,Object>> findProductWorkList(Map<String,Object> map);


    /**
     * 查询产品信息
     * @return
     */
    List<Map<String,Object>> findProductList(Map<String,Object> map);

    Map<String,Object> findProductInfoById(String productId);

    //查询项目图片
    List<Map<String,Object>> findProductList(String productId);

    List<Map<String,Object>> findProductListByWork(Map<String,Object> map);
}
