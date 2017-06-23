package org.egov.pgr.notification.domain.service.emailstrategy;

import org.egov.pgr.notification.domain.model.EmailMessageContext;
import org.egov.pgr.notification.domain.model.NotificationContext;
import org.egov.pgr.notification.domain.model.SevaRequest;
import org.egov.pgr.notification.domain.model.Tenant;
import org.trimou.util.ImmutableMap;

import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class ProcessingFeeEnteredCitizenEmailMessageStrategy implements EmailMessageStrategy {
    private static final String EMAIL_BODY_EN_TEMPLATE = "email_body_deliverable_service_processing_fee_entered";
    private static final String EMAIL_SUBJECT_EN_TEMPLATE = "email_subject_deliverable_service_processing_fee_entered";

    @Override
    public boolean matches(NotificationContext context) {
        return context.getServiceType().isDeliverableType()
            && context.getSevaRequest().isInProgress()
            && context.getSevaRequest().isEmployeeLoggedIn()
            && context.getSevaRequest().isProcessingFeePresent()
            && isNotEmpty(context.getSevaRequest().getRequesterEmail());
    }

    @Override
    public EmailMessageContext getMessageContext(NotificationContext context) {
        return EmailMessageContext.builder()
            .email(context.getSevaRequest().getRequesterEmail())
            .bodyTemplateName(EMAIL_BODY_EN_TEMPLATE)
            .bodyTemplateValues(getBodyTemplate(context.getSevaRequest(), context.getTenant()))
            .subjectTemplateName(EMAIL_SUBJECT_EN_TEMPLATE)
            .subjectTemplateValues(getSubjectTemplateValues(context.getSevaRequest()))
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
