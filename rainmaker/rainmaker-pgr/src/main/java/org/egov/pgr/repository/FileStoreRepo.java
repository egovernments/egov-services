package org.egov.pgr.repository;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class FileStoreRepo {

	@Value("${egov.filestore.host}")
	private String fileStoreHost;

	@Value("${egov.filestore.url.endpoint}")
	private String urlEndPoint;

	private static final String TENANTID_PARAM = "tenantId=";

	private static final String FILESTORE_ID_LIST_PARAM = "fileStoreIds=";

	@Autowired
	private RestTemplate restTemplate;

	@SuppressWarnings("unchecked")
	public Map<String, String> getUrlMaps(String tenantId, List<String> fileStoreIds) {

		return restTemplate.getForObject(
				fileStoreHost + urlEndPoint + "?" + TENANTID_PARAM + tenantId + FILESTORE_ID_LIST_PARAM + fileStoreIds,
				Map.class);
	}
}
