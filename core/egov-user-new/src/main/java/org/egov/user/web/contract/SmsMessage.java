package org.egov.user.web.contract;


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
