package org.egov.tradelicense.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.egov.tradelicense.web.requests.ResponseInfo;

import com.fasterxml.jackson.annotation.JsonProperty;


public class ErrorRes   {
  @JsonProperty("ResponseInfo")
  private ResponseInfo responseInfo = null;

  @JsonProperty("Errors")
  private List<Error> errors = null;

  public ErrorRes responseInfo(ResponseInfo responseInfo) {
    this.responseInfo = responseInfo;
    return this;
  }


  @Valid
  public ResponseInfo getResponseInfo() {
    return responseInfo;
  }

  public void setResponseInfo(ResponseInfo responseInfo) {
    this.responseInfo = responseInfo;
  }

  public ErrorRes errors(List<Error> errors) {
    this.errors = errors;
    return this;
  }

  public ErrorRes addErrorsItem(Error errorsItem) {
    if (this.errors == null) {
      this.errors = new ArrayList<Error>();
    }
    this.errors.add(errorsItem);
    return this;
  }


  public List<Error> getErrors() {
    return errors;
  }

  public void setErrors(List<Error> errors) {
    this.errors = errors;
  }


  @Override
  public boolean equals(java.lang.Object o) {
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
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

