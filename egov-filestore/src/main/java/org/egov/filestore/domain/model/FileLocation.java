package org.egov.filestore.domain.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URL;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class FileLocation {
    private String fileStoreId;
    private String module;
    private String jurisdictionId;
    private String tag;
}
