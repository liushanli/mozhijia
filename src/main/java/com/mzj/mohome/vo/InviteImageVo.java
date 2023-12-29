package com.mzj.mohome.vo;

import lombok.Data;

import java.util.Date;

/**
 * 二维码Vo
 */
@Data
public class InviteImageVo {
    private Integer id;
    /**
     * 邀请码
     */
    private String shopCode;
    /**
     * 二维码保存路径
     */
    private String imgPath;
    /**
     * 用户唯一id
     */
    private String shopId;
    /**
     * 类型：1：店铺，2：用户邀请码，3：技师邀请码
     */
    private String type;
    /**
     * 用来判断用户的路径是否发生变更
     */
    private String imgUrl;

    private Date updateTime;

    private Date addTime;
}
