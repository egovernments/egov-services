package org.egov.pgr.notification.domain.service.smsstrategy;

import org.egov.pgr.notification.domain.model.NotificationContext;
import org.egov.pgr.notification.domain.model.SMSMessageContext;
import org.trimou.util.ImmutableMap;

import java.util.Map;

public class ProcessingFeeEnteredCitizenSMSMessageStrategy implements SMSMessageStrategy {
    private static final String SERVICE_NAME = "serviceName";
    private static final String CRN = "crn";
    private static final String FEE = "fee";
    private static final String ULB_GRADE = "ulbGrade";
    private static final String TEMPLATE_NAME = "sms_deliverable_service_processing_fee_entered";

    @Override
    public boolean matches(NotificationContext context) {
        return context.getServiceType().isDeliverableType()
            && context.getSevaRequest().isProcessingFeePresent()
            && context.getSevaRequest().isInProgress()
            && context.getSevaRequest().isEmployeeLoggedIn();
    }

    @Override
    public SMSMessageContext getMessageContext(NotificationContext context) {
        final Map<Object, Object> map = ImmutableMap.of(
            SERVICE_NAME, context.getServiceType().getName(),
            CRN, context.getSevaRequest().getCrn(),
            FEE, context.getSevaRequest().getProcessingFee(),
            ULB_GRADE, context.getTenant().getUlbGrade()
        );
        final String mobileNumber = context.getSevaRequest().getMobileNumber();
        return new SMSMessageContext(TEMPLATE_NAME, map, mobileNumber);
    }
}
