package org.egov.swagger.model;

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




}

