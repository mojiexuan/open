package com.chenjiabao.open.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 校验工具集
 */
public class CheckUtils {

    /**
     * 判断是否是11位中国手机号
     * @param phoneNumber 手机号字符串
     * @return 是否手机号
     */
    public static boolean isValidChinaPhoneNumber(String phoneNumber) {
        // 正则表达式：以1 开头，第二位是3-9之间的数字，后面是9位数字
        String regex = "^1[3-9]\\d{9}$";
        // 判断字符串是否为空，并且是否匹配正则
        return phoneNumber != null && phoneNumber.matches(regex);
    }

    /**
     * 验证电子邮件地址的函数
     * @param email 电子邮件地址
     * @return 是否合法电子邮件
     */
    public static boolean isValidEmail(String email) {
        // 正则表达式模式，用于匹配整个文本是否是电子邮件地址
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * 判断字符串是否纯数字构成
     * @param string 需要验证的字符串
     * @return 是否纯数字字符串
     */
    public static boolean isValidNumberString(String string) {
        return string.matches("-?\\d+");
    }

    /**
     * 验证字符串是否仅由0-9、a-z、A-Z构成
     * @param str 需要验证的字符串
     * @return boolean
     */
    public static boolean isValidNumberAndLetters(String str) {
        return str != null && str.matches("^[a-zA-Z0-9]+$");
    }

}
