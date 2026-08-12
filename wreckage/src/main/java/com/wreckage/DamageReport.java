package com.wreckage;

public final class DamageReport {
    private final int damageLevel;
    private final boolean recoverable;

    public DamageReport(int damageLevel, boolean recoverable) {
        this.damageLevel = damageLevel;
        this.recoverable = recoverable;
    }

    public int getDamageLevel() { return damageLevel; }
    public boolean isRecoverable() { return recoverable; }
}
