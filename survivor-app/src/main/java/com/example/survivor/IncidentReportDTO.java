package com.example.survivor;

import com.wreckage.Severity;

public record IncidentReportDTO(String id, String name, String location,
                                int damageLevel, boolean recoverable,
                                Severity severity, String recoveryStatus) {}
