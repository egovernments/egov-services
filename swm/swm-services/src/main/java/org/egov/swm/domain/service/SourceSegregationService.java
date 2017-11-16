package org.egov.swm.domain.service;

import java.util.Date;
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

	@Transactional
	public SourceSegregationRequest create(SourceSegregationRequest sourceSegregationRequest) {

		validate(sourceSegregationRequest);

		Long userId = null;

		if (sourceSegregationRequest.getRequestInfo() != null
				&& sourceSegregationRequest.getRequestInfo().getUserInfo() != null
				&& null != sourceSegregationRequest.getRequestInfo().getUserInfo().getId()) {
			userId = sourceSegregationRequest.getRequestInfo().getUserInfo().getId();
		}

		for (SourceSegregation v : sourceSegregationRequest.getSourceSegregations()) {

			setAuditDetails(v, userId);

			v.setCode(UUID.randomUUID().toString().replace("-", ""));

			prepareCollectionDetails(v);

		}

		return sourceSegregationRepository.save(sourceSegregationRequest);

	}

	@Transactional
	public SourceSegregationRequest update(SourceSegregationRequest sourceSegregationRequest) {

		Long userId = null;

		if (sourceSegregationRequest.getRequestInfo() != null
				&& sourceSegregationRequest.getRequestInfo().getUserInfo() != null
				&& null != sourceSegregationRequest.getRequestInfo().getUserInfo().getId()) {
			userId = sourceSegregationRequest.getRequestInfo().getUserInfo().getId();
		}

		for (SourceSegregation v : sourceSegregationRequest.getSourceSegregations()) {

			setAuditDetails(v, userId);

			prepareCollectionDetails(v);

		}

		validate(sourceSegregationRequest);

		return sourceSegregationRepository.update(sourceSegregationRequest);

	}

	private void prepareCollectionDetails(SourceSegregation ss) {

		for (CollectionDetails cd : ss.getCollectionDetails()) {

			cd.setId(UUID.randomUUID().toString().replace("-", ""));
			cd.setTenantId(ss.getTenantId());

		}
	}

	private void validate(SourceSegregationRequest sourceSegregationRequest) {

		for (SourceSegregation sourceSegregation : sourceSegregationRequest.getSourceSegregations()) {

			if (sourceSegregation.getDumpingGround() != null && (sourceSegregation.getDumpingGround().getCode() == null
					|| sourceSegregation.getDumpingGround().getCode().isEmpty()))
				throw new CustomException("DumpingGround",
						"The field DumpingGround Code is Mandatory . It cannot be not be null or empty.Please provide correct value ");

			// Validate Ending Dumping ground
			if (sourceSegregation.getDumpingGround() != null
					&& sourceSegregation.getDumpingGround().getCode() != null) {

				sourceSegregation.setDumpingGround(dumpingGroundService.getDumpingGround(
						sourceSegregation.getTenantId(), sourceSegregation.getDumpingGround().getCode(),
						sourceSegregationRequest.getRequestInfo()));

			} else
				throw new CustomException("DumpingGround", "DumpingGround is required");

			for (CollectionDetails cd : sourceSegregation.getCollectionDetails()) {

				// Validate Collection Type

				if (cd.getCollectionType() != null
						&& (cd.getCollectionType().getCode() == null || cd.getCollectionType().getCode().isEmpty()))
					throw new CustomException("CollectionType",
							"The field CollectionType Code is Mandatory . It cannot be not be null or empty.Please provide correct value ");

				if (cd.getCollectionType() != null && cd.getCollectionType().getCode() != null) {

					cd.setCollectionType(collectionTypeService.getCollectionType(sourceSegregation.getTenantId(),
							cd.getCollectionType().getCode(), sourceSegregationRequest.getRequestInfo()));

				} else
					throw new CustomException("CollectionType", "CollectionType is required");

			}

		}
	}

	public Pagination<SourceSegregation> search(SourceSegregationSearch sourceSegregationSearch) {

		return sourceSegregationRepository.search(sourceSegregationSearch);
	}

	private void setAuditDetails(SourceSegregation contract, Long userId) {

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