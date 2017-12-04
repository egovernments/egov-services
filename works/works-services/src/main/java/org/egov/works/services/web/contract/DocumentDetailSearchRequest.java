package org.egov.works.services.web.contract;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class DocumentDetailSearchRequest {

    @NotNull
    private String tenantId;

    private List<String> objectIds;

    private List<String> ids;

    private Integer pageSize;

    private Integer pageNumber;

    private String sortProperty;
 }
