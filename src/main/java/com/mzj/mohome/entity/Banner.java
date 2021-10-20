package com.mzj.mohome.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Banner implements Serializable {
    private Integer id;
    private String bannerName;
    private String imgUrl;
    private String linkUrl;
    private Integer orderNum;
    private Date addTime;

}
