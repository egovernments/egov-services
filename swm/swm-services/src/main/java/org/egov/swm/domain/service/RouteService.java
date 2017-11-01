package org.egov.swm.domain.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import org.egov.swm.constants.Constants;
import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.CollectionPoint;
import org.egov.swm.domain.model.CollectionPointSearch;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.Route;
import org.egov.swm.domain.model.RouteCollectionPointMap;
import org.egov.swm.domain.model.RouteSearch;
import org.egov.swm.domain.repository.CollectionPointRepository;
import org.egov.swm.domain.repository.RouteRepository;
import org.egov.swm.persistence.entity.DumpingGroundEntity;
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
			r.setCode(UUID.randomUUID().toString().replace("-", ""));

			populateCollectionPointDetails(r);

		}
		return routeRepository.save(routeRequest);

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

	private void populateCollectionPointDetails(Route r) {

		if (r.getCollectionPoints() != null) {
			RouteCollectionPointMap map;
			r.setRouteCollectionPointMaps(new ArrayList<RouteCollectionPointMap>());
			for (CollectionPoint cp : r.getCollectionPoints()) {
				map = new RouteCollectionPointMap();
				map.setTenantId(r.getTenantId());
				map.setRoute(r.getName());
				map.setCollectionPoint(cp.getCode());
				map.setAuditDetails(r.getAuditDetails());
				r.getRouteCollectionPointMaps().add(map);
			}
		}
	}

	public Pagination<Route> search(RouteSearch routeSearch) {

		return routeRepository.search(routeSearch);
	}

	private void validate(RouteRequest routeRequest) {

		JSONArray responseJSONArray = null;
		ObjectMapper mapper = new ObjectMapper();

		for (Route route : routeRequest.getRoutes()) {

			// Validate Starting Collection Point

			if (route.getStartingCollectionPoint() != null && route.getStartingCollectionPoint().getCode() != null) {

				CollectionPointSearch search = new CollectionPointSearch();
				search.setTenantId(route.getTenantId());
				search.setCode(route.getStartingCollectionPoint().getCode());

				Pagination<CollectionPoint> collectionPoints = collectionPointRepository.search(search);

				if (collectionPoints == null || collectionPoints.getPagedData() == null
						|| collectionPoints.getPagedData().isEmpty())
					throw new CustomException("StartingCollectionPoint", "Given StartingCollectionPoint is invalid: "
							+ route.getStartingCollectionPoint().getCode());
				else
					route.setStartingCollectionPoint(collectionPoints.getPagedData().get(0));
			}

			// Validate Ending Collection Point

			if (route.getEndingCollectionPoint() != null && route.getEndingCollectionPoint().getCode() != null) {

				CollectionPointSearch search = new CollectionPointSearch();
				search.setTenantId(route.getTenantId());
				search.setCode(route.getEndingCollectionPoint().getCode());

				Pagination<CollectionPoint> collectionPoints = collectionPointRepository.search(search);

				if (collectionPoints == null || collectionPoints.getPagedData() == null
						|| collectionPoints.getPagedData().isEmpty())
					throw new CustomException("EndingCollectionPoint",
							"Given EndingCollectionPoint is invalid: " + route.getEndingCollectionPoint().getCode());
				else
					route.setEndingCollectionPoint(collectionPoints.getPagedData().get(0));
			}

			// Validate Ending Dumping ground
			if (route.getEndingDumpingGroundPoint() != null && route.getEndingDumpingGroundPoint().getCode() != null) {

				responseJSONArray = mdmsRepository.getByCriteria(route.getTenantId(), Constants.MODULE_CODE,
						Constants.DUMPINGGROUND_MASTER_NAME, "code", route.getEndingDumpingGroundPoint().getCode(),
						routeRequest.getRequestInfo());

				if (responseJSONArray != null && responseJSONArray.size() > 0)
					route.setEndingDumpingGroundPoint(
							mapper.convertValue(responseJSONArray.get(0), DumpingGroundEntity.class).toDomain());
				else
					throw new CustomException("DumpingGround",
							"Given DumpingGround is invalid: " + route.getEndingDumpingGroundPoint().getCode());

			}

			// Validate CollectionPoints
			if (route.getCollectionPoints() != null)
				for (CollectionPoint cp : route.getCollectionPoints()) {
					if (cp != null) {
						CollectionPointSearch search = new CollectionPointSearch();
						search.setTenantId(route.getTenantId());
						search.setCode(cp.getCode());

						Pagination<CollectionPoint> collectionPoints = collectionPointRepository.search(search);

						if (collectionPoints == null || collectionPoints.getPagedData() == null
								|| collectionPoints.getPagedData().isEmpty())
							throw new CustomException("CollectionPoint",
									"Given CollectionPoint is invalid: " + route.getEndingCollectionPoint().getName());
						else
							cp = collectionPoints.getPagedData().get(0);
					}
				}
		}
	}

	private void setAuditDetails(Route contract, Long userId) {

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