package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.constants.Constants;
import org.egov.swm.domain.model.Boundary;
import org.egov.swm.domain.model.CollectionPoint;
import org.egov.swm.domain.model.CollectionPointSearch;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.Route;
import org.egov.swm.domain.model.RouteSearch;
import org.egov.swm.domain.model.SanitationStaffTarget;
import org.egov.swm.domain.model.SanitationStaffTargetMap;
import org.egov.swm.domain.model.SanitationStaffTargetSearch;
import org.egov.swm.domain.model.SwmProcess;
import org.egov.swm.domain.service.RouteService;
import org.egov.swm.persistence.entity.DumpingGroundEntity;
import org.egov.swm.persistence.entity.SanitationStaffTargetEntity;
import org.egov.swm.web.contract.EmployeeResponse;
import org.egov.swm.web.repository.BoundaryRepository;
import org.egov.swm.web.repository.EmployeeRepository;
import org.egov.swm.web.repository.MdmsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONArray;

@Service
public class SanitationStaffTargetJdbcRepository extends JdbcRepository {

	public static final String TABLE_NAME = "egswm_sanitationstafftarget";

	@Autowired
	public SanitationStaffTargetMapJdbcRepository sanitationStaffTargetMapJdbcRepository;

	@Autowired
	public CollectionPointJdbcRepository collectionPointJdbcRepository;

	@Autowired
	private MdmsRepository mdmsRepository;

	@Autowired
	private BoundaryRepository boundaryRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private RouteService routeService;

	public Pagination<SanitationStaffTarget> search(SanitationStaffTargetSearch searchRequest) {

		String searchQuery = "select * from " + TABLE_NAME + " :condition  :orderby ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();

		if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
			validateSortByOrder(searchRequest.getSortBy());
			validateEntityFieldName(searchRequest.getSortBy(), SanitationStaffTargetSearch.class);
		}

		String orderBy = "order by targetNo";
		if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
			orderBy = "order by " + searchRequest.getSortBy();
		}

		if (searchRequest.getTargetNo() != null) {
			addAnd(params);
			params.append("targetNo in (:targetNo)");
			paramValues.put("targetNo", searchRequest.getTargetNo());
		}

		if (searchRequest.getTargetNos() != null) {
			addAnd(params);
			params.append("targetNo in (:targetNos)");
			paramValues.put("targetNos", new ArrayList<String>(Arrays.asList(searchRequest.getTargetNos().split(","))));
		}
		if (searchRequest.getTenantId() != null) {
			addAnd(params);
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", searchRequest.getTenantId());
		}

		if (searchRequest.getRouteCode() != null) {
			addAnd(params);
			params.append("route =:route");
			paramValues.put("route", searchRequest.getRouteCode());
		}

		if (searchRequest.getSwmProcessCode() != null) {
			addAnd(params);
			params.append("swmProcess =:swmProcess");
			paramValues.put("swmProcess", searchRequest.getSwmProcessCode());
		}

		if (searchRequest.getEmployeeCode() != null) {
			addAnd(params);
			params.append("employee =:employee");
			paramValues.put("employee", searchRequest.getEmployeeCode());
		}

		Pagination<SanitationStaffTarget> page = new Pagination<>();
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

		page = (Pagination<SanitationStaffTarget>) getPagination(searchQuery, page, paramValues);
		searchQuery = searchQuery + " :pagination";

		searchQuery = searchQuery.replace(":pagination",
				"limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(SanitationStaffTargetEntity.class);

		List<SanitationStaffTarget> sanitationStaffTargetList = new ArrayList<>();

		List<SanitationStaffTargetEntity> sanitationStaffTargetEntities = namedParameterJdbcTemplate
				.query(searchQuery.toString(), paramValues, row);

		SanitationStaffTarget sst;
		StringBuffer cpCodes = new StringBuffer();
		SanitationStaffTargetMap sstm;
		List<SanitationStaffTargetMap> collectionPoints;
		CollectionPointSearch cps;
		Pagination<CollectionPoint> collectionPointList;
		Boundary boundary;
		JSONArray responseJSONArray = null;
		ObjectMapper mapper = new ObjectMapper();
		RouteSearch routeSearch = new RouteSearch();
		Pagination<Route> routes;
		EmployeeResponse employeeResponse = null;

		for (SanitationStaffTargetEntity sanitationStaffTargetEntity : sanitationStaffTargetEntities) {

			sst = sanitationStaffTargetEntity.toDomain();

			if (sst.getLocation() != null && sst.getLocation().getCode() != null) {

				boundary = boundaryRepository.fetchBoundaryByCode(sst.getLocation().getCode(), sst.getTenantId());

				if (boundary != null)
					sst.setLocation(boundary);
			}

			if (sst.getSwmProcess() != null && sst.getSwmProcess().getCode() != null) {

				responseJSONArray = mdmsRepository.getByCriteria(sst.getTenantId(), Constants.MODULE_CODE,
						Constants.SWMPROCESS_MASTER_NAME, "code", sst.getSwmProcess().getCode(), new RequestInfo());

				if (responseJSONArray != null && responseJSONArray.size() > 0) {
					sst.setSwmProcess(mapper.convertValue(responseJSONArray.get(0), SwmProcess.class));
				}

			}

			if (sst.getRoute() != null && sst.getRoute().getCode() != null) {

				routeSearch.setTenantId(sst.getTenantId());
				routeSearch.setCode(sst.getRoute().getCode());
				routes = routeService.search(routeSearch);

				if (routes != null && routes.getPagedData() != null && !routes.getPagedData().isEmpty()) {
					sst.setRoute(routes.getPagedData().get(0));
				}

			}

			if (sst.getEmployee() != null && sst.getEmployee().getCode() != null) {

				employeeResponse = employeeRepository.getEmployeeByCode(sst.getEmployee().getCode(), sst.getTenantId(),
						new RequestInfo());

				if (employeeResponse != null && employeeResponse.getEmployees() != null
						&& !employeeResponse.getEmployees().isEmpty()) {
					sst.setEmployee(employeeResponse.getEmployees().get(0));
				}

			}

			if (sst.getDumpingGround() != null && sst.getDumpingGround().getCode() != null) {

				responseJSONArray = mdmsRepository.getByCriteria(sst.getTenantId(), Constants.MODULE_CODE,
						Constants.DUMPINGGROUND_MASTER_NAME, "code", sst.getDumpingGround().getCode(),
						new RequestInfo());

				if (responseJSONArray != null && responseJSONArray.size() > 0)
					sst.setDumpingGround(
							mapper.convertValue(responseJSONArray.get(0), DumpingGroundEntity.class).toDomain());

			}
			if (sanitationStaffTargetEntity.getTargetNo() != null
					&& !sanitationStaffTargetEntity.getTargetNo().isEmpty()) {
				sstm = SanitationStaffTargetMap.builder()
						.sanitationStaffTarget(sanitationStaffTargetEntity.getTargetNo()).build();
				sstm.setTenantId(sanitationStaffTargetEntity.getTenantId());
				collectionPoints = sanitationStaffTargetMapJdbcRepository.search(sstm);
				if (collectionPoints != null)
					for (SanitationStaffTargetMap map : collectionPoints) {
						if (cpCodes.length() > 0)
							cpCodes.append(",");
						cpCodes.append(map.getCollectionPoint());
					}
				cps = new CollectionPointSearch();
				cps.setCodes(cpCodes.toString());
				collectionPointList = collectionPointJdbcRepository.search(cps);

				sst.setCollectionPoints(collectionPointList.getPagedData());
			}
			sanitationStaffTargetList.add(sst);

		}

		page.setTotalResults(sanitationStaffTargetList.size());

		page.setPagedData(sanitationStaffTargetList);

		return page;
	}

}