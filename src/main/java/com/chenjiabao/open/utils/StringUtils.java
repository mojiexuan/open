package com.chenjiabao.open.utils;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Random;

/**
 * 字符串工具类
 */
public class StringUtils {

    /**
     * 字符串转base64
     * @param str 需要转换的字符串
     * @return base64
     */
    public static String stringToBase64(String str) {
        return Base64.getEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 判断字符串是否为空
     */
    public static boolean isStringEmpty(String string) {
        return string == null || string.isEmpty() || string.contains(" ");
    }

    /**
     * 判断字符串是否为空
     *
     * @param isContains 是否校验空格，为true时若字符串中存在空格将返回true
     */
    public static boolean isStringEmpty(String string, boolean isContains) {
        if (isContains) {
            return string == null || string.isEmpty() || string.contains(" ");
        } else {
            return string == null || string.isEmpty();
        }

    }

    /**
     * 判断字符串是否纯数字构成
     */
    public static boolean isStringNumber(String string) {
        return string.matches("-?\\d+");
    }

    /**
     * 复制文本到剪切板
     */
    public static void copyToClipboard(String string) {
        StringSelection stringSelection = new StringSelection(string);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    /**
     * 生成随机指定范围字符串
     *
     * @param characters 指定范围如"ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
     * @param num        长度
     */
    public static String generateSureString(String characters, int num) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < num; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }

    /**
     * 生成随机指定范围字符串 默认范围为"ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
     *
     * @param num 长度
     */
    public static String generateSureString(int num) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        return generateSureString(characters,num);
    }

    /**
     * 生成随机长度纯数字字符串
     *
     * @param length 长度
     */
    public static String generateRandomNumberString(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10)); // 生成0-9之间的随机数字
        }
        return sb.toString();
    }

    /**
     * 数量格式化，数量过大处理
     * @param num long型数量
     * @return 格式化后的字符串
     */
    public static String numberFormat(long num){
        if(num < 1000){
            return Long.toString(num);
        } else if (num < 10000) {
            return (num/1000)+"千+";
        } else if (num < 100000000) {
            return (num/10000)+"万+";
        }else {
            return "1亿+";
        }
    }

}
