package org.egov.pgr.rest.web.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AttributeValue   {
  @JsonProperty("code")
  private String code = null;

  @JsonProperty("value")
  private String value = null;

  public AttributeValue (String code, String value) {
    this.code = code;
    this.value = value;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public AttributeValue value(String value) {
    this.value = value;
    return this;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
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
    AttributeValue attributeValue = (AttributeValue) o;
    return Objects.equals(this.code, attributeValue.code) &&
        Objects.equals(this.value, attributeValue.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, value);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AttributeValue {\n");
    
    sb.append("code: ").append(toIndentedString(code)).append("\n");
    sb.append("value: ").append(toIndentedString(value)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}