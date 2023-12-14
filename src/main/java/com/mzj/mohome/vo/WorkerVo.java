package com.mzj.mohome.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class WorkerVo implements Serializable {
    //id,shopId,shopName,workerId,workerName,orderNum,productId,province," +
    //            " city,cityId,area,areaId,jd,wd,radius,addTime
    private Integer id;
    private String shopId;
    private String shopName;
    private String workerName;
    private String workerId;
    private Integer orderNum;
    private String provinceId;
    private String province;
    private String cityId;
    private String city;
    private String areaId;
    private String area;
    private Double jd;
    private Double wd;
    private Double radius;
    private Date addTime;
    private String status;
    private String address;
}
