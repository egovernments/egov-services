package org.egov.works.qualitycontrol.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Contract class to send response. Array of Quality Testing items are used in case of search results, also multiple  Quality Testing item is used for create and update
 */
@ApiModel(description = "Contract class to send response. Array of Quality Testing items are used in case of search results, also multiple  Quality Testing item is used for create and update")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-12-16T15:20:43.552Z")

public class QualityTestingResponse   {
  @JsonProperty("ResponseInfo")
  private ResponseInfo responseInfo = null;

  @JsonProperty("qualityTestings")
  private List<QualityTesting> qualityTestings = null;

  public QualityTestingResponse responseInfo(ResponseInfo responseInfo) {
    this.responseInfo = responseInfo;
    return this;
  }

   /**
   * Get responseInfo
   * @return responseInfo
  **/
  @ApiModelProperty(value = "")

  @Valid

  public ResponseInfo getResponseInfo() {
    return responseInfo;
  }

  public void setResponseInfo(ResponseInfo responseInfo) {
    this.responseInfo = responseInfo;
  }

  public QualityTestingResponse qualityTestings(List<QualityTesting> qualityTestings) {
    this.qualityTestings = qualityTestings;
    return this;
  }

  public QualityTestingResponse addQualityTestingsItem(QualityTesting qualityTestingsItem) {
    if (this.qualityTestings == null) {
      this.qualityTestings = new ArrayList<QualityTesting>();
    }
    this.qualityTestings.add(qualityTestingsItem);
    return this;
  }

   /**
   * Used for search result and create only
   * @return qualityTestings
  **/
  @ApiModelProperty(value = "Used for search result and create only")

  @Valid

  public List<QualityTesting> getQualityTestings() {
    return qualityTestings;
  }

  public void setQualityTestings(List<QualityTesting> qualityTestings) {
    this.qualityTestings = qualityTestings;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QualityTestingResponse qualityTestingResponse = (QualityTestingResponse) o;
    return Objects.equals(this.responseInfo, qualityTestingResponse.responseInfo) &&
        Objects.equals(this.qualityTestings, qualityTestingResponse.qualityTestings);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseInfo, qualityTestings);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QualityTestingResponse {\n");
    
    sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
    sb.append("    qualityTestings: ").append(toIndentedString(qualityTestings)).append("\n");
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

