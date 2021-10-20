package com.mzj.mohome.util;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.ParseException;
import javax.swing.ImageIcon;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ResourceUtils;

public class ImageUtil {

    /**
     * @param args
     * @throws ParseException
     */
    public static void main(String[] args) throws IOException {
        //获取文件的相对路径  可在控制台打印查看输出结果
        String filePath = ResourceUtils.getFile("classpath:zanting.png").getPath();

        exportImg2(filePath,"d:/image/1616821715831.jpg","D:/work/Admin");
    }

    //图片名称,小图标，大图标，存取路径
    public static String exportImg2(String headImg,String bigPath,String cunPath){
        try {
            //1.jpg是你的 主图片的路径
            //InputStream is = new FileInputStream("d:/image/202103273.jpg");
            InputStream is = new FileInputStream(bigPath);
            //通过JPEG图象流创建JPEG数据流解码器
            JPEGImageDecoder jpegDecoder = JPEGCodec.createJPEGDecoder(is);
            //解码当前JPEG数据流，返回BufferedImage对象
            BufferedImage buffImg = jpegDecoder.decodeAsBufferedImage();
            //得到画笔对象
            Graphics g = buffImg.getGraphics();
            //创建你要附加的图象。
            //小图片的路径
            ImageIcon imgIcon = new ImageIcon(headImg);
            //得到Image对象。
            Image img = imgIcon.getImage();
            //将小图片绘到大图片上。
            //5,300 .表示你的小图片在大图片上的位置。

            int x = 0, y = 0;
            String location = "center";
            if (StringUtils.equals(location, "left-top")) {
                x = 30;
                y = 30;
            } else if (StringUtils.equals(location, "right-top")) {
                x = buffImg.getWidth() - img.getWidth(null) - 30;
                y = 30;
            } else if (StringUtils.equals(location, "left-bottom")) {
                x += 30;
                y = buffImg.getHeight() - img.getHeight(null) - 30;
            } else if (StringUtils.equals(location, "right-bottom")) {
                x = buffImg.getWidth() - img.getWidth(null) - 30;
                y = buffImg.getHeight() - img.getHeight(null) - 30;
            } else {
                x = (buffImg.getWidth() - img.getWidth(null)) / 2;
                y = (buffImg.getHeight() - img.getHeight(null)) / 2;
            }
            g.drawImage(img,x,y,null);
            g.dispose();
            OutputStream os;
            String imageName = "image_"+System.currentTimeMillis() + ".jpg";

            File file = new File(cunPath);
/*            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }*/
            //如果文件夹不存在则创建
            if  (!file .exists()  && !file .isDirectory())
            {
                System.out.println("//不存在");
                file .mkdir();
            } else
            {
                System.out.println("//目录存在");
            }

            String shareFileName = cunPath+"/" + imageName;
            os = new FileOutputStream(shareFileName);
            //创键编码器，用于编码内存中的图象数据。
            JPEGImageEncoder en = JPEGCodec.createJPEGEncoder(os);
            en.encode(buffImg);
            is.close();
            os.close();
            return imageName;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return  null;
        } catch (ImageFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return  null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return  null;
        }
    }
}
