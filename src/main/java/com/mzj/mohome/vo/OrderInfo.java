package com.mzj.mohome.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderInfo implements Serializable {
    /**
     * 订单标题
     */
    private String title;
    /**
     * 商单号
     */
    private String orderNo;
    /**
     * 总价
     */
    private Integer totalFee;

    private String openId;

    private String code;

    private String userId;
}
