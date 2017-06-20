package org.egov.demand.web.contract;

import java.util.ArrayList;
import java.util.List;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.demand.model.BusinessServiceDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * BillRequest
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessServiceDetailResponse {

	private ResponseInfo responseInfo;

	private List<BusinessServiceDetail> businessServiceDetails = new ArrayList<>();
}