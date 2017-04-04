package org.egov.access.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
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

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date createdDate;

    private Long createdBy;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date lastModifiedDate;

    private Long lastModifiedBy;

}
