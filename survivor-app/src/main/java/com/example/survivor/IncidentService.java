package com.example.survivor;

import com.wreckage.DamageReport;
import com.wreckage.NotificationService;
import com.wreckage.RecoveryService;
import com.wreckage.Wreck;
import com.wreckage.WreckageClient;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class IncidentService {

    private final WreckageClient wreckageClient;
    private final RecoveryService recoveryService;
    private final NotificationService notificationService;

    public IncidentService() {
        this.wreckageClient = new WreckageClient();
        this.recoveryService = new RecoveryService();
        this.notificationService = new NotificationService();
    }

    public IncidentDTO findIncident(String id) {
        Wreck wreck = wreckageClient.findWreck(id);
        if (wreck == null) {
            return null;
        }
        return new IncidentDTO(wreck.getId(), wreck.getName(), wreck.getLocation());
    }

    public DamageReportDTO inspectIncident(String id) {
        Wreck wreck = wreckageClient.findWreck(id);
        if (wreck == null) {
            return null;
        }
        return new DamageReportDTO(id, wreck.assessDamage().getDamageLevel(), wreck.assessDamage().isRecoverable());
    }

    public IncidentReportDTO recoverIncident(String id) {
        Wreck wreck = wreckageClient.findWreck(id);
        if (wreck == null) {
            return null;
        }
        DamageReport damageReport = wreck.assessDamage();
        String recoveryStatus = "pending";
        if (wreck.isRecoverable()) {
            try {
                recoveryService.recover(wreck);
                notificationService.notifySurvivors(wreck);
                recoveryStatus = "recovered";
            } catch (IllegalArgumentException e) {
                recoveryStatus = "unrecoverable";
            }
        } else {
            recoveryStatus = "unrecoverable";
        }
        return new IncidentReportDTO(wreck.getId(), wreck.getName(), wreck.getLocation(),
                damageReport.getDamageLevel(), damageReport.isRecoverable(), recoveryStatus);
    }
}
