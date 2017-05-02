package org.egov.asset.web.controller;

import static org.junit.Assert.*;

import java.io.IOException;

import org.egov.asset.util.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AssetMasterController.class)
public class AssetMasterControllerTest {

		@Autowired 
		private MockMvc mockMvc;
		
		@Test
		public void test_Should_Return_Status() throws Exception{
		       
		        mockMvc.perform(get("/GET_STATUS"))
		                .andExpect(status().isOk())
		                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		                .andExpect(content().json(getFileContents("status.json")));
		}
		
		@Test
		public void test_Should_Return_AssetCategoryType() throws Exception{
		       
		        mockMvc.perform(get("/GET_ASSET_CATEGORY_TYPE"))
		                .andExpect(status().isOk())
		                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		                .andExpect(content().json(getFileContents("assetcategorytype.json")));
		}
		
		@Test
		public void test_Should_Return_DepreciationMethod() throws Exception{
		       
		        mockMvc.perform(get("/GET_DEPRECIATION_METHOD"))
		                .andExpect(status().isOk())
		                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		                .andExpect(content().json(getFileContents("depreciationmethod.json")));
		}
		
		@Test
		public void test_Should_Return_ModeOfAcquisition() throws Exception{
		       
		        mockMvc.perform(get("/GET_MODE_OF_ACQUISITION"))
		                .andExpect(status().isOk())
		                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		                .andExpect(content().json(getFileContents("modeofacquisition.json")));
		}

		private String getFileContents(String fileName) throws IOException {
			return new FileUtils().getFileContents(fileName);
		}
	}