package org.egov.asset.web.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.AssetRequest;
import org.egov.asset.contract.AssetResponse;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AssetCriteria;
import org.egov.asset.model.Location;
import org.egov.asset.model.enums.ModeOfAcquisition;
import org.egov.asset.model.enums.Status;
import org.egov.asset.service.AssetService;
import org.egov.asset.util.FileUtils;
import org.egov.asset.web.validator.AssetValidator;
import org.egov.common.contract.response.ResponseInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(AssetController.class)
public class AssetControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private AssetService assetService;
	
	@MockBean
	private ApplicationProperties applicationProperties;

	@MockBean
	private AssetValidator assetValidator;
	
	@Test
	public void test_Should_Search_Asset() throws Exception{
		
		List<Asset> assets = new ArrayList<>();
		assets.add(getAsset());
		
		when(assetService.getAssets(any(AssetCriteria.class))).thenReturn(assets); 

		mockMvc.perform(post("/assets/_search")
	        		.param("code","000013")
	        		.param("tenantId", "ap.kurnool")
	        		.param("assetCategory", "1")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(getFileContents("requestinfowrapper.json")))
	                .andExpect(status().isOk())
	                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	                .andExpect(content().json(getFileContents("assetsearchresponse.json")));
	}
	
	@Test
	public void test_Should_Create_Asset() throws Exception{
		
		List<Asset> assets = new ArrayList<>();
		Asset asset = getAsset();
		assets.add(asset);
		AssetResponse assetResponse = new AssetResponse();
		assetResponse.setAssets(assets);
		assetResponse.setResponseInfo(new ResponseInfo());
		
		when(assetService.createAsync(any(AssetRequest.class))).thenReturn(assetResponse); 

		mockMvc.perform(post("/assets/_create")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(getFileContents("assetcreaterequest.json")))
	                .andExpect(status().isCreated())
	                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	                .andExpect(content().json(getFileContents("assetcreateresponse.json")));
	}
	
	@Test
	public void test_Should_Update_Asset() throws Exception{
		
		List<Asset> assets = new ArrayList<>();
		Asset asset = getAsset();
		asset.setCode("13");
		assets.add(asset);
		AssetResponse assetResponse = new AssetResponse();
		assetResponse.setAssets(assets);
		assetResponse.setResponseInfo(new ResponseInfo());
		
		when(assetService.updateAsync(any(AssetRequest.class))).thenReturn(assetResponse); 

		mockMvc.perform(post("/assets/_update/{code}","13")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(getFileContents("assetupdaterequest.json")))
	                .andExpect(status().isOk())
	                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	                .andExpect(content().json(getFileContents("assetupdateresponse.json")));
	}

	private String getFileContents(String fileName) throws IOException {
		return new FileUtils().getFileContents(fileName);
	}
	
	private Asset getAsset() {
		Asset asset = new Asset();
		asset.setTenantId("ap.kurnool");
		asset.setName("asset name");
		asset.setStatus(Status.CWIP);
		asset.setModeOfAcquisition(ModeOfAcquisition.ACQUIRED);
		
		Location location = new Location();
		location.setLocality(4l);
		location.setDoorNo("door no");
		
		AssetCategory assetCategory = new AssetCategory();
		assetCategory.setId(1l);
		assetCategory.setName("category name");
		
		asset.setLocationDetails(location);
		asset.setAssetCategory(assetCategory);
		return asset;
	}
}
