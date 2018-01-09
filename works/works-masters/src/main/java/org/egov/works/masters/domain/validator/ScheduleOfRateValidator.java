package org.egov.works.masters.domain.validator;

import net.minidev.json.JSONArray;
import org.egov.tracer.model.CustomException;
import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.masters.domain.service.ScheduleOfRateService;
import org.egov.works.masters.utils.Constants;
import org.egov.works.masters.web.contract.*;
import org.egov.works.masters.web.repository.DetailedEstimateRepository;
import org.egov.works.masters.web.repository.MdmsRepository;
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
        Boolean isValidationError = Boolean.FALSE;
        List<String> codeCategoryList = new ArrayList<>();

        for (final ScheduleOfRate scheduleOfRate : scheduleOfRateRequest.getScheduleOfRates()) {
            if (scheduleOfRate.getScheduleCategory() != null && scheduleOfRate.getScheduleCategory().getCode() != null && !scheduleOfRate.getScheduleCategory().getCode().isEmpty()) {
                mdmsResponse = mdmsRepository.getByCriteria(scheduleOfRate.getTenantId(), CommonConstants.MODULENAME_WORKS,
                        CommonConstants.MASTERNAME_SCHEDULE_CATEGORY, "code", scheduleOfRate.getScheduleCategory().getCode(),
                        scheduleOfRateRequest.getRequestInfo());
                if (mdmsResponse == null || mdmsResponse.size() == 0) {
                    messages.put(Constants.KEY_SCHEDULERCATEGORY_CODE_INVALID, Constants.MESSAGE_SCHEDULERCATEGORY_CODE_INVALID + scheduleOfRate.getScheduleCategory());
                    isValidationError = Boolean.TRUE;
                }
            } else {
                messages.put(Constants.KEY_SCHEDULERCATEGORY_CODE_MANDATORY, Constants.MESSAGE_SCHEDULERCATEGORY_CODE_MANDATORY);
                isValidationError = Boolean.TRUE;
            }

            if (scheduleOfRate.getUom() != null && scheduleOfRate.getUom().getCode() != null && !scheduleOfRate.getUom().getCode().isEmpty()) {
                mdmsResponse = mdmsRepository.getByCriteria(scheduleOfRate.getTenantId(), CommonConstants.MODULENAME_COMMON,
                        CommonConstants.MASTERNAME_UOM, "code", scheduleOfRate.getUom().getCode(),
                        scheduleOfRateRequest.getRequestInfo());
                if (mdmsResponse == null || mdmsResponse.size() == 0) {
                    messages.put(Constants.KEY_UOM_CODE_INVALID, Constants.MESSAGE_UOM_CODE_INVALID + scheduleOfRate.getUom());
                    isValidationError = Boolean.TRUE;
                }
            } else {
                messages.put(Constants.KEY_UOM_CODE_MANDATORY, Constants.MESSAGE_UOM_CODE_MANDATORY);
                isValidationError = Boolean.TRUE;
            }

            if ((scheduleOfRate.getSorRates() != null && !scheduleOfRate.getSorRates().isEmpty())
                    || (scheduleOfRate.getMarketRates() != null && !scheduleOfRate.getMarketRates().isEmpty())) {
                if (scheduleOfRate.getSorRates() != null && !scheduleOfRate.getSorRates().isEmpty()
                        && scheduleOfRate.getSorRates().size() > 0) {
                    int openEndedDateCount = 0;
                    int sorCounter = 0;
                    SORRate sorRateP = null;
                    SORRate sorRateC = null;

                    //Sort SOR rates by from date
                    Collections.sort(scheduleOfRate.getSorRates(), (SORRate o1, SORRate o2) -> o1.getFromDate().compareTo(o2.getFromDate()));

                    for (final SORRate sorRate : scheduleOfRate.getSorRates()) {
                        if (sorRate.getToDate() == null) {
                            openEndedDateCount++;
                            sorRate.setToDate(System.currentTimeMillis());
                        } else if (sorRate.getFromDate().compareTo(sorRate.getToDate()) != -1) {
                            messages.put(Constants.KEY_SOR_FROMDATE_SHOULDBE_LESSTHAN_TODATE, Constants.MESSAGE_SOR_FROMDATE_SHOULDBE_LESSTHAN_TODATE);
                            isValidationError = Boolean.TRUE;
                        }
                        if (openEndedDateCount > 1) {
                            messages.put(Constants.KEY_SOR_SHOULDNOT_MULTIPLE_OPEN_ENDEDDATES, Constants.MESSAGE_SOR_SHOULDNOT_MULTIPLE_OPEN_ENDEDDATES);
                            isValidationError = Boolean.TRUE;
                            break;
                        }
                        //Date range overlapping validation
                        if (sorCounter == 0) {
                            sorRateP = sorRate;
                            sorRateC = sorRate;
                        } else {
                            sorRateP = sorRateC;
                            sorRateC = sorRate;
                            if (isDateRangeOverlapped(sorRateP.getFromDate(), sorRateP.getToDate(), sorRateC.getFromDate(), sorRateC.getToDate())) {
                                messages.put(Constants.KEY_SOR_DATES_SHOULDNOT_OVERLAP, Constants.MESSAGE_SOR_DATES_SHOULDNOT_OVERLAP);
                                isValidationError = Boolean.TRUE;
                            }
                        }

                        if (sorRate.getRate().compareTo(BigDecimal.ZERO) <= 0) {
                            messages.put(Constants.KEY_SOR_RATE_SHOULDBE_GREATERTHANZERO, Constants.MESSAGE_SOR_RATE_SHOULDBE_GREATERTHANZERO + sorRate.getRate());
                            isValidationError = Boolean.TRUE;
                        }
                        sorCounter++;
                    }
                }
                if (scheduleOfRate.getMarketRates() != null && !scheduleOfRate.getMarketRates().isEmpty() && scheduleOfRate.getMarketRates().size() > 0) {
                    int openEndedDateCount = 0;
                    int marketCounter = 0;
                    MarketRate marketRateP = null;
                    MarketRate marketRateC = null;

                    //Sort Market rates by from date
                    Collections.sort(scheduleOfRate.getMarketRates(), (MarketRate o1, MarketRate o2) -> o1.getFromDate().compareTo(o2.getFromDate()));
                    for (final MarketRate marketRate : scheduleOfRate.getMarketRates()) {
                        if (marketRate.getToDate() == null) {
                            openEndedDateCount++;
                            marketRate.setToDate(System.currentTimeMillis());
                        }
                        if (openEndedDateCount > 1) {
                            messages.put(Constants.KEY_SOR_SHOULDNOT_MULTIPLE_OPEN_ENDEDDATES, Constants.MESSAGE_SOR_SHOULDNOT_MULTIPLE_OPEN_ENDEDDATES);
                            isValidationError = Boolean.TRUE;
                            break;
                        }
                        //Date range overlapping validation
                        if (marketCounter == 0) {
                            marketRateP = marketRate;
                            marketRateC = marketRate;
                        } else {
                            marketRateP = marketRateC;
                            marketRateC = marketRate;
                            if (isDateRangeOverlapped(marketRateP.getFromDate(), marketRateP.getToDate(), marketRateC.getFromDate(), marketRateC.getToDate())) {
                                messages.put(Constants.KEY_SOR_MARKETRATE_DATES_SHOULDNOT_OVERLAP, Constants.MESSAGE_SOR_MARKETRATE_DATES_SHOULDNOT_OVERLAP);
                                isValidationError = Boolean.TRUE;
                            }
                        }
                        if (marketRate.getRate().compareTo(BigDecimal.ZERO) <= 0) {
                            messages.put(Constants.KEY_SOR_RATE_SHOULDBE_GREATERTHANZERO, Constants.MESSAGE_SOR_RATE_SHOULDBE_GREATERTHANZERO + marketRate.getRate());
                            isValidationError = Boolean.TRUE;
                        }
                        marketCounter++;
                    }
                }
            } else {
                messages.put(Constants.KEY_SOR_EITHER_SOR_OR_MARKETRATE_ISREQUIRED, Constants.MESSAGE_SOR_EITHER_SOR_OR_MARKETRATE_ISREQUIRED);
                isValidationError = Boolean.TRUE;
            }
            codeCategoryList.add(scheduleOfRate.getCode() + "_" + scheduleOfRate.getScheduleCategory().getCode());
        }
        Set<String> filteredCodeCategory = new HashSet<String>(codeCategoryList);
        //Checking duplicate codes in received list from request
        if (codeCategoryList.size() != filteredCodeCategory.size()) {
            messages.put(Constants.KEY_SOR_THEREARE_DUPLICATE_CODES, Constants.MESSAGE_SOR_THEREARE_DUPLICATE_CODES);
            isValidationError = Boolean.TRUE;
        }
        if (isValidationError) throw new CustomException(messages);
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
                if (!dbSor.getScheduleCategory().getCode().equals(scheduleOfRate.getScheduleCategory().getCode())) {
                    messages.put(Constants.KEY_SOR_SCHEDULECATEGORY_UPDATE_NOTALLOWED, Constants.MESSAGE_SOR_SCHEDULECATEGORY_UPDATE_NOTALLOWED);
                    isDataValid = Boolean.TRUE;
                }
                if (!dbSor.getUom().getCode().equals(scheduleOfRate.getUom().getCode())) {
                    messages.put(Constants.KEY_SOR_UOM_UPDATE_NOTALLOWED, Constants.MESSAGE_SOR_UOM_UPDATE_NOTALLOWED);
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

    private Boolean isDateRangeOverlapped(Long fromDate1, Long toDate1, Long fromDate2, Long toDate2) {
        if (toDate1 < fromDate2 && fromDate1 < fromDate2 && toDate1 < toDate2 && fromDate1 < toDate2)
            return false;
        else
            return true;
    }
}
