package org.egov.swm.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContractServicesOffered {

    private String tenantId;

    private String vendorcontract;

    private String vendorcontracts;

    private String service;

    private String services;

    private String createdBy;

    private String lastModifiedBy;

    private Long createdTime;

    private Long lastModifiedTime;
}