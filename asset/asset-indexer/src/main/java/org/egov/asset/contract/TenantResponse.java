package org.egov.asset.contract;

import java.util.List;

import org.egov.asset.model.Tenant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TenantResponse {

	private final ResponseInfo responseInfo;
	private final List<Tenant> tenant;
}
