# openrewrite-demo-app

Two independent Maven projects at the repo root. No parent POM. And is a demo project without any functionalit, code can
be added or changed at will. Keep existing patterns even if its nog logical, this can be for a demo. For example a class
remains a class even if it can be a record, a lot of if statements remain even if it can be folded or moved in to a
switch etc...

## Structure

- `wreckage/` — library (Wreck, WreckageClient, DamageReport, services)
- `survivor-app/` — Quarkus app (IncidentService, ReportService, SurvivorResource)

## Build

```bash
./build.sh
```

wreckage must be installed first (mvn install).

## Stack

Java 21 (or higher), Quarkus 3 Jakarta REST, JUnit 5, RestAssured.

## Conventions

- Java records used for DTOs.
- Agent commits: append `Co-authored-by: <self-identified-model>` (see `skills/commit-coauthor/`).
- You do not run openrewrite
