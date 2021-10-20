package com.mzj.mohome.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CompanyMade implements Serializable {
    private Integer id;
    private String userName;
    private String serviceTime;
    private String userPhone;
    private String remark;
    private Date addTime;
    private String city;
}
