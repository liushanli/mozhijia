package com.mzj.mohome.service;

import com.mzj.mohome.entity.*;
import com.mzj.mohome.vo.InviteImageVo;
import com.mzj.mohome.vo.ReturnOrderStatusVo;
import com.mzj.mohome.vo.UserMoneyRecord;

import java.util.List;
import java.util.Map;

public interface UserService {
    //获取用户名单
     List<User> getUser(Integer id) throws Exception;

     //更改用户信息
    int updUser(Map<String, Object> map);

    //更改用户信息
    String updUserInfo(Map<String, Object> map);

    /**
     *更改用户信息
     * @param map
     * @return
     */
    Map<String,Object> updUserInfoWx(Map<String, Object> map);

    //登录时判断用户是否存在
     List<User> getByUserExist(String phone, String sendCode, String openId, String appleData, String sourceType) throws Exception;

    //是否发送成功
     String SmsSendCode(String phone);

    /**
     * 根据手机号发送验证码
     * @param phone
     * @return
     */
     Map<String,Object> SmsSendCodeVail(String phone);


    Map<String,Object> SmsSendCodeJishi(String phone);

    //获取城市信息，根据等级
     Map<String,Object> findProvinceInfo(String level, String pid);

    /**
     * 查询所有的城市和区域的数据
     * @return
     */
    List<Map<String,Object>> findProvinceList();

    //查询banner图
    List<AppBanner> findBannerList();

    //添加企业定制
    int addCompanyInfo(Map<String, Object> map);

    //查询会员卡列表
    List<Card> findCardList();

    //添加地址
    int addUPDTbAddress(Map<String, Object> map);
    //查询地址
    List<Address> findAddress(Map<String, Object> map);

    //添加评价
    int addEvaluate(Map<String, Object> map);

    //根据用户id来查询评价列表信息
    List<Map<String,Object>> findEvaluateListByUserId(String userId);

    List<Map<String,Object>> findEvaluateListByUserIdNew(String userId, String shopId, Integer pageNum, Integer size);
    //根据条件来查询评价列表信息
    List<Map<String,Object>> findEvaluateListByUserIdCon(Map<String, Object> map);

    //添加城市合伙人
    public int addPartnet(Map<String, Object> map);

    //添加建议
    int addSuggest(Map<String, Object> map);

    //修改地址状态
    int updAddressById(Map<String, Object> map);

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
    int addWhatUserInfo(Map<String, Object> map);

    List<Map<String,Object>> findAccountInfo();

    List<Map<String,Object>> findAccountInfoRecord(Map<String, Object> map);

    //查询手机号是否存在重复
    List<User> findUserPhone(String phone) throws Exception;

    /**
     * 绑定手机号
     * @param paramMap
     * @return
     */
    Map<String,Object> bangDingPhoneInfo(Map<String, String> paramMap);

    //有手机号没有绑定微信用户信息的时候
    int updUserInfoByPhone(Map<String, Object> map);


    int addSchoolInfo(Map<String, Object> map);

    String addPayRecordInfo(Map<String, Object> map);

    Map<String,Object> addPayRecordInfoWx(Map<String, Object> map);

    List<ProvinceCityArea> findProvinceByNameInfo(Map<String, Object> map);

    List<User> getByUserInfoById(String userId, String version);

    //添加一键登录的用户
    List<User> getByUserExistPhone(String phone, String sourceType) throws Exception;


    //添加一键登录的用户
    List<User> getByUserPhone(String phone) throws Exception;

    int addCouponUserId(String userId, String parentId);

    String addUserPhone(String phone, String sourceType) throws Exception;

    List<Map<String,Object>> findCouponByUserId(String userId);

    int updateTbCoupon(String id, String orderId);

    String findContent();


    //查找版本的信息
    List<Map<String,Object>> findVersionList(String type);

    //查找版本的信息
    Map<String,Object> findVersionListNew(String userId, String type, String version);

    /**
     * 获取优惠券的数量
     * @param userId
     * @return
     */
    int findCouponCount(String userId);

    int getByUserInfoByIds();

    /**
     * 注销用户操作
     * @param userId
     * @return
     */
    int delUserById(String userId);

    /**
     * 根据手机号和验证码来获取信息
     * @param phone
     * @param sendCode
     * @return
     */
    int findSms(String phone, String sendCode);

    /**
     * 添加注册者
     * @param register
     * @return
     */
    int addRegister(Register register);

    /**
     * 根据用户id来查询信息
     * @param userId
     * @return
     */
    Register findRegisterByUserId(String userId);

    /**
     * 获取是否隐藏的状态
     * @return
     */
    int getUserStatus();

    /**
     * 根据orderId修改相关信息
     * @param orderId
     * @return
     */
    int updUserOrderRecord(String orderId, String payType);

    /**
     * //status为1，添加，2修改
     * @param userId
     * @param openId
     * @param status
     * @return
     */
    int addUserOpenInfo(String userId, String openId, String status);

    /**
     * 查询服务手机号
     * @return
     */
    String findServicePhone();

    /**
     * 获取二维码的信息
     * @return
     */
    Map<String,Object> findQRImgInfo();

    /**
     * 根据用户Id，获取二维码信息
     * @param inviteImageVo
     * @return
     */
    Map<String,Object> findInviteOrderImg(InviteImageVo inviteImageVo);
    /**
     * 根据orderId，查询退款信息状态
     * @param orderId
     * @return
     */
    List<ReturnOrderStatusVo> queryReturnInfoList(String orderId);

    int addReturnOrderHistory(String orderId, Integer status);


    /**
     * 添加用户钱包信息
     * @return
     */
    int addUserMoneyInfo(UserMoneyRecord record,UserMoneyRecord moneyRecord);

    /**
     * 添加钱包记录
     * @return
     */
    int addUserMoneyRecord(UserMoneyRecord record);

    /**
     * 查询根据用户id来查询是否已存在
     * @return
     */
    UserMoneyRecord findUserMoneyInfo(String userId);

    /**
     * 修改相关信息
     * @return
     */
    int updUserMoneyInfo(UserMoneyRecord record);
    /**
     * 订单确认完成后，则可以随机获得3-5元红包奖励
     */
    int updUserMoney(UserMoneyRecord record);

    List<UserMoneyRecord> findUserMoneyInfoRecord(UserMoneyRecord record);
}
