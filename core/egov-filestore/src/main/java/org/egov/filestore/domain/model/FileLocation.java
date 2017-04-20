package org.egov.filestore.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FileLocation {
    private String fileStoreId;
    private String module;
   // private String jurisdictionId;
    private String tag;
    private String tenantId;
}
