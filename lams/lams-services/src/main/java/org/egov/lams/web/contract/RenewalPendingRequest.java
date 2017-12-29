package org.egov.lams.web.contract;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RenewalPendingRequest {

	private Long assetCategory;
	
	private String expiryTime;

    private Long revenueWard;

    @Min(1)
    @Max(500)
    private Integer pageSize;

    private Integer pageNumber;

    @NotNull
    private String tenantId;
	
}
