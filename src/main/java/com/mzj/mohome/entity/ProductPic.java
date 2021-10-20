package com.mzj.mohome.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
public class ProductPic implements Serializable {
    private Integer id;
    private String productId;
    private String imgUrl;
    private String imgType;
    private Date addTime;

    public ProductPic() {
    }

    @Override
    public String toString() {
        return "ProductPic{" +
                "id=" + id +
                ", productId='" + productId + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", imgType='" + imgType + '\'' +
                ", addTime=" + addTime +
                '}';
    }
}
