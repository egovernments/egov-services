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

import java.util.*;

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
        List<String> codeList = new ArrayList<>();

        for (final EstimateTemplate estimateTemplate : estimateTemplateRequest.getEstimateTemplates()) {

            if (estimateTemplate.getTypeOfWork() != null && estimateTemplate.getTypeOfWork().getCode()!=null && !estimateTemplate.getTypeOfWork().getCode().isEmpty()) {
                mdmsResponse = mdmsRepository.getByCriteria(estimateTemplate.getTenantId(), CommonConstants.MODULENAME_WORKS,
                        CommonConstants.MASTERNAME_TYPEOFWORK, "code", estimateTemplate.getTypeOfWork().getCode(),
                        estimateTemplateRequest.getRequestInfo());
                if (mdmsResponse == null || mdmsResponse.size() == 0) {
                    validationMessages.put(Constants.KEY_TYPEOFWORK_CODE_INVALID, Constants.MESSAGE_TYPEOFWORK_CODE_INVALID + estimateTemplate.getTypeOfWork());
                    isDataValid = Boolean.TRUE;
                }
            }

            if (estimateTemplate.getSubTypeOfWork() != null && estimateTemplate.getSubTypeOfWork().getCode()!=null && !estimateTemplate.getSubTypeOfWork().getCode().isEmpty()) {
                mdmsResponse = mdmsRepository.getByCriteria(estimateTemplate.getTenantId(), CommonConstants.MODULENAME_WORKS,
                        CommonConstants.MASTERNAME_TYPEOFWORK, "code", estimateTemplate.getSubTypeOfWork().getCode(),
                        estimateTemplateRequest.getRequestInfo());
                if (mdmsResponse == null || mdmsResponse.size() == 0) {
                    validationMessages.put(Constants.KEY_SUBTYPEOFWORK_CODE_INVALID, Constants.MESSAGE_SUBTYPEOFWORK_CODE_INVALID + estimateTemplate.getSubTypeOfWork());
                    isDataValid = Boolean.TRUE;
                }
            }

            if (estimateTemplate.getEstimateTemplateActivities() != null && estimateTemplate.getEstimateTemplateActivities().size() > 0) {
                Set<String> distinctSors = new HashSet<String>(codeList);
                int sorCount = 0;
                for (EstimateTemplateActivities estimateTemplateActivities : estimateTemplate.getEstimateTemplateActivities()) {
                    if (estimateTemplateActivities.getScheduleOfRate() != null && estimateTemplateActivities.getNonSOR() != null) {
                        validationMessages.put(Constants.KEY_ESTIMATETEMPLATE_BOTH_SORANDNONSOR_SHOULDNOT_PRESENT, Constants.MESSAGE_ESTIMATETEMPLATE_BOTH_SORANDNONSOR_SHOULDNOT_PRESENT);
                        isDataValid = Boolean.TRUE;
                    } else if (estimateTemplateActivities.getScheduleOfRate() == null && estimateTemplateActivities.getNonSOR() == null) {
                        validationMessages.put(Constants.KEY_ESTIMATETEMPLATE_EITHER_SOR_OR_NONSOR_ISREQUIRED, Constants.MESSAGE_ESTIMATETEMPLATE_EITHER_SOR_OR_NONSOR_ISREQUIRED);
                        isDataValid = Boolean.TRUE;
                    } else {
                        if (estimateTemplateActivities.getUom() != null && estimateTemplateActivities.getUom().getCode() != null) {
                            mdmsResponse = mdmsRepository.getByCriteria(estimateTemplate.getTenantId(), CommonConstants.MODULENAME_COMMON,
                                    CommonConstants.MASTERNAME_UOM, "code", estimateTemplateActivities.getUom().getCode(),
                                    estimateTemplateRequest.getRequestInfo());
                            if (mdmsResponse == null || mdmsResponse.size() == 0) {
                                validationMessages.put(Constants.KEY_UOM_CODE_INVALID, Constants.MESSAGE_UOM_CODE_INVALID + estimateTemplateActivities.getUom());
                                isDataValid = Boolean.TRUE;
                            }
                        } else {
                            validationMessages.put(Constants.KEY_ESTIMATETEMPLATE_UOM_REQUIRED, Constants.MESSAGE_ESTIMATETEMPLATE_UOM_REQUIRED);
                            isDataValid = Boolean.TRUE;
                        }

                        if (estimateTemplateActivities.getScheduleOfRate() != null && estimateTemplateActivities.getScheduleOfRate().getId() != null) {
                            sorCount++;
                            distinctSors.add(estimateTemplateActivities.getScheduleOfRate().getId());
                            if (scheduleOfRateService.getById(estimateTemplateActivities.getScheduleOfRate().getId(), estimateTemplateActivities.getTenantId()) == null) {
                                validationMessages.put(Constants.KEY_SCHEDULEOFRATE_ID_INVALID, Constants.MESSAGE_SCHEDULEOFRATE_ID_INVALID + estimateTemplateActivities.getScheduleOfRate());
                                isDataValid = Boolean.TRUE;
                            }
                        }

                        if (estimateTemplateActivities.getNonSOR() != null) {
                            if (estimateTemplateActivities.getNonSOR().getUom().getCode() != null && !estimateTemplateActivities.getNonSOR().getUom().getCode().isEmpty()) {
                                mdmsResponse = mdmsRepository.getByCriteria(estimateTemplate.getTenantId(), CommonConstants.MODULENAME_COMMON,
                                        CommonConstants.MASTERNAME_UOM, "code", estimateTemplateActivities.getNonSOR().getUom().getCode(),
                                        estimateTemplateRequest.getRequestInfo());
                                if (mdmsResponse == null || mdmsResponse.size() == 0) {
                                    validationMessages.put(Constants.KEY_UOM_CODE_INVALID, Constants.MESSAGE_UOM_CODE_INVALID + estimateTemplateActivities.getNonSOR().getUom());
                                    isDataValid = Boolean.TRUE;
                                }
                            } else {
                                validationMessages.put(Constants.KEY_ESTIMATETEMPLATE_UOM_REQUIRED, Constants.MESSAGE_ESTIMATETEMPLATE_UOM_REQUIRED);
                                isDataValid = Boolean.TRUE;
                            }
                            if (!(estimateTemplateActivities.getNonSOR().getDescription() != null && !estimateTemplateActivities.getNonSOR().getDescription().isEmpty())) {
                                validationMessages.put(Constants.KEY_ESTIMATETEMPLATE_NONSOR_DESCRIPTION_REQUIRED, Constants.MESSAGE_ESTIMATETEMPLATE_NONSOR_DESCRIPTION_REQUIRED);
                                isDataValid = Boolean.TRUE;
                            }
                        }
                    }
                }
                if (distinctSors.size() != sorCount) {
                    validationMessages.put(Constants.KEY_ESTIMATETEMPLATE_DUPLICATE_SOR_NOTALLOWED, Constants.MESSAGE_ESTIMATETEMPLATE_DUPLICATE_SOR_NOTALLOWED);
                    isDataValid = Boolean.TRUE;
                }
            } else {
                validationMessages.put(Constants.KEY_ESTIMATETEMPLATE_MIN_ONE_ETA_REQUIRED, Constants.MESSAGE_ESTIMATETEMPLATE_MIN_ONE_ETA_REQUIRED);
                isDataValid = Boolean.TRUE;
            }
            codeList.add(estimateTemplate.getCode());
        }
        Set<String> filteredCode = new HashSet<String>(codeList);
        if (codeList.size() != filteredCode.size()) {
            validationMessages.put(Constants.KEY_ESTIMATETEMPLATE_THEREARE_DUPLICATE_CODES, Constants.MESSAGE_ESTIMATETEMPLATE_THEREARE_DUPLICATE_CODES);
            isDataValid = Boolean.TRUE;
        }
        if (isDataValid) throw new CustomException(validationMessages);
    }

    public void validateForExistance(EstimateTemplateRequest estimateTemplateRequest) {
        Map<String, String> validationMessages = new HashMap<>();
        Boolean isDataValid = Boolean.FALSE;

        for (final EstimateTemplate estimateTemplate : estimateTemplateRequest.getEstimateTemplates()) {
            if (estimateTemplateService.getByCode(estimateTemplate.getCode(), estimateTemplate.getTenantId(), null, Boolean.FALSE) != null) {
                validationMessages.put(Constants.KEY_ESTIMATETEMPLATE_CODE_EXISTS, Constants.MESSAGE_ESTIMATETEMPLATE_CODE_EXISTS + estimateTemplate.getCode());
                isDataValid = Boolean.TRUE;
            }
        }
        if (isDataValid) throw new CustomException(validationMessages);
    }

    public void validateForUpdate(EstimateTemplateRequest estimateTemplateRequest) {
        Map<String, String> messages = new HashMap<>();
        Boolean isDataValid = Boolean.FALSE;
        for (final EstimateTemplate estimateTemplate : estimateTemplateRequest.getEstimateTemplates()) {
            if (estimateTemplateService.getByCode(estimateTemplate.getCode(), estimateTemplate.getTenantId(), estimateTemplate.getId(), Boolean.TRUE) != null) {
                messages.put(Constants.KEY_ESTIMATETEMPLATE_CODE_EXISTS, Constants.MESSAGE_ESTIMATETEMPLATE_CODE_EXISTS + estimateTemplate.getCode());
                isDataValid = Boolean.TRUE;
            }
        }
        if (isDataValid) throw new CustomException(messages);
    }
}
