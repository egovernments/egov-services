package org.egov.pgr.model;

import org.egov.pgr.contracts.email.EmailMessage;
import org.egov.pgr.entity.Complaint;
import org.trimou.engine.MustacheEngine;

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
        //TODO - Get boundary name by id
        String locationName = "";
        final String formattedCreatedDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(complaint.getCreatedDate());
        final StringBuffer emailBody = new StringBuffer().append("Dear ")
                .append(complaint.getComplainant().getName())
                .append(",\n \n \tThank you for registering a grievance (")
                .append(complaint.getCrn())
                .append("). Your grievance is registered successfully.\n \tPlease use this number for all future references.")
                .append("\n \n Grievance Details - \n \n Complaint type - ")
                .append(complaint.getComplaintType().getName());
        if (complaint.getLocation() != null)
            emailBody.append(" \n Location details - ").append(locationName);
        emailBody.append("\n Grievance description - ")
                .append(complaint.getDetails())
                .append("\n Grievance status -")
                .append(complaint.getStatus().getName())
                .append("\n Grievance Registration Date - ")
                .append(formattedCreatedDate);
        return emailBody.toString();
    }

    private String getEmailSubject(Complaint complaint) {
        return new StringBuffer()
                .append("Registered Grievance -")
                .append(complaint.getCrn())
                .append(" successfuly").toString();
    }

}
