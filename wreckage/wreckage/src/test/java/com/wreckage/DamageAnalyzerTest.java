package com.wreckage;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DamageAnalyzerTest {

    private final DamageAnalyzer analyzer = new DamageAnalyzer();

    @Test
    @DisplayName("levels 0 → NONE")
    void classify_zero() {
        assertEquals(Severity.NONE, analyzer.classify(0));
    }

    @Test
    @DisplayName("levels 1-3 → LOW")
    void classify_low() {
        assertEquals(Severity.LOW, analyzer.classify(1));
        assertEquals(Severity.LOW, analyzer.classify(3));
    }

    @Test
    @DisplayName("levels 4-5 → MODERATE")
    void classify_moderate() {
        assertEquals(Severity.MODERATE, analyzer.classify(4));
        assertEquals(Severity.MODERATE, analyzer.classify(5));
    }

    @Test
    @DisplayName("levels 6-7 → HIGH")
    void classify_high() {
        assertEquals(Severity.HIGH, analyzer.classify(6));
        assertEquals(Severity.HIGH, analyzer.classify(7));
    }

    @Test
    @DisplayName("levels 8+ → CRITICAL")
    void classify_critical() {
        assertEquals(Severity.CRITICAL, analyzer.classify(8));
        assertEquals(Severity.CRITICAL, analyzer.classify(9));
    }
}
