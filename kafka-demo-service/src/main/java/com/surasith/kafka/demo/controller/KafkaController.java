package com.surasith.kafka.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaController {
    @Autowired
    KafkaTemplate<String, String> template;
    private final String TOPIC_NAME = "kafka-demo-topic";

    @PostMapping(path = "message")
    public void sendMessageToKafkaConsumer(@RequestParam String message) {
        this.template.send(TOPIC_NAME, message);
    }
}
