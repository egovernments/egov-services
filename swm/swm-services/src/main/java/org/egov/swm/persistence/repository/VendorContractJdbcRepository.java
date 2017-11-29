package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.Vendor;
import org.egov.swm.domain.model.VendorContract;
import org.egov.swm.domain.model.VendorContractSearch;
import org.egov.swm.domain.model.VendorSearch;
import org.egov.swm.domain.service.VendorService;
import org.egov.swm.persistence.entity.VendorContractEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class VendorContractJdbcRepository extends JdbcRepository {

    public static final String TABLE_NAME = "egswm_vendorcontract";

    @Autowired
    private VendorService vendorService;

    public Pagination<VendorContract> search(final VendorContractSearch searchRequest) {

        String searchQuery = "select * from " + TABLE_NAME + " :condition  :orderby ";

        final Map<String, Object> paramValues = new HashMap<>();
        final StringBuffer params = new StringBuffer();

        if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
            validateSortByOrder(searchRequest.getSortBy());
            validateEntityFieldName(searchRequest.getSortBy(), VendorContractSearch.class);
        }

        String orderBy = "order by contractNo";
        if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty())
            orderBy = "order by " + searchRequest.getSortBy();

        if (searchRequest.getTenantId() != null) {
            addAnd(params);
            params.append("tenantId =:tenantId");
            paramValues.put("tenantId", searchRequest.getTenantId());
        }

        if (searchRequest.getContractNos() != null) {
            addAnd(params);
            params.append("contractNo in(:contractNos) ");
            paramValues.put("contractNos",
                    new ArrayList<>(Arrays.asList(searchRequest.getContractNos().split(","))));
        }

        if (searchRequest.getVendorNo() != null) {
            addAnd(params);
            params.append("vendor =:vendor");
            paramValues.put("vendor", searchRequest.getVendorNo());
        }

        if (searchRequest.getContractNo() != null) {
            addAnd(params);
            params.append("contractNo =:contractNo");
            paramValues.put("contractNo", searchRequest.getContractNo());
        }

        if (searchRequest.getContractDate() != null) {
            addAnd(params);
            params.append("contractDate =:contractDate");
            paramValues.put("contractDate", searchRequest.getContractDate());
        }

        if (searchRequest.getContractPeriodFrom() != null) {
            addAnd(params);
            params.append("contractPeriodFrom =:contractPeriodFrom");
            paramValues.put("contractPeriodFrom", searchRequest.getContractPeriodFrom());
        }

        if (searchRequest.getContractPeriodTo() != null) {
            addAnd(params);
            params.append("contractPeriodTo =:contractPeriodTo");
            paramValues.put("contractPeriodTo", searchRequest.getContractPeriodTo());
        }

        if (searchRequest.getSecurityDeposit() != null) {
            addAnd(params);
            params.append("securityDeposit =:securityDeposit");
            paramValues.put("securityDeposit", searchRequest.getSecurityDeposit());
        }

        if (searchRequest.getPaymentAmount() != null) {
            addAnd(params);
            params.append("paymentAmount =:paymentAmount");
            paramValues.put("paymentAmount", searchRequest.getPaymentAmount());
        }

        Pagination<VendorContract> page = new Pagination<>();
        if (searchRequest.getOffset() != null)
            page.setOffset(searchRequest.getOffset());
        if (searchRequest.getPageSize() != null)
            page.setPageSize(searchRequest.getPageSize());

        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());
        else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        page = (Pagination<VendorContract>) getPagination(searchQuery, page, paramValues);
        searchQuery = searchQuery + " :pagination";

        searchQuery = searchQuery.replace(":pagination",
                "limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

        final BeanPropertyRowMapper row = new BeanPropertyRowMapper(VendorContractEntity.class);

        final List<VendorContract> vendorContractList = new ArrayList<>();

        final List<VendorContractEntity> vendorContractEntities = namedParameterJdbcTemplate.query(searchQuery.toString(),
                paramValues, row);

        for (final VendorContractEntity entity : vendorContractEntities) {

            vendorContractList.add(entity.toDomain());
        }

        if (vendorContractList != null && !vendorContractList.isEmpty()) {

            populateVendors(vendorContractList);
        }
        page.setTotalResults(vendorContractList.size());

        page.setPagedData(vendorContractList);

        return page;
    }

    private void populateVendors(List<VendorContract> vendorContractList) {

        VendorSearch vendorSearch;
        Pagination<Vendor> vendors;
        StringBuffer vendorNos = new StringBuffer();
        Set<String> vendorNoSet = new HashSet<>();

        for (VendorContract v : vendorContractList) {

            if (v.getVendor() != null && v.getVendor().getVendorNo() != null
                    && !v.getVendor().getVendorNo().isEmpty()) {

                vendorNoSet.add(v.getVendor().getVendorNo());

            }

        }

        List<String> vendorNoList = new ArrayList(vendorNoSet);

        for (String vendorNo : vendorNoList) {

            if (vendorNos.length() >= 1)
                vendorNos.append(",");

            vendorNos.append(vendorNo);

        }

        if (vendorNos != null && vendorNos.length() > 0) {
            String tenantId = null;
            Map<String, Vendor> vendorMap = new HashMap<>();

            if (vendorContractList != null && !vendorContractList.isEmpty())
                tenantId = vendorContractList.get(0).getTenantId();

            vendorSearch = new VendorSearch();
            vendorSearch.setTenantId(tenantId);
            vendorSearch.setVendorNos(vendorNos.toString());

            vendors = vendorService.search(vendorSearch);

            if (vendors != null && vendors.getPagedData() != null)
                for (Vendor bd : vendors.getPagedData()) {

                    vendorMap.put(bd.getVendorNo(), bd);

                }

            for (VendorContract vendorContract : vendorContractList) {

                if (vendorContract.getVendor() != null && vendorContract.getVendor().getVendorNo() != null
                        && !vendorContract.getVendor().getVendorNo().isEmpty()) {

                    vendorContract.setVendor(vendorMap.get(vendorContract.getVendor().getVendorNo()));
                }

            }
        }

    }

}