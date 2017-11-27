package org.egov.works.workorder.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class LoaActivitySearchContract {

    private List<String> ids;

    private String tenantId;

    private List<String> letterOfAcceptanceEstimateIds;

    private Integer pageSize;

    private Integer pageNumber;

    private String sortBy;
}
