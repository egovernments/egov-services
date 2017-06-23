package org.egov.pgr.notification.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ServiceType {
    private Long id;
    private String serviceName;
    private String serviceCode;
    private String description;
    private boolean metadata;
    private String type;
    private List<String> keywords;
    private Long groupId;
    private String groups;
    private Integer slaHours;
    private String tenantId;
}
