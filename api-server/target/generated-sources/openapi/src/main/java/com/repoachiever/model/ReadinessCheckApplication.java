package com.repoachiever.model;

import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("ReadinessCheckApplication")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2024-05-19T15:06:43.082824+02:00[Europe/Warsaw]")@lombok.Data @lombok.NoArgsConstructor @lombok.AllArgsConstructor(staticName = "of")

public class ReadinessCheckApplication  implements Serializable {
  private @Valid Object test;

  /**
   **/
  public ReadinessCheckApplication test(Object test) {
    this.test = test;
    return this;
  }

  
  @JsonProperty("test")
  public Object getTest() {
    return test;
  }

  @JsonProperty("test")
  public void setTest(Object test) {
    this.test = test;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReadinessCheckApplication readinessCheckApplication = (ReadinessCheckApplication) o;
    return Objects.equals(this.test, readinessCheckApplication.test);
  }

  @Override
  public int hashCode() {
    return Objects.hash(test);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReadinessCheckApplication {\n");
    
    sb.append("    test: ").append(toIndentedString(test)).append("\n");
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

