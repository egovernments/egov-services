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
import org.egov.swm.domain.model.Boundary;
import org.egov.swm.domain.model.Document;
import org.egov.swm.domain.model.DocumentSearch;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.ServicedLocations;
import org.egov.swm.domain.model.ServicesOffered;
import org.egov.swm.domain.model.Supplier;
import org.egov.swm.domain.model.SwmProcess;
import org.egov.swm.domain.model.Vendor;
import org.egov.swm.domain.model.VendorSearch;
import org.egov.swm.domain.repository.SupplierRepository;
import org.egov.swm.domain.service.BoundaryService;
import org.egov.swm.domain.service.SwmProcessService;
import org.egov.swm.persistence.entity.VendorEntity;
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
    public SupplierRepository supplierRepository;

    @Autowired
    public ServicedLocationsJdbcRepository servicedLocationsJdbcRepository;

    @Autowired
    public ServicesOfferedJdbcRepository servicesOfferedJdbcRepository;

    @Autowired
    private DocumentJdbcRepository documentJdbcRepository;

    @Autowired
    private SwmProcessService swmProcessService;

    @Autowired
    private BoundaryService boundaryService;

    public Boolean uniqueCheck(final String tenantId, final String fieldName, final String fieldValue,
            final String uniqueFieldName,
            final String uniqueFieldValue) {

        return uniqueCheck(TABLE_NAME, tenantId, fieldName, fieldValue, uniqueFieldName, uniqueFieldValue);
    }

    public Pagination<Vendor> search(final VendorSearch searchRequest) {

        String searchQuery = "select * from " + TABLE_NAME + " :condition  :orderby ";

        final Map<String, Object> paramValues = new HashMap<>();
        final StringBuffer params = new StringBuffer();

        if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
            validateSortByOrder(searchRequest.getSortBy());
            validateEntityFieldName(searchRequest.getSortBy(), VendorSearch.class);
        }

        String orderBy = "order by name";
        if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty())
            orderBy = "order by " + searchRequest.getSortBy();

        if (searchRequest.getVendorNo() != null) {
            addAnd(params);
            params.append("vendorNo in (:vendorNo)");
            paramValues.put("vendorNo", searchRequest.getVendorNo());
        }

        if (searchRequest.getVendorNos() != null) {
            addAnd(params);
            params.append("vendorNo in (:vendorNos)");
            paramValues.put("vendorNos", new ArrayList<>(Arrays.asList(searchRequest.getVendorNos().split(","))));
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

        if (searchRequest.getServices() != null) {

            ServicesOffered servicesOfferedSearch = ServicesOffered.builder().services(searchRequest.getServices())
                    .tenantId(searchRequest.getTenantId())
                    .build();
            List<ServicesOffered> servicesOffereds = servicesOfferedJdbcRepository.search(servicesOfferedSearch);
            List<String> vendorNos = servicesOffereds.stream()
                    .map(ServicesOffered::getVendor).distinct().collect(Collectors.toList());
            if (vendorNos != null && !vendorNos.isEmpty()) {
                addAnd(params);
                params.append("vendorNo in (:vendorNos)");
                paramValues.put("vendorNos", vendorNos);
            }

        }

        Pagination<Vendor> page = new Pagination<>();
        if (searchRequest.getOffset() != null)
            page.setOffset(searchRequest.getOffset());
        if (searchRequest.getPageSize() != null)
            page.setPageSize(searchRequest.getPageSize());

        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());
        else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        page = (Pagination<Vendor>) getPagination(searchQuery, page, paramValues);
        searchQuery = searchQuery + " :pagination";

        searchQuery = searchQuery.replace(":pagination",
                "limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

        final BeanPropertyRowMapper row = new BeanPropertyRowMapper(VendorEntity.class);

        final List<Vendor> vendorList = new ArrayList<>();
        LOG.info("Vendor search query  " + searchQuery + " and param values" + paramValues.toString());
        final List<VendorEntity> vendorEntities = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);

        StringBuffer vendorCodes = new StringBuffer();

        for (final VendorEntity vendorEntity : vendorEntities) {

            if (vendorCodes.length() >= 1)
                vendorCodes.append(",");

            vendorCodes.append(vendorEntity.getVendorNo());

            vendorList.add(vendorEntity.toDomain());

        }

        if (vendorList != null && !vendorList.isEmpty()) {

            populateServicedLocations(vendorList, vendorCodes.toString());

            populateSuppliers(vendorList);

            populateServicesOffered(vendorList, vendorCodes.toString());

            populateDocument(vendorList, vendorCodes.toString());
        }
        page.setPagedData(vendorList);

        return page;
    }

    private void populateDocument(List<Vendor> vendorList, String vendorCodes) {

        String tenantId = null;
        Map<String, Document> documentMap = new HashMap<>();

        if (vendorList != null && !vendorList.isEmpty())
            tenantId = vendorList.get(0).getTenantId();

        DocumentSearch search = new DocumentSearch();
        search.setTenantId(tenantId);
        search.setRefCodes(vendorCodes);

        List<Document> docs = documentJdbcRepository.search(search);

        if (docs != null)
            for (Document d : docs) {

                documentMap.put(d.getRefCode(), d);

            }

        for (Vendor vendor : vendorList) {

            vendor.setAgreementDocument(documentMap.get(vendor.getVendorNo()));

        }

    }

    private void populateSuppliers(List<Vendor> vendorList) {

        StringBuffer supplierCodes = new StringBuffer();
        Set<String> supplierCodesSet = new HashSet<>();
        Supplier supplierSearch = new Supplier();
        List<Supplier> suppliers;

        for (Vendor v : vendorList) {

            if (v.getSupplier() != null && v.getSupplier().getSupplierNo() != null
                    && !v.getSupplier().getSupplierNo().isEmpty()) {

                supplierCodesSet.add(v.getSupplier().getSupplierNo());

            }

        }

        List<String> supplierCodeList = new ArrayList(supplierCodesSet);

        for (String code : supplierCodeList) {

            if (supplierCodes.length() >= 1)
                supplierCodes.append(",");

            supplierCodes.append(code);

        }

        String tenantId = null;
        Map<String, Supplier> supplierMap = new HashMap<>();

        if (vendorList != null && !vendorList.isEmpty())
            tenantId = vendorList.get(0).getTenantId();

        supplierSearch.setTenantId(tenantId);
        supplierSearch.setSupplierNos(supplierCodes.toString());
        suppliers = supplierRepository.search(supplierSearch);

        if (suppliers != null)
            for (Supplier bd : suppliers) {

                supplierMap.put(bd.getSupplierNo(), bd);

            }

        for (Vendor vendor : vendorList) {

            if (vendor.getSupplier() != null && vendor.getSupplier().getSupplierNo() != null
                    && !vendor.getSupplier().getSupplierNo().isEmpty()) {

                vendor.setSupplier(supplierMap.get(vendor.getSupplier().getSupplierNo()));
            }

        }

    }

    private void populateServicedLocations(List<Vendor> vendorList, String vendorCodes) {
        Map<String, List<ServicedLocations>> servicedLocationsMap = new HashMap<>();
        Map<String, List<Boundary>> boundaryListMap = new HashMap<>();
        Map<String, Boundary> boundaryMap = new HashMap<>();
        String tenantId = null;
        ServicedLocations sls;
        sls = new ServicedLocations();

        if (vendorList != null && !vendorList.isEmpty())
            tenantId = vendorList.get(0).getTenantId();

        sls.setVendorNos(vendorCodes);
        sls.setTenantId(tenantId);

        List<ServicedLocations> servicedLocations = servicedLocationsJdbcRepository.search(sls);
        if (servicedLocations != null && !servicedLocations.isEmpty()) {

            List<Boundary> boundarys = boundaryService.getAll(tenantId, new RequestInfo());
            if (boundarys != null) {

                for (Boundary b : boundarys) {
                    boundaryMap.put(b.getCode(), b);
                }
            }
            List<Boundary> bList;
            List<ServicedLocations> cpdList;
            for (ServicedLocations sl : servicedLocations) {

                if (servicedLocationsMap.get(sl.getVendor()) == null) {

                    boundaryListMap.put(sl.getVendor(), Collections.singletonList(boundaryMap.get(sl.getLocation())));

                    servicedLocationsMap.put(sl.getVendor(), Collections.singletonList(sl));

                } else {

                    bList = new ArrayList<>(boundaryListMap.get(sl.getVendor()));

                    bList.add(boundaryMap.get(sl.getLocation()));

                    boundaryListMap.put(sl.getVendor(), bList);

                    cpdList = new ArrayList<>(servicedLocationsMap.get(sl.getVendor()));

                    cpdList.add(sl);

                    servicedLocationsMap.put(sl.getVendor(), cpdList);

                }
            }

            for (Vendor vendor : vendorList) {

                vendor.setServicedLocations(boundaryListMap.get(vendor.getVendorNo()));

            }
        }

    }

    private void populateServicesOffered(List<Vendor> vendorList, String vendorCodes) {
        Map<String, List<ServicesOffered>> servicesOfferedMap = new HashMap<>();
        Map<String, List<SwmProcess>> swmProcessListMap = new HashMap<>();
        Map<String, SwmProcess> swmProcessMap = new HashMap<>();
        String tenantId = null;
        ServicesOffered cpds;
        cpds = new ServicesOffered();

        if (vendorList != null && !vendorList.isEmpty())
            tenantId = vendorList.get(0).getTenantId();

        if (tenantId != null && !tenantId.isEmpty()) {

            cpds.setVendorNos(vendorCodes);
            cpds.setTenantId(tenantId);

            List<ServicesOffered> servicesOffered = servicesOfferedJdbcRepository.search(cpds);

            if (servicesOffered != null && !servicesOffered.isEmpty()) {

                List<SwmProcess> swmProcessList = swmProcessService.getAll(tenantId, new RequestInfo());

                for (SwmProcess sp : swmProcessList) {
                    swmProcessMap.put(sp.getCode(), sp);
                }
                List<SwmProcess> bList;
                List<ServicesOffered> cpdList;
                for (ServicesOffered cpd : servicesOffered) {

                    if (servicesOfferedMap.get(cpd.getVendor()) == null) {

                        swmProcessListMap.put(cpd.getVendor(), Collections.singletonList(swmProcessMap.get(cpd.getService())));

                        servicesOfferedMap.put(cpd.getVendor(), Collections.singletonList(cpd));

                    } else {

                        bList = new ArrayList<>(swmProcessListMap.get(cpd.getVendor()));

                        bList.add(swmProcessMap.get(cpd.getService()));

                        swmProcessListMap.put(cpd.getVendor(), bList);

                        cpdList = new ArrayList<>(servicesOfferedMap.get(cpd.getVendor()));

                        cpdList.add(cpd);

                        servicesOfferedMap.put(cpd.getVendor(), cpdList);

                    }
                }

                for (Vendor vendor : vendorList) {

                    vendor.setServicesOffered(swmProcessListMap.get(vendor.getVendorNo()));

                }

            }
        }
    }

}