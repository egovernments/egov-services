package org.egov.mseva.web.contract;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.egov.mseva.model.AuditDetails;
import org.egov.mseva.model.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Validated
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
@Builder
public class Event {
	
	@NotNull
	private String tenantId;
	
	private String id;
	
	@NotNull
	private String eventType;
	
	@NotNull
	private String description;
	
	@NotNull
	private Status status;
	
	@NotNull
	private List<String> toRoles;
	
	@NotNull
	private List<String> toUsers;
	
	private Action actions;
	
	private EventDetails eventDetails;
	
	private String source;
	
	private AuditDetails auditDetails;
	
	@JsonIgnore
	private Map<String, String> recepientEventMap;

}
