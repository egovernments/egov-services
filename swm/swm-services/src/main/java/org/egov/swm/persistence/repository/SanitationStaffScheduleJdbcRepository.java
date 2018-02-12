package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.SanitationStaffSchedule;
import org.egov.swm.domain.model.SanitationStaffScheduleSearch;
import org.egov.swm.domain.model.SanitationStaffTarget;
import org.egov.swm.domain.model.SanitationStaffTargetSearch;
import org.egov.swm.domain.model.Shift;
import org.egov.swm.domain.model.ShiftSearch;
import org.egov.swm.domain.service.ShiftService;
import org.egov.swm.persistence.entity.SanitationStaffScheduleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class SanitationStaffScheduleJdbcRepository extends JdbcRepository {

    public static final String TABLE_NAME = "egswm_sanitationstaffschedule";

    @Autowired
    public SanitationStaffTargetJdbcRepository sanitationStaffTargetJdbcRepository;

    @Autowired
    private ShiftService shiftService;

    public Pagination<SanitationStaffSchedule> search(final SanitationStaffScheduleSearch searchRequest) {

        String searchQuery = "select * from " + TABLE_NAME + " :condition  :orderby ";

        final Map<String, Object> paramValues = new HashMap<>();
        final StringBuffer params = new StringBuffer();

        if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
            validateSortByOrder(searchRequest.getSortBy());
            validateEntityFieldName(searchRequest.getSortBy(), SanitationStaffScheduleSearch.class);
        }

        String orderBy = "order by transactionNo";
        if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty())
            orderBy = "order by " + searchRequest.getSortBy();

        if (searchRequest.getTenantId() != null) {
            addAnd(params);
            params.append("tenantId =:tenantId");
            paramValues.put("tenantId", searchRequest.getTenantId());
        }

        if (searchRequest.getTransactionNo() != null) {
            addAnd(params);
            params.append("transactionNo in (:transactionNo)");
            paramValues.put("transactionNo", searchRequest.getTransactionNo());
        }

        if (searchRequest.getTransactionNos() != null) {
            addAnd(params);
            params.append("transactionNo in (:transactionNos)");
            paramValues.put("transactionNos",
                    new ArrayList<>(Arrays.asList(searchRequest.getTransactionNos().split(","))));
        }

        if (searchRequest.getTargetNo() != null) {
            addAnd(params);
            params.append("sanitationStaffTarget in (:sanitationStaffTarget)");
            paramValues.put("sanitationStaffTarget", searchRequest.getTargetNo());
        }

        if (searchRequest.getShiftCode() != null) {
            addAnd(params);
            params.append("shift in (:shift)");
            paramValues.put("shift", searchRequest.getShiftCode());
        }

        Pagination<SanitationStaffSchedule> page = new Pagination<>();
        if (searchRequest.getOffset() != null)
            page.setOffset(searchRequest.getOffset());
        if (searchRequest.getPageSize() != null)
            page.setPageSize(searchRequest.getPageSize());

        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());
        else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        page = (Pagination<SanitationStaffSchedule>) getPagination(searchQuery, page, paramValues);
        searchQuery = searchQuery + " :pagination";

        searchQuery = searchQuery.replace(":pagination",
                "limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

        final BeanPropertyRowMapper row = new BeanPropertyRowMapper(SanitationStaffScheduleEntity.class);

        final List<SanitationStaffSchedule> sanitationStaffScheduleList = new ArrayList<>();

        final List<SanitationStaffScheduleEntity> sanitationStaffScheduleEntities = namedParameterJdbcTemplate
                .query(searchQuery.toString(), paramValues, row);

        for (final SanitationStaffScheduleEntity sanitationStaffScheduleEntity : sanitationStaffScheduleEntities)
            sanitationStaffScheduleList.add(sanitationStaffScheduleEntity.toDomain());

        if (sanitationStaffScheduleList != null && !sanitationStaffScheduleList.isEmpty()) {
            populateShifts(sanitationStaffScheduleList);

            populateSanitationStaffTargets(sanitationStaffScheduleList);
        }
        page.setPagedData(sanitationStaffScheduleList);

        return page;
    }

    private void populateShifts(final List<SanitationStaffSchedule> sanitationStaffScheduleList) {

        final Map<String, Shift> shiftMap = new HashMap<>();
        String tenantId = null;
        final ShiftSearch shiftSearch = new ShiftSearch();

        if (sanitationStaffScheduleList != null && !sanitationStaffScheduleList.isEmpty())
            tenantId = sanitationStaffScheduleList.get(0).getTenantId();

        shiftSearch.setTenantId(tenantId);

        final Pagination<Shift> shifts = shiftService.search(shiftSearch);

        if (shifts != null && shifts.getPagedData() != null && !shifts.getPagedData().isEmpty())
            for (final Shift s : shifts.getPagedData())
                shiftMap.put(s.getCode(), s);

        for (final SanitationStaffSchedule sanitationStaffSchedule : sanitationStaffScheduleList)
            if (sanitationStaffSchedule.getShift() != null && sanitationStaffSchedule.getShift().getCode() != null
                    && !sanitationStaffSchedule.getShift().getCode().isEmpty())
                sanitationStaffSchedule.setShift(shiftMap.get(sanitationStaffSchedule.getShift().getCode()));
    }

    private void populateSanitationStaffTargets(final List<SanitationStaffSchedule> sanitationStaffScheduleList) {

        final StringBuffer targetNos = new StringBuffer();
        final Set<String> targetNosSet = new HashSet<>();
        final SanitationStaffTargetSearch targetSearch = new SanitationStaffTargetSearch();
        Pagination<SanitationStaffTarget> targets;

        for (final SanitationStaffSchedule sst : sanitationStaffScheduleList)
            if (sst.getSanitationStaffTarget() != null && sst.getSanitationStaffTarget().getTargetNo() != null
                    && !sst.getSanitationStaffTarget().getTargetNo().isEmpty())
                targetNosSet.add(sst.getSanitationStaffTarget().getTargetNo());

        final List<String> targetNoList = new ArrayList(targetNosSet);

        for (final String target : targetNoList) {

            if (targetNos.length() >= 1)
                targetNos.append(",");

            targetNos.append(target);

        }

        String tenantId = null;
        final Map<String, SanitationStaffTarget> targetMap = new HashMap<>();

        if (sanitationStaffScheduleList != null && !sanitationStaffScheduleList.isEmpty())
            tenantId = sanitationStaffScheduleList.get(0).getTenantId();

        targetSearch.setTenantId(tenantId);
        targetSearch.setTargetNos(targetNos.toString());
        targets = sanitationStaffTargetJdbcRepository.search(targetSearch);

        if (targets != null && targets.getPagedData() != null)
            for (final SanitationStaffTarget bd : targets.getPagedData())
                targetMap.put(bd.getTargetNo(), bd);

        for (final SanitationStaffSchedule sanitationStaffSchedule : sanitationStaffScheduleList)
            if (sanitationStaffSchedule.getSanitationStaffTarget() != null
                    && sanitationStaffSchedule.getSanitationStaffTarget().getTargetNo() != null
                    && !sanitationStaffSchedule.getSanitationStaffTarget().getTargetNo().isEmpty())
                sanitationStaffSchedule
                        .setSanitationStaffTarget(
                                targetMap.get(sanitationStaffSchedule.getSanitationStaffTarget().getTargetNo()));

    }

}