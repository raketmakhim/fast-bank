# Task Runner Guide

This project provides multiple ways to run common tasks, similar to Python's `invoke` or `make`.

## 🚀 Quick Start

### Windows Users (Recommended)

#### Option 1: Batch Script
```cmd
tasks.bat help          # Show all commands
tasks.bat build         # Build the project
tasks.bat coverage      # Run tests with coverage
tasks.bat quality       # Run quality checks
tasks.bat full-build    # Complete build with all checks
tasks.bat run           # Run the application
```

#### Option 2: PowerShell Script
```powershell
.\tasks.ps1 help        # Show all commands
.\tasks.ps1 build       # Build the project
.\tasks.ps1 coverage    # Run tests with coverage
.\tasks.ps1 quality     # Run quality checks
.\tasks.ps1 full-build  # Complete build with all checks
.\tasks.ps1 run         # Run the application
```

### Linux/Mac Users

#### Option 1: Makefile
```bash
make help               # Show all commands
make build              # Build the project
make coverage           # Run tests with coverage
make quality            # Run quality checks
make full-build         # Complete build with all checks
make run                # Run the application
```

#### Option 2: Gradle Wrapper
```bash
./gradlew build
./gradlew testWithCoverage
./gradlew qualityCheck
./gradlew fullBuild
./gradlew bootRun
```

### Direct Gradle (All Platforms)

All platforms can use Gradle directly:

```bash
# Windows
gradlew.bat <task>

# Linux/Mac
./gradlew <task>
```

## 📋 Available Commands

| Command      | Description                                      | Gradle Equivalent          |
|------------- |--------------------------------------------------|----------------------------|
| help         | Show available commands                          | `./gradlew tasks`          |
| install      | Install/refresh dependencies                     | `./gradlew build --refresh-dependencies` |
| build        | Build the project                                | `./gradlew build`          |
| test         | Run tests                                        | `./gradlew test`           |
| coverage     | Run tests with coverage report                   | `./gradlew testWithCoverage` |
| quality      | Run all quality checks                           | `./gradlew qualityCheck`   |
| clean        | Clean build artifacts                            | `./gradlew clean`          |
| run          | Run the Spring Boot application                  | `./gradlew bootRun`        |
| full-build   | Clean, build, test, and run all checks           | `./gradlew fullBuild`      |
| reports      | Show generated report locations                  | `./gradlew reports`        |
| jar          | Build executable JAR                             | `./gradlew bootJar`        |
| verify       | Run build with all verifications                 | `./gradlew clean check`    |

## 🎯 Common Workflows

### Development Workflow
```bash
# Edit code, then run tests with coverage
tasks.bat coverage

# Check code quality before committing
tasks.bat quality
```

### Before Committing
```bash
# Run full build to ensure everything passes
tasks.bat full-build
```

### Running the Application
```bash
# Development mode (with hot reload)
tasks.bat run

# Or build and run JAR
tasks.bat jar
java -jar build\libs\JavaStartUp.jar
```

### CI/CD Pipeline
```bash
# Clean build with all verifications
tasks.bat verify
```

## 📊 Viewing Reports

After running tests or quality checks, view reports at:

- **Test Results**: `build/reports/tests/test/index.html`
- **Coverage Report**: `build/reports/jacoco/test/html/index.html`
- **Checkstyle**: `build/reports/checkstyle/main.html`
- **PMD**: `build/reports/pmd/main.html`
- **SpotBugs**: `build/reports/spotbugs/main.html`

## 🔧 Custom Gradle Tasks

If you prefer using Gradle directly, these custom tasks are available:

```bash
./gradlew testWithCoverage   # Tests + coverage report
./gradlew qualityCheck       # All quality checks
./gradlew fullBuild          # Complete build pipeline
./gradlew reports            # List report locations
```

## 💡 Tips

1. **Windows Users**: Use `tasks.bat` for simplicity (no PowerShell execution policy issues)
2. **IDE Users**: Most tasks can be run from your IDE's Gradle panel
3. **CI/CD**: Use `./gradlew fullBuild` for comprehensive checks
4. **Quick Feedback**: Use `tasks.bat test` during development for fast feedback
