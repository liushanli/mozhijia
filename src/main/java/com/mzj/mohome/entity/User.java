package com.mzj.mohome.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;


@Data
public class User implements Serializable {
    private Integer id;
    private String userId;
    private String userName;
    private String passWord;
    private String openId;
    private String nickName;
    /// <summary>
///微信头像
/// <summary>
    private String avatarUrl;
    private String phone;
    private Integer age;
    private String job;
    /// <summary>
///0：男，1：女
/// <summary>
    private Integer sex;
    private String imgUrl;
    private String token;
    /// <summary>
///登录方式，0：默认用户名登录，1：微信登录
/// <summary>
    private Integer loginType;
    private Date loginTime;
    //默认0,1：黑名单，不准下单
    private int isBlackList;
    private Float jd; //经度
    private Float wd; //纬度
    private Float radius; //半径
    private Float allMoney; //账户余额
    private Float useMoney;//使用余额
    private Float surplusMoney; //剩余金额
    private String cardId; //会员卡id
    private String cardName;//会员卡名称
    private Date cardStartTime;//会员卡开始时间
    private Date cardEndTime; //会员卡结束时间
    private Date addTime;//添加时间
    private Integer month; //代表买几个月的会员卡
    private Integer level;//会员等级
    private String levelName;//会员等级名称

    private String cardStatus;


}
