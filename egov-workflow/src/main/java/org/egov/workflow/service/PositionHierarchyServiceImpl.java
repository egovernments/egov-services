package org.egov.workflow.service;

import java.util.List;

import org.egov.workflow.model.PositionHierarchyResponse;
import org.egov.workflow.model.PositionHierarchyServiceResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PositionHierarchyServiceImpl implements PositionHierarchyService {

	@Value("${egov.services.positionhierarchy_service.host}")
	private String positionHierarchyServiceHost;

	@Override
	public List<PositionHierarchyResponse> getByObjectTypeObjectSubTypeAndFromPosition(String objectType,
			String objectSubType, Long fromPositionid) {
		String url = positionHierarchyServiceHost
				+ "eis/positionhierarchys?psoitionHierarchy.objectType.type={objectType}&psoitionHierarchy.objectSubType={objectSubType}&psoitionHierarchy.fromPosition.id={fromPosition}";
		return getPositionHierarchyServiceResponse(url, objectType, objectSubType, fromPositionid)
				.getPositionHierarchy();
	}

	private PositionHierarchyServiceResponse getPositionHierarchyServiceResponse(final String url, String objectType,
			String objectSubType, Long fromPositionid) {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(url, PositionHierarchyServiceResponse.class, objectType, objectSubType,
				fromPositionid);
	}

}
