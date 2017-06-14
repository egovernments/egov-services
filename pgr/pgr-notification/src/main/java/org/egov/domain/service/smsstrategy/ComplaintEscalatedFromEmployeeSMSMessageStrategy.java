package org.egov.domain.service.smsstrategy;

import org.egov.domain.model.NotificationContext;
import org.egov.domain.model.SMSMessageContext;
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

    @Override
    public boolean matches(NotificationContext context) {
        return context.getServiceType().isComplaintType()
            && context.getSevaRequest().isEscalated()
            && isNotEmpty(context.getPreviousEmployee().getMobileNumber());
    }

    @Override
    public SMSMessageContext getMessageContext(NotificationContext context) {
        final Map<Object, Object> map = ImmutableMap.of(
            NAME, context.getServiceType().getName(),
            NUMBER, context.getSevaRequest().getCrn(),
            //TODO: Fetch employee details
            NEXT_ASSIGNEE_NAME, null,
            DESIGNATION, null,
            POSITION, null
        );
//        TODO: get previous employee mobile number
        final String mobileNumber = context.getPreviousEmployee().getMobileNumber();
        return new SMSMessageContext(TEMPLATE_NAME, map, mobileNumber);
    }
}
