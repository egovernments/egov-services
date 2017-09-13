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
public class EmailMessageContext {

	private String bodyTemplateName;

	private String subjectTemplateName;

	private Map<Object, Object> bodyTemplateValues;

	private Map<Object, Object> subjectTemplateValues;
}
