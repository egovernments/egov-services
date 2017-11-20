package org.egov.lcms.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
/**
 * 
 * @author Prasad
 *
 */
public class SearchTenantResponse {

	private TenantResponseInfo responseInfo;
	private List<Tenant> tenant;
}
