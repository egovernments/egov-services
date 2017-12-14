package org.egov.works.masters.domain.validator;

import java.util.HashMap;
import java.util.Map;

import org.egov.tracer.model.CustomException;
import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.masters.domain.repository.ContractorRepository;
import org.egov.works.masters.utils.Constants;
import org.egov.works.masters.web.contract.Contractor;
import org.egov.works.masters.web.contract.ContractorRequest;
import org.egov.works.masters.web.repository.MdmsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import net.minidev.json.JSONArray;

@Service
public class ContractorValidator {

    @Autowired
    private MdmsRepository mdmsRepository;

    @Autowired
    private ContractorRepository contractorRepository;

    public void validateContractor(ContractorRequest contractorRequest, String tenantId, Boolean isNew) {
        JSONArray mdmsResponse = null;
        Map<String, String> messages = new HashMap<>();
        Boolean isDataValid = Boolean.FALSE;
        Boolean financialIntegrationReq = Boolean.FALSE;
        Contractor dbContractorObj = null;

        mdmsResponse = mdmsRepository.getByCriteria(tenantId, CommonConstants.MODULENAME_WORKS,
                CommonConstants.APPCONFIGURATION_MDMS_OBJECT, CommonConstants.CODE,
                CommonConstants.FINANCIAL_INTEGRATION_REQUIRED_APPCONFIG, contractorRequest.getRequestInfo());
        if (mdmsResponse != null && !mdmsResponse.isEmpty()) {
            Map<String, Object> jsonMap = (Map<String, Object>) mdmsResponse.get(0);
            if (jsonMap.get("value") != null && jsonMap.get("value").toString().equalsIgnoreCase("Yes"))
                financialIntegrationReq = Boolean.TRUE;
        }

        for (final Contractor contractor : contractorRequest.getContractors()) {
            if (!StringUtils.isBlank(contractor.getCode())) {
                // In case of create, Validate for code uniqueness.
                if (isNew) {
                    dbContractorObj = contractorRepository.getByCode(contractor.getCode(), tenantId);
                    if (dbContractorObj != null) {
                        messages.put(Constants.KEY_CONTRACTOR_CODE_INVALID, Constants.MESSAGE_CONTRACTOR_CODE_INVALID);
                        isDataValid = Boolean.TRUE;
                    }
                } 
                // In case of update, do not allow to modify code
                if (!isNew){
                    dbContractorObj = contractorRepository.findByID(contractor.getId(), tenantId);
                    if (dbContractorObj != null) {
                       if(!dbContractorObj.getCode().equalsIgnoreCase(contractor.getCode())) {
                           messages.put(Constants.KEY_CONTRACTOR_CODE_MODIFY, Constants.MESSAGE_CONTRACTOR_CODE_MODIFY);
                           isDataValid = Boolean.TRUE;
                       }
                    }
                }
            }

            if (contractor.getStatus() != null) {
                mdmsResponse = mdmsRepository.getByCriteria(contractor.getTenantId(), CommonConstants.MODULENAME_WORKS,
                        CommonConstants.WORKS_STATUS_APPCONFIG, CommonConstants.CODE, contractor.getStatus(),
                        contractorRequest.getRequestInfo());
                if (mdmsResponse == null || mdmsResponse.size() == 0) {
                    messages.put(Constants.KEY_CONTRACTOR_STATUS_INVALID,
                            Constants.MESSAGE_CONTRACTOR_STATUS_INVALID + contractor.getStatus());
                    isDataValid = Boolean.TRUE;
                }
            }

            // Validate bank/ accountnumber/ accountcode/ ifsccode only if
            // financial integration is configured
            if (financialIntegrationReq) {
                if (contractor.getBank() != null && contractor.getBank().getCode() != null) {
                    mdmsResponse = mdmsRepository.getByCriteria(contractor.getTenantId(),
                            CommonConstants.MODULENAME_WORKS, CommonConstants.MASTERNAME_BANK, CommonConstants.CODE,
                            contractor.getBank().getCode(), contractorRequest.getRequestInfo());
                    if (mdmsResponse == null || mdmsResponse.size() == 0) {
                        messages.put(Constants.KEY_CONTRACTOR_BANK_CODE_INVALID,
                                Constants.MESSAGE_CONTRACTOR_BANK_CODE_INVALID + contractor.getBank().getCode());
                        isDataValid = Boolean.TRUE;
                    }
                }

                if (contractor.getAccountCode() != null && contractor.getAccountCode().getGlcode() != null) {
                    mdmsResponse = mdmsRepository.getByCriteria(contractor.getTenantId(),
                            CommonConstants.MODULENAME_WORKS, CommonConstants.MASTERNAME_CHARTOFACCOUNTS,
                            CommonConstants.GLCODE, contractor.getAccountCode().getGlcode(),
                            contractorRequest.getRequestInfo());
                    if (mdmsResponse == null || mdmsResponse.size() == 0) {
                        messages.put(Constants.KEY_CONTRACTOR_ACCOUNTCODE_GLCODE_INVALID,
                                Constants.MESSAGE_CONTRACTOR_ACCOUNTCODE_GLCODE_INVALID
                                        + contractor.getAccountCode().getGlcode());
                        isDataValid = Boolean.TRUE;
                    }
                }

                if (contractor.getBankAccountNumber() == null) {
                    messages.put(Constants.KEY_CONTRACTOR_BANKACCOUNTNUMBER_INVALID,
                            Constants.MESSAGE_CONTRACTOR_BANKACCOUNTNUMBER_INVALID);
                    isDataValid = Boolean.TRUE;
                }
                if (contractor.getAccountCode() == null) {
                    messages.put(Constants.KEY_CONTRACTOR_ACCOUNTCODE_INVALID,
                            Constants.MESSAGE_CONTRACTOR_ACCOUNTCODE_INVALID);
                    isDataValid = Boolean.TRUE;
                }
                if (contractor.getIfscCode() == null) {
                    messages.put(Constants.KEY_CONTRACTOR_IFSCCODE_INVALID,
                            Constants.MESSAGE_CONTRACTOR_IFSCCODE_INVALID);
                    isDataValid = Boolean.TRUE;
                }
            }
            // Contractorclass reference validation
            if (contractor.getContractorClass() != null && contractor.getContractorClass().getPropertyClass() != null) {
                mdmsResponse = mdmsRepository.getByCriteria(contractor.getTenantId(), CommonConstants.MODULENAME_WORKS,
                        CommonConstants.MASTERNAME_CONTRACTORCLASS, "class",
                        contractor.getContractorClass().getPropertyClass(), contractorRequest.getRequestInfo());
                if (mdmsResponse == null || mdmsResponse.size() == 0) {
                    messages.put(Constants.KEY_CONTRACTOR_CONTRACTORCLASS_CLASS_INVALID,
                            Constants.MESSAGE_CONTRACTOR_CONTRACTORCLASS_CLASS_INVALID
                                    + contractor.getContractorClass().getPropertyClass());
                    isDataValid = Boolean.TRUE;
                }
            }
        }
        if (isDataValid)
            throw new CustomException(messages);
    }
}
