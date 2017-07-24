package org.egov.pgr.notification.domain.service.emailstrategy;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.util.Map;

import org.egov.pgr.notification.domain.model.EmailMessageContext;
import org.egov.pgr.notification.domain.model.NotificationContext;
import org.egov.pgr.notification.domain.model.SevaRequest;
import org.egov.pgr.notification.domain.model.Tenant;
import org.springframework.beans.factory.annotation.Value;
import org.trimou.util.ImmutableMap;

public class RejectDeliverableCitizenEmailMessageStrategy implements EmailMessageStrategy {
    private static final String EMAIL_BODY_EN_TEMPLATE = "email_body_reject_deliverable_service";
    private static final String EMAIL_SUBJECT_EN_TEMPLATE = "email_subject_rejected_deliverable_service";


    @Value("${environment.host}")
    public String domain;

    @Override
    public boolean matches(NotificationContext context) {
        return context.getServiceType().isDeliverableType()
            && context.getSevaRequest().isRejected()
            && isNotEmpty(context.getSevaRequest().getRequesterEmail());
    }

    @Override
    public EmailMessageContext getMessageContext(NotificationContext context) {
        return EmailMessageContext.builder()
            .bodyTemplateName(EMAIL_BODY_EN_TEMPLATE)
            .bodyTemplateValues(getBodyTemplate(context.getSevaRequest(), context.getTenant()))
            .subjectTemplateName(EMAIL_SUBJECT_EN_TEMPLATE)
            .subjectTemplateValues(getSubjectTemplateValues(context.getSevaRequest()))
            .email(context.getSevaRequest().getRequesterEmail())
            .build();
    }

    private Map<Object, Object> getBodyTemplate(SevaRequest sevaRequest, Tenant tenant) {
        ImmutableMap.ImmutableMapBuilder<Object, Object> builder = ImmutableMap.builder();
        final String rejectionLetterUrl = getRejectionLetterUrl(sevaRequest);
        String urlLink = "<html><body><a href =" + rejectionLetterUrl + ">Download Link</a></body></html>";
        builder.put("citizenName", sevaRequest.getRequesterName());
        builder.put("crn", sevaRequest.getCrn());
        builder.put("serviceName", sevaRequest.getServiceTypeName());
        builder.put("ULBName", tenant.getName());
        builder.put("rejectionLetterUrl", urlLink);
        return builder.build();
    }

    private String getRejectionLetterUrl(SevaRequest sevaRequest) {
        String rejectionLetterId = sevaRequest.getLatestRejectionLetter();
        String tenantId = sevaRequest.getTenantId();
        return String.format("%s/filestore/v1/files/id?tenantId=%s&fileStoreId=%s", domain,
            tenantId, rejectionLetterId);
    }

    private Map<Object, Object> getSubjectTemplateValues(SevaRequest sevaRequest) {
        return ImmutableMap.of("crn", sevaRequest.getCrn(),
            "statusLowerCase", sevaRequest.getStatusName().toLowerCase());
    }
}
