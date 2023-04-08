package com.mzj.mohome.vo;

import lombok.Data;

/**
 * 地图的相关参数
 */
@Data
public class MapDto {
    /**
     * 经度
     */
    private String lng;
    /**
     * 纬度
     */
    private String lat;
    /**
     * 距离
     */
    private Long distance;
    /**
     * 请求地址
     */
    private String address;
}
