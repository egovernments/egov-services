package org.egov.works.masters.domain.validator;

import org.egov.tracer.model.CustomException;
import org.egov.works.masters.web.contract.SORRate;
import org.egov.works.masters.web.contract.ScheduleOfRate;
import org.egov.works.masters.web.contract.ScheduleOfRateRequest;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ramki on 3/11/17.
 */
@Service
public class ScheduleOfRateValidator {
    public void validateSorRates(ScheduleOfRateRequest scheduleOfRateRequest) {
        for (final ScheduleOfRate scheduleOfRate : scheduleOfRateRequest.getScheduleOfRates()) {
            if (scheduleOfRate.getSorRates() == null || scheduleOfRate.getSorRates().isEmpty()){
                Boolean isDateOverlaped=Boolean.FALSE;
                Map<SORRate, Interval> sorIntervals = new HashMap<>();
                for (final SORRate sorRate : scheduleOfRate.getSorRates()) {
                    sorIntervals.put(sorRate, new Interval(new DateTime(sorRate.getFromDate()), new DateTime(sorRate.getToDate())));
                }
                for (Map.Entry<SORRate, Interval> sorOuter : sorIntervals.entrySet()) {
                    for (Map.Entry<SORRate, Interval> sorInnter : sorIntervals.entrySet()) {
                        if(sorOuter.getValue().overlaps(sorInnter.getValue())){
                            isDateOverlaped=Boolean.TRUE;
                            break;
                        }
                    }
                }
                if(isDateOverlaped) throw new CustomException("SOR.dates.shouldnotoverlap", "Date should not be overlaped");
            }
        }
    }
}
