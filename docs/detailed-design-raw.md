```plantuml
!pragma teoz true

title 
    Detailed design of "RepoAchiever" 
end title

box "External environment" #MOTIVATION
actor "Client" as client
actor "Diagnostics" as diagnostics
end box

box "Control plain" #MOTIVATION
participant "API Server" as apiserver
database "Local storage" as localstorage

entity "Metrics registry" as metricsregistry

box "Decentralized environment" #Lavender
participant "Cluster" as cluster
entity "Worker" as worker
end box

end box

box "Third-party environment" #MOTIVATION
entity "External service" as externalservice 
end box

opt "endpoints"
opt "/v1/content [POST]"
localstorage -> apiserver: retrieve details about all the available content for the given user
end
opt "/v1/content/apply [POST]"
apiserver -> externalservice: validate provided credentials
apiserver -> localstorage: apply provided user configuration
apiserver -> cluster: update topology according to the new configuration 
end
opt "/v1/content/withdraw [DELETE]"
apiserver -> localstorage: remove configuration for the given user
apiserver -> cluster: remove topology
end
opt "/v1/content/download [POST]"
localstorage -> apiserver: retrieve requested content in a form of achieve
end
opt "/v1/content/clean [DELETE]"
apiserver -> localstorage: remove requested content for the given user
end
opt "/v1/content/clean/all [DELETE]"
apiserver -> localstorage: remove all content for the given user
end

opt "cluster execution flow"
cluster -> worker: allocate instances according to the provided locations
worker -> externalservice: retrieve content with the given identification
worker -> apiserver: transfer retrieved content
end

opt "requests"

opt "retrieve available content"
client -> apiserver: /v1/content [POST]
end
opt "configuration application"
client -> apiserver: /v1/content/apply [POST]
end
opt "configuration withdrawal"
client -> apiserver: /v1/content/withdraw [DELETE]
end
opt "download content"
client -> apiserver: /v1/content/download [POST]
end
opt "clean selected content"
client -> apiserver: /v1/content/clean [DELETE]
end
opt "clean all the content for the given user"
client -> apiserver: /v1/content/clean/all [DELETE]
end
opt "health check"
client -> apiserver: /v1/health [GET]
end
opt "version retrieval"
client -> apiserver: /v1/info/version [GET]
end
opt "topology retrieval"
client -> apiserver: /v1/info/topology [GET]
end
opt ""
client -> apiserver: /v1/terraform/destroy [POST]
end
opt "metrics retrieval"
diagnostics -> metricsregistry: / [GET] 
end

end
```