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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

import static org.egov.filestore.web.controller.StorageController.MAPPED_PATH;

@Controller
@RequestMapping(MAPPED_PATH)
public class StorageController {

    static final String MAPPED_PATH = "files";
    private StorageService storageService;
    private String fileStoreHost;
    private String fileStoreHostScheme;
    private String contextPath;

    public StorageController(StorageService storageService,
                             @Value("${fileStoreHost}") String fileStoreHost,
                             @Value("${fileStoreHostScheme}") String fileStoreHostScheme,
                             @Value("${server.contextPath}") String contextPath) {
        this.storageService = storageService;
        this.fileStoreHost = fileStoreHost;
        this.fileStoreHostScheme = fileStoreHostScheme;
        this.contextPath = contextPath;
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

    @GetMapping()
    @ResponseBody
    public LocationResponse getUrlListByTag(@RequestParam("tag") String tag) {
        return getLocationResponse(storageService.retrieveByTag(tag));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
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

        return UriComponentsBuilder
                .newInstance()
                .scheme(fileStoreHostScheme)
                .host(fileStoreHost)
                .pathSegment(contextPath, MAPPED_PATH, fileStorageId)
                .build()
                .toUriString();
    }

}
