package org.egov.user.domain.model;

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
	private static final String CITIZEN = "CITIZEN";
	private Long id;
    private String name;
    private String code;
    private String description;
    private Long createdBy;
    private Date createdDate;
    private Long lastModifiedBy;
    private Date lastModifiedDate;
    private String tenantId;

    public static Role getCitizenRole() {
    	return Role.builder().code(CITIZEN).build();
	}
}