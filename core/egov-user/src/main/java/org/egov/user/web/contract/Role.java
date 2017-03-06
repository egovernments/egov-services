package org.egov.user.web.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role {
	@JsonProperty("id")
	private Long id;

	@JsonProperty("name")
	private String name;

	@JsonProperty("description")
	private String description;

	@JsonProperty("createdBy")
	private Long createdBy;

	@JsonProperty("createdDate")
	@JsonFormat(pattern = "MM/dd/yyyy")
	private Date createdDate;

	@JsonProperty("lastModifiedBy")
	private Long lastModifiedBy;

    @JsonProperty("lastModifiedDate")
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date lastModifiedDate;

	public Role(org.egov.user.persistence.entity.Role roleEntity) {
		this.id = roleEntity.getId();
		this.name = roleEntity.getName();
		this.description = roleEntity.getDescription();
		this.createdBy = roleEntity.getCreatedBy().getId();
		this.createdDate = roleEntity.getCreatedDate();
		this.lastModifiedBy = roleEntity.getLastModifiedBy().getId();
		this.lastModifiedDate = roleEntity.getLastModifiedDate();
	}
}
