package org.egov.works.measurementbook.web.contract;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 */
@ApiModel(description = "")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-12-20T10:00:39.005Z")

public class AccountDetailKey   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("key")
  private String key = null;

  @JsonProperty("accountDetailType")
  private AccountDetailType accountDetailType = null;

  @JsonProperty("auditDetails")
  private Auditable auditDetails = null;

  public AccountDetailKey id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the AccountDetailKey 
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the AccountDetailKey ")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public AccountDetailKey key(String key) {
    this.key = key;
    return this;
  }

   /**
   * key of the AccountDetailKey 
   * @return key
  **/
  @ApiModelProperty(required = true, value = "key of the AccountDetailKey ")
  @NotNull


  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public AccountDetailKey accountDetailType(AccountDetailType accountDetailType) {
    this.accountDetailType = accountDetailType;
    return this;
  }

   /**
   * account detail type of the AccountDetailKey 
   * @return accountDetailType
  **/
  @ApiModelProperty(required = true, value = "account detail type of the AccountDetailKey ")
  @NotNull

  @Valid

  public AccountDetailType getAccountDetailType() {
    return accountDetailType;
  }

  public void setAccountDetailType(AccountDetailType accountDetailType) {
    this.accountDetailType = accountDetailType;
  }

  public AccountDetailKey auditDetails(Auditable auditDetails) {
    this.auditDetails = auditDetails;
    return this;
  }

   /**
   * Get auditDetails
   * @return auditDetails
  **/
  @ApiModelProperty(value = "")

  @Valid

  public Auditable getAuditDetails() {
    return auditDetails;
  }

  public void setAuditDetails(Auditable auditDetails) {
    this.auditDetails = auditDetails;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AccountDetailKey accountDetailKey = (AccountDetailKey) o;
    return Objects.equals(this.id, accountDetailKey.id) &&
        Objects.equals(this.key, accountDetailKey.key) &&
        Objects.equals(this.accountDetailType, accountDetailKey.accountDetailType) &&
        Objects.equals(this.auditDetails, accountDetailKey.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, key, accountDetailType, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AccountDetailKey {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    key: ").append(toIndentedString(key)).append("\n");
    sb.append("    accountDetailType: ").append(toIndentedString(accountDetailType)).append("\n");
    sb.append("    auditDetails: ").append(toIndentedString(auditDetails)).append("\n");
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

