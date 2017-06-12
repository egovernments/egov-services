package org.egov.domain.service;

import org.egov.domain.model.*;
import org.trimou.util.ImmutableMap;

import java.util.Map;

public class ComplaintEmailMessageStrategy implements EmailMessageStrategy {
    private static final String EMAIL_BODY_EN_TEMPLATE = "email_body_en";
    private static final String EMAIL_SUBJECT_EN_TEMPLATE = "email_subject_en";

    @Override
    public boolean matches(NotificationContext context) {
        return context.getServiceType().isComplaintType();
    }

    @Override
    public EmailMessageContext getMessageContext(NotificationContext context) {
        return EmailMessageContext.builder()
            .bodyTemplateName(EMAIL_BODY_EN_TEMPLATE)
            .bodyTemplateValues(getBodyTemplate(context.getSevaRequest()))
            .subjectTemplateName(EMAIL_SUBJECT_EN_TEMPLATE)
            .subjectTemplateValues(getSubjectTemplateValues(context.getSevaRequest()))
            .build();
    }

    private Map<Object, Object> getBodyTemplate(SevaRequest sevaRequest) {
        ImmutableMap.ImmutableMapBuilder<Object, Object> builder = ImmutableMap.builder();
        builder.put("complainantName", sevaRequest.getRequesterName());
        builder.put("crn", sevaRequest.getCrn());
        builder.put("complaintType", sevaRequest.getServiceTypeName());
        builder.put("locationName", sevaRequest.getLocationName());
        builder.put("complaintDetails", sevaRequest.getDetails());
        builder.put("registeredDate", sevaRequest.getFormattedCreatedDate());
        builder.put("statusUpperCase", sevaRequest.getStatusName());
        builder.put("statusLowerCase",sevaRequest.getStatusName().toLowerCase());
        return builder.build();
    }

    private Map<Object, Object> getSubjectTemplateValues(SevaRequest sevaRequest) {
        return ImmutableMap.of("crn", sevaRequest.getCrn(),
            "statusLowerCase",sevaRequest.getStatusName().toLowerCase());
    }
}

