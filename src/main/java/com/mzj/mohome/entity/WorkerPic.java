package com.mzj.mohome.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class WorkerPic implements Serializable{
    private Integer id;
    private String workerId;
    private String imgUrl;
    private String imgType;
    private Integer orderNum;
    private Date addTime;
}
