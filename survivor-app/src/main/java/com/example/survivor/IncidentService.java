package com.example.survivor;

import com.wreckage.DamageAnalyzer;
import com.wreckage.IncidentLog;
import com.wreckage.NotificationService;
import com.wreckage.RecoveryService;
import com.wreckage.Severity;
import com.wreckage.Wreck;
import com.wreckage.WreckageClient;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class IncidentService {
    private static final Logger LOGGER = Logger.getLogger(IncidentService.class.getName());

    private final WreckageClient wreckageClient;
    private final RecoveryService recoveryService;
    private final NotificationService notificationService;
    private final DamageAnalyzer damageAnalyzer;
    private final IncidentLog incidentLog;

    public IncidentService() {
        this(new WreckageClient(), new RecoveryService(), new NotificationService(), new DamageAnalyzer(), new IncidentLog());
    }

    IncidentService(WreckageClient wreckageClient, RecoveryService recoveryService,
                    NotificationService notificationService, DamageAnalyzer damageAnalyzer,
                    IncidentLog incidentLog) {
        this.wreckageClient = wreckageClient;
        this.recoveryService = recoveryService;
        this.notificationService = notificationService;
        this.damageAnalyzer = damageAnalyzer;
        this.incidentLog = incidentLog;
    }

    public IncidentDTO findIncident(String id) {
        LOGGER.log(Level.INFO, "Finding incident {0}", id);
        Wreck wreck = wreckageClient.findWreck(id);
        if (wreck == null) {
            LOGGER.log(Level.WARNING, "Incident not found: {0}", id);
            return null;
        }
        return new IncidentDTO(wreck.id(), wreck.name(), wreck.location());
    }

    public DamageReportDTO inspectIncident(String id) {
        Wreck wreck = wreckageClient.findWreck(id);
        if (wreck == null) {
            return null;
        }
        Severity severity = damageAnalyzer.classify(wreck.damageLevel());
        return new DamageReportDTO(wreck.id(), wreck.damageLevel(), wreck.recoverable(), severity);
    }

    public IncidentReportDTO recoverIncident(String id) {
        LOGGER.log(Level.INFO, "Recovering incident {0}", id);
        Wreck wreck = wreckageClient.findWreck(id);
        if (wreck == null) {
            LOGGER.log(Level.WARNING, "Cannot recover missing incident: {0}", id);
            return null;
        }
        Severity severity = damageAnalyzer.classify(wreck.damageLevel());
        incidentLog.log("Recovery requested for " + wreck.name());
        String recoveryStatus = switch (severity) {
            case MODERATE, LOW -> {
                try {
                    recoveryService.recover(wreck);
                    notificationService.notifySurvivors(wreck);
                    yield "recovered";
                } catch (IllegalArgumentException e) {
                    yield "unrecoverable";
                }
            }
            case CRITICAL, HIGH -> "unrecoverable";
            case NONE -> "recovered";
        };
        return new IncidentReportDTO(wreck.id(), wreck.name(), wreck.location(),
                wreck.damageLevel(), wreck.recoverable(), severity, recoveryStatus);
    }

    public String assessIncident(String id) {
        Wreck wreck = wreckageClient.findWreck(id);
        if (wreck == null) {
            return null;
        }
        return assessIncident(wreck, damageAnalyzer.classify(wreck.damageLevel()));
    }

    public String assessIncident(Wreck wreck, Severity severity) {
        String wreckAssessment = assessWreck(wreck);
        String severityAssessment;
        if (severity == Severity.CRITICAL) {
            severityAssessment = "urgent";
        } else if (severity == Severity.HIGH) {
            severityAssessment = "high";
        } else {
            severityAssessment = "normal";
        }
        return String.format("%s/%s", wreckAssessment, severityAssessment);
    }

    private String assessWreck(Wreck wreck) {
        switch ((Object) wreck) {
            case Wreck w -> {
                if (w.damageLevel() >= 8) {
                    return "critical";
                } else if (w.damageLevel() >= 4) {
                    return "moderate";
                } else {
                    return "minor";
                }
            }
            default -> throw new IllegalStateException("Unknown wreck: " + wreck);
        }
    }
 }

