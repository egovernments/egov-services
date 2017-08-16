package org.egov.pgr.persistence.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCategory {

    private Integer id;
    private String tenantId;
    private String name;
    private String description;
    private Boolean active;
}
