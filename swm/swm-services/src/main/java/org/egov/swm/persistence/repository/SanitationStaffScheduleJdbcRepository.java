package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.constants.Constants;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.SanitationStaffSchedule;
import org.egov.swm.domain.model.SanitationStaffScheduleSearch;
import org.egov.swm.domain.model.SanitationStaffTarget;
import org.egov.swm.domain.model.SanitationStaffTargetSearch;
import org.egov.swm.domain.model.Shift;
import org.egov.swm.persistence.entity.SanitationStaffScheduleEntity;
import org.egov.swm.web.repository.MdmsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONArray;

@Service
public class SanitationStaffScheduleJdbcRepository extends JdbcRepository {

	public static final String TABLE_NAME = "egswm_sanitationstaffschedule";

	@Autowired
	public SanitationStaffTargetJdbcRepository sanitationStaffTargetJdbcRepository;

	@Autowired
	private MdmsRepository mdmsRepository;

	public Pagination<SanitationStaffSchedule> search(SanitationStaffScheduleSearch searchRequest) {

		String searchQuery = "select * from " + TABLE_NAME + " :condition  :orderby ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();

		if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
			validateSortByOrder(searchRequest.getSortBy());
			validateEntityFieldName(searchRequest.getSortBy(), SanitationStaffScheduleSearch.class);
		}

		String orderBy = "order by transactionNo";
		if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
			orderBy = "order by " + searchRequest.getSortBy();
		}

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
					new ArrayList<String>(Arrays.asList(searchRequest.getTransactionNos().split(","))));
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
		if (searchRequest.getOffset() != null) {
			page.setOffset(searchRequest.getOffset());
		}
		if (searchRequest.getPageSize() != null) {
			page.setPageSize(searchRequest.getPageSize());
		}

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else

			searchQuery = searchQuery.replace(":condition", "");

		searchQuery = searchQuery.replace(":orderby", orderBy);

		page = (Pagination<SanitationStaffSchedule>) getPagination(searchQuery, page, paramValues);
		searchQuery = searchQuery + " :pagination";

		searchQuery = searchQuery.replace(":pagination",
				"limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(SanitationStaffScheduleEntity.class);

		List<SanitationStaffSchedule> sanitationStaffScheduleList = new ArrayList<>();

		List<SanitationStaffScheduleEntity> sanitationStaffScheduleEntities = namedParameterJdbcTemplate
				.query(searchQuery.toString(), paramValues, row);

		SanitationStaffSchedule sss;
		SanitationStaffTargetSearch ssts;
		Pagination<SanitationStaffTarget> sanitationStaffTargets;
		JSONArray responseJSONArray = null;
		ObjectMapper mapper = new ObjectMapper();

		for (SanitationStaffScheduleEntity sanitationStaffScheduleEntity : sanitationStaffScheduleEntities) {

			sss = sanitationStaffScheduleEntity.toDomain();

			if (sss.getShift() != null && sss.getShift().getCode() != null) {

				responseJSONArray = mdmsRepository.getByCriteria(sss.getTenantId(), Constants.MODULE_CODE,
						Constants.SHIFT_MASTER_NAME, "code", sss.getShift().getCode(), new RequestInfo());

				if (responseJSONArray != null && responseJSONArray.size() > 0) {
					sss.setShift(mapper.convertValue(responseJSONArray.get(0), Shift.class));
				}
			}

			if (sanitationStaffScheduleEntity.getSanitationStaffTarget() != null
					&& !sanitationStaffScheduleEntity.getSanitationStaffTarget().isEmpty()) {
				ssts = new SanitationStaffTargetSearch();
				ssts.setTenantId(sanitationStaffScheduleEntity.getTenantId());
				ssts.setTargetNo(sanitationStaffScheduleEntity.getSanitationStaffTarget());

				sanitationStaffTargets = sanitationStaffTargetJdbcRepository.search(ssts);

				if (sanitationStaffTargets != null && sanitationStaffTargets.getPagedData() != null
						&& !sanitationStaffTargets.getPagedData().isEmpty()) {

					sss.setSanitationStaffTarget(sanitationStaffTargets.getPagedData().get(0));

				}
			}

			sanitationStaffScheduleList.add(sss);

		}

		page.setTotalResults(sanitationStaffScheduleList.size());

		page.setPagedData(sanitationStaffScheduleList);

		return page;
	}

}