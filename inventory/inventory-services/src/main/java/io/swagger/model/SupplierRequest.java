/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.Supplier;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Contract class for web request. Array of Supplier items are used in case of
 * create or update
 */
@ApiModel(description = "Contract class for web request. Array of Supplier items  are used in case of create or update")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-02T13:59:35.200+05:30")

public class SupplierRequest {
	@JsonProperty("RequestInfo")
	private org.egov.common.contract.request.RequestInfo requestInfo = null;

	@JsonProperty("suppliers")
	@Valid
	private List<Supplier> suppliers = null;

	public SupplierRequest requestInfo(org.egov.common.contract.request.RequestInfo requestInfo) {
		this.requestInfo = requestInfo;
		return this;
	}

	/**
	 * Get requestInfo
	 * 
	 * @return requestInfo
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public org.egov.common.contract.request.RequestInfo getRequestInfo() {
		return requestInfo;
	}

	public void setRequestInfo(org.egov.common.contract.request.RequestInfo requestInfo) {
		this.requestInfo = requestInfo;
	}

	public SupplierRequest suppliers(List<Supplier> suppliers) {
		this.suppliers = suppliers;
		return this;
	}

	public SupplierRequest addSuppliersItem(Supplier suppliersItem) {
		if (this.suppliers == null) {
			this.suppliers = new ArrayList<Supplier>();
		}
		this.suppliers.add(suppliersItem);
		return this;
	}

	/**
	 * Used for search result and create only
	 * 
	 * @return suppliers
	 **/
	@ApiModelProperty(value = "Used for search result and create only")

	@Valid

	public List<Supplier> getSuppliers() {
		return suppliers;
	}

	public void setSuppliers(List<Supplier> suppliers) {
		this.suppliers = suppliers;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		SupplierRequest supplierRequest = (SupplierRequest) o;
		return Objects.equals(this.requestInfo, supplierRequest.requestInfo)
				&& Objects.equals(this.suppliers, supplierRequest.suppliers);
	}

	@Override
	public int hashCode() {
		return Objects.hash(requestInfo, suppliers);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class SupplierRequest {\n");

		sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
		sb.append("    suppliers: ").append(toIndentedString(suppliers)).append("\n");
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
