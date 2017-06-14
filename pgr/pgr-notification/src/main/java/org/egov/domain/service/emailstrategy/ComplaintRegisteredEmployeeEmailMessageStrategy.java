package org.egov.domain.service.emailstrategy;

import org.egov.domain.model.EmailMessageContext;
import org.egov.domain.model.NotificationContext;
import org.trimou.util.ImmutableMap;

import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class ComplaintRegisteredEmployeeEmailMessageStrategy implements EmailMessageStrategy {
    private static final String EMAIL_BODY_EN_TEMPLATE = "complaint_registered_employee_email_body_en";
    private static final String EMAIL_SUBJECT_EN_TEMPLATE = "complaint_registered_employee_email_subject_en";
    private static final String NAME = "name";
    private static final String LOCATION_NAME = "locationName";
    private static final String COMPLAINANT_NAME = "complainantName";
    private static final String MOBILE_NUMBER = "mobileNumber";

    @Override
    public boolean matches(NotificationContext context) {
        return context.getServiceType().isComplaintType()
            && isNotEmpty(context.getEmployee().getEmail())
            && context.getSevaRequest().isCreate();
    }

    @Override
    public EmailMessageContext getMessageContext(NotificationContext context) {
        return EmailMessageContext.builder()
            .bodyTemplateName(EMAIL_BODY_EN_TEMPLATE)
            .bodyTemplateValues(getBodyTemplate(context))
            .subjectTemplateName(EMAIL_SUBJECT_EN_TEMPLATE)
            .subjectTemplateValues(getSubjectTemplateValues())
            .email(context.getEmployee().getEmail())
            .build();
    }

    private Map<Object, Object> getBodyTemplate(NotificationContext context) {
        ImmutableMap.ImmutableMapBuilder<Object, Object> builder = ImmutableMap.builder();
        //TODO: Populate location name
        builder.put(NAME, context.getServiceType().getName());
        builder.put(LOCATION_NAME, null);
        builder.put(COMPLAINANT_NAME, context.getSevaRequest().getRequesterName());
        builder.put(MOBILE_NUMBER, context.getSevaRequest().getMobileNumber());
        return builder.build();
    }

    private Map<Object, Object> getSubjectTemplateValues() {
        //TODO: Populate location name
        return ImmutableMap.of(LOCATION_NAME, null);
    }
}

