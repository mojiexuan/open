package com.chenjiabao.open.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;

/**
 * jwt工具（支持东8区时间）
 * @author ChenJiaBao
 */
public class JwtUtils {

    // 默认密钥（至少 32 字符）
    private static SecretKey SECRET_KEY = null;
    // 过期时间（2小时，单位：秒）
    private static int EXPIRES = 7200;

    /**
     * 设置秘钥（只能设置一次）
     * @param jwtSecret 新的秘钥（建议使用Base64编码的32位以上字符串）
     */
    public synchronized void setJwtSecret(String jwtSecret) {
        if(SECRET_KEY == null){
            SECRET_KEY = new SecretKeySpec(Base64.getDecoder().decode(jwtSecret),"HmacSHA256");
        }
    }

    /**
     * 设置过期时间（单位：秒）
     * @param expires 过期时间 秒
     */
    public synchronized void setExpires(int expires){
        if(EXPIRES == 7200){
            EXPIRES = expires;
        }
    }

    /**
     * 获取统一的签名密钥
     */
    private SecretKey getSigningKey() {
        if(SECRET_KEY == null){
            SECRET_KEY = Jwts.SIG.HS256.key().build();
        }
        return SECRET_KEY;
    }

    /**
     * 生成Token（东8区时间）
     * @param subject 用户唯一标识（如用户ID）
     * @return 生成的JWT Token
     */
    public String createToken(String subject) {

        // 计算东8区时间
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("GMT+8"));
        Date issuedAt = Date.from(now.toInstant());
        Date expiration = Date.from(now.plusSeconds(EXPIRES).toInstant());

        // 生成 Token
        return Jwts.builder()
                .subject(subject)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    /**
     * 解析Token
     * @param token JWT Token
     * @return 声明内容
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 验证Token有效性
     * @param token JWT Token
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

}
