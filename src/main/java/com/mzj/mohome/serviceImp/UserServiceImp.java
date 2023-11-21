package com.mzj.mohome.serviceImp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mzj.mohome.entity.*;
import com.mzj.mohome.mapper.OrderMapper;
import com.mzj.mohome.mapper.UserMapper;
import com.mzj.mohome.mapper.WorkersMapper;
import com.mzj.mohome.service.UserService;
import com.mzj.mohome.util.RandomUtil;
import com.mzj.mohome.util.SmsSendUtil;
import com.mzj.mohome.util.ToolsUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service(value = "userService")
public class UserServiceImp implements UserService {
    private final static Logger logger = LoggerFactory.getLogger(UserServiceImp.class);
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private WorkersMapper workersMapper;

    @Value("${account}")
    private String account;
    @Value("${password}")
    private String password;
    @Value("${report}")
    private String report;
    @Value("${extend}")
    private String extend;

    @Value("${sendUrl}")
    private String sendUrl;

    @Override
    public List<User> getUser(Integer id) throws Exception {
        return userMapper.getById(id);
    }

    public int getByUserInfoByIds(){
        List<User> list=  userMapper.getUserInfo();
        logger.info("集合为："+list.size());
        if(list.size()>0 && list != null){
            for (User user:list){
                    userMapper.addCouponUserId(user.getUserId(),"");
            }
        }

        return 0;
    }

    //登录时判断用户是否存在
    public List<User> getByUserInfoById(String userId,String version){
        //存在的话，则修改该用户的登陆时间
        List<User> userVoList = new ArrayList<>();
        userVoList = userMapper.getByUserInfoById(userId);
        if(StringUtils.isNotEmpty(version)){
            userMapper.updateVersion(version,userId);
        }
        if(userVoList!=null && userVoList.size()>0){
            for(User user : userVoList){
                switch(user.getLevel()){
                    case 0:
                        user.setLevelName("普通用户");
                        break;
                    case 1:
                        user.setLevelName("普卡会员");
                        break;
                    case 2:
                        user.setLevelName("金卡会员");
                        break;
                    case 3:
                        user.setLevelName("黄金会员");
                        break;
                    case 4:
                        user.setLevelName("白金会员");
                        break;
                    case 5:
                        user.setLevelName("钻石会员");
                        break;
                }
            }
        }
        return userVoList;
    }

    //登录时判断用户是否存在
    public List<User> getByUserExist(String phone,String sendCode,String openId,String appleData,String sourceType) throws Exception{
        //存在的话，则修改该用户的登陆时间
        List<User> userVoList = new ArrayList<>();
        if(StringUtils.isNotEmpty(phone)&&StringUtils.isNotEmpty(sendCode)) {
            List<Map<String, Object>> mapList = userMapper.getByUserInfoExist(phone, sendCode);
            if(mapList.size()>0 && mapList != null){
                userVoList = userMapper.getByUserInfoByPhoneExist(phone);
                if(userVoList.size()<=0 || userVoList == null){
                    String userId = UUID.randomUUID().toString();
                    logger.info("首次登陆添加用户id:{},获取优惠券",userId);
                    int num = userMapper.addUserInfo(userId,phone,RandomUtil.randomName(false,4),sourceType);
                    userMapper.addCouponUserId(userId,null);
                    if(num>0){
                        userVoList = userMapper.getByUserInfoByPhoneExist(phone);
                    }
                }
            }
        }else if(StringUtils.isNotEmpty(openId))
            userVoList = userMapper.getByUserInfoExistWhat(openId);
        else{
            userVoList = userMapper.getByUserInfoExistApple(appleData);
        }
        if(userVoList!=null && userVoList.size()>0){
            for(User user : userVoList){
                switch(user.getLevel()){
                    case 0:
                        user.setLevelName("普通用户");
                        break;
                    case 1:
                        user.setLevelName("'普卡会员'");
                        break;
                    case 2:
                        user.setLevelName("'金卡会员'");
                        break;
                    case 3:
                        user.setLevelName("'黄金会员'");
                        break;
                    case 4:
                        user.setLevelName("'白金会员'");
                        break;
                    case 5:
                        user.setLevelName("'钻石会员'");
                        break;
                }
            }
        }
        return userVoList;
    }
    //登录时判断用户是否存在
    public List<User> getByUserExistPhone(String phone,String sourceType) throws Exception{
        //存在的话，则修改该用户的登陆时间

        List<User> userVoList = userMapper.getByUserInfoByPhoneExist(phone);
        if(userVoList.size()<=0 || userVoList == null){
            String userId = UUID.randomUUID().toString();
            int num = userMapper.addUserInfo(userId,phone,RandomUtil.randomName(false,4),sourceType);
            userMapper.addCouponUserId(userId,null);
            logger.info("首次登陆添加用户id:{},获取优惠券",userId);
            if(num>0){
                userVoList = userMapper.getByUserInfoByPhoneExist(phone);
            }
        }
        if(userVoList!=null && userVoList.size()>0){
            for(User user : userVoList){
                switch(user.getLevel()){
                    case 0:
                        user.setLevelName("普通用户");
                        break;
                    case 1:
                        user.setLevelName("'普卡会员'");
                        break;
                    case 2:
                        user.setLevelName("'金卡会员'");
                        break;
                    case 3:
                        user.setLevelName("'黄金会员'");
                        break;
                    case 4:
                        user.setLevelName("'白金会员'");
                        break;
                    case 5:
                        user.setLevelName("'钻石会员'");
                        break;
                }
            }
        }
        return userVoList;
    }

    public int updUser(Map<String,Object> map){
        User user = new User();
        System.out.println(map.get("surplusMoney"));
        user.setId((map.get("id")!=null && map.get("id")!="")?Integer.parseInt(String.valueOf(map.get("id"))):null);
        user.setNickName((map.get("nickName")!=null && map.get("nickName")!="")?String.valueOf(map.get("nickName")):null);
        user.setImgUrl((map.get("imgUrl")!=null && map.get("imgUrl")!="")?String.valueOf(map.get("imgUrl")):null);
        user.setSurplusMoney((map.get("surplusMoney")!=null && map.get("surplusMoney")!="")?Float.parseFloat(String.valueOf(map.get("surplusMoney"))):null);
        user.setCardId((map.get("cardId")!=null && map.get("cardId")!="")?String.valueOf(map.get("cardId")):null);
        user.setCardName((map.get("cardName")!=null && map.get("cardName")!="")?String.valueOf(map.get("cardName")):null);
        user.setMonth((map.get("month")!=null && map.get("month")!="")?Integer.parseInt(String.valueOf(map.get("month"))):null);
        return userMapper.updUser(user);
    }

    public String updUserInfo(Map<String,Object> map){
        String orderId = null;
        PayRecord payRecord = new PayRecord();
        try{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            orderId = "C"+simpleDateFormat.format(new Date())+ToolsUtil.getFourRandom();
            String status = ToolsUtil.getString(map.get("status"));
            //status为1的话,则是会员套餐，反之的话则是充值返现
            if("1".equals(status)){
                payRecord.setBuyType(2);
                payRecord.setSubject("月卡");
                payRecord.setMonth((map.get("month")!=null && map.get("month")!="")?Integer.parseInt(String.valueOf(map.get("month"))):null);
                payRecord.setOrderId(orderId);
                payRecord.setBody(String.valueOf(map.get("body")));
                payRecord.setOnlinePay(Float.parseFloat(map.get("payOnline").toString()));
                payRecord.setUserId(map.get("userId").toString());
                userMapper.addPayRecordInfoVip(payRecord);
            }else{
                payRecord.setBuyType(3);
                payRecord.setPayType(0);
                payRecord.setUserMoney((map.get("surplusMoney")!=null && map.get("surplusMoney")!="")?Float.parseFloat(String.valueOf(map.get("surplusMoney"))):null);
                payRecord.setSubject("余额充值");
                orderId = "B"+simpleDateFormat.format(new Date())+ToolsUtil.getFourRandom();
                payRecord.setOrderId(orderId);
                payRecord.setBody(String.valueOf(map.get("body")));
                payRecord.setOnlinePay(Float.parseFloat(map.get("payOnline").toString()));
                payRecord.setUserId(map.get("userId").toString());
                  userMapper.addPayRecordInfoCard(payRecord);
            }
        }catch (Exception e){
            orderId = null;
            logger.error("updUserInfo===充值失败，错误信息为：{}",e);
        }
        return orderId;
    }

    public Map<String,Object> updUserInfoWx(Map<String,Object> map){
        Map<String,Object> map1 = new HashMap<>();
        map1.put("success",true);
        map1.put("msg","");
        String orderId = null;
        PayRecord payRecord = new PayRecord();
        try{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            orderId = "C"+simpleDateFormat.format(new Date())+ToolsUtil.getFourRandom();
            String status = ToolsUtil.getString(map.get("status"));
            //status为1的话,则是会员套餐，反之的话则是充值返现
            if("1".equals(status)){
                payRecord.setBuyType(2);
                payRecord.setSubject("月卡");
                payRecord.setMonth((map.get("month")!=null && map.get("month")!="")?Integer.parseInt(String.valueOf(map.get("month"))):null);
                payRecord.setOrderId(orderId);
                payRecord.setBody(String.valueOf(map.get("body")));
                payRecord.setOnlinePay(Float.parseFloat(map.get("payOnline").toString()));
                payRecord.setUserId(map.get("userId").toString());
                userMapper.addPayRecordInfoVip(payRecord);
            }else{
                payRecord.setBuyType(3);
                payRecord.setPayType(0);
                payRecord.setUserMoney((map.get("surplusMoney")!=null && map.get("surplusMoney")!="")?Float.parseFloat(String.valueOf(map.get("surplusMoney"))):null);
                payRecord.setSubject("余额充值");
                orderId = "B"+simpleDateFormat.format(new Date())+ToolsUtil.getFourRandom();
                payRecord.setOrderId(orderId);
                payRecord.setBody(String.valueOf(map.get("body")));
                payRecord.setOnlinePay(Float.parseFloat(map.get("payOnline").toString()));
                payRecord.setUserId(map.get("userId").toString());
                userMapper.addPayRecordInfoCard(payRecord);
            }
        }catch (Exception e){
            map1.put("success",false);
            map1.put("msg","获取订单号失败");
            orderId = null;
            logger.error("updUserInfo===充值失败，错误信息为：{}",e);
        }
        if(StringUtils.isNotEmpty(orderId)){
            map1.put("subject",payRecord.getSubject());
            map1.put("money",payRecord.getOnlinePay());
            map1.put("orderId",orderId);
            map1.put("image","");
            map1.put("userMoney",userMapper.findUserMoney(payRecord.getUserId()));
        }
        return map1;
    }

    //补差价的信息
    public String addPayRecordInfo(Map<String,Object> map){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String orderId = "D"+simpleDateFormat.format(new Date())+ToolsUtil.getFourRandom();
        PayRecord payRecord = new PayRecord();
        //String status = ToolsUtil.getString(map.get("status"));
        payRecord.setBuyType(4);
        payRecord.setPayType(0);
        payRecord.setUserMoney(null);
        payRecord.setSubject("补差价");
        payRecord.setOrderId(orderId);
        payRecord.setOrderId2(String.valueOf(map.get("orderId")));
        payRecord.setBody(String.valueOf(map.get("body")));
        payRecord.setOnlinePay(Float.parseFloat(map.get("payOnline").toString()));
        payRecord.setUserId(map.get("userId").toString());
       userMapper.addPayRecordInfoCard(payRecord);
        return orderId;
    }

    //补差价的信息
    public Map<String,Object> addPayRecordInfoWx(Map<String,Object> map){
        Map<String, Object> map1 = new HashMap<>();
        map1.put("success",true);
        map1.put("msg","");
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String orderId = "D"+simpleDateFormat.format(new Date())+ToolsUtil.getFourRandom();
            PayRecord payRecord = new PayRecord();
            payRecord.setBuyType(4);
            payRecord.setPayType(0);
            payRecord.setUserMoney(null);
            payRecord.setSubject("补差价");
            payRecord.setOrderId(orderId);
            payRecord.setOrderId2(ToolsUtil.getString(map.get("orderId")));
            payRecord.setBody(ToolsUtil.getString(map.get("body")));
            payRecord.setOnlinePay(Float.parseFloat(map.get("payOnline").toString()));
            payRecord.setUserId(ToolsUtil.getString(map.get("userId")));
            map1.put("orderId",orderId);
            map1.put("subject",payRecord.getSubject());
            map1.put("money",payRecord.getOnlinePay());
            map1.put("image",orderMapper.findProductImg(ToolsUtil.getString(map.get("orderId"))));
            map1.put("userMoney",userMapper.findUserMoney(payRecord.getUserId()));
            userMapper.addPayRecordInfoCard(payRecord);
        }catch (Exception e){
            logger.error("该订单：{}，补差价失败：错误信息为：{}",ToolsUtil.getString(map.get("orderId")),e);
            map1.put("success",false);
            map1.put("msg","补差价失败");
        }
        return map1;
    }


    public String SmsSendCode(String phone){
        List<Map<String,Object>> mapList = userMapper.findSmsCode(phone);
        String message = "";
        if(mapList!=null && mapList.size()<=5){
            SmsSendUtil smsSendUtil = new SmsSendUtil();
            String random = smsSendUtil.randomCode();
            //短信下发
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("account",account);//API账号
            map.put("password",password);//API密码
            map.put("msg","【摩之家】您好，您的验证码是"+random);//短信内容
            map.put("phone",phone);//手机号findWorkListByShop
            map.put("report",report);//是否需要状态报告
            map.put("extend",extend);//自定义扩展码
            JSONObject js = (JSONObject) JSONObject.toJSON(map);
            System.out.println("js===="+js);
            logger.info("js===="+js);
            String jsonStr = smsSendUtil.sendSmsByPost(sendUrl,js.toString());
            net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(jsonStr);
            message = (String)jsonObject.get("errorMsg");
            if(StringUtils.isEmpty(message)){
                userMapper.addSmsSendInfo(phone,random);
            }
            //userMapper.addSmsSendInfo(phone,random);
            logger.info("message==="+message);
            System.out.println("message==="+message);
        }else{
            logger.info("已发送五次");
            message = "验证码每天只能发送五次，请您明天再试";
        }

        return message;
    }

    public Map<String,Object> SmsSendCodeJishi(String orderId){

        Map<String,Object> mapperPayRecordById = userMapper.findPayRecordById(orderId);
        SmsSendUtil smsSendUtil = new SmsSendUtil();
        String status = mapperPayRecordById.get("status").toString();
        String buyType = mapperPayRecordById.get("buyType").toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        System.out.println(sdf.format(new Date()));
        //发送短信
        if(status.equals("1") && buyType.equals("1")) {

            String aboutTime =  ToolsUtil.getString(mapperPayRecordById.get("aboutTime"));
            String workerId =  ToolsUtil.getString(mapperPayRecordById.get("workerId"));
            //修改技师的时间
            workersMapper.updateWorkTimeById(aboutTime,workerId,"1");

            //短信下发
            Map<String, Object> map = new HashMap<>();
            map.put("account", "N7792689");//API账号
            map.put("password", "jdzgl5twQ");//API密码
            map.put("msg", "摩之家新订单提醒：\n" +
                    "预约时间：" + mapperPayRecordById.get("aboutTime") +
                    "预约项目：" + mapperPayRecordById.get("productName") +
                    "客户电话：" + mapperPayRecordById.get("phone") +
                    "预约地址：" + mapperPayRecordById.get("address") +
                    "预约技师：" + mapperPayRecordById.get("workerName") +
                    "技师电话：" + mapperPayRecordById.get("workerPhone"));//短信内容
            map.put("phone", "13524908775");//手机号
            map.put("report", report);//是否需要状态报告
            map.put("extend", extend);//自定义扩展码
            JSONObject js = (JSONObject) JSONObject.toJSON(map);
            System.out.println("js====" + js);
            String jsonStr = smsSendUtil.sendSmsByPost("http://smssh1.253.com/msg/send/json", js.toString());
            net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(jsonStr);
            String message = (String) jsonObject.get("errorMsg");
            System.out.println("message===" + message);
        }
        return mapperPayRecordById;
    }



/*    public static void main(String[] args){
        UserServiceImp userServiceImp = new UserServiceImp();
        userServiceImp.SmsSendCodeJishi("13621883997");
    }*/

    //获取城市信息，根据等级
    @Cacheable(value = "users")
    public Map<String,Object> findProvinceInfo(String level,String pid){

        Integer level_1 = StringUtils.isEmpty(level)?null:Integer.parseInt(level);
        Integer pid_1 = StringUtils.isEmpty(pid)?null:Integer.parseInt(pid);
        Map<String,Object> listMap = new HashMap<String,Object>();
        List<ProvinceCityArea> provinceCityAreaList = userMapper.findProvinceInfo(level_1,pid_1);
        listMap.put("provinceList",provinceCityAreaList);
        return listMap;
    }

    public List<AppBanner> findBannerList(){
        return userMapper.findBannerList();
    }

    public int addCompanyInfo(Map<String,Object> map){
        CompanyMade companyMade = new CompanyMade();
        companyMade.setRemark(String.valueOf(map.get("remark")));
        companyMade.setServiceTime(String.valueOf(map.get("serviceTime")));
        companyMade.setUserName(String.valueOf(map.get("userName")));
        companyMade.setUserPhone(String.valueOf(map.get("userPhone")));
        companyMade.setCity(String.valueOf(map.get("city")));
        int count = userMapper.addCompanyInfo(companyMade);
        return count;
    }

    public List<Card> findCardList(){
        List<Card> cardList = userMapper.findCardList();
        return cardList;
    }

    //添加地址
    public int addUPDTbAddress(Map<String,Object> map){
        Address address = new Address();
        address.setId((map.get("id")!=null && map.get("id")!="" ) ?Integer.parseInt(String.valueOf(map.get("id"))) : null);
        address.setAddress(String.valueOf(map.get("address")));
        address.setArea(String.valueOf(map.get("area")));
        address.setCity(String.valueOf(map.get("city")));
        address.setUserId(String.valueOf(map.get("userId")));
        address.setJd(String.valueOf(map.get("jd")));
        address.setWd(String.valueOf(map.get("wd")));
        address.setGender(map.get("gender")!=null && map.get("gender")!="" ?Integer.parseInt(String.valueOf(map.get("gender"))) : null);
        address.setUserPhone(String.valueOf(map.get("userPhone")));
        address.setUserName(String.valueOf(map.get("userName")));
        address.setIsDefault(Integer.parseInt(String.valueOf(map.get("isDefault"))));
        address.setProvince(String.valueOf(map.get("provice")));
        int num = 0;
        //第一步，先把其他的地址的默认或者地址信息修改
        userMapper.updAddressById(address.getUserId());
        if(address.getId() != null){
            //第二步：把用户上一次使用的地址更改为默认状态
            num = userMapper.updAddress(address);
        }else{
            num = userMapper.addTbAddress(address);
        }
        return num;
    }

    @Override
    public List<Address> findAddress(Map<String,Object> map) {
        String id = ((map.get("id")!=null && map.get("id")!="")?String.valueOf(map.get("id")):null);
        String userId = ((map.get("userId")!=null && map.get("userId")!="")?String.valueOf(map.get("userId")):null);
        String isDefault = (map.get("isDefault")!="" && map.get("isDefault")!=null)?String.valueOf(map.get("isDefault")):null;
        return userMapper.findAddress(id,userId,isDefault);
    }

    //添加评价
    public int addEvaluate(Map<String,Object> map){
        Evaluate evaluate = new Evaluate();
        int num = 0;
        try {
            evaluate.setUserId(String.valueOf(map.get("userId")));
            evaluate.setContent(String.valueOf(map.get("content")));
            evaluate.setImgUrl(String.valueOf(map.get("imgUrl")));
            evaluate.setStar((Integer)(map.get("score")));
            evaluate.setWorkId(String.valueOf(map.get("workerId")));
            evaluate.setOrderId(String.valueOf(map.get("orderId")));
            if(ToolsUtil.getString(map.get("id"))!=null){
                evaluate.setId(Integer.parseInt(String.valueOf(map.get("id"))));
                num = userMapper.updEvalById(evaluate);
                userMapper.deleteEvalChose(evaluate.getId());
            }else{
                num = userMapper.addEvaluate(evaluate);
               Map<String,Object> map1 = userMapper.findEvalByUserId(String.valueOf(map.get("userId")));
                String str = String.valueOf(map.get("chooseId"));
                if(StringUtils.isNotEmpty(str)){
                    String[] strSlit = str.split(",");
                    for(String s_1 :strSlit){
                        userMapper.insertEvalChose(Integer.parseInt(map1.get("id").toString()),Integer.parseInt(s_1));
                    }
                }
            }
            Order order = new Order();
            order.setStatus(9);
            order.setOrderId(evaluate.getOrderId());
            logger.info("该订单"+order.getOrderId()+"修改为9");
            orderMapper.updateOrderInfoStatus(order);
        }catch (Exception e){
            System.out.println("评价=="+e.getMessage());
            num = 0;
        }
        return num;
    }



    public int addPartnet(Map<String,Object> map){
        Partner partner = new Partner();
        try {
            partner.setUserName(String.valueOf(map.get("userName")));
            partner.setUserPhone(String.valueOf(map.get("userPhone")));
            partner.setEmail(String.valueOf(map.get("email")));
            partner.setRemark(String.valueOf(map.get("remark")));
            partner.setProvince(String.valueOf(map.get("province")));
            partner.setCity(String.valueOf(map.get("city")));
            partner.setIsManager(Integer.parseInt(String.valueOf(map.get("isManager"))));
            partner.setIsMassage((Integer)(map.get("isMassage")));
            partner.setDeposit((Integer)((map.get("deposit"))));
            partner.setTeamSize(String.valueOf(map.get("teamSize")));
            return userMapper.addPartnet(partner);
        }catch (Exception e){
            return 0;
        }
    }
    public  int addSuggest(Map<String,Object> map){
        Suggest suggest = new Suggest();
        try {
            suggest.setUserId(String.valueOf(map.get("userId")));
            suggest.setContent(String.valueOf(map.get("content")));
            suggest.setImgUrl(String.valueOf(map.get("imgUrl")));
            return userMapper.addSuggest(suggest);
        }catch (Exception e){
            return 0;
        }
    }

    public int updAddressById(Map<String,Object> map){
        try {
            String userId = String.valueOf(map.get("userId"));
            String id = String.valueOf(map.get("id"));
            userMapper.updAddressById(userId);
            return userMapper.updAddressByIdStatus(userId,id);
        }catch (Exception e){
            return 0;
        }
    }
    //刪除
    public int delAddressById(Integer id){
        try {
            return userMapper.delAddressById(id);
        }catch (Exception e){
            return 0;
        }
    }

    public int addRecurit(Recurit recurit){
        try{
            int number = userMapper.addRecurit(recurit);
            return number;
        }catch (Exception e) {
            System.out.println("添加技师招聘：" + e.getMessage());
            return 0;
        }
    }

    public List<Map<String,Object>> findEvaluateListByUserId(String userId){
            try {
                return userMapper.findEvaluateListByUserId(userId,null);
            }catch (Exception e){
                System.out.println("查询评价列表信息报错："+e.getMessage());
                return  null;
            }
    }

    public List<Map<String,Object>> findEvaluateListByUserIdCon(Map<String,Object> map){
        try {

            String shopId =  ToolsUtil.getString(map.get("shopId"));
            Integer page = ToolsUtil.getString(map.get("page"))!=null?(Integer)map.get("page"):1;
            page = (page-1)*6;
            return userMapper.findEvaluateListByUserIdPage(null,shopId,page);
        }

        catch (Exception e){
            System.out.println("查询评价列表信息报错："+e.getMessage());
            return  null;
        }
    }

    //删除评价列表
    public int deleEvaluateListById(String id){
        try {
            return userMapper.deleEvaluateListById(id);
        }catch (Exception e){
            System.out.println("删除评价列表："+e.getMessage());
            return 0;
        }
    }

    //修改评价列表
    public int updEvalById(Evaluate evaluate){
        try {
            return userMapper.updEvalById(evaluate);
        }catch (Exception e){
            System.out.println("修改评价列表："+e.getMessage());
            return 0;
        }
    }

    public Map<String,Object> findEvalById(String id){
        try {
            return userMapper.findEvalById(id);
        }catch (Exception e){
            System.out.println("修改评价列表："+e.getMessage());
            return null;
        }
    }

    //添加手机号和微信绑定的用户
    public int addWhatUserInfo(Map<String,Object> map){
        try{
            String openId = ToolsUtil.getString(map.get("openId"));
            String imgUrl = ToolsUtil.getString(map.get("imgUrl"));
            String nickName = ToolsUtil.getString(map.get("nickName"));
            String sex = ToolsUtil.getString(map.get("sex"));
            String phone = ToolsUtil.getString(map.get("phone"));
            String appleData = ToolsUtil.getString(map.get("appleData"));
            int num;
            String sourceType = "";
            //来源类型
            if(map.get("sourceType")!=null){
                sourceType = String.valueOf(map.get("sourceType"));
            }
            String userId = UUID.randomUUID().toString();
            if(StringUtils.isNotEmpty(appleData) && appleData != null){
                num = userMapper.addAppleUserInfo(userId,appleData, RandomUtil.randomName(false,5),sourceType);
            }else{
                num = userMapper.addWhatUserInfo(userId,phone,nickName,imgUrl,openId,sex,sourceType);
            }
            return num;
        }catch (Exception e){
            System.out.println("添加错误："+e.getMessage());
            return 0;
        }
    }





    public List<Map<String,Object>> findAccountInfo(){
        return userMapper.findAccountInfo();
    }

    public List<Map<String,Object>> findAccountInfoRecord(Map<String,Object> map){
        String userId = ToolsUtil.getString(map.get("userId"));
        return userMapper.findAccountInfoRecord(userId);
    }

    public List<User> findUserPhone(String phone) throws Exception{
        List<User> users = userMapper.findUserCount(phone);
        return (users.size()>0 && users != null)?users:null;
    }
    public int updUserInfoByPhone(Map<String,Object> map){
        try{
            String openId = ToolsUtil.getString(map.get("openId"));
            String imgUrl = ToolsUtil.getString(map.get("imgUrl"));
            String nickName = ToolsUtil.getString(map.get("nickName"));
            String sex = ToolsUtil.getString(map.get("sex"));
            String phone = ToolsUtil.getString(map.get("phone"));
            String appData = ToolsUtil.getString(map.get("appleData"));
            int num = 0;
            if(StringUtils.isNotEmpty(appData) && appData != null){
                num =  userMapper.updateUserInfoByApple(appData,phone);
            }else{
                num = userMapper.updUserInfoByPhone(phone,nickName,imgUrl,openId,sex);
            }
            return num;
        }catch (Exception e){
            System.out.println("添加错误："+e.getMessage());
            return 0;
        }
    }

    public int addSchoolInfo(Map<String,Object> map){
        try{
            School school = new School();
            school.setArea(ToolsUtil.getString(map.get("area")));
            school.setCity(ToolsUtil.getString(map.get("city")));
            school.setEmail(ToolsUtil.getString(map.get("email")));
            school.setMoney(ToolsUtil.getString(map.get("money")));
            school.setMsg(ToolsUtil.getString(map.get("msg")));
            school.setPersonNum(ToolsUtil.getString(map.get("personNum")));
            school.setName(ToolsUtil.getString(map.get("name")));
            school.setPhone(ToolsUtil.getString(map.get("phone")));
            return userMapper.addSchool(school);
        }catch (Exception e){
            System.out.println("添加错误："+e.getMessage());
            return 0;
        }
    }

    public List<ProvinceCityArea> findProvinceByNameInfo(Map<String,Object> map){
        String pname = ToolsUtil.getString(map.get("pname"));
        return userMapper.findProvinceByNameInfo(pname);
    }


    public int addPhoneUserInfo(Map<String,Object> map){
        try{
            String openId = "";
            String imgUrl = "";
            String nickName = RandomUtil.randomName(false,4);
            String sex = "";
            String phone = ToolsUtil.getString(map.get("phone"));
            String userId = UUID.randomUUID().toString();
            String sourceType = ToolsUtil.getString(map.get("sourceType"));
            int num = userMapper.addWhatUserInfo(userId,phone,nickName,imgUrl,openId,sex,sourceType);
            return num;
        }catch (Exception e){
            System.out.println("添加错误："+e.getMessage());
            return 0;
        }
    }

    public List<User> getByUserPhone(String phone) throws Exception {
        List<User> users = userMapper.getByUserExist(phone);
        return users;
    }

    public int addCouponUserId(String userId,String parentId){
        //分享邀请者
        if(StringUtils.isNotEmpty(parentId)){
            userMapper.addCouponUserId(parentId,null);
        }
        //分享被邀请者
        return userMapper.addCouponUserId(userId,parentId);
    }

    //登录时判断用户是否存在
    public String addUserPhone(String phone,String sourceType) throws Exception{
        //存在的话，则修改该用户的登陆时间
        String userId = UUID.randomUUID().toString();
        int num = userMapper.addUserInfo(userId,phone,RandomUtil.randomName(false,4),sourceType);
        logger.info("首次登陆添加用户id:{},获取优惠券",userId);
        userMapper.addCouponUserId(userId,null);
        if(num>0){
           List<User> user = userMapper.getByUserInfoByPhoneExist(phone);
           userId = user.get(0).getUserId();
        }

        return userId;
    }

    public List<Map<String,Object>> findCouponByUserId(String userId){
        List<Map<String,Object>> mapList = userMapper.findCouponByUserId(userId);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(mapList!=null && mapList.size()>0){
            for(Map<String,Object> map1 : mapList){
                if(map1.get("addTime")!=null && map1.get("addTime")!=""){
                    String payTime = simpleDateFormat.format(map1.get("addTime"));
                    map1.put("addTime",payTime);
                }
                map1.put("couponYuan","50元优惠券");
            }
        }
        return mapList;
    }

    public int updateTbCoupon(String id,String orderId){
        int count = userMapper.updateTbCoupon("1",id,orderId);
        if(count>0){
            Map<String,Object> mapList = userMapper.findCouponById(id);
            userMapper.addCouponUserId(mapList.get("userId").toString(),"");
        }
        return count;
    }

    public String findContent(){
        return userMapper.findContent();
    }

    public List<Map<String,Object>> findVersionList(String type){
        return userMapper.findVersion(type);
    }

    //查找版本的信息
    public Map<String,Object> findVersionListNew(String userId,String type,String version){
        logger.info("查询用户的版本，是否更新");
        int count = userMapper.findVersionNew(userId,type);
        Map<String,Object> map1 = userMapper.findVersionInfo(version,type);
        Map<String,Object> map = new HashMap<>();
        if(count>0 && map1 != null){
            logger.info("该用户{}，可以更新,更新中间表",userId);
            map.put("success",true);
            map.put("url",map1.get("url"));
            userMapper.updUserVersion(userId,String .valueOf(map1.get("version")));
        }else if(count<=0 && map1 != null ){
            logger.info("该用户{}，可以更新，把他添加到中间表",userId);
            map.put("url",map1.get("url"));
            map.put("success",true);
            userMapper.addUserVersion(userId,version);
        }else{
            logger.info("该用户{}，不可以更新，没有更新地址",userId);
            map.put("success",false);
        }
        return map;
    }



    public int findCouponCount(String userId){
        return userMapper.findCount(userId);
    }

    public int delUserById(String userId){
        logger.info("删除用户Id:{}",userId);
        return userMapper.delUserById(userId);
    }
    public  int findSms(String phone,String sendCode){
        logger.info("注销手机号:{},验证码：{}",phone,sendCode);
        return userMapper.findSms(phone,sendCode);
    }

    public int addRegister(Register register){
        register.setId(UUID.randomUUID().toString().toUpperCase());
        return userMapper.addRegisterInfo(register);
    }

    public Register findRegisterByUserId(String userId){
        return userMapper.findRegisterByUserId(userId);
    }

    public int getUserStatus(){
        return  userMapper.getUserStatus();
    }

    public int updUserOrderRecord(String orderId){
        PayRecord payRecord = userMapper.findPayRecord(orderId);
        logger.info("获取订单信息为：{}", JSON.toJSONString(payRecord));
        User user = new User();
        user.setUserId(payRecord.getUserId());
        /**
         * payType支付类型，1：余额支付，2：微信，3：支付宝，
         * buyType：购买类型，1：下单，2：购买会员卡，3：充值余额，4：补差价
         */
        //购买类型为1或4，且支付类型为 1,则进行相减余额信息
        if((payRecord.getBuyType()==1 || payRecord.getBuyType()==4)
                && payRecord.getPayType() == 1){
            user.setSurplusMoney(-payRecord.getOnlinePay());
            return userMapper.updUser(user);
        }else if(payRecord.getBuyType() == 2){
            //购买类型为2，则进行更新用户的会员卡相关信息
            Card card = userMapper.findCardInfo(payRecord.getSubject());
            user.setCardId(card.getCardId());
            user.setCardName(card.getCardName());
            user.setMonth(card.getMonth());
            user.setLevel(1);
            return userMapper.updUser(user);
        }else if(payRecord.getBuyType() == 3){
            //购买类型为3，则进行相加余额信息
            user.setSurplusMoney(payRecord.getUserMoney());
            return userMapper.updUser(user);
        }else{
            return 1;
        }
    }
}
