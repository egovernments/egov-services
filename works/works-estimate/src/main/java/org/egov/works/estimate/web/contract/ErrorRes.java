package org.egov.works.estimate.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * All APIs will return ErrorRes in case of failure which will carry ResponseInfo as metadata and Error object as actual representation of error. In case of bulk apis, some apis may chose to return the array of Error objects to indicate individual failure.
 */
@ApiModel(description = "All APIs will return ErrorRes in case of failure which will carry ResponseInfo as metadata and Error object as actual representation of error. In case of bulk apis, some apis may chose to return the array of Error objects to indicate individual failure.")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-29T09:03:53.949Z")

public class ErrorRes   {
  @JsonProperty("ResponseInfo")
  private ResponseInfo responseInfo = null;

  @JsonProperty("Errors")
  private List<java.lang.Error> errors = null;

  public ErrorRes responseInfo(ResponseInfo responseInfo) {
    this.responseInfo = responseInfo;
    return this;
  }

   /**
   * Get responseInfo
   * @return responseInfo
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public ResponseInfo getResponseInfo() {
    return responseInfo;
  }

  public void setResponseInfo(ResponseInfo responseInfo) {
    this.responseInfo = responseInfo;
  }

  public ErrorRes errors(List<java.lang.Error> errors) {
    this.errors = errors;
    return this;
  }

  public ErrorRes addErrorsItem(java.lang.Error errorsItem) {
    if (this.errors == null) {
      this.errors = new ArrayList<java.lang.Error>();
    }
    this.errors.add(errorsItem);
    return this;
  }

   /**
   * Error response array corresponding to Request Object array. In case of single object submission or _search related paths this may be an array of one error element
   * @return errors
  **/
  @ApiModelProperty(value = "Error response array corresponding to Request Object array. In case of single object submission or _search related paths this may be an array of one error element")

  @Valid

  public List<java.lang.Error> getErrors() {
    return errors;
  }

  public void setErrors(List<java.lang.Error> errors) {
    this.errors = errors;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ErrorRes errorRes = (ErrorRes) o;
    return Objects.equals(this.responseInfo, errorRes.responseInfo) &&
        Objects.equals(this.errors, errorRes.errors);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseInfo, errors);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ErrorRes {\n");
    
    sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
    sb.append("    errors: ").append(toIndentedString(errors)).append("\n");
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

