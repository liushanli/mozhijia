package com.mzj.mohome.vo;

import lombok.Data;

/**
 * 查询订单请求参数（非必填）
 */
@Data
public class QueryOrderRequestExt extends QueryOrderRequest {
    // 签名类型，默认MD5
    private String sign_type;
}

