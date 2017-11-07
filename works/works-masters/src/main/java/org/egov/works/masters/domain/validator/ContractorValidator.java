package org.egov.works.masters.domain.validator;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
    
    public void validateContractor(ContractorRequest contractorRequest){
        JSONArray mdmsResponse = null;
        Map<String, String> messages = new HashMap<>();
        Boolean isDataValid = Boolean.FALSE;
        
       for(final Contractor contractor : contractorRequest.getContractors()){
           if(contractor.getContractorClass() !=null && contractor.getContractorClass().getPropertyClass()!=null){
               if (StringUtils.isBlank(contractor.getName())){
                   messages.put(Constants.KEY_CONTRACTOR_NAME_INVALID, Constants.MESSAGE_CONTRACTOR_NAME_INVALID);
                   isDataValid=Boolean.TRUE;
               }
               if (StringUtils.isBlank(contractor.getCode())){
                   messages.put(Constants.KEY_CONTRACTOR_CODE_INVALID, Constants.MESSAGE_CONTRACTOR_CODE_INVALID);
                   isDataValid=Boolean.TRUE;
               }
               if (StringUtils.isBlank(contractor.getCorrespondenceAddress())){
                   messages.put(Constants.KEY_CONTRACTOR_CORRESPONDENCEADDRESS_INVALID, Constants.MESSAGE_CONTRACTOR_CORRESPONDENCEADDRESS_INVALID);
                   isDataValid=Boolean.TRUE;
               }
               if (StringUtils.isBlank(contractor.getPaymentAddress())){
                   messages.put(Constants.KEY_CONTRACTOR_PAYMENTADDRESS_INVALID, Constants.MESSAGE_CONTRACTOR_PAYMENTADDRESS_INVALID);
                   isDataValid=Boolean.TRUE;
               }
               if (StringUtils.isBlank(contractor.getEmail())){
                   messages.put(Constants.KEY_CONTRACTOR_EMAIL_INVALID, Constants.MESSAGE_CONTRACTOR_EMAIL_INVALID);
                   isDataValid=Boolean.TRUE;
               }
               if (contractor.getMobileNumber()==null){
                   messages.put(Constants.KEY_CONTRACTOR_MOBILENUMBER_INVALID, Constants.MESSAGE_CONTRACTOR_MOBILENUMBER_INVALID);
                   isDataValid=Boolean.TRUE;
               }
               if (StringUtils.isBlank(contractor.getPanNumber())){
                   messages.put(Constants.KEY_CONTRACTOR_PANNUMBER_INVALID, Constants.MESSAGE_CONTRACTOR_PANNUMBER_INVALID);
                   isDataValid=Boolean.TRUE;
               }
               if (StringUtils.isBlank(contractor.getTinNumber())){
                   messages.put(Constants.KEY_CONTRACTOR_TINNUMBER_INVALID, Constants.MESSAGE_CONTRACTOR_TINNUMBER_INVALID);
                   isDataValid=Boolean.TRUE;
               }
               if (contractor.getBankAccountNumber()==null){
                   messages.put(Constants.KEY_CONTRACTOR_BANKACCOUNTNUMBER_INVALID, Constants.MESSAGE_CONTRACTOR_BANKACCOUNTNUMBER_INVALID);
                   isDataValid=Boolean.TRUE;
               }
               if (StringUtils.isBlank(contractor.getPwdApprovalCode())){
                   messages.put(Constants.KEY_CONTRACTOR_PWDAPPROVALCODE_INVALID, Constants.MESSAGE_CONTRACTOR_PWDAPPROVALCODE_INVALID);
                   isDataValid=Boolean.TRUE;
               }
               if (contractor.getPwdApprovalValidTill()==null){
                   messages.put(Constants.KEY_CONTRACTOR_PWDAPPROVALVALIDTILL_INVALID, Constants.MESSAGE_CONTRACTOR_PWDAPPROVALVALIDTILL_INVALID);
                   isDataValid=Boolean.TRUE;
               }
               if (StringUtils.isBlank(contractor.getEpfRegistrationNumber())){
                   messages.put(Constants.KEY_CONTRACTOR_REGISTRATIONNUMBER_INVALID, Constants.MESSAGE_CONTRACTOR_REGISTRATIONNUMBER_INVALID);
                   isDataValid=Boolean.TRUE;
               }
               if (contractor.getAccountCode()==null){
                   messages.put(Constants.KEY_CONTRACTOR_ACCOUNTCODE_INVALID, Constants.MESSAGE_CONTRACTOR_ACCOUNTCODE_INVALID);
                   isDataValid=Boolean.TRUE;
               }
               if (contractor.getIfscCode()==null){
                   messages.put(Constants.KEY_CONTRACTOR_IFSCCODE_INVALID, Constants.MESSAGE_CONTRACTOR_IFSCCODE_INVALID);
                   isDataValid=Boolean.TRUE;
               }
               
               mdmsResponse = mdmsRepository.getByCriteria(contractor.getTenantId(), CommonConstants.MODULENAME_WORKS,
                       CommonConstants.MASTERNAME_CONTRACTORCLASS, "class", contractor.getContractorClass().getPropertyClass(),
                       contractorRequest.getRequestInfo());
               if (mdmsResponse == null || mdmsResponse.size() == 0) {
                   messages.put(Constants.KEY_CONTRACTOR_CONTRACTORCLASS_CLASS_INVALID, Constants.MESSAGE_CONTRACTOR_CONTRACTORCLASS_CLASS_INVALID + contractor.getContractorClass().getPropertyClass());
                   isDataValid=Boolean.TRUE;
               }
           }
           
           // TODO : Need to add MDMS data. Waiting for clarification.
          /* if(contractor.getBank() !=null && contractor.getBank().getCode()!=null){
               mdmsResponse = mdmsRepository.getByCriteria(contractor.getTenantId(), CommonConstants.MODULENAME_WORKS,
                       CommonConstants.MASTERNAME_BANK, "code", contractor.getBank().getCode(),
                       contractorRequest.getRequestInfo());
               if (mdmsResponse == null || mdmsResponse.size() == 0) {
                   messages.put(Constants.KEY_CONTRACTOR_BANK_CODE_INVALID, Constants.MESSAGE_CONTRACTOR_BANK_CODE_INVALID + contractor.getBank().getCode());
                   isDataValid=Boolean.TRUE;
               }
           }*/
           
       }
       if(isDataValid) throw new CustomException(messages);
    }
}
