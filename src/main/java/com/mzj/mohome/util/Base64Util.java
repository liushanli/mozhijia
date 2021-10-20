package com.mzj.mohome.util;

import java.io.UnsupportedEncodingException;

public class Base64Util
{
    public static String encodeBase64(String str)
    {
        byte[] strByte = null;
        String result = null;
        try
        {
            strByte = str.getBytes("UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        byte[] encodeBase64 = org.apache.commons.codec.binary.Base64.encodeBase64(strByte);
        if (encodeBase64 != null) {
            result = new String(encodeBase64);
        }
        return result;
    }

    public static String decodeBase64(String str)
    {
        byte[] strByte = null;
        String result = null;
        try
        {
            strByte = str.getBytes("UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        byte[] encodeBase64 = org.apache.commons.codec.binary.Base64.decodeBase64(strByte);
        if (encodeBase64 != null) {
            result = new String(encodeBase64);
        }
        return result;
    }
}
