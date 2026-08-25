package com.wreckage;

import java.util.ArrayList;
import java.util.List;

/** Application data, deliberately shaped like a logger for the migration demo. */
public final class IncidentLog {
    private final List<String> entries = new ArrayList<>();

    public void log(String message) {
        entries.add(message);
    }

    public List<String> entries() {
        return List.copyOf(entries);
    }
}
