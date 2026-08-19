package com.example.survivor;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class SurvivorResourceTest {

    @Inject
    IncidentService incidentService;

    @Inject
    ReportService reportService;

    @Test
    void testGetIncident() {
        given()
            .when().get("/incidents/wreck-001")
            .then()
            .statusCode(200)
            .body("id", equalTo("wreck-001"))
            .body("name", equalTo("HMS Rewrite"))
            .body("location", equalTo("North Sea"));
    }

    @Test
    void testGetNonexistentIncident() {
        given()
            .when().get("/incidents/nonexistent")
            .then()
            .statusCode(404);
    }

    @Test
    void testInspectIncident() {
        given()
            .when().post("/incidents/wreck-001/inspect")
            .then()
            .body("damageLevel", is(4))
            .body("recoverable", is(true))
            .body("severity", equalTo("MODERATE"));
    }

    @Test
    void testRecoverIncident() {
        given()
            .when().post("/incidents/wreck-001/recover")
            .then()
            .body("recoveryStatus", equalTo("recovered"))
            .body("recoverable", is(true))
            .body("severity", equalTo("MODERATE"));
    }

    @Test
    void testRecoverUnrecoverableIncident() {
        given()
            .when().post("/incidents/wreck-002/recover")
            .then()
            .body("recoveryStatus", equalTo("unrecoverable"))
            .body("recoverable", is(false))
            .body("severity", equalTo("CRITICAL"));
    }

    @Test
    void testGenerateReport() {
        given()
            .when().get("/incidents/wreck-003/report")
            .then()
            .statusCode(200)
            .body("id", equalTo("wreck-003"))
            .body("name", equalTo("MV Refactor"))
            .body("location", equalTo("Pacific Rim"))
            .body("damageLevel", is(2))
            .body("recoverable", is(true))
            .body("severity", equalTo("LOW"))
            .body("recoveryStatus", equalTo("pending"));
    }
}
