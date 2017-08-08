package org.egov.pgr.persistence.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceTypeKeyword {

    private String servicecode;
    private String tenantId;
    private String keyword;
}
