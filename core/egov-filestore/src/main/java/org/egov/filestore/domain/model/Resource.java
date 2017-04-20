package org.egov.filestore.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Resource {
    private String contentType;
    private String fileName;
    private org.springframework.core.io.Resource resource;
    private String tenantId;
}
