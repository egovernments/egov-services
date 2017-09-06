package org.egov.pgr.location.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CrossHierarchy {

    private Long id;

    private String code;

    private String tenantId;

}
