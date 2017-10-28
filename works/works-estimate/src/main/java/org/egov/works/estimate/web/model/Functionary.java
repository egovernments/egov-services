package org.egov.works.estimate.web.model;

import io.swagger.annotations.ApiModel;

import java.util.Objects;

/**
 * Functionary is considered as another cost center. In the government set-up, demands for expenditure are drawn by the department discharging the functions and become the responsibility center for the assigned functions. Functionary group represents this. Each sub-level within this group typically can represent the organisational structure within the ULB. This level is used only for the internal control of the ULB. 
 */
@ApiModel(description = "Functionary is considered as another cost center. In the government set-up, demands for expenditure are drawn by the department discharging the functions and become the responsibility center for the assigned functions. Functionary group represents this. Each sub-level within this group typically can represent the organisational structure within the ULB. This level is used only for the internal control of the ULB. ")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-28T12:22:31.360Z")

public class Functionary   {

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hash();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Functionary {\n");
    
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

