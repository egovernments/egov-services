package org.egov.filestore.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AzureClientFacade {
	
	@Value("${azure.defaultEndpointsProtocol}")
	private String defaultEndpointsProtocol;
	
	@Value("${azure.accountName}")
	private String accountName;
	
	@Value("${azure.accountKey}")
	private String accountKey;
	
	public CloudBlobClient getAzureClient() {
		StringBuilder storageConnectionString = new StringBuilder();
		storageConnectionString.append("DefaultEndpointsProtocol=").append(defaultEndpointsProtocol).append(";")
		.append("AccountName=").append(accountName).append(";").append("AccountKey=").append(accountKey);
		CloudStorageAccount storageAccount = null;
		CloudBlobClient blobClient = null;
		try {
			storageAccount = CloudStorageAccount.parse(storageConnectionString.toString());
			if(null != storageAccount) {
				blobClient = storageAccount.createCloudBlobClient();
			}
		}catch(Exception e) {
			log.error("Exception while intializing client: ", e);
		}	
		return blobClient;
	}
	
	
}
