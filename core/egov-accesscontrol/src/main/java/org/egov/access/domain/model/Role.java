package org.egov.access.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
