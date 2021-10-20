package com.mzj.mohome.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

//城市合伙人
@Data
public class Partner implements Serializable {
    private Integer id;
    private String userName;
    private String userPhone;
    private String email;
    private String remark;
    private Integer isMassage;
    private Integer deposit;
    private String teamSize;
    private String province;
    private String city;
    private Date addTime;
    private Integer isManager;
}
