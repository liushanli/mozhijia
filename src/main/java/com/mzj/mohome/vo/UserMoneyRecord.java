package com.mzj.mohome.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author admin
 * 用户钱包相关信息
 */
@Data
public class UserMoneyRecord {
    private String userId;
    private String userName;
    private String submitName;
    private Integer money;
    private Integer type;
    private Date addTime;
    private Date updateTime;
    private Integer moneyType;
    private Integer submitStatus;
    private String submitStatusName;
    private String orderId;
    private String phone;
    private Integer submitType;
    private String addTimeStr;
    private Integer page;
}
