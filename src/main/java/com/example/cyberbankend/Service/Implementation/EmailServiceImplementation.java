package com.example.cyberbankend.Service.Implementation;

import com.example.cyberbankend.Dto.Request.EmailDetails;
import com.example.cyberbankend.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImplementation implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("raqibadeniran03@gmail.com")
    private String senderMail;
    @Override
    public void sendEmail(EmailDetails emailDetails) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(senderMail);
            mailMessage.setTo(emailDetails.getRecipient());
            mailMessage.setSubject(emailDetails.getSubject());
            mailMessage.setText(emailDetails.getMessageBody());

            javaMailSender.send(mailMessage);
            System.out.println("Mail sent successfully");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
