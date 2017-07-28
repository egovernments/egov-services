package org.egov.collection.notification.domain.model;


import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class SmsMessage {
	
	private String mobileNumber;
	private String message;
	
	    
}
