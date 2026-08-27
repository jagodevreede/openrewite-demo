# OpenRewrite Wreckage Migration Demo Runbook

## Purpose

Show a realistic library migration in small, inspectable steps:

1. Start with a Java 21 application containing deliberate Java 25 migration targets.
2. Add OpenRewrite execution to the application.
3. Encounter a real build/configuration problem instead of hiding it.
4. Separate the library, application, and custom recipes into Maven modules.
5. Run the Java upgrade and observe the migrated source.
6. Add a domain-specific recipe for a breaking library rename.
7. Run the complete migration chain and verify the application still builds.

The central story:

> “We are not rewriting code for its own sake. The Wreckage library is evolving, and the Survivor application needs a
> repeatable, testable upgrade path. OpenRewrite handles standard Java modernization; when the library’s semantic change
> is project-specific, we add a custom recipe and compose it with the standard migration.”

## Before the presentation

Use a clean checkout. The demo is organized as a linear commit story:

| Commit | Presentation role                                               |
|--------|-----------------------------------------------------------------|
| STEP 0 | Baseline: Java 21 code prepared for Java 25 migration           |
| STEP 1 | First attempt to configure `UpgradeToJava25`                    |
| STEP 2 | Give the Rewrite execution an explicit id                       |
| STEP 3 | Intentionally broken intermediate state                         |
| STEP 4 | Split the project into application, library, and recipe modules |
| STEP 5 | Configure the version-upgrade recipe chain                      |
| STEP 6 | Result after running the upgrade                                |
| STEP 7 | Add custom recipe plumbing and tests                            |
| STEP 8 | Implement and compose `RenameServiceVariable`                   |
| STEP 9 | Run the complete migration on the application                   |

The repository currently has a root Maven aggregator listing `wreckage` and `survivor-app`; the library has its own
nested reactor containing `wreckage` and `wreckage-rewrite`. The recipe implementation lives in
`wreckage/wreckage-rewrite`. The checked-in `build.sh` remains the normal end-to-end verification command.

Start at STEP 0, then use the repository's step navigator:

```bash
git switch --detach 7287ce5
./next.sh   # advances to STEP 1
./next.sh   # advances to STEP 2
```

Repeat `./next.sh` after each checkpoint to move through STEP 9. The script reads the current `STEP N` commit message,
finds `STEP N+1`, and checks it out with a hard reset. On macOS, if `STEP N+1.png` exists beside the script, it opens
the image in Preview, shows it fullscreen for five seconds, and closes Preview. Set `STEP_IMAGE_SECONDS=10` to change
the duration. Missing images and non-macOS hosts are ignored. Use it only from a clean demo checkout: it discards local
changes and cleans `./src`.

## Opening: establish the domain

Start at STEP 0:

```bash
# Nuke the m2 repo
rm -rf ~/.m2/repository/com/example/ 
git switch --detach 7287ce5
./build.sh
```

Explain the domain briefly:

- `wreckage` is the fictional library with bad API design to showcase openrewrite.
- `survivor-app` is the Quarkus REST consumer of this API.
- The application has incident lookup, inspection, recovery, and reporting flows.
- The code is intentionally shaped as migration material rather than already-modern Java.

Show these examples:

- `IncidentService` contains a type-pattern switch and conditional logic that Java migration recipes can refine.
- `NotificationCervice` is an intentional typo in the library API.
- The application uses the same misspelled identifier, `notificationCervice`.

Expected result: `./build.sh` installs the library and verifies the application.

show: survivor-app/src/main/java/com/example/survivor/IncidentService.java

Run openrewrite on the command line

```bash
cd survivor-app
mvn org.openrewrite.maven:rewrite-maven-plugin:6.46.1:run --define rewrite.recipeArtifactCoordinates=org.openrewrite.recipe:rewrite-migrate-java:RELEASE --define rewrite.ac
tiveRecipes=org.openrewrite.java.migrate.UpgradeToJava25
```

## Act 1 — Configure standard Java migration

### STEP 1 — Add the first Rewrite execution

```bash
next.sh
```

Show that the application POM now references:

- `org.openrewrite.maven:rewrite-maven-plugin:6.46.1`
- `org.openrewrite.recipe:rewrite-migrate-java:3.42.1`
- `org.openrewrite.java.migrate.UpgradeToJava25`

If you want to execute the plugin manually at this point:

```bash
cd survivor-app
mvn rewrite:run
```

### STEP 2 — Make the execution addressable

```bash
next.sh
```

> BUMP lib version

Step 2 show NotificationCervice
Do clean install!!! of version 2.0.0

Show the explicit execution id, `code-style`, and explain why named executions matter:

- Maven configuration becomes readable.
- A later application-specific `upgrade` execution can coexist with the standard Java migration.
- The two transformations have separate responsibilities.

```bash
mvn rewrite:run@code-style
```

## Act 2 — Let the failure teach the architecture

### STEP 3 — Reproduce the broken intermediate state

```bash
next.sh
```

This step is intentionally not a successful checkpoint. The library rename and Rewrite metadata are out of sync while
the project is still in the old layout. Use the failure as the transition rather than skipping it.

Show the relevant change in wreckage/src/main/resources/META-INF/rewrite/rewrite.yml

Implement the upgrade execution

```xml

<execution>
    <id>upgrade</id>
    <configuration>
        <activeRecipes>
            <recipe>com.example.wreckage.UpdateToLatest</recipe>
        </activeRecipes>
    </configuration>
</execution>
```

with the lib as dependency

```xml

<dependency>
    <groupId>com.example</groupId>
    <artifactId>wreckage-rewrite</artifactId>
    <version>2.0.0</version>
</dependency>
```

### STEP 4 — Split the concerns into Maven modules

> Image shown

```bash
next.sh
```

Explain the new structure:

```text
root reactor: wreckage-parent
├── wreckage/
│   ├── wreckage/          library artifact
│   └── wreckage-rewrite/  custom OpenRewrite recipe artifact
└── survivor-app/          Quarkus consumer
```

The useful architectural message:

- The library can be built and installed as a versioned dependency.
- The recipe has its own source, tests, and artifact.
- The application consumes both the library and the recipe without mixing runtime code and migration code.

Step 4 not clean upgrade path
Use next!

## Act 3 — Run the standard migration

### STEP 5 — Compose the upgrade configuration

```bash
next.sh
```

Explain the two layers:

- `com.example.wreckage.v1tov2` changes the library API from `NotificationCervice` to `NotificationService`.
- `com.example.wreckage.UpdateToLatest` composes the application migration and dependency-version update.
- `UpgradeToJava25` remains a separate standard migration execution.

At this checkpoint the v1-to-v2 recipe demonstrates the type rename. The variable rename is added later, after the
audience has seen why a type rename alone is insufficient.

```bash
mvn rewrite:run@upgrade
```

### STEP 6 — Inspect the migrated result

```bash
next.sh
```

Point out observable Java migration changes:

- the dependency moves to `wreckage` version `2.0.0`;

the changes in this step are done with the rewrite command in the previous step

## Act 4 — Add the project-specific recipe

### STEP 7 — Add recipe plumbing first

> Image shown

```bash
next.sh
```

We address the issue that the name is still invalid

Show:

- `RenameServiceVariable.java` exists as a recipe type.
- `RenameServiceVariableTest.java` defines the intended behavior.
- The test covers a `NotificationService` field and a negative case for an unrelated `String` field.

### STEP 8 — Implement and compose the recipe

> Image shown

```bash
next.sh
```

Show OMP session to showcat that qwen 3.6 on low effort was able to implement this recipe.

Explain the implementation at a high level:

- Visit variable declarations and identifier references.
- Match the old name, `notificationCervice`.
- Match the attributed declared type, `com.wreckage.NotificationService`.
- Update both source names and OpenRewrite type metadata.
- Leave same-named variables of other types unchanged.

The recipe is then appended to `v1tov2.yml` after `ChangeType`:

```yaml
recipeList:
  - org.openrewrite.java.ChangeType:
      oldFullyQualifiedTypeName: com.wreckage.NotificationCervice
      newFullyQualifiedTypeName: com.wreckage.NotificationService
  - com.wreckage.rewrite.RenameServiceVariable
```

Emphasize ordering: first repair the type, then rename variables whose type is now `NotificationService`.

## Act 5 — Run the complete story

### STEP 9 — Apply the composed migration

```bash
next.sh
```

Show the final result in `IncidentService`:

- `NotificationService notificationService` is consistently named.
- Constructor parameters, field use, and notification calls agree.
- The application is on `wreckage` 2.0.0.
- The standard Java migration and custom library migration coexist in the Maven configuration.

## Presenter checklist

- [ ] Start on STEP 0 and establish a passing baseline.
- [ ] Show the Java 25 targets before showing the recipe.
- [ ] Explain the difference between standard Java migration and library migration.
- [ ] Show STEP 3 as an intentional failure/architecture lesson.
- [ ] Show the module split before showing custom recipe implementation.
- [ ] Run recipe tests at STEP 8.
- [ ] Show the ordered YAML chain: `ChangeType`, then `RenameServiceVariable`.
- [ ] Finish on STEP 9 and run `./build.sh`.
- [ ] Leave the audience with the reviewable commit sequence, not a large opaque diff.
