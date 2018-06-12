package org.egov.pt.web.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode(of = "code")
public class Role {
    private Long id;
    private String name;
    private String code;
    private String description;
    private Long createdBy;
    private Date createdDate;
    private Long lastModifiedBy;
    private Date lastModifiedDate;
    private String tenantId;

    public Role(){}

}