package org.egov.tradelicense.notification.web.contract;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 
 * @author Shubham
 *
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SMSMessageContext {

	private String templateName;

	private Map<Object, Object> templateValues;
}
