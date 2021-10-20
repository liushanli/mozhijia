package com.mzj.mohome.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

//会员卡
@Data
public class Card implements Serializable {
    private Integer id;
    private String cardId;
    private String cardName;
    private String imgUrl;
    private String describe;
    private float price;
    private Date addTime;
    private Integer month;
}
