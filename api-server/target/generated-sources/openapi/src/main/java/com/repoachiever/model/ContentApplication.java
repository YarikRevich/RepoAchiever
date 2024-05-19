package com.repoachiever.model;

import com.repoachiever.model.ContentUnit;
import com.repoachiever.model.CredentialsFieldsFull;
import com.repoachiever.model.Exporter;
import com.repoachiever.model.Provider;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("ContentApplication")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2024-05-19T15:06:43.082824+02:00[Europe/Warsaw]")@lombok.Data @lombok.NoArgsConstructor @lombok.AllArgsConstructor(staticName = "of")

public class ContentApplication  implements Serializable {
  private @Valid ContentUnit content;
  private @Valid Provider provider;
  private @Valid Exporter exporter;
  private @Valid CredentialsFieldsFull credentials;

  /**
   **/
  public ContentApplication content(ContentUnit content) {
    this.content = content;
    return this;
  }

  
  @JsonProperty("content")
  @NotNull
  public ContentUnit getContent() {
    return content;
  }

  @JsonProperty("content")
  public void setContent(ContentUnit content) {
    this.content = content;
  }

  /**
   **/
  public ContentApplication provider(Provider provider) {
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
  public ContentApplication exporter(Exporter exporter) {
    this.exporter = exporter;
    return this;
  }

  
  @JsonProperty("exporter")
  public Exporter getExporter() {
    return exporter;
  }

  @JsonProperty("exporter")
  public void setExporter(Exporter exporter) {
    this.exporter = exporter;
  }

  /**
   **/
  public ContentApplication credentials(CredentialsFieldsFull credentials) {
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
    ContentApplication contentApplication = (ContentApplication) o;
    return Objects.equals(this.content, contentApplication.content) &&
        Objects.equals(this.provider, contentApplication.provider) &&
        Objects.equals(this.exporter, contentApplication.exporter) &&
        Objects.equals(this.credentials, contentApplication.credentials);
  }

  @Override
  public int hashCode() {
    return Objects.hash(content, provider, exporter, credentials);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ContentApplication {\n");
    
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
    sb.append("    provider: ").append(toIndentedString(provider)).append("\n");
    sb.append("    exporter: ").append(toIndentedString(exporter)).append("\n");
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

