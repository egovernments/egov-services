package org.egov.inv.domain.model;

import lombok.*;

import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuditDetails {

    private String createdBy;

    private String lastModifiedBy;

    private Long createdTime;

    private Long lastModifiedTime;

    @Size(max = 256)
    private String tenantId;

}
