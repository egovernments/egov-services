package org.egov.lams.service;

import org.egov.lams.model.*;
import org.egov.lams.model.enums.Action;
import org.egov.lams.model.enums.Source;
import org.egov.lams.repository.*;
import org.egov.lams.util.AcknowledgementNumberUtil;
import org.egov.lams.util.AgreementNumberUtil;
import org.egov.lams.web.contract.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AgreementServiceTest {

    @InjectMocks
    private AgreementService agreementService;

    @Mock
    private LamsConfigurationService lamsConfigurationService;

    @Mock
    private AcknowledgementNumberUtil acknowledgementNumberService;

    @Mock
    private AgreementRepository agreementRepository;

    @Mock
    private AllotteeRepository allotteeRepository;

    @Mock
    private PositionRestRepository positionRestRepository;

    @Mock
    private AgreementMessageQueueRepository agreementMessageQueueRepository;

    @Mock
    private DemandService demandService;

    @Mock
    private DemandRepository demandRepository;

    @Mock
    private AgreementNumberUtil agreementNumberService;

    @Value("${app.timezone}")
    private String timeZone;

    @Before
    public void setUp(){
        ReflectionTestUtils.setField(agreementService, "timeZone", "UTC");
    }

    @Test
    public void test_to_check_if_agreement_exists(){
        when(agreementRepository.isAgreementExist("AAA")).thenReturn(true);

        agreementRepository.isAgreementExist("AAA");

        verify(agreementRepository).isAgreementExist("AAA");
    }

    @Test
    public void test_to_fetch_agreements_for_given_asset_id(){
        when(agreementRepository.getAgreementForCriteria(any())).thenReturn(getAgreementsList());

        List<Agreement> agreements =  agreementService.getAgreementsForAssetId(1l);

        assertEquals(1, agreements.size());
        assertEquals("454",agreements.get(0).getCouncilNumber());
    }

    @Test
    public void test_to_check_for_eviction_create(){
        AgreementRequest agreementRequest = getAgreementRequest();
        when(allotteeRepository.getAllottees(agreementRequest.getAgreement().getAllottee(),
                agreementRequest.getRequestInfo())).thenReturn(getAllotteeResponse());
        when(positionRestRepository.getPositions(any(),any(),any()))
                .thenReturn(getPositionResponse());
        when(lamsConfigurationService.getLamsConfigurations(any()))
                .thenReturn(getDesignationsList());
        when(acknowledgementNumberService.generateAcknowledgeNumber())
                .thenReturn("AA453DD");

        Agreement agreement = agreementService.createEviction(agreementRequest);

        assertEquals("454", agreement.getCouncilNumber());
        assertEquals("AA453DD", agreement.getAcknowledgementNumber());
    }

    @Test
    public void test_for_cancellation_of_agreement(){
        AgreementRequest agreementRequest = getAgreementRequest();
        when(allotteeRepository.getAllottees(agreementRequest.getAgreement().getAllottee(),
                agreementRequest.getRequestInfo())).thenReturn(getAllotteeResponse());
        when(positionRestRepository.getPositions(any(),any(),any()))
                .thenReturn(getPositionResponse());
        when(lamsConfigurationService.getLamsConfigurations(any()))
                .thenReturn(getDesignationsList());
        when(acknowledgementNumberService.generateAcknowledgeNumber())
                .thenReturn("AA453DD");

        Agreement agreement = agreementService.createCancellation(agreementRequest);

        assertEquals("454", agreement.getCouncilNumber());
        assertEquals("AA453DD", agreement.getAcknowledgementNumber());
    }

    @Test
    public void test_for_renewal_of_agreement(){
        AgreementRequest agreementRequest = getAgreementRequest();
        when(allotteeRepository.getAllottees(agreementRequest.getAgreement().getAllottee(),
                agreementRequest.getRequestInfo())).thenReturn(getAllotteeResponse());
        when(positionRestRepository.getPositions(any(),any(),any()))
                .thenReturn(getPositionResponse());
        when(lamsConfigurationService.getLamsConfigurations(any()))
                .thenReturn(getDesignationsList());
        when(acknowledgementNumberService.generateAcknowledgeNumber())
                .thenReturn("AA453DD");
        when(demandService.prepareDemandsForClone(any())).thenReturn(getDemands());
        when(demandRepository.createDemand(any(), any())).thenReturn(getDemandResponse());

        Agreement agreement = agreementService.createRenewal(agreementRequest);

        assertEquals("454", agreement.getCouncilNumber());
        assertEquals("AA453DD", agreement.getAcknowledgementNumber());
    }

    @Test
    public void test_for_objection_of_agreement(){
        AgreementRequest agreementRequest = getAgreementRequest();
        when(allotteeRepository.getAllottees(agreementRequest.getAgreement().getAllottee(),
                agreementRequest.getRequestInfo())).thenReturn(getAllotteeResponse());
        when(positionRestRepository.getPositions(any(),any(),any()))
                .thenReturn(getPositionResponse());
        when(lamsConfigurationService.getLamsConfigurations(any()))
                .thenReturn(getDesignationsList());
        when(acknowledgementNumberService.generateAcknowledgeNumber())
                .thenReturn("AA453DD");
        when(demandService.prepareDemandsForClone(any())).thenReturn(getDemands());
        when(demandRepository.createDemand(any(), any())).thenReturn(getDemandResponse());

        Agreement agreement = agreementService.createObjection(agreementRequest);

        assertEquals("454", agreement.getCouncilNumber());
        assertEquals("AA453DD", agreement.getAcknowledgementNumber());
    }

    @Test
    public void test_for_create_judgement(){
        AgreementRequest agreementRequest = getAgreementRequest();
        when(allotteeRepository.getAllottees(agreementRequest.getAgreement().getAllottee(),
                agreementRequest.getRequestInfo())).thenReturn(getAllotteeResponse());
        when(positionRestRepository.getPositions(any(),any(),any()))
                .thenReturn(getPositionResponse());
        when(lamsConfigurationService.getLamsConfigurations(any()))
                .thenReturn(getDesignationsList());
        when(acknowledgementNumberService.generateAcknowledgeNumber())
                .thenReturn("AA453DD");
        when(demandService.prepareDemandsForClone(any())).thenReturn(getDemands());
        when(demandRepository.createDemand(any(), any())).thenReturn(getDemandResponse());

        Agreement agreement = agreementService.createJudgement(agreementRequest);

        assertEquals("454", agreement.getCouncilNumber());
        assertEquals("AA453DD", agreement.getAcknowledgementNumber());
    }

    @Test
    public void test_to_check_remission_save(){
        AgreementRequest agreementRequest = getAgreementRequest();
        when(agreementRepository.getAgreementID()).thenReturn(2l);
        when(demandService.updateDemandOnRemission(any(), any())).thenReturn(getDemands());
        when(demandRepository.createDemand(any(), any())).thenReturn(getDemandResponse());

        Agreement agreement = agreementService.saveRemission(agreementRequest);

        assertEquals("454", agreement.getCouncilNumber());
    }

    @Test
    public void test_to_create_agreement(){
        AgreementRequest agreementRequest = getAgreementRequest();
        agreementRequest.getAgreement().setAction(Action.CREATE);
        agreementRequest.getAgreement().setSource(Source.SYSTEM);

        when(allotteeRepository.getAllottees(agreementRequest.getAgreement().getAllottee(),
                agreementRequest.getRequestInfo())).thenReturn(getAllotteeResponse());
        when(positionRestRepository.getPositions(any(),any(),any()))
                .thenReturn(getPositionResponse());
        when(lamsConfigurationService.getLamsConfigurations(any()))
                .thenReturn(getDesignationsList());
        when(demandService.updateDemandOnRemission(any(), any())).thenReturn(getDemands());
        when(demandRepository.createDemand(any(), any())).thenReturn(getDemandResponse());
        when(acknowledgementNumberService.generateAcknowledgeNumber())
                .thenReturn("AA453DD");

        Agreement agreement = agreementService.createAgreement(agreementRequest);

        assertEquals("454", agreement.getCouncilNumber());
        assertEquals("AA453DD", agreement.getAcknowledgementNumber());
    }

    @Test
    public void test_to_create_agreement_with_source_as_data_entry(){
        AgreementRequest agreementRequest = getAgreementRequest();
        agreementRequest.getAgreement().setAction(Action.CREATE);
        agreementRequest.getAgreement().setSource(Source.DATA_ENTRY);

        when(demandService.updateDemandOnRemission(any(), any())).thenReturn(getDemands());
        when(demandRepository.createDemand(any(), any())).thenReturn(getDemandResponse());
        when(agreementNumberService.generateAgrementNumber(any())).thenReturn("LFHY454DWQ");

        Agreement agreement = agreementService.createAgreement(agreementRequest);

        assertEquals("454", agreement.getCouncilNumber());
        assertEquals("LFHY454DWQ", agreement.getAgreementNumber());
    }

    private Map<String, List<String>> getDesignationsList(){
        List<String> designationList = Arrays.asList("lams_workflow_initiator_designation");
        Map<String, List<String>> designations = new HashMap<>();
        designations.put("lams_workflow_initiator_designation", designationList);

        return designations;
    }

    private DemandResponse getDemandResponse(){
        return DemandResponse.builder()
                .responseInfo(null)
                .demands(getDemands())
                .build();
    }

    private List<Agreement> getAgreementsList(){
        Agreement agreement1 = Agreement.builder()
                .agreementDate(new Date())
                .id(1l)
                .asset(getAsset())
                .allottee(getAllottee())
                .bankGuaranteeAmount(210112.22)
                .councilNumber("454")
                .rent(2000d)
                .workflowDetails(getWorkflowDetails())
                .securityDeposit(2000d)
                .commencementDate(new Date())
                .build();

        List<Agreement> agreementList = new ArrayList<>();
        agreementList.add(agreement1);

        return agreementList;
    }

    private WorkflowDetails getWorkflowDetails(){
       return  WorkflowDetails.builder()
                .assignee(1l)
                .status("START")
                .build();
    }

    private AgreementRequest getAgreementRequest(){
        return AgreementRequest.builder()
                .requestInfo(getRequestInfo())
                .agreement(getAgreement())
                .build();
    }

    private RequestInfo getRequestInfo(){
        User user = User.builder()
                .type("EMPLOYEE")
                .name("Raghu")
                .userName("Raghu007")
                .id(1l)
                .build();

        return RequestInfo.builder()
                .userInfo(user)
                .build();
    }

    private AllotteeResponse getAllotteeResponse(){
        return AllotteeResponse.builder()
                .allottee(Arrays.asList(getAllotteeDetails()))
                .responseInfo(null)
                .build();
    }

    private Agreement getAgreement(){
        return Agreement.builder()
                .agreementDate(new Date())
                .id(1l)
                .asset(getAsset())
                .allottee(getAllottee())
                .bankGuaranteeAmount(210112.22)
                .councilNumber("454")
                .workflowDetails(getWorkflowDetails())
                .rent(2000d)
                .securityDeposit(2000d)
                .commencementDate(new Date())
                .timePeriod(3l)
                .legacyDemands(getDemands())
                .build();
    }

    private Allottee getAllottee(){
        return Allottee.builder()
                .userName("Raghu007")
                .build();
    }

    private Allottee getAllotteeDetails(){
        return Allottee.builder()
                .id(1l)
                .userName("Raghu007")
                .build();
    }

    private Asset getAsset(){
        return Asset.builder()
                .name("Fish Tank")
                .code("LLN")
                .category(getCategory())
                .id(1l)
                .locationDetails(getLocation())
                .build();
    }

    private AssetCategory getCategory(){
        return AssetCategory.builder()
                .code("234")
                .id(3l)
                .name("Governament Land")
                .build();
    }

    private Location getLocation(){
        return Location.builder()
                .block(2l)
                .doorNo("5454")
                .electionWard(3l)
                .street(5l)
                .zone(6l)
                .pinCode(542121542l)
                .revenueWard(4l)
                .build();
    }

    private PositionResponse getPositionResponse(){
        return PositionResponse.builder()
                .position(Arrays.asList(getPosition()))
                .responseInfo(null)
                .build();
    }

    private Position getPosition(){
        return Position.builder()
                .active(true)
                .id(1l)
                .name("Engineer")
                .isPostOutsourced(false)
                .deptdesig(getDepartmentDesignation())
                .build();
    }

    private DepartmentDesignation getDepartmentDesignation(){
        return DepartmentDesignation.builder()
                .id(2l)
                .departmentId(13l)
                .designation(getDesignation())
                .build();
    }

    private Designation getDesignation(){
        return Designation.builder()
                .id(3l)
                .name("lams_workflow_initiator_designation")
                .active(true)
                .code("ELE")
                .tenantId("ap.kurnool")
                .description("Electrical Department")
                .build();
    }

    private List<Demand> getDemands(){
        Demand demand = Demand.builder()
                .id("1")
                .collectionAmount(BigDecimal.valueOf(10000))
                .installment("1000")
                .minAmountPayable(1000d)
                .moduleName("LAMS")
                .build();

        return Arrays.asList(demand);
    }
}