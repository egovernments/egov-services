package org.egov.pgr.write.model;

import org.egov.pgr.common.contract.GetUserByIdResponse;
import org.egov.pgr.common.entity.ReceivingCenter;
import org.egov.pgr.common.repository.UserRepository;
import org.egov.pgr.write.contracts.grievance.RequestInfo;
import org.egov.pgr.write.contracts.grievance.ServiceRequest;
import org.egov.pgr.write.contracts.grievance.SevaRequest;
import org.egov.pgr.write.entity.Complaint;
import org.egov.pgr.write.entity.ComplaintType;
import org.egov.pgr.write.entity.enums.ComplaintStatus;
import org.egov.pgr.write.repository.PositionRepository;
import org.egov.pgr.write.service.ComplaintStatusWriteService;
import org.egov.pgr.write.service.ComplaintTypeWriteService;
import org.egov.pgr.write.service.ReceivingCenterWriteService;
import org.egov.pgr.write.service.ReceivingModeWriteService;

import java.util.Date;
import java.util.Objects;

public class ComplaintBuilder {

    private final Long requesterId;
    private final ComplaintTypeWriteService complaintTypeWriteService;
    private final ComplaintStatusWriteService complaintStatusWriteService;
    private final PositionRepository positionRepository;
    private final UserRepository userWriteRepository;
    private final Complaint complaint;
    private final ServiceRequest serviceRequest;
    private final ReceivingCenterWriteService receivingCenterWriteService;
    private final RequestInfo requestInfo;
    private final ReceivingModeWriteService receivingModeWriteService;

    public ComplaintBuilder(Complaint complaint, SevaRequest sevaRequest,
                            ComplaintTypeWriteService complaintTypeWriteService,
                            ComplaintStatusWriteService complaintStatusWriteService,
                            PositionRepository positionRepository,
                            UserRepository userRepository,
                            ReceivingCenterWriteService receivingCenterWriteService,
                            ReceivingModeWriteService receivingModeWriteService) {
        this.requesterId = sevaRequest.getRequesterId();
        this.serviceRequest = sevaRequest.getServiceRequest();
        this.complaintStatusWriteService = complaintStatusWriteService;
        this.positionRepository = positionRepository;
        this.complaint = complaint == null ? new Complaint() : complaint;
        this.complaintTypeWriteService = complaintTypeWriteService;
        this.userWriteRepository = userRepository;
        this.receivingCenterWriteService = receivingCenterWriteService;
        this.requestInfo = sevaRequest.getRequestInfo();
        this.receivingModeWriteService = receivingModeWriteService;
    }

    public Complaint build() {
        setBasicInfo();
        setAuditableFields();
        setComplainant();
        setReceivingMode();
        setComplaintType();
        setComplaintStatus();
        setAssigneeId();
        setLocationDetails();
        setEscalationDate();
        setWorkflowDetails();
        setDepartmentId();
        setReceivingCenter();
        return this.complaint;
    }

    private void setAuditableFields() {
        if (this.complaint.getCreatedBy() == null)
            this.complaint.setCreatedBy(this.requesterId);
        if (this.complaint.getCreatedDate() == null)
            this.complaint.setCreatedDate(new Date());
        this.complaint.setLastModifiedDate(new Date());
        this.complaint.setLastModifiedBy(this.requesterId);
    }

    private void setDepartmentId() {
        if (null != this.complaint.getComplaintType() && null != this.complaint.getComplaintType().getDepartment())
            this.complaint.setDepartment(this.complaint.getComplaintType().getDepartment());
        else if (null != this.complaint.getAssignee()) {
            Long departmentId = positionRepository.departmentIdForAssignee(serviceRequest.getTenantId(),
                this.complaint.getAssignee());
            complaint.setDepartment(departmentId);
        }

    }

    private void setWorkflowDetails() {
        String stateId = this.serviceRequest.getValues().get(ServiceRequest.VALUES_STATE_ID);
        if (stateId != null) {
            Long stateIdAsLong = Long.valueOf(stateId);
            this.complaint.setStateId(stateIdAsLong);
        }
    }

    private void setBasicInfo() {
        this.complaint.setCrn(this.serviceRequest.getCrn());
        this.complaint.setLat(this.serviceRequest.getLat() == null ? 0.0 : this.serviceRequest.getLat());
        this.complaint.setLng(this.serviceRequest.getLng() == null ? 0.0 : this.serviceRequest.getLng());
        this.complaint.setDetails(this.serviceRequest.getDetails());
        this.complaint.setLandmarkDetails(this.serviceRequest.getLandmarkDetails());
    }

    private void setComplainant() {
        String userId;
        if (this.requestInfo.getAction().equals("POST")) {
            if ((userId = this.serviceRequest.getValues().get(ServiceRequest.USER_ID)) != null) {
                GetUserByIdResponse user = userWriteRepository.findUserById(Long.valueOf(userId));
                this.complaint.getComplainant().setUserDetail(Long.valueOf(userId));
                this.complaint.getComplainant().setName(user.getUser().get(0).getName());
                this.complaint.getComplainant().setMobile(user.getUser().get(0).getMobileNumber());
                this.complaint.getComplainant().setEmail(user.getUser().get(0).getEmailId());
                this.complaint.getComplainant().setAddress(user.getUser().get(0).getPermanentAddress());
            } else {
                this.complaint.getComplainant().setName(this.serviceRequest.getFirstName());
                this.complaint.getComplainant().setMobile(this.serviceRequest.getPhone());
                this.complaint.getComplainant().setEmail(this.serviceRequest.getEmail());
                this.complaint.getComplainant().setAddress(this.serviceRequest.getValues().get
                    (ServiceRequest.VALUES_COMPLAINANT_ADDRESS));
            }
        }
    }

    private void setEscalationDate() {
        this.complaint.setEscalationDate(this.serviceRequest.getEscalationDate());
    }

    private void setLocationDetails() {
        String locationId = this.serviceRequest.getValues().get(ServiceRequest.LOCATION_ID);
        String childLocationId = this.serviceRequest.getValues().get(ServiceRequest.CHILD_LOCATION_ID);
        String locationName = this.serviceRequest.getValues().get(ServiceRequest.LOCATION_NAME);
        if (Objects.nonNull(locationId))
            this.complaint.setLocation(Long.parseLong(locationId));
        if (Objects.nonNull(childLocationId))
            this.complaint.setChildLocation(Long.parseLong(childLocationId));
        if (Objects.nonNull(locationName))
            this.complaint.setLocationName(locationName);
    }

    private void setAssigneeId() {
        String assigneeId = this.serviceRequest.getValues().get(ServiceRequest.VALUES_ASSIGNEE_ID);
        if (Objects.nonNull(assigneeId))
            this.complaint.setAssignee(Long.parseLong(assigneeId));
    }

    private void setComplaintStatus() {
        ComplaintStatus statusName =
            ComplaintStatus.valueOf(serviceRequest.getValues().get(ServiceRequest.VALUES_STATUS));
        org.egov.pgr.write.entity.ComplaintStatus complainStatus = complaintStatusWriteService.getByName(statusName
            .toString());
        this.complaint.setStatus(complainStatus);

    }

    private void setReceivingMode() {
        String receivingMode = this.serviceRequest.getValues().get(ServiceRequest.VALUES_RECIEVING_MODE);
        if (receivingMode != null)
            this.complaint.setReceivingMode(receivingModeWriteService.getReceivingModeByCode(receivingMode
                .toUpperCase()));
    }

    private void setReceivingCenter() {
        String receivingCenter = this.serviceRequest.getValues().get(ServiceRequest.VALUES_RECEIVING_CENTER);
        if (receivingCenter != null && !receivingCenter.isEmpty()) {
            ReceivingCenter receivingCenterDB = receivingCenterWriteService
                .getReceivingCenterById(Long.valueOf(receivingCenter));
            this.complaint.setReceivingCenter(receivingCenterDB);
        }
    }

    private void setComplaintType() {
        if (Objects.isNull(serviceRequest.getComplaintTypeCode()))
            return;
        ComplaintType complaintType = this.complaintTypeWriteService.findByCode(serviceRequest.getComplaintTypeCode());
        complaint.setComplaintType(complaintType);
    }

}
