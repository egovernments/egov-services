package org.egov.domain.service;

import org.egov.domain.model.SMSMessageContext;
import org.egov.domain.model.ServiceType;
import org.egov.domain.model.SevaRequest;
import org.egov.domain.model.Tenant;
import org.trimou.util.ImmutableMap;

import java.util.Map;

public class ProcessingFeeEnteredSMSMessageStrategy implements SMSMessageStrategy {
    private static final String SERVICE_NAME = "serviceName";
    private static final String CRN = "crn";
    private static final String FEE = "fee";
    private static final String ULB_GRADE = "ulbGrade";
    private static final String TEMPLATE_NAME = "sms_deliverable_service_processing_fee_entered";

    @Override
    public boolean matches(SevaRequest sevaRequest, ServiceType serviceType) {
        return serviceType.isDeliverableType()
            && sevaRequest.isProcessingFeePresent()
            && sevaRequest.isInProgress()
            && sevaRequest.isEmployeeLoggedIn();
    }

    @Override
    public SMSMessageContext getMessageContext(SevaRequest sevaRequest, ServiceType serviceType, Tenant tenant) {
        final Map<Object, Object> map = ImmutableMap.of(
            SERVICE_NAME, serviceType.getName(),
            CRN, sevaRequest.getCrn(),
            FEE, sevaRequest.getProcessingFee(),
            ULB_GRADE, tenant.getUlbGrade()
        );
        return new SMSMessageContext(TEMPLATE_NAME, map);
    }
}
