package com.example.communication.dao;

import com.example.communication.model.Communication;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@ApplicationScoped
public class CommunicationDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(Communication communication) {
        entityManager.persist(communication);
    }

    public List<Communication> findAll() {
        return entityManager.createQuery("SELECT c FROM Communication c", Communication.class).getResultList();
    }

    public Communication findById(Long id) {
        return entityManager.find(Communication.class, id);
    }

    public void update(Communication communication) {
        entityManager.merge(communication);
    }

    public void delete(Long id) {
        Communication communication = findById(id);
        if (communication != null) {
            entityManager.remove(communication);
        }
    }
}