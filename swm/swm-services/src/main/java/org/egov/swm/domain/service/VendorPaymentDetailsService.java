package org.egov.swm.domain.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.domain.model.*;
import org.egov.swm.domain.repository.PaymentDetailsRepository;
import org.egov.swm.domain.repository.VendorPaymentDetailsRepository;
import org.egov.swm.web.contract.EmployeeResponse;
import org.egov.swm.web.repository.EmployeeRepository;
import org.egov.swm.web.repository.IdgenRepository;
import org.egov.swm.web.requests.VendorPaymentDetailsRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class VendorPaymentDetailsService {

    @Autowired
    private VendorPaymentDetailsRepository vendorPaymentDetailsRepository;

    @Autowired
    private VendorContractService vendorContractService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private IdgenRepository idgenRepository;

    @Value("${egov.swm.vendor.paymentdetails.paymentno.idgen.name}")
    private String idGenNameForPaymentNumberPath;

    @Autowired
    private PaymentDetailsRepository paymentDetailsRepository;

    public VendorPaymentDetailsRequest create(final VendorPaymentDetailsRequest vendorPaymentDetailsRequest) {
        validate(vendorPaymentDetailsRequest);

        Long userId = null;
        if (vendorPaymentDetailsRequest.getRequestInfo() != null
                && vendorPaymentDetailsRequest.getRequestInfo().getUserInfo() != null
                && null != vendorPaymentDetailsRequest.getRequestInfo().getUserInfo().getId())
            userId = vendorPaymentDetailsRequest.getRequestInfo().getUserInfo().getId();

        for (final VendorPaymentDetails vendorPaymentDetails : vendorPaymentDetailsRequest.getVendorPaymentDetails()) {
            setAuditDetails(vendorPaymentDetails, userId);
            vendorPaymentDetails.setPaymentNo(generatePaymentNumber(vendorPaymentDetails.getTenantId(),
                    vendorPaymentDetailsRequest.getRequestInfo()));
            prepareDocuments(vendorPaymentDetails);
        }
        return vendorPaymentDetailsRepository.create(vendorPaymentDetailsRequest);
    }

    public VendorPaymentDetailsRequest update(final VendorPaymentDetailsRequest vendorPaymentDetailsRequest) {

        Long userId = null;
        if (vendorPaymentDetailsRequest.getRequestInfo() != null
                && vendorPaymentDetailsRequest.getRequestInfo().getUserInfo() != null
                && null != vendorPaymentDetailsRequest.getRequestInfo().getUserInfo().getId())
            userId = vendorPaymentDetailsRequest.getRequestInfo().getUserInfo().getId();

        for (final VendorPaymentDetails vendorPaymentDetails : vendorPaymentDetailsRequest.getVendorPaymentDetails()) {
            setAuditDetails(vendorPaymentDetails, userId);
            prepareDocuments(vendorPaymentDetails);
        }

        validateForUniquePaymentInfoInRequest(vendorPaymentDetailsRequest);
        validate(vendorPaymentDetailsRequest);

        return vendorPaymentDetailsRepository.update(vendorPaymentDetailsRequest);
    }

    public Pagination<VendorPaymentDetails> search(final VendorPaymentDetailsSearch vendorPaymentDetailsSearch) {

        Pagination<VendorPaymentDetails> vendorPaymentDetailsPage = vendorPaymentDetailsRepository.search(vendorPaymentDetailsSearch);

        if(vendorPaymentDetailsPage.getPagedData() != null && !vendorPaymentDetailsPage.getPagedData().isEmpty())
            vendorPaymentDetailsPage.setPagedData(enrichWithPendingAmount(vendorPaymentDetailsPage.getPagedData(),
                    vendorPaymentDetailsSearch));

        return vendorPaymentDetailsPage;
    }

    private List<VendorPaymentDetails> enrichWithPendingAmount(List<VendorPaymentDetails> vendorPaymentDetailsList,
                                                               VendorPaymentDetailsSearch vendorPaymentDetailsSearch){

        String paymentNumbers = vendorPaymentDetailsList.stream().map(VendorPaymentDetails::getPaymentNo)
                                .collect(Collectors.joining(","));

        PaymentDetailsSearch paymentDetailsSearch = new PaymentDetailsSearch();
        paymentDetailsSearch.setTenantId(vendorPaymentDetailsSearch.getTenantId());
        paymentDetailsSearch.setPaymentNos(paymentNumbers);
        paymentDetailsSearch.setExcludeVendorPaymentDetails(true);

        Pagination<PaymentDetails> paymentDetailsPage =  paymentDetailsRepository.search(paymentDetailsSearch);

        HashMap<String, Double> amountMap = new HashMap<>();
        if(paymentDetailsPage.getPagedData() != null && !paymentDetailsPage.getPagedData().isEmpty()){
            //Build map of sum of amount for payment number
            for(String paymentNo : Arrays.asList(paymentNumbers.split(","))){
                Double sum = 0.0;
                sum = sum + paymentDetailsPage.getPagedData().stream()
                            .filter(paymentDetail -> paymentDetail.getVendorPaymentDetails().getPaymentNo().equals(paymentNo))
                            .mapToDouble(PaymentDetails::getAmount).sum();
                amountMap.put(paymentNo,sum);
            }
        }

        for(VendorPaymentDetails vendorPaymentDetail : vendorPaymentDetailsList){
            if(amountMap.get(vendorPaymentDetail.getPaymentNo()) != null){
                vendorPaymentDetail.setPaidAmount(amountMap.get(vendorPaymentDetail.getPaymentNo()));
                vendorPaymentDetail.setPendingAmount(vendorPaymentDetail.getVendorInvoiceAmount() -
                                                     amountMap.get(vendorPaymentDetail.getPaymentNo()));
            }else{
                vendorPaymentDetail.setPaidAmount(0.0);
                vendorPaymentDetail.setPendingAmount(vendorPaymentDetail.getVendorInvoiceAmount());
            }
        }

        return vendorPaymentDetailsList;
    }

    private void validateForUniquePaymentInfoInRequest(final VendorPaymentDetailsRequest vendorPaymentDetailsRequest) {

        final List<String> paymentNumbersList = vendorPaymentDetailsRequest.getVendorPaymentDetails().stream()
                .map(VendorPaymentDetails::getPaymentNo).collect(Collectors.toList());

        if (paymentNumbersList.size() != paymentNumbersList.stream().distinct().count())
            throw new CustomException("Payment No", "Duplicate paymentNo in given Vendor Payment Details:");
    }

    private void prepareDocuments(final VendorPaymentDetails vendorPaymentDetail) {
        if (vendorPaymentDetail.getDocuments() != null) {
            final List<Document> documentList = vendorPaymentDetail.getDocuments().stream()
                    .filter(record -> record.getFileStoreId() != null).collect(Collectors.toList());

            vendorPaymentDetail.setDocuments(documentList);

            vendorPaymentDetail.getDocuments().forEach(document -> setDocumentDetails(document, vendorPaymentDetail));
        }

    }

    private void setDocumentDetails(final Document document, final VendorPaymentDetails vendorPaymentDetails) {
        document.setId(UUID.randomUUID().toString().replace("-", ""));
        document.setTenantId(vendorPaymentDetails.getTenantId());
        document.setRefCode(vendorPaymentDetails.getPaymentNo());
        document.setAuditDetails(vendorPaymentDetails.getAuditDetails());
    }

    private void validate(final VendorPaymentDetailsRequest vendorPaymentDetailsRequest) {

        EmployeeResponse employeeResponse;
        Pagination<VendorContract> vendorContractPage;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        for (final VendorPaymentDetails vendorPaymentDetail : vendorPaymentDetailsRequest.getVendorPaymentDetails()) {

            // Validate for vendor contract
            if (vendorPaymentDetail.getVendorContract() != null
                    && (vendorPaymentDetail.getVendorContract().getContractNo() == null
                            || vendorPaymentDetail.getVendorContract().getContractNo().isEmpty()))
                throw new CustomException("Vendor Contract", "Vendor Contract Number required ");

            if (vendorPaymentDetail.getVendorContract() != null
                    && vendorPaymentDetail.getVendorContract().getContractNo() != null) {

                vendorContractPage = getVendorContracts(vendorPaymentDetail);

                if (vendorContractPage == null || vendorContractPage.getPagedData() == null
                        || vendorContractPage.getPagedData().isEmpty())
                    throw new CustomException("Vendor Contract", "Vendor Contract Number required "
                            + vendorPaymentDetail.getVendorContract().getContractNo());
                else
                    vendorPaymentDetail.setVendorContract(vendorContractPage.getPagedData().get(0));
            }

            if (vendorPaymentDetail.getEmployee() != null && vendorPaymentDetail.getEmployee().getCode() != null) {

                employeeResponse = employeeRepository.getEmployeeByCode(vendorPaymentDetail.getEmployee().getCode(),
                        vendorPaymentDetail.getTenantId(), vendorPaymentDetailsRequest.getRequestInfo());

                if (employeeResponse == null || employeeResponse.getEmployees() == null
                        || employeeResponse.getEmployees().isEmpty())
                    throw new CustomException("Employee",
                            "Given Employee is invalid: " + vendorPaymentDetail.getEmployee().getCode());
                else
                    vendorPaymentDetail.setEmployee(employeeResponse.getEmployees().get(0));
            }

            //Validation for toDate to be greater than fromDate
            if (vendorPaymentDetail.getFromDate() != null && vendorPaymentDetail.getToDate() != null)
                if (new Date(vendorPaymentDetail.getToDate())
                        .before(new Date(vendorPaymentDetail.getFromDate())))
                    throw new CustomException("ToDate ", "Vendor Payment To date shall be greater than Vendor Payment From date: "
                            + dateFormat.format(new Date(vendorPaymentDetail.getToDate())));

            // validation for duplicate service periods
            VendorPaymentDetailsSearch vendorPaymentDetailsSearch = new VendorPaymentDetailsSearch();
            vendorPaymentDetailsSearch.setTenantId(vendorPaymentDetail.getTenantId());
            vendorPaymentDetailsSearch.setFromDate(vendorPaymentDetail.getFromDate());
            vendorPaymentDetailsSearch.setContractNo(vendorPaymentDetail.getVendorContract().getContractNo());
            vendorPaymentDetailsSearch.setToDate(vendorPaymentDetail.getToDate());
            vendorPaymentDetailsSearch.setValidate(true);

            Pagination<VendorPaymentDetails> vendorPaymentDetailsPage = vendorPaymentDetailsRepository
                    .search(vendorPaymentDetailsSearch);

            // For update scenario check after removing record being updated from request.
            if (vendorPaymentDetail.getPaymentNo() != null && !vendorPaymentDetail.getPaymentNo().isEmpty()
                    && vendorPaymentDetailsPage != null && vendorPaymentDetailsPage.getPagedData() != null
                    && !vendorPaymentDetailsPage.getPagedData().isEmpty()) {

                vendorPaymentDetailsPage.getPagedData()
                        .removeIf(record -> (record.getPaymentNo().equalsIgnoreCase(vendorPaymentDetail.getPaymentNo())));
            }

            if (vendorPaymentDetailsPage != null && vendorPaymentDetailsPage.getPagedData() != null
                    && !vendorPaymentDetailsPage.getPagedData().isEmpty())
                throw new CustomException("VendorContractNo",
                        "Invoice period is overlapping with earlier records: "
                                + vendorPaymentDetail.getVendorContract().getContractNo());

            //validation for invoice period outside contract period
            if(vendorPaymentDetail.getFromDate() != null && vendorPaymentDetail.getToDate() != null &&
               vendorPaymentDetail.getVendorContract() != null){

                VendorContractSearch vendorContractSearch = new VendorContractSearch();
                vendorContractSearch.setTenantId(vendorPaymentDetail.getTenantId());
                vendorContractSearch.setContractNo(vendorPaymentDetail.getVendorContract().getContractNo());

                Pagination<VendorContract> vendorContracts = vendorContractService.search(vendorContractSearch);
                if(vendorContracts != null && !vendorContracts.getPagedData().isEmpty()){
                    VendorContract vendorContract = vendorContracts.getPagedData().get(0);
                    if(new Date(vendorPaymentDetail.getFromDate())
                            .before(new Date(vendorContract.getContractPeriodFrom())) ||
                            new Date(vendorPaymentDetail.getToDate())
                                    .after(new Date(vendorContract.getContractPeriodTo())))
                        throw new CustomException("Out Of Contract Period",
                                " Invalid invoice. Submitted invoice period (" +
                                        dateFormat.format(vendorPaymentDetail.getFromDate()) + " - " +
                                        dateFormat.format(vendorPaymentDetail.getToDate()) + ") is outside the contract period (" +
                                        dateFormat.format(vendorContract.getContractPeriodFrom()) + " - " +
                                        dateFormat.format(vendorContract.getContractPeriodTo()) + ").");
                }
            }
        }
    }

    private Pagination<VendorContract> getVendorContracts(final VendorPaymentDetails vendorPaymentDetail) {
        final VendorContractSearch vendorContractSearch = new VendorContractSearch();
        vendorContractSearch.setTenantId(vendorPaymentDetail.getTenantId());
        vendorContractSearch.setContractNo(vendorPaymentDetail.getVendorContract().getContractNo());

        return vendorContractService.search(vendorContractSearch);
    }

    private String generatePaymentNumber(final String tenantId, final RequestInfo requestInfo) {

        return idgenRepository.getIdGeneration(tenantId, requestInfo, idGenNameForPaymentNumberPath);
    }

    private void setAuditDetails(final VendorPaymentDetails contract, final Long userId) {

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
