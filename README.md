# AsteroidsFXGame


## Build & Run

From the project root, build all modules:

```bash
mvn install
```

To do a clean build (removes previous build output first):

```bash
mvn clean install
```

To skip tests for a faster build:

```bash
mvn clean install -DskipTests
```

Then launch the game:

```bash
mvn exec:exec
```

## Enabling the Scoring System

The scoring system is a separate Spring Boot service that must be running **before** you launch the game.

**Step 1 — Start the scoring service** (leave this terminal open):

```bash
cd ScoringSystem
mvn spring-boot:run
```

It starts on `localhost:8080`.

**Step 2 — Launch the game** in a second terminal:

```bash
mvn exec:exec
```

Points are awarded for each asteroid destroyed and displayed in the top-right corner of the screen.
