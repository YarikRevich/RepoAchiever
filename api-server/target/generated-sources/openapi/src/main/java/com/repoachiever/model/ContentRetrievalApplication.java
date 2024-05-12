package com.repoachiever.model;

import com.repoachiever.model.CredentialsFieldsFull;
import com.repoachiever.model.Provider;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("ContentRetrievalApplication")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2024-05-12T16:25:41.890330+02:00[Europe/Warsaw]")@lombok.Data @lombok.NoArgsConstructor @lombok.AllArgsConstructor(staticName = "of")

public class ContentRetrievalApplication  implements Serializable {
  private @Valid Provider provider;
  private @Valid CredentialsFieldsFull credentials;

  /**
   **/
  public ContentRetrievalApplication provider(Provider provider) {
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
  public ContentRetrievalApplication credentials(CredentialsFieldsFull credentials) {
    this.credentials = credentials;
    return this;
  }

  
  @JsonProperty("credentials")
  @NotNull
  public CredentialsFieldsFull getCredentials() {
    return credentials;
  }

  @JsonProperty("credentials")
  public void setCredentials(CredentialsFieldsFull credentials) {
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
    ContentRetrievalApplication contentRetrievalApplication = (ContentRetrievalApplication) o;
    return Objects.equals(this.provider, contentRetrievalApplication.provider) &&
        Objects.equals(this.credentials, contentRetrievalApplication.credentials);
  }

  @Override
  public int hashCode() {
    return Objects.hash(provider, credentials);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ContentRetrievalApplication {\n");
    
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
