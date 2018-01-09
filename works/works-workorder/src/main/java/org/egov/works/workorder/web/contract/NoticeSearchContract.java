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
public class NoticeSearchContract {

    private List<String> ids;

    private List<String> workOrderNumbers;

    private List<String> loaNumbers;

    private List<String> contractorNames;

    private List<String> contractorCodes;

    private List<String> detailedEstimateNumbers;

    private List<String> workIdentificationNumbers;

    private String tenantId;

    private Integer pageSize;

    private Integer pageNumber;

    private String sortBy;

    private List<String> statuses;

}
