package org.egov.swm.domain.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.egov.swm.constants.Constants;
import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.BinDetails;
import org.egov.swm.domain.model.Boundary;
import org.egov.swm.domain.model.CollectionPoint;
import org.egov.swm.domain.model.CollectionPointDetails;
import org.egov.swm.domain.model.CollectionPointSearch;
import org.egov.swm.domain.model.CollectionType;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.repository.BinDetailsRepository;
import org.egov.swm.domain.repository.CollectionPointRepository;
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
	private BinDetailsRepository binDetailsRepository;

	@Autowired
	private MdmsRepository mdmsRepository;

	@Autowired
	private BoundaryRepository boundaryRepository;

	@Transactional
	public CollectionPointRequest create(CollectionPointRequest collectionPointRequest) {

		validate(collectionPointRequest);

		Long userId = null;

		if (collectionPointRequest.getRequestInfo() != null
				&& collectionPointRequest.getRequestInfo().getUserInfo() != null
				&& null != collectionPointRequest.getRequestInfo().getUserInfo().getId()) {
			userId = collectionPointRequest.getRequestInfo().getUserInfo().getId();
		}

		for (CollectionPoint cp : collectionPointRequest.getCollectionPoints()) {

			setAuditDetails(cp, userId);

			cp.setCode(UUID.randomUUID().toString().replace("-", ""));

			populateBinDetailsIds(cp);

			populateCollectionPointDetails(cp);

		}

		return collectionPointRepository.save(collectionPointRequest);

	}

	@Transactional
	public CollectionPointRequest update(CollectionPointRequest collectionPointRequest) {

		Long userId = null;

		if (collectionPointRequest.getRequestInfo() != null
				&& collectionPointRequest.getRequestInfo().getUserInfo() != null
				&& null != collectionPointRequest.getRequestInfo().getUserInfo().getId()) {
			userId = collectionPointRequest.getRequestInfo().getUserInfo().getId();
		}

		for (CollectionPoint cp : collectionPointRequest.getCollectionPoints()) {

			setAuditDetails(cp, userId);

			populateBinDetailsIds(cp);

			populateCollectionPointDetails(cp);
		}

		validate(collectionPointRequest);

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

		findDuplicatesInUniqueFields(collectionPointRequest);

		for (CollectionPoint collectionPoint : collectionPointRequest.getCollectionPoints()) {

			// Validate Boundary

			if (collectionPoint.getLocation() != null && (collectionPoint.getLocation().getCode() == null
					|| collectionPoint.getLocation().getCode().isEmpty()))
				throw new CustomException("Location",
						"The field Location Code is Mandatory . It cannot be not be null or empty.Please provide correct value ");

			if (collectionPoint.getLocation() != null && collectionPoint.getLocation().getCode() != null) {

				Boundary boundary = boundaryRepository.fetchBoundaryByCode(collectionPoint.getLocation().getCode(),
						collectionPoint.getTenantId());

				if (boundary != null)
					collectionPoint.setLocation(boundary);
				else
					throw new CustomException("Location",
							"Given Location is Invalid: " + collectionPoint.getLocation().getCode());
			}

			if (collectionPoint.getCollectionPointDetails() != null) {

				for (CollectionPointDetails cpd : collectionPoint.getCollectionPointDetails()) {

					if (cpd.getCollectionType() != null && (cpd.getCollectionType().getCode() == null
							|| cpd.getCollectionType().getCode().isEmpty()))
						throw new CustomException("CollectionType",
								"The field CollectionType Code is Mandatory . It cannot be not be null or empty.Please provide correct value ");

					// Validate Collection Type
					if (cpd.getCollectionType() != null && cpd.getCollectionType().getCode() != null) {

						responseJSONArray = mdmsRepository.getByCriteria(collectionPoint.getTenantId(),
								Constants.MODULE_CODE, Constants.COLLECTIONTYPE_MASTER_NAME, "code",
								cpd.getCollectionType().getCode(), collectionPointRequest.getRequestInfo());

						if (responseJSONArray != null && responseJSONArray.size() > 0)
							cpd.setCollectionType(mapper.convertValue(responseJSONArray.get(0), CollectionType.class));
						else
							throw new CustomException("CollectionType",
									"Given CollectionType is invalid: " + cpd.getCollectionType().getCode());

					} else
						throw new CustomException("CollectionType", "CollectionType is required");
				}
			}

			validateUniqueFields(collectionPoint);

		}

	}

	private void findDuplicatesInUniqueFields(CollectionPointRequest collectionPointRequest) {

		Map<String, String> assetOrBinIdsMap = new HashMap<>();
		Map<String, String> rfidsMap = new HashMap<>();
		Map<String, String> nameMap = new HashMap<>();

		for (CollectionPoint collectionPoint : collectionPointRequest.getCollectionPoints()) {
			if (collectionPoint.getName() != null) {

				if (nameMap.get(collectionPoint.getName()) != null)
					throw new CustomException("Name",
							"Duplicate names in given collection Points: " + collectionPoint.getName());

				nameMap.put(collectionPoint.getName(), collectionPoint.getName());

			}

			for (BinDetails bd : collectionPoint.getBinDetails()) {

				if (bd.getAssetOrBinId() != null) {

					if (assetOrBinIdsMap.get(bd.getAssetOrBinId()) != null)
						throw new CustomException("BinId",
								"Duplicate BinIds in given Bin details: " + bd.getAssetOrBinId());

					assetOrBinIdsMap.put(bd.getAssetOrBinId(), bd.getAssetOrBinId());

				}

				if (bd.getRfid() != null) {
					if (rfidsMap.get(bd.getRfid()) != null)
						throw new CustomException("Rfid", "Duplicate RFIDs in given Bin details: " + bd.getRfid());

					rfidsMap.put(bd.getRfid(), bd.getRfid());

				}

			}
		}

	}

	private void validateUniqueFields(CollectionPoint collectionPoint) {

		if (collectionPoint.getName() != null) {
			if (!collectionPointRepository.uniqueCheck(collectionPoint.getTenantId(), "name", collectionPoint.getName(),
					"code", collectionPoint.getCode())) {

				throw new CustomException("Name",
						"The field name must be unique in the system The  value " + collectionPoint.getName()
								+ " for the field name already exists in the system. Please provide different value ");

			}
		}

		for (BinDetails bd : collectionPoint.getBinDetails()) {

			if (bd.getAssetOrBinId() != null) {
				if (!binDetailsRepository.uniqueCheck(collectionPoint.getTenantId(), "assetOrBinId",
						bd.getAssetOrBinId(), "id", bd.getId())) {

					throw new CustomException("BinId", "The field BinId must be unique in the system The  value "
							+ bd.getAssetOrBinId()
							+ " for the field BinId already exists in the system. Please provide different value ");

				}
			}

			if (bd.getRfidAssigned() != null && bd.getRfidAssigned()) {

				if (bd.getRfid() == null || bd.getRfid().isEmpty()) {

					throw new CustomException("RFID",
							"The field RFID must be not be null or empty , the field RFID is Mandatory. "
									+ "It cannot be not be null or empty.Please provide correct value");

				}

			}

			if (bd.getRfid() != null) {

				if (bd.getRfidAssigned() && bd.getRfidAssigned() != null) {
					if (!binDetailsRepository.uniqueCheck(collectionPoint.getTenantId(), "rfid", bd.getRfid(), "id",
							bd.getId())) {

						throw new CustomException("RFID", "The field RFID must be unique in the system The  value "
								+ bd.getRfid()
								+ " for the field RFID already exists in the system. Please provide different value ");

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