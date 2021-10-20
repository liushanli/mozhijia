package com.mzj.mohome.util;
public class MapUtil {


        /**
         * 地球半径,单位 km
         */
        private static final double EARTH_RADIUS = 6378.137;

        /**
         * 根据经纬度，计算两点间的距离
         *
         * @param longitude1 第一个点的经度
         * @param latitude1  第一个点的纬度
         * @param longitude2 第二个点的经度
         * @param latitude2  第二个点的纬度
         * @return 返回距离 单位千米
         */
        public static double getDistance(double longitude1, double latitude1, double longitude2, double latitude2) {
            // 纬度
            double lat1 = Math.toRadians(latitude1);
            double lat2 = Math.toRadians(latitude2);
            // 经度
            double lng1 = Math.toRadians(longitude1);
            double lng2 = Math.toRadians(longitude2);
            // 纬度之差
            double a = lat1 - lat2;
            // 经度之差
            double b = lng1 - lng2;
            // 计算两点距离的公式
            double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                    Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(b / 2), 2)));
            // 弧长乘地球半径, 返回单位: 千米
            s =  s * EARTH_RADIUS;
            return s;
        }

        public static void main(String[] args) {
          /*  double d = getDistance(121.233958,31.105238, 121.4778854709201, 31.258792588975691);
            System.out.println(d);*/
          String name = "12343";
          Float f = Float.parseFloat(name);
          f = f/1000;
          float b = (float)(Math.round(f*100))/100;
          System.out.println(b);
        }
}
