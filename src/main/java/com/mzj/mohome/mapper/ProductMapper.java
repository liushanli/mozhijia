package com.mzj.mohome.mapper;

import com.mzj.mohome.entity.Product;
import com.mzj.mohome.entity.ProductType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 项目列表
 */
@Mapper
public interface ProductMapper {


    //项目类别列表
    @Select("select p.* from TB_ProductType p join TB_ShopPoint sp on p.shopId = sp.shopId" +
            " join TB_Shop shop on sp.shopId = shop.shopId " +
            " where sp.city = #{city} and shop.isOnline = 1 ")
    List<ProductType> findProductList(String city);

    //项目类别列表
    @Select("select p.* from TB_ProductType p join TB_ShopPoint sp on p.shopId = sp.shopId" +
            " join TB_Shop shop on sp.shopId = shop.shopId " +
            " where sp.city = (select top 1 p.name from TB_ProvinceCityArea p join TB_ProvinceCityArea p1 on p.id = p1.pid where p1.name = #{city} order by p.id) and shop.isOnline = 1 ")
    List<ProductType> findProductList_1(String city);

    //项目类别列表
    @Select("select * from TB_ProductType where shopId = #{shopId}")
    List<ProductType> findProductListByCon(String shopId);


    /**
     * 查询产品信息
     * @return
     */

    @Select("<script> select t.*,ISNULL(tp.couponType, 0) couponType,tp.secondPrice,shop.shopName from (select tp.*,ISNULL(t1.sellNum, 0) sellNum from tb_product tp " +
            "left join (select COUNT(1) sellNum,t.productId from TB_Order t left join TB_Product p on t.productId =  p.productId " +
            " where t.status = 1 or (t.status &gt;= 3 and t.status &lt;= 9) group by t.productId) t1 " +
            " on tp.productId =  t1.productId  where tp.isOnline = 1) t " +
            " left join (select * from TB_Coupon where GETDATE() BETWEEN startTime and endTime and status = 1 and couponType = 1) " +
            " tp on t.productId = tp.productId " +
            " JOIN TB_ShopPoint tbp on t.shopId = tbp.shopId " +
            " join tb_shop shop on tbp.shopId = shop.shopId " +
            "where shop.isOnline = 1 and  tbp.city =  #{city} " +
            "<if test='typeId != null'> and t.productTypeId = #{typeId} </if>"+
            /*" <if test='productTypeId!=null'> and t.productTypeId = #{productTypeId} </if>" +*/
            " <if test='id !=null '> and t.id = #{id}</if> "+
            " <if test='shopId !=null '> and t.shopId = #{shopId}</if> "+
            " order by t.orderNum,isRecom  </script>")
    List<Map<String,Object>> findProductListInfo(@Param("city") String city,@Param("page") Integer page,
                                                 @Param("typeId") String typeId,
                                                 @Param("id") String id, @Param("shopId") String shopId);


    /**
     * 查询产品信息
     * @return
     */

    @Select("<script> select t.*,ISNULL(tp.couponType, 0) couponType,tp.secondPrice,shop.shopName from (select tp.*,ISNULL(t1.sellNum, 0) sellNum from tb_product tp " +
            "left join (select COUNT(1) sellNum,t.productId from TB_Order t left join TB_Product p on t.productId =  p.productId " +
            " where t.status = 1 or (t.status &gt;= 3 and t.status &lt;= 9) group by t.productId) t1 " +
            " on tp.productId =  t1.productId  where tp.isOnline = 1) t " +
            " left join (select * from TB_Coupon where GETDATE() BETWEEN startTime and endTime and status = 1 and couponType = 1) " +
            " tp on t.productId = tp.productId " +
            " JOIN TB_ShopPoint tbp on t.shopId = tbp.shopId " +
            " join tb_shop shop on tbp.shopId = shop.shopId " +
            "where shop.isOnline = 1 and  tbp.city = (select top 1 p.name from TB_ProvinceCityArea p join TB_ProvinceCityArea p1 on p.id = p1.pid where p1.name = #{city} order by p.id) " +
            " <if test='productTypeId!=null'> and t.productTypeId = #{productTypeId} </if>" +
            " <if test='id !=null '> and t.id = #{id}</if> "+
            " <if test='shopId !=null '> and t.shopId = #{shopId}</if> "+
            " order by t.orderNum,isRecom  </script>")
    List<Map<String,Object>> findProductListInfo_1(@Param("city") String city,@Param("page") Integer page,
                                                 @Param("productTypeId") String productTypeId,
                                                 @Param("id") String id, @Param("shopId") String shopId);



    /**
     * 查询产品信息根据技师id和店铺id
     * @return
     */

    @Select("<script> select t.*,ISNULL(tp.couponType, 0) couponType,tp.secondPrice from (select tp.*,ISNULL(t1.sellNum, 0) sellNum from tb_product tp \n" +
    " left join (select COUNT(1) sellNum,t.productId from TB_Order t left join TB_Product p on t.productId =  p.productId \n" +
    " where t.status = 1 or (t.status &gt;= 3 and t.status &lt;= 9) group by t.productId) t1 \n" +
        " on tp.productId =  t1.productId where tp.isOnline = 1 ) t\n" +
    " left join (select * from TB_Coupon where GETDATE() BETWEEN startTime and endTime and status = 1 and couponType = 1) \n" +
    " tp on t.productId = tp.productId \n" +
    " left join TB_WorkerSerProduct tpl on t.productId = tpl.productId \n" +
    " where 1=1" +
            "<if test='shopId!=null'> and t.shopId = #{shopId} </if> " +
            "<if test='workerId!=null'> and tpl.workerId = #{workerId} </if> " +
    " order by t.orderNum </script>")
    List<Map<String,Object>> findProductListInfoWorkId(@Param("shopId") String shopId,@Param("workerId") String workerId);


    @Select("select s.shopName,p.price, p.id,p.shopId,p.productId,p.imgUrl,p.productName,p.detail,p.productTime,p.isRecom,p.orderNum,p.contentText,p.oldPrice,p.memberPrice,p.groupPrice,p.serviceNumber,p.serviceArea,p.personnel,p.addTime,p.isOnline,s.orderInfo,ISNULL(tp.couponType, 0) couponType,tp.secondPrice,t.sellSum,p.sellSum sellNum from TB_Product p join TB_Shop s on p.shopId = s.shopId left join (select * from TB_Coupon where GETDATE() BETWEEN startTime and endTime and status = 1 and couponType = 1) tp on p.productId = tp.productId\n" +
            "  left join (select COUNT(1) sellSum,t.productId from TB_Order t left join TB_Product p on t.productId =  p.productId \n" +
            "  where t.status = 1 or (t.status >= 3 and t.status <= 9)   group by t.productId) t\n" +
            "  on p.productId = t.productId " +
            " where  p.productId = #{productId}")
    Map<String,Object> findProductInfoById(String productId);


    @Select("select * from TB_Product where productId = #{productId}")
    Map<String,Object> findProductInfoByIdInfo(String productId);

    @Select("select * from TB_ProductPic where productId = #{productId} order by orderNum")
    List<Map<String,Object>> findProductListInfoById(String productId);

    @Select("select * from TB_ProductType where shopId = #{shopId}  order by id ")
    List<Map<String,Object>> findProductTypeListInfo(String shopId);

    @Select("<script> select p.*," +
            "(select COUNT(1) sellNum from TB_Order t left join TB_Product p on t.productId =  p.productId \n" +
            " where (t.status = 1 or (t.status &gt;= 3 and t.status &gt;= 9)) and t.productId =w.productId  group by t.productId) sellNum " +
            " from TB_Product p join TB_WorkerSerProduct w on p.productId = w.productId\n" +
            " join TB_Worker worker on w.workerId = worker.workerId \n" +
            " where p.isOnline = 1 " +
            "<if test='workerId!=null'> and w.workerId = #{workerId} </if> " +
            "<if test='shopId!=null'> and worker.shopId = #{shopId} </if> " +
            "<if test='id != null'> and p.productId =#{id} </if> \n" +
            "<if test='productTypeId != null'> and p.productTypeId =#{productTypeId} </if> \n" +
            "order by p.orderNum,isRecom </script>")
    List<Map<String,Object>> findProductInfoList(@Param("workerId") String workerId, @Param("productTypeId")String productTypeId,
                                                 @Param("id") String id, @Param("shopId")String shopId);


}
