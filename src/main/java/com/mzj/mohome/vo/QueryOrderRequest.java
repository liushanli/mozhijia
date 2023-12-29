package com.mzj.mohome.vo;

import lombok.Data;

/**
 * 订单查询请求参数（必填）
 */
@Data
public class QueryOrderRequest {
    // 公众账号id
    private String appid;

    // 商户号
    private String mch_id;

    // 商户订单号，32位以内，不重复
    private String out_trade_no;

    // 随机字符串，32位以内
    private String nonce_str;

    // 签名，遵循签名算法
    private String sign;
}