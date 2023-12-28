package com.mzj.mohome.vo;

import lombok.Data;

/**
 * 查询订单接收参数（必填）
 */
@Data
public class QueryOrderResponse {
    // 返回状态码，通信标识，SUCCESS/FAIL
    private String return_code;

    // 公众账号id
    private String appid;

    // 商户号
    private String mch_id;

    // 随机字符串
    private String nonce_str;

    // 签名
    private String sign;

    // 业务结果，交易标识，SUCCESS/FAIL
    private String result_code;

    // 用户标识
    private String openid;

    // 交易类型，JSAPI，NATIVE，APP
    private String trade_type;

    // 交易状态，SUCCESS-成功 USERPAYING-支付中
    private String trade_state;

    // 付款银行
    private String bank_type;

    // 标价金额，单位分
    private int total_fee;

    // 现金支付金额
    private int cash_fee;

    // 微信支付订单号
    private String transaction_id;

    // 商户订单号
    private String out_trade_no;

    // 支付完成时间
    private String time_end;

    // 交易状态描述
    private String trade_state_desc;
}