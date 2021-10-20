package com.mzj.mohome.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
public class ManageShopTime implements Serializable {
    private Integer id;
    private String shopId;
    private Date stopStartTime;
    private Date stopEndTime;
    private Date addTime;

    public ManageShopTime() {
    }

    @Override
    public String toString() {
        return "ManageShopTime{" +
                "id=" + id +
                ", shopId='" + shopId + '\'' +
                ", stopStartTime=" + stopStartTime +
                ", stopEndTime=" + stopEndTime +
                ", addTime=" + addTime +
                '}';
    }
}
