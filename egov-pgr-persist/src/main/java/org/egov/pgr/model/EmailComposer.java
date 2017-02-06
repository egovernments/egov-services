package org.egov.pgr.model;

import org.egov.pgr.contracts.email.EmailMessage;
import org.egov.pgr.entity.Complaint;
import org.trimou.engine.MustacheEngine;
import org.trimou.util.ImmutableMap;

import java.text.SimpleDateFormat;

public class EmailComposer {

    private Complaint complaint;
    private MustacheEngine templatingEngine;

    public EmailComposer(Complaint complaint, MustacheEngine templatingEngine) {
        this.complaint = complaint;
        this.templatingEngine = templatingEngine;
    }

    public EmailMessage compose() {
        return new EmailMessage(complaint.getComplainant().getEmail(), getEmailSubject(complaint), getEmailBody(complaint), null);
    }

    private String getEmailBody(Complaint complaint) {
        final String formattedCreatedDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(complaint.getCreatedDate());
        ImmutableMap.ImmutableMapBuilder<Object, Object> builder = ImmutableMap.builder();
        builder.put("complainantName", complaint.getComplainant().getName());
        builder.put("crn", complaint.getCrn());
        builder.put("complaintType", complaint.getComplaintType().getName());
        builder.put("locationName", complaint.getLocationName());
        builder.put("complaintDetails", complaint.getDetails());
        builder.put("registeredDate", formattedCreatedDate);
        builder.put("statusName", complaint.getStatus().getName());
        return templatingEngine.getMustache("email_body_en").render(builder.build());
    }

    private String getEmailSubject(Complaint complaint) {
        return templatingEngine
                .getMustache("email_subject_en")
                .render(ImmutableMap.<String, Object>of(
                        "crn", complaint.getCrn()
                ));
    }

}
