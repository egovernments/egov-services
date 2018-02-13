package org.egov.works.workorder.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by ritesh on 27/11/17.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WorkOrderSearchContract {

    private List<String> ids;

    private Long fromDate;

    private Long toDate;

    private List<String> statuses;

    private List<String> contractorNames;

    private List<String> contractorCodes;

    private List<String> detailedEstimateNumbers;

    private List<String> workIdentificationNumbers;
    
    private List<String> workOrderNumbers;

    private List<String> loaNumbers;
    
    private List<String> department;

    private String tenantId;

    private Integer pageSize;

    private Integer pageNumber;

    private String sortBy;
    
    private List<String> letterOfAcceptances;
    
    private String contractorNameLike;

    private String contractorCodeLike;

    private String detailedEstimateNumberLike;

    private String workIdentificationNumberLike;
    
    private String workOrderNumberLike;

    private String loaNumberLike;

    private Boolean withAllOfflineStatusAndMBNotCreated = false;

    private Boolean milestoneExists = false;

    private Boolean billExists = false;

    private Boolean contractorAdvanceExists = false;

    private Boolean mbExistsAndBillNotCreated = false;

    private Boolean withoutOfflineStatus = false;

}
