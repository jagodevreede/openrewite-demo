package com.example.survivor;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

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
            .statusCode(200)
            .body("damageLevel", is(4))
            .body("recoverable", is(true));
    }

    @Test
    void testRecoverIncident() {
        given()
            .when().post("/incidents/wreck-001/recover")
            .then()
            .statusCode(200)
            .body("recoveryStatus", equalTo("recovered"))
            .body("recoverable", is(true));
    }

    @Test
    void testRecoverUnrecoverableIncident() {
        given()
            .when().post("/incidents/wreck-002/recover")
            .then()
            .statusCode(200)
            .body("recoveryStatus", equalTo("unrecoverable"))
            .body("recoverable", is(false));
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
            .body("recoveryStatus", equalTo("pending"));
    }
}
