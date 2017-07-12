package org.egov.collection.indexer.contract;

import lombok.Builder;
import lombok.Getter;
import org.egov.common.contract.request.RequestInfo;

@Getter
@Builder
public class TenantRequest {
	private RequestInfo requestInfo;
}
