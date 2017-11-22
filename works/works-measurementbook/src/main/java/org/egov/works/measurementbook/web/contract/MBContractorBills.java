package org.egov.works.measurementbook.web.contract;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * An Object that holds the basic data for a Measurement book and contractor
 * bill mapping
 */
@ApiModel(description = "An Object that holds the basic data for a Measurement book and contractor bill mapping")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-19T06:59:20.916Z")

public class MBContractorBills {
	@JsonProperty("id")
	private String id = null;

	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("measurementBook")
	private String measurementBook = null;

	@JsonProperty("contractorBill")
	private ContractorBill contractorBill = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

	public MBContractorBills id(String id) {
		this.id = id;
		return this;
	}

	/**
	 * Unique Identifier
	 * 
	 * @return id
	 **/
	@ApiModelProperty(value = "Unique Identifier")

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public MBContractorBills tenantId(String tenantId) {
		this.tenantId = tenantId;
		return this;
	}

	/**
	 * Tenant id of the BillMB
	 * 
	 * @return tenantId
	 **/
	@ApiModelProperty(required = true, value = "Tenant id of the BillMB")
	@NotNull

	@Size(min = 2, max = 128)
	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public MBContractorBills measurementBook(String measurementBook) {
		this.measurementBook = measurementBook;
		return this;
	}

	/**
	 * Measurement book reference
	 * 
	 * @return measurementBook
	 **/
	@ApiModelProperty(required = true, value = "Measurement book reference")
	@NotNull

	@Valid

	public String getMeasurementBook() {
		return measurementBook;
	}

	public void setMeasurementBook(String measurementBook) {
		this.measurementBook = measurementBook;
	}

	public MBContractorBills contractorBill(ContractorBill contractorBill) {
		this.contractorBill = contractorBill;
		return this;
	}

	/**
	 * Contractor Bill reference
	 * 
	 * @return contractorBill
	 **/
	@ApiModelProperty(required = true, value = "Contractor Bill reference")
	@NotNull

	@Valid

	public ContractorBill getContractorBill() {
		return contractorBill;
	}

	public void setContractorBill(ContractorBill contractorBill) {
		this.contractorBill = contractorBill;
	}

	public MBContractorBills auditDetails(AuditDetails auditDetails) {
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

	public AuditDetails getAuditDetails() {
		return auditDetails;
	}

	public void setAuditDetails(AuditDetails auditDetails) {
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
		MBContractorBills mbContractorBills = (MBContractorBills) o;
		return Objects.equals(this.id, mbContractorBills.id)
				&& Objects.equals(this.tenantId, mbContractorBills.tenantId)
				&& Objects.equals(this.measurementBook, mbContractorBills.measurementBook)
				&& Objects.equals(this.contractorBill, mbContractorBills.contractorBill)
				&& Objects.equals(this.auditDetails, mbContractorBills.auditDetails);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, tenantId, measurementBook, contractorBill, auditDetails);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class MBContractorBills {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
		sb.append("    measurementBook: ").append(toIndentedString(measurementBook)).append("\n");
		sb.append("    contractorBill: ").append(toIndentedString(contractorBill)).append("\n");
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
