package com.zeke.service.impl;

import com.zeke.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailServiceImpl implements MailService {

    private static final Logger log = LoggerFactory.getLogger(MailServiceImpl.class);
    /**
     * springboot专门发送邮件接口
     */
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${my-email.title}")
    private String emailTitle;

    public MailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public boolean sendMail(String to, String code, String text) {
        MimeMessage msg = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            //发送邮件的邮箱
            helper.setFrom(from);
            //发送到哪(邮箱)
            helper.setTo(to);
            //邮箱标题
            helper.setSubject(emailTitle);
            msg.setText(String.format(text, code), "utf-8", "html");
            mailSender.send(msg);
        } catch (MailException | MessagingException e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }


}