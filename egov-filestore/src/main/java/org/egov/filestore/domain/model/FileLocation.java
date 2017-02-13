package org.egov.filestore.domain.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class FileLocation {
    private String fileStoreId;
    private String module;
    private String jurisdictionId;
    private String tag;
}
