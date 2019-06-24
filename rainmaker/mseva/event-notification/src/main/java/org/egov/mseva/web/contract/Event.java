package org.egov.mseva.web.contract;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.egov.mseva.model.AuditDetails;
import org.egov.mseva.model.RecepientEvent;
import org.egov.mseva.model.enums.Source;
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
	
	private String referenceId;
	
	@NotNull
	private String eventType;
	
	private String name;
	
	@NotNull
	private String description;
	
	@NotNull
	private Status status;
	
	@NotNull
	private Source source;
	
	private String postedBy;
	
	@Valid
	@NotNull
	private Recepient recepient;
	
	private Action actions;
	
	private EventDetails eventDetails;
		
	private AuditDetails auditDetails;
	
	private List<RecepientEvent> recepientEventMap;

}
