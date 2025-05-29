package com.chenjiabao.open.utils;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

/**
 * @author ChenJiaBao
 */
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

    public void send(){
        try{
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static class Builder{
        private static final Builder BUILDER = new Builder();
        private String host;
        private int port = 465;
        private boolean ssl = false;
        private boolean auth = false;
        private String username;
        private String password;
        private String protocol = "smtp";

        private Builder(){}

        public Builder setHost(String host) {
            this.host = host;
            return this;
        }

        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        public Builder setSsl(boolean ssl) {
            this.ssl = ssl;
            return this;
        }

        public Builder setAuth(boolean auth) {
            this.auth = auth;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setProtocol(String protocol) {
            this.protocol = protocol;
            return this;
        }

        public static Builder getInstance(){
            return Builder.BUILDER;
        }

        public MailUtils builder() {
            if(CheckUtils.isValidEmptyParam(this.host)){
                throw new RuntimeException("host不能为空");
            }
            Properties props = new Properties();
            props.put("mail.smtp.host", this.host);
            props.put("mail.smtp.port", this.port);
            props.put("mail.smtp.ssl", this.ssl);
            if(this.ssl){
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            }
            Session session;
            if(this.auth){
                if(CheckUtils.isValidEmptyParam(this.username,this.password)){
                    throw new RuntimeException("username或password为空");
                }
                props.put("mail.smtp.auth", "true");
                session = Session.getInstance(props, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
            }else {
                session = Session.getInstance(props);
            }
            props.put("mail.transport.protocol",this.protocol);
            return new MailUtils(session);
        }
    }

}
