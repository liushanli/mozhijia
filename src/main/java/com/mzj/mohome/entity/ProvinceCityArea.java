package com.mzj.mohome.entity;

import lombok.Data;
import java.io.Serializable;

@Data
public class ProvinceCityArea implements Serializable {
    private Integer id;
    private String name;
    private String pid;
    private String sname;
    private Integer level;
    private String citycode;
    private String yzcode;
    private String mername;
    private Float lng;
    private Float lat;
    private String pinyin;
}
