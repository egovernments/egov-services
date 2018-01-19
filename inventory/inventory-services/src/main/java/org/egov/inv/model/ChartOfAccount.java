package org.egov.inv.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 *
 */
@ApiModel(description = "")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-12-28T09:20:06.607Z")

public class ChartOfAccount {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("glcode")
    private String glcode = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("accountCodePurpose")
    private AccountCodePurpose accountCodePurpose = null;

    @JsonProperty("description")
    private String description = null;

    @JsonProperty("isActiveForPosting")
    private Boolean isActiveForPosting = null;

    @JsonProperty("parentId")
    private ChartOfAccount parentId = null;

    @JsonProperty("type")
    private String type = null;

    @JsonProperty("classification")
    private Long classification = null;

    @JsonProperty("functionRequired")
    private Boolean functionRequired = null;

    @JsonProperty("budgetCheckRequired")
    private Boolean budgetCheckRequired = null;

    @JsonProperty("majorCode")
    private String majorCode = null;

    @JsonProperty("isSubLedger")
    private Boolean isSubLedger = null;

    @JsonProperty("auditDetails")
    private Auditable auditDetails = null;

    public ChartOfAccount id(String id) {
        this.id = id;
        return this;
    }

    /**
     * id is the Unique Identifier . This data is generated internally
     *
     * @return id
     **/
    @ApiModelProperty(value = "id is the Unique Identifier . This data is generated internally ")


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ChartOfAccount glcode(String glcode) {
        this.glcode = glcode;
        return this;
    }

    /**
     * glcode is the Account Code or Account Head in Accounting terms.It may be Major head,Minor head or Detailed head.It is numeric examples are 1,110,1101,1101001,2,210,21000,210010
     *
     * @return glcode
     **/
    @ApiModelProperty(required = true, value = "glcode is the Account Code or Account Head in Accounting terms.It may be Major head,Minor head or Detailed head.It is numeric examples are 1,110,1101,1101001,2,210,21000,210010 ")
    @NotNull

    @Size(min = 1, max = 16)
    public String getGlcode() {
        return glcode;
    }

    public void setGlcode(String glcode) {
        this.glcode = glcode;
    }

    public ChartOfAccount name(String name) {
        this.name = name;
        return this;
    }

    /**
     * name is the name of the account code . for example 110 glcode has the name \"Tax Revenue\"
     *
     * @return name
     **/
    @ApiModelProperty(required = true, value = "name is the name of the account code . for example 110 glcode has the name \"Tax Revenue\" ")
    @NotNull

    @Size(min = 5, max = 128)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ChartOfAccount accountCodePurpose(AccountCodePurpose accountCodePurpose) {
        this.accountCodePurpose = accountCodePurpose;
        return this;
    }

    /**
     * accountCodePurpose is the mapped AccountCodePurpose . This mapping can happen at any level say Major,minor or detailed. When Account code is searched 1. If mapped at major code then it lists major and all other codes under that major code 2. If mapped at minor code then it list minor code and all other codes under that minor code 3. If mapped at detailed code then it lists only that code
     *
     * @return accountCodePurpose
     **/
    @ApiModelProperty(value = "accountCodePurpose is the mapped AccountCodePurpose . This mapping can happen at any level say Major,minor or detailed. When Account code is searched 1. If mapped at major code then it lists major and all other codes under that major code 2. If mapped at minor code then it list minor code and all other codes under that minor code 3. If mapped at detailed code then it lists only that code ")

    @Valid

    public AccountCodePurpose getAccountCodePurpose() {
        return accountCodePurpose;
    }

    public void setAccountCodePurpose(AccountCodePurpose accountCodePurpose) {
        this.accountCodePurpose = accountCodePurpose;
    }

    public ChartOfAccount description(String description) {
        this.description = description;
        return this;
    }

    /**
     * description is the more detailed description about the account code
     *
     * @return description
     **/
    @ApiModelProperty(value = "description is the more detailed description about the account code ")

    @Size(max = 256)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ChartOfAccount isActiveForPosting(Boolean isActiveForPosting) {
        this.isActiveForPosting = isActiveForPosting;
        return this;
    }

    /**
     * isActiveForPosting true will be considered for transactions. All major,minor codes will be false and only detailed code will be true . Further any account code can be disabled for transaction by making this field false
     *
     * @return isActiveForPosting
     **/
    @ApiModelProperty(required = true, value = "isActiveForPosting true will be considered for transactions. All major,minor codes will be false and only detailed code will be true . Further any account code can be disabled for transaction by making this field false ")
    @NotNull


    public Boolean getIsActiveForPosting() {
        return isActiveForPosting;
    }

    public void setIsActiveForPosting(Boolean isActiveForPosting) {
        this.isActiveForPosting = isActiveForPosting;
    }

    public ChartOfAccount parentId(ChartOfAccount parentId) {
        this.parentId = parentId;
        return this;
    }

    /**
     * parentId is the id of other account code in the chart of account .Chart of account is created in tree structure. Any code can have other code as parent . All minor code will have manjor code as parent . All detailed code will have minor code as parent only leaf account code that is which is not parent for any account code will used for transactions.
     *
     * @return parentId
     **/
    @ApiModelProperty(value = "parentId is the id of other account code in the chart of account .Chart of account is created in tree structure. Any code can have other code as parent . All minor code will have manjor code as parent . All detailed code will have minor code as parent only leaf account code that is which is not parent for any account code will used for transactions. ")


    public ChartOfAccount getParentId() {
        return parentId;
    }

    public void setParentId(ChartOfAccount parentId) {
        this.parentId = parentId;
    }

    public ChartOfAccount type(String type) {
        this.type = type;
        return this;
    }

    /**
     * type is a single character representation of account code type I: Income E: Expenditure L: Liability A: Asset Account code for all I start with 1 Account code for all E start with 2 Account code for all L start with 3 Account code for all A start with 4
     *
     * @return type
     **/
    @ApiModelProperty(required = true, value = "type is a single character representation of account code type I: Income E: Expenditure L: Liability A: Asset Account code for all I start with 1 Account code for all E start with 2 Account code for all L start with 3 Account code for all A start with 4 ")
    @NotNull


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ChartOfAccount classification(Long classification) {
        this.classification = classification;
        return this;
    }

    /**
     * classification is internal to the system. This tells whether the code is Major ,Minor,Subminor or detailed. Major code classification value is 1 Minor code is 2 Subminor code is 3 Detailed code is 4. Only classification 4 and is activeforposting=true will be used in transactions. Reports can be generated at any level.
     *
     * @return classification
     **/
    @ApiModelProperty(required = true, value = "classification is internal to the system. This tells whether the code is Major ,Minor,Subminor or detailed. Major code classification value is 1 Minor code is 2 Subminor code is 3 Detailed code is 4. Only classification 4 and is activeforposting=true will be used in transactions. Reports can be generated at any level. ")
    @NotNull


    public Long getClassification() {
        return classification;
    }

    public void setClassification(Long classification) {
        this.classification = classification;
    }

    public ChartOfAccount functionRequired(Boolean functionRequired) {
        this.functionRequired = functionRequired;
        return this;
    }

    /**
     * functionRequired field specifies while transacting with this accountcode is the function is mandatory or not . For any account code this field is set to true then all transactions expect a fun=ction code to be passed along with account code
     *
     * @return functionRequired
     **/
    @ApiModelProperty(required = true, value = "functionRequired field specifies while transacting with this accountcode is the function is mandatory or not . For any account code this field is set to true then all transactions expect a fun=ction code to be passed along with account code ")
    @NotNull


    public Boolean getFunctionRequired() {
        return functionRequired;
    }

    public void setFunctionRequired(Boolean functionRequired) {
        this.functionRequired = functionRequired;
    }

    public ChartOfAccount budgetCheckRequired(Boolean budgetCheckRequired) {
        this.budgetCheckRequired = budgetCheckRequired;
        return this;
    }

    /**
     * budgetCheckRequired field specifies whether budgeting check required for this account code. Apart from global Budgetcheck configuration this is where glcode wise budget check decision is made.
     *
     * @return budgetCheckRequired
     **/
    @ApiModelProperty(required = true, value = "budgetCheckRequired field specifies whether budgeting check required for this account code. Apart from global Budgetcheck configuration this is where glcode wise budget check decision is made. ")
    @NotNull


    public Boolean getBudgetCheckRequired() {
        return budgetCheckRequired;
    }

    public void setBudgetCheckRequired(Boolean budgetCheckRequired) {
        this.budgetCheckRequired = budgetCheckRequired;
    }

    public ChartOfAccount majorCode(String majorCode) {
        this.majorCode = majorCode;
        return this;
    }

    /**
     * major code of the ChartOfAccount
     *
     * @return majorCode
     **/
    @ApiModelProperty(value = "major code of the ChartOfAccount ")

    @Size(max = 16)
    public String getMajorCode() {
        return majorCode;
    }

    public void setMajorCode(String majorCode) {
        this.majorCode = majorCode;
    }

    public ChartOfAccount isSubLedger(Boolean isSubLedger) {
        this.isSubLedger = isSubLedger;
        return this;
    }

    /**
     * is sub ledger of the ChartOfAccount
     *
     * @return isSubLedger
     **/
    @ApiModelProperty(value = "is sub ledger of the ChartOfAccount ")


    public Boolean getIsSubLedger() {
        return isSubLedger;
    }

    public void setIsSubLedger(Boolean isSubLedger) {
        this.isSubLedger = isSubLedger;
    }

    public ChartOfAccount auditDetails(Auditable auditDetails) {
        this.auditDetails = auditDetails;
        return this;
    }

    /**
     * Get auditDetails
     *
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
        ChartOfAccount chartOfAccount = (ChartOfAccount) o;
        return Objects.equals(this.id, chartOfAccount.id) &&
                Objects.equals(this.glcode, chartOfAccount.glcode) &&
                Objects.equals(this.name, chartOfAccount.name) &&
                Objects.equals(this.accountCodePurpose, chartOfAccount.accountCodePurpose) &&
                Objects.equals(this.description, chartOfAccount.description) &&
                Objects.equals(this.isActiveForPosting, chartOfAccount.isActiveForPosting) &&
                Objects.equals(this.parentId, chartOfAccount.parentId) &&
                Objects.equals(this.type, chartOfAccount.type) &&
                Objects.equals(this.classification, chartOfAccount.classification) &&
                Objects.equals(this.functionRequired, chartOfAccount.functionRequired) &&
                Objects.equals(this.budgetCheckRequired, chartOfAccount.budgetCheckRequired) &&
                Objects.equals(this.majorCode, chartOfAccount.majorCode) &&
                Objects.equals(this.isSubLedger, chartOfAccount.isSubLedger) &&
                Objects.equals(this.auditDetails, chartOfAccount.auditDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, glcode, name, accountCodePurpose, description, isActiveForPosting, parentId, type, classification, functionRequired, budgetCheckRequired, majorCode, isSubLedger, auditDetails);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ChartOfAccount {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    glcode: ").append(toIndentedString(glcode)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    accountCodePurpose: ").append(toIndentedString(accountCodePurpose)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    isActiveForPosting: ").append(toIndentedString(isActiveForPosting)).append("\n");
        sb.append("    parentId: ").append(toIndentedString(parentId)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    classification: ").append(toIndentedString(classification)).append("\n");
        sb.append("    functionRequired: ").append(toIndentedString(functionRequired)).append("\n");
        sb.append("    budgetCheckRequired: ").append(toIndentedString(budgetCheckRequired)).append("\n");
        sb.append("    majorCode: ").append(toIndentedString(majorCode)).append("\n");
        sb.append("    isSubLedger: ").append(toIndentedString(isSubLedger)).append("\n");
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

