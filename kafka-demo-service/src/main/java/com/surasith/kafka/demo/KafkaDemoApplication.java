package com.surasith.kafka.demo;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
public class KafkaDemoApplication {
	private final String TOPIC_NAME = "kafka-demo-topic";

	public static void main(String[] args) {
		SpringApplication.run(KafkaDemoApplication.class, args);
	}

	/**
	 * With NewTopic we create a topic in kafka if it doesn't exist yet
	 */
	@Bean
	public NewTopic topic() {
		return TopicBuilder.name(TOPIC_NAME)
				.partitions(5)
				.replicas(1)
				.build();
	}

	@KafkaListener(id = "kafka-spring-listener", topics = TOPIC_NAME)
	public void listenMessage(String message) {
		System.out.println("message received: " + message);
	}

//	@Bean
//	public ApplicationRunner runner(KafkaTemplate<String, String> template) {
//		return args -> {
//			template.send(TOPIC_NAME, "test message at bootstrap");
//		};
//	}
}
