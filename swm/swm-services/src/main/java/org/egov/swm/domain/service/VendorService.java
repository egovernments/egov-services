package org.egov.swm.domain.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.Boundary;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.SwmProcess;
import org.egov.swm.domain.model.TenantBoundary;
import org.egov.swm.domain.model.Vendor;
import org.egov.swm.domain.model.VendorSearch;
import org.egov.swm.domain.repository.SupplierRepository;
import org.egov.swm.domain.repository.VendorRepository;
import org.egov.swm.web.repository.IdgenRepository;
import org.egov.swm.web.requests.VendorRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class VendorService {

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private IdgenRepository idgenRepository;

    @Value("${egov.swm.vendor.num.idgen.name}")
    private String idGenNameForVendorNumPath;

    @Value("${egov.swm.supplier.num.idgen.name}")
    private String idGenNameForSupplierNumPath;

    @Autowired
    private SwmProcessService swmProcessService;

    @Autowired
    private BoundaryService boundaryService;

    @Transactional
    public VendorRequest create(final VendorRequest vendorRequest) {

        validate(vendorRequest);

        Long userId = null;

        if (vendorRequest.getRequestInfo() != null && vendorRequest.getRequestInfo().getUserInfo() != null
                && null != vendorRequest.getRequestInfo().getUserInfo().getId())
            userId = vendorRequest.getRequestInfo().getUserInfo().getId();

        for (final Vendor v : vendorRequest.getVendors()) {

            setAuditDetails(v, userId);

            v.setVendorNo(generateVendorNumber(v.getTenantId(), vendorRequest.getRequestInfo()));

            if (v.getSupplier() != null) {
                v.getSupplier().setTenantId(v.getTenantId());
                v.getSupplier().setSupplierNo(
                        generateSupplierNumber(v.getSupplier().getTenantId(), vendorRequest.getRequestInfo()));
                v.getSupplier().setAuditDetails(v.getAuditDetails());
            }

            prepareAgreementDocument(v);

        }

        return vendorRepository.save(vendorRequest);

    }

    @Transactional
    public VendorRequest update(final VendorRequest vendorRequest) {

        Long userId = null;

        if (vendorRequest.getRequestInfo() != null && vendorRequest.getRequestInfo().getUserInfo() != null
                && null != vendorRequest.getRequestInfo().getUserInfo().getId())
            userId = vendorRequest.getRequestInfo().getUserInfo().getId();

        for (final Vendor v : vendorRequest.getVendors()) {

            setAuditDetails(v, userId);

            final VendorSearch vendorSearch = new VendorSearch();
            vendorSearch.setTenantId(v.getTenantId());
            vendorSearch.setVendorNo(v.getVendorNo());
            final Pagination<Vendor> vendorSearchResult = search(vendorSearch);

            if (vendorSearchResult != null && vendorSearchResult.getPagedData() != null
                    && !vendorSearchResult.getPagedData().isEmpty()) {
                v.getSupplier().setTenantId(v.getTenantId());
                v.getSupplier().setSupplierNo(vendorSearchResult.getPagedData().get(0).getSupplier().getSupplierNo());
            }

            prepareAgreementDocument(v);

        }

        validate(vendorRequest);

        return vendorRepository.update(vendorRequest);

    }

    private void validate(final VendorRequest vendorRequest) {

        SwmProcess p;
        TenantBoundary boundary;
        findDuplicatesInUniqueFields(vendorRequest);

        for (final Vendor vendor : vendorRequest.getVendors()) {

            if (vendor.getServicesOffered() != null)
                for (final SwmProcess process : vendor.getServicesOffered()) {

                    if (process != null && (process.getCode() == null || process.getCode().isEmpty()))
                        throw new CustomException("ServicesOffered", "Code is missing in ServicesOffered");

                    // Validate Swm Process
                    if (process.getCode() != null) {

                        p = swmProcessService.getSwmProcess(vendor.getTenantId(), process.getCode(),
                                vendorRequest.getRequestInfo());

                        if (p != null) {
                            process.setTenantId(p.getTenantId());
                            process.setName(p.getName());
                        }

                    }
                }

            if (vendor.getServicedLocations() != null)
                for (final Boundary location : vendor.getServicedLocations()) {

                    if (location != null && (location.getCode() == null || location.getCode().isEmpty()))
                        throw new CustomException("Location",
                                "The field Location Code is Mandatory . It cannot be not be null or empty.Please provide correct value ");

                    // Validate Location
                    if (location.getCode() != null) {

                        boundary = boundaryService.getByCode(vendor.getTenantId(), location.getCode(), new RequestInfo());

                        /*
                         * if (boundary != null && boundary.getBoundary() != null)
                         * location.builder().id(boundary.getBoundary().getId()).name(boundary.getBoundary().getName())
                         * .boundaryNum(String.valueOf(boundary.getBoundary().getBoundaryNum()))
                         * .code(boundary.getBoundary().getCode()) .build(); else throw new CustomException("Location",
                         * "Given Location is Invalid: " + location.getCode());
                         */

                        if (boundary == null || boundary.getBoundary() == null || boundary.getBoundary().getCode() == null
                                || boundary.getBoundary().getCode().isEmpty())
                            throw new CustomException("Location",
                                    "Given Location is Invalid: " + location.getCode());

                    }
                }

            validateUniqueFields(vendor);

        }
    }

    private void findDuplicatesInUniqueFields(final VendorRequest vendorRequest) {

        final Map<String, String> regNumberMap = new HashMap<>();
        final Map<String, String> gstMap = new HashMap<>();

        for (final Vendor vendor : vendorRequest.getVendors()) {

            if (vendor.getRegistrationNo() != null) {
                if (regNumberMap.get(vendor.getRegistrationNo()) != null)
                    throw new CustomException("registrationNo",
                            "Duplicate registration numbers in given vendors : " + vendor.getRegistrationNo());

                regNumberMap.put(vendor.getRegistrationNo(), vendor.getRegistrationNo());
            }

            if (vendor.getSupplier() != null)
                if (vendor.getSupplier().getGst() != null && !vendor.getSupplier().getGst().isEmpty()) {

                    if (gstMap.get(vendor.getSupplier().getGst()) != null)
                        throw new CustomException("gst",
                                "Duplicate gst's in given vendors : " + vendor.getSupplier().getGst());

                    gstMap.put(vendor.getSupplier().getGst(), vendor.getSupplier().getGst());
                }

        }

    }

    private void validateUniqueFields(final Vendor vendor) {

        if (vendor.getRegistrationNo() != null)
            if (!vendorRepository.uniqueCheck(vendor.getTenantId(), "registrationNo", vendor.getRegistrationNo(),
                    "vendorNo", vendor.getVendorNo()))
                throw new CustomException("registrationNo",
                        "The field registrationNo must be unique in the system The  value " + vendor.getRegistrationNo()
                                + " for the field registrationNo already exists in the system. Please provide different value ");

        if (vendor.getSupplier() != null)
            if (vendor.getSupplier().getGst() != null && !vendor.getSupplier().getGst().isEmpty())
                if (!supplierRepository.uniqueCheck(vendor.getTenantId(), "gst", vendor.getSupplier().getGst(),
                        "supplierNo", vendor.getSupplier().getSupplierNo()))
                    throw new CustomException("gst", "The field gst must be unique in the system The  value "
                            + vendor.getSupplier().getGst()
                            + " for the field gst already exists in the system. Please provide different value ");

    }

    private void prepareAgreementDocument(final Vendor v) {

        if (v.getAgreementDocument() != null && v.getAgreementDocument().getFileStoreId() != null) {
            v.getAgreementDocument().setId(UUID.randomUUID().toString().replace("-", ""));
            v.getAgreementDocument().setTenantId(v.getTenantId());
            v.getAgreementDocument().setRefCode(v.getVendorNo());
            v.getAgreementDocument().setAuditDetails(v.getAuditDetails());
        } else
            v.setAgreementDocument(null);
    }

    private String generateVendorNumber(final String tenantId, final RequestInfo requestInfo) {

        return idgenRepository.getIdGeneration(tenantId, requestInfo, idGenNameForVendorNumPath);
    }

    private String generateSupplierNumber(final String tenantId, final RequestInfo requestInfo) {

        return idgenRepository.getIdGeneration(tenantId, requestInfo, idGenNameForSupplierNumPath);
    }

    public Pagination<Vendor> search(final VendorSearch vendorSearch) {

        return vendorRepository.search(vendorSearch);
    }

    private void setAuditDetails(final Vendor contract, final Long userId) {

        if (contract.getAuditDetails() == null)
            contract.setAuditDetails(new AuditDetails());

        if (null == contract.getVendorNo() || contract.getVendorNo().isEmpty()) {
            contract.getAuditDetails().setCreatedBy(null != userId ? userId.toString() : null);
            contract.getAuditDetails().setCreatedTime(new Date().getTime());
        }

        contract.getAuditDetails().setLastModifiedBy(null != userId ? userId.toString() : null);
        contract.getAuditDetails().setLastModifiedTime(new Date().getTime());
    }

}