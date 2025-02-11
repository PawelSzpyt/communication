package com.example.communication.rest;

import com.example.communication.model.Communication;
import com.example.communication.model.CommunicationStatus;
import com.example.communication.service.CommunicationService;
import com.example.communication.validation.CommunicationValidator;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/communications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CommunicationResource {

    @Inject
    private CommunicationService service;

    @GET
    public Response getAllCommunications() {
        List<Communication> communications = service.getAllCommunications();
        if (communications.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(communications).build();
    }

    @GET
    @Path("/{id}")
    public Response getCommunication(@PathParam("id") Long id) {
        Communication communication = service.getCommunication(id);
        if (communication == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Communication not found for ID: " + id).build();
        }
        return Response.ok(communication).build();
    }

    @POST
    public Response createCommunication(@Valid Communication communication) {
        List<String> errors = CommunicationValidator.validate(communication);
        if (!errors.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(errors).build();
        }

        try {
            service.saveCommunication(communication);
            return Response.status(Response.Status.CREATED).entity("Communication created successfully").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error creating communication: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateCommunication(@PathParam("id") Long id, @Valid Communication communication) {
        Communication existingCommunication = service.getCommunication(id);
        if (existingCommunication == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Communication not found for ID: " + id).build();
        }

        List<String> errors = CommunicationValidator.validate(communication);
        if (!errors.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(errors).build();
        }

        try {
            communication.setId(id);
            service.updateCommunication(communication);
            return Response.ok("Communication updated successfully").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error updating communication: " + e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCommunication(@PathParam("id") Long id) {
        Communication existingCommunication = service.getCommunication(id);
        if (existingCommunication == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Communication not found for ID: " + id).build();
        }
        try {
            service.deleteCommunication(id);
            return Response.noContent().entity("Communication deleted successfully").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error deleting communication: " + e.getMessage()).build();
        }
    }

    @POST
    @Path("/{id}/deliver")
    public Response deliverCommunication(@PathParam("id") Long id) {
        Communication communication = service.getCommunication(id);
        if (communication == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Communication not found for ID: " + id).build();
        }
        try {
            CommunicationStatus status = service.deliverCommunication(id);
            return Response.ok(String.format("Communication status: %s" + status.name())).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error delivering communication: " + e.getMessage()).build();
        }
    }

    @POST
    @Path("/batch_delivery")
    public Response batchDeliverCommunications() {
        try {
            service.batchDeliverCommunications();
            return Response.ok("Batch delivery of communications completed successfully").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error in batch delivery: " + e.getMessage()).build();
        }
    }
}