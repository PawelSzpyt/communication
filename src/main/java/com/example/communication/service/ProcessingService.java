package com.example.communication.service;

import com.example.communication.dao.CommunicationDAO;
import com.example.communication.model.Communication;
import com.example.communication.model.CommunicationStatus;
import com.example.communication.service.channel.CommunicationChannel;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Singleton
@Startup
public class ProcessingService {

    @Inject
    private CommunicationDAO dao;

    @Inject
    private Map<String, CommunicationChannel> communicationChannels;

    private int intervalSeconds;

    @PostConstruct
    public void init() {
        intervalSeconds = Integer.parseInt(
                System.getenv().getOrDefault("processing.interval.seconds", "120")
        );

        startScheduler();
    }

    private void startScheduler() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                processUnprocessedCommunications();
            }
        }, 0, intervalSeconds * 1000L);
    }

    public void processUnprocessedCommunications() {
        List<Communication> unprocessed = dao.findAll().stream()
                .filter(c -> c.getStatus() == CommunicationStatus.LOADED)
                .toList();

        for (Communication communication : unprocessed) {
            if (communicationChannels.containsKey(communication.getType())) {
                communication.setStatus(CommunicationStatus.PROCESSED);
                dao.update(communication);
            } else {
                System.err.println("Unsupported communication type: " + communication.getType());
                communication.setStatus(CommunicationStatus.ERROR);
                dao.update(communication);
            }
        }
    }

    @Transactional
    public CommunicationStatus deliverSingleCommunication(Communication communication) {
        try {
            CommunicationChannel channel = communicationChannels.get(communication.getType());
            channel.send(communication.getBody(), communication.getDeliverySettings());
            communication.setStatus(CommunicationStatus.SENT);
        } catch (Exception e) {
            System.err.println("Error delivering communication ID: " + communication.getId());
            e.printStackTrace();
            communication.setStatus(CommunicationStatus.ERROR);
        } finally {
            dao.update(communication);
        }
        return communication.getStatus();
    }

    public void deliverAllCommunications() {
        int threadPoolSize = getThreadPoolSize();
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);

        List<Communication> toDeliver = dao.findAll().stream()
                .filter(c -> c.getStatus() == CommunicationStatus.PROCESSED)
                .toList();

        for (Communication communication : toDeliver) {
            executor.submit(() -> {
                deliverSingleCommunication(communication);
            });
        }

        executor.shutdown();
        try {
            boolean completed = executor.awaitTermination(5, TimeUnit.MINUTES);
            if (completed) {
                System.out.println("Communications delivery processed successfully");
            } else {
                System.out.println("Communications delivery timed out");
            }
        } catch (InterruptedException e) {
            System.out.println("Communications delivery was interrupted");
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    private int getThreadPoolSize() {
        return Integer.parseInt(System.getenv().getOrDefault("THREAD_POOL_SIZE", "10"));
    }
}