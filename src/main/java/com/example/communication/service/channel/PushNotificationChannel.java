package com.example.communication.service.channel;


import java.util.concurrent.ThreadLocalRandom;

public class PushNotificationChannel implements CommunicationChannel {

    @Override
    public void send(String body, String deliverySettings) {
        try {
            int delay = ThreadLocalRandom.current().nextInt(500, 3000);
            Thread.sleep(delay);
            System.out.println("Fake push notification sent with body: " + body);
            System.out.println("Delivery settings: " + deliverySettings);
        } catch (InterruptedException e) {
            System.err.println("Push notification sending interrupted.");
        }
    }
}