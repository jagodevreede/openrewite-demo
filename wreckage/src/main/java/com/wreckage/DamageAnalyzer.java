package com.wreckage;

public final class DamageAnalyzer {

    public Severity classify(int damageLevel) {
        if (damageLevel >= 8) {
            return Severity.CRITICAL;
        } else if (damageLevel >= 6) {
            return Severity.HIGH;
        } else if (damageLevel >= 4) {
            return Severity.MODERATE;
        } else if (damageLevel >= 1) {
            return Severity.LOW;
        } else {
            return Severity.NONE;
        }
    }
}
