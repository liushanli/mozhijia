package com.mzj.mohome.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
public class OrderWorkerJW implements Serializable {
    private Integer id;
    /// <summary>
///订单id
/// <summary>
    private String orderId;
    /// <summary>
///职工id
/// <summary>
    private String workerId;
    /// <summary>
///经度
/// <summary>
    private String jd;
    /// <summary>
///纬度
/// <summary>
    private String wd;
    private Date addtime;



    public OrderWorkerJW() {
    }

    @Override
    public String toString() {
        return "OrderWorkerJW{" +
                "id=" + id +
                ", orderId='" + orderId + '\'' +
                ", workerId='" + workerId + '\'' +
                ", jd='" + jd + '\'' +
                ", wd='" + wd + '\'' +
                ", addtime=" + addtime +
                '}';
    }
}
