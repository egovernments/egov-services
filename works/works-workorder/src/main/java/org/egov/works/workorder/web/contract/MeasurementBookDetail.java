package org.egov.works.workorder.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * An Object that hold details for the given Measurement book. At least one data should be given to create an Measurement book
 */
@ApiModel(description = "An Object that hold details for the given Measurement book. At least one data should be given to create an Measurement book")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-23T09:58:12.227Z")

public class MeasurementBookDetail {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("measurementBook")
  private String measurementBook = null;

  @JsonProperty("loaActivity")
  private LOAActivity loaActivity = null;

  @JsonProperty("quantity")
  private Double quantity = null;

  @JsonProperty("rate")
  private Double rate = null;

  @JsonProperty("remarks")
  private String remarks = null;

  @JsonProperty("amount")
  private BigDecimal amount = null;

  @JsonProperty("measurementSheets")
  private List<MBMeasurementSheet> measurementSheets = null;

  @JsonProperty("partRate")
  private BigDecimal partRate = null;

  @JsonProperty("reducedRate")
  private BigDecimal reducedRate = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;
  
  @JsonProperty("deleted")
  private Boolean deleted = null;

  public MeasurementBookDetail id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the MB Detail
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the MB Detail")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public MeasurementBookDetail tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the MB Detail
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "Tenant id of the MB Detail")
  @NotNull

 @Size(min=2,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public MeasurementBookDetail measurementBook(String measurementBook) {
    this.measurementBook = measurementBook;
    return this;
  }

   /**
   * Measurement book reference
   * @return measurementBook
  **/
  @ApiModelProperty(required = true, value = "Measurement book reference")
  @NotNull


  public String getMeasurementBook() {
    return measurementBook;
  }

  public void setMeasurementBook(String measurementBook) {
    this.measurementBook = measurementBook;
  }

  public MeasurementBookDetail loaActivity(LOAActivity loaActivity) {
    this.loaActivity = loaActivity;
    return this;
  }

   /**
   * Get loaActivity
   * @return loaActivity
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public LOAActivity getLoaActivity() {
    return loaActivity;
  }

  public void setLoaActivity(LOAActivity loaActivity) {
    this.loaActivity = loaActivity;
  }

  public MeasurementBookDetail quantity(Double quantity) {
    this.quantity = quantity;
    return this;
  }

   /**
   * Current entry Quantity for the Measurement book details
   * @return quantity
  **/
  @ApiModelProperty(required = true, value = "Current entry Quantity for the Measurement book details")
  @NotNull


  public Double getQuantity() {
    return quantity;
  }

  public void setQuantity(Double quantity) {
    this.quantity = quantity;
  }

  public MeasurementBookDetail rate(Double rate) {
    this.rate = rate;
    return this;
  }

   /**
   * The Approved Rate for the Measurement book details
   * @return rate
  **/
  @ApiModelProperty(required = true, value = "The Approved Rate for the Measurement book details")
  @NotNull


  public Double getRate() {
    return rate;
  }

  public void setRate(Double rate) {
    this.rate = rate;
  }

  public MeasurementBookDetail remarks(String remarks) {
    this.remarks = remarks;
    return this;
  }

   /**
   * Remarks for the Measurement book details
   * @return remarks
  **/
  @ApiModelProperty(value = "Remarks for the Measurement book details")

 @Pattern(regexp="[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]") @Size(max=1024)
  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public MeasurementBookDetail amount(BigDecimal amount) {
    this.amount = amount;
    return this;
  }

   /**
   * Amount for the Measurement book details
   * @return amount
  **/
  @ApiModelProperty(required = true, value = "Amount for the Measurement book details")
  @NotNull

  @Valid

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public MeasurementBookDetail measurementSheets(List<MBMeasurementSheet> measurementSheets) {
    this.measurementSheets = measurementSheets;
    return this;
  }

  public MeasurementBookDetail addMeasurementSheetsItem(MBMeasurementSheet measurementSheetsItem) {
    if (this.measurementSheets == null) {
      this.measurementSheets = new ArrayList<MBMeasurementSheet>();
    }
    this.measurementSheets.add(measurementSheetsItem);
    return this;
  }

   /**
   * Array of Measurement Book Measurement Sheets
   * @return measurementSheets
  **/
  @ApiModelProperty(value = "Array of Measurement Book Measurement Sheets")

  @Valid

  public List<MBMeasurementSheet> getMeasurementSheets() {
    return measurementSheets;
  }

  public void setMeasurementSheets(List<MBMeasurementSheet> measurementSheets) {
    this.measurementSheets = measurementSheets;
  }

  public MeasurementBookDetail partRate(BigDecimal partRate) {
    this.partRate = partRate;
    return this;
  }

   /**
   * Part Rate of the Measurement sheet
   * @return partRate
  **/
  @ApiModelProperty(value = "Part Rate of the Measurement sheet")

  @Valid

  public BigDecimal getPartRate() {
    return partRate;
  }

  public void setPartRate(BigDecimal partRate) {
    this.partRate = partRate;
  }

  public MeasurementBookDetail reducedRate(BigDecimal reducedRate) {
    this.reducedRate = reducedRate;
    return this;
  }

   /**
   * Reduced Rate of the Measurement sheet
   * @return reducedRate
  **/
  @ApiModelProperty(value = "Reduced Rate of the Measurement sheet")

  @Valid

  public BigDecimal getReducedRate() {
    return reducedRate;
  }

  public void setReducedRate(BigDecimal reducedRate) {
    this.reducedRate = reducedRate;
  }

  public MeasurementBookDetail auditDetails(AuditDetails auditDetails) {
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


  public Boolean getDeleted() {
	return deleted;
}

public void setDeleted(Boolean deleted) {
	this.deleted = deleted;
}

@Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MeasurementBookDetail measurementBookDetail = (MeasurementBookDetail) o;
    return Objects.equals(this.id, measurementBookDetail.id) &&
        Objects.equals(this.tenantId, measurementBookDetail.tenantId) &&
        Objects.equals(this.measurementBook, measurementBookDetail.measurementBook) &&
        Objects.equals(this.loaActivity, measurementBookDetail.loaActivity) &&
        Objects.equals(this.quantity, measurementBookDetail.quantity) &&
        Objects.equals(this.rate, measurementBookDetail.rate) &&
        Objects.equals(this.remarks, measurementBookDetail.remarks) &&
        Objects.equals(this.amount, measurementBookDetail.amount) &&
        Objects.equals(this.measurementSheets, measurementBookDetail.measurementSheets) &&
        Objects.equals(this.partRate, measurementBookDetail.partRate) &&
        Objects.equals(this.reducedRate, measurementBookDetail.reducedRate) &&
        Objects.equals(this.auditDetails, measurementBookDetail.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, measurementBook, loaActivity, quantity, rate, remarks, amount, measurementSheets, partRate, reducedRate, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MeasurementBookDetail {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    measurementBook: ").append(toIndentedString(measurementBook)).append("\n");
    sb.append("    loaActivity: ").append(toIndentedString(loaActivity)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    rate: ").append(toIndentedString(rate)).append("\n");
    sb.append("    remarks: ").append(toIndentedString(remarks)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    measurementSheets: ").append(toIndentedString(measurementSheets)).append("\n");
    sb.append("    partRate: ").append(toIndentedString(partRate)).append("\n");
    sb.append("    reducedRate: ").append(toIndentedString(reducedRate)).append("\n");
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

