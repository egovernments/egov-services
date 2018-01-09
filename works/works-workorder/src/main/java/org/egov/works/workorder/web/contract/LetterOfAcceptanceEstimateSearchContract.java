package org.egov.works.workorder.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LetterOfAcceptanceEstimateSearchContract {

    private List<String> ids;

    private String tenantId;

    private List<String> letterOfAcceptanceIds;

    private Integer pageSize;

    private Integer pageNumber;

    private String sortBy;
}
