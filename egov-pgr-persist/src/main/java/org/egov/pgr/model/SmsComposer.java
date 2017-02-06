package org.egov.pgr.model;

import org.egov.pgr.contracts.sms.SmsMessage;
import org.egov.pgr.entity.Complaint;
import org.trimou.engine.MustacheEngine;
import org.trimou.util.ImmutableMap;

public class SmsComposer {
    private Complaint complaint;
    private MustacheEngine templatingEngine;

    public SmsComposer(Complaint complaint, MustacheEngine templatingEngine) {
        this.complaint = complaint;
        this.templatingEngine = templatingEngine;
    }

    public SmsMessage compose() {
        return new SmsMessage(complaint.getComplainant().getEmail(), getSmsBody(complaint));
    }

    private String getSmsBody(Complaint complaint) {
        return templatingEngine
                .getMustache("sms_en")
                .render(ImmutableMap.<String, Object>of(
                        "name", complaint.getComplaintType().getName(),
                        "number", complaint.getCrn()
                ));
    }

}
