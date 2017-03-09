package org.egov.pgr.model;

import org.egov.pgr.contracts.grievance.ServiceRequest;
import org.egov.pgr.contracts.grievance.SevaRequest;
import org.egov.pgr.contracts.user.GetUserByIdResponse;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.ComplaintStatus;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.entity.enums.ReceivingMode;
import org.egov.pgr.repository.PositionRepository;
import org.egov.pgr.repository.UserRepository;
import org.egov.pgr.service.ComplaintStatusService;
import org.egov.pgr.service.ComplaintTypeService;
import org.egov.pgr.service.EscalationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Objects;

import static org.egov.pgr.contracts.grievance.ServiceRequest.*;

public class ComplaintBuilder {

    private final Long requesterId;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ComplaintTypeService complaintTypeService;
    private final ComplaintStatusService complaintStatusService;
    private final EscalationService escalationService;
    private final PositionRepository positionRepository;
    private final UserRepository userRepository;
    private final Complaint complaint;
    private final ServiceRequest serviceRequest;

    public ComplaintBuilder(Complaint complaint, SevaRequest sevaRequest,
                            ComplaintTypeService complaintTypeService, ComplaintStatusService complaintStatusService,
                            EscalationService escalationService, PositionRepository positionRepository,
                            UserRepository userRepository) {
        this.requesterId = sevaRequest.getRequesterId();
        this.serviceRequest = sevaRequest.getServiceRequest();
        this.complaintStatusService = complaintStatusService;
        this.escalationService = escalationService;
        this.positionRepository = positionRepository;
        this.complaint = complaint == null ? new Complaint() : complaint;
        this.complaintTypeService = complaintTypeService;
        this.userRepository = userRepository;
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
        return this.complaint;
    }

    private void setAuditableFields() {
        if (this.complaint.getCreatedBy() == null) this.complaint.setCreatedBy(this.requesterId);
        if (this.complaint.getCreatedDate() == null) this.complaint.setCreatedDate(new Date());
        this.complaint.setLastModifiedDate(new Date());
        this.complaint.setLastModifiedBy(this.requesterId);
    }

    private void setDepartmentId() {
        if (null != this.complaint.getComplaintType() && null != this.complaint.getComplaintType().getDepartment())
            this.complaint.setDepartment(this.complaint.getComplaintType().getDepartment());
        else if (null != this.complaint.getAssignee()) {
            Long departmentId = positionRepository.departmentIdForAssignee(serviceRequest.getTenantId(), this.complaint.getAssignee());
            complaint.setDepartment(departmentId);
        }

    }

    private void setWorkflowDetails() {
        String stateId = this.serviceRequest.getValues().get(VALUES_STATE_ID);
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
        if ((userId = this.serviceRequest.getValues().get(USER_ID)) != null) {
            GetUserByIdResponse user = userRepository.findUserById(Long.valueOf(userId));
            this.complaint.getComplainant().setUserDetail(Long.valueOf(userId));
            this.complaint.getComplainant().setName(user.getUser().get(0).getMobileNumber());
            this.complaint.getComplainant().setEmail(user.getUser().get(0).getEmailId());
            this.complaint.getComplainant().setAddress(user.getUser().get(0).getPermanentAddress());
        } else {
            this.complaint.getComplainant().setName(this.serviceRequest.getFirstName());
            this.complaint.getComplainant().setMobile(this.serviceRequest.getPhone());
            this.complaint.getComplainant().setEmail(this.serviceRequest.getEmail());
            this.complaint.getComplainant().setAddress(this.serviceRequest.getValues().get(VALUES_COMPLAINANT_ADDRESS));
        }
    }

    private void setEscalationDate() {
        this.complaint.setEscalationDate(new Date());
        Long designationId = positionRepository.designationIdForAssignee(serviceRequest.getTenantId(), this.complaint.getAssignee());
        this.complaint.setEscalationDate(escalationService.getExpiryDate(this.complaint, designationId));
    }

    private void setLocationDetails() {
        String locationId = this.serviceRequest.getValues().get(LOCATION_ID);
        String childLocationId = this.serviceRequest.getValues().get(CHILD_LOCATION_ID);
        String locationName = this.serviceRequest.getValues().get(LOCATION_NAME);
        if (Objects.nonNull(locationId))
            this.complaint.setLocation(Long.parseLong(locationId));
        if (Objects.nonNull(childLocationId))
            this.complaint.setChildLocation(Long.parseLong(childLocationId));
        if (Objects.nonNull(locationName))
            this.complaint.setLocationName(locationName);
    }

    private void setAssigneeId() {
        String assigneeId = this.serviceRequest.getValues().get(VALUES_ASSIGNEE_ID);
        if (Objects.nonNull(assigneeId))
            this.complaint.setAssignee(Long.parseLong(assigneeId));
    }

    private void setComplaintStatus() {
        try {
            org.egov.pgr.entity.enums.ComplaintStatus statusName = org.egov.pgr.entity.enums.ComplaintStatus
                    .valueOf(serviceRequest.getValues().get(VALUES_STATUS));
            ComplaintStatus complainStatus = complaintStatusService.getByName(statusName.toString());
            this.complaint.setStatus(complainStatus);
        } catch (IllegalArgumentException iae) {
            logger.error(iae.getMessage(), iae);
        }
    }

    private void setReceivingMode() {
        String receivingMode = this.serviceRequest.getValues().get(VALUES_RECIEVING_MODE);
        if (receivingMode != null)
            this.complaint.setReceivingMode(ReceivingMode.valueOf(receivingMode.toUpperCase()));

    }

    private void setComplaintType() {
        if (Objects.isNull(serviceRequest.getComplaintTypeCode()))
            return;
        ComplaintType complaintType = this.complaintTypeService.findByCode(serviceRequest.getComplaintTypeCode());
        complaint.setComplaintType(complaintType);
    }

}
