package org.egov.pgr.model;

import org.egov.pgr.contracts.sms.SmsMessage;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.service.TemplateService;
import org.trimou.engine.MustacheEngine;
import org.trimou.util.ImmutableMap;

public class SmsComposer {
    private Complaint complaint;
    private TemplateService templateService;

    public SmsComposer(Complaint complaint, TemplateService templateService) {
        this.complaint = complaint;
        this.templateService = templateService;
    }

    public SmsMessage compose() {
        return new SmsMessage(complaint.getComplainant().getMobile(), getSmsBody(complaint));
    }

    private String getSmsBody(Complaint complaint) {
        return templateService.loadByName("sms_en", ImmutableMap.of(
                "name", complaint.getComplaintType().getName(),
                "number", complaint.getCrn()
        ));
    }

}
