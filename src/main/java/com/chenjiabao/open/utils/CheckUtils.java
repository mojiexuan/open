package com.chenjiabao.open.utils;

import java.util.regex.Pattern;

/**
 * 校验工具集
 * @author ChenJiaBao
 */
public class CheckUtils {
    // 手机号校验，支持 +86 或 86 区号
    private static final Pattern CHINA_PHONE_PATTERN = Pattern.compile("^(\\+?86)?1[3-9]\\d{9}$");

    // 电子邮件正则
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", Pattern.CASE_INSENSITIVE);

    // 纯数字正则
    private static final Pattern NUMBER_PATTERN = Pattern.compile("-?\\d+");

    // 字母数字正则
    private static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile("^[a-zA-Z0-9]+$");

    // 中国大陆身份证号正则（简单版）
    private static final Pattern ID_CARD_PATTERN =
            Pattern.compile("^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[0-9Xx]$");

    /**
     * 是否空参检查
     * @param params 参数列表
     * @return boolean是否空参，任一参数为空则返回true
     */
    public static boolean isValidEmptyParam(String... params) {
        if (params == null || params.length == 0) {
            return true;
        }

        for (String param : params) {
            if (param == null || param.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是11位中国手机号
     * @param phoneNumber 手机号字符串
     * @return 是否手机号
     */
    public static boolean isValidChinaPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            return false;
        }
        // 去除所有空白字符
        String cleaned = phoneNumber.replaceAll("\\s+", "");
        return CHINA_PHONE_PATTERN.matcher(cleaned).matches();
    }

    /**
     * 验证电子邮件地址
     * @param email 电子邮件地址
     * @return 是否合法电子邮件
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * 判断字符串是否纯数字构成
     * @param string 需要验证的字符串
     * @return 是否纯数字字符串
     */
    public static boolean isValidNumberString(String string) {
        return string != null && NUMBER_PATTERN.matcher(string).matches();
    }

    /**
     * 验证字符串是否仅由0-9、a-z、A-Z构成
     * @param str 需要验证的字符串
     * @return boolean
     */
    public static boolean isValidNumberAndLetters(String str) {
        return str != null && ALPHANUMERIC_PATTERN.matcher(str).matches();
    }

    /**
     * 验证中国大陆身份证号（简单校验）
     * @param idCard 身份证号
     * @return 是否合法身份证号
     */
    public static boolean isValidIdCard(String idCard) {
        return idCard != null && ID_CARD_PATTERN.matcher(idCard).matches();
    }

    /**
     * 验证字符串长度是否在指定范围内
     * @param str 要验证的字符串
     * @param min 最小长度（包含）
     * @param max 最大长度（包含）
     * @return 是否在长度范围内
     */
    public static boolean isLengthInRange(String str, int min, int max) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        return length >= min && length <= max;
    }

    /**
     * 验证字符串是否是合法用户名（字母开头，允许字母数字下划线，长度4-20）
     * @param username 用户名
     * @return 是否合法
     */
    public static boolean isValidUsername(String username) {
        return username != null && username.matches("^[a-zA-Z]\\w{3,19}$");
    }
}
