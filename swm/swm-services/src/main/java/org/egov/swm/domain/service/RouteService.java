package org.egov.swm.domain.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import org.egov.swm.constants.Constants;
import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.CollectionPoint;
import org.egov.swm.domain.model.CollectionPointSearch;
import org.egov.swm.domain.model.CollectionType;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.Route;
import org.egov.swm.domain.model.RouteCollectionPointMap;
import org.egov.swm.domain.model.RouteSearch;
import org.egov.swm.domain.repository.CollectionPointRepository;
import org.egov.swm.domain.repository.RouteRepository;
import org.egov.swm.persistence.entity.DumpingGroundEntity;
import org.egov.swm.web.contract.MasterDetails;
import org.egov.swm.web.contract.MdmsCriteria;
import org.egov.swm.web.contract.MdmsRequest;
import org.egov.swm.web.contract.MdmsResponse;
import org.egov.swm.web.contract.ModuleDetails;
import org.egov.swm.web.repository.MdmsRepository;
import org.egov.swm.web.requests.RouteRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONArray;

@Service
@Transactional(readOnly = true)
public class RouteService {

	@Autowired
	private RouteRepository routeRepository;

	@Autowired
	private CollectionPointRepository collectionPointRepository;

	@Autowired
	private MdmsRepository mdmsRepository;

	@Transactional
	public RouteRequest create(RouteRequest routeRequest) {

		validate(routeRequest);

		Long userId = null;

		for (Route r : routeRequest.getRoutes()) {

			if (routeRequest.getRequestInfo() != null && routeRequest.getRequestInfo().getUserInfo() != null
					&& null != routeRequest.getRequestInfo().getUserInfo().getId()) {
				userId = routeRequest.getRequestInfo().getUserInfo().getId();
			}
			setAuditDetails(r, userId);
			r.setId(UUID.randomUUID().toString().replace("-", ""));

			populateCollectionPointDetails(r);
		}

		return routeRepository.save(routeRequest);

	}

	private void populateCollectionPointDetails(Route r) {
		if (r.getCollectionPoints() != null)
			for (RouteCollectionPointMap map : r.getCollectionPoints()) {
				map.setId(UUID.randomUUID().toString().replace("-", ""));
				map.setTenantId(r.getTenantId());
				map.setRoute(r.getName());
				map.setAuditDetails(r.getAuditDetails());
			}
	}

	@Transactional
	public RouteRequest update(RouteRequest routeRequest) {

		Long userId = null;

		validate(routeRequest);

		for (Route r : routeRequest.getRoutes()) {

			if (routeRequest.getRequestInfo() != null && routeRequest.getRequestInfo().getUserInfo() != null
					&& null != routeRequest.getRequestInfo().getUserInfo().getId()) {
				userId = routeRequest.getRequestInfo().getUserInfo().getId();
			}

			setAuditDetails(r, userId);

			populateCollectionPointDetails(r);
		}

		return routeRepository.update(routeRequest);

	}

	public Pagination<Route> search(RouteSearch routeSearch) {

		return routeRepository.search(routeSearch);
	}

	private void validate(RouteRequest routeRequest) {

		JSONArray responseJSONArray = null;
		MasterDetails[] masterDetailsArray;
		ModuleDetails[] moduleDetailsArray;
		MdmsRequest request;
		MdmsResponse response;
		ArrayList<CollectionType> ctResponseList;
		ArrayList<DumpingGroundEntity> dgResponseList;
		ObjectMapper mapper = new ObjectMapper();

		for (Route route : routeRequest.getRoutes()) {

			// Validate CollectionType
			if (route.getCollectionType() != null && route.getCollectionType().getCode() != null) {
				masterDetailsArray = new MasterDetails[1];
				masterDetailsArray[0] = MasterDetails.builder().name(Constants.COLLECTIONTYPE_MASTER_NAME)
						.filter("[?(@.code == '" + route.getCollectionType().getCode() + "')]").build();
				moduleDetailsArray = new ModuleDetails[1];
				moduleDetailsArray[0] = ModuleDetails.builder().moduleName(Constants.MODULE_CODE)
						.masterDetails(masterDetailsArray).build();

				request = MdmsRequest.builder()
						.mdmsCriteria(MdmsCriteria.builder().moduleDetails(moduleDetailsArray)
								.tenantId(route.getTenantId()).build())
						.requestInfo(routeRequest.getRequestInfo()).build();
				response = mdmsRepository.getByCriteria(request);
				if (response == null || response.getMdmsRes() == null
						|| !response.getMdmsRes().containsKey(Constants.MODULE_CODE)
						|| response.getMdmsRes().get(Constants.MODULE_CODE) == null
						|| !response.getMdmsRes().get(Constants.MODULE_CODE)
								.containsKey(Constants.COLLECTIONTYPE_MASTER_NAME)
						|| response.getMdmsRes().get(Constants.MODULE_CODE)
								.get(Constants.COLLECTIONTYPE_MASTER_NAME) == null) {
					throw new CustomException("CollectionType",
							"Given CollectionType is invalid: " + route.getCollectionType().getCode());
				} else {
					ctResponseList = new ArrayList<CollectionType>();

					responseJSONArray = response.getMdmsRes().get(Constants.MODULE_CODE)
							.get(Constants.COLLECTIONTYPE_MASTER_NAME);

					for (int i = 0; i < responseJSONArray.size(); i++) {
						ctResponseList.add(mapper.convertValue(responseJSONArray.get(i), CollectionType.class));
					}

					if (ctResponseList.isEmpty())
						throw new CustomException("CollectionType",
								"Given CollectionType is invalid: " + route.getCollectionType().getCode());
					else
						route.setCollectionType(ctResponseList.get(0));
				}
			}

			// Validate Starting Collection Point

			if (route.getStartingCollectionPoint() != null && route.getStartingCollectionPoint().getName() != null) {

				CollectionPointSearch search = new CollectionPointSearch();
				search.setTenantId(route.getTenantId());
				search.setName(route.getStartingCollectionPoint().getName());

				Pagination<CollectionPoint> collectionPoints = collectionPointRepository.search(search);

				if (collectionPoints == null || collectionPoints.getPagedData() == null
						|| collectionPoints.getPagedData().isEmpty())
					throw new CustomException("StartingCollectionPoint", "Given StartingCollectionPoint is invalid: "
							+ route.getStartingCollectionPoint().getName());
				else
					route.setStartingCollectionPoint(collectionPoints.getPagedData().get(0));
			}

			// Validate Ending Collection Point

			if (route.getEndingCollectionPoint() != null && route.getEndingCollectionPoint().getName() != null) {

				CollectionPointSearch search = new CollectionPointSearch();
				search.setTenantId(route.getTenantId());
				search.setName(route.getEndingCollectionPoint().getName());

				Pagination<CollectionPoint> collectionPoints = collectionPointRepository.search(search);

				if (collectionPoints == null || collectionPoints.getPagedData() == null
						|| collectionPoints.getPagedData().isEmpty())
					throw new CustomException("EndingCollectionPoint",
							"Given EndingCollectionPoint is invalid: " + route.getEndingCollectionPoint().getName());
				else
					route.setEndingCollectionPoint(collectionPoints.getPagedData().get(0));
			}

			// Validate Dumping Ground
			if (route.getEndingDumpingGroundPoint() != null && route.getEndingDumpingGroundPoint().getName() != null) {
				masterDetailsArray = new MasterDetails[1];
				masterDetailsArray[0] = MasterDetails.builder().name(Constants.DUMPINGGROUND_MASTER_NAME)
						.filter("[?(@.name == '" + route.getEndingDumpingGroundPoint().getName() + "')]").build();
				moduleDetailsArray = new ModuleDetails[1];
				moduleDetailsArray[0] = ModuleDetails.builder().moduleName(Constants.MODULE_CODE)
						.masterDetails(masterDetailsArray).build();

				request = MdmsRequest.builder()
						.mdmsCriteria(MdmsCriteria.builder().moduleDetails(moduleDetailsArray)
								.tenantId(route.getTenantId()).build())
						.requestInfo(routeRequest.getRequestInfo()).build();
				response = mdmsRepository.getByCriteria(request);
				if (response == null || response.getMdmsRes() == null
						|| !response.getMdmsRes().containsKey(Constants.MODULE_CODE)
						|| response.getMdmsRes().get(Constants.MODULE_CODE) == null
						|| !response.getMdmsRes().get(Constants.MODULE_CODE)
								.containsKey(Constants.DUMPINGGROUND_MASTER_NAME)
						|| response.getMdmsRes().get(Constants.MODULE_CODE)
								.get(Constants.DUMPINGGROUND_MASTER_NAME) == null) {
					throw new CustomException("Ending Dumping Ground",
							"Given Ending Dumping Ground is invalid: " + route.getEndingDumpingGroundPoint().getName());
				} else {
					dgResponseList = new ArrayList<DumpingGroundEntity>();

					responseJSONArray = response.getMdmsRes().get(Constants.MODULE_CODE)
							.get(Constants.DUMPINGGROUND_MASTER_NAME);

					for (int i = 0; i < responseJSONArray.size(); i++) {
						dgResponseList.add(mapper.convertValue(responseJSONArray.get(i), DumpingGroundEntity.class));
					}

					if (dgResponseList.isEmpty())
						throw new CustomException("Ending Dumping Ground", "Given Ending Dumping Ground is invalid: "
								+ route.getEndingDumpingGroundPoint().getName());
					else
						route.setEndingDumpingGroundPoint(dgResponseList.get(0).toDomain());
				}
			}

			// Validate CollectionPoints
			if (route.getCollectionPoints() != null)
				for (RouteCollectionPointMap map : route.getCollectionPoints()) {
					if (map.getCollectionPoint() != null && map.getCollectionPoint() != null) {
						CollectionPointSearch search = new CollectionPointSearch();
						search.setTenantId(route.getTenantId());
						search.setName(map.getCollectionPoint());

						Pagination<CollectionPoint> collectionPoints = collectionPointRepository.search(search);

						if (collectionPoints == null || collectionPoints.getPagedData() == null
								|| collectionPoints.getPagedData().isEmpty())
							throw new CustomException("CollectionPoint",
									"Given CollectionPoint is invalid: " + route.getEndingCollectionPoint().getName());
						else
							map.setCollectionPoint(collectionPoints.getPagedData().get(0).getName());
					}
				}
		}
	}

	private void setAuditDetails(Route contract, Long userId) {

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