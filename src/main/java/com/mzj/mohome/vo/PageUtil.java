package com.mzj.mohome.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class PageUtil implements Serializable {

    private Integer size;
    private Integer page;
    private String shopId;
    private String workerId;
    private String shopName;
    private String userName;
    private String orderBy; //排序
    private String onLine;//是否在线1：在，0不在线
    private String productId;
    private String city;
    private String evalNmsDesc;
    private String genderDesc;
    private String jd;
    private String wd;
    private String picStatus;
}
