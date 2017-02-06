package org.egov.pgr.factory;

import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.enums.ComplaintStatus;
import org.egov.pgr.model.ServiceRequest;

public class ServiceRequestFactory {
    public static ServiceRequest createFromComplaint(Complaint complaint) {
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setCrn(complaint.getCrn());
        serviceRequest.setStatus(complaint.isCompleted());
        serviceRequest.setStatusDetails(ComplaintStatus.valueOf(complaint.getStatus().getName()));
        serviceRequest.setComplaintTypeName(complaint.getComplaintType().getName());
        serviceRequest.setComplaintTypeCode(complaint.getComplaintType().getCode());
        serviceRequest.setComplaintTypeId(String.valueOf(complaint.getComplaintType().getId()));
        serviceRequest.setDetails(complaint.getDetails());
        serviceRequest.setCreatedDate(complaint.getCreatedDate());
        serviceRequest.setLastModifiedDate(complaint.getLastModifiedDate());
        serviceRequest.setEscalationDate(complaint.getEscalationDate());
        serviceRequest.setLandmarkDetails(complaint.getLandmarkDetails());
        serviceRequest.setCrossHierarchyId(String.valueOf(complaint.getCrossHierarchyId()));
        serviceRequest.setLat(complaint.getLat());
        serviceRequest.setLng(complaint.getLng());
        serviceRequest.setFirstName(complaint.getComplainant().getName());
        serviceRequest.setLastName(complaint.getComplainant().getName());
        serviceRequest.setPhone(complaint.getComplainant().getMobile());
        serviceRequest.setEmail(complaint.getComplainant().getEmail());
        return serviceRequest;
    }
}
