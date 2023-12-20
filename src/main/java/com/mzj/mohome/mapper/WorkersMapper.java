package com.mzj.mohome.mapper;

import com.mzj.mohome.entity.Worker;
import com.mzj.mohome.entity.WorkerPic;
import com.mzj.mohome.vo.WorkerVo;
import com.mzj.mohome.vo.WorkerWxInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface WorkersMapper {

    @Select("select * from tb_worker where workerId = #{workerId} and is_del = 1")
    Map<String,Object> findWorkerById(String workerId);


    //根绝店铺来查询员工的信息
    @Select("<script>" +
            " select t.id,t.shopId,t.workerId,t.userName,t.imgUrl,t.phone,t.introduce,t.serviceArea,t.workerTitle,t.isOnline,t.orderNum," +
            " t.nickName,t.gender,t.sellSum,t.addTime,t.updateTime,t.idcard,t.video,t.loginTime,t.videoImage,t.shopCodeStatus,tp.shopName," +
            " tp.city," +
            " (select top 1 (dateStr+' '+dateHHmm) dateHHmm from TB_WorkerTime where isBusy = 0 and [date]>DATEADD(Minute,30, GETDATE()) and workerId =  t.workerId)  dateHHmm," +
            " ISNULL(t1.sellNum,0) sellNum,twp.jd,twp.wd,twp.radius,twp.provinceId,twp.province,twp.cityId,twp.city,twp.areaId,twp.area," +
            "  (select count(1) from TB_Evaluate e inner join TB_Order o on e.orderId = o.orderId where o.workerId =  t.workerId) evaluateNum " +
            " from TB_Worker t INNER JOIN TB_WorkerPoint twp on t.workerId = twp.workerId INNER JOIN" +
            " TB_Shop shop on t.shopId = shop.shopId" +
            " INNER JOIN TB_ShopPoint tp ON t.shopId = tp.shopId " +
            " left join (select COUNT(1) sellNum,t.workerId from TB_Order t left join TB_Product p on t.productId =  p.productId" +
            " where  t.status = 1 or (t.status &gt;= 3 and t.status &lt;= 9) group by t.workerId) t1" +
            " on t.workerId = t1.workerId" +
            " <if test='productId != null'> left JOIN TB_WorkerSerProduct tbP on t.workerId = tbP.workerId </if> " +
            " where 1=1 and t.isOnline = '1' and t.is_del = 1 and shop.isOnline = 1  " +
            " <if test='city != null'> and tp.city =  #{city} </if>" +
            " <if test='productId != null'> and tbP.productId = #{productId} </if>" +
            " <if test='shopId != null'> and t.shopId = #{shopId} </if>" +
            " <if test='onLine != null'> and t.isOnline = #{onLine} </if>" +
            " <if test='shopName != null'> and shop.shopName = #{shopName} </if>" +
            " <if test='workerId!=null'> and t.workerId = #{workerId} </if> " +
            " <if test='userName!=null'> and (t.userName like concat('%',#{userName},'%') or (t.nickName like concat('%',#{userName},'%'))) </if>"+
            "<if test='genderDesc != null '> and t.gender= #{genderDesc} </if>" +

            " <if test='evalNmsDesc == null '> order by orderNum </if>" +
            " <if test='evalNmsDesc != null'>" +
            " <if test='evalNmsDesc == 1 '>order by evaluateNum </if>" +
            " <if test='evalNmsDesc == 2 '>order by evaluateNum desc</if>" +
            " </if>" +
            "  </script>")
    List<Map<String,Object>> findWorkerList(@Param("city") String city, @Param("shopName") String shopName,
                                            @Param("userName") String userName, @Param("workerId") String workerId,
                                            @Param("shopId") String shopId, @Param("pages") Integer pages,
                                            @Param("size") Integer size, @Param("genderDesc") String genderDesc,
                                            @Param("onLine") String onLine, @Param("productId") String productId,
                                            @Param("evalNmsDesc") String evalNmsDesc);


    //根绝店铺来查询员工的信息
    @Select("<script>" +
            "SELECT w2.n," +
            "w1.*," +
            " (select top 1 (dateStr+' '+dateHHmm) dateHHmm from TB_WorkerTime where isBusy = 0 and [date]>DATEADD(Minute,30, GETDATE()) and workerId =  w1.workerId)  dateHHmm "+
            " FROM tb_workerInfo(#{city},#{jd},#{wd}) w1," +
            "(SELECT TOP (${sizeNum}) row_number () OVER ( ORDER BY " +
            " <if test='evalNmsDesc == null '> distance asc,t.ID asc,orderNum asc </if>" +
            " <if test='evalNmsDesc != null'>" +
            " <if test='evalNmsDesc == 1 '> evaluateNum asc</if>" +
            " <if test='evalNmsDesc == 2 '> evaluateNum desc</if>" +
            " </if>"+
            " ) n, t.ID FROM tb_workerInfo(#{city},#{jd},#{wd}) t " +
            " <if test='productId != null'> left JOIN TB_WorkerSerProduct tbP on t.workerId = tbP.workerId </if> " +
            "  where 1=1 " +
            " <if test='shopId != null'> and shopId = #{shopId} </if>" +
            " <if test='productId != null'> and tbP.productId = #{productId} </if>" +
            " <if test='onLine != null'> and isOnline = #{onLine} </if>" +
            " <if test='shopName != null'> and shopName = #{shopName} </if>" +
            " <if test='workerId!=null'> and workerId = #{workerId} </if> " +
            " <if test='userName!=null'> and (userName like concat('%',#{userName},'%') or (nickName like concat('%',#{userName},'%'))) </if>"+
            "<if test='genderDesc != null '> and gender= #{genderDesc} </if>" +
            " ) w2 " +
            "WHERE " +
            " w1.ID = w2.ID AND w2.n  &gt; ${pages} ORDER BY w2.n ASC "+
            "  </script>")
    List<Map<String,Object>> findWorkerList_2(@Param("jd") String jd, @Param("wd") String wd,
                                              @Param("city") String city, @Param("shopName") String shopName,
                                              @Param("userName") String userName, @Param("workerId") String workerId,
                                              @Param("shopId") String shopId, @Param("pages") Integer pages,
                                              @Param("sizeNum") Integer sizeNum, @Param("genderDesc") String genderDesc,
                                              @Param("onLine") String onLine, @Param("productId") String productId,
                                              @Param("evalNmsDesc") String evalNmsDesc);


    @Select("<script> select * from (\n" +
            "select w.*,wp.jd workerJd,wp.wd workerWd, \n" +
            "tp.shopName,\n" +
            "\t(SELECT COUNT (1) sellNum FROM TB_Order t_order \n" +
            "            LEFT JOIN TB_Product p1 ON t_order.productId = p1.productId WHERE t_order.status = 1 OR ( t_order.status &gt;= 3 AND t_order.status &lt;= 9 ) \n" +
            "            and t_order.workerId = w.workerId ) sellNum,\n" +
            "\twp.radius workerRadius,\n" +
            "\twp.provinceId,\n" +
            "\twp.province,\n" +
            "\twp.cityId,\n" +
            "\twp.city,\n" +
            "\twp.areaId,\n" +
            "\twp.area,\n" +
            "(\n" +
            "SELECT COUNT\n" +
            "\t( 1 ) \n" +
            "FROM\n" +
            "\tTB_Evaluate e\n" +
            "\tINNER JOIN TB_Order o ON e.orderId = o.orderId \n" +
            "WHERE\n" +
            "\to.workerId = w.workerId \n" +
            "\t) evaluateNum,"+
            "row_number() OVER(order by w.orderNum) n from TB_Worker w join TB_WorkerPoint wp on w.workerId = wp.workerId\n" +
            "INNER JOIN TB_ShopPoint tp ON w.shopId = tp.shopId and tp.city = #{city} \n" +
            "<if test='productId != null'> JOIN TB_WorkerSerProduct tbP on w.workerId = tbP.workerId </if> " +
            " where  wp.city = #{city} and w.is_del = 1 " +
            " <if test='productId != null'> and tbP.productId = #{productId} </if> "+
            " <if test='shopId != null'> and w.shopId = #{shopId} </if> " +
            " <if test='onLine != null'> and w.isOnline = #{onLine} </if> " +
            " <if test='shopName != null'> and tp.shopName = #{shopName} </if> " +
            " <if test='workerId!=null'> and w.workerId = #{workerId} </if> " +
            " <if test='userName!=null'> and (w.userName like concat('%',#{userName},'%') or (w.nickName like concat('%',#{userName},'%'))) </if> "+
            " <if test='genderDesc != null '> and w.gender= #{genderDesc} </if> " +
            ") t where t.n &gt; ${pages} and t.n &lt;= ${sizeNum} </script>")
    List<Map<String,Object>> findWorkerList_3(@Param("jd") String jd, @Param("wd") String wd,
                                              @Param("city") String city, @Param("shopName") String shopName,
                                              @Param("userName") String userName, @Param("workerId") String workerId,
                                              @Param("shopId") String shopId, @Param("pages") Integer pages,
                                              @Param("sizeNum") Integer sizeNum, @Param("genderDesc") String genderDesc,
                                              @Param("onLine") String onLine, @Param("productId") String productId,
                                              @Param("evalNmsDesc") String evalNmsDesc);



    //根绝店铺来查询员工的信息
   /* @Select("<script>" +
            "SELECT w2.n," +
            "w1.*," +
            " (select top 1 (dateStr+' '+dateHHmm) dateHHmm from TB_WorkerTime where isBusy = 0 and [date]>DATEADD(Minute,30, GETDATE()) and workerId =  w1.workerId)  dateHHmm "+
            " FROM tb_workInfo_new(#{city}) w1," +
            "(SELECT TOP (${sizeNum}) row_number () OVER ( ORDER BY t.ID asc,orderNum asc " +
            " ) n, t.ID FROM tb_workerInfo(#{city},#{jd},#{wd}) t " +
            " <if test='productId != null'> left JOIN TB_WorkerSerProduct tbP on t.workerId = tbP.workerId </if> " +
            "  where 1=1 " +
            " <if test='shopId != null'> and shopId = #{shopId} </if>" +
            " <if test='productId != null'> and tbP.productId = #{productId} </if>" +
            " <if test='onLine != null'> and isOnline = #{onLine} </if>" +
            " <if test='shopName != null'> and shopName = #{shopName} </if>" +
            " <if test='workerId!=null'> and workerId = #{workerId} </if> " +
            " <if test='userName!=null'> and (userName like concat('%',#{userName},'%') or (nickName like concat('%',#{userName},'%'))) </if>"+
            "<if test='genderDesc != null '> and gender= #{genderDesc} </if>" +
            " ) w2 " +
            "WHERE " +
            " w1.ID = w2.ID AND w2.n  &gt; ${pages} ORDER BY w2.n ASC "+
            "  </script>")*/
    @Select("<script>" +
            "SELECT w1.*" +
            " FROM tb_workInfo_all(#{jd},#{wd}) w1 " +
            " <if test='productId != null'> left JOIN TB_WorkerSerProduct tbP on w1.workerId = tbP.workerId </if> " +
            "WHERE 1=1 " +
            " <if test='shopId != null'> and w1.shopId = #{shopId} </if>" +
            " <if test='productId != null'> and tbP.productId = #{productId} </if>" +
            " <if test='onLine != null'> and w1.isOnline = #{onLine} </if>" +
            " <if test='shopName != null'> and w1.shopName = #{shopName} </if>" +
            " <if test='workerId!=null'> and w1.workerId = #{workerId} </if> " +
            " <if test='userName!=null'> and (w1.userName like concat('%',#{userName},'%') or (w1.nickName like concat('%',#{userName},'%'))) </if>"+
            "<if test='genderDesc != null '> and w1.gender= #{genderDesc} </if>" +
            " order by w1.distance asc " +
            " <if test='evalNmsDesc != null'>" +
            " <if test='evalNmsDesc == 1 '> ,w1.quality asc</if>" +
            " <if test='evalNmsDesc == 2 '> ,w1.quality desc</if>" +
            " </if>"+
            " offset ${pages} rows fetch next ${sizeNum} rows only"+
            "  </script>")
    List<Map<String,Object>> findWorkerList_4(@Param("jd") String jd, @Param("wd") String wd,
                                              @Param("city") String city, @Param("shopName") String shopName,
                                              @Param("userName") String userName, @Param("workerId") String workerId,
                                              @Param("shopId") String shopId, @Param("pages") Integer pages,
                                              @Param("sizeNum") Integer sizeNum, @Param("genderDesc") String genderDesc,
                                              @Param("onLine") String onLine, @Param("productId") String productId,
                                              @Param("evalNmsDesc") String evalNmsDesc);


    /**
     * 获取技师的时间段
     * @param workerId
     * @return
     */
    @Select("select top 1 (dateStr+' '+dateHHmm) dateHHmm from TB_WorkerTime where isBusy = 0 and date>DATEADD(Minute,30, GETDATE()) and workerId = #{workerId}")
    String getDateMM(@Param("workerId") String workerId);

    @Select("<script> select works.*,(SELECT COUNT (1) sellNum FROM TB_Order t " +
            "LEFT JOIN TB_Product p ON t.productId = p.productId WHERE t.status = 1 OR ( t.status &gt;= 3 AND t.status &lt;= 9 ) \n" +
            "and t.workerId = works.workerId ) sellNum from " +
            "TB_Worker works " +
            " join TB_WorkerPoint tp on works.workerId = tp.workerId " +
            "where  works.isOnline = 1 and works.is_del = 1 and tp.city = #{city}" +
            "<if test='shopId!=null'> and works.shopId = #{shopId}</if>" +
            "  order by works.orderNum desc </script>")
    List<Map<String,Object>> workerInfoListByInfo(@Param("shopId") String shopId, @Param("city") String city);


    //修改技师的时间
    @Update("update TB_WorkerTime set isBusy = #{isBusy} where [date]>= #{date} and [date] <= DATEADD(hour,2, #{date}) and workerId = #{workerId}")
    int updateWorkTimeById(@Param("date") String date, @Param("workerId") String workerId, @Param("isBusy") String isBusy);


    //根绝店铺来查询员工的信息
    @Select("<script>" +
            " select t.id,t.shopId,t.workerId,t.userName,t.imgUrl,t.phone,t.introduce,t.serviceArea,t.workerTitle,t.isOnline,t.orderNum," +
            " t.nickName,t.gender,t.sellSum,t.addTime,t.updateTime,t.idcard,t.video,t.loginTime,t.videoImage,t.shopCodeStatus,tp.shopName," +
            " tp.city," +
            " (select top 1 (dateStr+' '+dateHHmm) dateHHmm from TB_WorkerTime where isBusy = 0 and [date]>DATEADD(Minute,30, GETDATE()) and workerId =  t.workerId)  dateHHmm," +
            " ISNULL(t1.sellNum,0) sellNum,twp.jd,twp.wd,twp.radius,twp.provinceId,twp.province,twp.cityId,twp.city,twp.areaId,twp.area," +
            "  (select count(1) from TB_Evaluate e inner join TB_Order o on e.orderId = o.orderId where o.workerId =  t.workerId) evaluateNum " +
            " from TB_Worker t INNER JOIN TB_WorkerPoint twp on t.workerId = twp.workerId INNER JOIN" +
            " TB_Shop shop on t.shopId = shop.shopId" +
            " INNER JOIN TB_ShopPoint tp ON t.shopId = tp.shopId " +
            " left join (select COUNT(1) sellNum,t.workerId from TB_Order t left join TB_Product p on t.productId =  p.productId" +
            " where  t.is_del = 1 and t.status = 1 or (t.status &gt;= 3 and t.status &lt;= 9) group by t.workerId) t1" +
            " on t.workerId = t1.workerId" +
            " <if test='productId != null'> left JOIN TB_WorkerSerProduct tbP on t.workerId = tbP.workerId </if> " +
            " where 1=1 and t.isOnline = '1' and shop.isOnline = 1  " +
            " <if test='city != null'> and tp.city = (select top 1 p.name from TB_ProvinceCityArea p join TB_ProvinceCityArea p1 on p.id = p1.pid where p1.name = #{city} order by p.id) </if>" +
            " <if test='productId != null'> and tbP.productId = #{productId} </if>" +
            " <if test='shopId != null'> and t.shopId = #{shopId} </if>" +
            " <if test='onLine != null'> and t.isOnline = #{onLine} </if>" +
            " <if test='shopName != null'> and shop.shopName = #{shopName} </if>" +
            " <if test='workerId!=null'> and t.workerId = #{workerId} </if> " +
            " <if test='userName!=null'> and (t.userName like concat('%',#{userName},'%') or (t.nickName like concat('%',#{userName},'%'))) </if>"+
            "<if test='genderDesc != null '> and t.gender= #{genderDesc} </if>" +
            " <if test='evalNmsDesc != null'>" +
            " <if test='evalNmsDesc == 1 '>order by evaluateNum </if>" +
            " <if test='evalNmsDesc == 2 '>order by evaluateNum desc</if>" +
            " </if>" +
            " <if test='evalNmsDesc == null '> order by orderNum </if>" +
            "  </script>")
    List<Map<String,Object>> findWorkerList_1(@Param("city") String city, @Param("shopName") String shopName,
                                              @Param("userName") String userName, @Param("workerId") String workerId,
                                              @Param("shopId") String shopId, @Param("pages") Integer pages,
                                              @Param("size") Integer size, @Param("genderDesc") String genderDesc,
                                              @Param("onLine") String onLine, @Param("productId") String productId,
                                              @Param("evalNmsDesc") String evalNmsDesc);


    @Select("select top 1 (dateStr+' '+dateHHmm) dateHHmm from TB_WorkerTime where isBusy = 0 and [date]>DATEADD(Minute,30, GETDATE()) and workerId =  #{workerId}")
    String getDateHHM(String workerId);

    //根据工作人员的工号，来查询员工的照片信息
    @Select("<script> select * from TB_WorkerPic where isCheck = 1  <if test='workerId != null'> and  workerId = #{workerId} </if> order by orderNum </script>")
    List<WorkerPic> findWorkerPicById(String workerId);

    //登录时判断员工是否存在
    @Select("select t.*,shop.shopName from TB_Worker t left  join TB_ShopPoint shop on t.shopId = shop.shopId\n" +
            "where   t.phone = #{phoneDesc} and t.is_del = 1 ")
    List<Worker> findWorkInfoExist(String phoneDesc) throws Exception;

    //修改登陆时间
    @Update("update TB_Worker set  loginTime = GETDATE() where workerId = #{workId}")
    int updateLoginTime(String workId);

    @Update("update TB_Worker set  video = #{videoUrl},videoImage = #{imgUrl},updateTime = GETDATE() where workerId = #{workId}")
    int updateLoginImg(@Param("workId") String workId, @Param("imgUrl") String imgUrl, @Param("videoUrl") String videoUrl);

    //查询店员的工作时间
    @Select("select * from TB_WorkerTime  w where w.date>=#{startTime} and w.date<#{endTime} and workerId=#{workerId} order by [date]")
    List<Map<String,Object>> findWorkTime(@Param("startTime") String startTime, @Param("endTime") String endTime,
                                          @Param("workerId") String workerId);

    //查询评价表
    @Select("select sum(maxNum) maxNum,sum(minNum) minNum from (" +
            "select count(1) maxNum,0 minNum from TB_Evaluate e join TB_Order o on e.orderId = o.orderId " +
            "             join TB_Worker w on o.workerId = w.workerId" +
            "             left join TB_User u on e.userId = u.userId " +
            "             where  w.isOnline = '1'  and w.is_del = 1 and o.workerId = #{workId}" +
            " union all" +
            " select 0 maxNum,count(1) minNum from TB_Evaluate e join TB_Order o on e.orderId = o.orderId " +
            "             join TB_Worker w on o.workerId = w.workerId" +
            "             left join TB_User u on e.userId = u.userId " +
            "             where  w.isOnline = '1'  and w.is_del = 1  and star>=3 and o.workerId = #{workId}" +
            " ) t ")
    List<Map<String,Object>> findEvaluate(String workId);

    @Select("select e.id,e.content,e.returnContent,e.star,e.imgUrl,e.updateTime,o.workerId,o.workerName workName,o.province,o.area,o.productName,u.nickName userName " +
            " from TB_Evaluate e join TB_Order o on e.orderId = o.orderId \n" +
            "             join TB_Worker w on o.workerId = w.workerId\n" +
            "             left join TB_User u on e.userId = u.userId \n" +
            "             where  w.isOnline = '1'  and w.is_del = 1  and o.workerId = #{workId} order by updateTime desc")
    List<Map<String,Object>> findEvaluateInfo(String workId);



    //查询评价表根据productId
/*    @Select("select e.*,w.userName workName,o.workerId workId,u.imgUrl headImg,u.nickName userName" +
            " ,o.productName,o.province,o.area  from TB_Evaluate e join TB_Order o on e.orderId = o.orderId " +
            " join TB_Worker w on o.workerId = w.workerId " +
            " left join TB_User u on e.userId = u.userId "+
            " where  w.isOnline = '1'  and w.is_del = 1 and o.productId = #{productId} order by e.updateTime desc ")*/
    @Select("select * from ( " +
            " select e.*,w.userName workName,o.workerId workId,u.imgUrl headImg,u.nickName userName " +
            " ,o.productName,o.province,o.area,row_number() over  (order by  e.updateTime desc) n " +
            " from TB_Evaluate e join TB_Order o on e.orderId = o.orderId  " +
            "  join TB_Worker w on o.workerId = w.workerId " +
            "  left join TB_User u on e.userId = u.userId " +
            "  where  w.isOnline = '1'  and w.is_del = 1 and o.productId = #{productId} " +
            ") hhh  where hhh.n > ${startPage} and hhh.n <= ${endPage}")
    List<Map<String,Object>> findEvaluateByProductId(@Param("productId") String productId, @Param("startPage") Integer startPage, @Param("endPage") Integer endPage);


    //删除和员工关联图片
    @Delete("delete from TB_WorkerPic where workerId = #{workerId} and imgType = 0")
    int delWorkPic(String workerId);

    //添加和员工关联图片
    @Insert("insert into TB_WorkerPic(workerId,imgUrl,imgType,orderNum,addTime,isCheck)" +
            "VALUES(#{workerId},#{imgUrl},#{imgType},${orderNum},GETDATE(),0)")
    int addWorkPic(@Param("workerId") String workerId, @Param("imgUrl") String imgUrl, @Param("imgType") String imgType, @Param("orderNum") Integer orderNum);

    //修改技师证书图片
    @Update("update TB_WorkerPic set imgUrl = #{imgUrl},isCheck = 0 where workerId=#{workerId} and imgType=#{imgType} ")
    int updWorkPic(@Param("imgUrl") String imgUrl, @Param("workerId") String workerId, @Param("imgType") String imgType);

    //根据员工id，来查询员工图片
    @Select("select * from TB_WorkerPic where workerId = #{workerId} and imgType = 0 ")
    List<Map<String,Object>> findWorkPicList(String workerId);

    @Select("select * from TB_WorkerPic where workerId = #{workerId} and imgType != 0 ")
    List<Map<String,Object>> findWorkPicTypeList(String workerId);

    //修改员工信息
    @Update("update TB_Worker set userName = #{userName},nickName= #{nickName},gender = #{gender},imgUrl = #{imgUrl}," +
            "introduce = #{introduce},idcard=#{idcard} where workerId = #{workerId}")
    int updateWorkInfo(Worker worker);

    //根据技师的id/时间来修改是否繁忙
    @Update("<script> update TB_WorkerTime set isBusy = #{isBusy} where " +
            "<if test='dateStr!=null'> dateStr = #{dateStr} and workerId = #{workerId} </if>" +
            "<if test='id!=null'> id = #{id} </if> </script>")
    int updateWorkBusy(@Param("isBusy") Integer isBusy, @Param("dateStr") String dateStr,
                       @Param("id") String id, @Param("workerId") String workerId);

    //查询技师的状态栏
    @Select("select  name from (select c.chooseId,count(1) nums  from TB_Eval_Choose c" +
            " left join TB_Evaluate e on c.evalId = e.id " +
            " left join TB_Order o on e.orderId = o.orderId" +
            " where 1=1 and o.workerId = #{workerId}" +
            " group by c.chooseId) c" +
            " left join TB_EvalChooseDetail d on c.chooseId = d.id" +
            " order by nums desc ")
    List<Map<String,Object>> findWorkEvalStatus(String workerId);


    //查看标签
    @Select("select * from TB_Eval_Choose c left join TB_EvalChooseDetail d on c.chooseId = d.id where evalId = #{evalId}")
    List<Map<String,Object>> findWorkEvalBiao(String evalId);

    @Select("<script> select e.id,e.orderId,e.userId,o.workerId workId,e.content,e.star,e.imgUrl,CONVERT(varchar(100), e.updateTime, 23) updateTime,o.address,o.serviceNumber,o.payOnline,p.productName," +
            " o.workerName,u.userName,u.imgUrl userImgUrl,sp.shopName,sp.shopId from TB_Evaluate e join TB_Order o on e.orderId = o.orderId join TB_Product p on o.productId = p.productId " +
            " join TB_User u on e.userId = u.userId " +
            " join TB_Shop sp on o.shopId = sp.shopId " +
            " where  1=1" +
            "<if test='shopId!=null'> and  o.shopId = #{shopId} </if>" +
            "<if test='workerId!=null'> and  o.workerId = #{workerId} </if>" +
            " order by e.updateTime desc </script>")
    List<Map<String,Object>> findWorkEval(@Param("workerId") String workerId, @Param("shopId") String shopId);



    @Select("<script> select count(1) orderNum,1 status from TB_Order where 1=1 " +
            "<if test='workerId!=null'> and workerId = #{workerId} </if>" +
            "<if test='shopId!=null'> and shopId = #{shopId} </if>" +
            " and status &gt;= 3 and status &lt;=9 \n" +
            "UNION ALL\n" +
            "select count(1) orderNum,2 status from TB_Order where 1=1 " +
            "<if test='workerId!=null'> and workerId = #{workerId} </if>" +
            "<if test='shopId!=null'> and shopId = #{shopId} </if>" +
            " and status &gt;= 10 and status &lt;=12 \n" +
            "UNION ALL\n" +
            "select count(1) orderNum,3 status from TB_Order o join TB_Evaluate e on o.orderId = e.orderId where 1=1 " +
            "<if test='workerId!=null'> and workerId = #{workerId} </if>" +
            "<if test='shopId!=null'> and shopId = #{shopId} </if>" +
            "and e.star &gt;= 3\n" +
            "UNION ALL\n" +
            "select count(1) orderNum,4 status from TB_Order o join TB_Evaluate e on o.orderId = e.orderId where 1=1 " +
            "<if test='workerId!=null'> and workerId = #{workerId} </if>" +
            "<if test='shopId!=null'> and shopId = #{shopId} </if>" +
            " and e.star &lt; 3" +
            "</script>")
    List<Map<String,Object>> findWorkOrderNum(@Param("workerId") String workerId, @Param("shopId") String shopId);



    @Select("<script> select count(1) orderNum,ISNULL(SUM(payOnLine), 0)  payOnLine,1 status from TB_Order where  1=1 " +
            "<if test='workerId!=null'> and workerId = #{workerId} </if> " +
            "<if test='shopId!=null'> and shopId = #{shopId} </if>" +
            "and status = -1 and shopReceiveTime BETWEEN #{startTime} AND  #{endTime}\n" +
            "UNION ALL\n" +
            "select count(1) orderNum,ISNULL(SUM(payOnLine), 0)  payOnLine,2 status from TB_Order where 1=1 " +
            "<if test='workerId!=null'> and workerId = #{workerId} </if> " +
            "<if test='shopId!=null'> and shopId = #{shopId} </if>" +
            "and status &gt;= 7 and status &lt;= 9 and shopReceiveTime BETWEEN #{startTime} AND  #{endTime}\n" +
            "UNION ALL\n" +
            "select count(1) orderNum,ISNULL(SUM(payOnLine), 0)  payOnLine,3 status from TB_Order where 1=1 " +
            "<if test='workerId!=null'> and workerId = #{workerId} </if> " +
            "<if test='shopId!=null'> and shopId = #{shopId} </if>" +
            " and status &gt;= 3 and status &lt; 7 and shopReceiveTime BETWEEN #{startTime} AND  #{endTime}" +
            "</script>")
    List<Map<String,Object>> findWorkOrderNumber(@Param("workerId") String workerId,
                                                 @Param("shopId") String shopId,
                                                 @Param("startTime") String startTime,
                                                 @Param("endTime") String endTime);

    @Select("<script> select count(1) orderNum,ISNULL(SUM(payOnLine), 0)  payOnLine,1 status from TB_Order where 1=1 " +
            "<if test='workerId!=null'> and workerId = #{workerId} </if> " +
            "<if test='shopId!=null'> and shopId = #{shopId} </if>" +
            " and status = -1 \n" +
            "UNION ALL\n" +
            "select count(1) orderNum,ISNULL(SUM(payOnLine), 0)  payOnLine,2 status from TB_Order where 1=1 " +
            "<if test='workerId!=null'> and workerId = #{workerId} </if> " +
            "<if test='shopId!=null'> and shopId = #{shopId} </if>" +
            "and status &gt;= 7 and status &lt;= 9 \n" +
            "UNION ALL\n" +
            "select count(1) orderNum,ISNULL(SUM(payOnLine), 0)  payOnLine,3 status from TB_Order where 1=1 " +
            "<if test='workerId!=null'> and workerId = #{workerId} </if> " +
            "<if test='shopId!=null'> and shopId = #{shopId} </if>" +
            " and status &gt;= 3 and status &lt; 7 " +
            "</script>")
    List<Map<String,Object>> findWorkOrderNumberAll(@Param("workerId") String workerId, @Param("shopId") String shopId);


    @Select("<script> select CONVERT(varchar(12) , workconfirmTime, 111 ) dates,count(1) num," +
            "SUM(ISNULL((payOnline),0)) payOnline,SUM(ISNULL((trafficPrice),0)) trafficPrice," +
            "SUM(ISNULL(fillMoney, 0)) fillMoney  from TB_Order where status &gt;=8 and status &lt;=9 " +
            "<if test='workerId!=null'> and workerId = #{workerId} </if>" +
            "<if test='shopId!=null'> and shopId = #{shopId} </if>" +
            " <if test='msgFlag!=0'> and workconfirmTime BETWEEN #{startTime} AND #{endTime} </if> \n" +
            "group by CONVERT(varchar(12) , workconfirmTime, 111 ) </script>")
    List<Map<String,Object>> findWorkerOrderDetail(@Param("workerId") String workerId, @Param("shopId") String shopId,
                                                   @Param("startTime") String startTime,
                                                   @Param("endTime") String endTime, @Param("msgFlag") Integer msgFlag);


    @Select("select top 5 ti.id,ti.dateHHmm,ti.dateStr from TB_Order o RIGHT join TB_WorkerTime ti " +
            " on o.workerId = ti.workerId where ti.workerId = #{workerId} " +
            " and ti.date>= DATEADD(mi, -90, #{date}) and o.orderId = #{orderId} order by ti.date")
    List<Map<String,Object>> findInfo(@Param("workerId") String workerId, @Param("orderId") String orderId, @Param("date") String date);

    //查询邀请码是否存在
    @Select("<script> select * from TB_Shop where 1=1 " +
            "<if test='code!=null'> and shopCode = #{code} </if>" +
            "<if test='shopId!=null'> and shopId = #{shopId} </if>" +
            "</script>")
    Map<String,Object> findShopByCode(@Param("code") String code, @Param("shopId") String shopId);



    @Update("update TB_Worker set shopCodeStatus = #{shopCodeStatus},shopId = #{shopId} where workerId = #{workerId} ")
    int updShopByCode(@Param("shopCodeStatus") Integer shopCodeStatus, @Param("workerId") String workerId, @Param("shopId") String shopId);

    //查询版本信息
    @Update("update TB_Worker set version = #{version} where phone = #{phone}")
    int updateVersion(@Param("version") String version, @Param("phone") String phone);

    @Update("update TB_Worker set quality = #{star},percentage = #{evaluateNumLv} where workerId = #{workerId}")
    int updateWorkerStar(@Param("workerId") String workerId, @Param("star") int star,
                         @Param("evaluateNumLv") String evaluateNumLv);

    //查询评价表
    @Select("select sum(maxNum) maxNum,sum(minNum) minNum,workerId from (\n" +
            "            select count(1) maxNum,0 minNum,w.workerId  from TB_Evaluate e join TB_Order o on e.orderId = o.orderId \n" +
            "                         join TB_Worker w on o.workerId = w.workerId\n" +
            "                         left join TB_User u on e.userId = u.userId \n" +
            "                         where  w.isOnline = '1'  and w.is_del = 1  group by w.workerId\n" +
            "             union all\n" +
            "             select 0 maxNum,count(1) minNum,w.workerId from TB_Evaluate e join TB_Order o on e.orderId = o.orderId \n" +
            "                         join TB_Worker w on o.workerId = w.workerId\n" +
            "                         left join TB_User u on e.userId = u.userId\n" +
            "                         where  w.isOnline = '1'   and w.is_del = 1 and e.star>=3  group by w.workerId\n" +
            "           ) t GROUP BY workerId")
    List<Map<String,Object>> findEvaluateInfo_1();

    /**
     * 根据技师id来查询信息
     * @param workerId
     * @return
     */
    @Select("select jd,wd,radius from TB_WorkerPoint where workerId = #{workerId}")
    Worker queryTbWorkerInfo(@Param("workerId") String workerId);

    /**
     * 查询所有员工信息
     * @return
     */
    @Select("select workerId,dateHHmm from TB_Worker where isOnline = 1 and is_del = 1")
    List<Map<String,String>> queryWorkerList();

    /**
     * 根据主键ID进行修改最新时间
     * @param workerId
     * @param dateHHmm
     * @return
     */
    @Update("update TB_Worker set dateHHmm =#{dateHHmm}  where workerId = #{workerId} and isOnline = 1 and is_del = 1")
    int updWorkerInfo(@Param("workerId") String workerId, @Param("dateHHmm") String dateHHmm);

    /**
     * 根据主键ID进行修改标签
     * @param workerId
     * @param evalStatus_one
     * @param evalStatus_two
     * @param evalStatus_three
     * @return
     */
    @Update("update TB_Worker set evalStatus_one =#{evalStatus_one},evalStatus_two =#{evalStatus_two},evalStatus_three =#{evalStatus_three}  where workerId = #{workerId} and isOnline = 1 and is_del = 1")
    int updWorkerInfoLabel(@Param("workerId") String workerId
            , @Param("evalStatus_one") String evalStatus_one
            , @Param("evalStatus_two") String evalStatus_two
            , @Param("evalStatus_three") String evalStatus_three);


    //修改技师的经纬度和地区
    @Update("update TB_WorkerPoint set provinceId = #{provinceId},province = #{province}," +
            "cityId = #{cityId},city = #{city},areaId =#{areaId},area=#{area}," +
            "jd=${jd},wd=${wd} where workerId = #{workerId}")
    int updWorkInfo(WorkerVo workerVo);

    //查看省份地区的id
    @Select("select id from TB_ProvinceCityArea where name = #{name}")
    String findProvinceInfo(@Param("name") String name);

    @Select("select id,shopId,shopName,workerId,workerName,orderNum,productId,province," +
            " city,cityId,area,areaId,jd,wd,radius,addTime from" +
            " TB_WorkerPoint where workerId = #{workerId}")
    WorkerVo findWorkerLocation(@Param("workerId") String workerId);

    /**
     * 添加技师绑定微信
     * @param workerWxInfo
     * @return
     */
    @Insert("insert into TB_UserAndOpenId " +
            "VALUES(#{workerId},#{openId},GETDATE(),GETDATE(),#{nickName},#{avatarUrl},#{sex})")
    int addWorkerOpenInfo(WorkerWxInfo workerWxInfo);

    /**
     * 查询是否已经绑定
     * @param openId
     * @param workerId
     * @return
     */
    @Select("<script>" +
            "select count(1) from TB_UserAndOpenId where 1=1" +
            "<if test='workerId!=null'> or workerId = #{workerId} </if>" +
            "<if test='openId!=null'> and openId = #{openId} </if>" +
            "</script>")
    int findWorkerOpenInfo(@Param("workerId") String workerId, @Param("openId") String openId);

    @Select("<script>" +
            "select count(1) from TB_UserAndOpenId where 1=1" +
            "<if test='workerId!=null'> and workerId = #{workerId} </if>" +
            "<if test='openId!=null'> and openId = #{openId} </if>" +
            "</script>")
    int findWorkerOpenInfoWx(@Param("workerId") String workerId, @Param("openId") String openId);

    /**
     * 修改绑定
     * @param workerWxInfo
     * @return
     */
    @Update("update TB_UserAndOpenId set openId = #{openId},nickName=#{nickName},avatarUrl=#{avatarUrl},sex=#{sex},updateTime=GETDATE() where workerId = #{workerId}")
    int updWorkerOpenInfo(WorkerWxInfo workerWxInfo);

    /**
     * 解除绑定微信
     * @param workerId
     * @return
     */
    @Update("delete from TB_UserAndOpenId where workerId = #{workerId}")
    int delWorkerOpenInfo(String workerId);

    @Insert("insert into TB_UserAndOpenId(workerId,openId,createTime,updateTime)" +
            " VALUES(#{workerId},#{openId},GETDATE(),GETDATE())")
    int addWorkerOpenInfos(@Param("openId") String openId, @Param("workerId") String workerId);

    /**
     * 技师基本信息
     * @param workerVo
     * @return
     */
    @Insert("insert into TB_Worker(shopId,workerId,userName,imgUrl,phone,introduce,serviceArea,isOnline," +
            "nickName,gender," +
            "sellSum,addTime,is_del,quality,percentage)" +
            " VALUES(#{shopId},#{workerId},#{workerName},#{imgUrl},#{phone},#{introduce},#{city},0," +
            "#{nickName},#{gender}," +
            "#{sellSum},GETDATE(),1,5,'100%')")
    int addWorkerInfo(WorkerVo workerVo);



    /**
     * 技师的经纬度保存
     * @param workerVo
     * @return
     */
    @Insert("insert into TB_WorkerPoint(shopId,shopName,workerId,workerName,provinceId,province,cityId,city," +
            "areaId,area,jd,wd,addTime,address)\n" +
            " VALUES(#{shopId},#{shopName},#{workerId},#{workerName},#{provinceId},#{province},#{cityId},#{city}," +
            "#{areaId},#{area},#{jd},#{wd},GETDATE(),#{address})")
    int addWorkerInfoPoint(WorkerVo workerVo);

    @Select("select count(1) from TB_UserAndOpenId where workerId = #{workerId}")
    int findOpenIdCount(String workerId);

    /**
     * 查询该手机号，是否已经注册
     * @param phone
     * @return
     */
    @Select("select count(1) from TB_Worker where phone = #{phone} and is_del = '1'")
    int findWorkerCount(String phone);
}
