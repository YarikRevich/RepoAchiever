# RepoAchiever

[![Build](https://github.com/YarikRevich/RepoAchiever/actions/workflows/build.yml/badge.svg)](https://github.com/YarikRevich/ResourceTracker/actions/workflows/build.yml)
![Linux](https://img.shields.io/badge/Linux-FCC624?style=for-the-badge&logo=linux&logoColor=black)
![MacOS](https://img.shields.io/badge/MacOS-8773f5?style=for-the-badge&logo=macos&logoColor=black)
[![StandWithUkraine](https://raw.githubusercontent.com/vshymanskyy/StandWithUkraine/main/badges/StandWithUkraine.svg)](https://github.com/vshymanskyy/StandWithUkraine/blob/main/docs/README.md)

## General Information

A tool used for remote content achieving operations.

![](./docs/high-level-design.png)

![](./docs/detailed-design-raw.png)

![](./docs/internal-storage-design.png)

## Setup

All setup related operations are processed via **Makefile** placed in the root directory.

### CLI

In order to build **RepoAchiever CLI** it's required to execute the following command. Initially it cleans the environment and builds Java project using **Maven**
```shell
make build-cli
```

After the execution of command given above the executable will be generated and placed into **bin** folder in the root directory of the project

**RepoAchiever CLI** build automatically places default **user.yaml** configuration file into **~/.repoachiever/config** directory.

### GUI

In order to build **RepoAchiever GUI** it's required to execute the following command. Initially it cleans the environment and build Java project using **Maven**
```shell
make build-gui
```

After the execution of command given above the executable will be generated and placed into **bin** folder in the root directory of the project

**RepoAchiever GUI** build automatically compiles **RepoAchiever API Server** and places both executable JAR and other dependencies into **~/.repoachiever/bin/api-server** directory

It's highly recommended not to move **RepoAchiever API Server** files from the default local directory

### API Server

In order to build **RepoAchiever API Server** it's required to execute the following command. Initially it cleans the environment and build Java project using **Maven**
```shell
make build-api-server
```

After the execution of command given above the executable will be generated and placed into **bin** folder in the root directory of the project

### Cluster

In order to build **RepoAchiever Cluster** it's required to execute the following command. Initially it cleans the environment and build Java project using **Maven**
```shell
make build-cluster
```

After the execution of command given above the executable will be generated and placed into **bin** folder in the root directory of the project

## Use cases

For both **RepoAchiever CLI** and **RepoAchiever GUI** examples, there was used the following user configuration file located at **~/.repoachiever/config** directory as **user.yaml**:
```yaml
# Represents section used to declare desired content.
content:
  # Represents locations of VCS provider repositories, which are intended to be used for requested operations.
  locations:
    - name: "[YarikRevich]test:master" # Represents location definition name.

      # Enables additional content download of repositories of VCS provider repositories.
      additional: true
      
    - name: "[YarikRevich]scripts:master"
      additional: true
      
    - name: "[YarikRevich]odb:master"
      additional: true
      
    - name: "[YarikRevich]tetris:main"
      additional: true
      
    - name: "[YarikRevich]tmpm:master"
      additional: true
      
    - name: "[YarikRevich]portfolio:master".
      additional: true
      
    - name: "[YarikRevich]ai-commit-generator:master"
      additional: true
      
    - name: "[YarikRevich]website:main"
      additional: true
      
    - name: "[YarikRevich]vim-config:master"
      additional: true
      
    - name: "[YarikRevich]critical_db_framework:master"
      additional: true

# Represents section used for VCS provider configuration.
service:
  # Represents the selected VCS provider for RepoAchiever. The first part divided by the hyphen represents
  # selected VCS core type and the second part defines external provider.
  #
  # Supported providers are "exporter" and "git-github".
  provider: "git-github"

  # Represents section used for RepoAchiever Exporter configuration.
  exporter:
    # Represents address for the host of RepoAchiever Exporter.
    host: "http://localhost:10000"

  # Represents credentials used for the selected VCS provider. Can be left null, if the provider does not
  # require it.
  credentials:
    # Represents session identificator, used to distinguish different workspaces and thus separate content inside.
    id: "1"

    # Represents authentication token intended to be used for VCS provider API access permission.
    token: "ghp_UMz4cQAD87xXAtEbsPazlRcLw7kVPu1zF8dj"

# Represents section used for RepoAchiever API Server configuration.
api-server:
  # Represents address for the host of RepoAchiever API Server.
  host: "http://localhost:8085"
```

For **RepoAchiever API Server** there was used the following configuration file located at **~/.repoachiever/config** directory as **api-server.yaml**:
```yaml
# Represents section used for RepoAchiever API Server connection configuration.
connection:
  # Represents port of RepoAchiever API Server used for connection establishment.
  port: 8085

# Represents section used for RepoAchiever API Server internal communication configuration.
communication:
  # Represents port of RMI registry used for RepoAchiever API Server to RepoAchiever Cluster communication.
  port: 8089

# Represents section used for RepoAchiever API Server content configuration.
content:
  # Represents format used for content download requests.
  format: "zip"

# Represents section used for RepoAchiever API Server diagnostics configuration.
diagnostics:
  # Enables diagnostics functionality.
  enabled: false

  # Represents section used for RepoAchiever diagnostics metrics configuration.
  metrics:
    # Represents port used for metrics endpoint.
    port: 8090

  # Represents section used for RepoAchiever diagnostics Grafana instance.
  grafana:
    # Represents port used for Grafana instance deployment.
    port: 8091

  # Represents section used for RepoAchiever diagnostics Prometheus instance.
  prometheus:
    # Represents port used for Prometheus instance deployment.
    port: 8120

  # Represents section used for RepoAchiever diagnostics Prometheus Node Exporter instance.
  node-exporter:
    # Represents port used for Prometheus Node Exporter instance deployment.
    port: 8121

# Represents section used for RepoAchiever resource configuration.
resource:
  # Represents a set of options used for RepoAchiever Cluster configuration.
  cluster:
    # Represents the highest amount of allocated workers per cluster.
    max-workers: 5

    # Represents the highest amount of downloaded content versions per each.
    max-versions: 5

  # Represents a set of options used for RepoAchiever Cluster worker configuration.
  worker:
    # Represents frequency of requests to selected VCS provider resources.
    frequency: "0/30 * * * * ?"
```

### CLI

![cli](./docs/examples/cli.gif)

### GUI

![gui](./docs/examples/gui.gif)

### Diagnostics dashboard

For **RepoAchiever API Server** configuration the following section should be modified:
```yaml
# Represents section used for RepoAchiever API Server diagnostics configuration.
diagnostics:
  # Enables diagnostics functionality.
  enabled: true

  # Represents section used for RepoAchiever diagnostics metrics configuration.
  metrics:
    # Represents port used for metrics endpoint.
    port: 8090

  # Represents section used for RepoAchiever diagnostics Grafana instance.
  grafana:
    # Represents port used for Grafana instance deployment.
    port: 8091

  # Represents section used for RepoAchiever diagnostics Prometheus instance.
  prometheus:
    # Represents port used for Prometheus instance deployment.
    port: 8120

  # Represents section used for RepoAchiever diagnostics Prometheus Node Exporter instance.
  node-exporter:
    # Represents port used for Prometheus Node Exporter instance deployment.
    port: 8121
```

![diagnostics](./docs/examples/diagnostics.gif)