package org.egov.works.estimate.web.contract;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Contract class to get search request.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AbstractEstimateSearchContract {

    @NotNull
    private String tenantId;

    private List<String> ids;

    private Integer pageSize;

    private Integer pageNumber;

    private String sortBy;

    private List<String> departments;

    private List<String> funds;

    private List<String> functions;

    private List<String> budgetHeads;

    private Long adminSanctionFromDate;

    private Long adminSanctionToDate;

    private Boolean spillOverFlag;

    private String createdBy;

    private List<String> statuses;

    private String nameOfWork;

    private List<String> abstractEstimateNumbers;

    private List<String> workIdentificationNumbers;

    private List<String> adminSanctionNumbers;

    private List<String> councilResolutionNumbers;

    private String abstractEstimateNumberLike;

    private String workIdentificationNumberLike;

    private String adminSanctionNumberLike;

    private String councilResolutionNumberLike;

    private Boolean detailedEstimateExists;

}
