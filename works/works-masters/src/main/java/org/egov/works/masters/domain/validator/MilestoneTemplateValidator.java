package org.egov.works.masters.domain.validator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minidev.json.JSONArray;

import org.egov.tracer.model.CustomException;
import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.masters.domain.service.MilestoneTemplateService;
import org.egov.works.masters.utils.Constants;
import org.egov.works.masters.web.contract.MilestoneTemplate;
import org.egov.works.masters.web.contract.MilestoneTemplateActivities;
import org.egov.works.masters.web.contract.MilestoneTemplateRequest;
import org.egov.works.masters.web.repository.MdmsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ramki on 15/12/17.
 */
@Service
public class MilestoneTemplateValidator {
    @Autowired
    private MdmsRepository mdmsRepository;

    @Autowired
    private MilestoneTemplateService milestoneTemplateService;

    public void validate(MilestoneTemplateRequest milestoneTemplateRequest) {
        JSONArray mdmsResponse = null;
        Map<String, String> validationMessages = new HashMap<>();
        Boolean isDataValid = Boolean.FALSE;
        List<String> codeList = new ArrayList<>();
        BigDecimal totalPercentage = BigDecimal.ZERO;
        BigDecimal hundred = new BigDecimal(100);
        
        for (final MilestoneTemplate milestoneTemplate : milestoneTemplateRequest.getMilestoneTemplates()) {

            if (milestoneTemplate.getTypeOfWork() != null && milestoneTemplate.getTypeOfWork().getCode()!=null && !milestoneTemplate.getTypeOfWork().getCode().isEmpty()) {
                mdmsResponse = mdmsRepository.getByCriteria(milestoneTemplate.getTenantId(), CommonConstants.MODULENAME_WORKS,
                        CommonConstants.MASTERNAME_TYPEOFWORK, "code", milestoneTemplate.getTypeOfWork().getCode(),
                        milestoneTemplateRequest.getRequestInfo());
                if (mdmsResponse == null || mdmsResponse.size() == 0) {
                    validationMessages.put(Constants.KEY_TYPEOFWORK_CODE_INVALID, Constants.MESSAGE_TYPEOFWORK_CODE_INVALID + milestoneTemplate.getTypeOfWork());
                    isDataValid = Boolean.TRUE;
                }
            }

            if (milestoneTemplate.getSubTypeOfWork() != null && milestoneTemplate.getSubTypeOfWork().getCode()!=null && !milestoneTemplate.getSubTypeOfWork().getCode().isEmpty()) {
                mdmsResponse = mdmsRepository.getByCriteria(milestoneTemplate.getTenantId(), CommonConstants.MODULENAME_WORKS,
                        CommonConstants.MASTERNAME_TYPEOFWORK, "code", milestoneTemplate.getSubTypeOfWork().getCode(),
                        milestoneTemplateRequest.getRequestInfo());
                if (mdmsResponse == null || mdmsResponse.size() == 0) {
                    validationMessages.put(Constants.KEY_SUBTYPEOFWORK_CODE_INVALID, Constants.MESSAGE_SUBTYPEOFWORK_CODE_INVALID + milestoneTemplate.getSubTypeOfWork());
                    isDataValid = Boolean.TRUE;
                }
            }

            if (milestoneTemplate.getMilestoneTemplateActivities() == null && milestoneTemplate.getMilestoneTemplateActivities().size() == 0) {
                validationMessages.put(Constants.KEY_MILESTONETEMPLATE_MIN_ONE_ETA_REQUIRED, Constants.MESSAGE_MILESTONETEMPLATE_MIN_ONE_ETA_REQUIRED);
                isDataValid = Boolean.TRUE;
            } else {
            	for(MilestoneTemplateActivities milestoneTemplateActivity : milestoneTemplate.getMilestoneTemplateActivities()){
            		totalPercentage = totalPercentage.add(BigDecimal.valueOf(milestoneTemplateActivity.getPercentage()));
            	}
                if(totalPercentage.compareTo(hundred)!=0){
                    validationMessages.put(Constants.KEY_MILESTONETEMPLATE_TOTAL_PERCENTAGE_SHOULDBE_100, Constants.MESSAGE_MILESTONETEMPLATE_TOTAL_PERCENTAGE_SHOULDBE_100);
                    isDataValid = Boolean.TRUE;
                }
            }

            codeList.add(milestoneTemplate.getCode());
        }
        Set<String> filteredCode = new HashSet<String>(codeList);
        if (codeList.size() != filteredCode.size()) {
            validationMessages.put(Constants.KEY_MILESTONETEMPLATE_THEREARE_DUPLICATE_CODES, Constants.MESSAGE_MILESTONETEMPLATE_THEREARE_DUPLICATE_CODES);
            isDataValid = Boolean.TRUE;
        }
        if (isDataValid) throw new CustomException(validationMessages);
    }

    public void validateForExistance(MilestoneTemplateRequest milestoneTemplateRequest) {
        Map<String, String> validationMessages = new HashMap<>();
        Boolean isDataValid = Boolean.FALSE;

        for (final MilestoneTemplate milestoneTemplate : milestoneTemplateRequest.getMilestoneTemplates()) {
            if (milestoneTemplateService.getByCode(milestoneTemplate.getCode(), milestoneTemplate.getTenantId(), null, Boolean.FALSE) != null) {
                validationMessages.put(Constants.KEY_MILESTONETEMPLATE_CODE_EXISTS, Constants.MESSAGE_MILESTONETEMPLATE_CODE_EXISTS + milestoneTemplate.getCode());
                isDataValid = Boolean.TRUE;
            }
        }
        if (isDataValid) throw new CustomException(validationMessages);
    }

    public void validateForUpdate(MilestoneTemplateRequest milestoneTemplateRequest) {
        Map<String, String> messages = new HashMap<>();
        Boolean isDataValid = Boolean.FALSE;
        for (final MilestoneTemplate milestoneTemplate : milestoneTemplateRequest.getMilestoneTemplates()) {
            if (milestoneTemplateService.getByCode(milestoneTemplate.getCode(), milestoneTemplate.getTenantId(), milestoneTemplate.getId(), Boolean.TRUE) != null) {
                messages.put(Constants.KEY_MILESTONETEMPLATE_CODE_EXISTS, Constants.MESSAGE_MILESTONETEMPLATE_CODE_EXISTS + milestoneTemplate.getCode());
                isDataValid = Boolean.TRUE;
            }
        }
        if (isDataValid) throw new CustomException(messages);
    }
}
