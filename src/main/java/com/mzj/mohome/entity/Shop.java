package com.mzj.mohome.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Data
public class Shop implements Serializable {
    private Integer id;
    private String shopId;
    /// <summary>
///店家名称
/// <summary>
    private String shopName;
    /// <summary>
///订购须知
/// <summary>
    private String orderInfo;
    /// <summary>
///店家介绍
/// <summary>
    private String shopIntroduce;
    private String shopPhone;
    private String shopEmail;
    /// <summary>
///排序
/// <summary>
    private Integer orderNum;
    /// <summary>
///客服电话
/// <summary>
    private String servicePhone;
    /// <summary>
///订单处理人电话
/// <summary>
    private String excutePhone;
    /// <summary>
///停业开始时间
/// <summary>
    private String StopStartTime;
    /// <summary>
///停业结束时间
/// <summary>
    private String StopEndTime;
    /// <summary>
///服务区域
/// <summary>
    private String serviceArea;
    /// <summary>
///服务交通费
/// <summary>
    private Integer trafficPice;
    /// <summary>
///交通费开始时间
/// <summary>
    private String trafficStartTime;
    /// <summary>
///交通费结束时间
/// <summary>
    private String trafficEndTime;
    /// <summary>
///接单方式，0:用户选人
/// <summary>
    private Integer receiveType;
    /// <summary>
///到达时间
/// <summary>
    private String arriveTimeFloat;
    /// <summary>
///客服头像
/// <summary>
    private String serviceLogoUrl;
    private Date addTime;
    private Date updateTime;
    private String returnInfo;
    private String shopLogoUrl; //店面图
    private String shopCode; //邀请码
}
