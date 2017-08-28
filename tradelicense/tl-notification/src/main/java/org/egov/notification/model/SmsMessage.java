package org.egov.notification.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * 
 * @author Shubham
 *
 */
public class SmsMessage {

	private String message;

	private String mobileNumber;
}
