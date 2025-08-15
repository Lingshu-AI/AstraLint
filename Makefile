#
# Copyright 2024-2026 the original author or authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

SHELL := /bin/bash

# Colors for output
BLUE := \033[36m
GREEN := \033[0;32m
RED := \033[0;31m
NC := \033[0m # No Color

# Log the running target
LOG_TARGET = echo -e "$(GREEN)==================> Running $@ ============> ... $(NC)"

.PHONY: help
help: ## Show this help message
	@echo -e "$(BLUE)AstraLint - AI Code Review Tool$(NC)\n"
	@echo -e "Usage:\n  make $(BLUE)<Target>$(NC)\n\nTargets:"
	@awk 'BEGIN {FS = ":.*##"; printf ""} /^[a-zA-Z_0-9-]+:.*?##/ { printf "  $(BLUE)%-15s$(NC) %s\n", $$1, $$2 } /^##@/ { printf "\n$(GREEN)%s$(NC)\n", substr($$0, 5) } ' $(MAKEFILE_LIST)

##@ Development

.PHONY: clean
clean: ## Clean build artifacts
	@$(LOG_TARGET)
	mvn clean

.PHONY: compile
compile: ## Compile the project
	@$(LOG_TARGET)
	mvn compile

.PHONY: test
test: ## Run tests
	@$(LOG_TARGET)
	mvn test

.PHONY: package
package: ## Package the application
	@$(LOG_TARGET)
	mvn package -DskipTests

.PHONY: build
build: clean compile test package ## Full build pipeline
	@$(LOG_TARGET)
	@echo -e "$(GREEN)Build completed successfully!$(NC)"

.PHONY: run
run: ## Run the application locally
	@$(LOG_TARGET)
	mvn spring-boot:run

.PHONY: run-dev
run-dev: ## Run the application in development mode
	@$(LOG_TARGET)
	mvn spring-boot:run -Dspring-boot.run.profiles=dev

##@ Code Quality

.PHONY: checkstyle
checkstyle: ## Run checkstyle analysis
	@$(LOG_TARGET)
	mvn checkstyle:check

.PHONY: quality
quality: checkstyle ## Run all code quality checks
	@$(LOG_TARGET)
	@echo -e "$(GREEN)All quality checks completed!$(NC)"

##@ Security

.PHONY: security-check
security-check: ## Run OWASP dependency check
	@$(LOG_TARGET)
	mvn org.owasp:dependency-check-maven:check

.PHONY: update-dependencies
update-dependencies: ## Update Maven dependencies
	@$(LOG_TARGET)
	mvn versions:display-dependency-updates

##@ Docker

.PHONY: docker-build
docker-build: ## Build Docker image
	@$(LOG_TARGET)
	docker build -t astralint:latest .

.PHONY: docker-run
docker-run: ## Run Docker container
	@$(LOG_TARGET)
	docker run -p 8080:8080 --env-file env.example astralint:latest

.PHONY: docker-clean
docker-clean: ## Clean Docker images and containers
	@$(LOG_TARGET)
	docker system prune -f

##@ CI/CD

.PHONY: ci-build
ci-build: clean compile test package ## CI build pipeline
	@$(LOG_TARGET)
	@echo -e "$(GREEN)CI build completed successfully!$(NC)"

.PHONY: ci-quality
ci-quality: quality security-check ## CI quality pipeline
	@$(LOG_TARGET)
	@echo -e "$(GREEN)CI quality checks completed!$(NC)"

.PHONY: ci-full
ci-full: ci-build ci-quality docker-build ## Full CI pipeline
	@$(LOG_TARGET)
	@echo -e "$(GREEN)Full CI pipeline completed successfully!$(NC)"

##@ Documentation

.PHONY: docs
docs: ## Generate documentation
	@$(LOG_TARGET)
	mvn javadoc:javadoc

.PHONY: site
site: ## Generate site documentation
	@$(LOG_TARGET)
	mvn site
