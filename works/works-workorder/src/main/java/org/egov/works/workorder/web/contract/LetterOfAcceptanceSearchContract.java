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

    private List<String> loaNumbers;

    private List<String> detailedEstimateNumbers;

    private String fileNumber;

    private Long fromDate;

    private Long toDate;

    private List<String> department;

    private List<String> statuses;

    private List<String> contractorNames;

    private List<String> contractorCodes;

    private Boolean spillOverFlag;

    private Integer pageSize;

    private Integer pageNumber;

    private String sortBy;

}
