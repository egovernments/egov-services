package org.egov.notification.model;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
/**
 * 
 * @author Yosadhara
 *
 */
public class SMSMessageContext {

	private String templateName;

	private Map<Object, Object> templateValues;
}
