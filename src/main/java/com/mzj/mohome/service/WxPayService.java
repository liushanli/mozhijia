package com.mzj.mohome.service;

import com.mzj.mohome.vo.OrderInfo;

import java.util.Map;

public interface WxPayService {
    Map<String, Object> jsApiPay(OrderInfo orderInfo);
}
