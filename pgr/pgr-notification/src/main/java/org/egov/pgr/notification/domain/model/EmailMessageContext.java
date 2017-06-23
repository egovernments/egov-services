package org.egov.pgr.notification.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class EmailMessageContext {
    private String email;
    private String bodyTemplateName;
    private String subjectTemplateName;
    private Map<Object, Object> bodyTemplateValues;
    private Map<Object, Object> subjectTemplateValues;
}
