package com.mzj.mohome.vo;

import com.mzj.mohome.entity.Order;
import lombok.Data;


import java.io.Serializable;
import java.text.SimpleDateFormat;

public class OrderVo extends Order implements Serializable {
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    //预约时间字符串格式（yyyy-MM-dd HH:mm）
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
        if(this.getAddTime()!=null)
            this.addTimeStr = simpleDateFormat.format(this.getAddTime());
        else
            this.addTimeStr = addTimeStr;
    }

    public String getOrderPayTimeStr() {
        return orderPayTimeStr;
    }

    public void setOrderPayTimeStr(String orderPayTimeStr) {
        if(this.getOrderPayTime()!=null)
            this.orderPayTimeStr = simpleDateFormat.format(this.getOrderPayTime());
        else
            this.orderPayTimeStr = orderPayTimeStr;
    }

    public String getShopReceiveTimeStr() {
        return shopReceiveTimeStr;
    }

    public void setShopReceiveTimeStr(String shopReceiveTimeStr) {
        if(this.getShopReceiveTime()!=null)
            this.shopReceiveTimeStr = simpleDateFormat.format(this.getShopReceiveTime());
        else
            this.shopReceiveTimeStr = shopReceiveTimeStr;
    }

    public String getServiceCompleteTimeStr() {
        return serviceCompleteTimeStr;
    }

    public void setServiceCompleteTimeStr(String serviceCompleteTimeStr) {
        if(this.getServiceCompleteTime()!=null)
            this.serviceCompleteTimeStr = simpleDateFormat.format(this.getServiceCompleteTime());
        else
            this.serviceCompleteTimeStr = serviceCompleteTimeStr;
    }

    public String getWorkconfirmTimeStr() {
        return workconfirmTimeStr;
    }

    public void setWorkconfirmTimeStr(String workconfirmTimeStr) {
        if(this.getWorkconfirmTime()!=null)
            this.workconfirmTimeStr = simpleDateFormat.format(this.getWorkconfirmTime());
        else
            this.workconfirmTimeStr = workconfirmTimeStr;
    }
    public OrderVo() {
    }
}
