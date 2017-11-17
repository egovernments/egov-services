package org.egov.pgr.notification.domain.service.smsstrategy;

import org.egov.pgr.notification.domain.model.NotificationContext;
import org.egov.pgr.notification.domain.model.SMSMessageContext;
import org.springframework.beans.factory.annotation.Value;
import org.trimou.util.ImmutableMap;

import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class ComplaintEscalatedToEmployeeSMSMessageStrategy implements SMSMessageStrategy {
    private static final String NAME = "name";
    private static final String NUMBER = "number";
    private static final String PREVIOUS_ASSIGNEE_NAME = "previousAssigneeName";
    private static final String DESIGNATION = "designation";
    private static final String POSITION = "position";
    private static final String TEMPLATE_NAME = "sms_complaint_escalated_to_employee";

    @Value("${sms.escalation.enabled}")
    private Boolean smsEnabled;

    @Override
    public boolean matches(NotificationContext context) {
        return smsEnabled && context.getServiceType().isComplaintType()
            && context.getSevaRequest().isEscalated()
            && isNotEmpty(context.getEmployee().getMobileNumber());
    }

    @Override
    public SMSMessageContext getMessageContext(NotificationContext context) {
        final Map<Object, Object> map = ImmutableMap.of(
            NAME, context.getServiceType().getName(),
            NUMBER, context.getSevaRequest().getCrn(),
            PREVIOUS_ASSIGNEE_NAME, context.getPreviousEmployee().getName(),
            DESIGNATION, context.getPreviousEmployeeDesignation(),
            POSITION, context.getPreviousEmployeePosition()
        );
        final String mobileNumber = context.getEmployee().getMobileNumber();
        return new SMSMessageContext(TEMPLATE_NAME, map, mobileNumber);
    }
}

