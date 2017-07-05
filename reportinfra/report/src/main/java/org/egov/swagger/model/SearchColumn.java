package org.egov.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


import javax.validation.constraints.*;
/**
 * This is the column definition for the purpose of defining the search columns 
 */


public class SearchColumn extends ColumnDetail  {
  @JsonProperty("source")
  private String source = null;

  @JsonProperty("colName")
  private String colName = null;

  @JsonProperty("isMandatory")
  private Boolean isMandatory = true;

  public SearchColumn source(String source) {
    this.source = source;
    return this;
  }

   /**
   * Table/Index path to which the column belongsor the URL from which to fecth the data if it is a singlevalue or multivalue list 
   * @return source
  **/
 
  @NotNull
  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public SearchColumn colName(String colName) {
    this.colName = colName;
    return this;
  }

   /**
   * column name in the table/index 
   * @return colName
  **/
  
  @NotNull
  public String getColName() {
    return colName;
  }

  public void setColName(String colName) {
    this.colName = colName;
  }

  public SearchColumn isMandatory(Boolean isMandatory) {
    this.isMandatory = isMandatory;
    return this;
  }

   /**
   * Get isMandatory
   * @return isMandatory
  **/
  
  public Boolean getIsMandatory() {
    return isMandatory;
  }

  public void setIsMandatory(Boolean isMandatory) {
    this.isMandatory = isMandatory;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SearchColumn searchColumn = (SearchColumn) o;
    return Objects.equals(this.source, searchColumn.source) &&
        Objects.equals(this.colName, searchColumn.colName) &&
        Objects.equals(this.isMandatory, searchColumn.isMandatory) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(source, colName, isMandatory, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SearchColumn {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    colName: ").append(toIndentedString(colName)).append("\n");
    sb.append("    isMandatory: ").append(toIndentedString(isMandatory)).append("\n");
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

