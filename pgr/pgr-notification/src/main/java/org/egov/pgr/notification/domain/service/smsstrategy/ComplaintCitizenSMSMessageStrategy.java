package org.egov.pgr.notification.domain.service.smsstrategy;

import org.egov.pgr.notification.domain.model.NotificationContext;
import org.egov.pgr.notification.domain.model.SMSMessageContext;
import org.trimou.util.ImmutableMap;

import java.util.Map;

public class ComplaintCitizenSMSMessageStrategy implements SMSMessageStrategy {
    private static final String NAME = "name";
    private static final String NUMBER = "number";
    private static final String STATUS = "status";
    private static final String SMS_COMPLAINT_SERVICE_REQUEST = "sms_complaint_service_request";

    @Override
    public boolean matches(NotificationContext context) {
        return context.getServiceType().isComplaintType() && !context.getSevaRequest().isEscalated();
    }

    @Override
    public SMSMessageContext getMessageContext(NotificationContext context) {
        final Map<Object, Object> map = ImmutableMap.of(
            NAME, context.getServiceType().getName(),
            NUMBER, context.getSevaRequest().getCrn(),
            STATUS, context.getSevaRequest().getStatusName().toLowerCase()
        );
        final String mobileNumber = context.getSevaRequest().getMobileNumber();
        return new SMSMessageContext(SMS_COMPLAINT_SERVICE_REQUEST, map, mobileNumber);
    }
}




