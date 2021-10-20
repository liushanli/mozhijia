package com.mzj.mohome.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Data
public class Address implements Serializable {
    private Integer id;
    private String userId;
    private String province;
    private String city;
    private String area;
    private String address;
    private String jd;
    private String wd;
    private Integer isDefault;
    private Date addTime;
    private Integer gender;
    private String userName;
    private String userPhone;
}
