package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.RequestInfo;
import io.swagger.model.Store;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Contract class for web request. Array of Store items  are used in case of create or update
 */
@ApiModel(description = "Contract class for web request. Array of Store items  are used in case of create or update")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-28T13:21:55.964+05:30")

public class StoreRequest   {
  @JsonProperty("requestInfo")
  private RequestInfo requestInfo = null;

  @JsonProperty("stores")
  @Valid
  private List<Store> stores = null;

  public StoreRequest requestInfo(RequestInfo requestInfo) {
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

  public StoreRequest stores(List<Store> stores) {
    this.stores = stores;
    return this;
  }

  public StoreRequest addStoresItem(Store storesItem) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StoreRequest storeRequest = (StoreRequest) o;
    return Objects.equals(this.requestInfo, storeRequest.requestInfo) &&
        Objects.equals(this.stores, storeRequest.stores);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestInfo, stores);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StoreRequest {\n");
    
    sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
    sb.append("    stores: ").append(toIndentedString(stores)).append("\n");
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

