package org.egov.user.web.contract;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleRequest {

	private Long id;

    private String name;

    private String description;

    private Long createdBy;

    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date createdDate;

    private Long lastModifiedBy;

    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date lastModifiedDate;

}