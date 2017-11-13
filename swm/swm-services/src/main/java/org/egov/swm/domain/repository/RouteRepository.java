package org.egov.swm.domain.repository;

import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.Route;
import org.egov.swm.domain.model.RouteSearch;
import org.egov.swm.persistence.queue.repository.RouteQueueRepository;
import org.egov.swm.persistence.repository.RouteJdbcRepository;
import org.egov.swm.web.requests.RouteRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RouteRepository {

	@Autowired
	private RouteJdbcRepository routeJdbcRepository;

	@Autowired
	private RouteQueueRepository routeQueueRepository;

	public RouteRequest save(RouteRequest routeRequest) {

		return routeQueueRepository.save(routeRequest);

	}

	public RouteRequest update(RouteRequest routeRequest) {

		return routeQueueRepository.update(routeRequest);

	}

	public Pagination<Route> search(RouteSearch routeSearch) {
		return routeJdbcRepository.search(routeSearch);

	}
	
	public Boolean uniqueCheck(String tenantId, String fieldName, String fieldValue, String uniqueFieldName,
			String uniqueFieldValue) {

		return routeJdbcRepository.uniqueCheck(tenantId, fieldName, fieldValue, uniqueFieldName, uniqueFieldValue);
	}


}