package com.mzj.mohome.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Register implements Serializable {

    private String id;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户身份证
     */
    private String idCard;
    /**
     * 是否有效
     */
    private String recStatus;
}
