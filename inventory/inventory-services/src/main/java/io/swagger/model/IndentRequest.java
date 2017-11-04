package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.Indent;
import io.swagger.model.RequestInfo;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Contract class for web request. Array of Indent items  are used in case of create or update
 */
@ApiModel(description = "Contract class for web request. Array of Indent items  are used in case of create or update")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-28T13:21:55.964+05:30")

public class IndentRequest   {
  @JsonProperty("requestInfo")
  private RequestInfo requestInfo = null;

  @JsonProperty("indents")
  @Valid
  private List<Indent> indents = null;

  public IndentRequest requestInfo(RequestInfo requestInfo) {
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

  public IndentRequest indents(List<Indent> indents) {
    this.indents = indents;
    return this;
  }

  public IndentRequest addIndentsItem(Indent indentsItem) {
    if (this.indents == null) {
      this.indents = new ArrayList<Indent>();
    }
    this.indents.add(indentsItem);
    return this;
  }

   /**
   * Used for search result and create only
   * @return indents
  **/
  @ApiModelProperty(value = "Used for search result and create only")

  @Valid

  public List<Indent> getIndents() {
    return indents;
  }

  public void setIndents(List<Indent> indents) {
    this.indents = indents;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IndentRequest indentRequest = (IndentRequest) o;
    return Objects.equals(this.requestInfo, indentRequest.requestInfo) &&
        Objects.equals(this.indents, indentRequest.indents);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestInfo, indents);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndentRequest {\n");
    
    sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
    sb.append("    indents: ").append(toIndentedString(indents)).append("\n");
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

