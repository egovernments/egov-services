package org.egov.filestore.domain.model;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
public class Artifact {
    private MultipartFile multipartFile;
    private String fileStoreId;
    private String module;
    private String jurisdictionId;
    private String tag;
}

