# OpenRewrite Migration Demo

This repository is a **demo project**, not a general-purpose application. It uses a small fictional Quarkus application and library to demonstrate how OpenRewrite can automate a realistic library migration.

The complete presentation is documented in [`DEMO-RUNBOOK.md`](DEMO-RUNBOOK.md). Follow that runbook from STEP 0 through STEP 9 to see the migration evolve in small, reviewable commits. The runbook explains the intended checkout sequence, commands, expected failures, recipe composition, and verification steps.

## What the demo shows

- standard Java modernization with `UpgradeToJava25`
- a breaking library API rename with `ChangeType`
- dependency version upgrades with `UpgradeDependencyVersion`
- a custom, type-aware Java recipe implemented with `JavaIsoVisitor`
- declarative recipe composition in YAML
- recipe tests with `RewriteTest`
- separating the runtime library, application, and rewrite recipe artifacts

The current checkout is the final migrated state. Use the historical STEP commits and the runbook when presenting the before-and-after transformation; do not treat the checked-in application as the initial baseline.

## Architecture

- **wreckage** — A fictional library providing wreck management classes (`WreckageClient`, `RecoveryService`, `NotificationService`). The demo migrates its earlier misspelled `NotificationCervice` API.
- **survivor-app** — A Quarkus REST application exposing REST endpoints that consume the wreckage library.

## REST API

| Method | Path | Description |
|--------|------|-------------|
| GET | `/incidents/{id}` | Retrieve incident details |
| POST | `/incidents/{id}/inspect` | Assess damage on a wreck |
| POST | `/incidents/{id}/recover` | Attempt to recover a wreck |
| GET | `/incidents/{id}/report` | Generate a full incident report |

## Run the demo

Start with a clean checkout and follow [`DEMO-RUNBOOK.md`](DEMO-RUNBOOK.md):

```bash
git switch --detach 7287ce5
./build.sh
./next.sh
```

Repeat `./next.sh` to move through the numbered presentation steps. The script advances to the next STEP commit and, where available, displays its presentation image. It is intentionally destructive: use it only when local changes are not needed.

The normal end-to-end verification command is:

```bash
./build.sh
```

To start the application in dev mode:

```bash
./build.sh start
```

## Run individual recipes

From `survivor-app`, the pinned Java migration recipe can be run manually:

```bash
mvn org.openrewrite.maven:rewrite-maven-plugin:6.46.1:run \
  --define rewrite.recipeArtifactCoordinates=org.openrewrite.recipe:rewrite-migrate-java:3.42.1 \
  --define rewrite.activeRecipes=org.openrewrite.java.migrate.lang.StringFormatted

mvn org.openrewrite.maven:rewrite-maven-plugin:6.46.1:run \
  --define rewrite.recipeArtifactCoordinates=org.openrewrite.recipe:rewrite-migrate-java:3.42.1 \
  --define rewrite.activeRecipes=org.openrewrite.java.migrate.UpgradeToJava25
```

The complete library migration is configured as the `upgrade` Maven execution in the application POM and uses `com.example.wreckage.UpdateToLatest`, supplied by the `wreckage-rewrite` artifact. See the runbook for the intended `discover`, `dryRun`, `run`, and verification sequence.