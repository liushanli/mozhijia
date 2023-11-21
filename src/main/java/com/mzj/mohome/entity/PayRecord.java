package com.mzj.mohome.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class PayRecord implements Serializable {

    private Integer id;

    private String orderId;

    private String tradeNo;

    private Float onlinePay;

    private int payType;

    private String payTime;

    private int buyType;

    private int status;

    private String subject;

    private String body;

    private String userId;

    private String message;

    private Float userMoney;
    private Date startTime;
    private Date endTime;
    private Date updateTime;
    private Date addTime;

    private Integer month;

    private String orderId2;

    private String sourceType;

    private String orderPayType;
}
