package org.egov.domain.service;

import org.egov.domain.model.SMSMessageContext;
import org.egov.domain.model.ServiceType;
import org.egov.domain.model.SevaRequest;
import org.egov.domain.model.Tenant;
import org.trimou.util.ImmutableMap;

import java.util.Map;

public class UpdateDeliverableStatusSMSMessageStrategy implements SMSMessageStrategy {
    private static final String SERVICE_NAME = "serviceName";
    private static final String CRN = "crn";
    private static final String SERVICE_STATUS = "status";
    private static final String TEMPLATE_NAME = "sms_deliverable_service_status_updated";

    @Override
    public boolean matches(SevaRequest sevaRequest, ServiceType serviceType) {
        return serviceType.isDeliverableType()
            && sevaRequest.isUpdate()
            && sevaRequest.isEmployeeLoggedIn();
    }

    @Override
    public SMSMessageContext getMessageContext(SevaRequest sevaRequest, ServiceType serviceType, Tenant tenant) {
        final Map<Object, Object> map = ImmutableMap.of(
            SERVICE_NAME, serviceType.getName(),
            CRN, sevaRequest.getCrn(),
            SERVICE_STATUS, sevaRequest.getStatusName().toLowerCase()
        );
        return new SMSMessageContext(TEMPLATE_NAME, map);
    }
}

