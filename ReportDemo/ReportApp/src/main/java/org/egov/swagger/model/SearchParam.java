package org.egov.swagger.model;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * SearchParam
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-06-30T13:11:27.519Z")

public class SearchParam extends ColumnDef  {
  @JsonProperty("value")
  private Object value = null;

  public SearchParam value(String value) {
    this.value = value;
    return this;
  }

   /**
   * User provided value of this parameter that will be used in query. Please note that value will be format checked against the value deficition of this parameter in report definition 
   * @return value
  **/
  
  @NotNull
  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

}

