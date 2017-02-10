package org.egov.filestore.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URL;

@AllArgsConstructor
@Getter
public class FileLocation {
    private String fileStoreId;
    private String module;
    private String jurisdictionId;

//    public URL getUrl() {
//        String filePath = String.format("%s/%s/%s/%s", fileStorageMountPath, jurisdictionId, module, fileStoreId);
//        return UriComponentsBuilder.fromPath(filePath);
//    };
}
