package org.egov.swm.domain.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.CollectionDetails;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.SourceSegregation;
import org.egov.swm.domain.model.SourceSegregationSearch;
import org.egov.swm.domain.repository.SourceSegregationRepository;
import org.egov.swm.web.requests.SourceSegregationRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SourceSegregationService {

    @Autowired
    private SourceSegregationRepository sourceSegregationRepository;

    @Autowired
    private CollectionTypeService collectionTypeService;

    @Autowired
    private DumpingGroundService dumpingGroundService;

    @Autowired
    private TenantService tenantService;

    @Transactional
    public SourceSegregationRequest create(final SourceSegregationRequest sourceSegregationRequest) {

        validate(sourceSegregationRequest);
        validateForDuplicateCollectionTypes(sourceSegregationRequest);

        Long userId = null;

        if (sourceSegregationRequest.getRequestInfo() != null
                && sourceSegregationRequest.getRequestInfo().getUserInfo() != null
                && null != sourceSegregationRequest.getRequestInfo().getUserInfo().getId())
            userId = sourceSegregationRequest.getRequestInfo().getUserInfo().getId();

        for (final SourceSegregation v : sourceSegregationRequest.getSourceSegregations()) {

            setAuditDetails(v, userId);

            v.setCode(UUID.randomUUID().toString().replace("-", ""));

            prepareCollectionDetails(v);

        }

        return sourceSegregationRepository.save(sourceSegregationRequest);

    }

    @Transactional
    public SourceSegregationRequest update(final SourceSegregationRequest sourceSegregationRequest) {

        Long userId = null;

        if (sourceSegregationRequest.getRequestInfo() != null
                && sourceSegregationRequest.getRequestInfo().getUserInfo() != null
                && null != sourceSegregationRequest.getRequestInfo().getUserInfo().getId())
            userId = sourceSegregationRequest.getRequestInfo().getUserInfo().getId();

        for (final SourceSegregation v : sourceSegregationRequest.getSourceSegregations()) {

            setAuditDetails(v, userId);

            prepareCollectionDetails(v);

        }

        validate(sourceSegregationRequest);
        validateForDuplicateCollectionTypes(sourceSegregationRequest);

        return sourceSegregationRepository.update(sourceSegregationRequest);

    }

    private void prepareCollectionDetails(final SourceSegregation ss) {

        for (final CollectionDetails cd : ss.getCollectionDetails()) {

            cd.setId(UUID.randomUUID().toString().replace("-", ""));
            cd.setTenantId(ss.getTenantId());

        }
    }

    private void validate(final SourceSegregationRequest sourceSegregationRequest) {

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        for (final SourceSegregation sourceSegregation : sourceSegregationRequest.getSourceSegregations()) {

            if (sourceSegregation.getUlb() != null && (sourceSegregation.getUlb().getCode() == null
                    || sourceSegregation.getUlb().getCode().isEmpty()))
                throw new CustomException("ULB",
                        "The field ULB Code is Mandatory . It cannot be not be null or empty.Please provide correct value ");

            // Validate ULB
            if (sourceSegregation.getUlb() != null
                    && sourceSegregation.getUlb().getCode() != null)
                sourceSegregation.setUlb(tenantService.getTenant(
                        sourceSegregation.getTenantId(), sourceSegregation.getUlb().getCode(),
                        sourceSegregationRequest.getRequestInfo()));
            else
                throw new CustomException("ULB", "ULB is required");

            if (sourceSegregation.getDumpingGround() != null && (sourceSegregation.getDumpingGround().getCode() == null
                    || sourceSegregation.getDumpingGround().getCode().isEmpty()))
                throw new CustomException("DumpingGround",
                        "The field DumpingGround Code is Mandatory . It cannot be not be null or empty.Please provide correct value ");

            // Validate Dumping ground
            if (sourceSegregation.getDumpingGround() != null
                    && sourceSegregation.getDumpingGround().getCode() != null)
                sourceSegregation.setDumpingGround(dumpingGroundService.getDumpingGround(
                        sourceSegregation.getTenantId(), sourceSegregation.getDumpingGround().getCode(),
                        sourceSegregationRequest.getRequestInfo()));
            else
                throw new CustomException("DumpingGround", "DumpingGround is required");

            if (sourceSegregation.getSourceSegregationDate() != null)
                if (new Date()
                        .before(new Date(sourceSegregation.getSourceSegregationDate())))
                    throw new CustomException("SourceSegregationDate ", "Given Source Segregation Date is invalid: "
                            + dateFormat.format(new Date(sourceSegregation.getSourceSegregationDate())));

            for (final CollectionDetails cd : sourceSegregation.getCollectionDetails()) {

                // Validate Collection Type

                if (cd.getCollectionType() != null
                        && (cd.getCollectionType().getCode() == null || cd.getCollectionType().getCode().isEmpty()))
                    throw new CustomException("CollectionType",
                            "The field CollectionType Code is Mandatory . It cannot be not be null or empty.Please provide correct value ");

                if (cd.getCollectionType() != null && cd.getCollectionType().getCode() != null)
                    cd.setCollectionType(collectionTypeService.getCollectionType(sourceSegregation.getTenantId(),
                            cd.getCollectionType().getCode(), sourceSegregationRequest.getRequestInfo()));
                else
                    throw new CustomException("CollectionType", "CollectionType is required");

            }

        }
    }

    private void validateForDuplicateCollectionTypes(final SourceSegregationRequest sourceSegregationRequest) {

        final Map<String, String> codeMap = new HashMap<>();

        for (SourceSegregation sourceSegregation : sourceSegregationRequest.getSourceSegregations()) {

            for (CollectionDetails collectionDetail : sourceSegregation.getCollectionDetails()) {

                if (codeMap.get(collectionDetail.getCollectionType().getCode()) != null)
                    throw new CustomException("Collection Type",
                            "Collection types shall be unique per record.: " + collectionDetail.getCollectionType().getCode());

                codeMap.put(collectionDetail.getCollectionType().getCode(), collectionDetail.getCollectionType().getCode());
            }
        }
    }

    public Pagination<SourceSegregation> search(final SourceSegregationSearch sourceSegregationSearch) {

        return sourceSegregationRepository.search(sourceSegregationSearch);
    }

    private void setAuditDetails(final SourceSegregation contract, final Long userId) {

        if (contract.getAuditDetails() == null)
            contract.setAuditDetails(new AuditDetails());

        if (null == contract.getCode() || contract.getCode().isEmpty()) {
            contract.getAuditDetails().setCreatedBy(null != userId ? userId.toString() : null);
            contract.getAuditDetails().setCreatedTime(new Date().getTime());
        }

        contract.getAuditDetails().setLastModifiedBy(null != userId ? userId.toString() : null);
        contract.getAuditDetails().setLastModifiedTime(new Date().getTime());
    }

}