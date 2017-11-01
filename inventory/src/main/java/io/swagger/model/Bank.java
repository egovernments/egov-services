package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * This object holds the bank information.   
 */
@ApiModel(description = "This object holds the bank information.   ")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-01T09:47:46.371Z")

public class Bank   {
  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("acctNo")
  private String acctNo = null;

  @JsonProperty("ifsc")
  private String ifsc = null;

  @JsonProperty("micr")
  private String micr = null;

  public Bank tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Enum
   * @return tenantId
  **/
  @ApiModelProperty(value = "Tenant id of the Enum")

 @Size(min=4,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public Bank name(String name) {
    this.name = name;
    return this;
  }

   /**
   * name of the Employee 
   * @return name
  **/
  @ApiModelProperty(required = true, value = "name of the Employee ")
  @NotNull

 @Pattern(regexp="^[a-zA-Z ]$")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Bank acctNo(String acctNo) {
    this.acctNo = acctNo;
    return this;
  }

   /**
   * account number in the bank  
   * @return acctNo
  **/
  @ApiModelProperty(required = true, value = "account number in the bank  ")
  @NotNull

 @Pattern(regexp="^[0-9]+$")
  public String getAcctNo() {
    return acctNo;
  }

  public void setAcctNo(String acctNo) {
    this.acctNo = acctNo;
  }

  public Bank ifsc(String ifsc) {
    this.ifsc = ifsc;
    return this;
  }

   /**
   * ifsc of the bank 
   * @return ifsc
  **/
  @ApiModelProperty(required = true, value = "ifsc of the bank ")
  @NotNull

 @Pattern(regexp="^[a-zA-Z0-9]+$")
  public String getIfsc() {
    return ifsc;
  }

  public void setIfsc(String ifsc) {
    this.ifsc = ifsc;
  }

  public Bank micr(String micr) {
    this.micr = micr;
    return this;
  }

   /**
   * micr of the bank    
   * @return micr
  **/
  @ApiModelProperty(value = "micr of the bank    ")

 @Pattern(regexp="^[a-zA-Z0-9]+$")
  public String getMicr() {
    return micr;
  }

  public void setMicr(String micr) {
    this.micr = micr;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Bank bank = (Bank) o;
    return Objects.equals(this.tenantId, bank.tenantId) &&
        Objects.equals(this.name, bank.name) &&
        Objects.equals(this.acctNo, bank.acctNo) &&
        Objects.equals(this.ifsc, bank.ifsc) &&
        Objects.equals(this.micr, bank.micr);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tenantId, name, acctNo, ifsc, micr);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Bank {\n");
    
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    acctNo: ").append(toIndentedString(acctNo)).append("\n");
    sb.append("    ifsc: ").append(toIndentedString(ifsc)).append("\n");
    sb.append("    micr: ").append(toIndentedString(micr)).append("\n");
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

