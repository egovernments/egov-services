package org.egov.pgr.location.contract;

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
