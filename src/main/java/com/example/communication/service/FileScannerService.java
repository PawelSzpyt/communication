package com.example.communication.service;

import com.example.communication.dao.CommunicationDAO;
import com.example.communication.model.Communication;
import com.example.communication.model.CommunicationStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ejb.Singleton;
import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Singleton
public class FileScannerService {

    @Inject
    private CommunicationDAO dao;

    private final String directoryPath = "communication_files";

    public void scanDirectory() {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(directoryPath), "*.{txt,json}")) {
            for (Path entry : stream) {
                String baseName = entry.getFileName().toString().split("\\.")[0];
                Path txtFile = Paths.get(directoryPath, baseName + ".txt");
                Path jsonFile = Paths.get(directoryPath, baseName + ".json");

                if (isValidCommunication(txtFile, jsonFile)) {
                    String body = Files.readString(txtFile);
                    String deliverySettings = Files.readString(jsonFile);

                    Communication communication = new Communication();
                    communication.setName(baseName);
                    communication.setBody(body);
                    communication.setDeliverySettings(deliverySettings);
                    communication.setSize((long) body.length());
                    communication.setType(getTypeFromJson(deliverySettings));
                    communication.setStatus(CommunicationStatus.LOADED);
                    dao.save(communication);

                    Files.delete(txtFile);
                    Files.delete(jsonFile);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isValidCommunication(Path txtFile, Path jsonFile) throws IOException {
        return Files.exists(txtFile) && containsCommunicationType(jsonFile);
    }

    private boolean containsCommunicationType(Path jsonFile) throws IOException {
        return Files.exists(jsonFile) && getTypeFromJson(Files.readString(jsonFile)) != null;
    }

    private String getTypeFromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> settings = null;
        try {
            Map<String, String> map = mapper.readValue(json, Map.class);
            return map.get("type");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
