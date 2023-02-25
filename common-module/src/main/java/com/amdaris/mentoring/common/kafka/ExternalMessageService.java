package com.amdaris.mentoring.common.kafka;

public interface ExternalMessageService<T> {
    void send(String topic, T message);

    void addReceiveListener(String topic, ReceiveListener listener);

    interface ReceiveListener {
        void onReceive(String topic, Object message);
    }
}
