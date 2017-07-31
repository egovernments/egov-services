package org.egov.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class describe the set of fields contained in LicenseStatusResponse
 * 
 * @author Shubham pratap Singh
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LicenseStatusResponse {

	private ResponseInfo responseInfo;

	private List<LicenseStatus> licenseStatuses;
}