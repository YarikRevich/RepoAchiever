package com.repoachiever.model;

import com.repoachiever.model.CredentialsFieldsExternal;
import com.repoachiever.model.CredentialsFieldsInternal;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("CredentialsFieldsFull")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2024-05-11T15:49:02.903593+02:00[Europe/Warsaw]")@lombok.Data @lombok.NoArgsConstructor @lombok.AllArgsConstructor(staticName = "of")

public class CredentialsFieldsFull  implements Serializable {
  private @Valid CredentialsFieldsInternal internal;
  private @Valid CredentialsFieldsExternal external;

  /**
   **/
  public CredentialsFieldsFull internal(CredentialsFieldsInternal internal) {
    this.internal = internal;
    return this;
  }

  
  @JsonProperty("internal")
  @NotNull
  public CredentialsFieldsInternal getInternal() {
    return internal;
  }

  @JsonProperty("internal")
  public void setInternal(CredentialsFieldsInternal internal) {
    this.internal = internal;
  }

  /**
   **/
  public CredentialsFieldsFull external(CredentialsFieldsExternal external) {
    this.external = external;
    return this;
  }

  
  @JsonProperty("external")
  public CredentialsFieldsExternal getExternal() {
    return external;
  }

  @JsonProperty("external")
  public void setExternal(CredentialsFieldsExternal external) {
    this.external = external;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CredentialsFieldsFull credentialsFieldsFull = (CredentialsFieldsFull) o;
    return Objects.equals(this.internal, credentialsFieldsFull.internal) &&
        Objects.equals(this.external, credentialsFieldsFull.external);
  }

  @Override
  public int hashCode() {
    return Objects.hash(internal, external);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CredentialsFieldsFull {\n");
    
    sb.append("    internal: ").append(toIndentedString(internal)).append("\n");
    sb.append("    external: ").append(toIndentedString(external)).append("\n");
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

