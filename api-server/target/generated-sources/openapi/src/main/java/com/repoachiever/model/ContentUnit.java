package com.repoachiever.model;

import com.repoachiever.model.LocationsUnit;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("ContentUnit")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2024-05-19T15:06:43.082824+02:00[Europe/Warsaw]")@lombok.Data @lombok.NoArgsConstructor @lombok.AllArgsConstructor(staticName = "of")

public class ContentUnit  implements Serializable {
  private @Valid List<LocationsUnit> locations = new ArrayList<>();

  /**
   **/
  public ContentUnit locations(List<LocationsUnit> locations) {
    this.locations = locations;
    return this;
  }

  
  @JsonProperty("locations")
  @NotNull
  public List<LocationsUnit> getLocations() {
    return locations;
  }

  @JsonProperty("locations")
  public void setLocations(List<LocationsUnit> locations) {
    this.locations = locations;
  }

  public ContentUnit addLocationsItem(LocationsUnit locationsItem) {
    if (this.locations == null) {
      this.locations = new ArrayList<>();
    }

    this.locations.add(locationsItem);
    return this;
  }

  public ContentUnit removeLocationsItem(LocationsUnit locationsItem) {
    if (locationsItem != null && this.locations != null) {
      this.locations.remove(locationsItem);
    }

    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ContentUnit contentUnit = (ContentUnit) o;
    return Objects.equals(this.locations, contentUnit.locations);
  }

  @Override
  public int hashCode() {
    return Objects.hash(locations);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ContentUnit {\n");
    
    sb.append("    locations: ").append(toIndentedString(locations)).append("\n");
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

