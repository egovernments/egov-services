package org.egov.tradelicense.notification.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
/**
 * 
 * @author Shubham
 * 
 *
 */
public class EmailMessage {

	private String email;

	private String subject;

	private String body;

	private String sender;
	
	private Boolean isHTML;
}
