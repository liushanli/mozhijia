package com.mzj.mohome.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

//技师招聘
@Data
public class Recurit implements Serializable {
    private Integer id;
    private Integer gender;
    private String name;
    private String address;
    private String phone;
    private Integer year;
    private String imgUrl;
    private Integer isUser;
    private Integer age;
    private String workExperience;
    private Date addTime;
    private String province;
    private String city;
}
