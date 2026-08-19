package com.example.survivor;

import com.wreckage.Severity;

public record DamageReportDTO(String id, int damageLevel, boolean recoverable, Severity severity) {}
