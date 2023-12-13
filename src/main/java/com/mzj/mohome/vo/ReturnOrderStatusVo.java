package com.mzj.mohome.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author admin
 * @ClassName ReturnOrderStatusVo
 * @Description TODO
 * @Date 2023/12/13 10:17
 */
@Data
public class ReturnOrderStatusVo implements Serializable {

    private String orderId;
    private Integer status;
}
