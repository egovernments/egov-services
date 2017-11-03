package org.egov.works.services.web.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileStoreResponse {
    private String contentType;
    private String fileName;
    private org.springframework.core.io.Resource resource;
    private String tenantId;
}
