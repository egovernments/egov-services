package org.egov.works.services.domain.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.works.services.config.PropertiesManager;
import org.egov.works.services.domain.exception.CustomBindException;
import org.egov.works.services.web.contract.FileStoreResponse;
import org.egov.works.services.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class FileStoreRepository {

    @Autowired
    private RestTemplate restTemplate;

    private String url;

    @Autowired
    private PropertiesManager propertiesManager;

    public FileStoreRepository(final RestTemplate restTemplate,
                                  @Value("${egov.filestore.hostname}") final String fileStoreHostName,
                                  @Value("${egov.filestore.uri}") final String url) {
        this.restTemplate = restTemplate;
        this.url = fileStoreHostName + url;
    }

    public FileStoreResponse searchFileStore(final String tenantId, final String fileStoreId, final RequestInfo requestInfo) {
        FileStoreResponse response = null;
        try {
            response = restTemplate.getForObject(url, FileStoreResponse.class, fileStoreId, tenantId);
        } catch (Exception e) {
            log.error("Error while validating filestore id", e);
        }
        return response;
    }
}
