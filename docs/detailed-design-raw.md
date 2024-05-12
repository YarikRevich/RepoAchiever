```plantuml
!pragma teoz true

title 
    Detailed design of "ResourceTracker" 
end title

actor "Client" as client

box "Control plain" #MOTIVATION
participant "API Server" as apiserver

box "Cloud environment" #Lavender
queue "Kafka" as kafka
participant "Kafka starter" as kafkastarter
participant "Agent" as agent
entity "Cloud provider" as cloudprovider
end box

end box

note over [[[[[[kafka]]]]]]: Kafka is considered to be used in persisted mode

opt "endpoints"
opt "/v1/secrets/acquire [POST]"
apiserver -> cloudprovider: validate provided credentials
cloudprovider -> apiserver: validation result
end
opt "/v1/topic/logs [GET]"
apiserver -> kafka: retrieve state for the given "logs" topic
kafka -> apiserver: transform data stream according to the specified filters
end
opt "/v1/terraform/apply [POST]"
apiserver -> cloudprovider: deploy resource tracking infrastructure
apiserver -> kafkastarter: request kafka cluster startup 
kafkastarter -> kafka: start kafka cluster
end
opt "/v1/terraform/destroy [POST]"
apiserver -> cloudprovider: destroy resource tracking infrastructure
end
end

opt "agent execution flow"
agent -> cloudprovider: execute remote operations
agent <-- cloudprovider: remote operation result
agent --> kafka: push latest resource state to "logs" topic
end

opt "requests"
note over client: Uses properties specified in a client\nconfiguration file located in\n a common directory
opt "credentials validation"
client -> apiserver: /v1/secrets/acquire [POST]
end
opt "script validation"
client -> apiserver: /v1/script/acquire [POST]
end
opt "health check"
client -> apiserver: /v1/health [GET]
end
opt "readiness check"
client -> apiserver: /v1/readiness [GET]
end
opt "version validation"
client -> apiserver: /v1/info/version [GET]
end
opt "infrustructure deployment"
client -> apiserver: /v1/terraform/apply [POST]
end
opt "infrustructure clean up"
client -> apiserver: /v1/terraform/destroy [POST]
end
opt "state retrieval"
client -> apiserver: /v1/topic/logs [GET]
end 
end
```