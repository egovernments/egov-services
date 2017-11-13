package org.egov.swm.domain.service;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.domain.model.*;
import org.egov.swm.domain.repository.VendorPaymentDetailsRepository;
import org.egov.swm.web.contract.EmployeeResponse;
import org.egov.swm.web.repository.EmployeeRepository;
import org.egov.swm.web.repository.IdgenRepository;
import org.egov.swm.web.requests.VendorPaymentDetailsRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class VendorPaymentDetailsService {

    private VendorPaymentDetailsRepository vendorPaymentDetailsRepository;

    private VendorContractService vendorContractService;

    private EmployeeRepository employeeRepository;

    private IdgenRepository idgenRepository;

    private String idGenNameForPaymentNumberPath;

    public VendorPaymentDetailsService(VendorPaymentDetailsRepository vendorPaymentDetailsRepository,
                                       VendorContractService vendorContractService,
                                       EmployeeRepository employeeRepository,
                                       IdgenRepository idgenRepository,
                                       @Value("${egov.swm.vendor.paymentdetails.paymentno.idgen.name}") final String idGenNameForPaymentNumberPath) {
        this.vendorPaymentDetailsRepository = vendorPaymentDetailsRepository;
        this.vendorContractService = vendorContractService;
        this.employeeRepository = employeeRepository;
        this.idgenRepository = idgenRepository;
        this.idGenNameForPaymentNumberPath = idGenNameForPaymentNumberPath;
    }

    public VendorPaymentDetailsRequest create(VendorPaymentDetailsRequest vendorPaymentDetailsRequest){
        validate(vendorPaymentDetailsRequest);

        Long userId = null;
        if (vendorPaymentDetailsRequest.getRequestInfo() != null
                && vendorPaymentDetailsRequest.getRequestInfo().getUserInfo() != null
                && null != vendorPaymentDetailsRequest.getRequestInfo().getUserInfo().getId()) {
            userId = vendorPaymentDetailsRequest.getRequestInfo().getUserInfo().getId();
        }

        for(VendorPaymentDetails vendorPaymentDetails : vendorPaymentDetailsRequest.getVendorPaymentDetails()){
            setAuditDetails(vendorPaymentDetails, userId);
            vendorPaymentDetails.setPaymentNo(generatePaymentNumber(vendorPaymentDetails.getTenantId(),
                    vendorPaymentDetailsRequest.getRequestInfo()));
            prepareDocuments(vendorPaymentDetails);
        }
        return vendorPaymentDetailsRepository.create(vendorPaymentDetailsRequest);
    }

    private void prepareDocuments(VendorPaymentDetails vendorPaymentDetail) {
        if(vendorPaymentDetail.getDocuments() != null){
            List<Document> documentList =   vendorPaymentDetail.getDocuments().stream()
                    .filter(record -> record.getFileStoreId() != null)
                    .collect(Collectors.toList());

            vendorPaymentDetail.setDocuments(documentList);
        }
        vendorPaymentDetail.getDocuments().forEach(document -> setDocumentDetails(document, vendorPaymentDetail));
    }

    private void setDocumentDetails(Document document, VendorPaymentDetails vendorPaymentDetails){
        document.setId(UUID.randomUUID().toString().replace("-", ""));
        document.setTenantId(vendorPaymentDetails.getTenantId());
        document.setRefCode(vendorPaymentDetails.getPaymentNo());
        document.setAuditDetails(vendorPaymentDetails.getAuditDetails());
    }

    private void validate(VendorPaymentDetailsRequest vendorPaymentDetailsRequest){

        for(VendorPaymentDetails vendorPaymentDetail : vendorPaymentDetailsRequest.getVendorPaymentDetails()){

            //Validate for vendor contract
            if(vendorPaymentDetail.getVendorContract() != null &&
                    (vendorPaymentDetail.getVendorContract().getContractNo() == null ||
                     vendorPaymentDetail.getVendorContract().getContractNo().isEmpty()))
                throw new CustomException("Vehicle Contract", "Vehicle Contract Number required ");

            if(vendorPaymentDetail.getVendorContract() != null &&
                    vendorPaymentDetail.getVendorContract().getContractNo() != null){

                Pagination<VendorContract> vendorContractPage = getVendorContracts(vendorPaymentDetail);

                if(vendorContractPage == null || vendorContractPage.getPagedData() == null || vendorContractPage.getPagedData().isEmpty())
                    throw new CustomException("Vehicle Contract",
                            "Vehicle Contract Number required " + vendorPaymentDetail.getVendorContract().getContractNo());
                else
                    vendorPaymentDetail.setVendorContract(vendorContractPage.getPagedData().get(0));
            }

            //Validate for employee
            if(vendorPaymentDetail.getEmployee() != null && (vendorPaymentDetail.getEmployee().getCode() == null ||
            vendorPaymentDetail.getEmployee().getCode().isEmpty()))
                throw new CustomException("Employee", "Employee code required" + vendorPaymentDetail.getPaymentNo());

            if(vendorPaymentDetail.getEmployee() != null && vendorPaymentDetail.getEmployee().getCode() != null){

                EmployeeResponse employeeResponse = employeeRepository.getEmployeeByCode(vendorPaymentDetail.getEmployee().getCode(),
                         vendorPaymentDetail.getTenantId(), vendorPaymentDetailsRequest.getRequestInfo());

                if (employeeResponse == null || employeeResponse.getEmployees() == null
                        || employeeResponse.getEmployees().isEmpty()) {
                    throw new CustomException("Employee",
                            "Given Employee is invalid: " + vendorPaymentDetail.getEmployee().getCode());
                } else {
                    vendorPaymentDetail.setEmployee(employeeResponse.getEmployees().get(0));
                }
            }
        }

    }

    private Pagination<VendorContract> getVendorContracts(VendorPaymentDetails vendorPaymentDetail){
        VendorContractSearch vendorContractSearch = new VendorContractSearch();
        vendorContractSearch.setTenantId(vendorPaymentDetail.getTenantId());
        vendorContractSearch.setContractNo(vendorPaymentDetail.getVendorContract().getContractNo());

        return vendorContractService.search(vendorContractSearch);
    }

    private String generatePaymentNumber(String tenantId, RequestInfo requestInfo) {

        return idgenRepository.getIdGeneration(tenantId, requestInfo, idGenNameForPaymentNumberPath);
    }

    private void setAuditDetails(VendorPaymentDetails contract, Long userId) {

        if (contract.getAuditDetails() == null)
            contract.setAuditDetails(new AuditDetails());

        if (null == contract.getPaymentNo() || contract.getPaymentNo().isEmpty()) {
            contract.getAuditDetails().setCreatedBy(null != userId ? userId.toString() : null);
            contract.getAuditDetails().setCreatedTime(new Date().getTime());
        }

        contract.getAuditDetails().setLastModifiedBy(null != userId ? userId.toString() : null);
        contract.getAuditDetails().setLastModifiedTime(new Date().getTime());
    }
}
