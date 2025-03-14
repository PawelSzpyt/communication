package com.example.communication.service.channel;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.ThreadLocalRandom;

@ApplicationScoped
public class WhatsAppChannel implements CommunicationChannel {

    @Override
    public void send(String body, String deliverySettings) {
        try {
            int delay = ThreadLocalRandom.current().nextInt(500, 3000);
            Thread.sleep(delay);
            System.out.println("Fake WhatsApp message sent with body: " + body);
            System.out.println("Delivery settings: " + deliverySettings);
        } catch (InterruptedException e) {
            System.err.println("WhatsApp message sending interrupted.");
        }
    }

    @Override
    public String getName() {
        return "whatsapp";
    }
}