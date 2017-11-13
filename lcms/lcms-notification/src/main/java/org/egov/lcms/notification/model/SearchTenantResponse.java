package org.egov.lcms.notification.model;

import java.util.List;
import org.egov.common.contract.response.ResponseInfo;
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

	private ResponseInfo responseInfo;
	private List<Tenant> tenant;
}
