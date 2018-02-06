package org.egov.works.workorder.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//import org.joda.time.LocalDate;

/**
 * BillRegister
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-01-16T09:56:01.690Z")

public class BillRegister {
  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("billType")
  private String billType = null;

  @JsonProperty("billSubType")
  private String billSubType = null;

  @JsonProperty("billNumber")
  private String billNumber = null;
// Commenting becouse in egf billDate is of type long
//  @JsonProperty("billDate")
//  private LocalDate billDate = null;
  
  private Long billDate = null;

  @JsonProperty("billAmount")
  private Double billAmount = null;

  @JsonProperty("passedAmount")
  private Double passedAmount = null;

  @JsonProperty("moduleName")
  private String moduleName = null;

  @JsonProperty("status")
  private BillStatus status = null;

  @JsonProperty("fund")
  private Fund fund = null;

  @JsonProperty("function")
  private Function function = null;

  @JsonProperty("fundsource")
  private Fundsource fundsource = null;

  @JsonProperty("scheme")
  private Scheme scheme = null;

  @JsonProperty("subScheme")
  private SubScheme subScheme = null;

  @JsonProperty("functionary")
  private Functionary functionary = null;

  @JsonProperty("division")
  private Boundary division = null;

  @JsonProperty("department")
  private Department department = null;

  @JsonProperty("sourcePath")
  private String sourcePath = null;

  @JsonProperty("budgetCheckRequired")
  private Boolean budgetCheckRequired = null;

  @JsonProperty("budgetAppropriationNo")
  private String budgetAppropriationNo = null;

  @JsonProperty("partyBillNumber")
  private String partyBillNumber = null;

  @JsonProperty("partyBillDate")
  private Long partyBillDate = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("billDetails")
  private List<BillDetail> billDetails = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public BillRegister tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * tenantId Unique Identifier of the tenant, Like AP, AP.Kurnool etc. represents the client for which the transaction is created. 
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "tenantId Unique Identifier of the tenant, Like AP, AP.Kurnool etc. represents the client for which the transaction is created. ")
  @NotNull

 @Size(min=0,max=256)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public BillRegister billType(String billType) {
    this.billType = billType;
    return this;
  }

   /**
   * billType is the type of the bill example is ExpenseBill,ContractorBill,PurchaseBill,SalaryBill etc 
   * @return billType
  **/
  @ApiModelProperty(required = true, value = "billType is the type of the bill example is ExpenseBill,ContractorBill,PurchaseBill,SalaryBill etc ")
  @NotNull

 @Size(max=50)
  public String getBillType() {
    return billType;
  }

  public void setBillType(String billType) {
    this.billType = billType;
  }

  public BillRegister billSubType(String billSubType) {
    this.billSubType = billSubType;
    return this;
  }

   /**
   * billSubType refers with each type of bill what is the subtype .  for example ContractorBill will have subType as \"FinalBill\" 
   * @return billSubType
  **/
  @ApiModelProperty(value = "billSubType refers with each type of bill what is the subtype .  for example ContractorBill will have subType as \"FinalBill\" ")

 @Size(max=50)
  public String getBillSubType() {
    return billSubType;
  }

  public void setBillSubType(String billSubType) {
    this.billSubType = billSubType;
  }

  public BillRegister billNumber(String billNumber) {
    this.billNumber = billNumber;
    return this;
  }

   /**
   * billNumber refers to the unique number generated for the bill. 
   * @return billNumber
  **/
  @ApiModelProperty(value = "billNumber refers to the unique number generated for the bill. ")

 @Size(max=50)
  public String getBillNumber() {
    return billNumber;
  }

  public void setBillNumber(String billNumber) {
    this.billNumber = billNumber;
  }

  public BillRegister billDate(Long billDate) {
    this.billDate = billDate;
    return this;
  }

   /**
   * billDate is the date when the bill is created. 
   * @return billDate
  **/
  @ApiModelProperty(required = true, value = "billDate is the date when the bill is created. ")
  @NotNull

  @Valid

  public Long getBillDate() {
    return billDate;
  }

  public void setBillDate(Long billDate) {
    this.billDate = billDate;
  }

  public BillRegister billAmount(Double billAmount) {
    this.billAmount = billAmount;
    return this;
  }

   /**
   * billAmount is the total bill Amount . even though the bill is created for billAmount of x it may be passed for amount x-k 
   * @return billAmount
  **/
  @ApiModelProperty(required = true, value = "billAmount is the total bill Amount . even though the bill is created for billAmount of x it may be passed for amount x-k ")
  @NotNull


  public Double getBillAmount() {
    return billAmount;
  }

  public void setBillAmount(Double billAmount) {
    this.billAmount = billAmount;
  }

  public BillRegister passedAmount(Double passedAmount) {
    this.passedAmount = passedAmount;
    return this;
  }

   /**
   * passedAmount refers to the amount passed by ulb . even though the bill is created for billAmount of x it may be passed for amount x-k 
   * @return passedAmount
  **/
  @ApiModelProperty(value = "passedAmount refers to the amount passed by ulb . even though the bill is created for billAmount of x it may be passed for amount x-k ")


  public Double getPassedAmount() {
    return passedAmount;
  }

  public void setPassedAmount(Double passedAmount) {
    this.passedAmount = passedAmount;
  }

  public BillRegister moduleName(String moduleName) {
    this.moduleName = moduleName;
    return this;
  }

   /**
   * moduleName is the name of the module who is posting the bill in financials 
   * @return moduleName
  **/
  @ApiModelProperty(value = "moduleName is the name of the module who is posting the bill in financials ")

 @Size(max=50)
  public String getModuleName() {
    return moduleName;
  }

  public void setModuleName(String moduleName) {
    this.moduleName = moduleName;
  }

  public BillRegister status(BillStatus status) {
    this.status = status;
    return this;
  }

   /**
   * status refers to the status of the bill like ,created,approved etc 
   * @return status
  **/
  @ApiModelProperty(value = "status refers to the status of the bill like ,created,approved etc ")

  @Valid

  public BillStatus getStatus() {
    return status;
  }

  public void setStatus(BillStatus status) {
    this.status = status;
  }

  public BillRegister fund(Fund fund) {
    this.fund = fund;
    return this;
  }

   /**
   * fund refers to the fund master 
   * @return fund
  **/
  @ApiModelProperty(value = "fund refers to the fund master ")

  @Valid

  public Fund getFund() {
    return fund;
  }

  public void setFund(Fund fund) {
    this.fund = fund;
  }

  public BillRegister function(Function function) {
    this.function = function;
    return this;
  }

   /**
   * function refers to the function master 
   * @return function
  **/
  @ApiModelProperty(value = "function refers to the function master ")

  @Valid

  public Function getFunction() {
    return function;
  }

  public void setFunction(Function function) {
    this.function = function;
  }

  public BillRegister fundsource(Fundsource fundsource) {
    this.fundsource = fundsource;
    return this;
  }

   /**
   * fundsource of the BillRegister 
   * @return fundsource
  **/
  @ApiModelProperty(value = "fundsource of the BillRegister ")

  @Valid

  public Fundsource getFundsource() {
    return fundsource;
  }

  public void setFundsource(Fundsource fundsource) {
    this.fundsource = fundsource;
  }

  public BillRegister scheme(Scheme scheme) {
    this.scheme = scheme;
    return this;
  }

   /**
   * scheme of the BillRegister 
   * @return scheme
  **/
  @ApiModelProperty(value = "scheme of the BillRegister ")

  @Valid

  public Scheme getScheme() {
    return scheme;
  }

  public void setScheme(Scheme scheme) {
    this.scheme = scheme;
  }

  public BillRegister subScheme(SubScheme subScheme) {
    this.subScheme = subScheme;
    return this;
  }

   /**
   * sub scheme of the BillRegister 
   * @return subScheme
  **/
  @ApiModelProperty(value = "sub scheme of the BillRegister ")

  @Valid

  public SubScheme getSubScheme() {
    return subScheme;
  }

  public void setSubScheme(SubScheme subScheme) {
    this.subScheme = subScheme;
  }

  public BillRegister functionary(Functionary functionary) {
    this.functionary = functionary;
    return this;
  }

   /**
   * functionary of the BillRegister 
   * @return functionary
  **/
  @ApiModelProperty(value = "functionary of the BillRegister ")

  @Valid

  public Functionary getFunctionary() {
    return functionary;
  }

  public void setFunctionary(Functionary functionary) {
    this.functionary = functionary;
  }

  public BillRegister division(Boundary division) {
    this.division = division;
    return this;
  }

   /**
   * division of the BillRegister 
   * @return division
  **/
  @ApiModelProperty(value = "division of the BillRegister ")

  @Valid

  public Boundary getDivision() {
    return division;
  }

  public void setDivision(Boundary division) {
    this.division = division;
  }

  public BillRegister department(Department department) {
    this.department = department;
    return this;
  }

   /**
   * department of the BillRegister 
   * @return department
  **/
  @ApiModelProperty(value = "department of the BillRegister ")

  @Valid

  public Department getDepartment() {
    return department;
  }

  public void setDepartment(Department department) {
    this.department = department;
  }

  public BillRegister sourcePath(String sourcePath) {
    this.sourcePath = sourcePath;
    return this;
  }

   /**
   * source path of the BillRegister 
   * @return sourcePath
  **/
  @ApiModelProperty(value = "source path of the BillRegister ")

 @Size(max=256)
  public String getSourcePath() {
    return sourcePath;
  }

  public void setSourcePath(String sourcePath) {
    this.sourcePath = sourcePath;
  }

  public BillRegister budgetCheckRequired(Boolean budgetCheckRequired) {
    this.budgetCheckRequired = budgetCheckRequired;
    return this;
  }

   /**
   * budgetCheckRequired is a boolean field is the budget check is required or not default is true 
   * @return budgetCheckRequired
  **/
  @ApiModelProperty(value = "budgetCheckRequired is a boolean field is the budget check is required or not default is true ")


  public Boolean getBudgetCheckRequired() {
    return budgetCheckRequired;
  }

  public void setBudgetCheckRequired(Boolean budgetCheckRequired) {
    this.budgetCheckRequired = budgetCheckRequired;
  }

  public BillRegister budgetAppropriationNo(String budgetAppropriationNo) {
    this.budgetAppropriationNo = budgetAppropriationNo;
    return this;
  }

   /**
   * budgetAppropriationNo is the number generated after budget check. This field will be null if the budget check not done. 
   * @return budgetAppropriationNo
  **/
  @ApiModelProperty(value = "budgetAppropriationNo is the number generated after budget check. This field will be null if the budget check not done. ")

 @Size(max=50)
  public String getBudgetAppropriationNo() {
    return budgetAppropriationNo;
  }

  public void setBudgetAppropriationNo(String budgetAppropriationNo) {
    this.budgetAppropriationNo = budgetAppropriationNo;
  }

  public BillRegister partyBillNumber(String partyBillNumber) {
    this.partyBillNumber = partyBillNumber;
    return this;
  }

   /**
   * partyBillNumber is the manual bill number . 
   * @return partyBillNumber
  **/
  @ApiModelProperty(value = "partyBillNumber is the manual bill number . ")

 @Size(max=50)
  public String getPartyBillNumber() {
    return partyBillNumber;
  }

  public void setPartyBillNumber(String partyBillNumber) {
    this.partyBillNumber = partyBillNumber;
  }

  public BillRegister partyBillDate(Long partyBillDate) {
    this.partyBillDate = partyBillDate;
    return this;
  }

   /**
   * partyBillDate is the manual bill date . 
   * @return partyBillDate
  **/
  @ApiModelProperty(value = "partyBillDate is the manual bill date . ")

  @Valid

  public Long getPartyBillDate() {
    return partyBillDate;
  }

  public void setPartyBillDate(Long partyBillDate) {
    this.partyBillDate = partyBillDate;
  }

  public BillRegister description(String description) {
    this.description = description;
    return this;
  }

   /**
   * description is the more detailed information about the bill 
   * @return description
  **/
  @ApiModelProperty(value = "description is the more detailed information about the bill ")

 @Size(max=256)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public BillRegister billDetails(List<BillDetail> billDetails) {
    this.billDetails = billDetails;
    return this;
  }

  public BillRegister addBillDetailsItem(BillDetail billDetailsItem) {
    if (this.billDetails == null) {
      this.billDetails = new ArrayList<BillDetail>();
    }
    this.billDetails.add(billDetailsItem);
    return this;
  }

   /**
   * bill details of the BillRegister 
   * @return billDetails
  **/
  @ApiModelProperty(value = "bill details of the BillRegister ")

  @Valid

  public List<BillDetail> getBillDetails() {
    return billDetails;
  }

  public void setBillDetails(List<BillDetail> billDetails) {
    this.billDetails = billDetails;
  }

  public BillRegister auditDetails(AuditDetails auditDetails) {
    this.auditDetails = auditDetails;
    return this;
  }

   /**
   * Get auditDetails
   * @return auditDetails
  **/
  @ApiModelProperty(value = "")

  @Valid

  public AuditDetails getAuditDetails() {
    return auditDetails;
  }

  public void setAuditDetails(AuditDetails auditDetails) {
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
    BillRegister billRegister = (BillRegister) o;
    return Objects.equals(this.tenantId, billRegister.tenantId) &&
        Objects.equals(this.billType, billRegister.billType) &&
        Objects.equals(this.billSubType, billRegister.billSubType) &&
        Objects.equals(this.billNumber, billRegister.billNumber) &&
        Objects.equals(this.billDate, billRegister.billDate) &&
        Objects.equals(this.billAmount, billRegister.billAmount) &&
        Objects.equals(this.passedAmount, billRegister.passedAmount) &&
        Objects.equals(this.moduleName, billRegister.moduleName) &&
        Objects.equals(this.status, billRegister.status) &&
        Objects.equals(this.fund, billRegister.fund) &&
        Objects.equals(this.function, billRegister.function) &&
        Objects.equals(this.fundsource, billRegister.fundsource) &&
        Objects.equals(this.scheme, billRegister.scheme) &&
        Objects.equals(this.subScheme, billRegister.subScheme) &&
        Objects.equals(this.functionary, billRegister.functionary) &&
        Objects.equals(this.division, billRegister.division) &&
        Objects.equals(this.department, billRegister.department) &&
        Objects.equals(this.sourcePath, billRegister.sourcePath) &&
        Objects.equals(this.budgetCheckRequired, billRegister.budgetCheckRequired) &&
        Objects.equals(this.budgetAppropriationNo, billRegister.budgetAppropriationNo) &&
        Objects.equals(this.partyBillNumber, billRegister.partyBillNumber) &&
        Objects.equals(this.partyBillDate, billRegister.partyBillDate) &&
        Objects.equals(this.description, billRegister.description) &&
        Objects.equals(this.billDetails, billRegister.billDetails) &&
        Objects.equals(this.auditDetails, billRegister.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tenantId, billType, billSubType, billNumber, billDate, billAmount, passedAmount, moduleName, status, fund, function, fundsource, scheme, subScheme, functionary, division, department, sourcePath, budgetCheckRequired, budgetAppropriationNo, partyBillNumber, partyBillDate, description, billDetails, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BillRegister {\n");

    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    billType: ").append(toIndentedString(billType)).append("\n");
    sb.append("    billSubType: ").append(toIndentedString(billSubType)).append("\n");
    sb.append("    billNumber: ").append(toIndentedString(billNumber)).append("\n");
    sb.append("    billDate: ").append(toIndentedString(billDate)).append("\n");
    sb.append("    billAmount: ").append(toIndentedString(billAmount)).append("\n");
    sb.append("    passedAmount: ").append(toIndentedString(passedAmount)).append("\n");
    sb.append("    moduleName: ").append(toIndentedString(moduleName)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    fund: ").append(toIndentedString(fund)).append("\n");
    sb.append("    function: ").append(toIndentedString(function)).append("\n");
    sb.append("    fundsource: ").append(toIndentedString(fundsource)).append("\n");
    sb.append("    scheme: ").append(toIndentedString(scheme)).append("\n");
    sb.append("    subScheme: ").append(toIndentedString(subScheme)).append("\n");
    sb.append("    functionary: ").append(toIndentedString(functionary)).append("\n");
    sb.append("    division: ").append(toIndentedString(division)).append("\n");
    sb.append("    department: ").append(toIndentedString(department)).append("\n");
    sb.append("    sourcePath: ").append(toIndentedString(sourcePath)).append("\n");
    sb.append("    budgetCheckRequired: ").append(toIndentedString(budgetCheckRequired)).append("\n");
    sb.append("    budgetAppropriationNo: ").append(toIndentedString(budgetAppropriationNo)).append("\n");
    sb.append("    partyBillNumber: ").append(toIndentedString(partyBillNumber)).append("\n");
    sb.append("    partyBillDate: ").append(toIndentedString(partyBillDate)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    billDetails: ").append(toIndentedString(billDetails)).append("\n");
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

