package com.example.survivor;

import com.wreckage.Wreck;
import com.wreckage.WreckageClient;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ReportService {

    private final WreckageClient wreckageClient;

    public ReportService() {
        this.wreckageClient = new WreckageClient();
    }

    public IncidentReportDTO generateReport(String id) {
        Wreck wreck = wreckageClient.findWreck(id);
        if (wreck == null) {
            return null;
        }
        String recoveryStatus = wreck.isRecoverable() ? "pending" : "unrecoverable";
        return new IncidentReportDTO(wreck.getId(), wreck.getName(), wreck.getLocation(),
                wreck.assessDamage().getDamageLevel(), wreck.assessDamage().isRecoverable(), recoveryStatus);
    }
}
