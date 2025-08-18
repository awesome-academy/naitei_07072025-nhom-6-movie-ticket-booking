package com.org.Movie_Ticket_Booking.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;

    public void sendMail(String toEmail, String subject, String content){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

    public void sendVerificationEmail(String toEmail, String token, String baseURL){
        String link = baseURL + token;
        String content = "Bạn đã đăng ký tài khoản thành công! " +
                "Vui lòng click vào link sau để xác nhận tài khoản " + link;
        sendMail(toEmail,"Xác thực tài koản", content);
    }
}
