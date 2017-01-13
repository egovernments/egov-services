package org.egov.pgr.rest.web.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorRes   {
  @JsonProperty("ResposneInfo")
  private ResponseInfo resposneInfo = null;

  @JsonProperty("Error")
  private Error error = null;

  public ErrorRes resposneInfo(ResponseInfo resposneInfo) {
    this.resposneInfo = resposneInfo;
    return this;
  }

  public ResponseInfo getResposneInfo() {
    return resposneInfo;
  }

  public void setResposneInfo(ResponseInfo resposneInfo) {
    this.resposneInfo = resposneInfo;
  }

  public ErrorRes error(Error error) {
    this.error = error;
    return this;
  }

  public Error getError() {
    return error;
  }

  public void setError(Error error) {
    this.error = error;
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
    return Objects.equals(this.resposneInfo, errorRes.resposneInfo) &&
        Objects.equals(this.error, errorRes.error);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resposneInfo, error);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ErrorRes {\n");
    
    sb.append("    resposneInfo: ").append(toIndentedString(resposneInfo)).append("\n");
    sb.append("    error: ").append(toIndentedString(error)).append("\n");
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

