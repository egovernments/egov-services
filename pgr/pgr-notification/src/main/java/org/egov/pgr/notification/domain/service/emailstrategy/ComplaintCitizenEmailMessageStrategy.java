package org.egov.pgr.notification.domain.service.emailstrategy;

import org.egov.pgr.notification.domain.model.EmailMessageContext;
import org.egov.pgr.notification.domain.model.NotificationContext;
import org.egov.pgr.notification.domain.model.SevaRequest;
import org.trimou.util.ImmutableMap;

import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class ComplaintCitizenEmailMessageStrategy implements EmailMessageStrategy {
    private static final String EMAIL_BODY_EN_TEMPLATE = "email_body_en";
    private static final String EMAIL_SUBJECT_EN_TEMPLATE = "email_subject_en";

    @Override
    public boolean matches(NotificationContext context) {
        return context.getServiceType().isComplaintType()
            && isNotEmpty(context.getSevaRequest().getRequesterEmail())
            && !context.getSevaRequest().isEscalated();
    }

    @Override
    public EmailMessageContext getMessageContext(NotificationContext context) {
        return EmailMessageContext.builder()
            .bodyTemplateName(EMAIL_BODY_EN_TEMPLATE)
            .bodyTemplateValues(getBodyTemplate(context.getSevaRequest()))
            .subjectTemplateName(EMAIL_SUBJECT_EN_TEMPLATE)
            .subjectTemplateValues(getSubjectTemplateValues(context.getSevaRequest()))
            .email(context.getSevaRequest().getRequesterEmail())
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

