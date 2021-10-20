package com.mzj.mohome.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class School implements Serializable {
    private Integer id;
    private String city;
    private String area;
    private String money;
    private String personNum;
    private String name;
    private String phone;
    private String email;
    private String msg;
    private String addTime;
}
