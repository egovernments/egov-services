package org.egov.works.measurementbook.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.commons.utils.CommonUtils;
import org.egov.works.measurementbook.config.Constants;
import org.egov.works.measurementbook.config.PropertiesManager;
import org.egov.works.measurementbook.domain.repository.ContractorBillRepository;
import org.egov.works.measurementbook.domain.repository.LetterOfAcceptanceRepository;
import org.egov.works.measurementbook.domain.repository.MeasurementBookRepository;
import org.egov.works.measurementbook.domain.repository.builder.IdGenerationRepository;
import org.egov.works.measurementbook.domain.validator.ContractorBillValidator;
import org.egov.works.measurementbook.utils.MeasurementBookUtils;
import org.egov.works.measurementbook.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.minidev.json.JSONArray;

@Service
public class ContractorBillService {

    @Autowired
    private ContractorBillValidator validator;

    @Autowired
    private MeasurementBookUtils measurementBookUtils;

    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private PropertiesManager propertiesManager;

    @Autowired
    private IdGenerationRepository idGenerationRepository;

    @Autowired
    private ContractorBillRepository contractorBillRepository;

    @Autowired
    private MeasurementBookRepository measurementBookRepository;

    @Autowired
    private LetterOfAcceptanceRepository letterOfAcceptanceRepository;
    
    @Transactional
    public ContractorBillResponse create(ContractorBillRequest contractorBillRequest) {
        for (final ContractorBill contractorBill : contractorBillRequest.getContractorBills()) { 
            String billNumber = idGenerationRepository.generateBillNumber(contractorBill.getTenantId(),
                    contractorBillRequest.getRequestInfo());
            contractorBill.setBillNumber(measurementBookUtils.getCityCode(contractorBill.getTenantId(),
                    contractorBillRequest.getRequestInfo()) + "/" + propertiesManager.getBillNumberPrefix() + "/"
                    + billNumber);
            
        }

        validator.validateContractorBill(contractorBillRequest, true);
        CommonUtils commonUtils = new CommonUtils();
        BillStatus finStatus = new BillStatus();
        List<ContractorBill> contractorBills = new ArrayList<>();
        for (final ContractorBill contractorBill : contractorBillRequest.getContractorBills()) {
            contractorBill.setId(commonUtils.getUUID());
            contractorBill.setAuditDetails(
                    measurementBookUtils.setAuditDetails(contractorBillRequest.getRequestInfo(), false));

            List<LetterOfAcceptance> letterOfAcceptances =
                    letterOfAcceptanceRepository.searchLoaByLoaEstimateId(contractorBill.getTenantId(), contractorBill.getLetterOfAcceptanceEstimate(), contractorBillRequest.getRequestInfo());
            if(letterOfAcceptances != null && !letterOfAcceptances.isEmpty())
                contractorBill.setLetterOfAcceptanceEstimate(letterOfAcceptances.get(0).getLetterOfAcceptanceEstimates().get(0));
            
            
            List<AssetForBill> assetForBill = new ArrayList<>();
            if(contractorBill.getAssets() != null) {
                for (final AssetForBill assetBill : contractorBill.getAssets()) {
                    assetBill.setId(commonUtils.getUUID());
                    assetBill.setAuditDetails(
                            measurementBookUtils.setAuditDetails(contractorBillRequest.getRequestInfo(), false));
                    assetBill.setContractorBill(contractorBill.getId());
                    assetForBill.add(assetBill);
                }
            }
            contractorBill.setAssets(assetForBill);
            List<MeasurementBookForContractorBill> mbForContractorBill = new ArrayList<>();
            if(contractorBill.getMbForContractorBill() != null) {
                for (final MeasurementBookForContractorBill mbContractorBill : contractorBill.getMbForContractorBill()) {
                    mbContractorBill.setId(commonUtils.getUUID());
                    mbContractorBill.setAuditDetails(
                            measurementBookUtils.setAuditDetails(contractorBillRequest.getRequestInfo(), false));
                    mbContractorBill.setContractorBill(contractorBill.getId());
                    mbForContractorBill.add(mbContractorBill);
                }
            }
            contractorBill.setMbForContractorBill(mbForContractorBill);
            if (contractorBill.getSpillOver() != null && contractorBill.getSpillOver()) {
                JSONArray responseJSONArray = measurementBookUtils.getMDMSData(CommonConstants.APPCONFIGURATION_OBJECT,
                        CommonConstants.CODE, CommonConstants.CONTRACTORBILL_SPILLOVER_WORKFLOW_REQUIRED,
                        contractorBill.getTenantId(), contractorBillRequest.getRequestInfo(),
                        CommonConstants.MODULENAME_WORKS);
                if (responseJSONArray != null && !responseJSONArray.isEmpty()) {
                    Map<String, Object> jsonMap = (Map<String, Object>) responseJSONArray.get(0);
                    if (jsonMap.get("value").toString().equalsIgnoreCase("Yes")) {
                        finStatus.setCode("CREATED");
                        contractorBill.setStatus(finStatus);
                    } else {
                        finStatus.setCode("APPROVED");
                        contractorBill.setStatus(finStatus);
                    }
                }
            } else {
                finStatus.setCode("CREATED");
                contractorBill.setStatus(finStatus);
            }

            if(contractorBill.getBillType() != null && contractorBill.getBillType().equalsIgnoreCase(Constants.BILL_SUB_TYPE_FINAL) && contractorBill.getStatus() != null && (contractorBill.getStatus().getCode().equals(CommonConstants.STATUS_CREATED) ||
                contractorBill.getStatus().getCode().equals(CommonConstants.STATUS_APPROVED))) {
                contractorBills.add(contractorBill);
            }
        }
        kafkaTemplate.send(propertiesManager.getContractorBillCreateUpdateTopic(), contractorBillRequest);
        if(contractorBills != null && !contractorBills.isEmpty()) {
            ContractorBillRequest backUpdateRequest = new ContractorBillRequest();
            backUpdateRequest.setContractorBills(contractorBills);
            kafkaTemplate.send(propertiesManager.getWorksLoaBackUpdateOnCreateBill(), backUpdateRequest);
            kafkaTemplate.send(propertiesManager.getWorksLoaBackUpdateOnCreateMBAndBill(), backUpdateRequest);

        }

        updateMBForBillStatus(contractorBillRequest);
        ContractorBillResponse contractorBillResponse = new ContractorBillResponse();
        contractorBillResponse.setContractorBills(contractorBillRequest.getContractorBills());
        return contractorBillResponse;
    }

    public ContractorBillResponse update(ContractorBillRequest contractorBillRequest) {
        ContractorBillResponse contractorBillResponse = new ContractorBillResponse();
        validator.validateContractorBill(contractorBillRequest, false);
        List<ContractorBill> contractorBills = new ArrayList<>();
        for (final ContractorBill contractorBill : contractorBillRequest.getContractorBills()) {
            populateAuditDetails(contractorBillRequest.getRequestInfo(), contractorBill);
            List<LetterOfAcceptance> letterOfAcceptances =
                    letterOfAcceptanceRepository.searchLoaByLoaEstimateId(contractorBill.getTenantId(), contractorBill.getLetterOfAcceptanceEstimate(), contractorBillRequest.getRequestInfo());
            if(letterOfAcceptances != null && !letterOfAcceptances.isEmpty())
                contractorBill.setLetterOfAcceptanceEstimate(letterOfAcceptances.get(0).getLetterOfAcceptanceEstimates().get(0));
            if(contractorBill.getStatus() != null && contractorBill.getStatus().getCode().equals(CommonConstants.STATUS_CANCELLED))
                contractorBills.add(contractorBill);
        }
        kafkaTemplate.send(propertiesManager.getContractorBillCreateUpdateTopic(), contractorBillRequest);
        if(contractorBills != null && !contractorBills.isEmpty()) {
            ContractorBillRequest backUpdateRequest = new ContractorBillRequest();
            backUpdateRequest.setContractorBills(contractorBills);
            kafkaTemplate.send(propertiesManager.getWorksLoaBackUpdateOnCreateBill(), backUpdateRequest);
        }
        updateMBForBillStatus(contractorBillRequest);
        contractorBillResponse.setContractorBills(contractorBillRequest.getContractorBills());
        return contractorBillResponse;
    }

    private void populateAuditDetails(final RequestInfo requestInfo, final ContractorBill contractorBill) {
        contractorBill.setAuditDetails(measurementBookUtils.setAuditDetails(requestInfo, true));
        if(contractorBill.getAssets() != null && !contractorBill.getAssets().isEmpty())
            for (final AssetForBill assetForBill : contractorBill.getAssets())
                assetForBill.setAuditDetails(measurementBookUtils.setAuditDetails(requestInfo, true));
        for (final MeasurementBookForContractorBill mbForBill : contractorBill.getMbForContractorBill())
            mbForBill.setAuditDetails(measurementBookUtils.setAuditDetails(requestInfo, true));
    }

    public ContractorBillResponse search(ContractorBillSearchContract contractorBillSearchContract,
            RequestInfo requestInfo) {
        ContractorBillResponse contractorBillResponse = new ContractorBillResponse();
        contractorBillResponse.setContractorBills(
                contractorBillRepository.searchContractorBills(contractorBillSearchContract, requestInfo));
        return contractorBillResponse;
    }

    public void updateMBForBillStatus(ContractorBillRequest contractorBillRequest){
        MeasurementBookRequest measurementBookRequest = new MeasurementBookRequest();
        measurementBookRequest.setRequestInfo(contractorBillRequest.getRequestInfo());
        List<MeasurementBook> measurementBooks = new ArrayList<>();
        for(ContractorBill contractorBill : contractorBillRequest.getContractorBills()) {
            MeasurementBook measurementBook = measurementBookRepository.searchMeasurementBooks(MeasurementBookSearchContract.
                    builder().loaEstimateId(contractorBill.getLetterOfAcceptanceEstimate().getId()).tenantId(contractorBill.getTenantId()).build(),
                    contractorBillRequest.getRequestInfo()).get(0);
            if(contractorBill.getDeleted())
                measurementBook.setBillExists(Boolean.FALSE);
            else
                measurementBook.setBillExists(Boolean.TRUE);
            measurementBooks.add(measurementBook);
        }
        measurementBookRequest.setMeasurementBooks(measurementBooks);
        kafkaTemplate.send(propertiesManager.getMeasurementBookBackUpdateForBillStatus(), measurementBookRequest);
    }
}
