package org.egov.filestore.domain.model;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
public class Artifact {

    private MultipartFile multipartFile;
    private String fileStoreId;
    private String module;
    private String jurisdictionId;
    private String fileStorageMountPath;

    public Path getPath() {
        return Paths.get(fileStorageMountPath, jurisdictionId, module, fileStoreId);
    }
}
