package com.mzj.mohome.util;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
public class PhoneUtil {

        public static void main(String[] args) {
            String phoneNumber = "19503003031";
            boolean isEmptyNumber = isEmptyNumber(phoneNumber);
            System.out.println("手机号是否为空号：" + isEmptyNumber);
        }

        public static boolean isEmptyNumber(String phoneNumber) {
            String regex = "^1[3456789]\\d{9}$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(phoneNumber);
            return !matcher.matches();
        }
}
