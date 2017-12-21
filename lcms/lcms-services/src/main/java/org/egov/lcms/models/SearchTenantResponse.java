package org.egov.lcms.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author Prasad
 *	This object holds information about the search tenant response
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SearchTenantResponse {

	private TenantResponseInfo responseInfo;
	private List<Tenant> tenant;
}
