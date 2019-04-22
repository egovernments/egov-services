package org.egov.tl.service.notification;

import org.egov.tl.util.NotificationUtil;
import org.egov.tl.web.models.Difference;
import org.egov.tl.web.models.SMSRequest;
import org.egov.tl.web.models.TradeLicense;
import org.egov.tl.web.models.TradeLicenseRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class EditNotificationService {


    private NotificationUtil util;


    @Autowired
    public EditNotificationService(NotificationUtil util) {
        this.util = util;
    }

    public void sendEditNotification(TradeLicenseRequest request, Map<String, Difference> diffMap) {
        List<SMSRequest> smsRequests = enrichSMSRequest(request, diffMap);
        util.sendSMS(smsRequests);
    }

    /**
     * Creates smsRequest for edits done in update
     * @param request The update Request
     * @param diffMap The map of id to Difference for each license
     * @return The smsRequest
     */
    private List<SMSRequest> enrichSMSRequest(TradeLicenseRequest request, Map<String, Difference> diffMap) {
        List<SMSRequest> smsRequests = new LinkedList<>();
        String tenantId = request.getLicenses().get(0).getTenantId();
        String localizationMessages = util.getLocalizationMessages(tenantId, request.getRequestInfo());
        for (TradeLicense license : request.getLicenses()) {
            Difference diff = diffMap.get(license.getId());
            String message = util.getCustomizedMsg(diff, license, localizationMessages);
            if (message == null) continue;

            Map<String, String> mobileNumberToOwner = new HashMap<>();

            license.getTradeLicenseDetail().getOwners().forEach(owner -> {
                if (owner.getMobileNumber() != null)
                    mobileNumberToOwner.put(owner.getMobileNumber(), owner.getName());
            });
            smsRequests.addAll(util.createSMSRequest(message, mobileNumberToOwner));
        }
        return smsRequests;
    }

}
