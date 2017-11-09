package org.egov.works.masters.domain.validator;

import java.util.HashMap;
import java.util.Map;

import org.egov.tracer.model.CustomException;
import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.masters.utils.Constants;
import org.egov.works.masters.web.contract.Contractor;
import org.egov.works.masters.web.contract.ContractorRequest;
import org.egov.works.masters.web.repository.MdmsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.minidev.json.JSONArray;

@Service
public class ContractorValidator {

    @Autowired
    private MdmsRepository mdmsRepository;

    public void validateContractor(ContractorRequest contractorRequest, String tenantId) {
        JSONArray mdmsResponse = null;
        Map<String, String> messages = new HashMap<>();
        Boolean isDataValid = Boolean.FALSE;
        Boolean financialIntegrationReq = Boolean.FALSE;

        mdmsResponse = mdmsRepository.getByCriteria(tenantId, CommonConstants.MODULENAME_WORKS,
                CommonConstants.APPCONFIGURATION_MDMS_OBJECT, CommonConstants.CODE,
                CommonConstants.FINANCIAL_INTEGRATION_REQUIRED_APPCONFIG, contractorRequest.getRequestInfo());
        if (mdmsResponse != null && !mdmsResponse.isEmpty()) {
            Map<String, Object> jsonMap = (Map<String, Object>) mdmsResponse.get(0);
            if (jsonMap.get("value") != null && jsonMap.get("value").toString().equalsIgnoreCase("Yes"))
                financialIntegrationReq = Boolean.TRUE;
        }

        for (final Contractor contractor : contractorRequest.getContractors()) {
            if (contractor.getContractorClass() != null && contractor.getContractorClass().getPropertyClass() != null) {
                if (financialIntegrationReq) {
                    // TODO : Need to add MDMS data for Bank and COA.
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
