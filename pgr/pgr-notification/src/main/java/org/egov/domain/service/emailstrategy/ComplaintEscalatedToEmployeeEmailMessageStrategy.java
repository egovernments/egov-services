package org.egov.domain.service.emailstrategy;

import org.egov.domain.model.EmailMessageContext;
import org.egov.domain.model.NotificationContext;
import org.trimou.util.ImmutableMap;

import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class ComplaintEscalatedToEmployeeEmailMessageStrategy implements EmailMessageStrategy {
    private static final String EMAIL_BODY_EN_TEMPLATE = "complaint_escalated_to_employee_email_body_en";
    private static final String EMAIL_SUBJECT_EN_TEMPLATE = "complaint_escalated_subject_en";
    private static final String LOCATION_NAME = "locationName";
    private static final String NAME = "name";
    private static final String NUMBER = "number";
    private static final String PREVIOUS_ASSIGNEE_NAME = "previousAssigneeName";
    private static final String DESIGNATION = "designation";
    private static final String POSITION = "position";

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
        //TODO: Fetch employee details
        builder.put(NAME, context.getServiceType().getName());
        builder.put(NUMBER, context.getSevaRequest().getCrn());
        builder.put(PREVIOUS_ASSIGNEE_NAME, null);
        builder.put(DESIGNATION, null);
        builder.put(POSITION, null);
        return builder.build();
    }

    private Map<Object, Object> getSubjectTemplateValues() {
        //TODO: Populate location name
        return ImmutableMap.of(LOCATION_NAME, null);
    }
}

