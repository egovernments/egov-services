package org.egov.works.estimate.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Contract class to send response. Array of offlineStatuses items are used in
 * case of search results, also multiple offlineStatuses item is used for create
 * and update
 */
@ApiModel(description = "Contract class to send response. Array of offlineStatuses items are used in case of search results, also multiple  offlineStatuses item is used for create and update")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-10T05:42:02.605Z")

public class OfflineStatusRequest {
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo = null;

	@JsonProperty("offlineStatuses")
	private List<OfflineStatus> offlineStatuses = null;

	public OfflineStatusRequest requestInfo(RequestInfo requestInfo) {
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

	public RequestInfo getRequestInfo() {
		return requestInfo;
	}

	public void setRequestInfo(RequestInfo requestInfo) {
		this.requestInfo = requestInfo;
	}

	public OfflineStatusRequest offlineStatuses(List<OfflineStatus> offlineStatuses) {
		this.offlineStatuses = offlineStatuses;
		return this;
	}

	public OfflineStatusRequest addOfflineStatusesItem(OfflineStatus offlineStatusesItem) {
		if (this.offlineStatuses == null) {
			this.offlineStatuses = new ArrayList<OfflineStatus>();
		}
		this.offlineStatuses.add(offlineStatusesItem);
		return this;
	}

	/**
	 * Used for create and update only
	 * 
	 * @return offlineStatuses
	 **/
	@ApiModelProperty(value = "Used for create and update only")

	@Valid

	public List<OfflineStatus> getOfflineStatuses() {
		return offlineStatuses;
	}

	public void setOfflineStatuses(List<OfflineStatus> offlineStatuses) {
		this.offlineStatuses = offlineStatuses;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		OfflineStatusRequest offlineStatusRequest = (OfflineStatusRequest) o;
		return Objects.equals(this.requestInfo, offlineStatusRequest.requestInfo)
				&& Objects.equals(this.offlineStatuses, offlineStatusRequest.offlineStatuses);
	}

	@Override
	public int hashCode() {
		return Objects.hash(requestInfo, offlineStatuses);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class OfflineStatusRequest {\n");

		sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
		sb.append("    offlineStatuses: ").append(toIndentedString(offlineStatuses)).append("\n");
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
