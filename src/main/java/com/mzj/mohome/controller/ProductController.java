package com.mzj.mohome.controller;
import com.alibaba.fastjson.JSON;
import com.mzj.mohome.entity.ProductType;
import com.mzj.mohome.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Controller
@RequestMapping("/product")
public class ProductController {

   private Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;


    @ResponseBody
    @PostMapping("/findProductTypeInfo")
    public Map<String,Object> findProductTypeInfo(@RequestBody Map<String,Object> objectMapmap){
        logger.info("findProductTypeInfo===请求参数为：{}", JSON.toJSONString(objectMapmap));
        Map<String,Object> stringObjectMap = new HashMap<>();
        stringObjectMap.put("success",true);
        stringObjectMap.put("msg","");
        try {
            List<ProductType>  productTypeList = productService.findProductTypeList(objectMapmap);
            if(productTypeList.size()>0 && productTypeList != null){
                stringObjectMap.put("productTypeList",productTypeList);
            }else{
                stringObjectMap.put("productTypeList",null);
                stringObjectMap.put("success",false);
            }
        }catch (Exception e){
            logger.error("findProductTypeInfo==== 报错信息=={}",e);
            stringObjectMap.put("success",false);
            stringObjectMap.put("msg","系统异常，请稍后操作");
        }
        return stringObjectMap;
    }


    @ResponseBody
    @PostMapping("/findProductTypeInfoByCon")
    public Map<String,Object> findProductTypeInfoByCon(@RequestBody Map<String,Object> objectMapmap){
        logger.info("findProductTypeInfoByCon===请求参数为：{}", JSON.toJSONString(objectMapmap));
        Map<String,Object> stringObjectMap = new HashMap<>();
        stringObjectMap.put("success",true);
        stringObjectMap.put("msg","");
        try {
            List<ProductType>  productTypeList = productService.findProductListByCon(objectMapmap);
            if(productTypeList.size()>0 && productTypeList != null){
                stringObjectMap.put("productTypeList",productTypeList);
            }else{
                stringObjectMap.put("productTypeList",null);
                stringObjectMap.put("success",false);
            }

        }catch (Exception e){
            logger.error("ProductController中findProductTypeInfo 报错=={}",e);
            stringObjectMap.put("success",false);
            stringObjectMap.put("msg","系统异常，请稍后操作");
            stringObjectMap.put("productTypeList",null);
        }
        return stringObjectMap;
    }



    @ResponseBody
    @PostMapping("/findProductInfo")
    public Map<String,Object> findProductInfo(@RequestBody Map<String,Object> objectMapmap){
        logger.info("findProductInfo===请求参数为：{}", JSON.toJSONString(objectMapmap));
        Map<String,Object> stringObjectMap = new HashMap<>();
        stringObjectMap.put("success",true);
        stringObjectMap.put("msg","");
        try {
            List<Map<String,Object>>  productList = productService.findProductList(objectMapmap);
            stringObjectMap.put("productList",productList);
        }catch (Exception e){
            logger.error("ProductController中findProductInfo 报错=={}",e);
            stringObjectMap.put("success",false);
            stringObjectMap.put("msg","系统异常，请稍后操作");
        }
        return stringObjectMap;
    }


    @ResponseBody
    @PostMapping("/findProductInfoByWorkId")
    public Map<String,Object> findProductInfoByWorkId(@RequestBody Map<String,Object> objectMapmap){
        logger.info("findProductInfoByWorkId===请求参数为：{}", JSON.toJSONString(objectMapmap));
        Map<String,Object> stringObjectMap = new HashMap<>();
        stringObjectMap.put("success",true);
        stringObjectMap.put("msg","");
        try {
            List<Map<String,Object>>  productList = productService.findProductListByWork(objectMapmap);
            stringObjectMap.put("productList",productList);
        }catch (Exception e){
            logger.error("ProductController中findProductInfo 报错=={}",e);
            stringObjectMap.put("success",false);
            stringObjectMap.put("msg","系统异常，请稍后操作");
        }
        return stringObjectMap;
    }


    @ResponseBody
    @RequestMapping(value = "/findProductInfoById", method = RequestMethod.POST)
    public Map<String,Object> findProductInfoById(@RequestBody Map<String,Object> objectMapmap){
        logger.info("findProductInfoById===请求参数为：{}", JSON.toJSONString(objectMapmap));
        Map<String,Object> stringObjectMap = new HashMap<>();
        stringObjectMap.put("success",true);
        stringObjectMap.put("msg","");
        try {
            Map<String,Object> product = productService.findProductInfoById(String.valueOf(objectMapmap.get("productId")));
            stringObjectMap.put("product",product);
        }catch (Exception e){
            logger.error("ProductController中findProductInfo 报错=={}",e);
            stringObjectMap.put("success",false);
            stringObjectMap.put("msg","系统异常，请稍后操作");
        }
        return stringObjectMap;
    }

    @ResponseBody
    @RequestMapping(value = "/findProductInfoByIdList", method = RequestMethod.POST)
    public Map<String,Object> findProductInfoByIdList(@RequestBody Map<String,Object> objectMapmap){
        logger.info("findProductInfoByIdList===请求参数为：{}", JSON.toJSONString(objectMapmap));
        Map<String,Object> stringObjectMap = new HashMap<>();
        stringObjectMap.put("success",true);
        stringObjectMap.put("msg","");
        try {
            List<Map<String,Object>> product = productService.findProductList(String.valueOf(objectMapmap.get("productId")));
            stringObjectMap.put("productList",product);
        }catch (Exception e){
            logger.error("ProductController中findProductInfo 报错=={}",e);
            stringObjectMap.put("success",false);
            stringObjectMap.put("msg","系统异常，请稍后操作");
        }
        return stringObjectMap;
    }


    @ResponseBody
    @RequestMapping(value = "/findProductTypeShop", method = RequestMethod.POST)
    public Map<String,Object> findProductTypeShop(@RequestBody Map<String,Object> objectMapmap){
        logger.info("findProductTypeShop===请求参数为：{}", JSON.toJSONString(objectMapmap));
        Map<String,Object> stringObjectMap = new HashMap<>();
        stringObjectMap.put("success",true);
        stringObjectMap.put("msg","");
        try {
            List<Map<String,Object>> productTypeList = productService.findProductWorkTypeList(objectMapmap);
            stringObjectMap.put("productTypeList",productTypeList);
        }catch (Exception e){
            logger.error("ProductController中findProductInfo 报错=={}",e);
            stringObjectMap.put("success",false);
            stringObjectMap.put("msg","系统异常，请稍后操作");
            stringObjectMap.put("productTypeList",null);
        }
        return stringObjectMap;
    }

    @ResponseBody
    @RequestMapping(value = "/findProductWorkTypeInfo", method = RequestMethod.POST)
    public Map<String,Object> findProductWorkTypeInfo(@RequestBody Map<String,Object> objectMapmap){
        logger.info("findProductWorkTypeInfo===请求参数为：{}", JSON.toJSONString(objectMapmap));
        Map<String,Object> stringObjectMap = new HashMap<>();
        stringObjectMap.put("success",true);
        stringObjectMap.put("msg","");
        try {
            List<Map<String,Object>> productList = productService.findProductWorkList(objectMapmap);
            stringObjectMap.put("productList",productList);
        }catch (Exception e){
            logger.error("findProductWorkTypeInfo==== 报错信息=={}",e);
            stringObjectMap.put("success",false);
            stringObjectMap.put("msg","数据异常");
            stringObjectMap.put("productList",null);
        }
        return stringObjectMap;
    }


}
