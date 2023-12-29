package com.mzj.mohome.vo;

import lombok.Data;

/**
 * 查询订单接收参数（非必填）
 */
@Data
public class QueryOrderResponseExt extends QueryOrderResponse {
    // 返回信息，非空则表示返回了错误信息
    private String return_msg;

    // 错误代码
    private String err_code;

    // 错误代码描述
    private String err_code_des;

    // 设备号
    private String device_info;

    // 是否关注公众号
    private String is_subscribe;

    // 应结订单金额
    private String settlement_total_fee;

    // 标价币种
    private String fee_type;

    // 现金支付币种
    private String cash_fee_type;

    // 附加数据
    private String attach;
}

