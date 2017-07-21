package org.egov.asset.model;



import com.fasterxml.jackson.annotation.JsonValue;

public enum AttributeType {
	
	TEXT("TEXT"),NUMBER("NUMBER"),SELECT("SELECT"),MULTISELECT("MULTISELECT"),DATE("DATE"),FILE("FILE"),TABLE("TABLE");
	
	 private String value;
	
	private AttributeType(String value) {
		this.value=value;
	}
	
	 @Override
	    @JsonValue
	    public String toString() {
	      return String.valueOf(value);
	    }

	 
	 public static AttributeType fromvalue(String text){
		 
		  for (AttributeType b : AttributeType.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
		return null;
		 
	 }
}
