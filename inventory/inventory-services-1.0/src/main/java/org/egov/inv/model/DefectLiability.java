package org.egov.inv.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Hold the asset defect liability related information.
 */
@ApiModel(description = "Hold the asset defect liability related information.")
@javax.annotation.Generated(value = "org.egov.inv.codegen.languages.SpringCodegen", date = "2017-11-08T13:51:07.770Z")

public class DefectLiability   {
  @JsonProperty("year")
  private Long year = null;

  @JsonProperty("month")
  private Long month = null;

  @JsonProperty("day")
  private Long day = null;

  public DefectLiability year(Long year) {
    this.year = year;
    return this;
  }

   /**
   * No of years.
   * @return year
  **/
  @ApiModelProperty(value = "No of years.")


  public Long getYear() {
    return year;
  }

  public void setYear(Long year) {
    this.year = year;
  }

  public DefectLiability month(Long month) {
    this.month = month;
    return this;
  }

   /**
   * No of months.
   * @return month
  **/
  @ApiModelProperty(value = "No of months.")


  public Long getMonth() {
    return month;
  }

  public void setMonth(Long month) {
    this.month = month;
  }

  public DefectLiability day(Long day) {
    this.day = day;
    return this;
  }

   /**
   * No of days.
   * @return day
  **/
  @ApiModelProperty(value = "No of days.")


  public Long getDay() {
    return day;
  }

  public void setDay(Long day) {
    this.day = day;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DefectLiability defectLiability = (DefectLiability) o;
    return Objects.equals(this.year, defectLiability.year) &&
        Objects.equals(this.month, defectLiability.month) &&
        Objects.equals(this.day, defectLiability.day);
  }

  @Override
  public int hashCode() {
    return Objects.hash(year, month, day);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DefectLiability {\n");
    
    sb.append("    year: ").append(toIndentedString(year)).append("\n");
    sb.append("    month: ").append(toIndentedString(month)).append("\n");
    sb.append("    day: ").append(toIndentedString(day)).append("\n");
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

