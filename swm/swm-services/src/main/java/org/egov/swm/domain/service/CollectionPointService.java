package org.egov.swm.domain.service;

import java.util.Date;
import java.util.UUID;

import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.BinIdDetails;
import org.egov.swm.domain.model.CollectionPoint;
import org.egov.swm.domain.model.CollectionPointSearch;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.repository.CollectionPointRepository;
import org.egov.swm.web.requests.CollectionPointRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CollectionPointService {

	@Autowired
	private CollectionPointRepository collectionPointRepository;

	@Transactional
	public CollectionPointRequest create(CollectionPointRequest collectionPointRequest) {

		for (CollectionPoint cp : collectionPointRequest.getCollectionPoints()) {

			Long userId = 1l;

			if (collectionPointRequest.getRequestInfo() != null
					&& collectionPointRequest.getRequestInfo().getUserInfo() != null
					&& null != collectionPointRequest.getRequestInfo().getUserInfo().getId()) {
				userId = collectionPointRequest.getRequestInfo().getUserInfo().getId();
			}
			setAuditDetails(cp, userId);
			cp.setId(UUID.randomUUID().toString().replace("-", ""));

			populateBinIdDetailsIds(cp);

		}

		return collectionPointRepository.save(collectionPointRequest);

	}

	@Transactional
	public CollectionPointRequest update(CollectionPointRequest collectionPointRequest) {

		for (CollectionPoint cp : collectionPointRequest.getCollectionPoints()) {

			setAuditDetails(cp, collectionPointRequest.getRequestInfo().getUserInfo().getId());
			populateBinIdDetailsIds(cp);
		}

		return collectionPointRepository.update(collectionPointRequest);

	}

	private void populateBinIdDetailsIds(CollectionPoint cp) {
		if (cp != null && cp.getBinIdDetails() != null)
			for (BinIdDetails bid : cp.getBinIdDetails()) {
				bid.setId(UUID.randomUUID().toString().replace("-", ""));
				bid.setTenantId(cp.getTenantId());
				bid.setCollectionPointId(cp.getId());

			}
	}

	public Pagination<CollectionPoint> search(CollectionPointSearch collectionPointSearch) {

		return collectionPointRepository.search(collectionPointSearch);
	}

	private void setAuditDetails(CollectionPoint contract, Long userId) {

		if (contract.getAuditDetails() == null)
			contract.setAuditDetails(new AuditDetails());

		if (null == contract.getId() || contract.getId().isEmpty()) {
			contract.getAuditDetails().setCreatedBy(null != userId ? userId.toString() : null);
			contract.getAuditDetails().setCreatedTime(new Date().getTime());
		}

		contract.getAuditDetails().setLastModifiedBy(null != userId ? userId.toString() : null);
		contract.getAuditDetails().setLastModifiedTime(new Date().getTime());
	}

}