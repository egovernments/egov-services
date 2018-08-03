/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.lams.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.AgreementCriteria;
import org.egov.lams.model.Allottee;
import org.egov.lams.model.Demand;
import org.egov.lams.model.DemandDetails;
import org.egov.lams.model.WorkflowDetails;
import org.egov.lams.model.enums.Action;
import org.egov.lams.model.enums.PaymentCycle;
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
import org.egov.lams.web.contract.DemandSearchCriteria;
import org.egov.lams.web.contract.LamsConfigurationGetRequest;
import org.egov.lams.web.contract.Position;
import org.egov.lams.web.contract.PositionResponse;
import org.egov.lams.web.contract.ProcessInstance;
import org.egov.lams.web.contract.RequestInfo;
import org.egov.lams.web.contract.RequestInfoWrapper;
import org.egov.lams.web.contract.TaskResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AgreementService {

    public static final String WF_ACTION_APPROVE = "Approve";
    public static final String WF_ACTION_REJECT = "Reject";
    public static final String WF_ACTION_CANCEL = "Cancel";
    public static final String WF_ACTION_FORWARD = "Forward";
    public static final String WF_ACTION_PRINT_NOTICE = "Print Notice";
    public static final String LAMS_WORKFLOW_INITIATOR_DESIGNATION = "lams_workflow_initiator_designation";
    private static final List<String> AUCTION_CATEGORIES = Arrays.asList("Market", "Fish Tanks", "Slaughter House",
            "Community Toilet Complex", "Community Hall");

    @Autowired
    private AgreementRepository agreementRepository;
    @Autowired
    private LamsConfigurationService lamsConfigurationService;
    @Autowired
    private DemandRepository demandRepository;
    @Autowired
    private AcknowledgementNumberUtil acknowledgementNumberService;
    @Autowired
    private AgreementNumberUtil agreementNumberService;
    @Autowired
    private AllotteeRepository allotteeRepository;
    @Autowired
    private PositionRestRepository positionRestRepository;
    @Autowired
    private DemandService demandService;
    @Autowired
    private NoticeNumberUtil noticeNumberUtil;

    @Autowired
    private WorkFlowRepository workFlowRepository;

    /**
     * service call to single agreement based on acknowledgementNumber
     *
     * @param code
     * @return
     */
    public boolean isAgreementExist(final String code) {
        return agreementRepository.isAgreementExist(code);
    }

    public List<Agreement> getAgreementsForAssetId(final Long assetId) {

        final AgreementCriteria agreementCriteria = new AgreementCriteria();
        final Set<Long> assets = new HashSet<>();
        assets.add(assetId);
        agreementCriteria.setAsset(assets);
        return agreementRepository.getAgreementForCriteria(agreementCriteria);
    }

    public List<Agreement> getAgreementsForAssetIdAndFloor(final Agreement agreement, final Long assetId) {

        final AgreementCriteria agreementCriteria = new AgreementCriteria();
        final Set<Long> assets = new HashSet<>();
        assets.add(assetId);
        agreementCriteria.setAsset(assets);
        if (StringUtils.isNotBlank(agreement.getReferenceNumber()))
            agreementCriteria.setReferenceNumber(agreement.getReferenceNumber());
        if (StringUtils.isNotBlank(agreement.getFloorNumber()))
            agreementCriteria.setFloorNumber(agreement.getFloorNumber());
        return agreementRepository.getAgreementForCriteria(agreementCriteria);
    }

    public List<Agreement> getAllHistoryAgreementsForAsset(final Agreement agreement) {

        return agreementRepository.getHistoryAgreementsByAsset(agreement);

    }

    /**
     * This method is used to create new agreement
     *
     * @return Agreement, return the agreement details with current status
     *
     * @param agreement, hold agreement details
     *
     */
    public Agreement createAgreement(final AgreementRequest agreementRequest) {

        final Agreement agreement = agreementRequest.getAgreement();
        log.info("createAgreement service::" + agreement);
        setAuditDetails(agreement, agreementRequest.getRequestInfo());

        if (agreement.getIsHistory()) {
            agreement.setStatus(Status.HISTORY);
            agreement.setExpiryDate(getExpiryDate(agreement));
            agreement.setId(agreementRepository.getAgreementID());
            agreement.setAgreementNumber(
                    agreementNumberService.generateAgrementNumber(agreement.getCommencementDate(), agreement.getTenantId()));
            agreementRepository.saveAgreement(agreementRequest);
        } else if (agreement.getAction().equals(Action.CREATE)) {

            agreement.setExpiryDate(getExpiryDate(agreement));
            agreement.setAdjustmentStartDate(getAdjustmentDate(agreement));
            setDetailsByAssetCategory(agreement);
            log.info("The closeDate calculated is " + agreement.getExpiryDate() + "from commencementDate of "
                    + agreement.getCommencementDate() + "by adding with no of years " + agreement.getTimePeriod());
            agreement.setId(agreementRepository.getAgreementID());
            if (agreement.getSource().equals(Source.DATA_ENTRY)) {
                agreement.setStatus(Status.ACTIVE);
                agreement.setIsUnderWorkflow(Boolean.FALSE);
                final List<Demand> demands = demandService.prepareDemands(agreementRequest);

                final DemandResponse demandResponse = demandRepository.createDemand(demands,
                        agreementRequest.getRequestInfo());
                final List<String> demandList = demandResponse.getDemands().stream().map(Demand::getId)
                        .collect(Collectors.toList());
                agreement.setDemands(demandList);
                agreement.setAgreementNumber(agreementNumberService
                        .generateAgrementNumber(agreement.getCommencementDate(), agreement.getTenantId()));
                agreement.setAgreementDate(agreement.getCommencementDate());
                agreement.setNoticeNumber(noticeNumberUtil.getNoticeNumber(agreement.getAction(),
                        agreement.getTenantId(), agreementRequest.getRequestInfo()));
                agreementRepository.saveAgreement(agreementRequest);
            } else {
                agreement.setStatus(Status.WORKFLOW);
                agreement.setIsUnderWorkflow(Boolean.TRUE);
                setInitiatorPosition(agreementRequest);

                final List<Demand> demands = demandService.prepareDemands(agreementRequest);

                final DemandResponse demandResponse = demandRepository.createDemand(demands,
                        agreementRequest.getRequestInfo());
                final List<String> demandIdList = demandResponse.getDemands().stream().map(Demand::getId)
                        .collect(Collectors.toList());
                agreement.setAcknowledgementNumber(acknowledgementNumberService.generateAcknowledgeNumber());
                if (agreement.getCgst() != null)
                    agreement.setCgst(Double.valueOf(Math.round(agreement.getCgst())));
                if (agreement.getSgst() != null)
                    agreement.setSgst(Double.valueOf(Math.round(agreement.getSgst())));
                log.info(agreement.getAcknowledgementNumber());
                agreement.setDemands(demandIdList);
                if (agreementRequest != null) {
                    final ProcessInstance processInstanceResponse = workFlowRepository.startWorkflow(agreementRequest);
                    log.info("the processInstanceresponse from workflow statrt : " + processInstanceResponse);
                }

            }

        }
        return agreement;
    }

    public Agreement modifyAgreement(final AgreementRequest agreementRequest) {

        final Agreement agreement = agreementRequest.getAgreement();
        updateAuditDetails(agreement, agreementRequest.getRequestInfo());
        agreement.setExpiryDate(getExpiryDate(agreement));
        agreement.setAdjustmentStartDate(getAdjustmentDate(agreement));
        final Allottee allottee = agreement.getAllottee();
        final Agreement oldAgreement = agreementRepository.findByAgreementId(agreement.getId()).get(0);
        if (isDemandChangeRequired(agreement, oldAgreement)) {
            agreement.setDemands(null);
            final List<Demand> demands = demandService.prepareDemands(agreementRequest);

            final DemandResponse demandResponse = demandRepository.createDemand(demands, agreementRequest.getRequestInfo());
            final List<String> demandList = demandResponse.getDemands().stream().map(Demand::getId)
                    .collect(Collectors.toList());
            agreement.setDemands(demandList);
        }
        if (allottee != null && allottee.getId() != null) {
            allottee.setTenantId(agreement.getTenantId());
            allotteeRepository.updateAllottee(allottee, agreementRequest.getRequestInfo());
        }
        agreement.setStatus(Status.ACTIVE);
        agreement.setIsUnderWorkflow(Boolean.FALSE);
        agreement.setAgreementDate(agreement.getCommencementDate());
        if (agreement.getCgst() != null)
            agreement.setCgst(Double.valueOf(Math.round(agreement.getCgst())));
        if (agreement.getSgst() != null)
            agreement.setSgst(Double.valueOf(Math.round(agreement.getSgst())));
        agreementRepository.modifyAgreement(agreementRequest);
        return agreement;
    }

    private Boolean isDemandChangeRequired(final Agreement newAgreement, final Agreement oldAgreement) {

        if (newAgreement.getCommencementDate().compareTo(oldAgreement.getCommencementDate()) != 0
                || !newAgreement.getPaymentCycle().equals(oldAgreement.getPaymentCycle()))
            return Boolean.TRUE;
        else
            return Boolean.FALSE;

    }

    public Agreement createEviction(final AgreementRequest agreementRequest) {
        log.info("create Eviction of agreement::" + agreementRequest.getAgreement());
        final Agreement agreement = enrichAgreement(agreementRequest);
        final ProcessInstance processInstanceResponse = workFlowRepository.startWorkflow(agreementRequest);
        log.info("the processInstanceresponse from workflow statrt : " + processInstanceResponse);

        return agreement;
    }

    public Agreement createCancellation(final AgreementRequest agreementRequest) {
        log.info("create Cancellation of agreement::" + agreementRequest.getAgreement());
        final Agreement agreement = enrichAgreement(agreementRequest);
        final ProcessInstance processInstanceResponse = workFlowRepository.startWorkflow(agreementRequest);
        log.info("the processInstanceresponse from workflow statrt : " + processInstanceResponse);
        return agreement;
    }

    public Agreement createRenewal(final AgreementRequest agreementRequest) {
        log.info("create Renewal of agreement::" + agreementRequest.getAgreement());
        final Agreement agreement = enrichAgreement(agreementRequest);
        // Renewal date = existing expiry date + 1
        final Date renewalStartDate = DateUtils.addDays(agreement.getExpiryDate(), 1);
        agreement.setAdjustmentStartDate(getAdjustmentDate(agreement));
        agreement.setRenewalDate(renewalStartDate);
        final List<Demand> demands = demandService.prepareDemandsForRenewal(agreementRequest, false);
        final DemandResponse demandResponse = demandRepository.createDemand(demands, agreementRequest.getRequestInfo());
        final List<String> demandIdList = demandResponse.getDemands().stream().map(Demand::getId)
                .collect(Collectors.toList());
        agreement.setDemands(demandIdList);
        agreement.setExpiryDate(getExpiryDate(agreement));

        final ProcessInstance processInstanceResponse = workFlowRepository.startWorkflow(agreementRequest);
        log.info("the processInstanceresponse from workflow statrt : " + processInstanceResponse);
        return agreement;
    }

    public Agreement createObjection(final AgreementRequest agreementRequest) {
        log.info("createObjection on agreement::" + agreementRequest.getAgreement());
        final Agreement agreement = enrichAgreement(agreementRequest);
        final List<Demand> demands = demandService.prepareDemandsForClone(agreement.getDemands().get(0),
                agreementRequest.getRequestInfo());
        final DemandResponse demandResponse = demandRepository.createDemand(demands, agreementRequest.getRequestInfo());
        final List<String> demandIdList = demandResponse.getDemands().stream().map(Demand::getId)
                .collect(Collectors.toList());
        agreement.setDemands(demandIdList);

        final ProcessInstance processInstanceResponse = workFlowRepository.startWorkflow(agreementRequest);
        log.info("the processInstanceresponse from workflow statrt : " + processInstanceResponse);
        return agreement;
    }

    public Agreement createJudgement(final AgreementRequest agreementRequest) {
        log.info("create judgement on agreement::" + agreementRequest.getAgreement());
        final Agreement agreement = enrichAgreement(agreementRequest);
        final List<Demand> demands = demandService.prepareDemandsForClone(agreement.getDemands().get(0),
                agreementRequest.getRequestInfo());
        final DemandResponse demandResponse = demandRepository.createDemand(demands, agreementRequest.getRequestInfo());
        final List<String> demandIdList = demandResponse.getDemands().stream().map(Demand::getId)
                .collect(Collectors.toList());
        agreement.setDemands(demandIdList);

        final ProcessInstance processInstanceResponse = workFlowRepository.startWorkflow(agreementRequest);
        log.info("the processInstanceresponse from workflow statrt : " + processInstanceResponse);
        return agreement;
    }

    public Agreement createRemission(final AgreementRequest agreementRequest) {
        log.info("create remission of agreement::" + agreementRequest.getAgreement());
        final Agreement agreement = enrichAgreement(agreementRequest);
        final List<Demand> demands = demandService.prepareDemandsForClone(agreement.getDemands().get(0),
                agreementRequest.getRequestInfo());
        final DemandResponse demandResponse = demandRepository.createDemand(demands, agreementRequest.getRequestInfo());
        final List<String> demandIdList = demandResponse.getDemands().stream().map(Demand::getId)
                .collect(Collectors.toList());
        agreement.setDemands(demandIdList);

        final ProcessInstance processInstanceResponse = workFlowRepository.startWorkflow(agreementRequest);
        log.info("the processInstanceresponse from workflow statrt : " + processInstanceResponse);
        return agreement;
    }

    private Agreement enrichAgreement(final AgreementRequest agreementRequest) {
        final Agreement agreement = agreementRequest.getAgreement();
        setAuditDetails(agreement, agreementRequest.getRequestInfo());
        agreement.setParent(agreement.getParent() != null ? agreement.getParent() : agreement.getId());
        agreement.setStatus(Status.WORKFLOW);
        agreement.setIsUnderWorkflow(Boolean.TRUE);
        setInitiatorPosition(agreementRequest);
        agreement.setAcknowledgementNumber(acknowledgementNumberService.generateAcknowledgeNumber());
        agreement.setId(agreementRepository.getAgreementID());

        return agreement;
    }

    /***
     * method to update agreementNumber using acknowledgeNumber
     *
     * @param agreement
     * @return
     */
    public Agreement updateAgreement(final AgreementRequest agreementRequest) {

        final Agreement agreement = agreementRequest.getAgreement();
        log.info("update create agreement ::" + agreement);
        final WorkflowDetails workFlowDetails = agreement.getWorkflowDetails();
        updateAuditDetails(agreement, agreementRequest.getRequestInfo());

        if (Action.RENEWAL.equals(agreement.getAction())
                && (Status.WORKFLOW.equals(agreement.getStatus()) || Status.REJECTED.equals(agreement.getStatus()))) {
            final TaskResponse taskResponse = workFlowRepository.updateWorkflow(agreementRequest);
            log.info("the taskResponse from workflow update : " + taskResponse);
        } else if (agreement.getSource().equals(Source.DATA_ENTRY)) {
            final Demand demand = agreement.getLegacyDemands().get(0);
            final List<DemandDetails> demandDetailList = new ArrayList<>();
            for (final DemandDetails demandDetail : demand.getDemandDetails())
                if (demandDetail.getTaxAmount().compareTo(BigDecimal.ZERO) >= 0)
                    demandDetailList.add(demandDetail);
            demand.getDemandDetails().clear();
            demand.setDemandDetails(demandDetailList);
            agreement.setDemands(updateDemand(agreement.getDemands(), agreement.getLegacyDemands(),
                    agreementRequest.getRequestInfo()));
            agreementRepository.updateAgreement(agreementRequest);

        } else if (agreement.getSource().equals(Source.SYSTEM)) {
            if (workFlowDetails != null)
                if (WF_ACTION_APPROVE.equalsIgnoreCase(workFlowDetails.getAction())) {
                    agreement.setStatus(Status.ACTIVE);
                    agreement.setAdjustmentStartDate(getAdjustmentDate(agreement));
                    agreement.setAgreementDate(new Date());
                    if (agreement.getAgreementNumber() == null)
                        agreement.setAgreementNumber(
                                agreementNumberService.generateAgrementNumber(agreement.getCommencementDate(),
                                        agreement.getTenantId()));
                    agreement.setNoticeNumber(noticeNumberUtil.getNoticeNumber(agreement.getAction(), agreement.getTenantId(),
                            agreementRequest.getRequestInfo()));
                    final List<Demand> demands = demandService.prepareDemands(agreementRequest);
                    updateDemand(agreement.getDemands(), demands,
                            agreementRequest.getRequestInfo());
                } else if (WF_ACTION_FORWARD.equalsIgnoreCase(workFlowDetails.getAction()))
                    agreement.setStatus(Status.WORKFLOW);
                else if (WF_ACTION_REJECT.equalsIgnoreCase(workFlowDetails.getAction()))
                    agreement.setStatus(Status.REJECTED);
                else if (WF_ACTION_CANCEL.equalsIgnoreCase(workFlowDetails.getAction())) {
                    agreement.setStatus(Status.CANCELLED);
                    agreement.setIsUnderWorkflow(Boolean.FALSE);
                } else if (WF_ACTION_PRINT_NOTICE.equalsIgnoreCase(workFlowDetails.getAction()))
                    agreement.setIsUnderWorkflow(Boolean.FALSE);
            final TaskResponse taskResponse = workFlowRepository.updateWorkflow(agreementRequest);
            log.info("the taskResponse from workflow update : " + taskResponse);
        }

        return agreement;
    }

    public Agreement updateCancellation(final AgreementRequest agreementRequest) {
        final Agreement agreement = agreementRequest.getAgreement();
        log.info("update cancellation agreement ::" + agreement);
        enrichAgreementWithWorkflowDetails(agreement, agreementRequest.getRequestInfo(), Status.INACTIVE);
        final TaskResponse taskResponse = workFlowRepository.updateWorkflow(agreementRequest);
        log.info("the taskResponse from workflow update : " + taskResponse);
        return agreement;
    }

    public Agreement updateEviction(final AgreementRequest agreementRequest) {
        final Agreement agreement = agreementRequest.getAgreement();
        log.info("update eviction agreement ::" + agreement);
        enrichAgreementWithWorkflowDetails(agreement, agreementRequest.getRequestInfo(), Status.EVICTED);
        final TaskResponse taskResponse = workFlowRepository.updateWorkflow(agreementRequest);
        log.info("the taskResponse from workflow update : " + taskResponse);
        return agreement;
    }

    private void enrichAgreementWithWorkflowDetails(final Agreement agreement, final RequestInfo requestInfo,
            final Status status) {
        final WorkflowDetails workFlowDetails = agreement.getWorkflowDetails();
        updateAuditDetails(agreement, requestInfo);
        final String userId = requestInfo.getUserInfo().getId().toString();
        if (workFlowDetails != null)
            if (WF_ACTION_APPROVE.equalsIgnoreCase(workFlowDetails.getAction())) {
                agreementRepository.updateExistingAgreementAsHistory(agreement, userId);
                agreement.setStatus(status);
                agreement.setAgreementDate(new Date());

                agreement.setNoticeNumber(
                        noticeNumberUtil.getNoticeNumber(agreement.getAction(), agreement.getTenantId(), requestInfo));
            } else if (WF_ACTION_REJECT.equalsIgnoreCase(workFlowDetails.getAction()))
                agreement.setStatus(Status.REJECTED);
            else if (WF_ACTION_CANCEL.equalsIgnoreCase(workFlowDetails.getAction())) {
                agreement.setStatus(Status.CANCELLED);
                agreement.setIsUnderWorkflow(Boolean.FALSE);
            } else if (WF_ACTION_PRINT_NOTICE.equalsIgnoreCase(workFlowDetails.getAction()))
                agreement.setIsUnderWorkflow(Boolean.FALSE);
    }

    public Agreement updateObjectionAndJudgement(final AgreementRequest agreementRequest) {
        final Agreement agreement = agreementRequest.getAgreement();
        final RequestInfo requestInfo = agreementRequest.getRequestInfo();
        final String userId = requestInfo.getUserInfo().getId().toString();
        log.info("update objection/judgement agreement ::" + agreement);
        final WorkflowDetails workFlowDetails = agreement.getWorkflowDetails();
        updateAuditDetails(agreement, requestInfo);

        if (WF_ACTION_APPROVE.equalsIgnoreCase(workFlowDetails.getAction())) {
            agreementRepository.updateExistingAgreementAsHistory(agreement, userId);
            agreement.setStatus(Status.ACTIVE);
            final List<Demand> demands = demandService.prepareDemandsByApprove(agreementRequest);
            updateDemand(agreement.getDemands(), demands,
                    agreementRequest.getRequestInfo());
            agreement.setNoticeNumber(
                    noticeNumberUtil.getNoticeNumber(agreement.getAction(), agreement.getTenantId(), requestInfo));

        } else if (WF_ACTION_REJECT.equalsIgnoreCase(workFlowDetails.getAction()))
            agreement.setStatus(Status.REJECTED);
        else if (WF_ACTION_CANCEL.equalsIgnoreCase(workFlowDetails.getAction())) {
            agreement.setStatus(Status.CANCELLED);
            agreement.setIsUnderWorkflow(Boolean.FALSE);
        } else if (WF_ACTION_PRINT_NOTICE.equalsIgnoreCase(workFlowDetails.getAction()))
            agreement.setIsUnderWorkflow(Boolean.FALSE);
        final TaskResponse taskResponse = workFlowRepository.updateWorkflow(agreementRequest);
        log.info("the taskResponse from workflow update : " + taskResponse);
        return agreement;
    }

    public Agreement updateRenewal(final AgreementRequest agreementRequest) {
        final Agreement agreement = agreementRequest.getAgreement();
        log.info("update renewal agreement ::" + agreement);
        final RequestInfo requestInfo = agreementRequest.getRequestInfo();
        final String userId = requestInfo.getUserInfo().getId().toString();
        final WorkflowDetails workFlowDetails = agreement.getWorkflowDetails();
        updateAuditDetails(agreement, requestInfo);
        if (workFlowDetails != null)
            if (WF_ACTION_APPROVE.equalsIgnoreCase(workFlowDetails.getAction())) {
                agreementRepository.updateExistingAgreementAsHistory(agreement, userId);
                agreement.setAgreementNumber(
                        agreementNumberService.generateAgrementNumber(agreement.getCommencementDate(), agreement.getTenantId()));
                agreement.setStatus(Status.ACTIVE);
                agreement.setAgreementDate(new Date());
                agreement.setAdjustmentStartDate(getAdjustmentDate(agreement));
                final List<Demand> demands = prepareDemandsForRenewalApproval(agreementRequest);
                updateDemand(agreement.getDemands(), demands,
                        agreementRequest.getRequestInfo());
                agreement.setNoticeNumber(
                        noticeNumberUtil.getNoticeNumber(agreement.getAction(), agreement.getTenantId(), requestInfo));

            } else if (WF_ACTION_REJECT.equalsIgnoreCase(workFlowDetails.getAction()))
                agreement.setStatus(Status.REJECTED);
            else if (WF_ACTION_CANCEL.equalsIgnoreCase(workFlowDetails.getAction())) {
                agreement.setStatus(Status.CANCELLED);
                agreement.setIsUnderWorkflow(Boolean.FALSE);
            } else if (WF_ACTION_PRINT_NOTICE.equalsIgnoreCase(workFlowDetails.getAction()))
                agreement.setIsUnderWorkflow(Boolean.FALSE);
        final TaskResponse taskResponse = workFlowRepository.updateWorkflow(agreementRequest);
        log.info("the taskResponse from workflow update : " + taskResponse);
        return agreement;
    }

    public Agreement updateRemission(final AgreementRequest agreementRequest) {
        final Agreement agreement = agreementRequest.getAgreement();
        final RequestInfo requestInfo = agreementRequest.getRequestInfo();
        final String userId = requestInfo.getUserInfo().getId().toString();
        final WorkflowDetails workFlowDetails = agreement.getWorkflowDetails();
        updateAuditDetails(agreement, requestInfo);
        if (workFlowDetails != null)
            if (WF_ACTION_APPROVE.equalsIgnoreCase(workFlowDetails.getAction())) {
                agreementRepository.updateExistingAgreementAsHistory(agreement, userId);
                agreement.setStatus(Status.ACTIVE);
                agreement.setAgreementDate(new Date());
                final List<Demand> demands = demandService.updateDemandOnRemission(agreement,
                        agreementRequest.getRequestInfo());
                final DemandResponse demandResponse = demandRepository.updateDemand(demands,
                        agreementRequest.getRequestInfo());
                final List<String> demandList = demandResponse.getDemands().stream().map(Demand::getId)
                        .collect(Collectors.toList());
                agreement.setDemands(demandList);
                agreement.setNoticeNumber(
                        noticeNumberUtil.getNoticeNumber(agreement.getAction(), agreement.getTenantId(), requestInfo));

            } else if (WF_ACTION_REJECT.equalsIgnoreCase(workFlowDetails.getAction()))
                agreement.setStatus(Status.REJECTED);
            else if (WF_ACTION_CANCEL.equalsIgnoreCase(workFlowDetails.getAction())) {
                agreement.setStatus(Status.CANCELLED);
                agreement.setIsUnderWorkflow(Boolean.FALSE);
            } else if (WF_ACTION_PRINT_NOTICE.equalsIgnoreCase(workFlowDetails.getAction()))
                agreement.setIsUnderWorkflow(Boolean.FALSE);
        final TaskResponse taskResponse = workFlowRepository.updateWorkflow(agreementRequest);
        log.info("the taskResponse from workflow update : " + taskResponse);
        return agreement;
    }

    public Agreement saveRemission(final AgreementRequest agreementRequest) {
        final Agreement agreement = agreementRequest.getAgreement();
        agreement.setId(agreementRepository.getAgreementID());
        final List<Demand> demands = demandService.updateDemandOnRemission(agreement, agreementRequest.getRequestInfo());
        final DemandResponse demandResponse = demandRepository.updateDemand(demands, agreementRequest.getRequestInfo());
        final List<String> demandList = demandResponse.getDemands().stream().map(Demand::getId)
                .collect(Collectors.toList());
        agreement.setDemands(demandList);
        agreementRepository.saveAgreement(agreementRequest);
        return agreement;
    }

    private List<Demand> prepareDemandsForRenewalApproval(final AgreementRequest agreementRequest) {
        final String demandId = agreementRequest.getAgreement().getDemands().get(0);
        final DemandSearchCriteria demandSearchCriteria = new DemandSearchCriteria();
        final List<Demand> demands = new ArrayList<>();
        if (demandId != null) {
            demandSearchCriteria.setDemandId(Long.valueOf(demandId));
            final DemandResponse demandResponse = demandRepository
                    .getDemandBySearch(demandSearchCriteria, agreementRequest.getRequestInfo());
            if (demandResponse != null) {
                final Demand demand = demandResponse.getDemands().get(0);
                if (demand != null) {
                    demand.getDemandDetails().addAll(demandService.prepareDemandDetailsForRenewal(agreementRequest,
                            demandRepository.getDemandReasonForRenewal(agreementRequest, true)));
                    demands.add(demand);
                }
            }
        }
        return demands;
    }

    public List<Agreement> searchAgreement(final AgreementCriteria agreementCriteria, final RequestInfo requestInfo) {

        if (agreementCriteria.getToDate() != null)
            agreementCriteria.setToDate(setToTime(agreementCriteria.getToDate()));

        if (agreementCriteria.isAgreementAndAssetAndAllotteeNotNull()) {
            log.info("agreementRepository.findByAllotee");
            return agreementRepository.findByAllotee(agreementCriteria, requestInfo);

        } else if (agreementCriteria.isAssetOnlyNull()) {
            log.info("agreementRepository.findByAllotee");
            return agreementRepository.findByAgreementAndAllotee(agreementCriteria, requestInfo);

        } else if (agreementCriteria.isAllotteeOnlyNull()) {
            log.info("agreementRepository.findByAgreementAndAsset : both agreement and ");
            return agreementRepository.findByAgreementAndAsset(agreementCriteria, requestInfo);

        } else if (agreementCriteria.isAgreementAndAssetNull()
                || agreementCriteria.isAgreementOnlyNull()) {
            log.info("agreementRepository.findByAllotee : only allottee || allotte and asset");
            return agreementRepository.findByAllotee(agreementCriteria, requestInfo);

        } else if (agreementCriteria.isAgreementAndAllotteeNull()) {
            log.info("agreementRepository.findByAsset : only asset");
            return agreementRepository.findByAsset(agreementCriteria, requestInfo);

        } else if (agreementCriteria.isAssetAndAllotteeNull()) {
            log.info("agreementRepository.findByAgreement : only agreement");
            return agreementRepository.findByAgreement(agreementCriteria, requestInfo);
        } else {
            // if no values are given for all the three criteria objects
            // (isAgreementNull && isAssetNull && isAllotteeNull)
            log.info("agreementRepository.findByAgreement : all values null");
            return agreementRepository.findByAgreement(agreementCriteria, requestInfo);
        }
    }

    public List<Agreement> getAgreementsByAgreementNumber(final AgreementCriteria agreementCriteria,
            final RequestInfo requestInfo) {
        return agreementRepository.findByAgreementNumber(agreementCriteria, requestInfo);
    }

    private static Date setToTime(final Date toDate) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(toDate);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    /*
     * calling to prepare the demands for Data entry agreements in Add/Edit demand
     */
    public List<Demand> prepareLegacyDemands(final AgreementRequest agreementRequest) {
        return demandService.prepareLegacyDemands(agreementRequest);
    }

    public List<Demand> prepareDemands(final AgreementRequest agreementRequest) {
        return demandService.prepareDemands(agreementRequest);
    }

    public List<Demand> getDemands(final AgreementRequest agreementRequest) {

        List<Demand> agreementDemands = null;
        final RequestInfo requestInfo = agreementRequest.getRequestInfo();
        final Agreement agreement = agreementRequest.getAgreement();
        final DemandSearchCriteria demandSearchCriteria = new DemandSearchCriteria();

        demandSearchCriteria.setDemandId(Long.valueOf(agreement.getDemands().get(0)));
        agreementDemands = demandRepository.getDemandBySearch(demandSearchCriteria, requestInfo).getDemands();
        return agreementDemands;
    }

    private List<String> updateDemand(final List<String> demands, final List<Demand> legacydemands,
            final RequestInfo requestInfo) {
        DemandResponse demandResponse = null;
        if (demands == null)
            demandResponse = demandRepository.createDemand(legacydemands, requestInfo);
        else
            demandResponse = demandRepository.updateDemand(legacydemands, requestInfo);
        return demandResponse.getDemands().stream().map(Demand::getId).collect(Collectors.toList());
    }

    private void setInitiatorPosition(final AgreementRequest agreementRequest) {

        final RequestInfo requestInfo = agreementRequest.getRequestInfo();
        final Agreement agreement = agreementRequest.getAgreement();
        final WorkflowDetails workFlowDetails = agreement.getWorkflowDetails();

        final RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
        requestInfoWrapper.setRequestInfo(agreementRequest.getRequestInfo());
        final String tenantId = requestInfo.getUserInfo().getTenantId();

        Allottee allottee = new Allottee();
        allottee.setUserName(requestInfo.getUserInfo().getUserName());
        allottee.setTenantId(tenantId);
        final AllotteeResponse allotteeResponse = allotteeRepository.getAllottees(allottee,
                requestInfoWrapper.getRequestInfo());
        allottee = allotteeResponse.getAllottee().get(0);

        final PositionResponse positionResponse = positionRestRepository.getPositions(allottee.getId().toString(), tenantId,
                requestInfoWrapper);

        final List<Position> positionList = positionResponse.getPosition();
        if (positionList == null || positionList.isEmpty())
            throw new RuntimeException("the respective user has no positions");
        final Map<String, Long> positionMap = new HashMap<>();

        for (final Position position : positionList)
            positionMap.put(position.getDeptdesig().getDesignation().getName(), position.getId());

        final LamsConfigurationGetRequest lamsConfigurationGetRequest = new LamsConfigurationGetRequest();
        lamsConfigurationGetRequest.setName(LAMS_WORKFLOW_INITIATOR_DESIGNATION);
        final List<String> assistantDesignations = lamsConfigurationService.getLamsConfigurations(lamsConfigurationGetRequest)
                .get(LAMS_WORKFLOW_INITIATOR_DESIGNATION);

        for (final String desginationName : assistantDesignations) {
            log.info("desg name" + desginationName);
            if (positionMap.containsKey(desginationName)) {
                workFlowDetails.setInitiatorPosition(positionMap.get(desginationName));
                log.info(" the initiator name  :: " + desginationName + "the value for key"
                        + workFlowDetails.getInitiatorPosition());
            }
        }
    }

    public void updateAdvanceFlag(final Agreement agreement) {
        if (agreement.getAcknowledgementNumber() != null)
            agreementRepository.updateAgreementAdvance(agreement.getAcknowledgementNumber());
    }

    private void setAuditDetails(final Agreement agreement, final RequestInfo requestInfo) {
        final String requesterId = requestInfo.getUserInfo().getId().toString();
        agreement.setCreatedBy(requesterId);
        agreement.setCreatedDate(new Date());
        agreement.setLastmodifiedBy(requesterId);
        agreement.setLastmodifiedDate(new Date());
    }

    private void updateAuditDetails(final Agreement agreement, final RequestInfo requestInfo) {
        final String requesterId = requestInfo.getRequesterId().toString();
        agreement.setLastmodifiedBy(requesterId);
        agreement.setLastmodifiedDate(new Date());
    }

    public Date getExpiryDate(final Agreement agreement) {
        Date commencementDate;
        if (Action.RENEWAL.equals(agreement.getAction()))
            commencementDate = agreement.getRenewalDate();
        else
            commencementDate = agreement.getCommencementDate();
        final Instant instant = Instant.ofEpochMilli(commencementDate.getTime());
        final LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        LocalDate expiryDate = localDateTime.toLocalDate();
        expiryDate = expiryDate.plusYears(agreement.getTimePeriod());
        expiryDate = expiryDate.minusDays(1);
        return Date.from(expiryDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private Date getAdjustmentDate(final Agreement agreement) {

        Long monthsToReduce = (long) (agreement.getSecurityDeposit() / agreement.getRent());
        if (PaymentCycle.QUARTER.equals(agreement.getPaymentCycle()))
            monthsToReduce = monthsToReduce * 3;
        else if (PaymentCycle.HALFYEAR.equals(agreement.getPaymentCycle()))
            monthsToReduce = monthsToReduce * 6;
        else if (PaymentCycle.ANNUAL.equals(agreement.getPaymentCycle()))
            monthsToReduce = monthsToReduce * 12;
        final Date expiryDate = agreement.getExpiryDate();
        final Instant instant = Instant.ofEpochMilli(expiryDate.getTime());
        final LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        LocalDate adjustmentDate = localDateTime.toLocalDate();
        adjustmentDate = adjustmentDate.minusMonths(monthsToReduce);
        adjustmentDate = adjustmentDate.plusDays(1);
        return Date.from(adjustmentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

    }

    public String checkRenewalStatus(final AgreementRequest agreementRequest) {
        final Agreement agreement = agreementRequest.getAgreement();
        return agreementRepository.getRenewalStatus(agreement.getAgreementNumber(), agreement.getTenantId());
    }

    public String checkObjectionStatus(final AgreementRequest agreementRequest) {
        final Agreement agreement = agreementRequest.getAgreement();
        return agreementRepository.getObjectionStatus(agreement.getAgreementNumber(), agreement.getTenantId());
    }

    public void setDetailsByAssetCategory(final Agreement agreement) {
        Boolean validCategory;
        final String assetCategory = agreement.getAsset().getCategory().getName();
        validCategory = AUCTION_CATEGORIES.stream().anyMatch(category -> category.equalsIgnoreCase(assetCategory));

        if (validCategory) {
            agreement.setExpiryDate(getEffectiveFinYearToDate());
            final Calendar cal = Calendar.getInstance();
            cal.setTime(agreement.getExpiryDate());
            cal.add(Calendar.MONTH, -3);
            cal.add(Calendar.MINUTE, 1);
            final Date adjustmentDate = cal.getTime();
            agreement.setAdjustmentStartDate(adjustmentDate);
            if (PaymentCycle.ANNUAL.equals(agreement.getPaymentCycle()))
                agreement.setRent(agreement.getAuctionAmount());

        }

    }

    public Date getEffectiveFinYearToDate() {
        final Calendar cal = Calendar.getInstance();
        final int month = cal.get(Calendar.MONTH);
        if (month < 3)
            cal.set(Calendar.MONTH, 2);
        else {
            cal.set(Calendar.MONTH, 2);
            cal.add(Calendar.YEAR, 1);
        }
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        return cal.getTime();
    }

}
