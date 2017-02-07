package org.egov.pgr.model;

import org.egov.pgr.contracts.email.EmailMessage;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.service.TemplateService;
import org.trimou.util.ImmutableMap;

import java.text.SimpleDateFormat;
import java.util.Map;

public class EmailComposer {

    private static final String EMAIL_BODY_EN_TEMPLATE = "email_body_end";
    private static final String EMAIL_SUBJECT_EN_TEMPLATE = "email_subject_en";

    private Complaint complaint;
    private TemplateService templateService;

    public EmailComposer(Complaint complaint, TemplateService templateService) {
        this.complaint = complaint;
        this.templateService = templateService;
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
        return templateService.loadByName(EMAIL_BODY_EN_TEMPLATE, builder);
    }

    private String getEmailSubject(Complaint complaint) {
        Map<Object, Object> objectMap = ImmutableMap.of("crn", complaint.getCrn());
        return templateService.loadByName(EMAIL_SUBJECT_EN_TEMPLATE, objectMap);
    }

}
