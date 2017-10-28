package org.egov.works.estimate.web.model;

import java.util.Objects;
import io.swagger.annotations.ApiModel;

/**
 * Fund is a defining concept in municipal accounting – where it is required to segregate all accounting transactions into designated funds. Each fund needs to be treated as an independent accounting entity – in other words, all vouchers within a fund must be self-balancing and balance sheets and IncomeExpenditure reports must be generated for each fund. A hierarchy of funds may be defined – i.e. each fund can have multiple sub-funds and so on. 
 */
@ApiModel(description = "Fund is a defining concept in municipal accounting – where it is required to segregate all accounting transactions into designated funds. Each fund needs to be treated as an independent accounting entity – in other words, all vouchers within a fund must be self-balancing and balance sheets and IncomeExpenditure reports must be generated for each fund. A hierarchy of funds may be defined – i.e. each fund can have multiple sub-funds and so on. ")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-28T12:22:31.360Z")

public class Fund   {

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
    sb.append("class Fund {\n");
    
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

