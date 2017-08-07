package org.egov.asset.web.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.egov.asset.TestConfiguration;
import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.AssetRequest;
import org.egov.asset.contract.AssetResponse;
import org.egov.asset.contract.DisposalRequest;
import org.egov.asset.contract.DisposalResponse;
import org.egov.asset.contract.RevaluationRequest;
import org.egov.asset.contract.RevaluationResponse;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AssetCriteria;
import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.Disposal;
import org.egov.asset.model.DisposalCriteria;
import org.egov.asset.model.Location;
import org.egov.asset.model.Revaluation;
import org.egov.asset.model.RevaluationCriteria;
import org.egov.asset.model.enums.ModeOfAcquisition;
import org.egov.asset.model.enums.Status;
import org.egov.asset.model.enums.TransactionType;
import org.egov.asset.model.enums.TypeOfChangeEnum;
import org.egov.asset.service.AssetCommonService;
import org.egov.asset.service.AssetService;
import org.egov.asset.service.CurrentValueService;
import org.egov.asset.service.DepreciationService;
import org.egov.asset.service.DisposalService;
import org.egov.asset.service.RevaluationService;
import org.egov.asset.util.FileUtils;
import org.egov.asset.web.validator.AssetValidator;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(AssetController.class)
@Import(TestConfiguration.class)
public class AssetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AssetService assetService;

    @MockBean
    private ApplicationProperties applicationProperties;

    @MockBean
    private AssetValidator assetValidator;

    @MockBean
    private RevaluationService revaluationService;

    @MockBean
    private CurrentValueService currentValueService;

    @MockBean
    private DisposalService disposalService;

    @MockBean
    private AssetCommonService assetCommonService;
    
    @MockBean
    private DepreciationService depreciationService;

    @Test
    public void test_Should_Search_Asset() throws Exception {
        final List<Asset> assets = new ArrayList<>();
        assets.add(getAsset());

        final AssetResponse assetResponse = new AssetResponse();
        assetResponse.setAssets(assets);
        assetResponse.setResponseInfo(new ResponseInfo());

        when(assetService.getAssets(Matchers.any(AssetCriteria.class), Matchers.any(RequestInfo.class)))
                .thenReturn(assetResponse);

        mockMvc.perform(post("/assets/_search").param("code", "000013").param("tenantId", "ap.kurnool")
                .param("assetCategory", "1").contentType(MediaType.APPLICATION_JSON)
                .content(getFileContents("requestinfowrapper.json"))).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(getFileContents("assetsearchresponse.json")));
    }

    @Test
    public void test_Should_ThrowException_OnSearchAsset() throws Exception {
        final List<Asset> assets = new ArrayList<>();
        assets.add(getAsset());

        final AssetResponse assetResponse = new AssetResponse();
        assetResponse.setAssets(assets);
        assetResponse.setResponseInfo(new ResponseInfo());

        when(assetService.getAssets(Matchers.any(AssetCriteria.class), Matchers.any(RequestInfo.class)))
                .thenReturn(assetResponse);

        mockMvc.perform(post("/assets/_search").param("grossValue", Double.valueOf("15").toString())
                .param("fromCapitalizedValue", Double.valueOf("15").toString())
                .param("toCapitalizedValue", Double.valueOf("20").toString()).param("tenantId", "ap.kurnool")
                .param("assetCategory", "1").contentType(MediaType.APPLICATION_JSON)
                .content(getFileContents("requestinfowrapper.json"))).andExpect(status().is4xxClientError());
    }

    @Test
    public void test_Should_Create_Asset() throws Exception {

        final List<Asset> assets = new ArrayList<>();
        final Asset asset = getAsset();
        assets.add(asset);
        final AssetResponse assetResponse = new AssetResponse();
        assetResponse.setAssets(assets);
        assetResponse.setResponseInfo(new ResponseInfo());

        when(assetService.createAsync(any(AssetRequest.class))).thenReturn(assetResponse);

        mockMvc.perform(post("/assets/_create").contentType(MediaType.APPLICATION_JSON)
                .content(getFileContents("assetcreaterequest.json"))).andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(getFileContents("assetcreateresponse.json")));
    }

    @Test
    public void test_Should_Update_Asset() throws Exception {

        final List<Asset> assets = new ArrayList<>();
        final Asset asset = getAsset();
        asset.setCode("13");
        assets.add(asset);
        final AssetResponse assetResponse = new AssetResponse();
        assetResponse.setAssets(assets);
        assetResponse.setResponseInfo(new ResponseInfo());

        when(assetService.updateAsync(any(AssetRequest.class))).thenReturn(assetResponse);

        mockMvc.perform(post("/assets/_update", "13").contentType(MediaType.APPLICATION_JSON)
                .content(getFileContents("assetupdaterequest.json"))).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(getFileContents("assetupdateresponse.json")));
    }

    @Test
    public void test_should_create_revaluate() throws IOException, Exception {
        final List<Revaluation> revaluations = new ArrayList<>();
        revaluations.add(getRevaluation());
        final RevaluationResponse revaluationResponse = new RevaluationResponse();
        revaluationResponse.setResposneInfo(null);
        revaluationResponse.setRevaluations(revaluations);
        when(revaluationService.createAsync(any(RevaluationRequest.class),any(HttpHeaders.class))).thenReturn(revaluationResponse);
        mockMvc.perform(post("/assets/revaluation" + "/_create").contentType(MediaType.APPLICATION_JSON)
                .content(getFileContents("revaluation/revaluationcreaterequest.json"))).andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(getFileContents("revaluation/revaluationcreateresponse.json")));
    }

    @Test
    public void test_should_search_revaluate() throws IOException, Exception {
        final List<Revaluation> revaluations = new ArrayList<>();
        revaluations.add(getRevaluation());
        final RevaluationResponse revaluationResponse = new RevaluationResponse();
        revaluationResponse.setResposneInfo(null);
        revaluationResponse.setRevaluations(revaluations);
        when(revaluationService.search(any(RevaluationCriteria.class))).thenReturn(revaluationResponse);
        mockMvc.perform(post("/assets/revaluation/_search").contentType(MediaType.APPLICATION_JSON)
                .param("tenantId", "ap.kurnool").content(getFileContents("requestinfowrapper.json")))
                .andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(getFileContents("revaluation/revaluatesearchresponse.json")));
    }

    @Test
    public void test_should_create_disposal() throws IOException, Exception {
        final List<Disposal> disposals = new ArrayList<>();
        disposals.add(getDisposals());
        final DisposalResponse disposalResponse = new DisposalResponse();
        disposalResponse.setResponseInfo(null);
        disposalResponse.setDisposals(disposals);
        when(disposalService.createAsync(any(DisposalRequest.class),any(HttpHeaders.class))).thenReturn(disposalResponse);
        mockMvc.perform(post("/assets/dispose/_create").contentType(MediaType.APPLICATION_JSON)
                .content(getFileContents("disposal/disposalcreaterequest.json"))).andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(getFileContents("disposal/disposalcreateresponse.json")));
    }

    @Test
    public void test_should_search_disposal() throws IOException, Exception {
        final List<Disposal> disposal = new ArrayList<>();
        disposal.add(getDisposals());
        final DisposalResponse disposalResponse = new DisposalResponse();
        disposalResponse.setResponseInfo(null);
        disposalResponse.setDisposals(disposal);
        when(disposalService.search(any(DisposalCriteria.class), any(RequestInfo.class))).thenReturn(disposalResponse);
        mockMvc.perform(post("/assets/dispose/_search").contentType(MediaType.APPLICATION_JSON)
                .param("tenantId", "ap.kurnool").content(getFileContents("requestinfowrapper.json")))
                .andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(getFileContents("disposal/disposesearchresponse.json")));
    }

    private String getFileContents(final String fileName) throws IOException {
        return new FileUtils().getFileContents(fileName);
    }

    private Asset getAsset() {
        final Asset asset = new Asset();
        asset.setTenantId("ap.kurnool");
        asset.setId(2L);
        asset.setName("asset name");
        asset.setCode("000013");
        asset.setDepartment(null);

        final AssetCategory assetCategory = new AssetCategory();
        assetCategory.setId(1l);
        assetCategory.setName("category name");
        asset.setAssetCategory(assetCategory);

        asset.setAssetDetails(null);
        asset.setModeOfAcquisition(ModeOfAcquisition.ACQUIRED);
        asset.setStatus("CREATED");
        asset.setDescription(null);
        asset.setDateOfCreation(null);

        final Location location = new Location();
        location.setLocality(4l);
        location.setDoorNo("door no");
        asset.setLocationDetails(location);

        asset.setRemarks(null);
        asset.setLength(null);
        asset.setWidth(null);
        asset.setTotalArea(null);
        asset.setGrossValue(null);
        asset.setAccumulatedDepreciation(null);
        asset.setAssetReference(null);
        asset.setVersion(null);
        asset.setAssetAttributes(null);

        return asset;
    }

    private Revaluation getRevaluation() {
        final Revaluation revaluation = new Revaluation();
        revaluation.setTenantId("ap.kurnool");
        revaluation.setId(Long.valueOf("15"));
        revaluation.setAssetId(Long.valueOf("31"));
        revaluation.setCurrentCapitalizedValue(new BigDecimal("100.68"));
        revaluation.setTypeOfChange(TypeOfChangeEnum.DECREASED);
        revaluation.setRevaluationAmount(new BigDecimal("10.0"));
        revaluation.setValueAfterRevaluation(new BigDecimal("90.68"));
        revaluation.setRevaluationDate(Long.valueOf("1496430744825"));
        revaluation.setReevaluatedBy("5");
        revaluation.setReasonForRevaluation("reasonForRevaluation");
        revaluation.setFixedAssetsWrittenOffAccount(Long.valueOf("1"));
        revaluation.setFunction(Long.valueOf("2"));
        revaluation.setFund(Long.valueOf("3"));
        revaluation.setScheme(Long.valueOf("4"));
        revaluation.setSubScheme(Long.valueOf("5"));
        revaluation.setComments("coments");
        revaluation.setStatus(Status.APPROVED.toString());

        final AuditDetails auditDetails = new AuditDetails();
        auditDetails.setCreatedBy(String.valueOf("5"));
        auditDetails.setCreatedDate(Long.valueOf("1495978422356"));
        auditDetails.setLastModifiedBy(String.valueOf("5"));
        auditDetails.setLastModifiedDate(Long.valueOf("1495978422356"));
        revaluation.setAuditDetails(auditDetails);

        return revaluation;
    }

    public Disposal getDisposals() {
        final Disposal disposal = new Disposal();
        disposal.setTenantId("ap.kurnool");
        disposal.setId(Long.valueOf("15"));
        disposal.setAssetId(Long.valueOf("31"));
        disposal.setBuyerName("Abhi");
        disposal.setBuyerAddress("Bangalore");
        disposal.setDisposalReason("disposalReason");
        disposal.setDisposalDate(Long.valueOf("1496564536178"));
        disposal.setPanCardNumber("baq1234567");
        disposal.setAadharCardNumber("12345678123456");
        disposal.setAssetCurrentValue(new BigDecimal("100.0"));
        disposal.setSaleValue(new BigDecimal("200.0"));
        disposal.setTransactionType(TransactionType.SALE);
        disposal.setAssetSaleAccount(Long.valueOf("6"));

        final AuditDetails auditDetails = new AuditDetails();
        auditDetails.setCreatedBy(String.valueOf("5"));
        auditDetails.setCreatedDate(Long.valueOf("1495978422356"));
        auditDetails.setLastModifiedBy(String.valueOf("5"));
        auditDetails.setLastModifiedDate(Long.valueOf("1495978422356"));
        disposal.setAuditDetails(auditDetails);
        return disposal;
    }
}
