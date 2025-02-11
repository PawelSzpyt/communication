package com.example.communication.service.channel;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class CommunicationChannelProducer {

    @Inject
    @Any
    private Instance<CommunicationChannel> channels;

    @Produces
    @ApplicationScoped
    public Map<String, CommunicationChannel> produceChannelMap() {
        Map<String, CommunicationChannel> map = new HashMap<>();
        for (CommunicationChannel channel : channels) {
            map.put(channel.getName(), channel);
        }
        return map;
    }
}