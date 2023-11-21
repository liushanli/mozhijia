package com.mzj.mohome.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class Order implements Serializable {

    private Integer id;

    private String orderId;
    /// <summary>
///商家id
/// <summary>
    private String shopId;
    /// <summary>
///产品id
/// <summary>
    private String productId;
    /// <summary>
///用户id
/// <summary>
    private String userId;
    /// <summary>
///职工id
/// <summary>
    private String workerId;
    /// <summary>
///产品名称
/// <summary>
    private String productName;
    /// <summary>
///产品原价格
/// <summary>
    private Float oldPrice;
    /// <summary>
///现价格
/// <summary>
    private Float price;
    /// <summary>
///会员价
/// <summary>
    private Float memberPrice;
    /// <summary>
///团购价
/// <summary>
    private Float groupPrice;

    //在线支付价格
    private Float payOnline;
    /// <summary>
///0：待付款，1：已付款，2：付款失败，3：待接单，4：已接单，5：订单完成，6：申请退款，7：退款中，8：退款成功，404：取消订单
/// <summary>
    private Integer status;
    private String userName;
    private String province;
    private String city;
    private String area;
    private String address;
    /// <summary>
///联系人
/// <summary>
    private String phone;
    /// <summary>
///经度
/// <summary>
    private Float jd;
    /// <summary>
///纬度
/// <summary>
    private Float wd;
    private String detail;
    /// <summary>
///交通费
/// <summary>
    private Integer trafficPrice;
    /// <summary>
///备注
/// <summary>
    private String remarks;
    /// <summary>
///服务次数
/// <summary>
    private Integer serviceNumber;
    /// <summary>
///支付方式，1：余额，2：微信，3：支付宝
/// <summary>
    private String orderPayType;
    /// <summary>
///预约时间
/// <summary>
    private Date aboutTime;
    /// <summary>
///支付时间
/// <summary>
    private Date orderPayTime;
    /// <summary>
///商家接单时间
/// <summary>
    private Date shopReceiveTime;
    /// <summary>
///服务结束时间
/// <summary>
    private Date serviceCompleteTime;
    /// <summary>
///职工确认完成时间
/// <summary>
    private Date workconfirmTime;
    /// <summary>
///订单处理人id
/// <summary>
    private String orderReviceId;
    /// <summary>
///订单处理人名字
/// <summary>
    private String orderReviceName;
    /// <summary>
///接订单员工
/// <summary>
    private String workerName;
    /// <summary>
///接订单员工手机号
/// <summary>
    private String workerPhone;
    /// <summary>
///添加时间,即下单时间
/// <summary>
    private Date addTime;
    //商家备注
    private String shopRemarks;

    //返回类型
    private Integer returnType;
    //返回金额
    private Double returnMoney;

    private String productImgUrl;

    //订单退款原因
    private String returnReason;
    private Double fillMoney;
    //订单流水号
    private String tradeNo;
    //秒杀价
    private Float bargainPrice;
    //来源，1：公众号，为空或其他是App
    private String sourceType;
}
