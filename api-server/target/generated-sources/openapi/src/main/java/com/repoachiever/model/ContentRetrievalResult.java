package com.repoachiever.model;

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



@JsonTypeName("ContentRetrievalResult")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2024-05-13T20:01:32.631637+02:00[Europe/Warsaw]")@lombok.Data @lombok.NoArgsConstructor @lombok.AllArgsConstructor(staticName = "of")

public class ContentRetrievalResult  implements Serializable {
  private @Valid List<String> locations = new ArrayList<>();

  /**
   **/
  public ContentRetrievalResult locations(List<String> locations) {
    this.locations = locations;
    return this;
  }

  
  @JsonProperty("locations")
  @NotNull
  public List<String> getLocations() {
    return locations;
  }

  @JsonProperty("locations")
  public void setLocations(List<String> locations) {
    this.locations = locations;
  }

  public ContentRetrievalResult addLocationsItem(String locationsItem) {
    if (this.locations == null) {
      this.locations = new ArrayList<>();
    }

    this.locations.add(locationsItem);
    return this;
  }

  public ContentRetrievalResult removeLocationsItem(String locationsItem) {
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
    ContentRetrievalResult contentRetrievalResult = (ContentRetrievalResult) o;
    return Objects.equals(this.locations, contentRetrievalResult.locations);
  }

  @Override
  public int hashCode() {
    return Objects.hash(locations);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ContentRetrievalResult {\n");
    
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

