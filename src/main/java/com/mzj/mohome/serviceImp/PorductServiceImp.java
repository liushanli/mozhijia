package com.mzj.mohome.serviceImp;

import com.mzj.mohome.entity.Product;
import com.mzj.mohome.entity.ProductType;
import com.mzj.mohome.mapper.ProductMapper;
import com.mzj.mohome.service.ProductService;
import com.mzj.mohome.util.ToolsUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.tools.Tool;
import java.util.List;
import java.util.Map;

@Service("productService")
public class PorductServiceImp implements ProductService {

    @Autowired
    private ProductMapper productMapper;
    /**
     * 查询服务类别
     * @return
     */
    public List<ProductType> findProductTypeList(Map<String,Object> map){
        String city = map.get("city").toString();
        List<ProductType> productTypeList = null;
        if(StringUtils.isNotEmpty(city) && ToolsUtil.getCityFlag(city)){
            productTypeList = productMapper.findProductList(city);
        }else if(StringUtils.isNotEmpty(city) && !ToolsUtil.getCityFlag(city)){
            productTypeList = productMapper.findProductList_1(city);
        }

        return productTypeList;
    }

    public List<ProductType> findProductListByCon(Map<String,Object> map){
        String shopId = ToolsUtil.getString(map.get("shopId"));
        List<ProductType> productTypeList = productMapper.findProductListByCon(shopId);
        return productTypeList;
    }

    /**
     * 查询产品信息
     * @return
     */
    public List<Map<String,Object>> findProductList(Map<String,Object> map){
        String page = String.valueOf(map.get("page"));
        String typeId = String.valueOf(map.get("typeId"));
        String id = String.valueOf(map.get("id"));
        String shopId = String.valueOf(map.get("shopId"));
        String city = String.valueOf(map.get("city"));
        if(StringUtils.isEmpty(page)) page = "1";
        if(Integer.parseInt(page)<=0) page = "1";
        if(StringUtils.isEmpty(shopId)) shopId = null;
        if(StringUtils.isEmpty(typeId) || typeId.equals("0")) typeId = null;
        if(StringUtils.isEmpty(id) || id.equals("0")) id = null;
        List<Map<String,Object>> productList = null;
        if(city!=null && ToolsUtil.getCityFlag(city)){
            productList = productMapper.findProductListInfo(city,Integer.parseInt(page),typeId,id,shopId);
        }else if(city!=null && !ToolsUtil.getCityFlag(city)){
            productList = productMapper.findProductListInfo_1(city,Integer.parseInt(page),typeId,id,shopId);
        }

        if(productList!=null && productList.size()>0){
            for(Map<String,Object> product:productList){
                int num = 0;
                if(product.get("sellSum")!=null){
                    num = (int) product.get("sellSum")+(int) product.get("sellNum");
                    product.put("sellNum",num);
                }
                product.put("sellSum",product.get("sellSum")!=null?product.get("sellSum"):0);
                product.put("number",0);
                product.put("showNumber","none");
                product.put("showButtons","");
                product.put("showButtons_1","none");

            }
        }
        return productList;
    }

    public List<Map<String,Object>> findProductListByWork(Map<String,Object> map){


        String workerId = String.valueOf(map.get("workerId"));
        String shopId = String.valueOf(map.get("shopId"));

        List<Map<String,Object>> productList = productMapper.findProductListInfoWorkId(shopId,workerId);
        if(productList!=null && productList.size()>0){
            for(Map<String,Object> product:productList){
                int num = 0;
                if(product.get("sellSum")!=null){
                    num = (int) product.get("sellSum")+(int) product.get("sellNum");
                    product.put("sellNum",num);
                }
                product.put("sellSum",product.get("sellSum")!=null?product.get("sellSum"):0);
                product.put("number",0);
                product.put("showNumber","none");
                product.put("showButtons","");
                product.put("showButtons_1","none");
            }
        }
        return productList;
    }






    public Map<String,Object> findProductInfoById(String productId){
        Map<String,Object> product = productMapper.findProductInfoById(productId);
        int num = 0;
        if(product.get("sellNum")!=null){
            num = (int)product.get("sellNum");
        }
        if(product.get("sellSum")==null){
            product.put("sellSum",num);
        }else{
            int secord_num = (int)product.get("sellSum");
            product.put("sellSum",secord_num+num);
        }
        if(product.get("cardId")==null){
            product.put("cardId","");
        }
        return product;
    }

    //查询项目图片
    public List<Map<String,Object>> findProductList(String productId){
        return productMapper.findProductListInfoById(productId);
    }

    /**
     * 查询服务类别,根据店铺id
     * @return
     */
   public List<Map<String,Object>> findProductWorkTypeList(Map<String,Object> map){
        String shopId = ToolsUtil.getString(map.get("shopId"));
        return productMapper.findProductTypeListInfo(shopId);
    }

    //查询属于某个技师的项目
    public List<Map<String,Object>> findProductWorkList(Map<String,Object> map){
       String workerId = ToolsUtil.getString(map.get("workerId"));
        String shopId = ToolsUtil.getString(map.get("shopId"));
       String productTypeId = ToolsUtil.getString(map.get("productTypeId"));
       String id = ToolsUtil.getString(map.get("id"));
       if(StringUtils.isNotEmpty(productTypeId)){
           if(productTypeId.equals("0")){
               productTypeId = null;
           }

       }
        List<Map<String,Object>> mapList = productMapper.findProductInfoList(workerId,productTypeId,id,shopId);
       for(Map<String,Object> map1 : mapList){
           int num = 0;
           if(map1.get("sellSum")!=""&& map1.get("sellSum")!=null){
               num = num+(int)map1.get("sellSum");
           }
           map1.put("sellNum",(map1.get("sellNum")!=""&& map1.get("sellNum")!=null)?(int)map1.get("sellNum")+num:num);


       }
        return mapList;
    }
}
