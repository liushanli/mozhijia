package com.mzj.mohome.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class AddressVo implements Serializable {
    private String provice;
    private String city;
    private String address;
    private String lng;
    private String lat;
    private String area;
}
