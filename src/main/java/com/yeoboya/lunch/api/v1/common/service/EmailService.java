package com.yeoboya.lunch.api.v1.common.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }



    public void resetPassword(String toEmail) {
        MimeMessage message = mailSender.createMimeMessage();

        StringBuilder sb = new StringBuilder();
        sb.append("<html><body>");
        sb.append("<meta http-equiv='Content-Type' content='text/html; charset=euc-kr'>");
        sb.append("<h1>" + "Reset your Yeoboya-lunch password" + "<h1><br>");
        sb.append("<p>We heard that you lost your GitHub password. Sorry about that!</p>");
        sb.append("<p>But don’t worry! You can use the following button to reset your password:</p>");
        sb.append("<a href='none'>Reset your password</a>");
        sb.append("<p>If you don’t use this link within 3 hours, it will expire.</p>");
        sb.append("</body></html>");
        String body = sb.toString();

        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
            messageHelper.setSubject("[Yeoboya-lunch] Please reset your password");
            messageHelper.setTo(toEmail);
            messageHelper.setText(body, true);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        mailSender.send(message);
    }

}
