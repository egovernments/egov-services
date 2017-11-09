package org.egov.inv.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Categories defined under asset category type are shown in the drop down.
 */
@ApiModel(description = "Categories defined under asset category type are shown in the drop down.")
@javax.annotation.Generated(value = "org.egov.inv.codegen.languages.SpringCodegen", date = "2017-11-08T13:51:07.770Z")

public class LandDetails   {
  @JsonProperty("surveyNo")
  private String surveyNo = null;

  @JsonProperty("area")
  private Double area = null;

  @JsonProperty("code")
  private String code = null;

  public LandDetails surveyNo(String surveyNo) {
    this.surveyNo = surveyNo;
    return this;
  }

   /**
   * land survey number
   * @return surveyNo
  **/
  @ApiModelProperty(value = "land survey number")


  public String getSurveyNo() {
    return surveyNo;
  }

  public void setSurveyNo(String surveyNo) {
    this.surveyNo = surveyNo;
  }

  public LandDetails area(Double area) {
    this.area = area;
    return this;
  }

   /**
   * Area of the land.
   * @return area
  **/
  @ApiModelProperty(value = "Area of the land.")


  public Double getArea() {
    return area;
  }

  public void setArea(Double area) {
    this.area = area;
  }

  public LandDetails code(String code) {
    this.code = code;
    return this;
  }

   /**
   * code of the landdetails
   * @return code
  **/
  @ApiModelProperty(value = "code of the landdetails")


  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LandDetails landDetails = (LandDetails) o;
    return Objects.equals(this.surveyNo, landDetails.surveyNo) &&
        Objects.equals(this.area, landDetails.area) &&
        Objects.equals(this.code, landDetails.code);
  }

  @Override
  public int hashCode() {
    return Objects.hash(surveyNo, area, code);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LandDetails {\n");
    
    sb.append("    surveyNo: ").append(toIndentedString(surveyNo)).append("\n");
    sb.append("    area: ").append(toIndentedString(area)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
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

