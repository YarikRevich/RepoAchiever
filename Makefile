dev := $(or $(dev), 'false')

ifneq (,$(wildcard .env))
include .env
export
endif

.PHONY: help
.DEFAULT_GOAL := help
help:
	@grep -h -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

.PHONY: clean
clean: ## Clean project area
	@mvn clean

.PHONY: prepare
prepare: ## Install prerequisites
	@mvn org.apache.maven.plugins:maven-dependency-plugin:3.6.0:tree -Dverbose=true

.PHONY: test
test: clean ## Run both unit and integration tests
	@mvn test
	@mvn verify

.PHONY: lint
lint: ## Run Apache Spotless linter
	@mvn spotless:apply

.PHONY: create-local-client
create-local-client: ## Create RepoAchiever local directory for client
.PHONY: create-local-client
create-local-client: ## Create RepoAchiever local directory for client
	@mkdir -p $(HOME)/.repoachiever/config/swap

.PHONY: create-local-api-server
create-local-api-server: ## Create RepoAchiever local directory for API Server
	@mkdir -p $(HOME)/.repoachiever/config
	@mkdir -p $(HOME)/.repoachiever/diagnostics/prometheus/internal
	@mkdir -p $(HOME)/.repoachiever/diagnostics/prometheus/config
	@mkdir -p $(HOME)/.repoachiever/diagnostics/grafana/internal
	@mkdir -p $(HOME)/.repoachiever/diagnostics/grafana/config/dashboards
	@mkdir -p $(HOME)/.repoachiever/diagnostics/grafana/config/datasources
	@mkdir -p $(HOME)/.repoachiever/workspace
	@mkdir -p $(HOME)/.repoachiever/internal/database
	@mkdir -p $(HOME)/.repoachiever/internal/state

.PHONY: clone-client-config
clone-client-config: ## Clone configuration files to local directory
	@cp -r ./samples/config/client/user.yaml $(HOME)/.repoachiever/config

.PHONY: clone-api-server-config
clone-api-server-config: ## Clone RepoAchiever API Server configuration files to local directory
	@cp -r ./config/grafana/dashboards/dashboard.yml $(HOME)/.repoachiever/diagnostics/grafana/config/dashboards
	@cp -r ./config/grafana/dashboards/diagnostics.tmpl $(HOME)/.repoachiever/diagnostics/grafana/config/dashboards
	@cp -r ./config/grafana/datasources/datasource.tmpl $(HOME)/.repoachiever/diagnostics/grafana/config/datasources
	@cp -r ./config/prometheus/prometheus.tmpl $(HOME)/.repoachiever/diagnostics/prometheus/config
	@cp -r ./samples/config/api-server/api-server.yaml $(HOME)/.repoachiever/config

.PHONY: clone-cluster
clone-cluster: ## Clone Cluster JAR into a RepoAchiever local directory
ifeq (,$(wildcard $(HOME)/.repoachiever/bin/cluster))
	@mkdir -p $(HOME)/.repoachiever/bin
endif
	@cp -r ./bin/cluster $(HOME)/.repoachiever/bin/

.PHONY: clone-api-server
clone-api-server: ## Clone API Server JAR into a RepoAchiever local directory
ifeq (,$(wildcard $(HOME)/.repoachiever/bin/api-server))
	@mkdir -p $(HOME)/.repoachiever/bin
endif
	@cp -r ./bin/api-server $(HOME)/.repoachiever/bin/

.PHONY: build-cluster
build-cluster: clean ## Build Cluster application
ifneq (,$(wildcard ./bin/cluster))
	@rm -r ./bin/cluster
endif
ifeq ($(dev), 'false')
	@mvn -pl cluster -T10 install
else
	@mvn -P dev -pl cluster -T10 install
endif
	$(MAKE) clone-cluster

.PHONY: build-api-server
build-api-server: clean create-local-api-server clone-api-server-config ## Build API Server application
ifneq (,$(wildcard ./bin/api-server))
	@rm -r ./bin/api-server
endif
ifeq ($(dev), 'false')
	@mvn -pl api-server -T10 install
else
	@mvn -P dev -pl api-server -T10 install
endif
	$(MAKE) clone-api-server

.PHONY: build-cli
build-cli: clean create-local-client clone-client-config ## Build CLI application
ifneq (,$(wildcard ./bin/cli))
	@rm -r ./bin/cli
endif
ifeq ($(dev), 'false')
	@mvn -pl cli -T10 install
else
	@mvn -P dev -pl cli -T10 install
endif

.PHONY: build-gui
build-gui: clean create-local-client clone-client-config ## Build GUI application
ifneq (,$(wildcard ./bin/gui))
	@rm -r ./bin/gui
endif
ifeq ($(dev), 'false')
	@mvn -pl gui -T10 install
else
	@mvn -P dev -pl gui -T10 install
endif