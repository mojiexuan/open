package com.chenjiabao.open.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;

/**
 * jwt工具（支持东8区时间）
 */
public class JwtUtils {

    // 默认密钥（至少32字符）
    private static String SECRET_KEY = "ckxrbGZKNHA3VUhCa3FoNmpwbjMwQTlpQkZnRHBhRXo=";
    // 过期时间（2小时，单位：秒）
    private static final int EXPIRES = 7200;

    /**
     * 设置秘钥（只能设置一次）
     * @param jwtSecret 新的秘钥（建议使用Base64编码的32位以上字符串）
     */
    public static void setJwtSecret(String jwtSecret) {
        if(Objects.equals(SECRET_KEY, "ckxrbGZKNHA3VUhCa3FoNmpwbjMwQTlpQkZnRHBhRXo=")){
            SECRET_KEY = jwtSecret;
        }
    }

    /**
     * 生成Token（东8区时间）
     * @param subject 用户唯一标识（如用户ID）
     * @return 生成的JWT Token
     */
    public static String createToken(String subject) {
        // 1. 生成密钥
        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
        Key signingKey = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());

        // 2. 计算东8区时间
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("GMT+8"));
        Date issuedAt = Date.from(now.toInstant());
        Date expiration = Date.from(now.plusSeconds(EXPIRES).toInstant());

        // 3. 生成 Token
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, signingKey)
                .compact();
    }

    /**
     * 解析Token
     * @param token JWT Token
     * @return 声明内容
     */
    public static Claims parseToken(String token) {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
        return Jwts.parser()
                .setSigningKey(keyBytes)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 验证Token有效性
     * @param token JWT Token
     * @return 是否有效
     */
    public static boolean validateToken(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

}
