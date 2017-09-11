package org.egov.collection.service;

import java.util.List;
import java.util.Map;

import org.egov.collection.repository.CollectionConfigRepository;
import org.egov.collection.web.contract.CollectionConfigGetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CollectionConfigService {
	
	@Autowired
	private CollectionConfigRepository collectionConfigRepository;

	public Map<String, List<String>> getCollectionConfiguration(CollectionConfigGetRequest collectionConfigGetRequest) {
		return collectionConfigRepository.findForCriteria(collectionConfigGetRequest);
	}

}
