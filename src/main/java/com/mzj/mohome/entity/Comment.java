package com.mzj.mohome.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
public class Comment implements Serializable {
    private Integer id;
    private String commentId;
    private String userId;
    private String orderId;
    private Integer commentLevel;
    private String commentText;
    private String province;
    private String city;
    private String area;
    private String productName;
    private String userName;
    private Date addTime;


    public Comment() {
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", commentId='" + commentId + '\'' +
                ", userId='" + userId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", commentLevel=" + commentLevel +
                ", commentText='" + commentText + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", area='" + area + '\'' +
                ", productName='" + productName + '\'' +
                ", userName='" + userName + '\'' +
                ", addTime=" + addTime +
                '}';
    }
}
