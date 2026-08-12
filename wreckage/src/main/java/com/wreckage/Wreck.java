package com.wreckage;

public final class Wreck {
    private final String id;
    private final String name;
    private final String location;
    private final int damageLevel;
    private final boolean recoverable;

    public Wreck(String id, String name, String location, int damageLevel, boolean recoverable) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.damageLevel = damageLevel;
        this.recoverable = recoverable;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public int getDamageLevel() { return damageLevel; }
    public boolean isRecoverable() { return recoverable; }

    public DamageReport assessDamage() {
        return new DamageReport(damageLevel, recoverable);
    }
}
