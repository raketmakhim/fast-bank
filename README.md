# fast-bank

Banking project for learning. A Gradle multi-project build with two Spring Boot services backed by PostgreSQL and RabbitMQ.

| Service          | Port | Base path       |
|------------------|------|-----------------|
| `peopleService`  | 8080 | `/`             |
| `accountService` | 8081 | `/api/accounts` |

## Requirements

- Java 21 (the Gradle toolchain will fetch it if missing)
- Docker (PostgreSQL and RabbitMQ)

## Infrastructure

PostgreSQL (built from the local Dockerfile so `postgres/init.sql` is applied):

```bash
docker build -t my_postgres_image postgres
docker run -d -p 5432:5432 --name my_postgres_container my_postgres_image
```

RabbitMQ (management UI at http://localhost:15672, admin/admin):

```bash
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 \
  -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=admin \
  -v rabbitmq_data:/var/lib/rabbitmq rabbitmq:3-management
```

Once the containers exist, later runs just need `docker start my_postgres_container rabbitmq`.

## Environment variables

`accountService` sends email via Gmail SMTP and will not start without:

```bash
export GMAIL_USERNAME=...        # PowerShell: $env:GMAIL_USERNAME = "..."
export GMAIL_APP_PASSWORD=...    # a Gmail app password, not your account password
```

## Running

Each service runs in its own terminal:

```bash
./gradlew :peopleService:bootRun     # http://localhost:8080
./gradlew :accountService:bootRun    # http://localhost:8081
```

On Windows use `gradlew.bat`. The Makefile wraps these as `make run-people` and `make run-account`.

## Common tasks

```bash
./gradlew build            # compile and test all services
./gradlew test             # tests only
./gradlew testWithCoverage # tests plus JaCoCo report
./gradlew qualityCheck     # Checkstyle, PMD, SpotBugs
./gradlew spotlessApply    # auto-format
./gradlew fullBuild        # clean, build, test, all checks
```

See [TASKS.md](TASKS.md) for the full list and the `make` / `tasks.bat` / `tasks.ps1` equivalents.
