package com.mzj.mohome.mapper;

import com.mzj.mohome.entity.Order;
import com.mzj.mohome.vo.OrderDto;
import com.mzj.mohome.vo.OrderVo;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {

    @Select("select *from TB_Order where orderId = #{orderId}")
    Map<String,Object> findOrder(String orderId);

    //查询订单列表信息
    @Select("<script>  select orders.*,shop.shopName,shop.servicePhone, p.imgUrl as imgProductUrl from TB_Order orders join TB_Shop shop on orders.shopId = shop.shopId join\n" +
            " TB_Product p on orders.productId = p.productId  where 1=1 " +
            "<if test='statusDesc != null'> " +
            "<if test='statusDesc==3'> and orders.status &gt;= 3 and orders.status &lt; 7 </if>" +
            "<if test='statusDesc==4'> and orders.status &gt;= 7 and orders.status &lt;= 9 </if>" +
            "<if test='statusDesc!=4 and statusDesc!=3'> and orders.status=#{statusDesc} </if> " +
            "</if> " +
            "<if test='shopId!=null'> and shop.shopId = #{shopId} </if>  " +
            "<if test='userId!=null'> and orders.userId = #{userId} </if>  " +
            "<if test='workerId!=null'> and orders.workerId = #{workerId} and  orders.status != 0 </if>" +
            "<if test='orderId!=null'> and orders.id = #{orderId} </if>" +
            "order by orders.id desc </script>")
    List<OrderVo> findOrerList(@Param("statusDesc") String statusDesc, @Param("userId")String userId,
                               @Param("orderId") String orderId, @Param("workerId") String workerId,
                               @Param("shopId") String shopId);


    //查询订单列表信息
    @Select("<script> select top 10 * from (" +
            "select row_number() over(order by orders.id desc) as rownumber, " +
            "orders.*,shop.shopName,shop.servicePhone, p.imgUrl as imgProductUrl from TB_Order orders join TB_Shop shop on orders.shopId = shop.shopId join\n" +
            " TB_Product p on orders.productId = p.productId  where 1=1 and orders.status not in(0,2) " +
            " and orders.workerId = #{workerId} and  orders.status != 0 " +
            "<if test='statusDesc != null'> " +
            "<if test='statusDesc==3'> and orders.status &gt;= 3 and orders.status &lt; 7 </if>" +
            "<if test='statusDesc==4'> and orders.status &gt;= 7 and orders.status &lt;= 9 </if>" +
            "<if test='statusDesc!=4 and statusDesc!=3'> and orders.status=#{statusDesc} </if> " +
            "</if> " +
            "<if test='shopId!=null'> and shop.shopId = #{shopId} </if>  " +
            "<if test='userId!=null'> and orders.userId = #{userId} </if>  " +
            "<if test='workerId!=null'> and orders.workerId = #{workerId} and  orders.status != 0 </if>" +
            "<if test='orderId!=null'> and orders.id = #{orderId} </if>" +
            ") t where rownumber &gt; ${page} </script>")
    List<OrderVo> findOrerList_1(OrderDto orderDto);



    //查询订单列表信息
    @Select("<script> select * from(\n" +
            "select t.id,t.userId, p.imgUrl,p.productId,p.productName,t.status,t.addTime,t.orderId,t.payOnline," +
            "t.serviceNumber,p.productTime,t.trafficPrice,tw.userName workerName,tw.phone workerPhone, " +
            "t.workerId,tw.imgUrl as workerImg,t.aboutTime,t.userName,t.phone,t.address,t.jd userJD," +
            "t.wd userWD,tw.jd workJd,tw.wd workWd,t.shopId,shop.shopName ,shop.servicePhone,t.returnMoney,t.tradeNo," +
            "t.payType,t.fillMoney,t.remarks,t.shopReceiveTime,t.serviceCompleteTime,t.workconfirmTime from TB_Order t join TB_Product p on t.productId = p.productId " +
            "join TB_Worker tw on t.workerId = tw.workerId join TB_Shop shop on tw.shopId = shop.shopId " +
            "where t.status != 0\n" +
            "union all \n" +
            "select t.id,t.userId, p.imgUrl,p.productId,p.productName,t.status,t.addTime,t.orderId,t.payOnline," +
            "t.serviceNumber,p.productTime,t.trafficPrice,tw.userName workerName,tw.phone workerPhone, " +
            "t.workerId,tw.imgUrl as workerImg,t.aboutTime,t.userName,t.phone,t.address,t.jd userJD," +
            "t.wd userWD,tw.jd workJd,tw.wd workWd,t.shopId,shop.shopName ,shop.servicePhone,t.returnMoney,t.tradeNo," +
            "t.payType,t.fillMoney,t.remarks,t.shopReceiveTime,t.serviceCompleteTime,t.workconfirmTime from TB_Order t join TB_Product p on t.productId = p.productId " +
            "join TB_Worker tw on t.workerId = tw.workerId join TB_Shop shop on tw.shopId = shop.shopId " +
            "where  t.status = 0 and  DateDiff(s,t.addTime,GETDATE()) &lt;= 900\n" +
            ") t " +
            "where 1=1 and t.status != 2 " +
            "<if test='statusDesc!=null'> " +
            "<if test='statusDesc==9'> and t.status in(7,8) </if>" +
            "<if test='statusDesc==8'> and t.status in(7,8,9) </if>" +
            "<if test='statusDesc!=8 and statusDesc!=9'> and t.status = #{statusDesc} </if>" +
            "  </if> " +
            "<if test='userId!=null'> and t.userId = #{userId}</if> " +
            "<if test='orderId!=null'> and t.orderId = #{orderId}</if> " +
            "<if test='workerId!=null'> and t.workerId = #{workerId}</if> "+
            "order by t.id desc  </script>")
    List<Map<String,Object>> findOrerListInfo(@Param("page") Integer page, @Param("statusDesc")String statusDesc,
                                              @Param("userId")String userId,@Param("orderId")String orderId,
                                              @Param("workerId")String workerId);

    //查询订单列表信息进行中的
    @Select("<script> select * from(\n" +
            "select t.id,t.userId, p.imgUrl,p.productId,p.productName,t.status,t.addTime,t.orderId,t.payOnline," +
            "t.serviceNumber,p.productTime,t.trafficPrice,tw.userName workerName,tw.phone workerPhone, " +
            "t.workerId,tw.imgUrl as workerImg,t.aboutTime,t.userName,t.phone,t.address,t.jd userJD," +
            "tw.wd userWD,tw.jd workJd,tw.wd workWd,t.shopId,shop.shopName ,shop.servicePhone,t.returnMoney,t.tradeNo," +
            "t.payType,t.fillMoney,t.remarks from TB_Order t join TB_Product p on t.productId = p.productId " +
            "join TB_Worker tw on t.workerId = tw.workerId join TB_Shop shop on tw.shopId = shop.shopId " +
            "where t.status != 0\n" +
            "union all \n" +
            "select t.id,t.userId, p.imgUrl,p.productId,p.productName,t.status,t.addTime,t.orderId,t.payOnline," +
            "t.serviceNumber,p.productTime,t.trafficPrice,tw.userName workerName,tw.phone workerPhone, " +
            "t.workerId,tw.imgUrl as workerImg,t.aboutTime,t.userName,t.phone,t.address,t.jd userJD," +
            "tw.wd userWD,tw.jd workJd,tw.wd workWd,t.shopId,shop.shopName ,shop.servicePhone, t.returnMoney,t.tradeNo," +
            "t.payType,t.fillMoney,t.remarks from TB_Order t join TB_Product p on t.productId = p.productId " +
            "join TB_Worker tw on t.workerId = tw.workerId join TB_Shop shop on tw.shopId = shop.shopId " +
            "where  t.status = 0 and  DateDiff(s,t.addTime,GETDATE()) &lt;= 900\n" +
            ") t " +
            "where 1=1 and t.status != 2 <if test='statusDesc != null'> and (t.status =1 or " +
            "(t.status &gt;= 3 and t.status &lt; 7)) </if> " +
            "<if test='userId!=null'> and t.userId = #{userId}</if> " +
            "<if test='orderId!=null'> and t.orderId = #{orderId}</if> " +
            "<if test='workerId!=null'> and t.workerId = #{workerId}</if> "+
            "order by t.id desc </script>")
    List<Map<String,Object>> findOrerListInfoByOn(@Param("page") Integer page, @Param("statusDesc")String statusDesc,
                                                  @Param("userId")String userId, @Param("orderId")String orderId,
                                                  @Param("workerId")String workerId);




    /**
     * 添加订单信息
     * @return
     */
    @Insert("INSERT INTO TB_Order([orderId], [shopId], [productId], [userId], [workerId], [productName], [oldPrice], [price], [memberPrice],  [status], [userName], [province], [city], [area], [address], [phone], [jd], [wd], [detail], [trafficPrice], [remarks], [serviceNumber], [aboutTime], [orderPayTime],[addTime],[payOnline],[bargainPrice],updateTime) \n" +
            "VALUES (#{orderId},#{shopId}, #{productId}, #{userId},#{workerId},#{productName},#{oldPrice},#{price},#{memberPrice},#{status}, #{userName}, #{province}, #{city},#{area},#{address},#{phone},#{jd},#{wd},#{detail},#{trafficPrice},#{remarks},#{serviceNumber}, #{aboutTime},getdate(), getdate(),#{payOnline},#{bargainPrice},GETDATE())")
    int addOrderInfo(Order order);

    //查询该商品的秒杀价
    @Select("select top 1 ISNULL(secondPrice, 0) secondPrice from TB_Coupon where GETDATE() BETWEEN startTime AND endTime and productId = #{productId} and couponType = 1 and status = 1")
    Float findSecorndPrice(String productId);


    //职工接单操作
    @Update("<script> update TB_Order set status=#{status}," +
            "orderReviceId=#{orderReviceId},orderReviceName=#{orderReviceName},workerPhone=#{workerPhone}," +
            "workerName=#{workerName}  " +
            "<if test='status==3'> ,shopReceiveTime = getdate()</if>" +
            ",updateTime = GETDATE()" +
            "where orderId =#{orderId} </script>")
    int updateOrderInfo(Order order);


    @Update("<script> update TB_Order set " +
            "status = #{status} " +
            "<if test='status == 8'> ,workconfirmTime = getdate() </if> " +
            "<if test='status == 6'> ,serviceCompleteTime=getdate() </if> " +
            ",updateTime = GETDATE()" +
            " where orderId =#{orderId} " +
            "</script>")
    int updateOrderInfoStatus(Order order);


    @Select("select w.userName,w.phone,w.workerId,s.shopName,s.shopPhone,s.shopId from TB_Worker w join TB_Shop s on w.shopId = s.shopId where w.workerId = ( \n" +
            " select workerId from TB_Order where orderId = #{orderId}" +
            ")")
    Map<String,Object> findWorkerInfo(@Param("orderId") String orderId);



    @Select("select * from TB_Order where orderId = #{orderId} and status >= #{statusDesc}")
    List<Map<String,Object>> findWorkerOrderList(@Param("orderId") String orderId,@Param("statusDesc")String statusDesc);

    @Select("select * from TB_Order where orderId = #{orderId}")
    List<Map<String,Object>> findWorkerOrderListById(String orderId);

    @Update("update TB_Order set status = #{status},workconfirmTime = GETDATE(),updateTime = GETDATE()  where orderId =#{orderId} ")
    int updateOrderStatusTime(Order order);

    @Update("UPDATE TB_WorkerTime set isBusy=0 where id in (select top 5 id from TB_WorkerTime where [date]>=  (select aboutTime from TB_Order where orderId = #{orderId}) " +
            "and workerId = #{workerId} " +
            ")")
    int updateOrdersTimes(@Param("workerId") String workerId,@Param("orderId") String orderId);

    //修改订单退款
    @Update("update TB_Order set status = 10,returnReason=#{returnReason},returnMoney=#{returnMoney},returnType=0,updateTime = GETDATE()  where orderId =#{orderId}")
    int updateReturnOrder(Order order);

    @Select("select COUNT(1) from TB_CouponAndUserId where orderId = #{orderId} and isUser = 1")
    int findCouponId(@Param("orderId") String orderId);




    @Select("select * from TB_Order where orderId = #{orderId}")
    List<Map<String,Object>> orderStatusList(String orderId);

    //查询评论的状态
    @Select("select id,name,addTime from TB_EvalChooseDetail order by id ")
    List<Map<String,Object>> findEvalChooseDetail();

    //查询小号列表
    @Select("select * from TB_Trumpet where status = 1")
    List<Map<String,Object>> findTrum();

    //根据状态A和小号进行查询是否正在使用
    @Select("select * from TB_Trump_Phone where phone = #{phone} and phoneA = #{phoneA} and status=1 ")
    List<Map<String,Object>> findPhoneAAndB(@Param("phone") String phone,@Param("phoneA")String phoneA);

    //修改小号的状态
    @Update("update TB_Trump_Phone set status = 0 where id = #{id}")
    int UpPhoneStatus(String id);

    //添加小号的关联表
    @Insert("insert into TB_Trump_Phone(phone,phoneA,phoneB,status,addTime)\n" +
            "VALUES(#{phone},#{phoneA},#{phoneB},1,getdate())")
    int addPhoneInfo(@Param("phone")String phone,@Param("phoneA")String phoneA,@Param("phoneB")String phoneB);


    //查询钱包明细
    @Select("select * from( \n" +
            "select body,payTime,onlinePay,'1' pay_status,payType from TB_PayRecord where (payType = 1 or buyType = 3)  and status = 1 \n" +
            "and userId = #{userId}\n" +
            "union all\n" +
            "select t_r.body,t_r.return_time payTime,t_r.onlinePay,'2' pay_status,t_r.return_type payType  from TB_ReturnRecord t_r INNER JOIN TB_Order t_o on t_r.orderId = t_o.orderId  where \n" +
            "t_r.status = 1 and t_o.userId = #{userId} and t_r.return_type = 1\n" +
            ") t order by payTime desc")
    List<Map<String,Object>> findOrderList(String userId);

    //查询新订单，且未发送订单的人员
    @Select("select w.phone,w.workerId from TB_Order t join TB_Worker w on t.workerId = w.workerId where t.status = 1  and t.orderSMSFlag = 0  group by w.phone,w.workerId")
    List<Map<String,Object>> findWorkerInfoNew();

    //查询新订单，把状态进行修改为已发送的
    @Update("update TB_Order set orderSMSFlag = 1,updateTime = GETDATE() where workerId = #{phoneDesc} and status = 1 and  orderSMSFlag = 0")
    int updateInfoNew(String phoneDesc);



}
