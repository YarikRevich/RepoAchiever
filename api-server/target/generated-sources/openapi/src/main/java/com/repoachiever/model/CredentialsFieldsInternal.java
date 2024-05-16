package com.repoachiever.model;

import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("CredentialsFieldsInternal")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2024-05-17T01:46:26.799650+02:00[Europe/Warsaw]")@lombok.Data @lombok.NoArgsConstructor @lombok.AllArgsConstructor(staticName = "of")

public class CredentialsFieldsInternal  implements Serializable {
  private @Valid Integer id;

  /**
   **/
  public CredentialsFieldsInternal id(Integer id) {
    this.id = id;
    return this;
  }

  
  @JsonProperty("id")
  public Integer getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(Integer id) {
    this.id = id;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CredentialsFieldsInternal credentialsFieldsInternal = (CredentialsFieldsInternal) o;
    return Objects.equals(this.id, credentialsFieldsInternal.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CredentialsFieldsInternal {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
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

