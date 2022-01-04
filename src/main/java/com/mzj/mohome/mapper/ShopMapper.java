package com.mzj.mohome.mapper;

import com.mzj.mohome.entity.Shop;
import com.mzj.mohome.entity.Worker;
import org.apache.ibatis.annotations.*;
import org.apache.logging.log4j.core.config.plugins.PluginAliases;

import java.util.List;
import java.util.Map;

@Mapper
public interface ShopMapper {

    //查询商家信息
    @Select(" select t.id,t.shopId,sp.shopName,t.orderInfo,t.shopIntroduce,t.shopPhone,t.shopEmail,t.orderNum,t.servicePhone,\n" +
            " t.excutePhone,t.StopStartTime,t.StopEndTime,t.serviceArea,t.trafficPice,t.trafficStartTime,t.trafficEndTime,\n" +
            " t.receiveType,t.arriveTimeFloat,t.serviceLogoUrl,t.shopLogoUrl,t.welcome,t.orderPhone,t.lowerPrice,t.jd,t.wd,\n" +
            " t.radius,t.earlyTime,t.returnInfo,t.addTime,t.updateTime,t.shopCode,t.onLine,t.shopStartTime,t.shopEndTime,\n" +
            " t.isOnline from TB_Shop t INNER JOIN TB_ShopPoint sp ON t.shopId = sp.shopId\n" +
            " where sp.city =  #{city} and t.isOnline = 1 ")
    List<Shop> findShopList(String city);

    //查询商家信息
    @Select(" select t.id,t.shopId,sp.shopName,t.orderInfo,t.shopIntroduce,t.shopPhone,t.shopEmail,t.orderNum,t.servicePhone,\n" +
            " t.excutePhone,t.StopStartTime,t.StopEndTime,t.serviceArea,t.trafficPice,t.trafficStartTime,t.trafficEndTime,\n" +
            " t.receiveType,t.arriveTimeFloat,t.serviceLogoUrl,t.shopLogoUrl,t.welcome,t.orderPhone,t.lowerPrice,t.jd,t.wd,\n" +
            " t.radius,t.earlyTime,t.returnInfo,t.addTime,t.updateTime,t.shopCode,t.onLine,t.shopStartTime,t.shopEndTime,\n" +
            " t.isOnline from TB_Shop t INNER JOIN TB_ShopPoint sp ON t.shopId = sp.shopId\n" +
            " where sp.city = (select top 1 p.name from TB_ProvinceCityArea p join TB_ProvinceCityArea p1 on p.id = p1.pid where p1.name = #{city} order by p.id) and t.isOnline = 1 ")
    List<Shop> findShopList_1(String city);

    //查询店铺工作的时间
    @Select("<script> select w.* from TB_ShopTime w join TB_Worker t on w.shopId = t.shopId " +
            " where  1=1 <if test='shopId!=null'> and w.shopId=#{shopId} </if> " +
            " <if test='workerId!=null'> and t.workerId = #{workerId} </if> " +
            " and w.date &gt;= #{startTime} and w.date &lt; #{endTime}  " +
            "  order by w.[date] </script>")
    List<Map<String,Object>> findShopTime(@Param("startTime") String startTime, @Param("endTime")String endTime,
                                          @Param("shopId") String shopId, @Param("workerId")String workerId);

    @Select("<script> select w.* from TB_ShopTime w " +
            " where  1=1 <if test='shopId!=null'> and w.shopId=#{shopId} </if> " +
            " and w.date &gt;= #{startTime} and w.date &lt; #{endTime}  " +
            "  order by w.[date] </script>")
    List<Map<String,Object>> findShopTimeByShopId(@Param("startTime") String startTime, @Param("endTime")String endTime, @Param("shopId")String shopId);




    //查询条件查询商家信息
    @Select(" <script> select t.id,t.shopId,sp.shopName,t.orderInfo,t.shopIntroduce,t.shopPhone,t.shopEmail,t.orderNum,t.servicePhone,\n" +
            " t.excutePhone,t.StopStartTime,t.StopEndTime,t.serviceArea,t.trafficPice,t.trafficStartTime,t.trafficEndTime,\n" +
            " t.receiveType,t.arriveTimeFloat,t.serviceLogoUrl,t.shopLogoUrl,t.welcome,t.orderPhone,t.lowerPrice,t.jd,t.wd,\n" +
            " t.radius,t.earlyTime,t.returnInfo,t.addTime,t.updateTime,t.shopCode,t.onLine,t.shopStartTime,t.shopEndTime,\n" +
            " t.isOnline from TB_Shop t INNER JOIN TB_ShopPoint sp ON t.shopId = sp.shopId\n" +
            " where 1=1 <if test='shopId!=null'> and t.shopId=#{shopId}  </if> and t.isOnline = 1 " +
            "</script>")
    List<Shop> findShopInfoByCon(String shopId);

    //根据手机号和验证码查询是否存在
    //@Select("select  top 1 * from TB_SmsSend where phone = #{phone} and sendCode = #{sendCode} and  addTime<=GETDATE() and GETDATE()<=DATEADD(mi,5,addTime)  order by addTime desc")
    @Select("select  top 1 * from TB_SmsSend where phone = #{phoneDesc} and sendCode = #{sendCodeDesc} order by addTime desc")
    Map<String,Object> findShopBySend(@Param("phoneDesc") String phoneDesc, @Param("sendCodeDesc") String sendCodeDesc);

    @Select("select top 1 t.*,ts.city from TB_Shop t join TB_ShopPoint ts on  t.shopId = ts.shopId where t.shopPhone = #{phoneDesc} and t.isOnline = 1")
    Map<String,Object> findShopByPhone(@Param("phoneDesc") String phoneDesc);

    //查询店员的工作时间
    @Select("select * from TB_ShopTime  w where w.date >= #{startTime} and w.date < #{endTime} and shopId=#{shopId} order by [date]")
    List<Map<String,Object>> findShopTimeCon(@Param("startTime") String startTime, @Param("endTime")String endTime, @Param("shopId")String shopId);


    //根据技师的id/时间来修改是否繁忙
    @Update("<script> update TB_ShopTime set isBusy = #{isBusy} where " +
            "<if test='dateStr!=null'> dateStr = #{dateStr} and shopId = #{shopId} </if>" +
            "<if test='id!=null'> id = #{id} </if> </script>")
    int updateShopBusy(@Param("isBusy") Integer isBusy, @Param("dateStr")String dateStr,
                       @Param("id")String id, @Param("shopId")String shopId);




    //查询店员属于店铺
    @Select("select s.trafficPice,s.trafficStartTime,s.trafficEndTime from TB_Shop s join TB_Worker w on s.shopId = w.shopId and w.workerId = #{workerId}")
    List<Map<String,Object>> findShopTimeConByWorkId(String workerId);

    @Update("update TB_WorkerPoint set jd = #{jd},wd = #{wd}  where workerId = #{workerId}")
    int updWorkerPoint(@Param("jd") String jd,@Param("wd")String wd,@Param("workerId")String workerId);




    //根绝店铺来查询员工的信息
    @Select("<script>select top 10 * from (" +
            " select " +
            " row_number() over(order by t.orderNum desc) as rownumber,\n" +
            " t.userName,t.imgUrl,t.phone,t.introduce,t.serviceArea,t.workerTitle,t.isOnline,CONVERT(varchar(100)," +
            " loginTime, 120) loginTime,t.orderNum,t.workerId,shop.shopName,t.jd,t.wd," +
            "             t.gender from TB_Worker t INNER JOIN TB_Shop shop on t.shopId = shop.shopId " +
            ") t where rownumber &gt; ${page} </script>")
    List<Map<String,Object>> findWorkerListByShopId(@Param("city") String city,@Param("shopId")String shopId,
                                                    @Param("onLine")String onLine,@Param("page")Integer page);


    @Update("update TB_Worker set isOnline = #{isOnline}  where workerId = #{workerId}")
    int updateJishiOnline(@Param("workerId") String workerId,@Param("isOnline")String isOnline);


    @Insert("insert into TB_Worker(workerId,userName,phone,isOnline,loginTime,sellSum)\n" +
            "values(REPLACE(NEWID(),'-',''),#{phoneDesc},#{phoneDesc},0,GETDATE(),15)")
    int addWorkInfo(String phoneDesc);

}
