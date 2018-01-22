package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.domain.model.ContractServicesOffered;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.SwmProcess;
import org.egov.swm.domain.model.Vendor;
import org.egov.swm.domain.model.VendorContract;
import org.egov.swm.domain.model.VendorContractSearch;
import org.egov.swm.domain.model.VendorSearch;
import org.egov.swm.domain.service.SwmProcessService;
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

    @Autowired
    private SwmProcessService swmProcessService;

    @Autowired
    public VendorContractServicesOfferedJdbcRepository vendorContractServicesOfferedJdbcRepository;

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

        if (searchRequest.getServices() != null) {

            ContractServicesOffered servicesOfferedSearch = ContractServicesOffered.builder()
                    .services(searchRequest.getServices())
                    .tenantId(searchRequest.getTenantId())
                    .build();
            List<ContractServicesOffered> servicesOffereds = vendorContractServicesOfferedJdbcRepository
                    .search(servicesOfferedSearch);
            List<String> vendorContractNos = servicesOffereds.stream()
                    .map(ContractServicesOffered::getVendorcontract).distinct().collect(Collectors.toList());
            if (vendorContractNos != null && !vendorContractNos.isEmpty()) {
                addAnd(params);
                params.append("contractNo in(:vendorContractNos) ");
                paramValues.put("vendorContractNos", vendorContractNos);
            }

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

        StringBuffer vendorContractNos = new StringBuffer();

        for (final VendorContractEntity entity : vendorContractEntities) {

            vendorContractList.add(entity.toDomain());

            if (vendorContractNos.length() >= 1)
                vendorContractNos.append(",");

            vendorContractNos.append(entity.getContractNo());

        }

        if (vendorContractList != null && !vendorContractList.isEmpty()) {

            populateVendors(vendorContractList);

            populateServicesOffered(vendorContractList, vendorContractNos.toString());
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

    private void populateServicesOffered(List<VendorContract> vendorContracts, String vendorContractNos) {
        Map<String, List<ContractServicesOffered>> servicesOfferedMap = new HashMap<>();
        Map<String, List<SwmProcess>> swmProcessListMap = new HashMap<>();
        Map<String, SwmProcess> swmProcessMap = new HashMap<>();
        String tenantId = null;
        ContractServicesOffered cpds;
        cpds = new ContractServicesOffered();

        if (vendorContracts != null && !vendorContracts.isEmpty())
            tenantId = vendorContracts.get(0).getTenantId();

        if (tenantId != null && !tenantId.isEmpty()) {

            cpds.setVendorcontracts(vendorContractNos);
            cpds.setTenantId(tenantId);

            List<ContractServicesOffered> servicesOffered = vendorContractServicesOfferedJdbcRepository.search(cpds);

            if (servicesOffered != null && !servicesOffered.isEmpty()) {

                List<SwmProcess> swmProcessList = swmProcessService.getAll(tenantId, new RequestInfo());

                for (SwmProcess sp : swmProcessList) {
                    swmProcessMap.put(sp.getCode(), sp);
                }

                for (ContractServicesOffered cpd : servicesOffered) {

                    if (servicesOfferedMap.get(cpd.getVendorcontract()) == null) {

                        swmProcessListMap.put(cpd.getVendorcontract(),
                                Collections.singletonList(swmProcessMap.get(cpd.getService())));

                        servicesOfferedMap.put(cpd.getVendorcontract(), Collections.singletonList(cpd));

                    } else {

                        List<SwmProcess> bList = new ArrayList<>(swmProcessListMap.get(cpd.getVendorcontract()));

                        bList.add(swmProcessMap.get(cpd.getService()));

                        swmProcessListMap.put(cpd.getVendorcontract(), bList);

                        List<ContractServicesOffered> cpdList = new ArrayList<>(servicesOfferedMap.get(cpd.getVendorcontract()));

                        cpdList.add(cpd);

                        servicesOfferedMap.put(cpd.getVendorcontract(), cpdList);

                    }
                }

                for (VendorContract vendorContract : vendorContracts) {

                    vendorContract.setServicesOffered(swmProcessListMap.get(vendorContract.getContractNo()));

                }

            }
        }
    }

}