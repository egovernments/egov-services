package org.egov.works.measurementbook.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.commons.utils.CommonUtils;
import org.egov.works.measurementbook.config.PropertiesManager;
import org.egov.works.measurementbook.domain.repository.builder.IdGenerationRepository;
import org.egov.works.measurementbook.domain.validator.ContractorBillValidator;
import org.egov.works.measurementbook.utils.MeasurementBookUtils;
import org.egov.works.measurementbook.web.contract.AssetForBill;
import org.egov.works.measurementbook.web.contract.ContractorBill;
import org.egov.works.measurementbook.web.contract.ContractorBillRequest;
import org.egov.works.measurementbook.web.contract.ContractorBillResponse;
import org.egov.works.measurementbook.web.contract.ContractorBillSearchContract;
import org.egov.works.measurementbook.web.contract.FinancialStatus;
import org.egov.works.measurementbook.web.contract.MeasurementBookForContractorBill;
import org.egov.works.measurementbook.web.contract.RequestInfo;
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
    

    @Transactional
	public ContractorBillResponse create(ContractorBillRequest contractorBillRequest) {
		validator.validateContractorBill(contractorBillRequest, true);
	    CommonUtils commonUtils = new CommonUtils();
	    FinancialStatus finStatus = new FinancialStatus();
	    for (final ContractorBill contractorBill : contractorBillRequest.getContractorBills()) {
	        contractorBill.setId(commonUtils.getUUID());
	        contractorBill.setAuditDetails(measurementBookUtils.setAuditDetails(contractorBillRequest.getRequestInfo(), false));
	        String billNumber = idGenerationRepository.generateBillNumber(contractorBill.getTenantId(), contractorBillRequest.getRequestInfo());
	        contractorBill.setBillNumber(measurementBookUtils.getCityCode(contractorBill.getTenantId(), contractorBillRequest.getRequestInfo())
                                + "/" + billNumber);
	        List<AssetForBill> assetForBill = new ArrayList<>();
	        for (final AssetForBill assetBill : contractorBill.getAssets()){ 
	            assetBill.setId(commonUtils.getUUID());
	            assetBill.setAuditDetails(measurementBookUtils.setAuditDetails(contractorBillRequest.getRequestInfo(), false));
	            assetBill.setContractorBill(contractorBill.getId());
	            assetForBill.add(assetBill);
	        }
	        contractorBill.setAssets(assetForBill);
	        List<MeasurementBookForContractorBill> mbForContractorBill = new ArrayList<>();
	        for (final MeasurementBookForContractorBill mbContractorBill : contractorBill.getMbForContractorBill()){
	            mbContractorBill.setId(commonUtils.getUUID());
	            mbContractorBill.setAuditDetails(measurementBookUtils.setAuditDetails(contractorBillRequest.getRequestInfo(), false));
	            mbContractorBill.setContractorBill(contractorBill);
	            mbForContractorBill.add(mbContractorBill);
            }
	        contractorBill.setMbForContractorBill(mbForContractorBill);
	        if(contractorBill.getSpillOver()){
	            JSONArray responseJSONArray = measurementBookUtils.getMDMSData(CommonConstants.APPCONFIGURATION_OBJECT, CommonConstants.CODE,
	                    CommonConstants.CONTRACTORBILL_SPILLOVER_WORKFLOW_REQUIRED, contractorBill.getTenantId(), contractorBillRequest.getRequestInfo(),
	                    CommonConstants.MODULENAME_WORKS);
	            if (responseJSONArray != null && !responseJSONArray.isEmpty()) {
	                Map<String, Object> jsonMap = (Map<String, Object>) responseJSONArray.get(0);
	                if (jsonMap.get("value").toString().equalsIgnoreCase("Yes")){
	                    finStatus.setCode("CREATED");
	                    contractorBill.setStatus(finStatus);
	                } else {
	                    finStatus.setCode("APPROVED");
	                    contractorBill.setStatus(finStatus);
	                }
	            }
	        }
	        else{
	            finStatus.setCode("CREATED");
	            contractorBill.setStatus(finStatus);
	        }
	    }
		kafkaTemplate.send(propertiesManager.getContractorBillCreateUpdateTopic(), contractorBillRequest);
		ContractorBillResponse contractorBillResponse = new ContractorBillResponse();
		contractorBillResponse.setContractorBills(contractorBillRequest.getContractorBills());
		return contractorBillResponse;
	}

	public ContractorBillResponse update(ContractorBillRequest contractorBillRequest) {
		ContractorBillResponse contractorBillResponse = new ContractorBillResponse();
		return contractorBillResponse;
	}

	public ContractorBillResponse search(ContractorBillSearchContract contractorBillSearchContract,
			RequestInfo requestInfo) {
		ContractorBillResponse contractorBillResponse = new ContractorBillResponse();
		return contractorBillResponse;
	}
}
