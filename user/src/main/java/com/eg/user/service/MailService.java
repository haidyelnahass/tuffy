package com.eg.user.service;

import com.eg.user.exception.BadRequestException;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.eg.user.exception.model.ErrorCode.EMAIL_FAILURE;
import static com.eg.user.util.Constants.FAILED_TO_SEND_EMAIL;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {
  private final SendGrid sendGrid;

  @Value("${sendgrid.from-mail}")
  private String fromMail;

  public void sendMail(String contentText, String subject, String to) {

    Email from = new Email(fromMail, "Tuffy Team");
    Email toEmail = new Email(to);
    Content content = new Content("text/plain", contentText);
    Mail mail = new Mail(from, subject, toEmail, content);
    Request request = new Request();

    log.info("Attempting to send email");
    try {
      request.setMethod(Method.POST);
      request.setEndpoint("mail/send");
      request.setBody(mail.build());
      Response response = sendGrid.api(request);
      log.info("Response status : {}", response.getStatusCode());
      log.info("Response body: {}", response.getBody());
    } catch (IOException e) {
      throw new BadRequestException(FAILED_TO_SEND_EMAIL, EMAIL_FAILURE);
    }

  }
}
