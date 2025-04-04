package com.chenjiabao.open.utils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 敏感词检测
 */
public class SensitiveWordUtils {

    // 敏感词库树形结构（DFA结构）
    private Map<Character, Object> sensitiveWordMap = new HashMap<>();
    // 结束标记常量，提高可读性
    private static final Character END_FLAG = Character.valueOf('\0');

    private SensitiveWordUtils() {}


    /**
     * 初始化敏感词库（线程安全，建议在应用启动时执行一次）
     * @param words 敏感词列表
     * @return this
     */
    public SensitiveWordUtils init(List<String> words) {
        // 使用LinkedHashMap保证插入顺序，避免哈希冲突导致的误判
        Map<Character, Object> root = new LinkedHashMap<>();
        for (String word : words) {
            if (word == null || word.isEmpty()) continue;

            Map<Character, Object> currentNode = root;
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                // 如果当前字符已经存在，直接复用已有节点
                if (currentNode.containsKey(c)) {
                    currentNode = (Map<Character, Object>) currentNode.get(c);
                } else {
                    // 创建新节点并标记结束位置
                    Map<Character, Object> newChild = new LinkedHashMap<>();
                    newChild.put(END_FLAG, false);
                    currentNode.put(c, newChild);
                    currentNode = newChild;
                }
                // 标记词尾
                if (i == word.length() - 1) {
                    currentNode.put(END_FLAG, true);
                }
            }
        }
        this.sensitiveWordMap = root;
        return this;
    }

    /**
     * 检测文本是否包含敏感词
     * @param txt 待检测文本
     * @return true表示存在敏感词
     */
    public boolean contains(String txt) {
        if (txt == null || txt.isEmpty()) return false;

        for (int i = 0; i < txt.length(); i++) {
            // 快速跳过非敏感词起始字符
            if (!sensitiveWordMap.containsKey(txt.charAt(i))) continue;

            int checkResult = checkWord(txt, i);
            if (checkResult > 0) return true;
        }
        return false;
    }

    /**
     * 从指定位置开始检查敏感词
     * @param txt 文本
     * @param startIndex 起始位置
     * @return 匹配到的敏感词长度（0表示无匹配）
            */
    private int checkWord(String txt, int startIndex) {
        int matchLength = 0;
        Map<Character, Object> currentNode = sensitiveWordMap;

        for (int i = startIndex; i < txt.length(); i++) {
            char currentChar = txt.charAt(i);
            Object nextNode = currentNode.get(currentChar);

            if (nextNode == null) break; // 路径中断，直接返回

            // 更新当前节点和已匹配长度
            currentNode = (Map<Character, Object>) nextNode;
            matchLength++;

            // 检查是否到达词尾
            if (Boolean.TRUE.equals(currentNode.get(END_FLAG))) {
                return matchLength;
            }
        }
        return 0; // 未完整匹配敏感词
    }

    public static SensitiveWordUtils builder() {
        return new SensitiveWordUtils();
    }

}
