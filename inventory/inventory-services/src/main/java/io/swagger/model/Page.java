package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Page
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-28T13:21:55.964+05:30")

public class Page   {
  @JsonProperty("totalResults")
  private Integer totalResults = null;

  @JsonProperty("totalPages")
  private Integer totalPages = null;

  @JsonProperty("pageSize")
  private Integer pageSize = null;

  @JsonProperty("currentPage")
  private Integer currentPage = null;

  @JsonProperty("offSet")
  private Integer offSet = null;

  public Page totalResults(Integer totalResults) {
    this.totalResults = totalResults;
    return this;
  }

   /**
   * total results of the Pagination
   * @return totalResults
  **/
  @ApiModelProperty(value = "total results of the Pagination")


  public Integer getTotalResults() {
    return totalResults;
  }

  public void setTotalResults(Integer totalResults) {
    this.totalResults = totalResults;
  }

  public Page totalPages(Integer totalPages) {
    this.totalPages = totalPages;
    return this;
  }

   /**
   * total pages of the Pagination
   * @return totalPages
  **/
  @ApiModelProperty(value = "total pages of the Pagination")


  public Integer getTotalPages() {
    return totalPages;
  }

  public void setTotalPages(Integer totalPages) {
    this.totalPages = totalPages;
  }

  public Page pageSize(Integer pageSize) {
    this.pageSize = pageSize;
    return this;
  }

   /**
   * Number of records in a per page in  the Pagination, Default value is 20
   * @return pageSize
  **/
  @ApiModelProperty(value = "Number of records in a per page in  the Pagination, Default value is 20")


  public Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public Page currentPage(Integer currentPage) {
    this.currentPage = currentPage;
    return this;
  }

   /**
   * current page of the Pagination
   * @return currentPage
  **/
  @ApiModelProperty(value = "current page of the Pagination")


  public Integer getCurrentPage() {
    return currentPage;
  }

  public void setCurrentPage(Integer currentPage) {
    this.currentPage = currentPage;
  }

  public Page offSet(Integer offSet) {
    this.offSet = offSet;
    return this;
  }

   /**
   * page number of the Pagination, Default value is 0
   * @return offSet
  **/
  @ApiModelProperty(value = "page number of the Pagination, Default value is 0")


  public Integer getOffSet() {
    return offSet;
  }

  public void setOffSet(Integer offSet) {
    this.offSet = offSet;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Page page = (Page) o;
    return Objects.equals(this.totalResults, page.totalResults) &&
        Objects.equals(this.totalPages, page.totalPages) &&
        Objects.equals(this.pageSize, page.pageSize) &&
        Objects.equals(this.currentPage, page.currentPage) &&
        Objects.equals(this.offSet, page.offSet);
  }

  @Override
  public int hashCode() {
    return Objects.hash(totalResults, totalPages, pageSize, currentPage, offSet);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Page {\n");
    
    sb.append("    totalResults: ").append(toIndentedString(totalResults)).append("\n");
    sb.append("    totalPages: ").append(toIndentedString(totalPages)).append("\n");
    sb.append("    pageSize: ").append(toIndentedString(pageSize)).append("\n");
    sb.append("    currentPage: ").append(toIndentedString(currentPage)).append("\n");
    sb.append("    offSet: ").append(toIndentedString(offSet)).append("\n");
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

