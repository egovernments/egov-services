package org.egov.works.workorder.web.contract;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class LetterOfAcceptanceSearchContract {

    private String tenantId;

    private List<String> ids;

    private String fileNumber;

    private Long fromDate;

    private Long toDate;

    private List<String> department;

    private List<String> statuses;

    private List<String> contractorNames;

    private List<String> contractorCodes;

    private List<String> loaNumbers;

    private List<String> detailedEstimateNumbers;

    private String contractorNameLike;

    private String contractorCodeLike;

    private String loaNumberLike;

    private String detailedEstimateNumberLike;

    private Boolean spillOverFlag;

    private Integer pageSize;

    private Integer pageNumber;

    private String sortBy;

    private String loaEstimateId;

    private Boolean workOrderExists = false;

    private Boolean withAllOfflineStatusAndWONotCreated = false;

    private Boolean withoutOfflineStatus = false;

    private Boolean milestoneExists = false;

    private Boolean billExists = false;

    private Boolean contractorAdvanceExists = false;

    private Boolean mbExistsAndBillNotCreated = false;

}
