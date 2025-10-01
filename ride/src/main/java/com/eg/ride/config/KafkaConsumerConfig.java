package com.eg.ride.config;

import com.eg.ride.model.message.DriverAssignmentMessage;
import com.eg.ride.model.message.RideMatchFailedMessage;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;

  @Value("${spring.kafka.consumer.group-id}")
  private String groupId;

  @Bean
  public ConsumerFactory<String, RideMatchFailedMessage> rideMatchFailedMessageConsumerFactory() {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
    props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, RideMatchFailedMessage.class.getName());
    props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
    return new DefaultKafkaConsumerFactory<>(props);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, RideMatchFailedMessage> rideMatchFailedConsumerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, RideMatchFailedMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(rideMatchFailedMessageConsumerFactory());
    return factory;
  }

  @Bean
  public ConsumerFactory<String, DriverAssignmentMessage> driverAssignmentMessageConsumerFactory() {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
    props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, DriverAssignmentMessage.class.getName());
    props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
    return new DefaultKafkaConsumerFactory<>(props);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, DriverAssignmentMessage> driverAssignmentKafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, DriverAssignmentMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(driverAssignmentMessageConsumerFactory());
    return factory;
  }
}
