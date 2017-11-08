package org.egov.works.masters.domain.validator;

import net.minidev.json.JSONArray;
import org.egov.tracer.model.CustomException;
import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.masters.domain.service.ScheduleOfRateService;
import org.egov.works.masters.utils.Constants;
import org.egov.works.masters.web.contract.SORRate;
import org.egov.works.masters.web.contract.ScheduleOfRate;
import org.egov.works.masters.web.contract.ScheduleOfRateRequest;
import org.egov.works.masters.web.repository.MdmsRepository;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ramki on 3/11/17.
 */
@Service
public class ScheduleOfRateValidator {
    @Autowired
    private MdmsRepository mdmsRepository;

    @Autowired
    private ScheduleOfRateService scheduleOfRateService;

    public void validate(ScheduleOfRateRequest scheduleOfRateRequest) {
        JSONArray mdmsResponse = null;
        Map<String, String> messages = new HashMap<>();
        Boolean isDataValid = Boolean.FALSE;

        for (final ScheduleOfRate scheduleOfRate : scheduleOfRateRequest.getScheduleOfRates()) {
            if (scheduleOfRate.getScheduleCategory() != null && scheduleOfRate.getScheduleCategory().getCode() != null) {
                mdmsResponse = mdmsRepository.getByCriteria(scheduleOfRate.getTenantId(), CommonConstants.MODULENAME_WORKS,
                        CommonConstants.MASTERNAME_SCHEDULE_CATEGORY, "code", scheduleOfRate.getScheduleCategory().getCode(),
                        scheduleOfRateRequest.getRequestInfo());
                if (mdmsResponse == null || mdmsResponse.size() == 0) {
                    messages.put(Constants.KEY_SCHEDULERCATEGORY_CODE_INVALID, Constants.MESSAGE_SCHEDULERCATEGORY_CODE_INVALID + scheduleOfRate.getScheduleCategory().getCode());
                    isDataValid=Boolean.TRUE;
                }
            }

            if (scheduleOfRate.getScheduleCategory() != null && scheduleOfRate.getScheduleCategory().getCode() != null) {
                mdmsResponse = mdmsRepository.getByCriteria(scheduleOfRate.getTenantId(), CommonConstants.MODULENAME_COMMON,
                        CommonConstants.MASTERNAME_UOM, "code", scheduleOfRate.getUom().getCode(),
                        scheduleOfRateRequest.getRequestInfo());
                if (mdmsResponse == null || mdmsResponse.size() == 0) {
                    messages.put(Constants.KEY_UOM_CODE_INVALID, Constants.MESSAGE_UOM_CODE_INVALID + scheduleOfRate.getUom().getCode());
                    isDataValid = Boolean.TRUE;
                }
            }

            if (scheduleOfRate.getSorRates() != null && (!scheduleOfRate.getSorRates().isEmpty() && scheduleOfRate.getSorRates().size()>1)){
                Boolean isDateOverlaped = Boolean.FALSE;
                List<SORRate> sorRates;
                Map<SORRate, Interval> sorIntervals = new HashMap<>();
                for (final SORRate sorRate : scheduleOfRate.getSorRates()) {
                    sorIntervals.put(sorRate, new Interval(new DateTime(sorRate.getFromDate()), new DateTime(sorRate.getToDate())));
                }
                if(sorIntervals.size()==1) {
                    messages.put(Constants.KEY_SOR_DATES_SHOULDNOT_OVERLAP, Constants.MESSAGE_SOR_DATES_SHOULDNOT_OVERLAP);
                    isDateOverlaped=Boolean.TRUE;
                }
                else {
                    sorRates = new ArrayList(sorIntervals.keySet());
                    for (int outerIndex = 0; outerIndex < sorRates.size(); outerIndex++) {
                        for (int innerIndex = outerIndex + 1; innerIndex < sorRates.size(); innerIndex++) {
                            if (sorIntervals.get(sorRates.get(outerIndex)).overlaps(sorIntervals.get(sorRates.get(innerIndex)))) {
                                isDateOverlaped = Boolean.TRUE;
                                break;
                            }
                            if(isDateOverlaped) break;
                        }
                    }
                    if (isDateOverlaped) {
                        messages.put(Constants.KEY_SOR_DATES_SHOULDNOT_OVERLAP, Constants.MESSAGE_SOR_DATES_SHOULDNOT_OVERLAP);
                        isDataValid = Boolean.TRUE;
                    }
                }
            }
        }
        if(isDataValid) throw new CustomException(messages);
    }

    public void validateForExistance(ScheduleOfRateRequest scheduleOfRateRequest) {
        Map<String, String> messages = new HashMap<>();
        Boolean isDataValid = Boolean.FALSE;
        for (final ScheduleOfRate scheduleOfRate : scheduleOfRateRequest.getScheduleOfRates()) {
            if (scheduleOfRate.getScheduleCategory() != null && scheduleOfRate.getScheduleCategory().getCode() != null && scheduleOfRate.getScheduleCategory().getCode() != null) {
                if (scheduleOfRateService.getByCodeCategory(scheduleOfRate.getCode(), scheduleOfRate.getScheduleCategory().getCode(), scheduleOfRate.getTenantId()) != null) {
                    messages.put(Constants.KEY_SOR_CODE_EXISTS, Constants.MESSAGE_SOR_CODE_EXISTS + scheduleOfRate.getCode());
                    isDataValid = Boolean.TRUE;
                }
            }
        }
        if(isDataValid) throw new CustomException(messages);
    }
}
