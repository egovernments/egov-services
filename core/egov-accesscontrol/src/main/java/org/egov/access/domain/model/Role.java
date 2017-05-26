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
public class Role {

    private Long id;
    private String name;
    private String description;
    private String code;
    private Date createdDate;
    private Long createdBy;
    private Date lastModifiedDate;
    private Long lastModifiedBy;
}
