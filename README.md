# Survivor App

A Quarkus REST application that exercises the fictional `wreckage` library.

## Architecture

- **wreckage** — A fictional library providing wreck management classes (`WreckageClient`, `RecoveryService`, `NotificationService`). This library will introduce breaking changes in future versions.
- **survivor-app** — A Quarkus REST application exposing REST endpoints that consume the wreckage library.

## REST API

| Method | Path | Description |
|--------|------|-------------|
| GET | `/incidents/{id}` | Retrieve incident details |
| POST | `/incidents/{id}/inspect` | Assess damage on a wreck |
| POST | `/incidents/{id}/recover` | Attempt to recover a wreck |
| GET | `/incidents/{id}/report` | Generate a full incident report |

## OpenRewrite Migration

This application is the initial **Wreckage 1.x** baseline. The `wreckage` library will introduce breaking changes in future versions, and OpenRewrite recipes will be used to migrate `survivor-app` between versions automatically.

## Build

```bash
cd survivor-app
mvn clean test
mvn clean package
```
