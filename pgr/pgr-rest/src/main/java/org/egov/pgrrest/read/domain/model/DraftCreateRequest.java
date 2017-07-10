package org.egov.pgrrest.read.domain.model;

import lombok.*;

import java.util.HashMap;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class DraftCreateRequest {
    private Long userId;

    private String tenantId;

    private String serviceCode;

    private HashMap<String, Object> draft;
}
