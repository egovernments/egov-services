package org.egov.works.measurementbook.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Contract class to send response. Array of Measurement Book items are used in case of search results, also multiple  Measurement Book item is used for create and update
 */
@ApiModel(description = "Contract class to send response. Array of Measurement Book items are used in case of search results, also multiple  Measurement Book item is used for create and update")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-23T09:58:12.227Z")

public class MeasurementBookRequest   {
  @JsonProperty("RequestInfo")
  private RequestInfo requestInfo = null;

  @JsonProperty("measurementBooks")
  private List<MeasurementBook> measurementBooks = null;

  public MeasurementBookRequest requestInfo(RequestInfo requestInfo) {
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

  public MeasurementBookRequest measurementBooks(List<MeasurementBook> measurementBooks) {
    this.measurementBooks = measurementBooks;
    return this;
  }

  public MeasurementBookRequest addMeasurementBooksItem(MeasurementBook measurementBooksItem) {
    if (this.measurementBooks == null) {
      this.measurementBooks = new ArrayList<MeasurementBook>();
    }
    this.measurementBooks.add(measurementBooksItem);
    return this;
  }

   /**
   * Used for create and update only
   * @return measurementBooks
  **/
  @ApiModelProperty(value = "Used for create and update only")

  @Valid

  public List<MeasurementBook> getMeasurementBooks() {
    return measurementBooks;
  }

  public void setMeasurementBooks(List<MeasurementBook> measurementBooks) {
    this.measurementBooks = measurementBooks;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MeasurementBookRequest measurementBookRequest = (MeasurementBookRequest) o;
    return Objects.equals(this.requestInfo, measurementBookRequest.requestInfo) &&
        Objects.equals(this.measurementBooks, measurementBookRequest.measurementBooks);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestInfo, measurementBooks);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MeasurementBookRequest {\n");
    
    sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
    sb.append("    measurementBooks: ").append(toIndentedString(measurementBooks)).append("\n");
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

