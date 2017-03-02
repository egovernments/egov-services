package org.egov.filestore.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class FileInfo {
    private String contentType;
    private FileLocation fileLocation;
}
