package com.example.survivor;

public record IncidentReportDTO(String id, String name, String location,
                                int damageLevel, boolean recoverable,
                                String recoveryStatus) {}
