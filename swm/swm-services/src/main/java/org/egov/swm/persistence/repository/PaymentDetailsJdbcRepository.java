package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.PaymentDetails;
import org.egov.swm.domain.model.PaymentDetailsSearch;
import org.egov.swm.domain.model.VendorPaymentDetails;
import org.egov.swm.domain.model.VendorPaymentDetailsSearch;
import org.egov.swm.domain.repository.VendorPaymentDetailsRepository;
import org.egov.swm.domain.service.VendorPaymentDetailsService;
import org.egov.swm.persistence.entity.PaymentDetailsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class PaymentDetailsJdbcRepository extends JdbcRepository {

    public static final String TABLE_NAME = "egswm_paymentdetails";

    /*@Autowired
    private VendorPaymentDetailsService vendorPaymentDetailsService;*/

    @Autowired
    private VendorPaymentDetailsJdbcRepository vendorPaymentDetailsJdbcRepository;

    public Boolean uniqueCheck(final String tenantId, final String fieldName, final String fieldValue,
            final String uniqueFieldName,
            final String uniqueFieldValue) {

        return uniqueCheck(TABLE_NAME, tenantId, fieldName, fieldValue, uniqueFieldName, uniqueFieldValue);
    }

    public Pagination<PaymentDetails> search(final PaymentDetailsSearch searchRequest) {

        String searchQuery = "select * from " + TABLE_NAME + " :condition  :orderby ";

        final Map<String, Object> paramValues = new HashMap<>();
        final StringBuffer params = new StringBuffer();

        if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
            validateSortByOrder(searchRequest.getSortBy());
            validateEntityFieldName(searchRequest.getSortBy(), PaymentDetailsSearch.class);
        }

        String orderBy = "order by code";
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

        if (searchRequest.getCodes() != null) {
            addAnd(params);
            params.append("code in (:codes)");
            paramValues.put("codes",
                    new ArrayList<>(Arrays.asList(searchRequest.getCodes().split(","))));
        }
        if (searchRequest.getTenantId() != null) {
            addAnd(params);
            params.append("tenantId =:tenantId");
            paramValues.put("tenantId", searchRequest.getTenantId());
        }

        if (searchRequest.getVoucherNumber() != null) {
            addAnd(params);
            params.append("voucherNumber =:voucherNumber");
            paramValues.put("voucherNumber", searchRequest.getVoucherNumber());
        }

        if (searchRequest.getVoucherDate() != null) {
            addAnd(params);
            params.append("voucherDate =:voucherDate");
            paramValues.put("voucherDate", searchRequest.getVoucherDate());
        }

        if (searchRequest.getInstrumentType() != null) {
            addAnd(params);
            params.append("instrumentType =:instrumentType");
            paramValues.put("instrumentType", searchRequest.getInstrumentType());
        }

        if (searchRequest.getInstrumentNumber() != null) {
            addAnd(params);
            params.append("instrumentNumber =:instrumentNumber");
            paramValues.put("instrumentNumber", searchRequest.getInstrumentNumber());
        }

        if (searchRequest.getInstrumentDate() != null) {
            addAnd(params);
            params.append("instrumentDate =:instrumentDate");
            paramValues.put("instrumentDate", searchRequest.getInstrumentDate());
        }

        Pagination<PaymentDetails> page = new Pagination<>();
        if (searchRequest.getOffset() != null)
            page.setOffset(searchRequest.getOffset());
        if (searchRequest.getPageSize() != null)
            page.setPageSize(searchRequest.getPageSize());

        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());
        else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        page = (Pagination<PaymentDetails>) getPagination(searchQuery, page, paramValues);
        searchQuery = searchQuery + " :pagination";

        searchQuery = searchQuery.replace(":pagination",
                "limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

        final BeanPropertyRowMapper row = new BeanPropertyRowMapper(PaymentDetailsEntity.class);

        final List<PaymentDetails> paymentdetailsList = new ArrayList<>();

        final List<PaymentDetailsEntity> paymentdetailsEntities = namedParameterJdbcTemplate
                .query(searchQuery.toString(), paramValues, row);

        for (final PaymentDetailsEntity paymentdetailsEntity : paymentdetailsEntities) {

            paymentdetailsList.add(paymentdetailsEntity.toDomain());

        }

       if (paymentdetailsList != null && !paymentdetailsList.isEmpty() &&
                (searchRequest.getExcludeVendorPaymentDetails() == null ||
                searchRequest.getExcludeVendorPaymentDetails().equals(Boolean.FALSE))) {

            populateVendorPaymentDetails(paymentdetailsList);

        }
        page.setTotalResults(paymentdetailsList.size());

        page.setPagedData(paymentdetailsList);

        return page;
    }

    private void populateVendorPaymentDetails(List<PaymentDetails> paymentdetailsList) {

        VendorPaymentDetailsSearch vendorPaymentDetailsSearch;
        Pagination<VendorPaymentDetails> vendorPaymentDetailsPage;
        StringBuffer paymentNos = new StringBuffer();
        Set<String> paymentNoSet = new HashSet<>();

        for (PaymentDetails v : paymentdetailsList) {

            if (v.getVendorPaymentDetails() != null && v.getVendorPaymentDetails().getPaymentNo() != null
                    && !v.getVendorPaymentDetails().getPaymentNo().isEmpty()) {

                paymentNoSet.add(v.getVendorPaymentDetails().getPaymentNo());

            }

        }

        List<String> paymentNoList = new ArrayList(paymentNoSet);

        for (String paymentNo : paymentNoList) {

            if (paymentNos.length() >= 1)
                paymentNos.append(",");

            paymentNos.append(paymentNo);

        }
        if (paymentNos != null && paymentNos.length() > 0) {
            String tenantId = null;
            Map<String, VendorPaymentDetails> vendorPaymentMap = new HashMap<>();

            if (paymentdetailsList != null && !paymentdetailsList.isEmpty())
                tenantId = paymentdetailsList.get(0).getTenantId();

            vendorPaymentDetailsSearch = new VendorPaymentDetailsSearch();
            vendorPaymentDetailsSearch.setTenantId(tenantId);
            vendorPaymentDetailsSearch.setPaymentNos(paymentNos.toString());

            vendorPaymentDetailsPage = vendorPaymentDetailsJdbcRepository.search(vendorPaymentDetailsSearch);

            if (vendorPaymentDetailsPage != null && vendorPaymentDetailsPage.getPagedData() != null)
                for (VendorPaymentDetails bd : vendorPaymentDetailsPage.getPagedData()) {

                    vendorPaymentMap.put(bd.getPaymentNo(), bd);

                }

            for (PaymentDetails paymentdetails : paymentdetailsList) {

                if (paymentdetails.getVendorPaymentDetails() != null
                        && paymentdetails.getVendorPaymentDetails().getPaymentNo() != null
                        && !paymentdetails.getVendorPaymentDetails().getPaymentNo().isEmpty()) {

                    paymentdetails
                            .setVendorPaymentDetails(
                                    vendorPaymentMap.get(paymentdetails.getVendorPaymentDetails().getPaymentNo()));
                }

            }
        }

    }

}