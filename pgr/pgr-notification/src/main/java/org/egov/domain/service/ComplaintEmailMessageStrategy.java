package org.egov.domain.service;

import org.egov.domain.model.EmailMessageContext;
import org.egov.domain.model.ServiceType;
import org.egov.domain.model.SevaRequest;
import org.egov.domain.model.Tenant;
import org.trimou.util.ImmutableMap;

import java.util.Map;

public class ComplaintEmailMessageStrategy implements EmailMessageStrategy {
    private static final String EMAIL_BODY_EN_TEMPLATE = "email_body_en";
    private static final String EMAIL_SUBJECT_EN_TEMPLATE = "email_subject_en";

    @Override
    public boolean matches(SevaRequest sevaRequest, ServiceType serviceType, Tenant tenant) {
        return serviceType.isComplaintType();
    }

    @Override
    public EmailMessageContext getMessageContext(SevaRequest sevaRequest, ServiceType serviceType, Tenant tenant) {
        return EmailMessageContext.builder()
            .bodyTemplateName(EMAIL_BODY_EN_TEMPLATE)
            .bodyTemplateValues(getBodyTemplate(sevaRequest))
            .subjectTemplateName(EMAIL_SUBJECT_EN_TEMPLATE)
            .subjectTemplateValues(getSubjectTemplateValues(sevaRequest))
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

