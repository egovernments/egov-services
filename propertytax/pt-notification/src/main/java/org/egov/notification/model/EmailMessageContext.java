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
public class EmailMessageContext {

	private String bodyTemplateName;

	private String subjectTemplateName;

	private Map<Object, Object> bodyTemplateValues;

	private Map<Object, Object> subjectTemplateValues;
}
