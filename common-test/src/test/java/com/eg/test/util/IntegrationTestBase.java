package com.eg.test.util;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.OracleContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class IntegrationTestBase {

  @Container
  protected static final OracleContainer ORACLE =
    new OracleContainer("gvenzl/oracle-xe:21-slim")
      .withReuse(true);


  @Container
  protected static final KafkaContainer KAFKA =
    new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.2.2")
      .asCompatibleSubstituteFor("apache/kafka"))
      .withReuse(true);


  @Container
  protected static final GenericContainer<?> REDIS =
    new GenericContainer<>(DockerImageName.parse("redis:7.2.4"))
      .withExposedPorts(6379)
      .withReuse(true);

  static {
    ORACLE.start();
    KAFKA.start();
    REDIS.start();
  }


  @DynamicPropertySource
  static void registerProperties(DynamicPropertyRegistry registry) {
    System.out.println("Oracle running? " + ORACLE.isRunning());
    System.out.println("Kafka running? " + KAFKA.isRunning());
    System.out.println("Redis running? " + REDIS.isRunning());
    registry.add("spring.datasource.url", ORACLE::getJdbcUrl);
    registry.add("spring.datasource.username", ORACLE::getUsername);
    registry.add("spring.datasource.password", ORACLE::getPassword);

    registry.add("spring.kafka.bootstrap-servers", KAFKA::getBootstrapServers);

    registry.add("spring.data.redis.host", REDIS::getHost);
    registry.add("spring.data.redis.port", () -> REDIS.getMappedPort(6379));
  }
}
