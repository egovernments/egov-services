package org.egov.userevent.web.contract;

import org.springframework.validation.annotation.Validated;

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
public class EventDetails {
	
	private String id;
	
	private String eventId;

	private Long fromDate;
	
	private Long toDate;
	
	private Long latitude;
	
	private Long longitude;
	
	
	public boolean isEmpty(EventDetails details) {
		if(null == details.getFromDate() || null == details.getToDate() || null == details.getLatitude() || null == details.getLongitude()) {
			return true;
		}
		return false;
	}
	
}
