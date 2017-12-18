package org.egov.works.qualitycontrol.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * An Object that holds the basic data for Quality Testing
 */
@ApiModel(description = "An Object that holds the basic data for Quality Testing")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-12-18T07:23:14.953Z")

public class QualityTesting   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("letterOfAcceptanceEstimate")
  private LetterOfAcceptanceEstimate letterOfAcceptanceEstimate = null;

  @JsonProperty("qualityTestingDetails")
  private List<QualityTestingDetail> qualityTestingDetails = null;

  @JsonProperty("remarks")
  private String remarks = null;

  @JsonProperty("status")
  private String status = null;

  @JsonProperty("deleted")
  private Boolean deleted = false;

  @JsonProperty("testReports")
  private List<DocumentDetail> testReports = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public QualityTesting id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Quality Testing
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Quality Testing")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public QualityTesting tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Quality Testing
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "Tenant id of the Quality Testing")
  @NotNull

 @Size(min=2,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public QualityTesting letterOfAcceptanceEstimate(LetterOfAcceptanceEstimate letterOfAcceptanceEstimate) {
    this.letterOfAcceptanceEstimate = letterOfAcceptanceEstimate;
    return this;
  }

   /**
   * Reference of Letter of Acceptance Estimate
   * @return letterOfAcceptanceEstimate
  **/
  @ApiModelProperty(value = "Reference of Letter of Acceptance Estimate")

  @Valid

  public LetterOfAcceptanceEstimate getLetterOfAcceptanceEstimate() {
    return letterOfAcceptanceEstimate;
  }

  public void setLetterOfAcceptanceEstimate(LetterOfAcceptanceEstimate letterOfAcceptanceEstimate) {
    this.letterOfAcceptanceEstimate = letterOfAcceptanceEstimate;
  }

  public QualityTesting qualityTestingDetails(List<QualityTestingDetail> qualityTestingDetails) {
    this.qualityTestingDetails = qualityTestingDetails;
    return this;
  }

  public QualityTesting addQualityTestingDetailsItem(QualityTestingDetail qualityTestingDetailsItem) {
    if (this.qualityTestingDetails == null) {
      this.qualityTestingDetails = new ArrayList<QualityTestingDetail>();
    }
    this.qualityTestingDetails.add(qualityTestingDetailsItem);
    return this;
  }

   /**
   * Array of Quality Testing Detail
   * @return qualityTestingDetails
  **/
  @ApiModelProperty(value = "Array of Quality Testing Detail")

  @Valid
 @Size(min=1)
  public List<QualityTestingDetail> getQualityTestingDetails() {
    return qualityTestingDetails;
  }

  public void setQualityTestingDetails(List<QualityTestingDetail> qualityTestingDetails) {
    this.qualityTestingDetails = qualityTestingDetails;
  }

  public QualityTesting remarks(String remarks) {
    this.remarks = remarks;
    return this;
  }

   /**
   * Remarks for the Quality Testing
   * @return remarks
  **/
  @ApiModelProperty(required = true, value = "Remarks for the Quality Testing")
  @NotNull

 @Pattern(regexp="[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+") @Size(max=1024)
  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public QualityTesting status(String status) {
    this.status = status;
    return this;
  }

   /**
   * Status for quality testing.
   * @return status
  **/
  @ApiModelProperty(required = true, value = "Status for quality testing.")
  @NotNull


  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public QualityTesting deleted(Boolean deleted) {
    this.deleted = deleted;
    return this;
  }

   /**
   * Boolean value to identify whether the object is deleted or not from UI.
   * @return deleted
  **/
  @ApiModelProperty(value = "Boolean value to identify whether the object is deleted or not from UI.")


  public Boolean getDeleted() {
    return deleted;
  }

  public void setDeleted(Boolean deleted) {
    this.deleted = deleted;
  }

  public QualityTesting testReports(List<DocumentDetail> testReports) {
    this.testReports = testReports;
    return this;
  }

  public QualityTesting addTestReportsItem(DocumentDetail testReportsItem) {
    if (this.testReports == null) {
      this.testReports = new ArrayList<DocumentDetail>();
    }
    this.testReports.add(testReportsItem);
    return this;
  }

   /**
   * Array of test reports in PDF format
   * @return testReports
  **/
  @ApiModelProperty(value = "Array of test reports in PDF format")

  @Valid

  public List<DocumentDetail> getTestReports() {
    return testReports;
  }

  public void setTestReports(List<DocumentDetail> testReports) {
    this.testReports = testReports;
  }

  public QualityTesting auditDetails(AuditDetails auditDetails) {
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
    QualityTesting qualityTesting = (QualityTesting) o;
    return Objects.equals(this.id, qualityTesting.id) &&
        Objects.equals(this.tenantId, qualityTesting.tenantId) &&
        Objects.equals(this.letterOfAcceptanceEstimate, qualityTesting.letterOfAcceptanceEstimate) &&
        Objects.equals(this.qualityTestingDetails, qualityTesting.qualityTestingDetails) &&
        Objects.equals(this.remarks, qualityTesting.remarks) &&
        Objects.equals(this.status, qualityTesting.status) &&
        Objects.equals(this.deleted, qualityTesting.deleted) &&
        Objects.equals(this.testReports, qualityTesting.testReports) &&
        Objects.equals(this.auditDetails, qualityTesting.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, letterOfAcceptanceEstimate, qualityTestingDetails, remarks, status, deleted, testReports, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QualityTesting {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    letterOfAcceptanceEstimate: ").append(toIndentedString(letterOfAcceptanceEstimate)).append("\n");
    sb.append("    qualityTestingDetails: ").append(toIndentedString(qualityTestingDetails)).append("\n");
    sb.append("    remarks: ").append(toIndentedString(remarks)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    deleted: ").append(toIndentedString(deleted)).append("\n");
    sb.append("    testReports: ").append(toIndentedString(testReports)).append("\n");
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

