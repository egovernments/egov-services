package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.swm.domain.model.Boundary;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.ServicedLocations;
import org.egov.swm.domain.model.ServicesOffered;
import org.egov.swm.domain.model.Supplier;
import org.egov.swm.domain.model.SwmProcess;
import org.egov.swm.domain.model.Vendor;
import org.egov.swm.domain.model.VendorSearch;
import org.egov.swm.persistence.entity.VendorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class VendorJdbcRepository extends JdbcRepository {

	public static final String TABLE_NAME = "egswm_vendor";

	@Autowired
	public JdbcTemplate jdbcTemplate;

	@Autowired
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	public SupplierJdbcRepository supplierJdbcRepository;

	@Autowired
	public ServicedLocationsJdbcRepository servicedLocationsJdbcRepository;

	@Autowired
	public ServicesOfferedJdbcRepository servicesOfferedJdbcRepository;

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
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("vendorNo in (:vendorNo)");
			paramValues.put("vendorNo", searchRequest.getVendorNo());
		}

		if (searchRequest.getVendorNos() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("vendorNo in (:vendorNos)");
			paramValues.put("vendorNos", new ArrayList<String>(Arrays.asList(searchRequest.getVendorNos().split(","))));
		}
		if (searchRequest.getTenantId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", searchRequest.getTenantId());
		}

		if (searchRequest.getName() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("name =:name");
			paramValues.put("name", searchRequest.getName());
		}

		if (searchRequest.getRegistrationNo() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("registrationNo =:registrationNo");
			paramValues.put("registrationNo", searchRequest.getRegistrationNo());
		}

		if (searchRequest.getSupplierNo() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
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

		List<VendorEntity> vendorEntities = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
		Vendor v;
		Supplier cs;
		ServicedLocations servicedLocations;
		ServicesOffered servicesOffered;
		List<Supplier> contractors;
		List<ServicedLocations> sls;
		List<ServicesOffered> sos;
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
					v.getServicedLocations().add(Boundary.builder().code(sl.getLocation()).build());
				}
			}

			servicesOffered = ServicesOffered.builder().tenantId(v.getTenantId()).vendor(v.getVendorNo()).build();

			sos = servicesOfferedJdbcRepository.search(servicesOffered);

			if (sos != null && !sos.isEmpty()) {

				v.setServicesOffered(new ArrayList<>());

				for (ServicesOffered so : sos) {
					v.getServicesOffered().add(SwmProcess.builder().name(so.getService()).build());
				}
			}

			vendorList.add(v);

		}

		page.setTotalResults(vendorList.size());

		page.setPagedData(vendorList);

		return page;
	}

}