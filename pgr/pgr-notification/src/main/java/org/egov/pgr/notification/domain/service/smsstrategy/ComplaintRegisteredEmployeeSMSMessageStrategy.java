package org.egov.pgr.notification.domain.service.smsstrategy;

import org.egov.pgr.notification.domain.model.NotificationContext;
import org.egov.pgr.notification.domain.model.SMSMessageContext;
import org.trimou.util.ImmutableMap;

import java.util.Map;

public class ComplaintRegisteredEmployeeSMSMessageStrategy implements SMSMessageStrategy {
    private static final String NAME = "name";
    private static final String LOCATION_NAME = "locationName";
    private static final String COMPLAINANT_NAME = "complainantName";
    private static final String MOBILE_NUMBER = "mobileNumber";
    private static final String TEMPLATE_NAME = "sms_employee_complaint_registered";

    @Override
    public boolean matches(NotificationContext context) {
        return context.getServiceType().isComplaintType() && context.getSevaRequest().isCreate();
    }

    @Override
    public SMSMessageContext getMessageContext(NotificationContext context) {
        final Map<Object, Object> map = ImmutableMap.of(
            NAME, context.getServiceType().getName(),
            LOCATION_NAME, context.getLocation().getName(),
            COMPLAINANT_NAME, context.getSevaRequest().getRequesterName(),
            MOBILE_NUMBER, context.getSevaRequest().getMobileNumber()
        );
        final String mobileNumber = context.getEmployee().getMobileNumber();
        return new SMSMessageContext(TEMPLATE_NAME, map, mobileNumber);
    }
}
