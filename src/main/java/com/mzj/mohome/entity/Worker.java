package com.mzj.mohome.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class Worker implements Serializable {

    private Integer id;
    ///职工工号
    private String workerId;
    private String userName;
    private String passWord;
    //关联店铺
    private String shopId;
    //所在地区
    private String serviceArea;
    ///职工图片
    private String imgUrl;
    private String phone;
    ///职工介绍
    private String introduce;
    ///服务省份
    private String province;
    ///职工种类、标签
    private String workerTitle;
    ///是否上线，1：上线，0：下线
    private Integer isOnline;
    private double jd;
    private double wd;
    private Date addTime;

    private Date updateTime;

    private double radius;

    private int serviceNum; //服务次數
    private String distanceLen; //距离

    private String shopName; //店铺名

    private Integer sellSum; //销售次数

    private String nickName; //昵称
    private String gender; //性别 1：男 2：女

    private String dateHHmm; //最近可约时间
    private Integer evaluateNum;//好评条数
    private String evaluateNumLv; //好评率
    private Integer star; //星级

    private Integer sellNum;

    private String evalStatus_1; //标签1
    private String evalStatus_2; //标签2
    private String evalStatus_3; //标签3

    private String video;

    private String videoImage;

    private String productId;

    private String idcard; //身份证号

}
