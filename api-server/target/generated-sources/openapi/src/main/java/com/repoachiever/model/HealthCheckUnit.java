package com.repoachiever.model;

import com.repoachiever.model.HealthCheckStatus;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("HealthCheckUnit")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2024-05-17T01:46:26.799650+02:00[Europe/Warsaw]")@lombok.Data @lombok.NoArgsConstructor @lombok.AllArgsConstructor(staticName = "of")

public class HealthCheckUnit  implements Serializable {
  private @Valid String name;
  private @Valid HealthCheckStatus status;

  /**
   **/
  public HealthCheckUnit name(String name) {
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
  public HealthCheckUnit status(HealthCheckStatus status) {
    this.status = status;
    return this;
  }

  
  @JsonProperty("status")
  @NotNull
  public HealthCheckStatus getStatus() {
    return status;
  }

  @JsonProperty("status")
  public void setStatus(HealthCheckStatus status) {
    this.status = status;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HealthCheckUnit healthCheckUnit = (HealthCheckUnit) o;
    return Objects.equals(this.name, healthCheckUnit.name) &&
        Objects.equals(this.status, healthCheckUnit.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HealthCheckUnit {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

