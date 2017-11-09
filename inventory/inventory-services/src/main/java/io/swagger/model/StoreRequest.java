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

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Contract class for web request. Array of Store items are used in case of
 * create or update
 */
@ApiModel(description = "Contract class for web request. Array of Store items  are used in case of create or update")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-28T13:21:55.964+05:30")

public class StoreRequest {
	@JsonProperty("RequestInfo")
	private org.egov.common.contract.request.RequestInfo requestInfo = null;

	@JsonProperty("stores")
	@Valid
	private List<Store> stores = null;

	public StoreRequest requestInfo(
			org.egov.common.contract.request.RequestInfo requestInfo) {
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

	public void setRequestInfo(
			org.egov.common.contract.request.RequestInfo requestInfo) {
		this.requestInfo = requestInfo;
	}

	public StoreRequest stores(List<Store> stores) {
		this.stores = stores;
		return this;
	}

	public StoreRequest addStoresItem(Store storesItem) {
		if (this.stores == null) {
			this.stores = new ArrayList<Store>();
		}
		this.stores.add(storesItem);
		return this;
	}

	/**
	 * Used for search result and create only
	 *
	 * @return stores
	 **/
	@ApiModelProperty(value = "Used for search result and create only")

	@Valid

	public List<Store> getStores() {
		return stores;
	}

	public void setStores(List<Store> stores) {
		this.stores = stores;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		StoreRequest storeRequest = (StoreRequest) o;
		return Objects.equals(this.requestInfo, storeRequest.requestInfo)
				&& Objects.equals(this.stores, storeRequest.stores);
	}

	@Override
	public int hashCode() {
		return Objects.hash(requestInfo, stores);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class StoreRequest {\n");

		sb.append("    requestInfo: ").append(toIndentedString(requestInfo))
				.append("\n");
		sb.append("    stores: ").append(toIndentedString(stores)).append("\n");
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
