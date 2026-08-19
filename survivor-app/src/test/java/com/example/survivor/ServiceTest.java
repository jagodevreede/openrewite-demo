package com.example.survivor;

import com.wreckage.DamageAnalyzer;
import com.wreckage.IncidentLog;
import com.wreckage.NotificationCervice;
import com.wreckage.RecoveryService;
import com.wreckage.Severity;
import com.wreckage.Wreck;
import com.wreckage.WreckageClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ServiceTest {
    @Test
    void findingUnknownIncidentReturnsNull() {
        WreckageClient client = mock(WreckageClient.class);
        when(client.findWreck("missing")).thenReturn(null);

        IncidentService service = new IncidentService(client, mock(RecoveryService.class),
                mock(NotificationCervice.class), new DamageAnalyzer(), new IncidentLog());

        assertNull(service.findIncident("missing"));
    }

    @Test
    void generatingReportClassifiesDamage() {
        WreckageClient client = mock(WreckageClient.class);
        when(client.findWreck("wreck-001"))
                .thenReturn(new Wreck("wreck-001", "HMS Rewrite", "North Sea", 4, true));

        ReportService service = new ReportService(client, new DamageAnalyzer());

        assertEquals(Severity.MODERATE, service.generateReport("wreck-001").severity());
    }
}
