package com.repoachiever.model;

import com.repoachiever.model.CredentialsFields;
import com.repoachiever.model.Provider;
import java.util.UUID;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("ValidationSecretsApplication")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2024-04-19T11:12:35.929414+02:00[Europe/Warsaw]")@lombok.Data @lombok.NoArgsConstructor @lombok.AllArgsConstructor(staticName = "of")

public class ValidationSecretsApplication  implements Serializable {
  private @Valid UUID id;
  private @Valid Provider provider;
  private @Valid CredentialsFields credentials;

  /**
   **/
  public ValidationSecretsApplication id(UUID id) {
    this.id = id;
    return this;
  }

  
  @JsonProperty("id")
  @NotNull
  public UUID getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   **/
  public ValidationSecretsApplication provider(Provider provider) {
    this.provider = provider;
    return this;
  }

  
  @JsonProperty("provider")
  @NotNull
  public Provider getProvider() {
    return provider;
  }

  @JsonProperty("provider")
  public void setProvider(Provider provider) {
    this.provider = provider;
  }

  /**
   **/
  public ValidationSecretsApplication credentials(CredentialsFields credentials) {
    this.credentials = credentials;
    return this;
  }

  
  @JsonProperty("credentials")
  @NotNull
  public CredentialsFields getCredentials() {
    return credentials;
  }

  @JsonProperty("credentials")
  public void setCredentials(CredentialsFields credentials) {
    this.credentials = credentials;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ValidationSecretsApplication validationSecretsApplication = (ValidationSecretsApplication) o;
    return Objects.equals(this.id, validationSecretsApplication.id) &&
        Objects.equals(this.provider, validationSecretsApplication.provider) &&
        Objects.equals(this.credentials, validationSecretsApplication.credentials);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, provider, credentials);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ValidationSecretsApplication {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    provider: ").append(toIndentedString(provider)).append("\n");
    sb.append("    credentials: ").append(toIndentedString(credentials)).append("\n");
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

