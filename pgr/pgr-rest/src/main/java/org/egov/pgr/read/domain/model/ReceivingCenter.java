package org.egov.pgr.read.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Created by mansibansal on 4/27/17.
 */
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
