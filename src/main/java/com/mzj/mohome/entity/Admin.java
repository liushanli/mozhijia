package com.mzj.mohome.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
public class Admin implements Serializable {
    private Integer id;
    private String adminId;
    private String shopid;
    private String userName;
    private String passWord;
    private String phone;
    private String email;
    private Integer levelId;
    private Date addTime;

    public Admin() {
    }

    public Admin(Integer id, String adminId, String shopid, String userName, String passWord, String phone, String email, Integer levelId, Date addTime) {
        this.id = id;
        this.adminId = adminId;
        this.shopid = shopid;
        this.userName = userName;
        this.passWord = passWord;
        this.phone = phone;
        this.email = email;
        this.levelId = levelId;
        this.addTime = addTime;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", adminId='" + adminId + '\'' +
                ", shopid='" + shopid + '\'' +
                ", userName='" + userName + '\'' +
                ", passWord='" + passWord + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", levelId=" + levelId +
                ", addTime=" + addTime +
                '}';
    }
}
