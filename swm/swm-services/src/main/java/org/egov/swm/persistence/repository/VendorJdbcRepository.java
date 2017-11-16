package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.domain.model.Boundary;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.ServicedLocations;
import org.egov.swm.domain.model.ServicesOffered;
import org.egov.swm.domain.model.Supplier;
import org.egov.swm.domain.model.SwmProcess;
import org.egov.swm.domain.model.Vendor;
import org.egov.swm.domain.model.VendorSearch;
import org.egov.swm.domain.service.SwmProcessService;
import org.egov.swm.persistence.entity.VendorEntity;
import org.egov.swm.web.repository.BoundaryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class VendorJdbcRepository extends JdbcRepository {

	private static final Logger LOG = LoggerFactory.getLogger(JdbcRepository.class);

	public static final String TABLE_NAME = "egswm_vendor";

	@Autowired
	public SupplierJdbcRepository supplierJdbcRepository;

	@Autowired
	public ServicedLocationsJdbcRepository servicedLocationsJdbcRepository;

	@Autowired
	public ServicesOfferedJdbcRepository servicesOfferedJdbcRepository;

	@Autowired
	private SwmProcessService swmProcessService;

	@Autowired
	private BoundaryRepository boundaryRepository;

	public Boolean uniqueCheck(String tenantId, String fieldName, String fieldValue, String uniqueFieldName,
			String uniqueFieldValue) {

		return uniqueCheck(TABLE_NAME, tenantId, fieldName, fieldValue, uniqueFieldName, uniqueFieldValue);
	}

	public Pagination<Vendor> search(VendorSearch searchRequest) {

		String searchQuery = "select * from " + TABLE_NAME + " :condition  :orderby ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();

		if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
			validateSortByOrder(searchRequest.getSortBy());
			validateEntityFieldName(searchRequest.getSortBy(), VendorSearch.class);
		}

		String orderBy = "order by name";
		if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
			orderBy = "order by " + searchRequest.getSortBy();
		}

		if (searchRequest.getVendorNo() != null) {
			addAnd(params);
			params.append("vendorNo in (:vendorNo)");
			paramValues.put("vendorNo", searchRequest.getVendorNo());
		}

		if (searchRequest.getVendorNos() != null) {
			addAnd(params);
			params.append("vendorNo in (:vendorNos)");
			paramValues.put("vendorNos", new ArrayList<String>(Arrays.asList(searchRequest.getVendorNos().split(","))));
		}
		if (searchRequest.getTenantId() != null) {
			addAnd(params);
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", searchRequest.getTenantId());
		}

		if (searchRequest.getName() != null) {
			addAnd(params);
			params.append("name =:name");
			paramValues.put("name", searchRequest.getName());
		}

		if (searchRequest.getRegistrationNo() != null) {
			addAnd(params);
			params.append("registrationNo =:registrationNo");
			paramValues.put("registrationNo", searchRequest.getRegistrationNo());
		}

		if (searchRequest.getSupplierNo() != null) {
			addAnd(params);
			params.append("supplier =:supplier");
			paramValues.put("supplier", searchRequest.getSupplierNo());
		}

		Pagination<Vendor> page = new Pagination<>();
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

		page = (Pagination<Vendor>) getPagination(searchQuery, page, paramValues);
		searchQuery = searchQuery + " :pagination";

		searchQuery = searchQuery.replace(":pagination",
				"limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(VendorEntity.class);

		List<Vendor> vendorList = new ArrayList<>();
		LOG.info("Vendor search query  " + searchQuery + " and param values" + paramValues.toString());
		List<VendorEntity> vendorEntities = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
		Vendor v;
		Supplier cs;
		ServicedLocations servicedLocations;
		ServicesOffered servicesOffered;
		List<Supplier> contractors;
		List<ServicedLocations> sls;
		List<ServicesOffered> sos;
		SwmProcess p;
		Boundary boundary;

		for (VendorEntity vendorEntity : vendorEntities) {

			v = vendorEntity.toDomain();
			cs = Supplier.builder().tenantId(v.getTenantId()).supplierNo(vendorEntity.getSupplier()).build();

			contractors = supplierJdbcRepository.search(cs);

			if (contractors != null && !contractors.isEmpty()) {
				v.setSupplier(contractors.get(0));
			}

			servicedLocations = ServicedLocations.builder().tenantId(v.getTenantId()).vendor(v.getVendorNo()).build();

			sls = servicedLocationsJdbcRepository.search(servicedLocations);

			if (sls != null && !sls.isEmpty()) {

				v.setServicedLocations(new ArrayList<>());

				for (ServicedLocations sl : sls) {

					if (sl.getLocation() != null && !sl.getLocation().isEmpty()) {

						boundary = boundaryRepository.fetchBoundaryByCode(sl.getLocation(), sl.getTenantId());

						if (boundary != null)
							v.getServicedLocations().add(boundary);

					}

				}
			}

			servicesOffered = ServicesOffered.builder().tenantId(v.getTenantId()).vendor(v.getVendorNo()).build();

			sos = servicesOfferedJdbcRepository.search(servicesOffered);

			if (sos != null && !sos.isEmpty()) {

				v.setServicesOffered(new ArrayList<>());

				for (ServicesOffered so : sos) {
					if (so.getService() != null && !so.getService().isEmpty()) {

						p = swmProcessService.getSwmProcess(so.getTenantId(), so.getService(), new RequestInfo());
						if (p != null)
							v.getServicesOffered().add(p);

					}
				}
			}

			vendorList.add(v);

		}

		page.setTotalResults(vendorList.size());

		page.setPagedData(vendorList);

		return page;
	}

}