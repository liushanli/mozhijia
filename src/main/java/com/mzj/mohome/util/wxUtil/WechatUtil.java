package com.mzj.mohome.util.wxUtil;

import com.google.common.collect.Maps;
import com.mzj.mohome.util.DateUtils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyReplacer;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.apache.commons.beanutils.BeanMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.SortedMap;

public class WechatUtil {

    private static Logger logger = LoggerFactory.getLogger(WechatUtil.class);

    /**
     * 数据转换为xml格式
     *
     * @param object
     * @param obj
     * @return
     */
    public static String truncateDataToXML(Class<?> object, Object obj) {
        XStream xStream = new XStream(new XppDriver(new XmlFriendlyReplacer("_-", "_")));
        xStream.alias("xml", object);

        return xStream.toXML(obj);
    }

    /**
     * 数据转换为对象
     *
     * @param object
     * @param str
     * @return
     */
    public static Object truncateDataFromXML(Class<?> object, String str) {
        XStream xStream = new XStream(new XppDriver(new XmlFriendlyReplacer("_-", "_")));
        xStream.alias("xml", object);

        return xStream.fromXML(str);
    }

    /**
     * 生成随机字符串
     *
     * @return
     */
    public static String makeNonceStr() {
        StringBuffer str = new StringBuffer(DateUtils.getSysDateString("yyyyMMddHHmmssS"));
        str.append((new Random().nextInt(900) + 100));

        return str.toString();
    }

    /**
     * 拼接签名数据
     *
     * @return
     */
    public static String makeSign(BeanMap beanMap) {
        SortedMap<String, String> signMaps = Maps.newTreeMap();

        for (Object key : beanMap.keySet()) {
            Object value = beanMap.get(key);

            // 排除空数据
            if (value == null) {
                continue;
            }

            signMaps.put(key + "", String.valueOf(value));
        }

        // 生成签名
        return generateSign(signMaps);
    }


    /**
     * 生成签名
     *
     * @param signMaps
     * @return
     * @throws Exception
     */
    public static String generateSign(SortedMap<String, String> signMaps) {
        StringBuffer sb = new StringBuffer();

        // 字典序
        for (Map.Entry signMap : signMaps.entrySet()) {
            String key = (String) signMap.getKey();
            String value = (String) signMap.getValue();

            // 为空不参与签名、参数名区分大小写
            if (null != value && !"".equals(value) && !"sign".equals(key) && !"key".equals(key)) {
                sb.append(key).append("=").append(value).append("&");
            }
        }

        // 拼接key
        //sb.append("key=").append();

        // MD5加密
        return Objects.requireNonNull(encoderByMd5(sb.toString())).toUpperCase();
    }

    /**
     * 利用MD5进行加密
     *
     * @param str 待加密的字符串
     * @return 加密后的字符串
     */
    private static String encoderByMd5(String str) {
        // 生成一个MD5加密计算摘要
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            logger.error("MD5加密失败！", e);
        }

        if (md == null) {
            return null;
        }

        // 计算md5函数
        md.update(str.getBytes());
        // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
        // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
        return new BigInteger(1, md.digest()).toString(16);
    }

}
