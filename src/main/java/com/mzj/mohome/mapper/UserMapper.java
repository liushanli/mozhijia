package com.mzj.mohome.mapper;

import com.mzj.mohome.entity.*;
import org.apache.ibatis.annotations.*;
import org.apache.logging.log4j.core.config.plugins.PluginAliases;

import javax.websocket.server.ServerEndpoint;
import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {
    //获取用户名单
    @Select("select * from tb_user where id=${id}")
    List<User> getById(Integer id) throws Exception;

    //登录时判断用户是否存在
    @Select("select * from tb_user  where phone = #{phoneDesc}")
    List<User> getByUserExist(String phoneDesc) throws Exception;

    //更改用户信息
    @Update("<script> update tb_user  set userId=concat(userId,'') <if test='surplusMoney != null'> ,surplusMoney = ISNULL(surplusMoney, 0) + ${surplusMoney}  </if> " +
            "<if test='nickName != null'> ,nickName = #{nickName} </if>" +
            "<if test='imgUrl != null'> ,imgUrl = #{imgUrl} </if>" +
            "<if test='cardId != null'> ,cardId = #{cardId},cardName = #{cardName},cardStartTime = getdate(),cardEndTime = DATEADD(mm,${month},GETDATE()) </if>" +
            "where id = #{id}</script>")
    int updUser(User user);

    //登录时判断客户是否存在
    @Select("select top 1 s.* from TB_SmsSend s  where s.phone = #{phoneDesc} and s.sendCode =  #{sendCode}")
    List<Map<String,Object>> getByUserInfoExist(@Param("phoneDesc") String phoneDesc, @Param("sendCode")String sendCode) throws Exception;

    @Select("select top 1 t.* from TB_User t  " +
            "where 1=1 and t.isBlackList = 0 and t.phone = #{phoneDesc} ")
    List<User> getByUserInfoByPhoneExist(@Param("phoneDesc") String phoneDesc) throws Exception;


    //登录时判断客户是否存在
    @Select("select top 1 t.* from TB_User t where t.isBlackList = 0 and userId = #{userId} order by id desc ")
    List<User> getByUserInfoById(String userId);

    @Select("select * from TB_User " +
            " where userId not in (select DISTINCT userId from TB_CouponAndUserId where isUser=0) and isBlackList =0")
    List<User> getUserInfo();


    //登录时判断客户是否存在
    @Select("<script>select top 1 t.* from TB_User t " +
            " where 1=1 and openId = #{openId} and t.isBlackList = 0  order by t.id desc </script>")
    List<User> getByUserInfoExistWhat(@Param("openId") String openId) throws Exception;

    //登录时判断客户是否存在
    @Select("<script>select top 1 t.* from TB_User t " +
            " where 1=1 and  t.appleData = #{appleData} and t.isBlackList = 0 order by t.id desc </script>")
    List<User> getByUserInfoExistApple(@Param("appleData") String appleData) throws Exception;

    //如果用户不存在则直接添加到数据库
    @Insert("insert into TB_User(userId,userName,phone,nickName,loginType,loginTime,addTime,isBlackList,level,imgUrl)\n" +
            "VALUES(#{userId},#{name},#{phoneDesc},#{name},0,GETDATE(),GETDATE(),0,0,'/static/images/userLogo.png')")
    int addUserInfo(@Param("userId")String userId,@Param("phoneDesc") String phoneDesc,@Param("name")String name) throws Exception;

    //如果用户不存在则直接添加到数据库微信
    @Insert("insert into TB_User(userId,userName,phone,nickName,imgUrl,sex,openId,loginType,loginTime,addTime,isBlackList,level)\n" +
            "VALUES(#{userId},#{phoneDesc},#{phoneDesc},#{nickName},#{imgUrl},#{sex},#{openId}, 0,GETDATE(),GETDATE(),0,0)")
    int addWhatUserInfo(@Param("userId")String userId,@Param("phoneDesc")String phoneDesc, @Param("nickName")String nickName,
                        @Param("imgUrl") String imgUrl, @Param("openId")String openId,
                        @Param("sex")String sex) throws Exception;


    //如果用户不存在则直接添加到数据库微信
    @Insert("insert into TB_User(userId,userName,phone,nickName,imgUrl,sex,openId,loginType,loginTime,addTime,isBlackList,level,appleData)\n" +
            " VALUES(#{userId},#{nickName},'',#{nickName},'','','', 0,GETDATE(),GETDATE(),0,0,#{appleData})")
    int addAppleUserInfo(@Param("userId")String userId,@Param("appleData") String appleData, @Param("nickName")String nickName) throws Exception;



    //有手机号没有绑定微信用户信息的时候
    @Update("update TB_User set openId = #{openId},imgUrl = #{imgUrl},nickName=#{nickName},sex=#{sex} where phone = #{phoneDesc}")
    int updUserInfoByPhone(@Param("phoneDesc") String phoneDesc, @Param("nickName")String nickName,
                           @Param("imgUrl")String imgUrl, @Param("openId")String openId,
                           @Param("sex")String sex);

    @Update("update TB_User set appleData = #{appleData},loginTime = GETDATE() where phone = #{phoneDesc} ")
    int updateUserInfoByApple(@Param("appleData") String appleData, @Param("phoneDesc")String phoneDesc) throws Exception;

    @Update("update TB_User set loginTime = GETDATE() where phone = #{phoneDesc}")
    int updateUserInfo(String phoneDesc) throws Exception;

    //根据openId来修改手机号信息
    @Update("update TB_User set phone = #{phoneDesc} where openId = #{openId}")
    int updateUserPhone(@Param("phoneDesc") String phoneDesc, @Param("openId")String openId) throws Exception;

    //根据手机号查询是否存在有重复的
    @Select("select *  from TB_User where phone = #{phoneDesc}")
    List<User> findUserCount(String phoneDesc) throws Exception;


    //添加短信驗證碼
    @Insert("insert into TB_SmsSend(phone,sendCode,addTime)" +
            "VALUES(#{phoneDesc},#{code},GETDATE()) ")
    int addSmsSendInfo(@Param("phoneDesc") String phoneDesc, @Param("code")String code);

    //获取城市信息，根据等级
    @Select("<script> select * from TB_ProvinceCityArea where 1=1 <if test='level!=null'> and [level] = #{level} </if> " +
            "<if test='pid!=null'> and pid = ${pid} </if>order by id " +
            "</script>")
    List<ProvinceCityArea> findProvinceInfo(@Param("level") Integer level, @Param("pid")Integer pid);


    //获取城市信息，根据等级
    @Select("<script> select * from TB_ProvinceCityArea where [level] = 2 " +
            "<if test = 'pname!=null'> and (pinyin  like concat('%',#{pname},'%') or name  like concat('%',#{pname},'%')) </if>" +
            "</script>")
    List<ProvinceCityArea> findProvinceByNameInfo(String pname);


    //查询banner图
    @Select(" select * from TB_AppBanner order by orderNum ")
    List<AppBanner> findBannerList();

    //添加企业定制的信息
    @Insert("insert into TB_CompanyMade(city,serviceTime,userName,userPhone,remark,addTime)\n" +
            " VALUES(#{city},#{serviceTime},#{userName},#{userPhone},#{remark},GETDATE())")
    int addCompanyInfo(CompanyMade companyMade);

    //查询月卡列表
    @Select("select * from TB_Card order by [month] desc ")
    List<Card> findCardList();

    //添加用户的地址
    @Insert(" insert into TB_Address(userId,province,city,area,address,gender,userName,userPhone,jd,wd,isDefault,addTime)\n " +
            " values(#{userId},#{province},#{city},#{area},#{address},#{gender},#{userName},#{userPhone},#{jd},#{wd},#{isDefault},GETDATE())\n")
    int addTbAddress(Address address);




    //修改用户的默认情况
    @Update("<script> update TB_Address set isDefault = ${isDefault},province =#{province} ,city=#{city},area=#{area}," +
            "address = #{address},gender=#{gender},jd=#{jd},wd=#{wd},userName=#{userName},userPhone=#{userPhone} where 1=1 " +
            "<if test='id!=null'> and id = #{id}</if> <if test='userId != null'> and userId = #{userId} </if></script>")
    int updAddress(Address address);

    @Update(" update TB_Address set isDefault = 0 where userId=#{userId}")
    int updAddressById(String userId);

    @Update(" update TB_Address set isDefault = 1 where userId=#{userId} and id = #{id}")
    int updAddressByIdStatus(@Param("userId") String userId, @Param("id")String id);

    @Delete("delete from TB_Address where id = ${id}")
    int delAddressById(Integer id);


    //查询地址
    @Select("<script> select * from TB_Address where 1=1 <if test='id!=null'> and id = #{id} </if> <if test='userId!=null'> and userId = #{userId}</if> <if test='isDefault!=null'> and isDefault=#{isDefault} </if> order by isDefault desc</script>")
    List<Address> findAddress(@Param("id") String id, @Param("userId") String userId, @Param("isDefault")String isDefault);

    //添加评价
    @Insert("insert into TB_Evaluate(userId,content,star,imgUrl,orderId,addTime,updateTime)\n" +
            "VALUES(#{userId},#{content},#{star},#{imgUrl},#{orderId},GETDATE(),GETDATE())")
    int addEvaluate(Evaluate evaluate);

    //添加城市合伙人
    @Insert("insert into tb_partner(userName,userPhone,email,remark,isMassage,deposit,teamSize,province,city,addTime,isManager)\n" +
            "VALUES(#{userName},#{userPhone},#{email},#{remark},${isMassage},${deposit},${teamSize},#{province},#{city},GETDATE(),${isManager});")
    int addPartnet(Partner partner);

    @Insert("insert into tb_Suggest(content,imgUrl,userId,addTime)\n" +
            "values(#{content},#{imgUrl},#{userId},GETDATE())")
    int addSuggest(Suggest suggest);


    //添加技师招聘
    @Insert(" INSERT INTO TB_Recruit(name,gender,age,phone,address,[year],isUser,workExperience,imgUrl,addTime)\n" +
            " VALUES(#{name},${gender},${age},#{phone},#{address},${year},${isUser},#{workExperience},#{imgUrl},GETDATE())")
    int addRecurit(Recurit recurit);

    /**
     * 分页的数据
     * @param userId
     * @param shopId
     * @return
     */
    @Select("<script> select top 6 * from ("+
    "select row_number() over(order by e.updateTime desc) as rownumber,e.id,e.orderId," +
            "e.userId,o.workerId workId,e.content,e.star,e.imgUrl," +
            "CONVERT(varchar(100), e.updateTime, 23) updateTime," +
            "o.province+' '+o.city+' '+o.area as address,o.serviceNumber,o.payOnline," +
            "p.productName,o.workerName,u.nickName userName,u.imgUrl userImgUrl,sp.shopName," +
            "sp.shopId,e.returnContent from TB_Evaluate e join TB_Order o " +
            "on e.orderId = o.orderId join TB_Product p on o.productId = p.productId join " +
            "TB_User u on e.userId = u.userId  join TB_Shop sp on o.shopId = sp.shopId"+
            " where  1=1 <if test='userId!=null'> and e.userId = #{userId} </if> " +
            "<if test='shopId!=null'> and  o.shopId = #{shopId} </if>" +
            ") t where rownumber &gt; ${page} </script>")
    List<Map<String,Object>> findEvaluateListByUserIdPage(@Param("userId") String userId, @Param("shopId")String shopId,@Param("page")Integer page);



    @Select("<script> select e.id,e.orderId,e.userId,o.workerId workId,e.content,e.star,e.imgUrl,CONVERT(varchar(100), e.updateTime, 23) updateTime,o.province+' '+o.city+' '+o.area as address,o.serviceNumber,o.payOnline,p.productName,o.workerName,u.nickName userName,u.imgUrl userImgUrl,sp.shopName,sp.shopId,e.returnContent from TB_Evaluate e join TB_Order o on e.orderId = o.orderId join TB_Product p on o.productId = p.productId \n" +
            " join TB_User u on e.userId = u.userId " +
            " join TB_Shop sp on o.shopId = sp.shopId  " +
            " where  1=1 <if test='userId!=null'> and e.userId = #{userId} </if> " +
            "<if test='shopId!=null'> and  o.shopId = #{shopId} </if>" +
            " order by e.updateTime desc </script>")
    List<Map<String,Object>> findEvaluateListByUserId(@Param("userId") String userId, @Param("shopId")String shopId);
    //查询评价根据id
    @Select("select e.*,w.userName from TB_Evaluate e join TB_Worker w on e.workId = w.id  where e.id = #{id}")
    Map<String,Object> findEvalById(String id);

    //根据id来删除评价信息
    @Delete("delete from TB_Evaluate where id = #{id}")
    int deleEvaluateListById(String id);

    @Update("update TB_Evaluate set content = #{content},star=#{star},imgUrl=#{imgUrl},updateTime=getdate() where id = #{id}")
    int updEvalById(Evaluate evaluate);

    //查询充值的列表
    @Select("select * from TB_Account order by id")
    List<Map<String,Object>> findAccountInfo();

    //查询充值的列表
    @Select("select t.*,a.sendMoney,a.sendBody from TB_AccountRecord t join TB_Account a on t.accountId = a.id  where t.userId = #{userId} order by t.addTime desc")
    List<Map<String,Object>> findAccountInfoRecord(String userId);

    @Select("select top 1 * from TB_Evaluate where userId = #{userId} order by updateTime desc")
    Map<String,Object> findEvalByUserId(String userId);

    //添加中间表
    @Insert("INSERT into TB_Eval_Choose VALUES(${evalId},${chooseId})")
    int insertEvalChose(@Param("evalId") Integer evalId, @Param("chooseId")Integer chooseId);

    ////删除中间表数据
    @Delete("delete from TB_Eval_Choose where evalId = ${chooseId}")
    int deleteEvalChose(Integer chooseId);

    @Insert("INSERT INTO [TB_School]([city], [area], [money], [personNum], [name], [phone], [email], [msg], [addTime]) " +
            "VALUES (#{city},#{area}, #{money}, #{personNum}, #{name}, #{phone}, #{email},#{msg},getdate())")
    int addSchool(School school);


    @Insert(" INSERT INTO [TB_PayRecord]([orderId], [trade_no], [onlinePay], [payType], [payTime], [buyType], [status], [subject], [body], [userId], [message], [user_money], [startTime], [endTime], [updateTime], [addTime]) \n" +
            " VALUES ( #{orderId},'',#{onlinePay},#{payType}, GETDATE(),#{buyType}, 0,#{subject}, #{body},#{userId}, '',#{userMoney}," +
            "GETDATE(),DATEADD(mm,${month},GETDATE()),GETDATE(), GETDATE())")
    int addPayRecordInfoVip(PayRecord payRecord);

    @Insert(" INSERT INTO [TB_PayRecord]([orderId], [trade_no], [onlinePay], [payType], [payTime], [buyType], [status], [subject], [body], [userId], [message], [user_money], [startTime], [endTime], [updateTime], [addTime],[orderId2]) \n" +
            " VALUES ( #{orderId},'',#{onlinePay},#{payType}, GETDATE(),#{buyType}, 0,#{subject}, #{body},#{userId}, '',#{userMoney},#{startTime},#{endTime}, GETDATE(), GETDATE(),#{orderId2})")
    int addPayRecordInfoCard(PayRecord payRecord);

    @Select("\n" +
            "select o.workerId,o.productName,o.phone,w.phone workerPhone,w.userName workerName,o.address,o.aboutTime,r.buyType,r.status,CONVERT(VARCHAR(20),r.payTime,120) payTime,ISNULL(u.surplusMoney,0) surplusMoney from TB_PayRecord r join TB_Order o on (r.orderId = o.orderId or r.orderId2 = o.orderId) join TB_Worker w on o.workerId = w.workerId \n" +
            "join TB_User u on o.userId = u.userId\n" +
            "where r.orderId = #{orderId}")
    Map<String,Object> findPayRecordById(String orderId);



    @Select("select CONVERT(varchar(12) , addTime, 111 ) addTime,count(1) num,phone from TB_SmsSend \n" +
            "where CONVERT(varchar(12) , addTime, 111 ) = CONVERT(varchar(12) ,GETDATE(), 111 ) \n" +
            "and phone = #{phoneDesc}\n" +
            "GROUP BY  CONVERT(varchar(12) , addTime, 111 ),phone\n")
    List<Map<String,Object>> findSmsCode(@Param("phoneDesc") String phoneDesc);

    //添加优惠券和用户表Id
    @Insert("insert into TB_CouponAndUserId(userId,parentId,isUser,addTime)\n" +
            "values(#{userId},#{parentId},0,GETDATE())")
    int addCouponUserId(@Param("userId") String userId,@Param("parentId")String parentId);

    //查询优惠券
    @Select("select * from TB_CouponAndUserId where  isUser  = 0  and userId = #{userId} ")
    List<Map<String,Object>> findCouponByUserId(String userId);


    @Select("select * from TB_CouponAndUserId where  id= #{id} ")
    Map<String,Object> findCouponById(String id);


    @Update("update TB_CouponAndUserId set isUser = #{isUser},orderId=#{orderId} where id = #{id}")
    int updateTbCoupon(@Param("isUser")String isUser,@Param("id")String id,@Param("orderId") String orderId);

    /**
     * 查询是否有用户十五分钟内没有支付，且给返还优惠券
     * @return
     */
    @Select(" select id from TB_CouponAndUserId where orderId in ( " +
            " select orderId from TB_Order where status = 0 and addTime <= dateadd(minute,-15,GETDATE()) and addTime >= dateadd(DAY,-2,GETDATE())\n" +
            " ) ")
    List<String> findCouponDate();



    @Select("select content from tb_content where isOnline = 1")
    String findContent();

    //查询版本信息
    @Select("select * from tb_version where type = #{version} and status = 1")
    List<Map<String,Object>> findVersion(String version);

    //查询版本信息
    @Update("update TB_User set version = #{version} where userId = #{userId}")
    int updateVersion(@Param("version") String version,@Param("userId") String userId);

    /**
     * 查询优惠券的数量
     * @param userId
     * @return
     */
    @Select("select count(1) from TB_CouponAndUserId where userId = #{userId} and isUser = 0")
    int findCount(@Param("userId") String userId);

    /**
     * 删除用户
     * @param userId
     * @return
     */
    @Delete("delete from TB_User where id = #{userId}")
    int delUserById(String userId);

}
