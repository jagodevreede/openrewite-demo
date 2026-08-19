package com.example.survivor;

import com.wreckage.DamageAnalyzer;
import com.wreckage.Severity;
import com.wreckage.Wreck;
import com.wreckage.WreckageClient;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.logging.Level;
import java.util.logging.Logger;
@ApplicationScoped
public class ReportService {
    private static final Logger LOGGER = Logger.getLogger(ReportService.class.getName());

    private final WreckageClient wreckageClient;
    private final DamageAnalyzer damageAnalyzer;

    public ReportService() {
        this(new WreckageClient(), new DamageAnalyzer());
    }

    ReportService(WreckageClient wreckageClient, DamageAnalyzer damageAnalyzer) {
        this.wreckageClient = wreckageClient;
        this.damageAnalyzer = damageAnalyzer;
    }

    public IncidentReportDTO generateReport(String id) {
        LOGGER.log(Level.INFO, "Generating report for incident {0}", id);
        Wreck wreck = wreckageClient.findWreck(id);
        if (wreck == null) {
            LOGGER.log(Level.WARNING, "Cannot generate report for missing incident: {0}", id);
            return null;
        }
        Severity severity = damageAnalyzer.classify(wreck.damageLevel());
        String recoveryStatus = switch (severity) {
            case MODERATE, LOW -> "pending";
            case CRITICAL, HIGH -> "unrecoverable";
            case NONE -> "recovered";
        };
        return new IncidentReportDTO(wreck.id(), wreck.name(), wreck.location(),
                wreck.damageLevel(), wreck.recoverable(), severity, recoveryStatus);
    }
}
