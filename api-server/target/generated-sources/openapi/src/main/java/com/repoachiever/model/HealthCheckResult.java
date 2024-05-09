package com.repoachiever.model;

import com.repoachiever.model.HealthCheckStatus;
import com.repoachiever.model.HealthCheckUnit;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("HealthCheckResult")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2024-05-09T20:19:19.197395+02:00[Europe/Warsaw]")@lombok.Data @lombok.NoArgsConstructor @lombok.AllArgsConstructor(staticName = "of")

public class HealthCheckResult  implements Serializable {
  private @Valid HealthCheckStatus status;
  private @Valid List<HealthCheckUnit> checks = new ArrayList<>();

  /**
   **/
  public HealthCheckResult status(HealthCheckStatus status) {
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

  /**
   **/
  public HealthCheckResult checks(List<HealthCheckUnit> checks) {
    this.checks = checks;
    return this;
  }

  
  @JsonProperty("checks")
  @NotNull
  public List<HealthCheckUnit> getChecks() {
    return checks;
  }

  @JsonProperty("checks")
  public void setChecks(List<HealthCheckUnit> checks) {
    this.checks = checks;
  }

  public HealthCheckResult addChecksItem(HealthCheckUnit checksItem) {
    if (this.checks == null) {
      this.checks = new ArrayList<>();
    }

    this.checks.add(checksItem);
    return this;
  }

  public HealthCheckResult removeChecksItem(HealthCheckUnit checksItem) {
    if (checksItem != null && this.checks != null) {
      this.checks.remove(checksItem);
    }

    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HealthCheckResult healthCheckResult = (HealthCheckResult) o;
    return Objects.equals(this.status, healthCheckResult.status) &&
        Objects.equals(this.checks, healthCheckResult.checks);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, checks);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HealthCheckResult {\n");
    
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    checks: ").append(toIndentedString(checks)).append("\n");
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

