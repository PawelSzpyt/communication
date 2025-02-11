package com.example.communication.service.channel;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.ThreadLocalRandom;

@ApplicationScoped
public class SmsChannel implements CommunicationChannel {

    @Override
    public void send(String body, String deliverySettings) {
        try {
            int delay = ThreadLocalRandom.current().nextInt(500, 3000);
            Thread.sleep(delay);
            System.out.println("Fake SMS sent with body: " + body);
            System.out.println("Delivery settings: " + deliverySettings);
        } catch (InterruptedException e) {
            System.err.println("SMS sending interrupted.");
        }
    }

    @Override
    public String getName() {
        return "sms";
    }
}