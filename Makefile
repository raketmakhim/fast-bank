.PHONY: help install build test coverage quality clean run-account run-people full-build reports

help: ## Show this help message
	@echo 'Usage: make [target]'
	@echo ''
	@echo 'Available targets:'
	@awk 'BEGIN {FS = ":.*##"; printf "\n"} /^[a-zA-Z_-]+:.*?##/ { printf "  %-15s %s\n", $$1, $$2 }' $(MAKEFILE_LIST)

install: ## Install dependencies
	./gradlew build --refresh-dependencies

build: ## Build all services
	./gradlew build

test: ## Run tests for all services
	./gradlew test

coverage: ## Run tests with coverage report for all services
	./gradlew testWithCoverage

quality: ## Run quality checks for all services (Checkstyle, PMD, SpotBugs)
	./gradlew qualityCheck

clean: ## Clean build artifacts
	./gradlew clean

run-account: ## Run the account service
	./gradlew :accountService:bootRun

run-people: ## Run the people service
	./gradlew :peopleService:bootRun

full-build: ## Clean, build, test, and run all checks for all services
	./gradlew fullBuild

reports: ## Show generated report locations
	./gradlew reports

jar: ## Build executable JARs for all services
	./gradlew bootJar
	@echo "JARs created in each service's build/libs/ directory"

verify: ## Run build with all verifications
	./gradlew clean check
