package org.egov.works.masters.domain.repository;

import org.egov.works.masters.domain.repository.builder.ScheduleOfRateQueryBuilder;
import org.egov.works.masters.domain.repository.helper.MarketRateHelper;
import org.egov.works.masters.domain.repository.helper.SORRateHelper;
import org.egov.works.masters.domain.repository.helper.ScheduleOfRateHelper;
import org.egov.works.masters.web.contract.MarketRate;
import org.egov.works.masters.web.contract.SORRate;
import org.egov.works.masters.web.contract.ScheduleOfRate;
import org.egov.works.masters.web.contract.ScheduleOfRateSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ramki on 31/10/17.
 */
@Service
public class ScheduleOfRateRepository {
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private ScheduleOfRateQueryBuilder scheduleOfRateQueryBuilder;

    public List<ScheduleOfRate> getScheduleOfRateByCriteria(ScheduleOfRateSearchCriteria scheduleOfRateSearchCriteria){
        Map params = new HashMap();
        String queryStr = scheduleOfRateQueryBuilder.getSearchQuery(scheduleOfRateSearchCriteria, params);
        List<ScheduleOfRateHelper> scheduleOfRateHelpers = namedParameterJdbcTemplate.query(queryStr, params, new BeanPropertyRowMapper(ScheduleOfRateHelper.class));
        List<ScheduleOfRate> scheduleOfRates = new ArrayList<>();

        ScheduleOfRate scheduleOfRate;

        for(ScheduleOfRateHelper scheduleOfRateHelper : scheduleOfRateHelpers) {
            scheduleOfRate = scheduleOfRateHelper.toDomain();
            scheduleOfRate.setSorRates(prepareSorRates(scheduleOfRate.getId(), scheduleOfRate.getTenantId()));
            scheduleOfRate.setMarketRates(prepareMarketRates(scheduleOfRate.getId(), scheduleOfRate.getTenantId()));
            scheduleOfRates.add(scheduleOfRate);
        }
        return scheduleOfRates;
    }

    private List<SORRate> prepareSorRates(String scheduleOfRate, String tenantId){
        Map params = new HashMap();
        String queryStr = scheduleOfRateQueryBuilder.getSORRates(scheduleOfRate, tenantId, params);
        List<SORRateHelper> sorRateHelpers = namedParameterJdbcTemplate.query(queryStr, params, new BeanPropertyRowMapper(SORRateHelper.class));
        List<SORRate> sorRates = new ArrayList<>();

        SORRate sorRate;

        for(SORRateHelper sorRateHelper : sorRateHelpers) {
            sorRate = sorRateHelper.toDomain();
            sorRates.add(sorRate);
        }
        return sorRates;
    }

    private List<MarketRate> prepareMarketRates(String scheduleOfRate, String tenantId){
        Map params = new HashMap();
        String queryStr = scheduleOfRateQueryBuilder.getMarketRates(scheduleOfRate, tenantId, params);
        List<MarketRateHelper> marketRateHelpers = namedParameterJdbcTemplate.query(queryStr, params, new BeanPropertyRowMapper(MarketRateHelper.class));
        List<MarketRate> marketRates = new ArrayList<>();

        MarketRate marketRate;

        for(MarketRateHelper marketRateHelper : marketRateHelpers) {
            marketRate = marketRateHelper.toDomain();
            marketRates.add(marketRate);
        }
        return marketRates;
    }

    public ScheduleOfRate getbyCode(String code, String tenantId){
        ScheduleOfRateSearchCriteria scheduleOfRateSearchCriteria = new ScheduleOfRateSearchCriteria();
        List<String> codes = new ArrayList<>();
        codes.add(code);
        scheduleOfRateSearchCriteria.setSorCodes(codes);
        scheduleOfRateSearchCriteria.setTenantId(tenantId);
        return getScheduleOfRateByCriteria(scheduleOfRateSearchCriteria).get(0);
    }
}
