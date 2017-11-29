package org.egov.works.estimate.web.contract;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * 
 */
@ApiModel(description = "")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-29T09:03:53.949Z")

public class BudgetGroup   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("majorCode")
  private ChartOfAccount majorCode = null;

  @JsonProperty("maxCode")
  private ChartOfAccount maxCode = null;

  @JsonProperty("minCode")
  private ChartOfAccount minCode = null;

  /**
   * account type of the BudgetGroup 
   */
  public enum AccountTypeEnum {
    REVENUE_RECEIPTS("REVENUE_RECEIPTS"),
    
    REVENUE_EXPENDITURE("REVENUE_EXPENDITURE"),
    
    CAPITAL_RECEIPTS("CAPITAL_RECEIPTS"),
    
    CAPITAL_EXPENDITURE("CAPITAL_EXPENDITURE");

    private String value;

    AccountTypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static AccountTypeEnum fromValue(String text) {
      for (AccountTypeEnum b : AccountTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("accountType")
  private AccountTypeEnum accountType = null;

  /**
   * budgeting type of the BudgetGroup 
   */
  public enum BudgetingTypeEnum {
    ALL("ALL"),
    
    DEBIT("DEBIT"),
    
    CREDIT("CREDIT");

    private String value;

    BudgetingTypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static BudgetingTypeEnum fromValue(String text) {
      for (BudgetingTypeEnum b : BudgetingTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("budgetingType")
  private BudgetingTypeEnum budgetingType = null;

  @JsonProperty("active")
  private Boolean active = null;

  @JsonProperty("auditDetails")
  private Auditable auditDetails = null;

  public BudgetGroup id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the BudgetGroup 
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the BudgetGroup ")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public BudgetGroup name(String name) {
    this.name = name;
    return this;
  }

   /**
   * name of the BudgetGroup 
   * @return name
  **/
  @ApiModelProperty(value = "name of the BudgetGroup ")

 @Size(min=1,max=250)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BudgetGroup description(String description) {
    this.description = description;
    return this;
  }

   /**
   * description of the BudgetGroup 
   * @return description
  **/
  @ApiModelProperty(value = "description of the BudgetGroup ")

 @Size(max=250)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public BudgetGroup majorCode(ChartOfAccount majorCode) {
    this.majorCode = majorCode;
    return this;
  }

   /**
   * major code of the BudgetGroup 
   * @return majorCode
  **/
  @ApiModelProperty(value = "major code of the BudgetGroup ")

  @Valid

  public ChartOfAccount getMajorCode() {
    return majorCode;
  }

  public void setMajorCode(ChartOfAccount majorCode) {
    this.majorCode = majorCode;
  }

  public BudgetGroup maxCode(ChartOfAccount maxCode) {
    this.maxCode = maxCode;
    return this;
  }

   /**
   * max code of the BudgetGroup 
   * @return maxCode
  **/
  @ApiModelProperty(value = "max code of the BudgetGroup ")

  @Valid

  public ChartOfAccount getMaxCode() {
    return maxCode;
  }

  public void setMaxCode(ChartOfAccount maxCode) {
    this.maxCode = maxCode;
  }

  public BudgetGroup minCode(ChartOfAccount minCode) {
    this.minCode = minCode;
    return this;
  }

   /**
   * min code of the BudgetGroup 
   * @return minCode
  **/
  @ApiModelProperty(value = "min code of the BudgetGroup ")

  @Valid

  public ChartOfAccount getMinCode() {
    return minCode;
  }

  public void setMinCode(ChartOfAccount minCode) {
    this.minCode = minCode;
  }

  public BudgetGroup accountType(AccountTypeEnum accountType) {
    this.accountType = accountType;
    return this;
  }

   /**
   * account type of the BudgetGroup 
   * @return accountType
  **/
  @ApiModelProperty(value = "account type of the BudgetGroup ")


  public AccountTypeEnum getAccountType() {
    return accountType;
  }

  public void setAccountType(AccountTypeEnum accountType) {
    this.accountType = accountType;
  }

  public BudgetGroup budgetingType(BudgetingTypeEnum budgetingType) {
    this.budgetingType = budgetingType;
    return this;
  }

   /**
   * budgeting type of the BudgetGroup 
   * @return budgetingType
  **/
  @ApiModelProperty(value = "budgeting type of the BudgetGroup ")


  public BudgetingTypeEnum getBudgetingType() {
    return budgetingType;
  }

  public void setBudgetingType(BudgetingTypeEnum budgetingType) {
    this.budgetingType = budgetingType;
  }

  public BudgetGroup active(Boolean active) {
    this.active = active;
    return this;
  }

   /**
   * Whether BudgetGroup is Active or not. If the value is TRUE, then BudgetGroup is active,If the value is FALSE then BudgetGroup is inactive,Default value is TRUE 
   * @return active
  **/
  @ApiModelProperty(value = "Whether BudgetGroup is Active or not. If the value is TRUE, then BudgetGroup is active,If the value is FALSE then BudgetGroup is inactive,Default value is TRUE ")


  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public BudgetGroup auditDetails(Auditable auditDetails) {
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
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BudgetGroup budgetGroup = (BudgetGroup) o;
    return Objects.equals(this.id, budgetGroup.id) &&
        Objects.equals(this.name, budgetGroup.name) &&
        Objects.equals(this.description, budgetGroup.description) &&
        Objects.equals(this.majorCode, budgetGroup.majorCode) &&
        Objects.equals(this.maxCode, budgetGroup.maxCode) &&
        Objects.equals(this.minCode, budgetGroup.minCode) &&
        Objects.equals(this.accountType, budgetGroup.accountType) &&
        Objects.equals(this.budgetingType, budgetGroup.budgetingType) &&
        Objects.equals(this.active, budgetGroup.active) &&
        Objects.equals(this.auditDetails, budgetGroup.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, majorCode, maxCode, minCode, accountType, budgetingType, active, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BudgetGroup {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    majorCode: ").append(toIndentedString(majorCode)).append("\n");
    sb.append("    maxCode: ").append(toIndentedString(maxCode)).append("\n");
    sb.append("    minCode: ").append(toIndentedString(minCode)).append("\n");
    sb.append("    accountType: ").append(toIndentedString(accountType)).append("\n");
    sb.append("    budgetingType: ").append(toIndentedString(budgetingType)).append("\n");
    sb.append("    active: ").append(toIndentedString(active)).append("\n");
    sb.append("    auditDetails: ").append(toIndentedString(auditDetails)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

