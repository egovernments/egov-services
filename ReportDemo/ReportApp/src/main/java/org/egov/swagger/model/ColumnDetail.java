package org.egov.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


import javax.validation.constraints.*;
/**
 * ColumnDetail
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-06-30T13:11:27.519Z")

public class ColumnDetail extends ColumnDef  {
  @JsonProperty("label")
  private String label = null;

  /**
   * column type to help the consumer. EPOCH means time being carried over in epoch format, while date means time in given display format URL - will be used to indicate taht the column value is basically a URL to some other resource 
   */
  public enum TypeEnum {
    NUMBER("number"),
    
    STRING("string"),
    
    DATE("date"),
    
    DATETIME("datetime"),
    
    EPOCH("epoch"),
    
    URL("url"),
    
    SINGLEVALUELIST("singlevaluelist"),
    
    MULTIVALUELIST("multivaluelist");

    private String value;

    TypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TypeEnum fromValue(String text) {
      for (TypeEnum b : TypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("type")
  private TypeEnum type = null;

  public ColumnDetail label(String label) {
    this.label = label;
    return this;
  }

   /**
   * localization label for the column. In case label is not provided, name would be used as a label.
   * @return label
  **/
  
  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public ColumnDetail type(TypeEnum type) {
    this.type = type;
    return this;
  }

   /**
   * column type to help the consumer. EPOCH means time being carried over in epoch format, while date means time in given display format URL - will be used to indicate taht the column value is basically a URL to some other resource 
   * @return type
  **/
  
  @NotNull
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ColumnDetail columnDetail = (ColumnDetail) o;
    return Objects.equals(this.label, columnDetail.label) &&
        Objects.equals(this.type, columnDetail.type) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(label, type, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ColumnDetail {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    label: ").append(toIndentedString(label)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
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

