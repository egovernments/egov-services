package org.egov.works.estimate.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Contract class to get search request.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DetailedEstimateSearchContract {

    @NotNull
    private String tenantId;

    private List<String> ids;
    
    private Integer pageSize;

    private Integer pageNumber;

    private String sortBy;

    private List<String> departments;
    
    private Long fromDate;

    private Long toDate;

    private Long fromAmount;

    private Long toAmount;

    private List<String> typeOfWork;

    private List<String> subTypeOfWork;

    private List<String> statuses;

    private String nameOfWork;

    private List<String> wards;

    private String currentOwner;

    private Boolean spillOverFlag;

    private String createdBy;

    private List<String> technicalSanctionNumbers;

    private List<String> workIdentificationNumbers;

    private List<String> abstractEstimateNumbers;

    private List<String> detailedEstimateNumbers;
    
    private String technicalSanctionNumberLike;

    private String workIdentificationNumberLike;

    private String abstractEstimateNumberLike;

    private String detailedEstimateNumberLike;

    private String scheduleOfRate;

    private Boolean loaCreated;

    private Boolean withoutOfflineStatus;

    private Boolean withAllOfflineStatusAndLoaNotCreated;

}
