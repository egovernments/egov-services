package org.egov.eis.web.contract;

import java.util.Objects;

import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Probation {
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("designation")
	private String designation = null;

	@JsonProperty("declaredOn")
	private LocalDate declaredOn = null;

	@JsonProperty("orderNo")
	private String orderNo = null;

	@JsonProperty("orderDate")
	private LocalDate orderDate = null;

	@JsonProperty("remarks")
	private String remarks = null;

	@JsonProperty("tenantId")
	private String tenantId = null;

	public Probation id(Long id) {
		this.id = id;
		return this;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Probation designation(String designation) {
		this.designation = designation;
		return this;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public Probation declaredOn(LocalDate declaredOn) {
		this.declaredOn = declaredOn;
		return this;
	}

	public LocalDate getDeclaredOn() {
		return declaredOn;
	}

	public void setDeclaredOn(LocalDate declaredOn) {
		this.declaredOn = declaredOn;
	}

	public Probation orderNo(String orderNo) {
		this.orderNo = orderNo;
		return this;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Probation orderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
		return this;
	}

	public LocalDate getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}

	public Probation remarks(String remarks) {
		this.remarks = remarks;
		return this;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Probation probation = (Probation) o;
		return Objects.equals(this.id, probation.id) && Objects.equals(this.designation, probation.designation)
				&& Objects.equals(this.declaredOn, probation.declaredOn)
				&& Objects.equals(this.orderNo, probation.orderNo)
				&& Objects.equals(this.orderDate, probation.orderDate)
				&& Objects.equals(this.tenantId, probation.tenantId) && Objects.equals(this.remarks, probation.remarks);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, designation, declaredOn, orderNo, orderDate, remarks, tenantId);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Probation {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    designation: ").append(toIndentedString(designation)).append("\n");
		sb.append("    declaredOn: ").append(toIndentedString(declaredOn)).append("\n");
		sb.append("    orderNo: ").append(toIndentedString(orderNo)).append("\n");
		sb.append("    orderDate: ").append(toIndentedString(orderDate)).append("\n");
		sb.append("    remarks: ").append(toIndentedString(remarks)).append("\n");
		sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
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
