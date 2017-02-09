package org.egov.filestore.persistence.repository;

import org.apache.commons.io.FileUtils;
import org.egov.filestore.domain.model.Artifact;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Repository
public class DiskFileStoreRepository implements ServletContextAware{

    private String fileStorageMountPath;
    private ServletContext servletContext;

    public DiskFileStoreRepository(@Value("${fileStorageMountPath}") String fileStorageMountPath) {
        this.fileStorageMountPath = fileStorageMountPath;
    }

    public void storeFilesOnDisk(List<Artifact> atrifactsToStore, String tenantId, String module) throws IOException {
        for(Artifact artifactToStore: atrifactsToStore) {
            MultipartFile multipartFile = artifactToStore.getMultipartFile();
            String fileStoreId = artifactToStore.getFileStoreId();

            Path path = Paths.get(fileStorageMountPath, fileStoreId);
            FileUtils.forceMkdirParent(path.toFile());
            multipartFile.transferTo(path.toFile());
        }
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
