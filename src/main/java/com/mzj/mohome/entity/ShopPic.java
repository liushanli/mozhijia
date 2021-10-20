package com.mzj.mohome.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class ShopPic {
    private Integer id;
    private String shopId;
    private String imgUrl;
    /// <summary>
///图片类型，0：客服头像,1：商家相册
/// <summary>
    private Integer imgType;
    private Date addTime;


    public ShopPic() {
    }

    @Override
    public String toString() {
        return "ShopPic{" +
                "id=" + id +
                ", shopId='" + shopId + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", imgType=" + imgType +
                ", addTime=" + addTime +
                '}';
    }
}
