package org.egov.models;

import java.util.List;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class describe the set of fields contained in LicenseStatusRequest
 * 
 * @author Shubham pratap Singh
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LicenseStatusRequest {

	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;

	@Valid
	private List<LicenseStatus> licenseStatuses;
}