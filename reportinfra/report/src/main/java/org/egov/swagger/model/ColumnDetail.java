package org.egov.swagger.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
/**
 * ColumnDetail
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-06-30T13:11:27.519Z")

public class ColumnDetail extends ColumnDef  {
  @JsonProperty("label")
  private String label = null;

  @JsonProperty("pattern")
  private String pattern;
  
  @JsonProperty("type")
  private TypeEnum type = null;
  
  
  public ColumnDetail() {
		
	}
  
  public ColumnDetail(String label, String pattern, TypeEnum type, String name) {
	super();
	this.label = label;
	this.pattern = pattern;
	this.type = type;
	this.name(name);
}
  public ColumnDetail(String label, String pattern, TypeEnum type) {
		super();
		this.label = label;
		this.pattern = pattern;
		this.type = type;
	}
public String getLabel() {
	return label;
}
public void setLabel(String label) {
	this.label = label;
}

public String getPattern() {
	return pattern;
}

public void setPattern(String pattern) {
	this.pattern = pattern;
}

public TypeEnum getType() {
	return type;
}

public void setType(TypeEnum type) {
	this.type = type;
}


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

@JsonProperty("defaultValue")
private Object defaultValue = null;
public ColumnDetail defaultValue(Object defaultValue) {
    this.defaultValue = defaultValue;
    return this;
  }

   /**
   * any default value for the column if the column type is number, string, date, datetime, epoch or URL collection contaning possible list value pairs for singlevaluelist and multivaluelist 
   * @return defaultValue
  **/
  
  public Object getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(Object defaultValue) {
    this.defaultValue = defaultValue;
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
        Objects.equals(this.defaultValue, columnDetail.defaultValue) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(label, type, defaultValue, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ColumnDetail {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    label: ").append(toIndentedString(label)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    defaultValue: ").append(toIndentedString(defaultValue)).append("\n");
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

