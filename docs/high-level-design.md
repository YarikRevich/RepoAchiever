```plantuml
title

High-level design of "RepoAchiever"

end title

cloud "External environment" {
actor "Client"
actor "Diagnostics"
}

component "Control plane" {
node "API Server"
node "Metrics registry"

cloud " Decentralized environment" {
node "Cluster"
entity "Worker"
}
}

cloud " Third-party environment" {
entity "External service"
}

[Client] <--> [API Server]: " Send requests"
[API Server] <-> [Cluster]: " Schedule content processing"
[Cluster] <--> [Worker]: " Propagate individual requests   "
[Worker] <--> [External service]: " Retrieve content"
[API Server] <-- [Metrics registry]: " Retrieve metrics"
[Diagnostics] --> [Metrics registry]: " Scrap metrics"
```