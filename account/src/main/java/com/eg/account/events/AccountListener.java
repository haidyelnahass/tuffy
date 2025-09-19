package com.eg.account.events;

import com.eg.account.model.message.ProfileDetailsMessage;
import org.springframework.kafka.annotation.KafkaListener;

@KafkaListener
public class AccountListener {

  @KafkaListener(topics = "${spring.kafka.account-ready}", groupId = "${spring.kafka.consumer.group-id}")
  public void consume(ProfileDetailsMessage message) {

  }
}
