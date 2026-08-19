package com.example.survivor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IncidentDTOTest {

    @Test
    @DisplayName("toHumanString returns name and location")
    void toHumanString_returnsNameAndLocation() {
        IncidentDTO dto = new IncidentDTO("wreck-001", "HMS Rewrite", "North Sea");

        String result = dto.toHumanString();

        assertEquals("HMS Rewrite is at location North Sea", result);
    }

    @Test
    @DisplayName("toHumanString handles null fields")
    void toHumanString_handlesNullFields() {
        IncidentDTO dto = new IncidentDTO("wreck-002", null, null);

        String result = dto.toHumanString();

        assertEquals("null is at location null", result);
    }
}
