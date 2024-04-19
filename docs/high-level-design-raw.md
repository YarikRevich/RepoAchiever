```plantuml
title

High-level design of "RepoArchiver"

end title

actor "Client"

component "Control plane" {
node "API Server"
node "Prometheus"

cloud " Decentralized environment" {
node "Cluster"
entity "Worker"
}

[Client] <--> [API Server]: " Send requests"
[API Server] <-> [Cluster]: " Schedule content processing"
[Cluster] <--> [Worker]: " Propagate individual requests   "
[API Server] --> [Prometheus]: " Export diagnostics"
```