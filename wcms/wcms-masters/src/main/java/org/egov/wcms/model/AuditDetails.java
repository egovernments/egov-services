package org.egov.wcms.model;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
@Builder
public class AuditDetails {
	
	@NotNull
	private long createdBy;
	
	@NotNull
	private long lastModifiedBy;
	
	@JsonIgnore
	@JsonFormat(pattern="dd/MM/yyyy")
	private Date createdDate;
	
	@JsonIgnore
	@JsonFormat(pattern="dd/MM/yyyy")
	private Date lastModifiedDate;

}
