package org.egov.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Contract class to send response. Array of Property items  are used in case of search results or response for create. Where as single Property item is used for update
 * Author : Narendra
 */

public class PropertyResponse   {
  @JsonProperty("responseInfo")
  private ResponseInfo responseInfo = null;

  @JsonProperty("properties")
  private List<Property> properties = new ArrayList<Property>();

   /**
   * Get responseInfo
   * @return responseInfo
  **/
  public ResponseInfo getResponseInfo() {
    return responseInfo;
  }

  public void setResponseInfo(ResponseInfo responseInfo) {
    this.responseInfo = responseInfo;
  }

   /**
   * Used for search result and create only
   * @return properties
  **/
  public List<Property> getProperties() {
    return properties;
  }

  public void setProperties(List<Property> properties) {
    this.properties = properties;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PropertyResponse propertyResponse = (PropertyResponse) o;
    return Objects.equals(this.responseInfo, propertyResponse.responseInfo) &&
        Objects.equals(this.properties, propertyResponse.properties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseInfo, properties);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PropertyResponse {\n");
    
    sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
    sb.append("    properties: ").append(toIndentedString(properties)).append("\n");
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

