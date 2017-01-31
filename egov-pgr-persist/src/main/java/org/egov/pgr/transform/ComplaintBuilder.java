package org.egov.pgr.transform;

import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.ComplaintStatus;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.entity.enums.ReceivingMode;
import org.egov.pgr.model.ServiceRequest;
import org.egov.pgr.service.ComplaintStatusService;
import org.egov.pgr.service.ComplaintTypeService;

import java.util.Objects;

public class ComplaintBuilder {

    private final ComplaintTypeService complaintTypeService;
    private final ServiceRequest serviceRequest;
    private final ComplaintStatusService complaintStatusService;
    private final Complaint complaint;

    public ComplaintBuilder(ServiceRequest serviceRequest, ComplaintTypeService complaintTypeService, ComplaintStatusService complaintStatusService) {
        this.serviceRequest = serviceRequest;
        this.complaintStatusService = complaintStatusService;
        this.complaint = new Complaint();
        this.complaintTypeService = complaintTypeService;
    }

    public Complaint build() {
        this.complaint.getComplainant().setName(this.serviceRequest.getFirstName());
        this.complaint.getComplainant().setMobile(this.serviceRequest.getPhone());
        this.complaint.getComplainant().setEmail(this.serviceRequest.getEmail());
        setReceivingMode();
        setComplaintType();
        setComplaintStatus();
        setAssigneeId();
        setLocationDetails();
        return this.complaint;
    }

    private void setLocationDetails() {
        String locationId = this.serviceRequest.getValues().get("LOCATION_ID");
        String childLocationId = this.serviceRequest.getValues().get("CHILD_LOCATION_ID");
        if (Objects.nonNull(locationId)) this.complaint.setLocation(Long.parseLong(locationId));
        if (Objects.nonNull(childLocationId)) this.complaint.setLocation(Long.parseLong(childLocationId));
    }

    private void setAssigneeId() {
        String assigneeId = this.serviceRequest.getValues().get("ASSIGNEE_ID");
        if (Objects.nonNull(assigneeId)) this.complaint.setAssignee(Long.parseLong(assigneeId));
    }

    private void setComplaintStatus() {
        String statusName = org.egov.pgr.entity.enums.ComplaintStatus.REGISTERED.name();
        ComplaintStatus complainStatus = complaintStatusService.getByName(statusName);
        this.complaint.setStatus(complainStatus);
    }

    private void setReceivingMode() {
        //TODO - Set this correctly
        complaint.setReceivingMode(ReceivingMode.MOBILE);
    }

    private void setComplaintType() {
        if (Objects.isNull(serviceRequest.getComplaintTypeCode())) return;
        ComplaintType complaintType = this.complaintTypeService.findBy(Long.valueOf(serviceRequest.getComplaintTypeCode()));
        complaint.setComplaintType(complaintType);
    }


}
