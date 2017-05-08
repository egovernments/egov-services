package org.egov.pgrrest.read.web.contract;

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
 
public ReceivingMode(org.egov.pgrrest.common.model.ReceivingMode receivingMode)
 {
    this.id  = receivingMode.getId();
    this.name = receivingMode.getName();
    this.code = receivingMode.getCode();
    this.tenantId =   receivingMode.getTenantId();

 }
}
