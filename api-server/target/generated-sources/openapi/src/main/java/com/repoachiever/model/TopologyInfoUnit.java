package com.repoachiever.model;

import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("TopologyInfoUnit")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2024-05-19T15:06:43.082824+02:00[Europe/Warsaw]")@lombok.Data @lombok.NoArgsConstructor @lombok.AllArgsConstructor(staticName = "of")

public class TopologyInfoUnit  implements Serializable {
  private @Valid String name;
  private @Valid Boolean health;
  private @Valid Integer workers;

  /**
   **/
  public TopologyInfoUnit name(String name) {
    this.name = name;
    return this;
  }

  
  @JsonProperty("name")
  @NotNull
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  /**
   **/
  public TopologyInfoUnit health(Boolean health) {
    this.health = health;
    return this;
  }

  
  @JsonProperty("health")
  public Boolean getHealth() {
    return health;
  }

  @JsonProperty("health")
  public void setHealth(Boolean health) {
    this.health = health;
  }

  /**
   **/
  public TopologyInfoUnit workers(Integer workers) {
    this.workers = workers;
    return this;
  }

  
  @JsonProperty("workers")
  public Integer getWorkers() {
    return workers;
  }

  @JsonProperty("workers")
  public void setWorkers(Integer workers) {
    this.workers = workers;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TopologyInfoUnit topologyInfoUnit = (TopologyInfoUnit) o;
    return Objects.equals(this.name, topologyInfoUnit.name) &&
        Objects.equals(this.health, topologyInfoUnit.health) &&
        Objects.equals(this.workers, topologyInfoUnit.workers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, health, workers);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TopologyInfoUnit {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    health: ").append(toIndentedString(health)).append("\n");
    sb.append("    workers: ").append(toIndentedString(workers)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }


}

