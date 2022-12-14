# Kafka docker compose

This docker compose will host instances **Kafka**, **Zookeeper** and **Kafka UI**. And in this repository contain **Kafka Demo Service** that created as a Proof of Concepts for learn Apache Kafka and test it in local environment.

## Kafka Demo Service (Consumer and Producer)

After created Kafka **broker** then create a **consumer** and a **producer** by using **Java Spring Boot**

## Step 1

Create a Spring Boot project name **Kafka demo service** and add Kafka dependency (in this case use **Gradle** project)

```java
dependencies {
	...
	implementation 'org.springframework.kafka:spring-kafka'
	testImplementation 'org.springframework.kafka:spring-kafka-test'
}
```

## Step 2

Create a new **Topic** name `kafka-demo-topic` and create a **consumer** by create a method that will waits for messages of `kafka-demo-topic` using the `@KafkaListener` annotation in main class

```java
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
}
```

## Step 3

In the same project, I will create a `Controller` and create a method as a **producer** to produce messages to the `kafka-demo-topic` the I created.

```java
@RestController
public class KafkaController {
    @Autowired
    KafkaTemplate<String, String> template;
    private final String TOPIC_NAME = "kafka-demo-topic";

    @PostMapping(path = "message")
    public void sendMessageToKafkaConsumer(@RequestParam SendMessageDto sendMessageDto) {
        this.template.send(TOPIC_NAME, sendMessageDto.getMessage());
    }
}
```

## Step 4

Add the configuration in `applicaiton.properties` file to connect to Kafka broker

```java
#Server configuration
server.port=8081
server.servlet.context-path=/kafka-demo-service/v1/api

#Kafka configuration
spring.kafka.bootstrap-servers=localhost:9093
```

## Testing

After start **Kafka broker** and **Kafka demo service**, you will see the `kafka-demo-topic` in the **Kafka UI.**

And then try to call an API that created to produce message to Kafka

```bash
curl --location --request POST 'http://localhost:8081/kafka-demo-service/v1/api/message?message=Hello Kafka!'
```

You will see the message produced to the topic in **Kafka UI** and in the application log output stream.

```log
message received: Hello Kafka!
```

Credit : [Spring Boot Kafka example](https://marco.dev/spring-boot-kafka-tutorial)

## Example Kafka docker compose

[conduktor/kafka-stack-docker-compose](https://github.com/conduktor/kafka-stack-docker-compose)

## Broker configuration

- [Kafka Broker Configurations for Confluent Platform](https://docs.confluent.io/platform/current/installation/configuration/broker-configs.html)
- [Custom MSK configurations](https://docs.aws.amazon.com/msk/latest/developerguide/msk-configuration-properties.html)

## Founding issues

### offsets retention

- [Apache Kafka Consumer Group offset Retention](https://praveenganesh.medium.com/apache-kafka-consumer-group-offset-retention-bfe21285033a)
- [Problems with the retention period for offset topic of kafka](https://stackoverflow.com/questions/42546501/problems-with-the-retention-period-for-offset-topic-of-kafka)
- [Kafka consumer group offset retention](https://stackoverflow.com/questions/36977071/kafka-consumer-group-offset-retention)
- [Difference between retention configuration offsets.retention.minutes and log.retention.minutes](https://stackoverflow.com/questions/49027929/difference-between-retention-configuration-offsets-retention-minutes-and-log-ret)
