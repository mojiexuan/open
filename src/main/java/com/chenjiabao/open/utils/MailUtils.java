package com.chenjiabao.open.utils;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * 邮件发送工具类
 * @author ChenJiaBao
 */
@Component
public class MailUtils {

    private final Session session;
    private String from;
    private String to;
    private String subject;
    private String content;

    private MailUtils(Session session){
        this.session = session;
    }

    public MailUtils setFrom(String address){
        this.from = address;
        return this;
    }

    public MailUtils setTo(String address){
        this.to = address;
        return this;
    }

    public MailUtils setSubject(String subject){
        this.subject = subject;
        return this;
    }

    public MailUtils setContent(String content){
        this.content = content;
        return this;
    }

    /**
     * 发送邮件
     * @throws MessagingException 邮件发送异常
     */
    public void send() throws MessagingException {
        if (from == null || to == null || subject == null || content == null) {
            throw new IllegalStateException("邮件发送信息不完整，请检查发件人、收件人、主题和内容");
        }
        
        MimeMessage mimeMessage = new MimeMessage(this.session);
        //发件人
        mimeMessage.setFrom(this.from);
        //收件人
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(this.to));
        //标题
        mimeMessage.setSubject(this.subject);
        //解析html
        mimeMessage.setContent(this.content, "text/html;charset=utf-8");
        //发送邮件
        Transport.send(mimeMessage);
    }

    /**
     * 邮件配置构建器
     */
    public static class Builder {
        private final CheckUtils checkUtils;
        private String host;
        private int port = 465;
        private boolean ssl = true; // 默认启用SSL
        private boolean auth = false;
        private String username;
        private String password;
        private String protocol = "smtp";

        public Builder(CheckUtils checkUtils) {
            this.checkUtils = checkUtils;
        }

        /**
         * 设置SMTP服务器地址
         * @param host SMTP服务器地址
         * @return Builder实例
         */
        public Builder setHost(String host) {
            if (checkUtils.isValidEmptyParam(host)) {
                throw new IllegalArgumentException("SMTP服务器地址不能为空");
            }
            this.host = host;
            return this;
        }

        /**
         * 设置SMTP服务器端口
         * @param port SMTP服务器端口
         * @return Builder实例
         */
        public Builder setPort(int port) {
            if (port <= 0) {
                throw new IllegalArgumentException("端口号必须大于0");
            }
            this.port = port;
            return this;
        }

        /**
         * 设置是否使用SSL连接
         * @param ssl 是否使用SSL
         * @return Builder实例
         */
        public Builder setSsl(boolean ssl) {
            this.ssl = ssl;
            return this;
        }

        /**
         * 设置是否需要身份验证
         * @param auth 是否需要身份验证
         * @return Builder实例
         */
        public Builder setAuth(boolean auth) {
            this.auth = auth;
            return this;
        }

        /**
         * 设置SMTP身份验证用户名
         * @param username 用户名
         * @return Builder实例
         */
        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        /**
         * 设置SMTP身份验证密码
         * @param password 密码
         * @return Builder实例
         */
        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        /**
         * 设置邮件传输协议
         * @param protocol 协议名称
         * @return Builder实例
         */
        public Builder setProtocol(String protocol) {
            if (checkUtils.isValidEmptyParam(protocol)) {
                throw new IllegalArgumentException("协议不能为空");
            }
            this.protocol = protocol;
            return this;
        }

        /**
         * 构建MailUtils实例
         * @return MailUtils实例
         */
        public MailUtils build() {
            if (checkUtils.isValidEmptyParam(this.host)) {
                throw new IllegalArgumentException("SMTP服务器地址不能为空");
            }
            
            Properties props = new Properties();
            props.put("mail.transport.protocol", this.protocol);
            props.put("mail.smtp.host", this.host);
            props.put("mail.smtp.port", this.port);
            
            // 正确设置SSL属性
            if (this.ssl) {
                props.put("mail.smtp.ssl.enable", "true");
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.socketFactory.port", this.port);
            }
            
            Session session;
            if (this.auth) {
                if (checkUtils.isValidEmptyParam(this.username, this.password)) {
                    throw new IllegalArgumentException("启用身份验证时，用户名和密码不能为空");
                }
                props.put("mail.smtp.auth", "true");
                final String finalUsername = this.username;
                final String finalPassword = this.password;
                
                session = Session.getInstance(props, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(finalUsername, finalPassword);
                    }
                });
            } else {
                session = Session.getInstance(props);
            }
            
            return new MailUtils(session);
        }
    }
}
