package org.egov.filestore.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.commons.io.FilenameUtils;
import org.egov.filestore.domain.model.Artifact;
import org.egov.filestore.repository.AzureClientFacade;
import org.egov.filestore.repository.SaveFiles;
import org.egov.tracer.model.CustomException;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.microsoft.azure.storage.OperationContext;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.azure.storage.blob.BlobRequestOptions;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@ConditionalOnProperty(value = "isAzureStorageEnabled", havingValue = "true")
public class SaveToAzureBlobStorage implements SaveFiles {

	private CloudBlobClient azureBlobClient;
	
	@Autowired
	private AzureClientFacade azureFacade;
	
	@Autowired
	private FileSaveUtils util;
	
	@Value("${is.container.fixed}")
	private Boolean isContainerFixed;
	
	@Value("${fixed.container.name}")
	private String fixedContainerName;
	
	/**
	 * Azure specific implementation
	 * 
	 */
	@Override
	public void save(List<Artifact> artifacts) {
		if(null == azureBlobClient)
			azureBlobClient = azureFacade.getAzureClient();
		
		artifacts.forEach(artifact -> {
			CloudBlobContainer container= null;
			String completeName = artifact.getFileLocation().getFileName();
			int index = completeName.indexOf('/');
			String containerName = completeName.substring(0, index);
			String fileNameWithPath = completeName.substring(index + 1, completeName.length());
			try {
				if(isContainerFixed)
					container = azureBlobClient.getContainerReference(fixedContainerName);
				else
					container = azureBlobClient.getContainerReference(containerName);
				container.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(), new OperationContext());	
				if(artifact.getMultipartFile().getContentType().startsWith("image/")) {
					String extension = FilenameUtils.getExtension(artifact.getMultipartFile().getOriginalFilename());
					Map<String, BufferedImage> mapOfImagesAndPaths = util.createVersionsOfImage(artifact.getMultipartFile(), fileNameWithPath);
					for(String key: mapOfImagesAndPaths.keySet()) {
						upload(container, fileNameWithPath, null, mapOfImagesAndPaths.get(key), extension);
						mapOfImagesAndPaths.get(key).flush();
					}
				}else {
					upload(container, fileNameWithPath, artifact.getMultipartFile(), null, null);
				}
				for (ListBlobItem blobItem : container.listBlobs())
					log.info("URI of blob is: " + blobItem.getUri());
			}catch(Exception e) {
				log.error("Exceptione while creating the container: ", e);
			}
			
		});			
	}
	
	/**
	 * Uploads the file to Azure Blob Storage
	 * 
	 * @param container
	 * @param completePath
	 * @param file
	 * @param image
	 * @param extension
	 */
	public void upload(CloudBlobContainer container, String completePath, MultipartFile file, BufferedImage image, String extension) {
		try{
			if(null == file && null != image) {
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				ImageIO.write(image, extension, os);
				CloudBlockBlob blob = container.getBlockBlobReference(completePath);
				blob.upload(new ByteArrayInputStream(os.toByteArray()), 8*1024*1024);
			}else {
				CloudBlockBlob blob = container.getBlockBlobReference(completePath);
				blob.upload(file.getInputStream(), file.getSize());
			}

		}catch(Exception e) {
			log.error("Exception while uploading the file: ",e);
		}
	}
	
	
}
