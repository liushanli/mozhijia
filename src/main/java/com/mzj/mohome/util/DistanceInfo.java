package com.mzj.mohome.util;

import static com.mzj.mohome.util.getLngAndLat.getDistances;

/**
 * @Author admin
 * @ClassName DistanceInfo
 * @Description TODO
 * @Date 2023/10/25 9:57
 */
public class DistanceInfo {
    private static final double EARTH_RADIUS_KM = 6371.0;

    /**
     * 使用Haversine公式计算经纬度
     * lat1  纬度1
     * lon1  经度1
     * lat2  纬度2
     * lon2  经度2
     */
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // 将经纬度转换为弧度
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // 计算经纬度的差值
        double deltaLat = lat2Rad - lat1Rad;
        double deltaLon = lon2Rad - lon1Rad;

        // 应用Haversine公式
        double a = Math.pow(Math.sin(deltaLat / 2), 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.pow(Math.sin(deltaLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS_KM * c;

        return distance;
    }

    //Vincenty公式
    public static double calculateDistanceVincenty(double lat1, double lon1, double lat2, double lon2) {
        // 将经纬度转换为弧度
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // 计算经纬度的差值
        double deltaLon = lon2Rad - lon1Rad;

        // 应用Vincenty公式
        double a = Math.pow(Math.cos(lat2Rad) * Math.sin(deltaLon), 2) +
                Math.pow(Math.cos(lat1Rad) * Math.sin(lat2Rad) - Math.sin(lat1Rad) * Math.cos(lat2Rad) * Math.cos(deltaLon), 2);
        double b = Math.sin(lat1Rad) * Math.sin(lat2Rad) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.cos(deltaLon);
        double c = Math.atan2(Math.sqrt(a), b);
        double distance = EARTH_RADIUS_KM * c;

        return distance;
    }

    public static void main(String[] args) {
        String origin = "31.2595,121.526";
        String destination = "31.2204,121.425";

      /*  String origin = "39.923423,116.368904";
        String destination = "39.922501,116.387271";*/

      /*  String origin = "23.135336,113.27143";
        String destination = "22.822607,108.37345";*/

        //113.27143,23.135336
        //108.37345,22.822607

/*        String origin = "116.368904,39.923423";
        String destination = "116.387271,39.922501";*/
/*        System.out.println(calculateDistance(23.135336,113.27143,22.822607,108.37345));
        System.out.println(calculateDistanceVincenty(23.135336,113.27143,22.822607,108.37345));*/

        getDistances(origin,destination);
    }

}
