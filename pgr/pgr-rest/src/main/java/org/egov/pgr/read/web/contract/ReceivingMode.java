package org.egov.pgr.read.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ReceivingMode {

 private Long id;
 private String name;
 private String code;
 private String tenantId;
 
public ReceivingMode(org.egov.pgr.common.entity.ReceivingMode receivingMode)
 {
    this.id  = receivingMode.getId();
    this.name = receivingMode.getName();
    this.code = receivingMode.getCode();
    this.tenantId =   receivingMode.getTenantId();

 }
}
