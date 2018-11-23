package org.egov.infra.indexer.custom.pgr.models;

import java.util.ArrayList;
import java.util.List;

import org.egov.infra.indexer.util.IndexerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class PGRCustomDecorator {
	
	@Autowired
	private IndexerUtils indexerUtils;
	
	public PGRIndexObject dataTransformationForPGR(ServiceResponse serviceResponse) {
		PGRIndexObject indexObject = new PGRIndexObject();
		ObjectMapper mapper = indexerUtils.getObjectMapper();
		List<ServiceIndexObject> indexObjects = new ArrayList<>();
		for(int i = 0; i < serviceResponse.getServices().size(); i++) {
			ServiceIndexObject object = new ServiceIndexObject();
			object = mapper.convertValue(serviceResponse.getServices().get(i), ServiceIndexObject.class);
			object.setActionHistory(serviceResponse.getActionHistory().get(i));
			indexObjects.add(object);
		}
		indexObject.setServiceRequests(indexObjects);
		
		return indexObject;
	}

}
