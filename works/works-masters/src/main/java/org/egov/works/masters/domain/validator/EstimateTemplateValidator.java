package org.egov.works.masters.domain.validator;

import net.minidev.json.JSONArray;
import org.egov.tracer.model.CustomException;
import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.masters.domain.service.EstimateTemplateService;
import org.egov.works.masters.domain.service.ScheduleOfRateService;
import org.egov.works.masters.utils.Constants;
import org.egov.works.masters.web.contract.EstimateTemplate;
import org.egov.works.masters.web.contract.EstimateTemplateActivities;
import org.egov.works.masters.web.contract.EstimateTemplateRequest;
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

    @Autowired
    private EstimateTemplateService estimateTemplateService;

    public void validate(EstimateTemplateRequest estimateTemplateRequest) {
        JSONArray mdmsResponse = null;
        Map<String, String> validationMessages = new HashMap<>();
        Boolean isDataValid = Boolean.FALSE;

        for (final EstimateTemplate estimateTemplate : estimateTemplateRequest.getEstimateTemplates()) {
            if ((estimateTemplate.getTypeOfWork() != null && !estimateTemplate.getTypeOfWork().isEmpty())
                    && (estimateTemplate.getSubTypeOfWork() != null && !estimateTemplate.getSubTypeOfWork().isEmpty())) {
                validationMessages.put(Constants.KEY_TYPEOFWORK_SUBTYPEOFWORK_EITHER_ONE_MANDATORY,
                        Constants.MESSAGE_TYPEOFWORK_SUBTYPEOFWORK_EITHER_ONE_MANDATORY + estimateTemplate.getTypeOfWork()
                                + ", " + estimateTemplate.getSubTypeOfWork());
                isDataValid=Boolean.TRUE;
            }

            if (estimateTemplate.getTypeOfWork() != null && !estimateTemplate.getTypeOfWork().isEmpty()) {
                mdmsResponse = mdmsRepository.getByCriteria(estimateTemplate.getTenantId(), CommonConstants.MODULENAME_WORKS,
                        CommonConstants.MASTERNAME_TYPEOFWORK, "code", estimateTemplate.getTypeOfWork(),
                        estimateTemplateRequest.getRequestInfo());
                if (mdmsResponse == null || mdmsResponse.size() == 0) {
                    validationMessages.put(Constants.KEY_TYPEOFWORK_CODE_INVALID, Constants.MESSAGE_TYPEOFWORK_CODE_INVALID + estimateTemplate.getTypeOfWork());
                    isDataValid=Boolean.TRUE;
                }
            }

            if (estimateTemplate.getSubTypeOfWork() != null && !estimateTemplate.getSubTypeOfWork().isEmpty()) {
                mdmsResponse = mdmsRepository.getByCriteria(estimateTemplate.getTenantId(), CommonConstants.MODULENAME_WORKS,
                        CommonConstants.MASTERNAME_TYPEOFWORK, "code", estimateTemplate.getSubTypeOfWork(),
                        estimateTemplateRequest.getRequestInfo());
                if (mdmsResponse == null || mdmsResponse.size() == 0) {
                    validationMessages.put(Constants.KEY_SUBTYPEOFWORK_CODE_INVALID, Constants.MESSAGE_SUBTYPEOFWORK_CODE_INVALID + estimateTemplate.getSubTypeOfWork());
                    isDataValid=Boolean.TRUE;
                }
            }

            for(EstimateTemplateActivities estimateTemplateActivities : estimateTemplate.getEstimateTemplateActivities()) {
                if (estimateTemplateActivities.getScheduleOfRate() != null && !estimateTemplateActivities.getScheduleOfRate().isEmpty()) {
                    if (scheduleOfRateService.getById(estimateTemplateActivities.getScheduleOfRate(), estimateTemplateActivities.getTenantId()) == null) {
                        validationMessages.put(Constants.KEY_SCHEDULEOFRATE_ID_INVALID, Constants.MESSAGE_SCHEDULEOFRATE_ID_INVALID + estimateTemplateActivities.getScheduleOfRate());
                        isDataValid = Boolean.TRUE;
                    }
                }

                if (estimateTemplateActivities.getUom() != null && !estimateTemplateActivities.getUom().isEmpty()) {
                    mdmsResponse = mdmsRepository.getByCriteria(estimateTemplate.getTenantId(), CommonConstants.MODULENAME_COMMON,
                            CommonConstants.MASTERNAME_UOM, "code", estimateTemplateActivities.getUom(),
                            estimateTemplateRequest.getRequestInfo());
                    if (mdmsResponse == null || mdmsResponse.size() == 0) {
                        validationMessages.put(Constants.KEY_UOM_CODE_INVALID, Constants.MESSAGE_UOM_CODE_INVALID + estimateTemplateActivities.getUom());
                        isDataValid = Boolean.TRUE;
                    }
                }

                if (estimateTemplateActivities.getNonSOR() != null && !estimateTemplateActivities.getNonSOR().getUom().isEmpty()) {
                    mdmsResponse = mdmsRepository.getByCriteria(estimateTemplate.getTenantId(), CommonConstants.MODULENAME_COMMON,
                            CommonConstants.MASTERNAME_UOM, "code", estimateTemplateActivities.getNonSOR().getUom(),
                            estimateTemplateRequest.getRequestInfo());
                    if (mdmsResponse == null || mdmsResponse.size() == 0) {
                        validationMessages.put(Constants.KEY_UOM_CODE_INVALID, Constants.MESSAGE_UOM_CODE_INVALID + estimateTemplateActivities.getNonSOR().getUom());
                        isDataValid = Boolean.TRUE;
                    }
                }
            }
        }

        if(isDataValid) throw new CustomException(validationMessages);
    }

    public void validateForExistance(EstimateTemplateRequest estimateTemplateRequest) {
        Map<String, String> validationMessages = new HashMap<>();
        Boolean isDataValid = Boolean.FALSE;

        for (final EstimateTemplate estimateTemplate : estimateTemplateRequest.getEstimateTemplates()) {
            if (estimateTemplate.getCode() != null) {
                if (estimateTemplateService.getByCode(estimateTemplate.getCode(), estimateTemplate.getTenantId()) != null) {
                    validationMessages.put(Constants.KEY_ESTIMATETEMPLATE_CODE_EXISTS, Constants.MESSAGE_ESTIMATETEMPLATE_CODE_EXISTS + estimateTemplate.getCode());
                    isDataValid = Boolean.TRUE;
                }
            }
        }
        if(isDataValid) throw new CustomException(validationMessages);
    }
}
