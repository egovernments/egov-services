package org.egov.domain.service;

import org.egov.domain.model.EmailMessageContext;
import org.egov.domain.model.ServiceType;
import org.egov.domain.model.SevaRequest;
import org.egov.domain.model.Tenant;
import org.trimou.util.ImmutableMap;

import java.util.Map;

public class ProcessingFeeEnteredEmailMessageStrategy implements EmailMessageStrategy {
    private static final String EMAIL_BODY_EN_TEMPLATE = "email_body_deliverable_service_processing_fee_entered";
    private static final String EMAIL_SUBJECT_EN_TEMPLATE = "email_subject_deliverable_service_processing_fee_entered";

    @Override
    public boolean matches(SevaRequest sevaRequest, ServiceType serviceType, Tenant tenant) {
        return serviceType.isDeliverableType()
            && sevaRequest.isInProgress()
            && sevaRequest.isEmployeeLoggedIn()
            && sevaRequest.isProcessingFeePresent();
    }

    @Override
    public EmailMessageContext getMessageContext(SevaRequest sevaRequest, ServiceType serviceType, Tenant tenant) {
        return EmailMessageContext.builder()
            .bodyTemplateName(EMAIL_BODY_EN_TEMPLATE)
            .bodyTemplateValues(getBodyTemplate(sevaRequest, tenant))
            .subjectTemplateName(EMAIL_SUBJECT_EN_TEMPLATE)
            .subjectTemplateValues(getSubjectTemplateValues(sevaRequest))
            .build();
    }

    private Map<Object, Object> getBodyTemplate(SevaRequest sevaRequest, Tenant tenant) {
        ImmutableMap.ImmutableMapBuilder<Object, Object> builder = ImmutableMap.builder();
        builder.put("citizenName", sevaRequest.getRequesterName());
        builder.put("crn", sevaRequest.getCrn());
        builder.put("fee", sevaRequest.getProcessingFee());
        builder.put("serviceName", sevaRequest.getServiceTypeName());
        builder.put("ulbGrade", tenant.getUlbGrade());
        builder.put("ulbName", tenant.getName());
        return builder.build();
    }

    private Map<Object, Object> getSubjectTemplateValues(SevaRequest sevaRequest) {
        return ImmutableMap.of("crn", sevaRequest.getCrn());
    }
}
