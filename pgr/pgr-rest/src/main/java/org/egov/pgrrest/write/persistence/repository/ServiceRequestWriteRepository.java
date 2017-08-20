package org.egov.pgrrest.write.persistence.repository;

import org.egov.pgrrest.common.domain.model.AttributeEntry;
import org.egov.pgrrest.common.persistence.entity.Submission;
import org.egov.pgrrest.common.persistence.entity.SubmissionAttribute;
import org.egov.pgrrest.common.persistence.entity.SubmissionAttributeKey;
import org.egov.pgrrest.common.persistence.entity.SubmissionKey;
import org.egov.pgrrest.common.persistence.repository.SubmissionAttributeJpaRepository;
import org.egov.pgrrest.common.persistence.repository.SubmissionJpaRepository;
import org.egov.pgrrest.write.domain.model.ServiceRequestRecord;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ServiceRequestWriteRepository {

    private SubmissionJpaRepository submissionJpaRepository;
    private SubmissionAttributeJpaRepository submissionAttributeJpaRepository;


    public ServiceRequestWriteRepository(SubmissionJpaRepository submissionJpaRepository,
                                         SubmissionAttributeJpaRepository submissionAttributeJpaRepository) {
        this.submissionJpaRepository = submissionJpaRepository;
        this.submissionAttributeJpaRepository = submissionAttributeJpaRepository;
    }

    public void updateOrInsert(ServiceRequestRecord serviceRequestRecord) {
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
        //to remove attributes which are null
        List<AttributeEntry> notNullAttributes = record.getAttributeEntries().stream()
            .filter(attributeEntry -> attributeEntry.getCode() != null)
            .collect(Collectors.toList());
        record.getAttributeEntries().clear();
        record.getAttributeEntries().addAll(notNullAttributes);
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

    private void saveSubmission(Submission submission) {
        submissionJpaRepository.save(submission);
    }

    private void setDepartmentId(ServiceRequestRecord serviceRequestRecord, Submission submission) {
        submission.setDepartment(serviceRequestRecord.getDepartment());
    }

    private void setEscalationDate(ServiceRequestRecord serviceRequestRecord, Submission submission) {
        submission.setEscalationDate(serviceRequestRecord.getEscalationDate());
    }

    private void setAssigneeId(ServiceRequestRecord serviceRequestRecord, Submission submission) {
        submission.setPosition(serviceRequestRecord.getPositionId());
    }

    private void setComplaintStatus(ServiceRequestRecord serviceRequestRecord, Submission submission) {
        submission.setStatus(serviceRequestRecord.getServiceRequestStatus());
    }

    private void setComplaintType(ServiceRequestRecord serviceRequestRecord, Submission submission) {
        submission.setServiceCode(serviceRequestRecord.getServiceRequestTypeCode());
    }

    private void setBasicInfo(ServiceRequestRecord serviceRequestRecord, Submission submission) {
        submission.setId(new SubmissionKey(serviceRequestRecord.getCRN(), serviceRequestRecord.getTenantId()));
        submission.setLatitude(serviceRequestRecord.getLatitude());
        submission.setLongitude(serviceRequestRecord.getLongitude());
        submission.setDetails(serviceRequestRecord.getDescription());
        submission.setLandmarkDetails(serviceRequestRecord.getLandmarkDetails());
    }

    private void setAuditFields(ServiceRequestRecord serviceRequestRecord, Submission submission) {
        if (submission.getCreatedBy() == null)
            submission.setCreatedBy(serviceRequestRecord.getCreatedBy());
        if (submission.getCreatedDate() == null)
            submission.setCreatedDate(serviceRequestRecord.getCreatedDate());
        submission.setLastModifiedDate(serviceRequestRecord.getLastModifiedDate());
        submission.setLastModifiedBy(serviceRequestRecord.getLastModifiedBy());
    }

    private void setComplainant(ServiceRequestRecord serviceRequestRecord, Submission submission) {
        submission.setLoggedInRequester(serviceRequestRecord.getLoggedInRequesterUserId());
        submission.setName(serviceRequestRecord.getRequesterName());
        submission.setMobile(serviceRequestRecord.getRequesterMobileNumber());
        submission.setEmail(serviceRequestRecord.getRequesterEmail());
        submission.setRequesterAddress(serviceRequestRecord.getRequesterAddress());
    }

    private Submission getSubmission(ServiceRequestRecord serviceRequestRecord) {
        final Submission existingSubmission = submissionJpaRepository
            .findOne(new SubmissionKey(serviceRequestRecord.getCRN(), serviceRequestRecord.getTenantId()));
        return existingSubmission == null ? new Submission() : existingSubmission;
    }
}
