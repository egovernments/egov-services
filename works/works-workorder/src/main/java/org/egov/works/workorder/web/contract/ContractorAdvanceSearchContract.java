package org.egov.works.workorder.web.contract;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by ritesh on 27/11/17.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContractorAdvanceSearchContract {

    private List<String> ids;

    private Long fromDate;

    private Long toDate;

    //Not Implemented
    private List<String> advanceRequisitionNumbers;

    private List<String> contractorNames;

    private List<String> contractorCodes;

    private List<String> loaNumbers;

    private String tenantId;

    private String sortBy;

    private String contractorNameLike;

    private String contractorCodeLike;

    private String loaNumberLike;
    //Not Implemented
    private String advanceRequisitionNumberLike;

    //Not Implemented
    private List<String> advanceBillNumbers;
    
    private List<String> statuses;
    
    private String letterOfAcceptanceEstimate;
    
}
