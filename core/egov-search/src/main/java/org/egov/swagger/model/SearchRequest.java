package org.egov.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;



public class SearchRequest extends MetadataRequest  {
  @JsonProperty("searchParams")
  private List<SearchParam> searchParams = new ArrayList<SearchParam>();

  public SearchRequest searchParams(List<SearchParam> searchParams) {
    this.searchParams = searchParams;
    return this;
  }

  public SearchRequest addSearchParamsItem(SearchParam searchParamsItem) {
    this.searchParams.add(searchParamsItem);
    return this;
  }

   /**
   * array of search parameters to use in report query 
   * @return searchParams
  **/
  
  public List<SearchParam> getSearchParams() {
    return searchParams;
  }

  public void setSearchParams(List<SearchParam> searchParams) {
    this.searchParams = searchParams;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SearchRequest reportRequest = (SearchRequest) o;
    return Objects.equals(this.searchParams, reportRequest.searchParams) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(searchParams, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReportRequest {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    searchParams: ").append(toIndentedString(searchParams)).append("\n");
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

