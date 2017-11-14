package org.egov.inv.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Contract class for web response. Array of Store items  are used in case of search ,create or update request.
 */
@ApiModel(description = "Contract class for web response. Array of Store items  are used in case of search ,create or update request.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-28T13:21:55.964+05:30")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreResponse   {
  @JsonProperty("responseInfo")
  private ResponseInfo responseInfo = null;

  @JsonProperty("stores")
  @Valid
  private List<Store> stores = null;

  @JsonProperty("page")
  private Page page = null;

  public StoreResponse responseInfo(ResponseInfo responseInfo) {
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

  public StoreResponse stores(List<Store> stores) {
    this.stores = stores;
    return this;
  }

  public StoreResponse addStoresItem(Store storesItem) {
    if (this.stores == null) {
      this.stores = new ArrayList<Store>();
    }
    this.stores.add(storesItem);
    return this;
  }

   /**
   * Used for search result and create only
   * @return stores
  **/
  @ApiModelProperty(value = "Used for search result and create only")

  @Valid

  public List<Store> getStores() {
    return stores;
  }

  public void setStores(List<Store> stores) {
    this.stores = stores;
  }

  public StoreResponse page(Page page) {
    this.page = page;
    return this;
  }

   /**
   * Get page
   * @return page
  **/
  @ApiModelProperty(value = "")

  @Valid

  public Page getPage() {
    return page;
  }

  public void setPage(Page page) {
    this.page = page;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StoreResponse storeResponse = (StoreResponse) o;
    return Objects.equals(this.responseInfo, storeResponse.responseInfo) &&
        Objects.equals(this.stores, storeResponse.stores) &&
        Objects.equals(this.page, storeResponse.page);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseInfo, stores, page);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StoreResponse {\n");
    
    sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
    sb.append("    stores: ").append(toIndentedString(stores)).append("\n");
    sb.append("    page: ").append(toIndentedString(page)).append("\n");
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

