package com.example.communication.service.channel;

public interface CommunicationChannel {
    void send(String body, String deliverySettings);
}
