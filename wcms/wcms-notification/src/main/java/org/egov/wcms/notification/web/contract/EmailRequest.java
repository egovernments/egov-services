package org.egov.wcms.notification.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
/**
 * 
 * @author Shubham
 *
 */
public class EmailRequest {

	@NonNull
	private String subject;

	@NonNull
	private String body;
}