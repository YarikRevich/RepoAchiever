package com.repoachiever.model;

import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("GitLocalCredentials")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2024-04-19T11:12:35.929414+02:00[Europe/Warsaw]")@lombok.Data @lombok.NoArgsConstructor @lombok.AllArgsConstructor(staticName = "of")

public class GitLocalCredentials  implements Serializable {
  private @Valid String stub = "stub";

  /**
   **/
  public GitLocalCredentials stub(String stub) {
    this.stub = stub;
    return this;
  }

  
  @JsonProperty("stub")
  @NotNull
  public String getStub() {
    return stub;
  }

  @JsonProperty("stub")
  public void setStub(String stub) {
    this.stub = stub;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GitLocalCredentials gitLocalCredentials = (GitLocalCredentials) o;
    return Objects.equals(this.stub, gitLocalCredentials.stub);
  }

  @Override
  public int hashCode() {
    return Objects.hash(stub);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GitLocalCredentials {\n");
    
    sb.append("    stub: ").append(toIndentedString(stub)).append("\n");
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

