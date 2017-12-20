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

public class QualityTestingRequest   {
  @JsonProperty("RequestInfo")
  private RequestInfo requestInfo = null;

  @JsonProperty("qualityTestings")
  private List<QualityTesting> qualityTestings = null;

  public QualityTestingRequest requestInfo(RequestInfo requestInfo) {
    this.requestInfo = requestInfo;
    return this;
  }

   /**
   * Get requestInfo
   * @return requestInfo
  **/
  @ApiModelProperty(value = "")

  @Valid

  public RequestInfo getRequestInfo() {
    return requestInfo;
  }

  public void setRequestInfo(RequestInfo requestInfo) {
    this.requestInfo = requestInfo;
  }

  public QualityTestingRequest qualityTestings(List<QualityTesting> qualityTestings) {
    this.qualityTestings = qualityTestings;
    return this;
  }

  public QualityTestingRequest addQualityTestingsItem(QualityTesting qualityTestingsItem) {
    if (this.qualityTestings == null) {
      this.qualityTestings = new ArrayList<QualityTesting>();
    }
    this.qualityTestings.add(qualityTestingsItem);
    return this;
  }

   /**
   * Used for create and update only
   * @return qualityTestings
  **/
  @ApiModelProperty(value = "Used for create and update only")

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
    QualityTestingRequest qualityTestingRequest = (QualityTestingRequest) o;
    return Objects.equals(this.requestInfo, qualityTestingRequest.requestInfo) &&
        Objects.equals(this.qualityTestings, qualityTestingRequest.qualityTestings);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestInfo, qualityTestings);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QualityTestingRequest {\n");
    
    sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
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

