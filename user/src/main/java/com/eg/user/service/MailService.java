package com.eg.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {
  private final JavaMailSender javaMailSender;

  public void sendMail(Integer randomNumber, String toEmail) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom("no-reply@test.com");
    message.setTo(toEmail);
    message.setSubject("Confirmation Code");
    message.setText("Your confirmation code is " + randomNumber
      + ". Please use it within the next 10 minutes");

    javaMailSender.send(message);
  }
}
