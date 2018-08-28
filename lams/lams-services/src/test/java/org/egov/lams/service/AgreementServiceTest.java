package org.egov.lams.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.lams.model.Agreement;
import org.egov.lams.model.AgreementCriteria;
import org.egov.lams.model.Allottee;
import org.egov.lams.model.Asset;
import org.egov.lams.model.AssetCategory;
import org.egov.lams.model.Demand;
import org.egov.lams.model.Location;
import org.egov.lams.model.Renewal;
import org.egov.lams.model.WorkflowDetails;
import org.egov.lams.model.enums.Action;
import org.egov.lams.model.enums.Source;
import org.egov.lams.model.enums.Status;
import org.egov.lams.repository.AgreementRepository;
import org.egov.lams.repository.AllotteeRepository;
import org.egov.lams.repository.DemandRepository;
import org.egov.lams.repository.PositionRestRepository;
import org.egov.lams.repository.WorkFlowRepository;
import org.egov.lams.util.AcknowledgementNumberUtil;
import org.egov.lams.util.AgreementNumberUtil;
import org.egov.lams.util.NoticeNumberUtil;
import org.egov.lams.web.contract.AgreementRequest;
import org.egov.lams.web.contract.AllotteeResponse;
import org.egov.lams.web.contract.DemandResponse;
import org.egov.lams.web.contract.DepartmentDesignation;
import org.egov.lams.web.contract.Designation;
import org.egov.lams.web.contract.Position;
import org.egov.lams.web.contract.PositionResponse;
import org.egov.lams.web.contract.RequestInfo;
import org.egov.lams.web.contract.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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
    private DemandService demandService;

    @Mock
    private DemandRepository demandRepository;

    @Mock
    private AgreementNumberUtil agreementNumberService;

    @Mock
    private NoticeNumberUtil noticeNumberUtil;
    
    @Mock
    private WorkFlowRepository workFlowRepository;


    @Test
    public void test_to_check_if_agreement_exists() {
        when(agreementRepository.isAgreementExist("AAA")).thenReturn(true);

        agreementRepository.isAgreementExist("AAA");

        verify(agreementRepository).isAgreementExist("AAA");
    }

    @Test
    public void test_to_fetch_agreements_for_given_asset_id() {
        when(agreementRepository.getAgreementForCriteria(any())).thenReturn(getAgreementsList());

        final List<Agreement> agreements = agreementService.getAgreementsForAssetId(1l);

        assertEquals(1, agreements.size());
        assertEquals("454", agreements.get(0).getCouncilNumber());
    }

    @Test
    public void test_to_check_for_eviction_create() {
        final AgreementRequest agreementRequest = getAgreementRequest();
        when(allotteeRepository.getAllottees(agreementRequest.getAgreement().getAllottee(),
                agreementRequest.getRequestInfo())).thenReturn(getAllotteeResponse());
        when(positionRestRepository.getPositions(any(), any(), any()))
                .thenReturn(getPositionResponse());
        when(lamsConfigurationService.getLamsConfigurations(any()))
                .thenReturn(getDesignationsList());
        when(acknowledgementNumberService.generateAcknowledgeNumber())
                .thenReturn("AA453DD");

        final Agreement agreement = agreementService.createEviction(agreementRequest);

        assertEquals("454", agreement.getCouncilNumber());
        assertEquals("AA453DD", agreement.getAcknowledgementNumber());
    }

    @Test
    public void test_for_cancellation_of_agreement() {
        final AgreementRequest agreementRequest = getAgreementRequest();
        when(allotteeRepository.getAllottees(agreementRequest.getAgreement().getAllottee(),
                agreementRequest.getRequestInfo())).thenReturn(getAllotteeResponse());
        when(positionRestRepository.getPositions(any(), any(), any()))
                .thenReturn(getPositionResponse());
        when(lamsConfigurationService.getLamsConfigurations(any()))
                .thenReturn(getDesignationsList());
        when(acknowledgementNumberService.generateAcknowledgeNumber())
                .thenReturn("AA453DD");

        final Agreement agreement = agreementService.createCancellation(agreementRequest);

        assertEquals("454", agreement.getCouncilNumber());
        assertEquals("AA453DD", agreement.getAcknowledgementNumber());
    }

    @Test
    public void test_for_renewal_of_agreement() {
        final AgreementRequest agreementRequest = getAgreementRequest();
        agreementRequest.getAgreement().setRenewal(getRenewalDatails());
        when(allotteeRepository.getAllottees(agreementRequest.getAgreement().getAllottee(),
                agreementRequest.getRequestInfo())).thenReturn(getAllotteeResponse());
        when(positionRestRepository.getPositions(any(), any(), any()))
                .thenReturn(getPositionResponse());
        when(lamsConfigurationService.getLamsConfigurations(any()))
                .thenReturn(getDesignationsList());
        when(acknowledgementNumberService.generateAcknowledgeNumber())
                .thenReturn("AA453DD");
        when(demandService.prepareDemandsForClone(any(), any())).thenReturn(getDemands());
        when(demandRepository.createDemand(any(), any())).thenReturn(getDemandResponse());

        final Agreement agreement = agreementService.createRenewal(agreementRequest);

        assertEquals("454", agreement.getCouncilNumber());
        assertEquals("AA453DD", agreement.getAcknowledgementNumber());
    }

    @Test
    public void test_for_objection_of_agreement() {
        final AgreementRequest agreementRequest = getAgreementRequest();
        when(allotteeRepository.getAllottees(agreementRequest.getAgreement().getAllottee(),
                agreementRequest.getRequestInfo())).thenReturn(getAllotteeResponse());
        when(positionRestRepository.getPositions(any(), any(), any()))
                .thenReturn(getPositionResponse());
        when(lamsConfigurationService.getLamsConfigurations(any()))
                .thenReturn(getDesignationsList());
        when(acknowledgementNumberService.generateAcknowledgeNumber())
                .thenReturn("AA453DD");
        when(demandService.prepareDemandsForClone(any(), any())).thenReturn(getDemands());
        when(demandRepository.createDemand(any(), any())).thenReturn(getDemandResponse());

        final Agreement agreement = agreementService.createObjection(agreementRequest);

        assertEquals("454", agreement.getCouncilNumber());
        assertEquals("AA453DD", agreement.getAcknowledgementNumber());
    }

    @Test
    public void test_for_create_judgement() {
        final AgreementRequest agreementRequest = getAgreementRequest();
        when(allotteeRepository.getAllottees(agreementRequest.getAgreement().getAllottee(),
                agreementRequest.getRequestInfo())).thenReturn(getAllotteeResponse());
        when(positionRestRepository.getPositions(any(), any(), any()))
                .thenReturn(getPositionResponse());
        when(lamsConfigurationService.getLamsConfigurations(any()))
                .thenReturn(getDesignationsList());
        when(acknowledgementNumberService.generateAcknowledgeNumber())
                .thenReturn("AA453DD");
        when(demandService.prepareDemandsForClone(any(), any())).thenReturn(getDemands());
        when(demandRepository.createDemand(any(), any())).thenReturn(getDemandResponse());

        final Agreement agreement = agreementService.createJudgement(agreementRequest);

        assertEquals("454", agreement.getCouncilNumber());
        assertEquals("AA453DD", agreement.getAcknowledgementNumber());
    }

    @Test
    public void test_for_remission_of_agreement() {
        final AgreementRequest agreementRequest = getAgreementRequest();
        when(allotteeRepository.getAllottees(agreementRequest.getAgreement().getAllottee(),
                agreementRequest.getRequestInfo())).thenReturn(getAllotteeResponse());
        when(positionRestRepository.getPositions(any(), any(), any())).thenReturn(getPositionResponse());
        when(lamsConfigurationService.getLamsConfigurations(any())).thenReturn(getDesignationsList());
        when(acknowledgementNumberService.generateAcknowledgeNumber()).thenReturn("RM1234Q");
        when(demandService.prepareDemandsForClone(any(), any())).thenReturn(getDemands());
        when(demandRepository.createDemand(any(), any())).thenReturn(getDemandResponse());

        final Agreement agreement = agreementService.createRemission(agreementRequest);

        assertEquals("454", agreement.getCouncilNumber());
        assertEquals("RM1234Q", agreement.getAcknowledgementNumber());
    }

    @Test
    public void test_to_create_agreement() {
        final AgreementRequest agreementRequest = getAgreementRequest();
        agreementRequest.getAgreement().setAction(Action.CREATE);
        agreementRequest.getAgreement().setSource(Source.SYSTEM);

        when(allotteeRepository.getAllottees(agreementRequest.getAgreement().getAllottee(),
                agreementRequest.getRequestInfo())).thenReturn(getAllotteeResponse());
        when(positionRestRepository.getPositions(any(), any(), any()))
                .thenReturn(getPositionResponse());
        when(lamsConfigurationService.getLamsConfigurations(any()))
                .thenReturn(getDesignationsList());
        when(demandService.updateDemandOnRemission(any(), any())).thenReturn(getDemands());
        when(demandRepository.createDemand(any(), any())).thenReturn(getDemandResponse());
        when(acknowledgementNumberService.generateAcknowledgeNumber())
                .thenReturn("AA453DD");

        final Agreement agreement = agreementService.createAgreement(agreementRequest);

        assertEquals("454", agreement.getCouncilNumber());
        assertEquals("AA453DD", agreement.getAcknowledgementNumber());
    }

    @Test
    public void test_to_create_agreement_with_source_as_data_entry() {
        final AgreementRequest agreementRequest = getAgreementRequest();
        agreementRequest.getAgreement().setAction(Action.CREATE);
        agreementRequest.getAgreement().setSource(Source.DATA_ENTRY);

        when(demandService.updateDemandOnRemission(any(), any())).thenReturn(getDemands());
        when(demandRepository.createDemand(any(), any())).thenReturn(getDemandResponse());
        when(agreementNumberService.generateAgrementNumber(any(), any())).thenReturn("LFHY454DWQ");

        final Agreement agreement = agreementService.createAgreement(agreementRequest);

        assertEquals("454", agreement.getCouncilNumber());
        assertEquals("LFHY454DWQ", agreement.getAgreementNumber());
    }

    @Test
    public void test_for_update_cancellation_for_approve_status() {
        final AgreementRequest agreementRequest = getAgreementRequest();
        agreementRequest.getAgreement().getWorkflowDetails().setAction("Approve");

        final Agreement agreement = agreementService.updateCancellation(agreementRequest);

        assertEquals("454", agreement.getCouncilNumber());
        assertEquals(Status.INACTIVE, agreement.getStatus());
    }

    @Test
    public void test_for_update_cancellation_for_rejected_status() {
        final AgreementRequest agreementRequest = getAgreementRequest();
        agreementRequest.getAgreement().getWorkflowDetails().setAction("Reject");

        final Agreement agreement = agreementService.updateCancellation(agreementRequest);

        assertEquals("454", agreement.getCouncilNumber());
        assertEquals(Status.REJECTED, agreement.getStatus());
    }

    @Test
    public void test_for_update_cancellation_for_cancel_status() {
        final AgreementRequest agreementRequest = getAgreementRequest();
        agreementRequest.getAgreement().getWorkflowDetails().setAction("Cancel");

        final Agreement agreement = agreementService.updateCancellation(agreementRequest);

        assertEquals("454", agreement.getCouncilNumber());
        assertEquals(Status.CANCELLED, agreement.getStatus());
    }

    @Test
    public void test_for_update_eviction_for_approve_status() {
        final AgreementRequest agreementRequest = getAgreementRequest();
        agreementRequest.getAgreement().getWorkflowDetails().setAction("Approve");

        final Agreement agreement = agreementService.updateEviction(agreementRequest);

        assertEquals("454", agreement.getCouncilNumber());
        assertEquals(Status.EVICTED, agreement.getStatus());
    }

    @Test
    public void test_for_update_eviction_for_rejected_status() {
        final AgreementRequest agreementRequest = getAgreementRequest();
        agreementRequest.getAgreement().getWorkflowDetails().setAction("Reject");

        final Agreement agreement = agreementService.updateEviction(agreementRequest);

        assertEquals("454", agreement.getCouncilNumber());
        assertEquals(Status.REJECTED, agreement.getStatus());
    }

    @Test
    public void test_for_update_eviction_for_cancel_status() {
        final AgreementRequest agreementRequest = getAgreementRequest();
        agreementRequest.getAgreement().getWorkflowDetails().setAction("Cancel");

        final Agreement agreement = agreementService.updateEviction(agreementRequest);

        assertEquals("454", agreement.getCouncilNumber());
        assertEquals(Status.CANCELLED, agreement.getStatus());
    }

    @Test
	public void test_for_update_renewal_of_agreement() {
		final AgreementRequest agreementRequest = getAgreementRequest();
		agreementRequest.getAgreement().getWorkflowDetails().setAction("Approve");
		agreementRequest.getAgreement().setAction(Action.RENEWAL);
		agreementRequest.getAgreement().setRenewalDate(new Date());
		when(demandService.prepareDemands(any())).thenReturn(getDemands());
		when(demandRepository.createDemand(any(), any())).thenReturn(getDemandResponse());
		when(demandRepository.updateDemand(any(), any())).thenReturn(getDemandResponse());

		final Agreement agreement = agreementService.updateRenewal(agreementRequest);

		assertEquals("454", agreement.getCouncilNumber());
		assertTrue(Status.ACTIVE.equals(agreement.getStatus()) || Status.RENEWED.equals(agreement.getStatus()));

	}

    @Test
    public void test_for_update_renewal_of_agreement_with_status_cancel() {
        final AgreementRequest agreementRequest = getAgreementRequest();
        agreementRequest.getAgreement().getWorkflowDetails().setAction("Cancel");
        when(demandService.prepareDemands(any())).thenReturn(getDemands());
        when(demandRepository.createDemand(any(), any())).thenReturn(getDemandResponse());
        when(demandRepository.updateDemand(any(), any())).thenReturn(getDemandResponse());

        final Agreement agreement = agreementService.updateRenewal(agreementRequest);

        assertEquals("454", agreement.getCouncilNumber());
        assertEquals(Status.CANCELLED, agreement.getStatus());
    }

    @Test
    public void test_for_update_renewal_of_agreement_with_status_rejected() {
        final AgreementRequest agreementRequest = getAgreementRequest();
        agreementRequest.getAgreement().getWorkflowDetails().setAction("Reject");
        when(demandService.prepareDemands(any())).thenReturn(getDemands());
        when(demandRepository.createDemand(any(), any())).thenReturn(getDemandResponse());
        when(demandRepository.updateDemand(any(), any())).thenReturn(getDemandResponse());

        final Agreement agreement = agreementService.updateRenewal(agreementRequest);

        assertEquals("454", agreement.getCouncilNumber());
        assertEquals(Status.REJECTED, agreement.getStatus());
    }

    @Test
    public void test_for_update_objection_and_judgement_of_agreement_with_status_rejected() {
        final AgreementRequest agreementRequest = getAgreementRequest();
        agreementRequest.getAgreement().getWorkflowDetails().setAction("Reject");
        when(demandService.prepareDemands(any())).thenReturn(getDemands());
        when(demandRepository.createDemand(any(), any())).thenReturn(getDemandResponse());
        when(demandRepository.updateDemand(any(), any())).thenReturn(getDemandResponse());

        final Agreement agreement = agreementService.updateObjectionAndJudgement(agreementRequest);

        assertEquals("454", agreement.getCouncilNumber());
        assertEquals(Status.REJECTED, agreement.getStatus());
    }

    @Test
    public void test_for_update_objection_and_judgement_of_agreement_with_status_cancelled() {
        final AgreementRequest agreementRequest = getAgreementRequest();
        agreementRequest.getAgreement().getWorkflowDetails().setAction("Cancel");
        when(demandService.prepareDemands(any())).thenReturn(getDemands());
        when(demandRepository.createDemand(any(), any())).thenReturn(getDemandResponse());
        when(demandRepository.updateDemand(any(), any())).thenReturn(getDemandResponse());

        final Agreement agreement = agreementService.updateObjectionAndJudgement(agreementRequest);

        assertEquals("454", agreement.getCouncilNumber());
        assertEquals(Status.CANCELLED, agreement.getStatus());
    }

    @Test
    public void test_for_update_objection_and_judgement_of_agreement_with_status_approved() {
        final AgreementRequest agreementRequest = getAgreementRequest();
        agreementRequest.getAgreement().getWorkflowDetails().setAction("Approve");
        when(demandService.prepareDemands(any())).thenReturn(getDemands());
        when(demandRepository.createDemand(any(), any())).thenReturn(getDemandResponse());
        when(demandRepository.updateDemand(any(), any())).thenReturn(getDemandResponse());

        final Agreement agreement = agreementService.updateObjectionAndJudgement(agreementRequest);

        assertEquals("454", agreement.getCouncilNumber());
        assertEquals(Status.ACTIVE, agreement.getStatus());
    }

    @Test
    public void test_for_update_remission_of_agreement() {
        final AgreementRequest agreementRequest = getAgreementRequest();
        agreementRequest.getAgreement().getWorkflowDetails().setAction("Approve");
        when(demandService.updateDemandOnRemission(any(), any())).thenReturn(getDemands());
        when(demandRepository.updateDemand(any(), any())).thenReturn(getDemandResponse());

        final Agreement agreement = agreementService.updateRemission(agreementRequest);

        assertEquals("454", agreement.getCouncilNumber());
        assertEquals(Status.ACTIVE, agreement.getStatus());
    }

    @Test
    public void test_for_update_remission_of_agreement_with_status_cancel() {
        final AgreementRequest agreementRequest = getAgreementRequest();
        agreementRequest.getAgreement().getWorkflowDetails().setAction("Cancel");
        when(demandService.updateDemandOnRemission(any(), any())).thenReturn(getDemands());
        when(demandRepository.updateDemand(any(), any())).thenReturn(getDemandResponse());

        final Agreement agreement = agreementService.updateRemission(agreementRequest);

        assertEquals("454", agreement.getCouncilNumber());
        assertEquals(Status.CANCELLED, agreement.getStatus());
    }

    @Test
    public void test_for_update_remission_of_agreement_with_status_rejected() {
        final AgreementRequest agreementRequest = getAgreementRequest();
        agreementRequest.getAgreement().getWorkflowDetails().setAction("Reject");
        when(demandService.updateDemandOnRemission(any(), any())).thenReturn(getDemands());
        when(demandRepository.updateDemand(any(), any())).thenReturn(getDemandResponse());

        final Agreement agreement = agreementService.updateRemission(agreementRequest);

        assertEquals("454", agreement.getCouncilNumber());
        assertEquals(Status.REJECTED, agreement.getStatus());
    }

    @Test
    public void test_for_update_agreement_for_data_entry() {
        final AgreementRequest agreementRequest = getAgreementRequest();
        agreementRequest.getAgreement().setSource(Source.DATA_ENTRY);
        when(demandRepository.createDemand(any(), any())).thenReturn(getDemandResponse());
        when(demandRepository.updateDemand(any(), any())).thenReturn(getDemandResponse());

        final Agreement agreement = agreementService.updateAgreement(agreementRequest);

        assertEquals("454", agreement.getCouncilNumber());
    }

    @Test
    public void test_for_update_agreement_for_system_as_source() {
        final AgreementRequest agreementRequest = getAgreementRequest();
        agreementRequest.getAgreement().setSource(Source.SYSTEM);
        when(demandService.prepareDemands(any())).thenReturn(getDemands());
        when(demandRepository.createDemand(any(), any())).thenReturn(getDemandResponse());
        when(demandRepository.updateDemand(any(), any())).thenReturn(getDemandResponse());

        final Agreement agreement = agreementService.updateAgreement(agreementRequest);

        assertEquals("454", agreement.getCouncilNumber());
    }

    @Test
    public void test_for_search_by_agreement() {
        final AgreementCriteria agreementCriteria = getAgreementCriteria();
        when(agreementRepository.findByAgreement(any(), any()))
                .thenReturn(getAgreementsList());

        final List<Agreement> agreements = agreementService.searchAgreement(agreementCriteria, getRequestInfo());

        assertEquals("454", agreements.get(0).getCouncilNumber());
        assertEquals(2000.0, agreements.get(0).getRent(), 0.0);
    }

    private Map<String, List<String>> getDesignationsList() {
        final List<String> designationList = Arrays.asList("lams_workflow_initiator_designation");
        final Map<String, List<String>> designations = new HashMap<>();
        designations.put("lams_workflow_initiator_designation", designationList);

        return designations;
    }

    private DemandResponse getDemandResponse() {
        return DemandResponse.builder()
                .responseInfo(null)
                .demands(getDemands())
                .build();
    }

    private List<Agreement> getAgreementsList() {
        final Agreement agreement1 = Agreement.builder()
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

        final List<Agreement> agreementList = new ArrayList<>();
        agreementList.add(agreement1);

        return agreementList;
    }

    private WorkflowDetails getWorkflowDetails() {
        return WorkflowDetails.builder()
                .assignee(1l)
                .status("START")
                .build();
    }

    private AgreementRequest getAgreementRequest() {
        return AgreementRequest.builder()
                .requestInfo(getRequestInfo())
                .agreement(getAgreement())
                .build();
    }

    private AgreementCriteria getAgreementCriteria() {
        return AgreementCriteria.builder()
                .toDate(new Date())
                .build();
    }

    private RequestInfo getRequestInfo() {
        final User user = User.builder()
                .type("EMPLOYEE")
                .name("Raghu")
                .userName("Raghu007")
                .id(1l)
                .build();

        return RequestInfo.builder()
                .userInfo(user)
                .requesterId("1")
                .build();
    }

    private AllotteeResponse getAllotteeResponse() {
        return AllotteeResponse.builder()
                .allottee(Arrays.asList(getAllotteeDetails()))
                .responseInfo(null)
                .build();
    }

    private Agreement getAgreement() {
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
                .demands(getDemandList())
                .legacyDemands(getDemands())
                .expiryDate(new Date())
                .isHistory(false)
                .build();
    }

    private List<String> getDemandList() {
        return Arrays.asList("2");
    }

    private Allottee getAllottee() {
        return Allottee.builder()
                .userName("Raghu007")
                .build();
    }

    private Allottee getAllotteeDetails() {
        return Allottee.builder()
                .id(1l)
                .userName("Raghu007")
                .build();
    }

    private Asset getAsset() {
        return Asset.builder()
                .name("Fish Tank")
                .code("LLN")
                .category(getCategory())
                .id(1l)
                .locationDetails(getLocation())
                .build();
    }

    private AssetCategory getCategory() {
        return AssetCategory.builder()
                .code("234")
                .id(3l)
                .name("Governament Land")
                .build();
    }

    private Location getLocation() {
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

    private PositionResponse getPositionResponse() {
        return PositionResponse.builder()
                .position(Arrays.asList(getPosition()))
                .responseInfo(null)
                .build();
    }

    private Position getPosition() {
        return Position.builder()
                .active(true)
                .id(1l)
                .name("Engineer")
                .isPostOutsourced(false)
                .deptdesig(getDepartmentDesignation())
                .build();
    }

    private DepartmentDesignation getDepartmentDesignation() {
        return DepartmentDesignation.builder()
                .id(2l)
                .departmentId(13l)
                .designation(getDesignation())
                .build();
    }

    private Designation getDesignation() {
        return Designation.builder()
                .id(3l)
                .name("lams_workflow_initiator_designation")
                .active(true)
                .code("ELE")
                .tenantId("ap.kurnool")
                .description("Electrical Department")
                .build();
    }

    private List<Demand> getDemands() {
        final Demand demand = Demand.builder()
                .id("1")
                .collectionAmount(BigDecimal.valueOf(10000))
                .installment("1000")
                .minAmountPayable(1000d)
                .moduleName("LAMS")
                .demandDetails(Collections.emptyList())
                .build();

        return Arrays.asList(demand);
    }
    
	private Renewal getRenewalDatails() {
		return Renewal.builder().renewalFromDate(new Date()).renewalOrderDate(new Date()).renewalOrderNumber("RN123")
				.build();
	}
}