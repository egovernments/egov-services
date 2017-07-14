package org.egov.notification.model;

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
 * @author Yosadhara
 *
 */
public class EmailRequest {

	@NonNull
	private String subject;

	@NonNull
	private String body;
}