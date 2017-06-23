package org.egov.pgr.notification.domain.service.smsstrategy;

import org.egov.pgr.notification.domain.model.NotificationContext;
import org.egov.pgr.notification.domain.model.SMSMessageContext;
import org.trimou.util.ImmutableMap;

import java.util.Map;

public class UpdateDeliverableStatusCitizenSMSMessageStrategy implements SMSMessageStrategy {
    private static final String SERVICE_NAME = "serviceName";
    private static final String CRN = "crn";
    private static final String SERVICE_STATUS = "status";
    private static final String TEMPLATE_NAME = "sms_deliverable_service_status_updated";

    @Override
    public boolean matches(NotificationContext context) {
        return context.getServiceType().isDeliverableType()
            && context.getSevaRequest().isUpdate()
            && context.getSevaRequest().isEmployeeLoggedIn();
    }

    @Override
    public SMSMessageContext getMessageContext(NotificationContext context) {
        final Map<Object, Object> map = ImmutableMap.of(
            SERVICE_NAME, context.getServiceType().getName(),
            CRN, context.getSevaRequest().getCrn(),
            SERVICE_STATUS, context.getSevaRequest().getStatusName().toLowerCase()
        );
        final String mobileNumber = context.getSevaRequest().getMobileNumber();
        return new SMSMessageContext(TEMPLATE_NAME, map, mobileNumber);
    }
}

