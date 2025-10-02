package com.eg.user.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MailServiceTest {

  @InjectMocks
  private MailService mailService;

  @Mock
  private JavaMailSender javaMailSender;

  @Test
  void sendMailSuccessfully() {
    doNothing().when(javaMailSender).send((SimpleMailMessage) any());
    mailService.sendMail(211222, "aa@aa.com");
    verify(javaMailSender, times(1)).send((SimpleMailMessage) any());
  }

}
