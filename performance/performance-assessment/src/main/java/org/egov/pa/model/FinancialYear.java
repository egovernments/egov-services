package org.egov.pa.model;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Financial year
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-02T05:16:16.756Z")

public class FinancialYear   {
  @JsonProperty("fromDate")
  private Long fromDate = null;

  @JsonProperty("toDate")
  private Long toDate = null;

  public FinancialYear fromDate(Long fromDate) {
    this.fromDate = fromDate;
    return this;
  }

   /**
   * timestamp for the start of the fianncial year
   * @return fromDate
  **/
  @NotNull


  public Long getFromDate() {
    return fromDate;
  }

  public void setFromDate(Long fromDate) {
    this.fromDate = fromDate;
  }

  public FinancialYear toDate(Long toDate) {
    this.toDate = toDate;
    return this;
  }

   /**
   * timestamp for the end of the fianncial year
   * @return toDate
  **/
  @NotNull


  public Long getToDate() {
    return toDate;
  }

  public void setToDate(Long toDate) {
    this.toDate = toDate;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FinancialYear financialYear = (FinancialYear) o;
    return Objects.equals(this.fromDate, financialYear.fromDate) &&
        Objects.equals(this.toDate, financialYear.toDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fromDate, toDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FinancialYear {\n");
    
    sb.append("    fromDate: ").append(toIndentedString(fromDate)).append("\n");
    sb.append("    toDate: ").append(toIndentedString(toDate)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

