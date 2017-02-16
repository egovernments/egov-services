package org.egov.filestore.web.controller;

import org.egov.filestore.domain.service.StorageService;
import org.egov.filestore.web.contract.File;
import org.egov.filestore.web.contract.LocationResponse;
import org.egov.filestore.web.contract.StorageResponse;
import org.egov.filestore.web.contract.Url;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@Controller
@RequestMapping("/files")
public class StorageController {

    private StorageService storageService;
    private String fileStoreHost;
    private final static String PATH_SEPARATOR = "/";

    public StorageController(StorageService storageService,
                             @Value("${file.store.host}") String fileStoreHost) {
        this.storageService = storageService;
        this.fileStoreHost = fileStoreHost;
    }

    @GetMapping("/{fileStoreId}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable("fileStoreId") String fileStoreId) {
        org.egov.filestore.domain.model.Resource resource = storageService.retrieve(fileStoreId);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFileName() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, resource.getContentType())
                .body(resource.getResource());
    }

    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public LocationResponse getUrlListByTag(@RequestParam("tag") String tag) {
        return getLocationResponse(storageService.retrieveByTag(tag));
    }

    @PostMapping(produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public StorageResponse storeFiles(@RequestParam("file") List<MultipartFile> files,
                                      @RequestParam("jurisdictionId") String jurisdictionId,
                                      @RequestParam("module") String module,
                                      @RequestParam(value = "tag", required = false) String tag){

        return getStorageResponse(storageService.save(files, jurisdictionId, module, tag));
    }

    private StorageResponse getStorageResponse(List<String> fileStorageIds) {
        List<File> files = fileStorageIds
                .stream()
                .map(File::new)
                .collect(Collectors.toList());
        return new StorageResponse(files);

    }

    private LocationResponse getLocationResponse(List<String> fileStorageIds) {
        return new LocationResponse(
                fileStorageIds.stream()
                        .map(fileStorageId -> new Url(constructUrl(fileStorageId)))
                        .collect(Collectors.toList())
        );
    }

    private String constructUrl(String fileStorageId) {
        return String.format("%s%s%s%s%s", fileStoreHost, PATH_SEPARATOR, "files", PATH_SEPARATOR, fileStorageId);
    }

}
