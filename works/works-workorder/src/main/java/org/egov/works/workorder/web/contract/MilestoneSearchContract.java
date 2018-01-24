package org.egov.works.workorder.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by ramki on 20/12/17.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MilestoneSearchContract {
    private String tenantId;
    private List<String> ids;
    private List<String> statuses;
    private List<String> workOrderNumbers;
    private List<String> loaNumbers;
    private List<String> detailedEstimateNumbers;
    private List<String> workIdentificationNumbers;
    private List<String> departments;
    private List<String> contractorNames;
    private List<String> contractorCodes;
    private Boolean isTrackMilestoneCompleted;
    private Integer pageSize;
    private Integer pageNumber;
    private String sortBy;
}
