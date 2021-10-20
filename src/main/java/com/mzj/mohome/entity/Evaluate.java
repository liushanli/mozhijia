package com.mzj.mohome.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

//评价表
@Data
public class Evaluate implements Serializable {

    private Integer id;
    private String userId;
    private String content;
    private String workId;
    private String imgUrl;
    private Date addTime;
    private Date updateTime;
    private Integer star;
    private String orderId;

}
