package org.egov.pgrrest.write.repository;

import org.apache.commons.lang3.StringUtils;
import org.egov.pgrrest.common.entity.*;
import org.egov.pgrrest.common.entity.enums.CitizenFeedback;
import org.egov.pgrrest.common.repository.*;
import org.egov.pgrrest.write.model.ServiceRequestRecord;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ServiceRequestWriteRepository {

    private ComplaintJpaRepository complaintJpaRepository;
    private ReceivingModeJpaRepository receivingModeRepository;
    private ReceivingCenterJpaRepository receivingCenterRepository;
    private ServiceRequestTypeJpaRepository serviceRequestTypeJpaRepository;
    private SubmissionJpaRepository submissionJpaRepository;
    private SubmissionAttributeJpaRepository submissionAttributeJpaRepository;


    public ServiceRequestWriteRepository(ComplaintJpaRepository complaintJpaRepository,
                                         ReceivingModeJpaRepository receivingModeRepository,
                                         ReceivingCenterJpaRepository receivingCenterRepository,
                                         ServiceRequestTypeJpaRepository serviceRequestTypeJpaRepository,
                                         SubmissionJpaRepository submissionJpaRepository,
                                         SubmissionAttributeJpaRepository submissionAttributeJpaRepository) {
        this.complaintJpaRepository = complaintJpaRepository;
        this.receivingModeRepository = receivingModeRepository;
        this.receivingCenterRepository = receivingCenterRepository;
        this.serviceRequestTypeJpaRepository = serviceRequestTypeJpaRepository;
        this.submissionJpaRepository = submissionJpaRepository;
        this.submissionAttributeJpaRepository = submissionAttributeJpaRepository;
    }

    public void updateOrInsert(ServiceRequestRecord serviceRequestRecord) {
        createOrUpdateComplaint(serviceRequestRecord);
        createOrUpdateSubmission(serviceRequestRecord);
    }

    private void createOrUpdateSubmission(ServiceRequestRecord serviceRequestRecord) {
        final Submission submission = getSubmission(serviceRequestRecord);
        setBasicInfo(serviceRequestRecord, submission);
        setAuditFields(serviceRequestRecord, submission);
        setComplainant(serviceRequestRecord, submission);
        setComplaintType(serviceRequestRecord, submission);
        setComplaintStatus(serviceRequestRecord, submission);
        setAssigneeId(serviceRequestRecord, submission);
        setEscalationDate(serviceRequestRecord, submission);
        setDepartmentId(serviceRequestRecord, submission);
        saveSubmission(submission);
        saveOrUpdateSubmissionAttributes(serviceRequestRecord);
    }

    private void saveOrUpdateSubmissionAttributes(ServiceRequestRecord record) {
        final Map<String, List<SubmissionAttribute>> keyToAttributeListMap = getSubmissionAttributes(record);
        createOrUpdateAttributes(record, keyToAttributeListMap);
        deleteAttributes(keyToAttributeListMap);
    }

    private void deleteAttributes(Map<String, List<SubmissionAttribute>> keyToAttributeListMap) {
        final List<SubmissionAttribute> attributesToDelete = getAttributesToDelete(keyToAttributeListMap);
        submissionAttributeJpaRepository.delete(attributesToDelete);
    }

    private void createOrUpdateAttributes(ServiceRequestRecord record,
                                          Map<String, List<SubmissionAttribute>> keyToAttributeListMap) {
        record.getAttributeEntries().forEach(a -> {
            final SubmissionAttributeKey attributeKey =
                new SubmissionAttributeKey(a.getCode(), a.getKey(), record.getCRN(), record.getTenantId());
            SubmissionAttribute attribute = findAttribute(attributeKey, keyToAttributeListMap);
            updateAuditFields(record, attribute);
            submissionAttributeJpaRepository.save(attribute);
        });
    }

    private void updateAuditFields(ServiceRequestRecord record, SubmissionAttribute attribute) {
        if (attribute.getCreatedBy() == null)
            attribute.setCreatedBy(record.getCreatedBy());
        if (attribute.getCreatedDate() == null)
            attribute.setCreatedDate(new Date());
        attribute.setLastModifiedDate(new Date());
        attribute.setLastModifiedBy(record.getLastModifiedBy());
    }

    private List<SubmissionAttribute> getAttributesToDelete(Map<String, List<SubmissionAttribute>>
                                                                keyToAttributeListMap) {
        return keyToAttributeListMap.values().stream()
            .flatMap(List::stream)
            .filter(attribute -> !attribute.isRetained())
            .collect(Collectors.toList());
    }

    private SubmissionAttribute findAttribute(SubmissionAttributeKey key,
                                              Map<String, List<SubmissionAttribute>> keyToAttributeListMap) {
        final List<SubmissionAttribute> matchingAttributes = keyToAttributeListMap.get(key.getKey());
        if (matchingAttributes == null) {
            return new SubmissionAttribute(true, key);
        }
        final SubmissionAttribute matchingAttribute = matchingAttributes.stream()
            .filter(a -> key.equals(a.getId()))
            .findFirst()
            .orElse(new SubmissionAttribute(true, key));
        matchingAttribute.setRetained(true);
        return matchingAttribute;
    }

    private Map<String, List<SubmissionAttribute>> getSubmissionAttributes(ServiceRequestRecord record) {
        return submissionAttributeJpaRepository
            .findByCrnAndTenantId(record.getCRN(), record.getTenantId())
            .stream().collect(Collectors.groupingBy(SubmissionAttribute::getKey));
    }

    private void createOrUpdateComplaint(ServiceRequestRecord serviceRequestRecord) {
        final Complaint complaint = getComplaint(serviceRequestRecord);
        setBasicInfo(serviceRequestRecord, complaint);
        setAuditFields(serviceRequestRecord, complaint);
        setComplainant(serviceRequestRecord, complaint);
        setReceivingMode(serviceRequestRecord, complaint);
        setReceivingCenter(serviceRequestRecord, complaint);
        setComplaintType(serviceRequestRecord, complaint);
        setComplaintStatus(serviceRequestRecord, complaint);
        setAssigneeId(serviceRequestRecord, complaint);
        setLocationDetails(serviceRequestRecord, complaint);
        setEscalationDate(serviceRequestRecord, complaint);
        setWorkflowDetails(serviceRequestRecord, complaint);
        setDepartmentId(serviceRequestRecord, complaint);
        setCitizenFeedBack(serviceRequestRecord, complaint);
        saveComplaint(complaint);
    }

    private void saveComplaint(Complaint complaint) {
        complaintJpaRepository.save(complaint);
    }

    private void saveSubmission(Submission submission) {
        submissionJpaRepository.save(submission);
    }

    private void setDepartmentId(ServiceRequestRecord serviceRequestRecord, Complaint complaint) {
        complaint.setDepartment(serviceRequestRecord.getDepartment());
    }

    private void setDepartmentId(ServiceRequestRecord serviceRequestRecord, Submission submission) {
        submission.setDepartment(serviceRequestRecord.getDepartment());
    }

    private void setCitizenFeedBack(ServiceRequestRecord serviceRequestRecord, Complaint complaint) {
        if (serviceRequestRecord.getCitizenFeedback() != null)
            complaint.setCitizenFeedback(CitizenFeedback.valueOf(serviceRequestRecord.getCitizenFeedback()));
    }

    private void setWorkflowDetails(ServiceRequestRecord serviceRequestRecord, Complaint complaint) {
        if (serviceRequestRecord.getWorkflowStateId() != null) {
            complaint.setStateId(serviceRequestRecord.getWorkflowStateId());
        }
    }

    private void setEscalationDate(ServiceRequestRecord serviceRequestRecord, Complaint complaint) {
        complaint.setEscalationDate(serviceRequestRecord.getEscalationDate());
    }

    private void setEscalationDate(ServiceRequestRecord serviceRequestRecord, Submission submission) {
        submission.setEscalationDate(serviceRequestRecord.getEscalationDate());
    }

    private void setLocationDetails(ServiceRequestRecord serviceRequestRecord, Complaint complaint) {
        if (serviceRequestRecord.getLocation() != null)
            complaint.setLocation(serviceRequestRecord.getLocation());
        if (serviceRequestRecord.getChildLocation() != null)
            complaint.setChildLocation(serviceRequestRecord.getChildLocation());
    }

    private void setAssigneeId(ServiceRequestRecord serviceRequestRecord, Complaint complaint) {
        complaint.setAssignee(serviceRequestRecord.getAssigneeId());
    }

    private void setAssigneeId(ServiceRequestRecord serviceRequestRecord, Submission submission) {
        submission.setAssignee(serviceRequestRecord.getAssigneeId());
    }

    private void setComplaintStatus(ServiceRequestRecord serviceRequestRecord, Complaint complaint) {
        complaint.setStatus(serviceRequestRecord.getServiceRequestStatus());
    }

    private void setComplaintStatus(ServiceRequestRecord serviceRequestRecord, Submission submission) {
        submission.setStatus(serviceRequestRecord.getServiceRequestStatus());
    }

    private void setComplaintType(ServiceRequestRecord serviceRequestRecord, Complaint complaint) {
        ServiceType complaintType = serviceRequestTypeJpaRepository
            .findByCodeAndTenantId(serviceRequestRecord.getServiceRequestTypeCode(), serviceRequestRecord.getTenantId());
        complaint.setComplaintType(complaintType);
    }

    private void setComplaintType(ServiceRequestRecord serviceRequestRecord, Submission submission) {
        submission.setServiceCode(serviceRequestRecord.getServiceRequestTypeCode());
    }

    private void setReceivingMode(ServiceRequestRecord serviceRequestRecord, Complaint complaint) {
        if (StringUtils.isNotEmpty(serviceRequestRecord.getReceivingMode())) {
            final String receivingModeInUpperCase = serviceRequestRecord.getReceivingMode().toUpperCase();
            complaint.setReceivingMode(receivingModeRepository.findByCode(receivingModeInUpperCase));
        }
    }

    private void setReceivingCenter(ServiceRequestRecord serviceRequestRecord, Complaint complaint) {
        if (serviceRequestRecord.getReceivingCenter() != null) {
            ReceivingCenter receivingCenterDB = receivingCenterRepository
                .findByIdAndTenantId(serviceRequestRecord.getReceivingCenter(), serviceRequestRecord.getTenantId());
            complaint.setReceivingCenter(receivingCenterDB);
        }
    }

    private void setBasicInfo(ServiceRequestRecord serviceRequestRecord, Complaint complaint) {
        complaint.setCrn(serviceRequestRecord.getCRN());
        complaint.setLatitude(serviceRequestRecord.getLatitude());
        complaint.setLongitude(serviceRequestRecord.getLongitude());
        complaint.setDetails(serviceRequestRecord.getDescription());
        complaint.setLandmarkDetails(serviceRequestRecord.getLandmarkDetails());
        complaint.setTenantId(serviceRequestRecord.getTenantId());
    }

    private void setBasicInfo(ServiceRequestRecord serviceRequestRecord, Submission submission) {
        submission.setId(new SubmissionKey(serviceRequestRecord.getCRN(), serviceRequestRecord.getTenantId()));
        submission.setLatitude(serviceRequestRecord.getLatitude());
        submission.setLongitude(serviceRequestRecord.getLongitude());
        submission.setDetails(serviceRequestRecord.getDescription());
        submission.setLandmarkDetails(serviceRequestRecord.getLandmarkDetails());
    }

    private void setAuditFields(ServiceRequestRecord serviceRequestRecord, Complaint complaint) {
        if (complaint.getCreatedBy() == null)
            complaint.setCreatedBy(serviceRequestRecord.getCreatedBy());
        if (complaint.getCreatedDate() == null)
            complaint.setCreatedDate(new Date());
        complaint.setLastModifiedDate(new Date());
        complaint.setLastModifiedBy(serviceRequestRecord.getLastModifiedBy());
    }

    private void setAuditFields(ServiceRequestRecord serviceRequestRecord, Submission submission) {
        if (submission.getCreatedBy() == null)
            submission.setCreatedBy(serviceRequestRecord.getCreatedBy());
        if (submission.getCreatedDate() == null)
            submission.setCreatedDate(serviceRequestRecord.getCreatedDate());
        submission.setLastModifiedDate(serviceRequestRecord.getLastModifiedDate());
        submission.setLastModifiedBy(serviceRequestRecord.getLastModifiedBy());
    }

    private void setComplainant(ServiceRequestRecord serviceRequestRecord, Complaint complaint) {
        if (complaint.getId() == null) {
            complaint.getComplainant().setUserDetail(serviceRequestRecord.getComplainantUserId());
        }
        complaint.getComplainant().setName(serviceRequestRecord.getRequesterName());
        complaint.getComplainant().setMobile(serviceRequestRecord.getRequesterMobileNumber());
        complaint.getComplainant().setEmail(serviceRequestRecord.getRequesterEmail());
        complaint.getComplainant().setAddress(serviceRequestRecord.getRequesterAddress());
        complaint.getComplainant().setTenantId(serviceRequestRecord.getTenantId());
    }

    private void setComplainant(ServiceRequestRecord serviceRequestRecord, Submission submission) {
        submission.setLoggedInRequester(serviceRequestRecord.getComplainantUserId());
        submission.setName(serviceRequestRecord.getRequesterName());
        submission.setMobile(serviceRequestRecord.getRequesterMobileNumber());
        submission.setEmail(serviceRequestRecord.getRequesterEmail());
        submission.setRequesterAddress(serviceRequestRecord.getRequesterAddress());
    }

    private Complaint getComplaint(ServiceRequestRecord serviceRequestRecord) {
        final Complaint existingComplaint = complaintJpaRepository.findByCrnAndTenantId(serviceRequestRecord.getCRN(),
            serviceRequestRecord.getTenantId());
        return existingComplaint == null ? new Complaint() : existingComplaint;
    }

    private Submission getSubmission(ServiceRequestRecord serviceRequestRecord) {
        final Submission existingSubmission = submissionJpaRepository
            .findOne(new SubmissionKey(serviceRequestRecord.getCRN(), serviceRequestRecord.getTenantId()));
        return existingSubmission == null ? new Submission() : existingSubmission;
    }
}
