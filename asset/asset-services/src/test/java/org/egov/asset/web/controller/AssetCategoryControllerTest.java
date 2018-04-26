/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

package org.egov.asset.web.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.asset.TestConfiguration;
import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.AssetCategoryRequest;
import org.egov.asset.contract.AssetCategoryResponse;
import org.egov.asset.exception.ErrorResponse;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AssetCategoryCriteria;
import org.egov.asset.model.enums.AssetCategoryType;
import org.egov.asset.model.enums.DepreciationMethod;
import org.egov.asset.service.AssetCategoryService;
import org.egov.asset.service.AssetCommonService;
import org.egov.asset.util.FileUtils;
import org.egov.asset.web.validator.AssetCategoryValidator;
import org.egov.asset.web.wrapperfactory.ResponseInfoFactory;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

@RunWith(SpringRunner.class)
@WebMvcTest(AssetCategoryController.class)
@Import(TestConfiguration.class)
public class AssetCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private AssetCategoryController AssetCategoryController;

    @MockBean
    private AssetCategoryService assetCategoryService;

    @MockBean
    private ApplicationProperties applicationProperties;

    @MockBean
    private AssetCategoryValidator assetCategoryValidator;

    @MockBean
    private AssetCommonService assetCommonService;

    @MockBean
    private ResponseInfoFactory responseInfoFactory;

    @Test
    public void test_Should_Search_AssetCategory() throws Exception {

        final List<AssetCategory> assetCategories = new ArrayList<>();
        assetCategories.add(getAssetCategory());

        when(assetCategoryService.search(any(AssetCategoryCriteria.class))).thenReturn(assetCategories);

        mockMvc.perform(post("/assetCategories/_search").param("tenantId", "ap.kurnool")
                .contentType(MediaType.APPLICATION_JSON).content(getFileContents("requestinfowrapper.json")))
                .andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(getFileContents("assetcategorysearchresponse.json")));
    }

    @Test
    public void test_Should_Create_AssetCategory() throws Exception {

        final List<AssetCategory> assetCategories = new ArrayList<>();
        assetCategories.add(getAssetCategory());

        final ResponseInfo resInfo = new ResponseInfo();

        when(assetCategoryService.createAssetCategory(any(AssetCategoryRequest.class))).thenReturn(getAssetCategory());
        when(responseInfoFactory.createResponseInfoFromRequestHeaders(any(RequestInfo.class))).thenReturn(resInfo);

        mockMvc.perform(post("/assetCategories/_create").contentType(MediaType.APPLICATION_JSON)
                .content(getFileContents("assetcategorycreaterequest.json"))).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(getFileContents("assetcategorysearchresponse.json")));
    }

    @Test
    public void test_Should_Update_AssetCategory() throws Exception {

        final List<AssetCategory> assetCategories = new ArrayList<>();
        assetCategories.add(getUpdateAssetCategory());
        final AssetCategoryResponse assetCategoryResponse = new AssetCategoryResponse();
        assetCategoryResponse.setAssetCategory(assetCategories);
        final ResponseInfo resInfo = new ResponseInfo();

        when(assetCategoryService.updateAssetCategory(any(AssetCategoryRequest.class))).thenReturn(getUpdateAssetCategory());
        when(responseInfoFactory.createResponseInfoFromRequestHeaders(any(RequestInfo.class))).thenReturn(resInfo);
        mockMvc.perform(post("/assetCategories/_update").contentType(MediaType.APPLICATION_JSON)
                .content(getFileContents("assetcategoryupdaterequest.json"))).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(getFileContents("assetcategoryupdateresponse.json")));
    }

    @Test
    public void test_error_assetCategorySearch() throws IOException, Exception {
        final ErrorResponse errorResponse = getErrorResponse();

        when(assetCommonService.populateErrors(any(BindingResult.class))).thenReturn(errorResponse);

        mockMvc.perform(post("/assetCategories/_search").contentType(MediaType.APPLICATION_JSON)
                .content(getFileContents("requestinfowrapper.json"))).andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(getFileContents("errorresponse.json")));
    }

    @Test
    public void test_error_assetCategoryCreate() throws IOException, Exception {
        final ErrorResponse errorResponse = getErrorResponse();

        when(assetCommonService.populateErrors(any(BindingResult.class))).thenReturn(errorResponse);

        mockMvc.perform(post("/assetCategories/_create").contentType(MediaType.APPLICATION_JSON)
                .content(getFileContents("assetcategoryerrorrequest.json"))).andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(getFileContents("errorresponse.json")));
    }

    @Test
    public void test_error_assetCategoryUpdate() throws IOException, Exception {
        final ErrorResponse errorResponse = getErrorResponse();

        when(assetCommonService.populateErrors(any(BindingResult.class))).thenReturn(errorResponse);

        mockMvc.perform(post("/assetCategories/_update").contentType(MediaType.APPLICATION_JSON)
                .content(getFileContents("assetcategoryerrorrequest.json"))).andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(getFileContents("errorresponse.json")));
    }

    private ErrorResponse getErrorResponse() {
        final ErrorResponse errorResponse = new ErrorResponse();
        final org.egov.asset.exception.Error error = new org.egov.asset.exception.Error();
        error.setCode(400);
        error.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        error.setDescription(HttpStatus.BAD_REQUEST.toString());
        errorResponse.setResponseInfo(new ResponseInfo());
        errorResponse.setError(error);
        return errorResponse;
    }

    private String getFileContents(final String fileName) throws IOException {
        return new FileUtils().getFileContents(fileName);
    }

    private AssetCategory getAssetCategory() {

        final AssetCategory assetCategory = new AssetCategory();
        assetCategory.setTenantId("ap.kurnool");
        assetCategory.setVersion("v1");
        assetCategory.setName("assetcategory3");
        assetCategory.setAssetCategoryType(AssetCategoryType.IMMOVABLE);
        assetCategory.setDepreciationMethod(DepreciationMethod.STRAIGHT_LINE_METHOD);
        assetCategory.setIsAssetAllow(true);
        assetCategory.setAssetAccount(2l);

        return assetCategory;
    }

    private AssetCategory getUpdateAssetCategory() {

        final AssetCategory assetCategory = new AssetCategory();
        assetCategory.setTenantId("ap.kurnool");
        assetCategory.setId(2L);
        assetCategory.setName("assetcategory3");
        assetCategory.setCode("2");
        assetCategory.setAssetCategoryType(AssetCategoryType.IMMOVABLE);
        assetCategory.setParent(2L);
        assetCategory.setDepreciationMethod(DepreciationMethod.STRAIGHT_LINE_METHOD);
        assetCategory.setIsAssetAllow(true);
        assetCategory.setAssetAccount(2l);
        assetCategory.setAccumulatedDepreciationAccount(2l);
        assetCategory.setRevaluationReserveAccount(5l);
        assetCategory.setDepreciationExpenseAccount(3l);
        assetCategory.setUnitOfMeasurement(10L);
        assetCategory.setVersion("v1");
        assetCategory.setDepreciationRate(null);

        return assetCategory;
    }

}
