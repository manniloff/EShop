package com.amdaris.mentoring.payment.env;

public interface ExternalMessageTestService {
    void send(String topic, Object message);
    void addReceiveListener(String topic, ReceiveListener listener);

    interface ReceiveListener {
        void onReceive(String topic, Object message);
    }
}
