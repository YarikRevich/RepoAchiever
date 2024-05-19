package com.repoachiever.model;

import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("LocationsUnit")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2024-05-19T13:57:36.985010+02:00[Europe/Warsaw]")@lombok.Data @lombok.NoArgsConstructor @lombok.AllArgsConstructor(staticName = "of")

public class LocationsUnit  implements Serializable {
  private @Valid String name;
  private @Valid Boolean additional = false;

  /**
   **/
  public LocationsUnit name(String name) {
    this.name = name;
    return this;
  }

  
  @JsonProperty("name")
  @NotNull
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  /**
   **/
  public LocationsUnit additional(Boolean additional) {
    this.additional = additional;
    return this;
  }

  
  @JsonProperty("additional")
  @NotNull
  public Boolean getAdditional() {
    return additional;
  }

  @JsonProperty("additional")
  public void setAdditional(Boolean additional) {
    this.additional = additional;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LocationsUnit locationsUnit = (LocationsUnit) o;
    return Objects.equals(this.name, locationsUnit.name) &&
        Objects.equals(this.additional, locationsUnit.additional);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, additional);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LocationsUnit {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    additional: ").append(toIndentedString(additional)).append("\n");
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

