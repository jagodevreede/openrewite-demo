package com.example.survivor;

public record IncidentDTO(String id, String name, String location) {
    public String toHumanString() {
        return String.format("%s is at location %s", name, location);
    }

}
