package org.egov.asset.web.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.asset.contract.AssetConfigurationResponse;
import org.egov.asset.model.AssetConfigurationCriteria;
import org.egov.asset.model.enums.AssetConfigurationKeys;
import org.egov.asset.service.AssetCommonService;
import org.egov.asset.service.AssetConfigurationService;
import org.egov.asset.util.FileUtils;
import org.egov.common.contract.response.ResponseInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(AssetConfigurationController.class)
public class AssetConfigurationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AssetConfigurationService assetConfigurationService;

    @MockBean
    private AssetCommonService assetCommonService;

    @Test
    public void test_Should_Return_Config_Value() throws Exception {
        final Map<String, List<String>> assetConfiguration = new HashMap<String, List<String>>();
        final List<String> assetConfigValues = new ArrayList<String>();
        assetConfigValues.add("true");
        assetConfiguration.put(AssetConfigurationKeys.ENABLEVOUCHERGENERATION.toString(), assetConfigValues);
        
        final AssetConfigurationResponse assetConfigurationResponse = new AssetConfigurationResponse();
        assetConfigurationResponse.setAssetConfiguration(assetConfiguration);
        assetConfigurationResponse.setResponseInfo(new ResponseInfo());

        when(assetConfigurationService.search(Matchers.any(AssetConfigurationCriteria.class)))
                .thenReturn(assetConfigurationResponse);

        mockMvc.perform(post("/assetconfigurations/_search")
                .param("name", AssetConfigurationKeys.ENABLEVOUCHERGENERATION.toString()).param("tenantId", "default")
                .contentType(MediaType.APPLICATION_JSON).content(getFileContents("requestinfowrapper.json")))
                .andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(getFileContents("configuration.json")));
    }

    private String getFileContents(final String fileName) throws IOException {
        return new FileUtils().getFileContents(fileName);
    }
}
