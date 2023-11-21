package com.mzj.mohome.util;

import lombok.Data;

/**
 * @Author admin
 * @ClassName LatitudeAndLongitude
 * @Description 经纬度的类
 * @Date 2023/10/23 15:19
 */
@Data
public class LatitudeAndLongitude {
    //经度
    private String Longitude;
    //纬度
    private String Latitude;
    @Override
    public String toString() {
        return "LatitudeAndLongitude [Longitude=" + Longitude + ", Latitude="
                + Latitude + "]";
    }


}


