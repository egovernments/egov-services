package org.egov.lcms.notification.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
public class EmailMessage {
	
	private String email;

	private String subject;

	private String body;

	private String sender;
	
	private Boolean isHTML;
}
