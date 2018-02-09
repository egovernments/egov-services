package org.egov.swm.domain.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.SwmProcess;
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

    @Autowired
    private SwmProcessService swmProcessService;
    
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

        SwmProcess p;
        VendorSearch vendorSearch;
        Pagination<Vendor> vendors;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        for (final VendorContract vendorContract : vendorContractRequest.getVendorContracts()) {

            if (vendorContract.getVendor() != null && (vendorContract.getVendor().getVendorNo() == null
                    || vendorContract.getVendor().getVendorNo().isEmpty()))
                throw new CustomException("FuelType",
                        "The field Vendor number is Mandatory . It cannot be not be null or empty.Please provide correct value ");

            if (vendorContract.getServicesOffered() != null)
                for (final SwmProcess process : vendorContract.getServicesOffered()) {

                    if (process != null && (process.getCode() == null || process.getCode().isEmpty()))
                        throw new CustomException("ServicesOffered", "Code is missing in ServicesOffered");

                    // Validate Swm Process
                    if (process.getCode() != null) {

                        p = swmProcessService.getSwmProcess(vendorContract.getTenantId(), process.getCode(),
                                vendorContractRequest.getRequestInfo());

                        if (p != null) {
                            process.setTenantId(p.getTenantId());
                            process.setName(p.getName());
                        }

                    }
                }
            
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
                    throw new CustomException("ContractPeriodToDate ", "Contract Period To date shall be greater than Contract Period From date: "
                            + dateFormat.format(new Date(vendorContract.getContractPeriodTo())));
            
            if (vendorContract.getContractDate() != null)
                if (new Date()
                        .before(new Date(vendorContract.getContractDate())))
                    throw new CustomException("ContractDate ", "Contract date can not be future date: "
                            + dateFormat.format(new Date(vendorContract.getContractDate())));
            
            if (vendorContract.getContractPeriodFrom() != null && vendorContract.getContractDate() != null)
                if (new Date(vendorContract.getContractDate())
                        .after(new Date(vendorContract.getContractPeriodFrom())))
                    throw new CustomException("ContractPeriodFrom ", "Contract Period From date shall be equal to or greater than Contract date: "
                            + dateFormat.format(new Date(vendorContract.getContractPeriodFrom())));

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