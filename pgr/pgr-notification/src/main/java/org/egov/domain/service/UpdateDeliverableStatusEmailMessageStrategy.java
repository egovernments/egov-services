package org.egov.domain.service;

import org.egov.domain.model.*;
import org.trimou.util.ImmutableMap;

import java.util.Map;

public class UpdateDeliverableStatusEmailMessageStrategy implements EmailMessageStrategy {
    private static final String EMAIL_BODY_EN_TEMPLATE = "email_body_deliverable_service_status_updated";
    private static final String EMAIL_SUBJECT_EN_TEMPLATE = "email_subject_deliverable_service_status_updated";

    @Override
    public boolean matches(NotificationContext context) {
        return context.getServiceType().isDeliverableType()
            && context.getSevaRequest().isUpdate()
            && context.getSevaRequest().isEmployeeLoggedIn();
    }

    @Override
    public EmailMessageContext getMessageContext(NotificationContext context) {
        return EmailMessageContext.builder()
            .bodyTemplateName(EMAIL_BODY_EN_TEMPLATE)
            .bodyTemplateValues(getBodyTemplate(context))
            .subjectTemplateName(EMAIL_SUBJECT_EN_TEMPLATE)
            .subjectTemplateValues(getSubjectTemplateValues(context.getSevaRequest()))
            .build();
    }

    private Map<Object, Object> getBodyTemplate(NotificationContext context) {
        ImmutableMap.ImmutableMapBuilder<Object, Object> builder = ImmutableMap.builder();
        builder.put("citizenName", context.getSevaRequest().getRequesterName());
        builder.put("crn", context.getSevaRequest().getCrn());
        builder.put("serviceName", context.getServiceType().getName());
        builder.put("ULBName", context.getTenant().getName());
        return builder.build();
    }

    private Map<Object, Object> getSubjectTemplateValues(SevaRequest sevaRequest) {
        return ImmutableMap.of("crn", sevaRequest.getCrn(),
            "statusLowerCase",sevaRequest.getStatusName().toLowerCase());
    }
}
