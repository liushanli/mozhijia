package com.mzj.mohome.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderDto implements Serializable {

    private String statusDesc;
    private String userId;
    private String orderId;
    private String workerId;
    private String shopId;
    private Integer size;
    private Integer page;
    private Integer sourceType;
}
