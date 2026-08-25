package com.wreckage;

public record Wreck(String id, String name, String location,
                    int damageLevel, boolean recoverable) {}
