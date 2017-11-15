package org.egov.inv.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Additional information of the asset.Hold the data for dynamic custom field in JSON format. There key and value will be LABEL NAME and USER INPUT DATA respactively.
 */
@ApiModel(description = "Additional information of the asset.Hold the data for dynamic custom field in JSON format. There key and value will be LABEL NAME and USER INPUT DATA respactively.")
@javax.annotation.Generated(value = "org.egov.inv.codegen.languages.SpringCodegen", date = "2017-11-08T13:51:07.770Z")

public class Attributes   {
  @JsonProperty("key")
  private String key = null;

  @JsonProperty("type")
  private String type = null;

  @JsonProperty("value")
  private Object value = null;

  public Attributes key(String key) {
    this.key = key;
    return this;
  }

   /**
   * LABEL Name or Table name.
   * @return key
  **/
  @ApiModelProperty(value = "LABEL Name or Table name.")


  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public Attributes type(String type) {
    this.type = type;
    return this;
  }

   /**
   * type of the field.
   * @return type
  **/
  @ApiModelProperty(value = "type of the field.")


  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Attributes value(Object value) {
    this.value = value;
    return this;
  }

   /**
   * value of the field(if data type is text the value will be string, if data type is multivaluelist then value will be list, if datatype is table then value will be the key value pair where key is  column name and value is column value).
   * @return value
  **/
  @ApiModelProperty(value = "value of the field(if data type is text the value will be string, if data type is multivaluelist then value will be list, if datatype is table then value will be the key value pair where key is  column name and value is column value).")


  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Attributes attributes = (Attributes) o;
    return Objects.equals(this.key, attributes.key) &&
        Objects.equals(this.type, attributes.type) &&
        Objects.equals(this.value, attributes.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(key, type, value);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Attributes {\n");
    
    sb.append("    key: ").append(toIndentedString(key)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
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

