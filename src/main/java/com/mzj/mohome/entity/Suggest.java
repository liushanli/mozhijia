package com.mzj.mohome.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Suggest implements Serializable {
    private Integer id;
    private String content;
    private String imgUrl;
    private String userId;
    private Date addTime;

}
