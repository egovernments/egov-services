package org.egov.works.masters.domain.validator;

import net.minidev.json.JSONArray;
import org.egov.tracer.model.CustomException;
import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.masters.domain.service.ScheduleOfRateService;
import org.egov.works.masters.utils.Constants;
import org.egov.works.masters.web.contract.*;
import org.egov.works.masters.web.repository.DetailedEstimateRepository;
import org.egov.works.masters.web.repository.MdmsRepository;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by ramki on 3/11/17.
 */
@Service
public class ScheduleOfRateValidator {
    @Autowired
    private MdmsRepository mdmsRepository;

    @Autowired
    private ScheduleOfRateService scheduleOfRateService;

    @Autowired
    private DetailedEstimateRepository detailedEstimateRepository;

    public void validate(ScheduleOfRateRequest scheduleOfRateRequest) {
        JSONArray mdmsResponse = null;
        Map<String, String> messages = new HashMap<>();
        Boolean isDataValid = Boolean.FALSE;
        List<String> codeCategoryList = new ArrayList<>();

        for (final ScheduleOfRate scheduleOfRate : scheduleOfRateRequest.getScheduleOfRates()) {
            if (scheduleOfRate.getScheduleCategory() != null && scheduleOfRate.getScheduleCategory().getCode() != null && !scheduleOfRate.getScheduleCategory().getCode().isEmpty()) {
                mdmsResponse = mdmsRepository.getByCriteria(scheduleOfRate.getTenantId(), CommonConstants.MODULENAME_WORKS,
                        CommonConstants.MASTERNAME_SCHEDULE_CATEGORY, "code", scheduleOfRate.getScheduleCategory().getCode(),
                        scheduleOfRateRequest.getRequestInfo());
                if (mdmsResponse == null || mdmsResponse.size() == 0) {
                    messages.put(Constants.KEY_SCHEDULERCATEGORY_CODE_INVALID, Constants.MESSAGE_SCHEDULERCATEGORY_CODE_INVALID + scheduleOfRate.getScheduleCategory());
                    isDataValid = Boolean.TRUE;
                }
            } else {
                messages.put(Constants.KEY_SCHEDULERCATEGORY_CODE_MANDATORY, Constants.MESSAGE_SCHEDULERCATEGORY_CODE_MANDATORY);
                isDataValid = Boolean.TRUE;
            }

            if (scheduleOfRate.getUom() != null && scheduleOfRate.getUom().getCode() != null && !scheduleOfRate.getUom().getCode().isEmpty()) {
                mdmsResponse = mdmsRepository.getByCriteria(scheduleOfRate.getTenantId(), CommonConstants.MODULENAME_COMMON,
                        CommonConstants.MASTERNAME_UOM, "code", scheduleOfRate.getUom().getCode(),
                        scheduleOfRateRequest.getRequestInfo());
                if (mdmsResponse == null || mdmsResponse.size() == 0) {
                    messages.put(Constants.KEY_UOM_CODE_INVALID, Constants.MESSAGE_UOM_CODE_INVALID + scheduleOfRate.getUom());
                    isDataValid = Boolean.TRUE;
                }
            } else {
                messages.put(Constants.KEY_UOM_CODE_MANDATORY, Constants.MESSAGE_UOM_CODE_MANDATORY);
                isDataValid = Boolean.TRUE;
            }

            if ((scheduleOfRate.getSorRates() != null && !scheduleOfRate.getSorRates().isEmpty() && scheduleOfRate.getSorRates().size() > 0) && (scheduleOfRate.getMarketRates() != null && !scheduleOfRate.getMarketRates().isEmpty() && scheduleOfRate.getMarketRates().size() > 0)) {
                messages.put(Constants.KEY_SOR_BOTH_RATES_SHOULDNOT_PRESENT, Constants.MESSAGE_SOR_BOTH_RATES_SHOULDNOT_PRESENT);
                isDataValid = Boolean.TRUE;
            } else if ((scheduleOfRate.getSorRates() != null && !scheduleOfRate.getSorRates().isEmpty())
                    || (scheduleOfRate.getMarketRates() != null && !scheduleOfRate.getMarketRates().isEmpty())) {
                if (scheduleOfRate.getSorRates() != null && !scheduleOfRate.getSorRates().isEmpty() && scheduleOfRate.getSorRates().size() > 0) {
                    Boolean isDateOverlaped = Boolean.FALSE;
                    List<SORRate> sorRates;
                    Map<SORRate, Interval> sorIntervals = new HashMap<>();
                    int openEndedDateCount = 0;
                    for (final SORRate sorRate : scheduleOfRate.getSorRates()) {
                        if (sorRate.getToDate() == null) {
                            openEndedDateCount++;
                            sorRate.setToDate(System.currentTimeMillis());
                        }
                        if (openEndedDateCount > 1) {
                            messages.put(Constants.KEY_SOR_SHOULDNOT_MULTIPLE_OPEN_ENDEDDATES, Constants.MESSAGE_SOR_SHOULDNOT_MULTIPLE_OPEN_ENDEDDATES);
                            isDataValid = Boolean.TRUE;
                            break;
                        }
                        sorIntervals.put(sorRate, new Interval(new DateTime(sorRate.getFromDate()), new DateTime(sorRate.getToDate())));
                        if (sorRate.getRate().compareTo(BigDecimal.ZERO) <= 0) {
                            messages.put(Constants.KEY_SOR_RATE_SHOULDBE_GREATERTHANZERO, Constants.MESSAGE_SOR_RATE_SHOULDBE_GREATERTHANZERO + sorRate.getRate());
                            isDataValid = Boolean.TRUE;
                        }
                    }
                    sorRates = new ArrayList(sorIntervals.keySet());
                    for (int outerIndex = 0; outerIndex < sorRates.size(); outerIndex++) {
                        for (int innerIndex = outerIndex + 1; innerIndex < sorRates.size(); innerIndex++) {
                            if (sorIntervals.get(sorRates.get(outerIndex)).overlaps(sorIntervals.get(sorRates.get(innerIndex)))) {
                                isDateOverlaped = Boolean.TRUE;
                                break;
                            }
                            if (isDateOverlaped) break;
                        }
                    }
                    if (isDateOverlaped) {
                        messages.put(Constants.KEY_SOR_DATES_SHOULDNOT_OVERLAP, Constants.MESSAGE_SOR_DATES_SHOULDNOT_OVERLAP);
                        isDataValid = Boolean.TRUE;
                    }
                }
                if (scheduleOfRate.getMarketRates() != null && !scheduleOfRate.getMarketRates().isEmpty() && scheduleOfRate.getMarketRates().size() > 0) {
                    Boolean isDateOverlaped = Boolean.FALSE;
                    List<MarketRate> marketRates;
                    Map<MarketRate, Interval> sorIntervals = new HashMap<>();
                    int openEndedDateCount = 0;
                    for (final MarketRate marketRate : scheduleOfRate.getMarketRates()) {
                        if (marketRate.getToDate() == null) {
                            openEndedDateCount++;
                            marketRate.setToDate(System.currentTimeMillis());
                        }
                        if (openEndedDateCount > 1) {
                            messages.put(Constants.KEY_SOR_SHOULDNOT_MULTIPLE_OPEN_ENDEDDATES, Constants.MESSAGE_SOR_SHOULDNOT_MULTIPLE_OPEN_ENDEDDATES);
                            isDataValid = Boolean.TRUE;
                            break;
                        }
                        sorIntervals.put(marketRate, new Interval(new DateTime(marketRate.getFromDate()), new DateTime(marketRate.getToDate())));
                        if (marketRate.getRate().compareTo(BigDecimal.ZERO) <= 0) {
                            messages.put(Constants.KEY_SOR_RATE_SHOULDBE_GREATERTHANZERO, Constants.MESSAGE_SOR_RATE_SHOULDBE_GREATERTHANZERO + marketRate.getRate());
                            isDataValid = Boolean.TRUE;
                        }
                    }
                    marketRates = new ArrayList(sorIntervals.keySet());
                    for (int outerIndex = 0; outerIndex < marketRates.size(); outerIndex++) {
                        for (int innerIndex = outerIndex + 1; innerIndex < marketRates.size(); innerIndex++) {
                            if (sorIntervals.get(marketRates.get(outerIndex)).overlaps(sorIntervals.get(marketRates.get(innerIndex)))) {
                                isDateOverlaped = Boolean.TRUE;
                                break;
                            }
                            if (isDateOverlaped) break;
                        }
                    }
                    if (isDateOverlaped) {
                        messages.put(Constants.KEY_SOR_MARKETRATE_DATES_SHOULDNOT_OVERLAP, Constants.MESSAGE_SOR_MARKETRATE_DATES_SHOULDNOT_OVERLAP);
                        isDataValid = Boolean.TRUE;
                    }
                }
            } else {
                messages.put(Constants.KEY_SOR_EITHER_SOR_OR_MARKETRATE_ISREQUIRED, Constants.MESSAGE_SOR_EITHER_SOR_OR_MARKETRATE_ISREQUIRED);
                isDataValid = Boolean.TRUE;
            }
            codeCategoryList.add(scheduleOfRate.getCode() + "_" + scheduleOfRate.getScheduleCategory().getCode());
        }
        Set<String> filteredCodeCategory = new HashSet<String>(codeCategoryList);
        if (codeCategoryList.size() != filteredCodeCategory.size()) {
            messages.put(Constants.KEY_SOR_THEREARE_DUPLICATE_CODES, Constants.MESSAGE_SOR_THEREARE_DUPLICATE_CODES);
            isDataValid = Boolean.TRUE;
        }
        if (isDataValid) throw new CustomException(messages);
    }

    public void validateForExistance(ScheduleOfRateRequest scheduleOfRateRequest) {
        Map<String, String> messages = new HashMap<>();
        Boolean isDataValid = Boolean.FALSE;
        for (final ScheduleOfRate scheduleOfRate : scheduleOfRateRequest.getScheduleOfRates()) {
            if (scheduleOfRateService.getByCodeCategory(scheduleOfRate.getCode(), scheduleOfRate.getScheduleCategory().getCode(), scheduleOfRate.getTenantId(), null, Boolean.FALSE) != null) {
                messages.put(Constants.KEY_SOR_CODE_EXISTS, Constants.MESSAGE_SOR_CODE_EXISTS + scheduleOfRate.getCode() + ", " + scheduleOfRate.getScheduleCategory().getCode());
                isDataValid = Boolean.TRUE;
            }
        }
        if (isDataValid) throw new CustomException(messages);
    }

    public void validateForUpdate(ScheduleOfRateRequest scheduleOfRateRequest) {
        Map<String, String> messages = new HashMap<>();
        Boolean isDataValid = Boolean.FALSE;
        ScheduleOfRate dbSor = null;
        for (final ScheduleOfRate scheduleOfRate : scheduleOfRateRequest.getScheduleOfRates()) {
            dbSor = scheduleOfRateService.getById(scheduleOfRate.getId(), scheduleOfRate.getTenantId());
            if (dbSor != null) {
                if (!dbSor.getCode().equals(scheduleOfRate.getCode())) {
                    messages.put(Constants.KEY_SOR_CODE_UPDATE_NOTALLOWED, Constants.MESSAGE_SOR_CODE_UPDATE_NOTALLOWED);
                    isDataValid = Boolean.TRUE;
                }
            } else {
                messages.put(Constants.KEY_SOR_KEY_INVALID, Constants.MESSAGE_SOR_KEY_INVALID + scheduleOfRate.getId());
                isDataValid = Boolean.TRUE;
            }
            if (scheduleOfRateService.getByCodeCategory(scheduleOfRate.getCode(), scheduleOfRate.getScheduleCategory().getCode(), scheduleOfRate.getTenantId(), scheduleOfRate.getId(), Boolean.TRUE) != null) {
                messages.put(Constants.KEY_SOR_CODE_EXISTS, Constants.MESSAGE_SOR_CODE_EXISTS + scheduleOfRate.getCode() + ", " + scheduleOfRate.getScheduleCategory().getCode());
                isDataValid = Boolean.TRUE;
            }
        }
        if (isDataValid) throw new CustomException(messages);
    }

    public void validateRatesForUpdate(ScheduleOfRateRequest scheduleOfRateRequest, final String sorId, final Long sorFromDate, final Long sorToDate, final String tenantId) {
        List<DetailedEstimate> des = detailedEstimateRepository.searchDetailedEstimatesBySOR(sorId, sorFromDate, sorToDate, tenantId, scheduleOfRateRequest.getRequestInfo());
        if (des != null && des.size() > 0) {
            throw new CustomException(Constants.KEY_SOR_THEREARE_DE, Constants.MESSAGE_SOR_THEREARE_DE);
        }
    }
}
