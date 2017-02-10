package org.egov.filestore.web.controller;

import org.egov.filestore.domain.service.StorageService;
import org.egov.filestore.web.contract.File;
import org.egov.filestore.web.contract.StorageResponse;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/files")
public class StorageController {

    private StorageService storageService;

    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping()
    public ResponseEntity<Resource> getFile() {
        return ResponseEntity.ok(new FileSystemResource("application.properties"));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public StorageResponse storeFiles(@RequestParam("file") List<MultipartFile> files,
                                      @RequestParam("jurisdictionId") String jurisdictionId,
                                      @RequestParam("module") String module) throws IOException {

        return getStorageResponse(storageService.save(files, jurisdictionId, module));
    }

    private StorageResponse getStorageResponse(List<String> fileStorageIds) {
        List<File> files = fileStorageIds
                .stream()
                .map(File::new)
                .collect(Collectors.toList());
        return new StorageResponse(files);

    }


}
