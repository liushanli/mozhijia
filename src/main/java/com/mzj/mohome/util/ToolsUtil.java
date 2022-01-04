package com.mzj.mohome.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.text.NumberFormat;
import java.util.*;

public class ToolsUtil {
    private final static Logger logger = LoggerFactory.getLogger(ToolsUtil.class);
    public static String getString(Object object){
        if(object!=null && object != "" && object != "null")
            return String.valueOf(object);
        else
            return null;

    }

    /**
     * 产生4位随机数(0000-9999)
     * @return 4位随机数
     */
    public static String getFourRandom(){
        Random random = new Random();
        String fourRandom = random.nextInt(10000) + "";
        int randLength = fourRandom.length();
        if(randLength<4){
            for(int i=0; i<=4-randLength; i++)
                fourRandom = "" + fourRandom ;
        }
        return fourRandom;
    }

    //获取百分比值
    public static String getNumberFormat(int num1,int num2){
        // 创建一个数值格式化对象

        NumberFormat numberFormat = NumberFormat.getInstance();

        // 设置精确到小数点后2位

        numberFormat.setMaximumFractionDigits(2);

        String result_1 = "0%";
        if(num1==0 || num2 == 0){
            result_1 = "0%";
        }else{
            result_1 = numberFormat.format((float) num1 / (float) num2 * 100)+"%";
        }
        //System.out.println("num1和num2的百分比为:" + result_1 + "%");
        return  result_1;
    }

    /**
     * 根据当前时间，添加或减去指定的时间量。例如，要从当前日历时间减去 5 天，可以通过调用以下方法做到这一点：
     * add(Calendar.DAY_OF_MONTH, -5)。
     * @param date 指定时间
     * @param num  为时间添加或减去的时间天数
     * @return
     */
    public static Date getBeforeOrAfterDate(Date date, int num) {
        Calendar calendar = Calendar.getInstance();//获取日历
        calendar.setTime(date);//当date的值是当前时间，则可以不用写这段代码。
        calendar.add(Calendar.DATE, num);
        Date d = calendar.getTime();//把日历转换为Date
        return d;
    }

    //获取近N个月
    public static Date getBeforeOrAfterMontg(Date date, int num) {
        Calendar calendar = Calendar.getInstance();//获取日历
        calendar.setTime(date);//当date的值是当前时间，则可以不用写这段代码。
        calendar.add(Calendar.MONTH, num);
        Date d = calendar.getTime();//把日历转换为Date
        return d;
    }





    public static List<String> processImg(String veido_path,String ffmpeg_path) {

        File file = new File(veido_path);
        List<String> commands = new java.util.ArrayList<String>();

        if (!file.exists()) {

            logger.info("路径[" + veido_path + "]对应的视频文件不存在!");
        } else {
            commands.add(ffmpeg_path);

            commands.add("-i");

            commands.add(veido_path);

            commands.add("-y");

            commands.add("-f");

            commands.add("image2");

            commands.add("-ss");

            commands.add("0.5");//这个参数是设置截取视频多少秒时的画面

            commands.add("-t");

            commands.add("1");

            commands.add("-s");

            commands.add("1920x1080");//宽X高

            commands.add(veido_path.substring(0, veido_path.lastIndexOf(".")).replaceFirst("vedio", "file") + ".jpg");

            try {
                ProcessBuilder builder = new ProcessBuilder();

                builder.command(commands);

                builder.start();

                logger.info("截取成功");

            } catch (Exception e) {

                e.printStackTrace();
                logger.error("截屏失败==" + e.getMessage());
            }
        }
        return commands;
    }

    public static boolean getCityFlag(String city){
        if(StringUtils.isEmpty(city)){
            return false;
        }/*else if("北京市、上海市、天津市、重庆市".contains(city)){
            return true;
        }else if(city.contains("省")){
            return false;
        }*/else{
            return true;
        }
    }

    public static String getStringValue(Object value){
        if(value!=null && value != "" && value != "null"){
            return value.toString();
        }else{
            return "";
        }
    }

    public static void main(String[] args) {

        Map<String,Object> map = new HashMap<>();
        map.put("x",null);
        map.put("y","null");
        map.put("z","");
        map.put("q","ddd");
        System.out.println(getStringValue(map.get("x")));
        System.out.println(getStringValue(map.get("y")));
        System.out.println(getStringValue(map.get("z")));
        System.out.println(getStringValue(map.get("q")));


      /*  List<String> list =  processImg("D:/work/Admin/Upload/worker_ImgUrl/20211020/compress_video_2500753990.mp4","D:\\work\\ffmpeg.exe");
      System.out.println(JSON.toJSONString(list));*/
       /* String name = "D:/work/Admin/Upload/worker_ImgUrl/20210523/compress_video_1117863920.jpg";
        System.out.println(name.substring(21));*/
      /*  boolean flag = getCityFlag("上海市");
        boolean flag_1 = getCityFlag("安徽省");
        boolean flag_2 = getCityFlag("合肥市");
        boolean flag_3 = getCityFlag("松江区");
        System.out.println(flag+"_"+flag_1+"==="+flag_3+"==="+flag_2);*/
    }
}
