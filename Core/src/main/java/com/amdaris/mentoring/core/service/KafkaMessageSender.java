package com.amdaris.mentoring.core.service;

public interface KafkaMessageSender<T> {
    void send(T message);
}
