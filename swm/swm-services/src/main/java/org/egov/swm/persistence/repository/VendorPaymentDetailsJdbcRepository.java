package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.VendorContract;
import org.egov.swm.domain.model.VendorContractSearch;
import org.egov.swm.domain.model.VendorPaymentDetails;
import org.egov.swm.domain.model.VendorPaymentDetailsSearch;
import org.egov.swm.domain.service.VendorContractService;
import org.egov.swm.persistence.entity.VendorPaymentDetailsEntity;
import org.egov.swm.web.contract.EmployeeResponse;
import org.egov.swm.web.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class VendorPaymentDetailsJdbcRepository extends JdbcRepository {

	public static final String TABLE_NAME = "egswm_vendorpaymentdetails";

	@Autowired
	private VendorContractService vendorContractService;

	@Autowired
	private EmployeeRepository employeeRepository;

	public Boolean uniqueCheck(String tenantId, String fieldName, String fieldValue, String uniqueFieldName,
			String uniqueFieldValue) {

		return uniqueCheck(TABLE_NAME, tenantId, fieldName, fieldValue, uniqueFieldName, uniqueFieldValue);
	}

	public Pagination<VendorPaymentDetails> search(VendorPaymentDetailsSearch searchRequest) {

		String searchQuery = "select * from " + TABLE_NAME + " :condition  :orderby ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();

		if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
			validateSortByOrder(searchRequest.getSortBy());
			validateEntityFieldName(searchRequest.getSortBy(), VendorPaymentDetailsSearch.class);
		}

		String orderBy = "order by paymentNo";
		if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
			orderBy = "order by " + searchRequest.getSortBy();
		}

		if (searchRequest.getPaymentNo() != null) {
			addAnd(params);
			params.append("paymentNo in (:paymentNo)");
			paramValues.put("paymentNo", searchRequest.getPaymentNo());
		}

		if (searchRequest.getPaymentNos() != null) {
			addAnd(params);
			params.append("paymentNo in (:paymentNos)");
			paramValues.put("paymentNos",
					new ArrayList<String>(Arrays.asList(searchRequest.getPaymentNos().split(","))));
		}
		if (searchRequest.getTenantId() != null) {
			addAnd(params);
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", searchRequest.getTenantId());
		}

		if (searchRequest.getContractNo() != null) {
			addAnd(params);
			params.append("vendorcontract =:contractNo");
			paramValues.put("contractNo", searchRequest.getContractNo());
		}

		if (searchRequest.getEmployeeCode() != null) {
			addAnd(params);
			params.append("employee =:employeeCode");
			paramValues.put("employeeCode", searchRequest.getEmployeeCode());
		}

		if (searchRequest.getVendorInvoiceAmount() != null) {
			addAnd(params);
			params.append("vendorinvoiceamount =:vendorinvoiceamount");
			paramValues.put("vendorinvoiceamount", searchRequest.getVendorInvoiceAmount());
		}

		if (searchRequest.getInvoiceNo() != null) {
			addAnd(params);
			params.append("invoiceNo =:invoiceNo");
			paramValues.put("invoiceNo", searchRequest.getInvoiceNo());
		}

		if (searchRequest.getFromDate() != null) {
			addAnd(params);
			params.append("fromDate =:fromDate");
			paramValues.put("fromDate", searchRequest.getFromDate());
		}

		if (searchRequest.getToDate() != null) {
			addAnd(params);
			params.append("toDate =:toDate");
			paramValues.put("toDate", searchRequest.getToDate());
		}

		Pagination<VendorPaymentDetails> page = new Pagination<>();
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

		page = (Pagination<VendorPaymentDetails>) getPagination(searchQuery, page, paramValues);
		searchQuery = searchQuery + " :pagination";

		searchQuery = searchQuery.replace(":pagination",
				"limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(VendorPaymentDetailsEntity.class);

		List<VendorPaymentDetails> vendorPaymentDetailsList = new ArrayList<>();

		List<VendorPaymentDetailsEntity> vendorPaymentDetailsEntities = namedParameterJdbcTemplate
				.query(searchQuery.toString(), paramValues, row);
		VendorPaymentDetails vendorPaymentDetail;
		Pagination<VendorContract> vendorContractPage;
		EmployeeResponse employeeResponse;

		for (VendorPaymentDetailsEntity vendorPaymentDetailsEntity : vendorPaymentDetailsEntities) {

			vendorPaymentDetail = vendorPaymentDetailsEntity.toDomain();

			if (vendorPaymentDetail.getVendorContract() != null
					&& vendorPaymentDetail.getVendorContract().getContractNo() != null) {

				vendorContractPage = getVendorContracts(vendorPaymentDetail);

				if (vendorContractPage != null && vendorContractPage.getPagedData() != null
						&& !vendorContractPage.getPagedData().isEmpty())
					vendorPaymentDetail.setVendorContract(vendorContractPage.getPagedData().get(0));
			}

			if (vendorPaymentDetail.getEmployee() != null && vendorPaymentDetail.getEmployee().getCode() != null) {

				employeeResponse = employeeRepository.getEmployeeByCode(vendorPaymentDetail.getEmployee().getCode(),
						vendorPaymentDetail.getTenantId(), new RequestInfo());

				if (employeeResponse != null && employeeResponse.getEmployees() != null
						&& !employeeResponse.getEmployees().isEmpty()) {
					vendorPaymentDetail.setEmployee(employeeResponse.getEmployees().get(0));
				}
			}

			vendorPaymentDetailsList.add(vendorPaymentDetail);

		}

		page.setTotalResults(vendorPaymentDetailsList.size());

		page.setPagedData(vendorPaymentDetailsList);

		return page;
	}

	private Pagination<VendorContract> getVendorContracts(VendorPaymentDetails vendorPaymentDetail) {
		VendorContractSearch vendorContractSearch = new VendorContractSearch();
		vendorContractSearch.setTenantId(vendorPaymentDetail.getTenantId());
		vendorContractSearch.setContractNo(vendorPaymentDetail.getVendorContract().getContractNo());

		return vendorContractService.search(vendorContractSearch);
	}

}