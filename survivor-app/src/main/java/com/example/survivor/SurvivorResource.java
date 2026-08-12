package com.example.survivor;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
@Path("/incidents")
public class SurvivorResource {

    private final IncidentService incidentService;
    private final ReportService reportService;

    public SurvivorResource(IncidentService incidentService, ReportService reportService) {
        this.incidentService = incidentService;
        this.reportService = reportService;
    }

    @GET
    @Path("/{id}")
    public Response getIncident(@PathParam("id") String id) {
        IncidentDTO dto = incidentService.findIncident(id);
        if (dto == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(dto).build();
    }

    @POST
    @Path("/{id}/inspect")
    public Response inspectIncident(@PathParam("id") String id) {
        DamageReportDTO dto = incidentService.inspectIncident(id);
        if (dto == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(dto).build();
    }

    @POST
    @Path("/{id}/recover")
    public Response recoverIncident(@PathParam("id") String id) {
        IncidentReportDTO dto = incidentService.recoverIncident(id);
        if (dto == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(dto).build();
    }

    @GET
    @Path("/{id}/report")
    public Response getReport(@PathParam("id") String id) {
        IncidentReportDTO dto = reportService.generateReport(id);
        if (dto == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(dto).build();
    }
}
