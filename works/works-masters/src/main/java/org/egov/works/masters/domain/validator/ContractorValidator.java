package org.egov.works.masters.domain.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.tracer.model.CustomException;
import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.masters.domain.repository.ContractorRepository;
import org.egov.works.masters.utils.Constants;
import org.egov.works.masters.web.contract.Contractor;
import org.egov.works.masters.web.contract.ContractorRequest;
import org.egov.works.masters.web.contract.ContractorSearchCriteria;
import org.egov.works.masters.web.contract.WorksStatus;
import org.egov.works.masters.web.repository.MdmsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONArray;

@Service
public class ContractorValidator {

    @Autowired
    private MdmsRepository mdmsRepository;

    @Autowired
    private ContractorRepository contractorRepository;

    public void validateContractor(ContractorRequest contractorRequest, Boolean isNew) {
        JSONArray mdmsResponse = null;
        Map<String, String> messages = new HashMap<>();
        Boolean isDataValid = Boolean.FALSE;
        Boolean financialIntegrationReq = Boolean.FALSE;
        Contractor dbContractorObj = null;
        List<Contractor> dbContractorObjByMobileNo = null;
        List<Contractor> dbContractorObjByEmail = null;
        ContractorSearchCriteria contractorSearchCriteria = null;

        for (final Contractor contractor : contractorRequest.getContractors()) {

            mdmsResponse = mdmsRepository.getByCriteria(contractor.getTenantId(), CommonConstants.MODULENAME_WORKS,
                    CommonConstants.APPCONFIGURATION_MDMS_OBJECT, CommonConstants.CODE,
                    CommonConstants.FINANCIAL_INTEGRATION_REQUIRED_APPCONFIG, contractorRequest.getRequestInfo());
            if (mdmsResponse != null && !mdmsResponse.isEmpty()) {
                Map<String, Object> jsonMap = (Map<String, Object>) mdmsResponse.get(0);
                if (jsonMap.get("value") != null && jsonMap.get("value").toString().equalsIgnoreCase("Yes"))
                    financialIntegrationReq = Boolean.TRUE;
            }

            contractorSearchCriteria = new ContractorSearchCriteria();
            contractorSearchCriteria.setMobileNumber(contractor.getMobileNumber());
            contractorSearchCriteria.setTenantId(contractor.getTenantId());
            dbContractorObjByMobileNo = contractorRepository.getContractorByCriteria(contractorSearchCriteria);

            contractorSearchCriteria = new ContractorSearchCriteria();
            contractorSearchCriteria.setTenantId(contractor.getTenantId());
            contractorSearchCriteria.setEmailId(contractor.getEmail());
            dbContractorObjByEmail = contractorRepository.getContractorByCriteria(contractorSearchCriteria);

            if (!StringUtils.isBlank(contractor.getCode())) {
                // In case of create, Validate for code / mobilenumber / email
                // uniqueness.
                if (isNew) {
                    dbContractorObj = contractorRepository.getByCode(contractor.getCode(), contractor.getTenantId());
                    if (dbContractorObj != null) {
                        messages.put(Constants.KEY_CONTRACTOR_CODE_INVALID, Constants.MESSAGE_CONTRACTOR_CODE_INVALID);
                        isDataValid = Boolean.TRUE;
                    }

                    if (dbContractorObjByMobileNo != null && !dbContractorObjByMobileNo.isEmpty()) {
                        messages.put(Constants.KEY_CONTRACTOR_MOBILE_INVALID,
                                Constants.MESSAGE_CONTRACTOR_MOBILE_INVALID);
                        isDataValid = Boolean.TRUE;
                    }

                    if (dbContractorObjByEmail != null && !dbContractorObjByEmail.isEmpty()) {
                        messages.put(Constants.KEY_CONTRACTOR_EMAIL_INVALID,
                                Constants.MESSAGE_CONTRACTOR_EMAIL_INVALID);
                        isDataValid = Boolean.TRUE;
                    }

                }
                // In case of update, do not allow to modify code
                if (!isNew) {
                    dbContractorObj = contractorRepository.findByID(contractor.getId(), contractor.getTenantId());
                    if (dbContractorObj != null) {
                        if (!dbContractorObj.getCode().equalsIgnoreCase(contractor.getCode())) {
                            messages.put(Constants.KEY_CONTRACTOR_CODE_MODIFY,
                                    Constants.MESSAGE_CONTRACTOR_CODE_MODIFY);
                            isDataValid = Boolean.TRUE;
                        }
                    }

                    if (dbContractorObjByMobileNo != null && !dbContractorObjByMobileNo.isEmpty()) {
                        if (!dbContractorObjByMobileNo.get(0).getMobileNumber()
                                .equalsIgnoreCase(contractor.getMobileNumber())) {
                            messages.put(Constants.KEY_CONTRACTOR_MOBILE_INVALID,
                                    Constants.MESSAGE_CONTRACTOR_MOBILE_INVALID);
                            isDataValid = Boolean.TRUE;
                        }
                    }

                    if (dbContractorObjByEmail != null && !dbContractorObjByEmail.isEmpty()) {
                        if (!dbContractorObjByEmail.get(0).getEmail()
                                .equalsIgnoreCase(contractor.getEmail())) {
                            messages.put(Constants.KEY_CONTRACTOR_EMAIL_INVALID,
                                    Constants.MESSAGE_CONTRACTOR_EMAIL_INVALID);
                            isDataValid = Boolean.TRUE;
                        }
                    }
                }
            }

            // Contractor status reference validation
            if (contractor.getStatus() != null) {
                mdmsResponse = mdmsRepository.getByCriteria(contractor.getTenantId(), CommonConstants.MODULENAME_WORKS,
                        CommonConstants.WORKS_STATUS_APPCONFIG, CommonConstants.CODE, contractor.getStatus().getCode(),
                        contractorRequest.getRequestInfo());
                ObjectMapper mapper = new ObjectMapper();
                List<WorksStatus> worksStatus = new ArrayList<WorksStatus>();
                if (mdmsResponse != null && mdmsResponse.size() > 0) {
                    for (Object obj : mdmsResponse)
                        worksStatus.add(mapper.convertValue(obj, WorksStatus.class));
                    if (!worksStatus.isEmpty()) {
                        Boolean isValidStatus = Boolean.FALSE;
                        for (WorksStatus ws : worksStatus) {
                            if (ws.getModuleType() != null
                                    && ws.getModuleType()
                                            .equalsIgnoreCase(CommonConstants.MDMS_CONTRACTORMASTER_MODULETYPE)
                                    && ws.getCode() != null && ws.getCode().equalsIgnoreCase(ws.getCode())) {
                                isValidStatus = Boolean.TRUE;
                                break;
                            }
                        }
                        if (!isValidStatus) {
                            messages.put(Constants.KEY_CONTRACTOR_STATUS_INVALID,
                                    Constants.MESSAGE_CONTRACTOR_STATUS_INVALID + contractor.getStatus());
                            isDataValid = Boolean.TRUE;
                        }
                    }
                } else {
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
            if (contractor.getContractorClass() != null) {
                if (contractor.getContractorClass().getPropertyClass() == null) {
                    messages.put(Constants.KEY_CONTRACTOR_CONTRACTORCLASS_CLASS_INVALID,
                            Constants.MESSAGE_CONTRACTOR_CONTRACTORCLASS_CLASS_INVALID
                                    + contractor.getContractorClass().getPropertyClass());
                    isDataValid = Boolean.TRUE;
                } else {
                    mdmsResponse = mdmsRepository.getByCriteria(contractor.getTenantId(),
                            CommonConstants.MODULENAME_WORKS, CommonConstants.MASTERNAME_CONTRACTORCLASS, "class",
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
}
