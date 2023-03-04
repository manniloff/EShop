package com.amdaris.mentoring.payment.env;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@TestConfiguration
public class ExternalMessageTestConfiguration {
    private TestExternalMessageTestService testExternalMessageService;

    @PostConstruct
    public void init() {
        testExternalMessageService = new TestExternalMessageTestService();
    }

    public void reset() {
        testExternalMessageService.reset();
    }

    public List<String> getSentMessages(String topicName) {
        if (!testExternalMessageService.getSentMessages().containsKey(topicName)) {
            return Collections.emptyList();
        }

        return testExternalMessageService.getSentMessages().get(topicName);
    }

    @Primary
    @Bean
    public ExternalMessageTestService externalMessageService() {
        return testExternalMessageService;
    }

    public void onReceive(String topic, Object message) {
        testExternalMessageService.onReceive(topic, message);
    }

    static class TestExternalMessageTestService implements ExternalMessageTestService {
        private Map<String, List<ReceiveListener>> listeners = new ConcurrentHashMap<>();

        private final ObjectMapper objectMapper = new ObjectMapper();

        @Getter
        private Map<String, List<String>> sentMessages = new ConcurrentHashMap<>();

        public void reset() {
            sentMessages.clear();
        }

        @Override
        public void send(String topic, Object message) {
            if (!sentMessages.containsKey(topic)) {
                sentMessages.put(topic, new CopyOnWriteArrayList<>());
            }

            try {
                sentMessages.get(topic).add(objectMapper.writeValueAsString(message));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        public void onReceive(String topic, Object message) {
            if (!listeners.containsKey(topic)) {
                return;
            }

            for (ReceiveListener receiveListener : listeners.get(topic)) {
                receiveListener.onReceive(topic, message);
            }
        }

        @Override
        public void addReceiveListener(String topic, ReceiveListener listener) {
            if (!listeners.containsKey(topic)) {
                listeners.put(topic, new CopyOnWriteArrayList<>());
            }

            listeners.get(topic).add(listener);
        }
    }
}
