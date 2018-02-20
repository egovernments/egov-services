package org.egov.pgr.contract;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * ServiceErrors
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-02-20T05:39:55.235Z")

public class ServiceErrors extends ErrorRes  {
  /**
   * Gets or Sets codes
   */
  public enum CodesEnum {
    SERVICENOTFOUND("ServiceNotFound"),
    
    INVALIDSEARCHPARAMS("InvalidSearchParams"),
    
    TENANTNOTFOUND("TenantNotFound"),
    
    SEARCHCRITERIATOOBROAD("SearchCriteriaTooBroad");

    private String value;

    CodesEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static CodesEnum fromValue(String text) {
      for (CodesEnum b : CodesEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("codes")
  private CodesEnum codes = null;

  public ServiceErrors codes(CodesEnum codes) {
    this.codes = codes;
    return this;
  }

   /**
   * Get codes
   * @return codes
  **/


  public CodesEnum getCodes() {
    return codes;
  }

  public void setCodes(CodesEnum codes) {
    this.codes = codes;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ServiceErrors serviceErrors = (ServiceErrors) o;
    return Objects.equals(this.codes, serviceErrors.codes) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codes, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ServiceErrors {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    codes: ").append(toIndentedString(codes)).append("\n");
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

