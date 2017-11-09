package org.egov.inv.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.egov.inv.model.RequestInfo;
import org.egov.inv.model.Scrap;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Contract class for web request. Array of Scrap items  are used in case of create or update
 */
@ApiModel(description = "Contract class for web request. Array of Scrap items  are used in case of create or update")
@javax.annotation.Generated(value = "org.egov.inv.codegen.languages.SpringCodegen", date = "2017-11-08T13:51:07.770Z")

public class ScrapRequest   {
  @JsonProperty("RequestInfo")
  private RequestInfo requestInfo = null;

  @JsonProperty("scraps")
  private List<Scrap> scraps = new ArrayList<Scrap>();

  public ScrapRequest requestInfo(RequestInfo requestInfo) {
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

  public ScrapRequest scraps(List<Scrap> scraps) {
    this.scraps = scraps;
    return this;
  }

  public ScrapRequest addScrapsItem(Scrap scrapsItem) {
    this.scraps.add(scrapsItem);
    return this;
  }

   /**
   * Used for search result and create only
   * @return scraps
  **/
  @ApiModelProperty(required = true, value = "Used for search result and create only")
  @NotNull

  @Valid

  public List<Scrap> getScraps() {
    return scraps;
  }

  public void setScraps(List<Scrap> scraps) {
    this.scraps = scraps;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ScrapRequest scrapRequest = (ScrapRequest) o;
    return Objects.equals(this.requestInfo, scrapRequest.requestInfo) &&
        Objects.equals(this.scraps, scrapRequest.scraps);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestInfo, scraps);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ScrapRequest {\n");
    
    sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
    sb.append("    scraps: ").append(toIndentedString(scraps)).append("\n");
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

