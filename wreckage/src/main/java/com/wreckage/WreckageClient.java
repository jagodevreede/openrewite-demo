package com.wreckage;

import java.util.HashMap;
import java.util.Map;

public final class WreckageClient {
    private final Map<String, Wreck> wrecks;

    public WreckageClient() {
        wrecks = new HashMap<>();
        wrecks.put("wreck-001", new Wreck("wreck-001", "HMS Rewrite", "North Sea", 4, true));
        wrecks.put("wreck-002", new Wreck("wreck-002", "USS Legacy", "Atlantic Ocean", 9, false));
        wrecks.put("wreck-003", new Wreck("wreck-003", "MV Refactor", "Pacific Rim", 2, true));
    }

    public Wreck findWreck(String id) {
        return wrecks.get(id);
    }
}
