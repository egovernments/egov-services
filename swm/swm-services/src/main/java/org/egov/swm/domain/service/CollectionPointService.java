package org.egov.swm.domain.service;

import java.util.Date;
import java.util.UUID;

import org.egov.swm.constants.Constants;
import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.BinDetails;
import org.egov.swm.domain.model.CollectionPoint;
import org.egov.swm.domain.model.CollectionPointDetails;
import org.egov.swm.domain.model.CollectionPointSearch;
import org.egov.swm.domain.model.CollectionType;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.repository.CollectionPointRepository;
import org.egov.swm.web.contract.Boundary;
import org.egov.swm.web.repository.BoundaryRepository;
import org.egov.swm.web.repository.MdmsRepository;
import org.egov.swm.web.requests.CollectionPointRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONArray;

@Service
@Transactional(readOnly = true)
public class CollectionPointService {

	@Autowired
	private CollectionPointRepository collectionPointRepository;

	@Autowired
	private MdmsRepository mdmsRepository;

	@Autowired
	private BoundaryRepository boundaryRepository;

	@Transactional
	public CollectionPointRequest create(CollectionPointRequest collectionPointRequest) {

		validate(collectionPointRequest);

		Long userId = null;

		for (CollectionPoint cp : collectionPointRequest.getCollectionPoints()) {

			if (collectionPointRequest.getRequestInfo() != null
					&& collectionPointRequest.getRequestInfo().getUserInfo() != null
					&& null != collectionPointRequest.getRequestInfo().getUserInfo().getId()) {
				userId = collectionPointRequest.getRequestInfo().getUserInfo().getId();
			}

			setAuditDetails(cp, userId);

			cp.setCode(UUID.randomUUID().toString().replace("-", ""));

			populateBinDetailsIds(cp);

			populateCollectionPointDetails(cp);

		}

		return collectionPointRepository.save(collectionPointRequest);

	}

	@Transactional
	public CollectionPointRequest update(CollectionPointRequest collectionPointRequest) {

		validate(collectionPointRequest);

		Long userId = null;

		for (CollectionPoint cp : collectionPointRequest.getCollectionPoints()) {

			if (collectionPointRequest.getRequestInfo() != null
					&& collectionPointRequest.getRequestInfo().getUserInfo() != null
					&& null != collectionPointRequest.getRequestInfo().getUserInfo().getId()) {
				userId = collectionPointRequest.getRequestInfo().getUserInfo().getId();
			}

			setAuditDetails(cp, userId);

			populateBinDetailsIds(cp);

			populateCollectionPointDetails(cp);
		}

		return collectionPointRepository.update(collectionPointRequest);

	}

	private void populateBinDetailsIds(CollectionPoint cp) {
		if (cp != null && cp.getBinDetails() != null)
			for (BinDetails bid : cp.getBinDetails()) {
				bid.setId(UUID.randomUUID().toString().replace("-", ""));
				bid.setTenantId(cp.getTenantId());
			}
	}

	private void populateCollectionPointDetails(CollectionPoint cp) {
		if (cp != null && cp.getCollectionPointDetails() != null)
			for (CollectionPointDetails cpd : cp.getCollectionPointDetails()) {
				cpd.setId(UUID.randomUUID().toString().replace("-", ""));
				cpd.setTenantId(cp.getTenantId());
			}
	}

	private void validate(CollectionPointRequest collectionPointRequest) {

		JSONArray responseJSONArray;
		ObjectMapper mapper = new ObjectMapper();

		for (CollectionPoint collectionPoint : collectionPointRequest.getCollectionPoints()) {

			// Validate Boundary
			if (collectionPoint.getLocation() != null && collectionPoint.getLocation().getBndryId() != null) {

				Boundary boundary = boundaryRepository.fetchBoundaryByCode(collectionPoint.getLocation().getCode(),
						collectionPoint.getTenantId());

				if (boundary != null)
					collectionPoint.setLocation(org.egov.swm.domain.model.Boundary.builder()
							.id(String.valueOf(boundary.getId())).name(boundary.getName())
							.boundaryNum(String.valueOf(boundary.getBoundaryNum())).code(boundary.getCode()).build());
				else
					throw new CustomException("Boundary",
							"Given Boundary is Invalid: " + collectionPoint.getLocation().getCode());
			}

			if (collectionPoint.getCollectionPointDetails() != null) {

				for (CollectionPointDetails cpd : collectionPoint.getCollectionPointDetails()) {

					// Validate Collection Type
					if (cpd.getCollectionType() != null) {

						responseJSONArray = mdmsRepository.getByCriteria(collectionPoint.getTenantId(),
								Constants.MODULE_CODE, Constants.COLLECTIONTYPE_MASTER_NAME, "code",
								cpd.getCollectionType().getCode(), collectionPointRequest.getRequestInfo());

						if (responseJSONArray != null && responseJSONArray.size() > 0)
							cpd.setCollectionType(mapper.convertValue(responseJSONArray.get(0), CollectionType.class));
						else
							throw new CustomException("CollectionType",
									"Given CollectionType is invalid: " + cpd.getCollectionType().getCode());

					}
				}
			}

		}

	}

	public Pagination<CollectionPoint> search(CollectionPointSearch collectionPointSearch) {

		return collectionPointRepository.search(collectionPointSearch);
	}

	private void setAuditDetails(CollectionPoint contract, Long userId) {

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