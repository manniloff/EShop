package com.amdaris.mentoring.core.env;

import com.amdaris.mentoring.core.service.KafkaMessageSender;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Primary
@Service
public class KafkaTestMessageSender implements KafkaMessageSender<Object> {

    private Map<String, List<Object>> kafka = new HashMap<>();

    private String topic = "payment";

    public void reset() {
        kafka.clear();
    }

    @Override
    public void send(Object message) {
        if(!kafka.containsKey(topic)) {
            kafka.put(topic, new CopyOnWriteArrayList<>());
        }

        kafka.get(topic).add(message);
    }

    public List<Object> getSentMessages(String topicName) {
        if (!kafka.containsKey(topicName)) {
            return Collections.emptyList();
        }

        return kafka.get(topicName);
    }
}
