package org.egov.lams.notification.model;

import org.egov.lams.notification.model.enums.Priority;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Sms {
	
	private String mobileNumber;
	private String message;
	private Priority priority;
	    
}
