package org.egov.filestore.web.controller;

import org.egov.filestore.domain.service.FileStorageService;
import org.egov.filestore.persistence.entity.FileStoreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/files")
public class FileController {

    private FileStorageService fileStorageService;

    @Autowired
    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @GetMapping()
    public ResponseEntity<Resource> getFile() {
        return ResponseEntity.ok(new FileSystemResource("application.properties"));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FileStoreMapper>> storeFile(@RequestParam("file") List<MultipartFile> files,
                                                           @RequestParam("tenantId") String tenantId,
                                                           @RequestParam("module") String module) throws IOException {

        return ResponseEntity.status(HttpStatus.CREATED).body(fileStorageService.storeFiles(files, tenantId, module));
    }
}
