package com.example.demo.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author huo
 * @date 2024/04/19 21:32
 **/


public class MailUtils {
    public static void sendMail(String clientMail,String code) throws MessagingException {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        SimpleMailMessage message = new SimpleMailMessage();
        javaMailSender.setHost("smtp.qq.com");              // 设置邮箱服务器
        javaMailSender.setPort(465);                        // 设置端口
        javaMailSender.setUsername("1599272181@qq.com");    // 设置用户名
//        javaMailSender.setPassword("<你的密码/授权码>");      // 设置密码（记得替换为你实际的密码、授权码）

        javaMailSender.setProtocol("smtps");                // 设置协议

        message.setFrom("1599272181@qq.com");
        message.setTo(clientMail);
        message.setSubject("Library验证码");
        message.setText( "\t<span style=\"font-size:16px;\">亲爱的用户：</span> \n" +
                "</h3>\n" +
                "<p>\n" +
                "\t<span style=\"font-size:14px;\">&nbsp;&nbsp;&nbsp;&nbsp;</span><span style=\"font-size:14px;\">&nbsp; <span style=\"font-size:16px;\">&nbsp;&nbsp;您好！您正在进行邮箱验证，本次请求的验证码为：<span style=\"font-size:24px;color:#FFE500;\"> "+code+"</span>,本验证码5分钟内有效，请在5分钟内完成验证。（请勿泄露此验证码）如非本人操作，请忽略该邮件。(这是一封自动发送的邮件，请不要直接回复）</span></span>\n" +
                "</p>\n" +
                "<p style=\"text-align:right;\">\n" +
                "\t<span style=\"background-color:#FFFFFF;font-size:16px;color:#000000;\"><span style=\"color:#000000;font-size:16px;background-color:#FFFFFF;\"><span class=\"token string\" style=\"font-family:&quot;font-size:16px;color:#000000;line-height:normal !important;background-color:#FFFFFF;\">B5-LibraryManagement</span></span></span> \n" +
                "</p>\n" +
                "<p style=\"text-align:right;\">\n" +
                "\t<span style=\"background-color:#FFFFFF;font-size:14px;\"><span style=\"color:#FF9900;font-size:18px;\"><span class=\"token string\" style=\"font-family:&quot;font-size:16px;color:#000000;line-height:normal !important;\"><span style=\"font-size:16px;color:#000000;background-color:#FFFFFF;\">"+"</span><span style=\"font-size:18px;color:#000000;background-color:#FFFFFF;\"></span></span></span></span> \n" +
                "</p>");
        javaMailSender.send(message);
    }
}
