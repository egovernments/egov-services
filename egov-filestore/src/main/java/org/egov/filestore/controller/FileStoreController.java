package org.egov.filestore.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.egov.filestore.entity.FileStoreMapper;
import org.egov.filestore.service.FileStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/filestoremapper")
public class FileStoreController {

	@Autowired
	private FileStoreService fileStoreService;

	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String store(@RequestParam("file") final MultipartFile[] files, @RequestParam("tenant") final String tenant,
			@RequestParam("module") final String module) {
		StringBuilder fileStoreIds = new StringBuilder();
		if (files.length != 0) {
			for (int i = 0; i <= files.length - 1; i++) {
				try {
					FileStoreMapper fileStore = fileStoreService.store(files[i].getInputStream(),
							files[i].getOriginalFilename(), files[i].getContentType(), module, tenant);
					fileStoreService.saveFileStoreMapper(fileStore);
					fileStoreIds.append(fileStore.getFileStoreId()).append(",");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return fileStoreIds.toString();
	}

	@RequestMapping(value = "/fetch", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Set<File> fetchDocument(@RequestParam("fileStoreIds") final String[] fileStoreIds,
			@RequestParam("module") final String module) {
		Set<FileStoreMapper> fileStoreMapperSet = new HashSet<FileStoreMapper>();
		Set<File> files = new HashSet<File>();
		if (fileStoreIds.length != 0) {
			for (int i = 0; i <= fileStoreIds.length - 1; i++) {
				fileStoreMapperSet.add(fileStoreService.findByFileStoreId(fileStoreIds[i]));
			}
			files = fileStoreService.fetchAll(fileStoreMapperSet, module);
		}
		return files;
	}
}
