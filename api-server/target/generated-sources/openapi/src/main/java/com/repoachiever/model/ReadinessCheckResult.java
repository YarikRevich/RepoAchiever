package com.repoachiever.model;

import com.repoachiever.model.ReadinessCheckStatus;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("ReadinessCheckResult")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2024-05-15T10:52:30.878903+02:00[Europe/Warsaw]")@lombok.Data @lombok.NoArgsConstructor @lombok.AllArgsConstructor(staticName = "of")

public class ReadinessCheckResult  implements Serializable {
  private @Valid String name;
  private @Valid ReadinessCheckStatus status;
  private @Valid Object data;

  /**
   **/
  public ReadinessCheckResult name(String name) {
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
  public ReadinessCheckResult status(ReadinessCheckStatus status) {
    this.status = status;
    return this;
  }

  
  @JsonProperty("status")
  @NotNull
  public ReadinessCheckStatus getStatus() {
    return status;
  }

  @JsonProperty("status")
  public void setStatus(ReadinessCheckStatus status) {
    this.status = status;
  }

  /**
   **/
  public ReadinessCheckResult data(Object data) {
    this.data = data;
    return this;
  }

  
  @JsonProperty("data")
  @NotNull
  public Object getData() {
    return data;
  }

  @JsonProperty("data")
  public void setData(Object data) {
    this.data = data;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReadinessCheckResult readinessCheckResult = (ReadinessCheckResult) o;
    return Objects.equals(this.name, readinessCheckResult.name) &&
        Objects.equals(this.status, readinessCheckResult.status) &&
        Objects.equals(this.data, readinessCheckResult.data);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, status, data);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReadinessCheckResult {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
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

