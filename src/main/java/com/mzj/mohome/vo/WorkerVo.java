package com.mzj.mohome.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author admin
 * 工作信息
 */
@Data
public class WorkerVo implements Serializable {
    private Integer id;
    private String shopId;
    private String shopName;
    private String workerName;
    private String nickName;
    private String workerId;
    private Integer orderNum;
    private Integer provinceId;
    private String province;
    private Integer cityId;
    private String city;
    private Integer areaId;
    private String area;
    private BigDecimal jd;
    private BigDecimal wd;
    private BigDecimal radius;
    private Date addTime;
    private String status;
    private String imgUrl;
    private String phone;
    private String introduce;
    private String gender;
    private Integer sellSum;
    private String address;
    //邀请码
    private String shopCode;
}
