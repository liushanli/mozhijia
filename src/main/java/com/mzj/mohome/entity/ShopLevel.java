package com.mzj.mohome.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class ShopLevel {
    private Integer id;
    private Integer levelId;
    private String levelCH;
    private Date addTime;

    public ShopLevel() {
    }

    @Override
    public String toString() {
        return "ShopLevel{" +
                "id=" + id +
                ", levelId=" + levelId +
                ", levelCH='" + levelCH + '\'' +
                ", addTime=" + addTime +
                '}';
    }
}
