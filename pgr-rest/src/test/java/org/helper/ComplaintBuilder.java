package org.helper;

import org.egov.pgr.persistence.entity.*;
import org.egov.pgr.persistence.entity.enums.ReceivingMode;

public class ComplaintBuilder {
    private Complaint complaint;

    public ComplaintBuilder() {
        this.complaint = new Complaint();
    }

    public ComplaintBuilder crn(String crn) {
        this.complaint.setCrn(crn);
        return this;
    }

    public ComplaintBuilder complaintStatus(org.egov.pgr.persistence.entity.enums.ComplaintStatus status) {
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

    public ComplaintBuilder receivingMode(String mode) {
        this.complaint.setReceivingMode(ReceivingMode.valueOf(mode));
        return this;
    }

    public ComplaintBuilder receivingCenter(String center) {
        ReceivingCenter receivingCenter = new ReceivingCenter();
        receivingCenter.setName(center);
        this.complaint.setReceivingCenter(receivingCenter);
        return this;
    }

    public ComplaintBuilder locationId(String name) {
        Boundary boundary = new Boundary();
        boundary.setName(name);
        this.complaint.setLocation(boundary);
        return this;
    }

    public ComplaintBuilder childLocationId(String name) {
        Boundary boundary = new Boundary();
        boundary.setName(name);
        this.complaint.setChildLocation(boundary);
        return this;
    }

    public Complaint buildComplaint() {
        return this.complaint;
    }
}
