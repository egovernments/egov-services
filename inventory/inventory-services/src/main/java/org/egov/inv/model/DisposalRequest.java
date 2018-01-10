package org.egov.inv.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.egov.inv.model.Disposal;
import org.egov.inv.model.RequestInfo;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Contract class for web request. Array of Disposal items  are used in case of create or update
 */
@ApiModel(description = "Contract class for web request. Array of Disposal items  are used in case of create or update")
@javax.annotation.Generated(value = "org.egov.inv.codegen.languages.SpringCodegen", date = "2017-11-08T13:51:07.770Z")
@AllArgsConstructor
@NoArgsConstructor
public class DisposalRequest   {
  @JsonProperty("RequestInfo")
  private RequestInfo requestInfo = null;

  @JsonProperty("disposals")
  private List<Disposal> disposals = new ArrayList<Disposal>();

  public DisposalRequest requestInfo(RequestInfo requestInfo) {
    this.requestInfo = requestInfo;
    return this;
  }

   /**
   * Get requestInfo
   * @return requestInfo
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public RequestInfo getRequestInfo() {
    return requestInfo;
  }

  public void setRequestInfo(RequestInfo requestInfo) {
    this.requestInfo = requestInfo;
  }

  public DisposalRequest disposals(List<Disposal> disposals) {
    this.disposals = disposals;
    return this;
  }

  public DisposalRequest addDisposalsItem(Disposal disposalsItem) {
    this.disposals.add(disposalsItem);
    return this;
  }

   /**
   * Used for search result and create only
   * @return disposals
  **/
  @ApiModelProperty(required = true, value = "Used for search result and create only")
  @NotNull

  @Valid

  public List<Disposal> getDisposals() {
    return disposals;
  }

  public void setDisposals(List<Disposal> disposals) {
    this.disposals = disposals;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DisposalRequest disposalRequest = (DisposalRequest) o;
    return Objects.equals(this.requestInfo, disposalRequest.requestInfo) &&
        Objects.equals(this.disposals, disposalRequest.disposals);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestInfo, disposals);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DisposalRequest {\n");
    
    sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
    sb.append("    disposals: ").append(toIndentedString(disposals)).append("\n");
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

