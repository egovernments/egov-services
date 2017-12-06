package org.egov.works.services.web.contract;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentDetailSearchCriteria {

    private String tenantId;

    private List<String> objectIds;

    private List<String> ids;

    private Integer pageSize;

    private Integer pageNumber;

    private String sortProperty;

}
