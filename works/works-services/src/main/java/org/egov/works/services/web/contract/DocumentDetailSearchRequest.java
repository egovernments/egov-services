package org.egov.works.services.web.contract;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DocumentDetailSearchRequest {

    private String tenantId;

    private List<String> objectIds;

    private List<String> ids;

    private Integer pageSize;

    private Integer pageNumber;

    private String sortProperty;
 }
