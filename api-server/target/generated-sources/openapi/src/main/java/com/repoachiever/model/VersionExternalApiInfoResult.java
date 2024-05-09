package com.repoachiever.model;

import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("VersionExternalApiInfoResult")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2024-05-09T20:19:19.197395+02:00[Europe/Warsaw]")@lombok.Data @lombok.NoArgsConstructor @lombok.AllArgsConstructor(staticName = "of")

public class VersionExternalApiInfoResult  implements Serializable {
  private @Valid String version;
  private @Valid String hash;

  /**
   **/
  public VersionExternalApiInfoResult version(String version) {
    this.version = version;
    return this;
  }

  
  @JsonProperty("version")
  @NotNull
  public String getVersion() {
    return version;
  }

  @JsonProperty("version")
  public void setVersion(String version) {
    this.version = version;
  }

  /**
   **/
  public VersionExternalApiInfoResult hash(String hash) {
    this.hash = hash;
    return this;
  }

  
  @JsonProperty("hash")
  @NotNull
  public String getHash() {
    return hash;
  }

  @JsonProperty("hash")
  public void setHash(String hash) {
    this.hash = hash;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VersionExternalApiInfoResult versionExternalApiInfoResult = (VersionExternalApiInfoResult) o;
    return Objects.equals(this.version, versionExternalApiInfoResult.version) &&
        Objects.equals(this.hash, versionExternalApiInfoResult.hash);
  }

  @Override
  public int hashCode() {
    return Objects.hash(version, hash);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VersionExternalApiInfoResult {\n");
    
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    hash: ").append(toIndentedString(hash)).append("\n");
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

