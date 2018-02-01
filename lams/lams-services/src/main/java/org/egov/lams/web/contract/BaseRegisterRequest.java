package org.egov.lams.web.contract;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseRegisterRequest {

	private Long assetCategory;

    private Long revenueWard;

    @Min(1)
    @Max(500)
    private Integer pageSize;

    private Integer pageNumber;

    @NotNull
    private String tenantId;
    
    private Long locality;
    private Long electionWard;
    private String agreementNo;
    private String oldAgreementNo;
    private String councilResolutionNo;
    private String aadharNo;
}
