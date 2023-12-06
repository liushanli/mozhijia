package com.mzj.mohome.vo;

import com.mzj.mohome.entity.Order;


import java.io.Serializable;

public class OrderVo extends Order implements Serializable {
    //预约时间字符串格式（yyyy-MM-dd HH:mm）
    private String aboutTimeStr;

    //下单时间字符串格式（yyyy-MM-dd HH:mm）
    private String addTimeStr;
    //支付时间字符串格式（yyyy-MM-dd HH:mm）
    private String orderPayTimeStr;
    //商家接单时间字符串格式（yyyy-MM-dd HH:mm）
    private String shopReceiveTimeStr;
    //服务结束时间字符串格式（yyyy-MM-dd HH:mm）
    private String serviceCompleteTimeStr;
    //职工确认完成时间字符串格式（yyyy-MM-dd HH:mm）
    private String workconfirmTimeStr;

    private String statusDesc;

    private String shopName;
    private String imgProductUrl;

    private String openId;

    private String sourceType;
    private int coupon;

    private Integer productTime;

    public Integer getProductTime() {
        return productTime;
    }

    public void setProductTime(Integer productTime) {
        this.productTime = productTime;
    }

    public int getCoupon() {
        return coupon;
    }

    public void setCoupon(int coupon) {
        this.coupon = coupon;
    }

    public String getAboutTimeStr() {
        return aboutTimeStr;
    }

    public void setAboutTimeStr(String aboutTimeStr) {
            this.aboutTimeStr = aboutTimeStr;
    }

    @Override
    public String getSourceType() {
        return sourceType;
    }

    @Override
    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getImgProductUrl() {
        return imgProductUrl;
    }

    public void setImgProductUrl(String imgProductUrl) {
        this.imgProductUrl = imgProductUrl;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getAddTimeStr() {
        return addTimeStr;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public void setAddTimeStr(String addTimeStr) {
            this.addTimeStr = addTimeStr;
    }

    public String getOrderPayTimeStr() {
        return orderPayTimeStr;
    }

    public void setOrderPayTimeStr(String orderPayTimeStr) {
            this.orderPayTimeStr = orderPayTimeStr;
    }

    public String getShopReceiveTimeStr() {
        return shopReceiveTimeStr;
    }

    public void setShopReceiveTimeStr(String shopReceiveTimeStr) {
            this.shopReceiveTimeStr = shopReceiveTimeStr;
    }

    public String getServiceCompleteTimeStr() {
        return serviceCompleteTimeStr;
    }

    public void setServiceCompleteTimeStr(String serviceCompleteTimeStr) {
            this.serviceCompleteTimeStr = serviceCompleteTimeStr;
    }

    public String getWorkconfirmTimeStr() {
        return workconfirmTimeStr;
    }

    public void setWorkconfirmTimeStr(String workconfirmTimeStr) {
            this.workconfirmTimeStr = workconfirmTimeStr;
    }
    public OrderVo() {
    }
}
