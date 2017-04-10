package org.egov.user.domain.model;

import lombok.*;

import java.util.Date;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    private Long id;
    private String name;
    private String code;
    private String description;
    private Long createdBy;
    private Date createdDate;
    private Long lastModifiedBy;
    private Date lastModifiedDate;
}