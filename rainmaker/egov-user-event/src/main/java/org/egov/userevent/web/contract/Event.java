package org.egov.userevent.web.contract;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.egov.userevent.model.AuditDetails;
import org.egov.userevent.model.RecepientEvent;
import org.egov.userevent.model.enums.Source;
import org.egov.userevent.model.enums.Status;

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
public class Event implements Comparable<Event> {
	
	@NotNull
	private String tenantId;
	
	private String id;
	
	private String referenceId;
	
	@NotNull
	private String eventType;
	
	private String eventCategory;
	
	@NotNull
	@Size(max = 65)
	private String name;
	
	@NotNull
	@Size(max = 500)
	private String description;
	
	private Status status;
	
	@NotNull
	private Source source;
	
	private String postedBy;
	
	private Recepient recepient;
	
	private Action actions;
	
	private EventDetails eventDetails;
		
	private AuditDetails auditDetails;
	
	private List<RecepientEvent> recepientEventMap;
	
	private Boolean generateCounterEvent;
	

	@Override
	/**
	 * Comparator to sort on lastModifiedTime
	 */
	public int compareTo(Event obj) {
		return this.getAuditDetails().getLastModifiedTime().compareTo(obj.getAuditDetails().getLastModifiedTime());
	}

}
