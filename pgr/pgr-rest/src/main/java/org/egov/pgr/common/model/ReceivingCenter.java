package org.egov.pgr.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder

public class ReceivingCenter {

    private Long id;
    private String name;
    private boolean crnRequired;
    private Long orderNo;
    private String tenantId;
}
