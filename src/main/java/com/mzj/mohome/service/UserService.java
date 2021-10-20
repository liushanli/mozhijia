package com.mzj.mohome.service;

import com.mzj.mohome.entity.*;

import java.util.List;
import java.util.Map;

public interface UserService {
    //获取用户名单
     List<User> getUser(Integer id) throws Exception;

     //更改用户信息
    int updUser(Map<String,Object> map);

    //更改用户信息
    String updUserInfo(Map<String,Object> map);

    //登录时判断用户是否存在
     List<User> getByUserExist(String phone,String sendCode,String openId,String appleData) throws Exception;

    //是否發送成功
     String SmsSendCode(String phone);

    Map<String,Object> SmsSendCodeJishi(String phone);

    //获取城市信息，根据等级
     Map<String,Object> findProvinceInfo(String level,String pid);

    //查询banner图
    List<AppBanner> findBannerList();

    //添加企业定制
    int addCompanyInfo(Map<String,Object> map);

    //查询会员卡列表
    List<Card> findCardList();

    //添加地址
    int addUPDTbAddress(Map<String,Object> map);
    //查询地址
    List<Address> findAddress(Map<String,Object> map);

    //添加评价
    int addEvaluate(Map<String,Object> map);

    //根据用户id来查询评价列表信息
    List<Map<String,Object>> findEvaluateListByUserId(String userId);

    //根据条件来查询评价列表信息
    List<Map<String,Object>> findEvaluateListByUserIdCon(Map<String,Object> map);

    //添加城市合伙人
    public int addPartnet(Map<String,Object> map);

    //添加建议
    int addSuggest(Map<String,Object> map);

    //修改地址状态
    int updAddressById(Map<String,Object> map);

    //删除地址
    int delAddressById(Integer id);

    //添加技师招聘
    int addRecurit(Recurit recurit);

    //删除评价列表
    int deleEvaluateListById(String id);

    //修改评价列表
    int updEvalById(Evaluate evaluate);

    //查询评价信息
    Map<String,Object> findEvalById(String id);

    //添加手机号和微信绑定的用户
    int addWhatUserInfo(Map<String,Object> map);

    List<Map<String,Object>> findAccountInfo();

    List<Map<String,Object>> findAccountInfoRecord(Map<String,Object> map);

    //查询手机号是否存在重复
    List<User> findUserPhone(String phone) throws Exception;

    //有手机号没有绑定微信用户信息的时候
    int updUserInfoByPhone(Map<String,Object> map);


    int addSchoolInfo(Map<String,Object> map);

    String addPayRecordInfo(Map<String,Object> map);

    List<ProvinceCityArea> findProvinceByNameInfo(Map<String,Object> map);

    List<User> getByUserInfoById(String userId,String version);

    //添加一键登录的用户
    List<User> getByUserExistPhone(String phone) throws Exception;


    //添加一键登录的用户
    List<User> getByUserPhone(String phone) throws Exception;

    int addCouponUserId(String userId,String parentId);

    String addUserPhone(String phone) throws Exception;

    List<Map<String,Object>> findCouponByUserId(String userId);

    int updateTbCoupon(String id);

    String findContent();


    //查找版本的信息
    List<Map<String,Object>> findVersionList(String type);




}
