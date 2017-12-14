package org.egov.works.workorder.web.contract;

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

    private List<String> workOrderNumbers;

    private List<String> loaNumbers;

    private Long fromDate;

    private Long toDate;

    private List<String> statuses;

    private List<String> contractorNames;

    private List<String> contractorCodes;

    private List<String> detailedEstimateNumbers;

    private List<String> workIdentificationNumbers;

    private List<String> department;

    private String tenantId;

    private Integer pageSize;

    private Integer pageNumber;

    private String sortBy;
    
    private List<String> letterOfAcceptances;

}
