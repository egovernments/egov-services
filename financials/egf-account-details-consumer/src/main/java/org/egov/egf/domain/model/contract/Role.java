package org.egov.egf.domain.model.contract;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Role {

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
