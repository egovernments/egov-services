package org.egov.works.masters.domain.validator;

import net.minidev.json.JSONArray;
import org.egov.tracer.model.CustomException;
import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.masters.domain.service.ScheduleOfRateService;
import org.egov.works.masters.utils.Constants;
import org.egov.works.masters.web.contract.EstimateTemplate;
import org.egov.works.masters.web.contract.EstimateTemplateActivities;
import org.egov.works.masters.web.contract.EstimateTemplateRequest;
import org.egov.works.masters.web.contract.ScheduleOfRate;
import org.egov.works.masters.web.repository.MdmsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ramki on 3/11/17.
 */
@Service
public class EstimateTemplateValidator {
    @Autowired
    private MdmsRepository mdmsRepository;

    @Autowired
    private ScheduleOfRateService scheduleOfRateService;

    public void validate(EstimateTemplateRequest estimateTemplateRequest) {
        JSONArray mdmsResponse = null;
        Map<String, String> validationMessages = new HashMap<>();
        Boolean isDataValid = Boolean.FALSE;

        for (final EstimateTemplate estimateTemplate : estimateTemplateRequest.getEstimateTemplates()) {
            if (estimateTemplate.getTypeOfWork() != null && estimateTemplate.getTypeOfWork().getCode() != null) {
                mdmsResponse = mdmsRepository.getByCriteria(estimateTemplate.getTenantId(), CommonConstants.MODULENAME_WORKS,
                        CommonConstants.MASTERNAME_TYPEOFWORK, "code", estimateTemplate.getTypeOfWork().getCode(),
                        estimateTemplateRequest.getRequestInfo());
                if (mdmsResponse == null || mdmsResponse.size() == 0) {
                    validationMessages.put(Constants.KEY_TYPEOFWORK_CODE_INVALID, Constants.MESSAGE_TYPEOFWORK_CODE_INVALID + estimateTemplate.getTypeOfWork().getCode());
                    isDataValid=Boolean.TRUE;
                }
            }
            if (estimateTemplate.getSubTypeOfWork() != null && estimateTemplate.getSubTypeOfWork().getCode() != null) {
                mdmsResponse = mdmsRepository.getByCriteria(estimateTemplate.getTenantId(), CommonConstants.MODULENAME_WORKS,
                        CommonConstants.MASTERNAME_TYPEOFWORK, "code", estimateTemplate.getSubTypeOfWork().getCode(),
                        estimateTemplateRequest.getRequestInfo());
                if (mdmsResponse == null || mdmsResponse.size() == 0) {
                    validationMessages.put(Constants.KEY_TYPEOFWORK_CODE_INVALID, Constants.MESSAGE_TYPEOFWORK_CODE_INVALID + estimateTemplate.getSubTypeOfWork().getCode());
                    isDataValid=Boolean.TRUE;
                }
            }
            for(EstimateTemplateActivities estimateTemplateActivities : estimateTemplate.getEstimateTemplateActivities()) {
                if (estimateTemplateActivities.getScheduleOfRate() != null && estimateTemplateActivities.getScheduleOfRate().getCode() != null) {
                    ScheduleOfRate scheduleOfRate = scheduleOfRateService.getbyId(estimateTemplateActivities.getScheduleOfRate().getCode(), estimateTemplateActivities.getTenantId());
                    mdmsResponse = mdmsRepository.getByCriteria(estimateTemplate.getTenantId(), CommonConstants.MODULENAME_WORKS,
                            CommonConstants.MASTERNAME_SCHEDULE_CATEGORY, "code", estimateTemplateActivities.getScheduleOfRate().getCode(),
                            estimateTemplateRequest.getRequestInfo());
                    if (scheduleOfRate == null) {
                        validationMessages.put(Constants.KEY_SCHEDULERCATEGORY_CODE_INVALID, Constants.MESSAGE_SCHEDULERCATEGORY_CODE_INVALID + estimateTemplateActivities.getScheduleOfRate().getCode());
                        isDataValid = Boolean.TRUE;
                    }
                }

                if (estimateTemplateActivities.getUom() != null && estimateTemplateActivities.getUom().getCode() != null) {
                    mdmsResponse = mdmsRepository.getByCriteria(estimateTemplate.getTenantId(), CommonConstants.MODULENAME_COMMON,
                            CommonConstants.MASTERNAME_UOM, "code", estimateTemplateActivities.getUom().getCode(),
                            estimateTemplateRequest.getRequestInfo());
                    if (mdmsResponse == null || mdmsResponse.size() == 0) {
                        validationMessages.put(Constants.KEY_UOM_CODE_INVALID, Constants.MESSAGE_UOM_CODE_INVALID + estimateTemplateActivities.getUom().getCode());
                        isDataValid = Boolean.TRUE;
                    }
                }

                if (estimateTemplateActivities.getNonSOR() != null && estimateTemplateActivities.getNonSOR().getUom().getCode() != null) {
                    mdmsResponse = mdmsRepository.getByCriteria(estimateTemplate.getTenantId(), CommonConstants.MODULENAME_COMMON,
                            CommonConstants.MASTERNAME_UOM, "code", estimateTemplateActivities.getNonSOR().getUom().getCode(),
                            estimateTemplateRequest.getRequestInfo());
                    if (mdmsResponse == null || mdmsResponse.size() == 0) {
                        validationMessages.put(Constants.KEY_UOM_CODE_INVALID, Constants.MESSAGE_UOM_CODE_INVALID + estimateTemplateActivities.getNonSOR().getUom().getCode());
                        isDataValid = Boolean.TRUE;
                    }
                }
            }
        }
        if(isDataValid) throw new CustomException(validationMessages);
    }
}
