package org.egov.swm.domain.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import org.egov.swm.constants.Constants;
import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.CollectionPoint;
import org.egov.swm.domain.model.CollectionPointDetails;
import org.egov.swm.domain.model.CollectionPointDetailsSearch;
import org.egov.swm.domain.model.CollectionPointSearch;
import org.egov.swm.domain.model.CollectionType;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.repository.CollectionPointDetailsRepository;
import org.egov.swm.domain.repository.CollectionPointRepository;
import org.egov.swm.web.contract.MasterDetails;
import org.egov.swm.web.contract.MdmsCriteria;
import org.egov.swm.web.contract.MdmsRequest;
import org.egov.swm.web.contract.MdmsResponse;
import org.egov.swm.web.contract.ModuleDetails;
import org.egov.swm.web.repository.MdmsRepository;
import org.egov.swm.web.requests.CollectionPointDetailsRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONArray;

@Service
@Transactional(readOnly = true)
public class CollectionPointDetailsService {

	@Autowired
	private CollectionPointDetailsRepository collectionPointDetailsRepository;

	@Autowired
	private CollectionPointRepository collectionPointRepository;

	@Autowired
	private MdmsRepository mdmsRepository;

	@Transactional
	public CollectionPointDetailsRequest create(CollectionPointDetailsRequest collectionPointDetailsRequest) {

		validate(collectionPointDetailsRequest);

		Long userId = null;

		for (CollectionPointDetails cp : collectionPointDetailsRequest.getCollectionPointDetails()) {

			if (collectionPointDetailsRequest.getRequestInfo() != null
					&& collectionPointDetailsRequest.getRequestInfo().getUserInfo() != null
					&& null != collectionPointDetailsRequest.getRequestInfo().getUserInfo().getId()) {
				userId = collectionPointDetailsRequest.getRequestInfo().getUserInfo().getId();
			}
			setAuditDetails(cp, userId);
			cp.setId(UUID.randomUUID().toString().replace("-", ""));

		}

		return collectionPointDetailsRepository.save(collectionPointDetailsRequest);

	}

	@Transactional
	public CollectionPointDetailsRequest update(CollectionPointDetailsRequest collectionPointDetailsRequest) {

		Long userId = null;

		validate(collectionPointDetailsRequest);

		for (CollectionPointDetails cp : collectionPointDetailsRequest.getCollectionPointDetails()) {

			if (collectionPointDetailsRequest.getRequestInfo() != null
					&& collectionPointDetailsRequest.getRequestInfo().getUserInfo() != null
					&& null != collectionPointDetailsRequest.getRequestInfo().getUserInfo().getId()) {
				userId = collectionPointDetailsRequest.getRequestInfo().getUserInfo().getId();
			}

			setAuditDetails(cp, userId);
		}

		return collectionPointDetailsRepository.update(collectionPointDetailsRequest);

	}

	public Pagination<CollectionPointDetails> search(CollectionPointDetailsSearch collectionPointDetailsSearch) {

		return collectionPointDetailsRepository.search(collectionPointDetailsSearch);
	}

	private void validate(CollectionPointDetailsRequest collectionPointDetailsRequest) {

		JSONArray responseJSONArray = null;
		MasterDetails[] masterDetailsArray;
		ModuleDetails[] moduleDetailsArray;
		MdmsRequest request;
		MdmsResponse response;
		ArrayList<CollectionType> ctResponseList;
		ObjectMapper mapper = new ObjectMapper();

		for (CollectionPointDetails details : collectionPointDetailsRequest.getCollectionPointDetails()) {

			// Validate CollectionType
			if (details.getCollectionType() != null && details.getCollectionType().getCode() != null) {
				masterDetailsArray = new MasterDetails[1];
				masterDetailsArray[0] = MasterDetails.builder().name(Constants.COLLECTIONTYPE_MASTER_NAME)
						.filter("[?(@.code == '" + details.getCollectionType().getCode() + "')]").build();
				moduleDetailsArray = new ModuleDetails[1];
				moduleDetailsArray[0] = ModuleDetails.builder().moduleName(Constants.MODULE_CODE)
						.masterDetails(masterDetailsArray).build();

				request = MdmsRequest.builder()
						.mdmsCriteria(MdmsCriteria.builder().moduleDetails(moduleDetailsArray)
								.tenantId(details.getTenantId()).build())
						.requestInfo(collectionPointDetailsRequest.getRequestInfo()).build();
				response = mdmsRepository.getByCriteria(request);
				if (response == null || response.getMdmsRes() == null
						|| !response.getMdmsRes().containsKey(Constants.MODULE_CODE)
						|| response.getMdmsRes().get(Constants.MODULE_CODE) == null
						|| !response.getMdmsRes().get(Constants.MODULE_CODE)
								.containsKey(Constants.COLLECTIONTYPE_MASTER_NAME)
						|| response.getMdmsRes().get(Constants.MODULE_CODE)
								.get(Constants.COLLECTIONTYPE_MASTER_NAME) == null) {
					throw new CustomException("CollectionType",
							"Given CollectionType is invalid: " + details.getCollectionType().getCode());
				} else {
					ctResponseList = new ArrayList<CollectionType>();

					responseJSONArray = response.getMdmsRes().get(Constants.MODULE_CODE)
							.get(Constants.COLLECTIONTYPE_MASTER_NAME);

					for (int i = 0; i < responseJSONArray.size(); i++) {
						ctResponseList.add(mapper.convertValue(responseJSONArray.get(i), CollectionType.class));
					}

					if (ctResponseList.isEmpty())
						throw new CustomException("CollectionType",
								"Given CollectionType is invalid: " + details.getCollectionType().getCode());
					else
						details.setCollectionType(ctResponseList.get(0));
				}
			}

			// Validate Collection Point

			if (details.getCollectionPoint() != null && details.getCollectionPoint().getName() != null) {

				CollectionPointSearch search = new CollectionPointSearch();
				search.setTenantId(details.getTenantId());
				search.setName(details.getCollectionPoint().getName());

				Pagination<CollectionPoint> collectionPoints = collectionPointRepository.search(search);

				if (collectionPoints == null || collectionPoints.getPagedData() == null
						|| collectionPoints.getPagedData().isEmpty())
					throw new CustomException("CollectionPoint",
							"Given CollectionPoint is invalid: " + details.getCollectionPoint().getName());
				else
					details.setCollectionPoint(collectionPoints.getPagedData().get(0));
			}
		}
	}

	private void setAuditDetails(CollectionPointDetails contract, Long userId) {

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