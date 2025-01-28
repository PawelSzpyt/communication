package com.example.communication.validation;

import com.example.communication.model.Communication;

import java.util.ArrayList;
import java.util.List;

public class CommunicationValidator {

    public static List<String> validate(Communication communication) {
        List<String> errors = new ArrayList<>();

        if (communication.getName() == null || communication.getName().isBlank()) {
            errors.add("Name is mandatory");
        }

        if (communication.getBody() == null || communication.getBody().isBlank()) {
            errors.add("Body is mandatory");
        }

        if (communication.getDeliverySettings() == null || communication.getDeliverySettings().isBlank()) {
            errors.add("Delivery settings are mandatory");
        }

        if (communication.getSize() == null) {
            errors.add("Size is mandatory");
        }

        if (communication.getStatus() == null) {
            errors.add("Status is mandatory");
        }

        if (communication.getType() == null || communication.getType().isBlank()) {
            errors.add("Type is mandatory");
        }

        return errors;
    }
}