package com.example.communication.service;

import com.example.communication.dao.CommunicationDAO;
import com.example.communication.model.Communication;
import com.example.communication.model.CommunicationStatus;

import javax.ejb.Singleton;
import javax.inject.Inject;
import java.util.List;

@Singleton
public class CommunicationService {

    @Inject
    private CommunicationDAO dao;

    @Inject
    private ProcessingService processingService;

    public List<Communication> getAllCommunications() {
        return dao.findAll();
    }

    public Communication getCommunication(Long id) {
        return dao.findById(id);
    }

    public void saveCommunication(Communication communication) {
        dao.save(communication);
    }

    public void updateCommunication(Communication communication) {
        dao.update(communication);
    }

    public void deleteCommunication(Long id) {
        dao.delete(id);
    }

    public void processCommunications() {
        processingService.processUnprocessedCommunications();
    }

    public CommunicationStatus deliverCommunication(Long id) {
        return processingService.deliverSingleCommunication(dao.findById(id));
    }

    public void batchDeliverCommunications() {
        processingService.deliverAllCommunications();
    }
}