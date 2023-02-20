package com.amdaris.mentoring.payment.kafka;

public interface ExternalMessageService {
    void send(String topic, String message);
    void addReceiveListener(String topic, ReceiveListener listener);

    interface ReceiveListener {
        void onReceive(String topic, String message);
    }
}