package org.egov.filestore.web.contract;

import org.egov.filestore.domain.model.FileInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ResponseFactory {

    private String contextPath;
    private final String PATH_SEPERATOR = "/";

    public ResponseFactory(@Value("${server.contextPath}") String contextPath) {
        this.contextPath = contextPath;
    }

    public GetFilesByTagResponse getFilesByTagResponse(List<FileInfo> listOfFileInfo) {
        List<FileRecord> fileRecords = listOfFileInfo.stream().map(fileInfo -> {
            String url = String.format("%s%s%s%s%s", contextPath, PATH_SEPERATOR, "files",
                    PATH_SEPERATOR, fileInfo.getFileLocation().getFileStoreId());
            return new FileRecord(url, fileInfo.getContentType());
        }).collect(Collectors.toList());

        return new GetFilesByTagResponse(fileRecords);
    }


}
