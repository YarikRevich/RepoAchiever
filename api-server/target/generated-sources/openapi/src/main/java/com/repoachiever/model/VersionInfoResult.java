package com.repoachiever.model;

import com.repoachiever.model.VersionExternalApiInfoResult;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("VersionInfoResult")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2024-05-17T01:46:26.799650+02:00[Europe/Warsaw]")@lombok.Data @lombok.NoArgsConstructor @lombok.AllArgsConstructor(staticName = "of")

public class VersionInfoResult  implements Serializable {
  private @Valid VersionExternalApiInfoResult externalApi;

  /**
   **/
  public VersionInfoResult externalApi(VersionExternalApiInfoResult externalApi) {
    this.externalApi = externalApi;
    return this;
  }

  
  @JsonProperty("externalApi")
  public VersionExternalApiInfoResult getExternalApi() {
    return externalApi;
  }

  @JsonProperty("externalApi")
  public void setExternalApi(VersionExternalApiInfoResult externalApi) {
    this.externalApi = externalApi;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VersionInfoResult versionInfoResult = (VersionInfoResult) o;
    return Objects.equals(this.externalApi, versionInfoResult.externalApi);
  }

  @Override
  public int hashCode() {
    return Objects.hash(externalApi);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VersionInfoResult {\n");
    
    sb.append("    externalApi: ").append(toIndentedString(externalApi)).append("\n");
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

