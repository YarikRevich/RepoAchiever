package com.repoachiever.model;

import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("ClusterInfoUnit")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2024-04-20T13:09:08.079025+02:00[Europe/Warsaw]")@lombok.Data @lombok.NoArgsConstructor @lombok.AllArgsConstructor(staticName = "of")

public class ClusterInfoUnit  implements Serializable {
  private @Valid String name;
  private @Valid Boolean health;
  private @Valid Integer workers;

  /**
   **/
  public ClusterInfoUnit name(String name) {
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
  public ClusterInfoUnit health(Boolean health) {
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
  public ClusterInfoUnit workers(Integer workers) {
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
    ClusterInfoUnit clusterInfoUnit = (ClusterInfoUnit) o;
    return Objects.equals(this.name, clusterInfoUnit.name) &&
        Objects.equals(this.health, clusterInfoUnit.health) &&
        Objects.equals(this.workers, clusterInfoUnit.workers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, health, workers);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ClusterInfoUnit {\n");
    
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

