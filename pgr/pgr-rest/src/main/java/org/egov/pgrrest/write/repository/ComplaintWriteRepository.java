package org.egov.pgrrest.write.repository;

import org.apache.commons.lang3.StringUtils;
import org.egov.pgrrest.common.entity.*;
import org.egov.pgrrest.common.entity.enums.CitizenFeedback;
import org.egov.pgrrest.common.repository.*;
import org.egov.pgrrest.write.model.ComplaintRecord;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ComplaintWriteRepository {

    private ComplaintJpaRepository complaintJpaRepository;
    private ReceivingModeJpaRepository receivingModeRepository;
    private ReceivingCenterJpaRepository receivingCenterRepository;
    private ComplaintTypeJpaRepository complaintTypeJpaRepository;
    private SubmissionJpaRepository submissionJpaRepository;
    private SubmissionAttributeJpaRepository submissionAttributeJpaRepository;


    public ComplaintWriteRepository(ComplaintJpaRepository complaintJpaRepository,
                                    ReceivingModeJpaRepository receivingModeRepository,
                                    ReceivingCenterJpaRepository receivingCenterRepository,
                                    ComplaintTypeJpaRepository complaintTypeJpaRepository,
                                    SubmissionJpaRepository submissionJpaRepository,
                                    SubmissionAttributeJpaRepository submissionAttributeJpaRepository) {
        this.complaintJpaRepository = complaintJpaRepository;
        this.receivingModeRepository = receivingModeRepository;
        this.receivingCenterRepository = receivingCenterRepository;
        this.complaintTypeJpaRepository = complaintTypeJpaRepository;
        this.submissionJpaRepository = submissionJpaRepository;
        this.submissionAttributeJpaRepository = submissionAttributeJpaRepository;
    }

    public void updateOrInsert(ComplaintRecord complaintRecord) {
        createOrUpdateComplaint(complaintRecord);
        createOrUpdateSubmission(complaintRecord);
    }

    private void createOrUpdateSubmission(ComplaintRecord complaintRecord) {
        final Submission submission = getSubmission(complaintRecord);
        setBasicInfo(complaintRecord, submission);
        setAuditFields(complaintRecord, submission);
        setComplainant(complaintRecord, submission);
        setComplaintType(complaintRecord, submission);
        setComplaintStatus(complaintRecord, submission);
        setAssigneeId(complaintRecord, submission);
        setEscalationDate(complaintRecord, submission);
        setDepartmentId(complaintRecord, submission);
        saveSubmission(submission);
        saveOrUpdateSubmissionAttributes(complaintRecord);
    }

    private void saveOrUpdateSubmissionAttributes(ComplaintRecord record) {
        final Map<String, List<SubmissionAttribute>> keyToAttributeListMap = getSubmissionAttributes(record);
        createOrUpdateAttributes(record, keyToAttributeListMap);
        deleteAttributes(keyToAttributeListMap);
    }

    private void deleteAttributes(Map<String, List<SubmissionAttribute>> keyToAttributeListMap) {
        final List<SubmissionAttribute> attributesToDelete = getAttributesToDelete(keyToAttributeListMap);
        submissionAttributeJpaRepository.delete(attributesToDelete);
    }

    private void createOrUpdateAttributes(ComplaintRecord record,
                                          Map<String, List<SubmissionAttribute>> keyToAttributeListMap) {
        record.getAttributeEntries().forEach(a -> {
            final SubmissionAttributeKey attributeKey =
                new SubmissionAttributeKey(a.getCode(), a.getKey(), record.getCRN(), record.getTenantId());
            SubmissionAttribute attribute = findAttribute(attributeKey, keyToAttributeListMap);
            submissionAttributeJpaRepository.save(attribute);
        });
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
            new SubmissionAttribute(true, key);
        }
        final SubmissionAttribute matchingAttribute = matchingAttributes.stream()
            .filter(a -> key.equals(a.getId()))
            .findFirst()
            .orElse(new SubmissionAttribute(true, key));
        matchingAttribute.setRetained(true);
        return matchingAttribute;
    }

    private Map<String, List<SubmissionAttribute>> getSubmissionAttributes(ComplaintRecord record) {
        return submissionAttributeJpaRepository
            .findByCrnAndTenantId(record.getCRN(), record.getTenantId())
            .stream().collect(Collectors.groupingBy(SubmissionAttribute::getKey));
    }

    private void createOrUpdateComplaint(ComplaintRecord complaintRecord) {
        final Complaint complaint = getComplaint(complaintRecord);
        setBasicInfo(complaintRecord, complaint);
        setAuditFields(complaintRecord, complaint);
        setComplainant(complaintRecord, complaint);
        setReceivingMode(complaintRecord, complaint);
        setReceivingCenter(complaintRecord, complaint);
        setComplaintType(complaintRecord, complaint);
        setComplaintStatus(complaintRecord, complaint);
        setAssigneeId(complaintRecord, complaint);
        setLocationDetails(complaintRecord, complaint);
        setEscalationDate(complaintRecord, complaint);
        setWorkflowDetails(complaintRecord, complaint);
        setDepartmentId(complaintRecord, complaint);
        setCitizenFeedBack(complaintRecord, complaint);
        saveComplaint(complaint);
    }

    private void saveComplaint(Complaint complaint) {
        complaintJpaRepository.save(complaint);
    }

    private void saveSubmission(Submission submission) {
        submissionJpaRepository.save(submission);
    }

    private void setDepartmentId(ComplaintRecord complaintRecord, Complaint complaint) {
        complaint.setDepartment(complaintRecord.getDepartment());
    }

    private void setDepartmentId(ComplaintRecord complaintRecord, Submission submission) {
        submission.setDepartment(complaintRecord.getDepartment());
    }

    private void setCitizenFeedBack(ComplaintRecord complaintRecord, Complaint complaint) {
        if (complaintRecord.getCitizenFeedback() != null)
            complaint.setCitizenFeedback(CitizenFeedback.valueOf(complaintRecord.getCitizenFeedback()));
    }

    private void setWorkflowDetails(ComplaintRecord complaintRecord, Complaint complaint) {
        if (complaintRecord.getWorkflowStateId() != null) {
            complaint.setStateId(complaintRecord.getWorkflowStateId());
        }
    }

    private void setEscalationDate(ComplaintRecord complaintRecord, Complaint complaint) {
        complaint.setEscalationDate(complaintRecord.getEscalationDate());
    }

    private void setEscalationDate(ComplaintRecord complaintRecord, Submission submission) {
        submission.setEscalationDate(complaintRecord.getEscalationDate());
    }

    private void setLocationDetails(ComplaintRecord complaintRecord, Complaint complaint) {
        if (complaintRecord.getLocation() != null)
            complaint.setLocation(complaintRecord.getLocation());
        if (complaintRecord.getChildLocation() != null)
            complaint.setChildLocation(complaintRecord.getChildLocation());
    }

    private void setAssigneeId(ComplaintRecord complaintRecord, Complaint complaint) {
        complaint.setAssignee(complaintRecord.getAssigneeId());
    }

    private void setAssigneeId(ComplaintRecord complaintRecord, Submission submission) {
        submission.setAssignee(complaintRecord.getAssigneeId());
    }

    private void setComplaintStatus(ComplaintRecord complaintRecord, Complaint complaint) {
        complaint.setStatus(complaintRecord.getComplaintStatus());
    }

    private void setComplaintStatus(ComplaintRecord complaintRecord, Submission submission) {
        submission.setStatus(complaintRecord.getComplaintStatus());
    }

    private void setComplaintType(ComplaintRecord complaintRecord, Complaint complaint) {
        ComplaintType complaintType = complaintTypeJpaRepository
            .findByCodeAndTenantId(complaintRecord.getComplaintTypeCode(), complaintRecord.getTenantId());
        complaint.setComplaintType(complaintType);
    }

    private void setComplaintType(ComplaintRecord complaintRecord, Submission submission) {
        submission.setServiceCode(complaintRecord.getComplaintTypeCode());
    }

    private void setReceivingMode(ComplaintRecord complaintRecord, Complaint complaint) {
        if (StringUtils.isNotEmpty(complaintRecord.getReceivingMode())) {
            final String receivingModeInUpperCase = complaintRecord.getReceivingMode().toUpperCase();
            complaint.setReceivingMode(receivingModeRepository.findByCode(receivingModeInUpperCase));
        }
    }

    private void setReceivingCenter(ComplaintRecord complaintRecord, Complaint complaint) {
        if (complaintRecord.getReceivingCenter() != null) {
            ReceivingCenter receivingCenterDB = receivingCenterRepository
                .findByIdAndTenantId(complaintRecord.getReceivingCenter(), complaintRecord.getTenantId());
            complaint.setReceivingCenter(receivingCenterDB);
        }
    }

    private void setBasicInfo(ComplaintRecord complaintRecord, Complaint complaint) {
        complaint.setCrn(complaintRecord.getCRN());
        complaint.setLatitude(complaintRecord.getLatitude());
        complaint.setLongitude(complaintRecord.getLongitude());
        complaint.setDetails(complaintRecord.getDescription());
        complaint.setLandmarkDetails(complaintRecord.getLandmarkDetails());
        complaint.setTenantId(complaintRecord.getTenantId());
    }

    private void setBasicInfo(ComplaintRecord complaintRecord, Submission submission) {
        submission.setId(complaintRecord.getCRN());
        submission.setLatitude(complaintRecord.getLatitude());
        submission.setLongitude(complaintRecord.getLongitude());
        submission.setDetails(complaintRecord.getDescription());
        submission.setLandmarkDetails(complaintRecord.getLandmarkDetails());
        submission.setTenantId(complaintRecord.getTenantId());
    }

    private void setAuditFields(ComplaintRecord complaintRecord, Complaint complaint) {
        if (complaint.getCreatedBy() == null)
            complaint.setCreatedBy(complaintRecord.getCreatedBy());
        if (complaint.getCreatedDate() == null)
            complaint.setCreatedDate(new Date());
        complaint.setLastModifiedDate(new Date());
        complaint.setLastModifiedBy(complaintRecord.getLastModifiedBy());
    }

    private void setAuditFields(ComplaintRecord complaintRecord, Submission submission) {
        if (submission.getCreatedBy() == null)
            submission.setCreatedBy(complaintRecord.getCreatedBy());
        if (submission.getCreatedDate() == null)
            submission.setCreatedDate(new Date());
        submission.setLastModifiedDate(new Date());
        submission.setLastModifiedBy(complaintRecord.getLastModifiedBy());
    }

    private void setComplainant(ComplaintRecord complaintRecord, Complaint complaint) {
        if (complaint.getId() == null) {
            complaint.getComplainant().setUserDetail(complaintRecord.getComplainantUserId());
        }
        complaint.getComplainant().setName(complaintRecord.getComplainantName());
        complaint.getComplainant().setMobile(complaintRecord.getComplainantMobileNumber());
        complaint.getComplainant().setEmail(complaintRecord.getComplainantEmail());
        complaint.getComplainant().setAddress(complaintRecord.getComplainantAddress());
        complaint.getComplainant().setTenantId(complaintRecord.getTenantId());
    }

    private void setComplainant(ComplaintRecord complaintRecord, Submission submission) {
        submission.setLoggedInRequester(complaintRecord.getComplainantUserId());
        submission.setName(complaintRecord.getComplainantName());
        submission.setMobile(complaintRecord.getComplainantMobileNumber());
        submission.setEmail(complaintRecord.getComplainantEmail());
        submission.setRequesterAddress(complaintRecord.getComplainantAddress());
        submission.setTenantId(complaintRecord.getTenantId());
    }

    private Complaint getComplaint(ComplaintRecord complaintRecord) {
        final Complaint existingComplaint = complaintJpaRepository.findByCrnAndTenantId(complaintRecord.getCRN(),
            complaintRecord.getTenantId());
        return existingComplaint == null ? new Complaint() : existingComplaint;
    }

    private Submission getSubmission(ComplaintRecord complaintRecord) {
        final Submission existingSubmission = submissionJpaRepository
            .findByIdAndTenantId(complaintRecord.getCRN(), complaintRecord.getTenantId());
        return existingSubmission == null ? new Submission() : existingSubmission;
    }
}
