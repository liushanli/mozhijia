package com.mzj.mohome.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 技师绑定的微信信息
 * @author lsl
 */
@Data
public class WorkerWxInfo implements Serializable {
    private String workerId;
    private String openId;
    private String nickName;
    private String avatarUrl;
    private String sex;
    private Date createTime;
    private Date updateTime;

}
