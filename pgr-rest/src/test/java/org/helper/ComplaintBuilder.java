package org.helper;

import org.egov.pgr.entity.Complainant;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.ComplaintStatus;
import org.egov.pgr.entity.ComplaintType;

public class ComplaintBuilder {
    private Complaint complaint;

    public ComplaintBuilder() {
        this.complaint = new Complaint();
    }

    public ComplaintBuilder crn(String crn) {
        this.complaint.setCrn(crn);
        return this;
    }

    public ComplaintBuilder complaintStatus(org.egov.pgr.entity.enums.ComplaintStatus status) {
        ComplaintStatus complaintStatus = new ComplaintStatus();
        complaintStatus.setName(String.valueOf(status));
        this.complaint.setStatus(complaintStatus);
        return this;
    }

    public ComplaintBuilder complaintType(String type) {
        ComplaintType complaintType = new ComplaintType();
        complaintType.setName(type);
        this.complaint.setComplaintType(complaintType);
        return this;
    }

    public ComplaintBuilder complainant(String name) {
        Complainant complainant = new Complainant();
        complainant.setName(name);
        this.complaint.setComplainant(complainant);
        return this;
    }

    public Complaint buildComplaint() {
        return this.complaint;
    }
}
