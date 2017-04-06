package org.egov.access.domain.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class Action {

    private Long id;
    private String name;
    private String url;
    private String displayName;
    private Integer orderNumber;
    private String queryParams;
    private String parentModule;
    private boolean enabled;
    private String serviceCode;
    private String tenantId;

    private Date createdDate;

    private Long createdBy;

    private Date lastModifiedDate;

    private Long lastModifiedBy;

}
