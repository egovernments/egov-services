package org.egov.swm.domain.service;

import java.util.Date;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.Vendor;
import org.egov.swm.domain.model.VendorContract;
import org.egov.swm.domain.model.VendorContractSearch;
import org.egov.swm.domain.model.VendorSearch;
import org.egov.swm.domain.repository.VendorContractRepository;
import org.egov.swm.web.repository.IdgenRepository;
import org.egov.swm.web.requests.VendorContractRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class VendorContractService {

    @Autowired
    private VendorContractRepository vendorContractRepository;

    @Autowired
    private IdgenRepository idgenRepository;

    @Autowired
    private VendorService vendorService;

    @Value("${egov.swm.vendor.contract.num.idgen.name}")
    private String idGenNameForVendorContractNumPath;

    @Transactional
    public VendorContractRequest create(final VendorContractRequest vendorContractRequest) {

        validate(vendorContractRequest);

        Long userId = null;

        if (vendorContractRequest.getRequestInfo() != null
                && vendorContractRequest.getRequestInfo().getUserInfo() != null
                && null != vendorContractRequest.getRequestInfo().getUserInfo().getId())
            userId = vendorContractRequest.getRequestInfo().getUserInfo().getId();

        if (vendorContractRequest.getVendorContracts() != null)
            for (final VendorContract vc : vendorContractRequest.getVendorContracts()) {

                setAuditDetails(vc, userId);

                vc.setContractNo(
                        generateVendorContractNumber(vc.getTenantId(), vendorContractRequest.getRequestInfo()));

            }

        return vendorContractRepository.save(vendorContractRequest);

    }

    @Transactional
    public VendorContractRequest update(final VendorContractRequest vendorContractRequest) {

        Long userId = null;

        if (vendorContractRequest.getRequestInfo() != null
                && vendorContractRequest.getRequestInfo().getUserInfo() != null
                && null != vendorContractRequest.getRequestInfo().getUserInfo().getId())
            userId = vendorContractRequest.getRequestInfo().getUserInfo().getId();

        if (vendorContractRequest.getVendorContracts() != null)
            for (final VendorContract vc : vendorContractRequest.getVendorContracts())
                setAuditDetails(vc, userId);

        validate(vendorContractRequest);

        return vendorContractRepository.update(vendorContractRequest);

    }

    private String generateVendorContractNumber(final String tenantId, final RequestInfo requestInfo) {

        return idgenRepository.getIdGeneration(tenantId, requestInfo, idGenNameForVendorContractNumPath);
    }

    private void validate(final VendorContractRequest vendorContractRequest) {

        VendorSearch vendorSearch;
        Pagination<Vendor> vendors;
        for (final VendorContract vendorContract : vendorContractRequest.getVendorContracts()) {

            if (vendorContract.getVendor() != null && (vendorContract.getVendor().getVendorNo() == null
                    || vendorContract.getVendor().getVendorNo().isEmpty()))
                throw new CustomException("FuelType",
                        "The field Vendor number is Mandatory . It cannot be not be null or empty.Please provide correct value ");

            if (vendorContract.getVendor() != null && vendorContract.getVendor().getVendorNo() != null) {
                vendorSearch = new VendorSearch();
                vendorSearch.setTenantId(vendorContract.getTenantId());
                vendorSearch.setVendorNo(vendorContract.getVendor().getVendorNo());
                vendors = vendorService.search(vendorSearch);
                if (vendors != null && vendors.getPagedData() != null && !vendors.getPagedData().isEmpty())
                    vendorContract.setVendor(vendors.getPagedData().get(0));
                else
                    throw new CustomException("Vendor",
                            "Given Vendor is invalid: " + vendorContract.getVendor().getVendorNo());
            }

            if (vendorContract.getContractPeriodFrom() != null && vendorContract.getContractPeriodTo() != null)
                if (new Date(vendorContract.getContractPeriodTo())
                        .before(new Date(vendorContract.getContractPeriodFrom())))
                    throw new CustomException("ContractPeriodToDate ", "Given Contract Period To Date is invalid: "
                            + new Date(vendorContract.getContractPeriodTo()));
            
            if (vendorContract.getContractDate() != null)
                if (new Date()
                        .before(new Date(vendorContract.getContractDate())))
                    throw new CustomException("ContractDate ", "Given Contract Date is invalid: "
                            + new Date(vendorContract.getContractDate()));
            
            if (vendorContract.getContractPeriodFrom() != null && vendorContract.getContractDate() != null)
                if (new Date(vendorContract.getContractDate())
                        .after(new Date(vendorContract.getContractPeriodFrom())))
                    throw new CustomException("ContractPeriodFrom ", "Given Contract Period From Date is invalid: "
                            + new Date(vendorContract.getContractPeriodFrom()));

        }

    }

    public Pagination<VendorContract> search(final VendorContractSearch vendorContractSearch) {

        return vendorContractRepository.search(vendorContractSearch);
    }

    private void setAuditDetails(final VendorContract vc, final Long userId) {

        if (vc.getAuditDetails() == null)
            vc.setAuditDetails(new AuditDetails());

        if (null == vc.getContractNo() || vc.getContractNo().isEmpty()) {
            vc.getAuditDetails().setCreatedBy(null != userId ? userId.toString() : null);
            vc.getAuditDetails().setCreatedTime(new Date().getTime());
        }

        vc.getAuditDetails().setLastModifiedBy(null != userId ? userId.toString() : null);
        vc.getAuditDetails().setLastModifiedTime(new Date().getTime());
    }

}