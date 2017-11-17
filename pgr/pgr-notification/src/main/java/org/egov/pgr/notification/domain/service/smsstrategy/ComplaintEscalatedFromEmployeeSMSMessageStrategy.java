package org.egov.pgr.notification.domain.service.smsstrategy;

import org.egov.pgr.notification.domain.model.NotificationContext;
import org.egov.pgr.notification.domain.model.SMSMessageContext;
import org.springframework.beans.factory.annotation.Value;
import org.trimou.util.ImmutableMap;

import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class ComplaintEscalatedFromEmployeeSMSMessageStrategy implements SMSMessageStrategy {
    private static final String NAME = "name";
    private static final String NUMBER = "number";
    private static final String NEXT_ASSIGNEE_NAME = "nextAssigneeName";
    private static final String DESIGNATION = "designation";
    private static final String POSITION = "position";
    private static final String TEMPLATE_NAME = "sms_complaint_escalated_from_employee";

    @Value("${sms.escalation.enabled}")
    private Boolean smsEnabled;

    @Override
    public boolean matches(NotificationContext context) {
        return smsEnabled && context.getServiceType().isComplaintType()
            && context.getSevaRequest().isEscalated()
            && isNotEmpty(context.getPreviousEmployee().getMobileNumber());
    }

    @Override
    public SMSMessageContext getMessageContext(NotificationContext context) {
        final Map<Object, Object> map = ImmutableMap.of(
            NAME, context.getServiceType().getName(),
            NUMBER, context.getSevaRequest().getCrn(),
            NEXT_ASSIGNEE_NAME, context.getEmployee().getName(),
            DESIGNATION, context.getEmployee().getPrimaryDesignation(),
            POSITION, context.getEmployee().getPrimaryPosition()
        );
        final String mobileNumber = context.getPreviousEmployee().getMobileNumber();
        return new SMSMessageContext(TEMPLATE_NAME, map, mobileNumber);
    }
}
