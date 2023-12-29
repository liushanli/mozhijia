package com.mzj.mohome.vo;

import lombok.Data;

import java.util.Date;

@Data
public class NoticeMsgVo {
    private Integer id ;
    private String title;
    private String content;
    private Date addTime;
    private Date updateTime;

    private String shopId;
    private String sendType;
    private String updateTimeStr;
}
