package com.repoachiever.model;

import com.repoachiever.model.GitGitHubCredentials;
import com.repoachiever.model.GitLocalCredentials;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("CredentialsFields")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2024-04-19T11:12:35.929414+02:00[Europe/Warsaw]")@lombok.Data @lombok.NoArgsConstructor @lombok.AllArgsConstructor(staticName = "of")

public class CredentialsFields  implements Serializable {
  private @Valid String stub = "stub";
  private @Valid String token;

  /**
   **/
  public CredentialsFields stub(String stub) {
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

  /**
   **/
  public CredentialsFields token(String token) {
    this.token = token;
    return this;
  }

  
  @JsonProperty("token")
  @NotNull
  public String getToken() {
    return token;
  }

  @JsonProperty("token")
  public void setToken(String token) {
    this.token = token;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CredentialsFields credentialsFields = (CredentialsFields) o;
    return Objects.equals(this.stub, credentialsFields.stub) &&
        Objects.equals(this.token, credentialsFields.token);
  }

  @Override
  public int hashCode() {
    return Objects.hash(stub, token);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CredentialsFields {\n");
    
    sb.append("    stub: ").append(toIndentedString(stub)).append("\n");
    sb.append("    token: ").append(toIndentedString(token)).append("\n");
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

