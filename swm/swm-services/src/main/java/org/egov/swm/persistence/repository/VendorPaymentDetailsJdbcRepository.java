package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.domain.model.Document;
import org.egov.swm.domain.model.DocumentSearch;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.VendorContract;
import org.egov.swm.domain.model.VendorContractSearch;
import org.egov.swm.domain.model.VendorPaymentDetails;
import org.egov.swm.domain.model.VendorPaymentDetailsSearch;
import org.egov.swm.domain.service.VendorContractService;
import org.egov.swm.persistence.entity.VendorPaymentDetailsEntity;
import org.egov.swm.web.contract.Employee;
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

    @Autowired
    private DocumentJdbcRepository documentJdbcRepository;

    public Boolean uniqueCheck(final String tenantId, final String fieldName, final String fieldValue,
            final String uniqueFieldName,
            final String uniqueFieldValue) {

        return uniqueCheck(TABLE_NAME, tenantId, fieldName, fieldValue, uniqueFieldName, uniqueFieldValue);
    }

    public Pagination<VendorPaymentDetails> search(final VendorPaymentDetailsSearch searchRequest) {

        String searchQuery = "select * from " + TABLE_NAME + " :condition  :orderby ";

        final Map<String, Object> paramValues = new HashMap<>();
        final StringBuffer params = new StringBuffer();

        if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
            validateSortByOrder(searchRequest.getSortBy());
            validateEntityFieldName(searchRequest.getSortBy(), VendorPaymentDetails.class);
        }

        String orderBy = "order by paymentNo";
        if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty())
            orderBy = "order by " + searchRequest.getSortBy();

        if (searchRequest.getPaymentNo() != null) {
            addAnd(params);
            params.append("paymentNo in (:paymentNo)");
            paramValues.put("paymentNo", searchRequest.getPaymentNo());
        }

        if (searchRequest.getPaymentNos() != null) {
            addAnd(params);
            params.append("paymentNo in (:paymentNos)");
            paramValues.put("paymentNos",
                    new ArrayList<>(Arrays.asList(searchRequest.getPaymentNos().split(","))));
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

        if (searchRequest.getInvoiceDate() != null) {
            addAnd(params);
            params.append("invoiceDate =:invoiceDate");
            paramValues.put("invoiceDate", searchRequest.getInvoiceDate());
        }

        if (searchRequest.getFromDate() != null && (searchRequest.getValidate() == null ||
                searchRequest.getValidate().equals(Boolean.FALSE))) {
            addAnd(params);
            params.append("fromDate >=:fromDate");
            paramValues.put("fromDate", searchRequest.getFromDate());
        }

        if (searchRequest.getToDate() != null && (searchRequest.getValidate() == null ||
                searchRequest.getValidate().equals(Boolean.FALSE))) {
            addAnd(params);
            params.append("toDate <=:toDate");
            paramValues.put("toDate", searchRequest.getToDate());
        }

        if (searchRequest.getInvoiceFromDate() != null) {
            addAnd(params);
            params.append("invoicedate >=:invoiceFromDate");
            paramValues.put("invoiceFromDate", searchRequest.getInvoiceFromDate());
        }

        if (searchRequest.getInvoiceToDate() != null) {
            addAnd(params);
            params.append("invoicedate <=:invoiceToDate");
            paramValues.put("invoiceToDate", searchRequest.getInvoiceToDate());
        }

        if (searchRequest.getFromAmount() != null) {
            addAnd(params);
            params.append("vendorinvoiceamount >= :fromAmount");
            paramValues.put("fromAmount", searchRequest.getFromAmount());
        }

        if (searchRequest.getToAmount() != null) {
            addAnd(params);
            params.append("vendorinvoiceamount <= :toAmount");
            paramValues.put("toAmount", searchRequest.getToAmount());
        }

        // Used for validation of overlapping time periods
        if (searchRequest.getFromDate() != null && searchRequest.getToDate() != null &&
                searchRequest.getValidate() != null && searchRequest.getValidate()) {
            addAnd(params);
            params.append("((to_timestamp(fromdate/1000) AT TIME ZONE 'Asia/Kolkata') BETWEEN" +
                    " (to_timestamp(:fromDate/1000) AT TIME ZONE 'Asia/Kolkata') AND (to_timestamp(:toDate/1000) AT TIME ZONE 'Asia/Kolkata')"
                    +
                    " or (to_timestamp(todate/1000) AT TIME ZONE 'Asia/Kolkata') BETWEEN" +
                    " (to_timestamp(:fromDate/1000) AT TIME ZONE 'Asia/Kolkata') AND (to_timestamp(:toDate/1000) AT TIME ZONE 'Asia/Kolkata')"
                    +
                    " or (to_timestamp(:fromDate/1000) AT TIME ZONE 'Asia/Kolkata') BETWEEN" +
                    " (to_timestamp(fromdate/1000) AT TIME ZONE 'Asia/Kolkata') AND (to_timestamp(todate/1000) AT TIME ZONE 'Asia/Kolkata')"
                    +
                    " or (to_timestamp(:toDate/1000) AT TIME ZONE 'Asia/Kolkata') BETWEEN" +
                    " (to_timestamp(fromdate/1000) AT TIME ZONE 'Asia/Kolkata') AND (to_timestamp(todate/1000) AT TIME ZONE 'Asia/Kolkata'))");
            paramValues.put("fromDate", searchRequest.getFromDate());
            paramValues.put("toDate", searchRequest.getToDate());
        }

        Pagination<VendorPaymentDetails> page = new Pagination<>();
        if (searchRequest.getOffset() != null)
            page.setOffset(searchRequest.getOffset());
        if (searchRequest.getPageSize() != null)
            page.setPageSize(searchRequest.getPageSize());

        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());
        else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        page = (Pagination<VendorPaymentDetails>) getPagination(searchQuery, page, paramValues);
        searchQuery = searchQuery + " :pagination";

        searchQuery = searchQuery.replace(":pagination",
                "limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

        final BeanPropertyRowMapper row = new BeanPropertyRowMapper(VendorPaymentDetailsEntity.class);

        List<VendorPaymentDetails> vendorPaymentDetailsList = new ArrayList<>();

        final List<VendorPaymentDetailsEntity> vendorPaymentDetailsEntities = namedParameterJdbcTemplate
                .query(searchQuery.toString(), paramValues, row);

        for (final VendorPaymentDetailsEntity vendorPaymentDetailsEntity : vendorPaymentDetailsEntities) {

            vendorPaymentDetailsList.add(vendorPaymentDetailsEntity.toDomain());

        }

        if (vendorPaymentDetailsList != null && !vendorPaymentDetailsList.isEmpty()) {

            populateVendorContracts(vendorPaymentDetailsList);

            populateEmployees(vendorPaymentDetailsList);

            populateDocument(vendorPaymentDetailsList);
        }

        if (searchRequest.getVendorNo() != null && !searchRequest.getVendorNo().isEmpty())
            vendorPaymentDetailsList = filterForVendorNumber(vendorPaymentDetailsList, searchRequest);

        page.setPagedData(vendorPaymentDetailsList);

        return page;
    }

    private List<VendorPaymentDetails> filterForVendorNumber(List<VendorPaymentDetails> vendorPaymentDetailsList,
            VendorPaymentDetailsSearch searchRequest) {
        return vendorPaymentDetailsList.stream()
                .filter(vp -> vp.getVendorContract().getVendor().getVendorNo()
                        .equalsIgnoreCase(searchRequest.getVendorNo()))
                .collect(Collectors.toList());
    }

    private void populateDocument(List<VendorPaymentDetails> vendorPaymentDetailsList) {
        String tenantId = null;
        Map<String, List<Document>> documentMap = new HashMap<>();
        String vendorPaymentCodes = vendorPaymentDetailsList.stream()
                .map(VendorPaymentDetails::getPaymentNo)
                .collect(Collectors.joining(","));

        if (vendorPaymentDetailsList != null && !vendorPaymentDetailsList.isEmpty())
            tenantId = vendorPaymentDetailsList.get(0).getTenantId();

        DocumentSearch search = new DocumentSearch();
        search.setTenantId(tenantId);
        search.setRefCodes(vendorPaymentCodes);

        List<Document> docs = documentJdbcRepository.search(search);

        if (docs != null)
            documentMap = docs.stream().collect(Collectors.groupingBy(Document::getRefCode));

        for (VendorPaymentDetails vendorPaymentDetails : vendorPaymentDetailsList) {
            vendorPaymentDetails.setDocuments(documentMap.get(vendorPaymentDetails.getPaymentNo()));
        }

    }

    private void populateVendorContracts(List<VendorPaymentDetails> vendorPaymentDetailsList) {

        VendorContractSearch vendorContractSearch;
        Pagination<VendorContract> vendorContracts;
        StringBuffer vendorContractNos = new StringBuffer();
        Set<String> vendorContractNoSet = new HashSet<>();

        for (VendorPaymentDetails v : vendorPaymentDetailsList) {

            if (v.getVendorContract() != null && v.getVendorContract().getContractNo() != null
                    && !v.getVendorContract().getContractNo().isEmpty()) {

                vendorContractNoSet.add(v.getVendorContract().getContractNo());
            }
        }

        List<String> vendorContractNoList = new ArrayList(vendorContractNoSet);

        for (String vendorContractNo : vendorContractNoList) {

            if (vendorContractNos.length() >= 1)
                vendorContractNos.append(",");

            vendorContractNos.append(vendorContractNo);

        }
        if (vendorContractNos != null && vendorContractNos.length() > 0) {
            String tenantId = null;
            Map<String, VendorContract> vendorContractMap = new HashMap<>();

            if (vendorPaymentDetailsList != null && !vendorPaymentDetailsList.isEmpty())
                tenantId = vendorPaymentDetailsList.get(0).getTenantId();

            vendorContractSearch = new VendorContractSearch();
            vendorContractSearch.setTenantId(tenantId);
            vendorContractSearch.setContractNos(vendorContractNos.toString());

            vendorContracts = vendorContractService.search(vendorContractSearch);

            if (vendorContracts != null && vendorContracts.getPagedData() != null)
                for (VendorContract bd : vendorContracts.getPagedData()) {

                    vendorContractMap.put(bd.getContractNo(), bd);

                }

            for (VendorPaymentDetails vendorPaymentDetails : vendorPaymentDetailsList) {

                if (vendorPaymentDetails.getVendorContract() != null
                        && vendorPaymentDetails.getVendorContract().getContractNo() != null
                        && !vendorPaymentDetails.getVendorContract().getContractNo().isEmpty()) {

                    vendorPaymentDetails
                            .setVendorContract(vendorContractMap.get(vendorPaymentDetails.getVendorContract().getContractNo()));
                }

            }
        }

    }

    private void populateEmployees(List<VendorPaymentDetails> vendorPaymentDetailsList) {

        StringBuffer employeeCodes = new StringBuffer();
        Set<String> employeeCodesSet = new HashSet<>();

        for (VendorPaymentDetails sst : vendorPaymentDetailsList) {

            if (sst.getEmployee() != null && sst.getEmployee().getCode() != null
                    && !sst.getEmployee().getCode().isEmpty()) {

                employeeCodesSet.add(sst.getEmployee().getCode());

            }

        }

        List<String> employeeCodeList = new ArrayList(employeeCodesSet);

        for (String code : employeeCodeList) {

            if (employeeCodes.length() >= 1)
                employeeCodes.append(",");

            employeeCodes.append(code);

        }
        if (employeeCodes != null && employeeCodes.length() > 0) {
            String tenantId = null;
            Map<String, Employee> employeeMap = new HashMap<>();

            if (vendorPaymentDetailsList != null && !vendorPaymentDetailsList.isEmpty())
                tenantId = vendorPaymentDetailsList.get(0).getTenantId();

            EmployeeResponse response = employeeRepository.getEmployeeByCodes(employeeCodes.toString(), tenantId,
                    new RequestInfo());

            if (response != null && response.getEmployees() != null)
                for (Employee e : response.getEmployees()) {

                    employeeMap.put(e.getCode(), e);

                }

            for (VendorPaymentDetails vendorPaymentDetails : vendorPaymentDetailsList) {

                if (vendorPaymentDetails.getEmployee() != null && vendorPaymentDetails.getEmployee().getCode() != null
                        && !vendorPaymentDetails.getEmployee().getCode().isEmpty()) {

                    vendorPaymentDetails.setEmployee(employeeMap.get(vendorPaymentDetails.getEmployee().getCode()));
                }

            }
        }
    }

}