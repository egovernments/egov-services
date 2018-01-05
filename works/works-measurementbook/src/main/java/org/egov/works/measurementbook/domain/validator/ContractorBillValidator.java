package org.egov.works.measurementbook.domain.validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.tracer.model.CustomException;
import org.egov.works.measurementbook.config.Constants;
import org.egov.works.measurementbook.domain.repository.ContractorBillRepository;
import org.egov.works.measurementbook.domain.repository.LetterOfAcceptanceRepository;
import org.egov.works.measurementbook.domain.repository.MeasurementBookRepository;
import org.egov.works.measurementbook.domain.service.ContractorBillService;
import org.egov.works.measurementbook.web.contract.ContractorBill;
import org.egov.works.measurementbook.web.contract.ContractorBillRequest;
import org.egov.works.measurementbook.web.contract.ContractorBillSearchContract;
import org.egov.works.measurementbook.web.contract.LetterOfAcceptance;
import org.egov.works.measurementbook.web.contract.LetterOfAcceptanceEstimate;
import org.egov.works.measurementbook.web.contract.LetterOfAcceptanceResponse;
import org.egov.works.measurementbook.web.contract.MBForContractorBillSearchContract;
import org.egov.works.measurementbook.web.contract.MeasurementBook;
import org.egov.works.measurementbook.web.contract.MeasurementBookDetail;
import org.egov.works.measurementbook.web.contract.MeasurementBookForContractorBill;
import org.egov.works.measurementbook.web.contract.MeasurementBookSearchContract;
import org.egov.works.measurementbook.web.contract.MeasurementBookStatus;
import org.egov.works.measurementbook.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContractorBillValidator {

    @Autowired
    private ContractorBillService contractorBillService;
    @Autowired
    private ContractorBillRepository contractorBillRepository;
    @Autowired
    private LetterOfAcceptanceRepository letterOfAcceptanceRepository;
    @Autowired
    private MeasurementBookRepository measurementBookRepository;

    public void validateContractorBill(final ContractorBillRequest contractorBillRequest, Boolean isNew) {
        HashMap<String, String> messages = new HashMap<>();
        // Validate duplicate bill numbers with in request
        if (contractorBillRequest.getContractorBills().size() > 1) {
            validateDuplicateBillNumber(messages, contractorBillRequest);
        }
        for (final ContractorBill contractorBill : contractorBillRequest.getContractorBills()) {
            if (isNew) {
                // For LOA exists or not
                LetterOfAcceptanceResponse letterOfAcceptanceResponse = letterOfAcceptanceRepository.searchLOAById(
                        Arrays.asList(contractorBill.getLetterOfAcceptanceEstimate().getLetterOfAcceptance()),
                        contractorBill.getTenantId(), contractorBillRequest.getRequestInfo());
                if (letterOfAcceptanceResponse.getLetterOfAcceptances().isEmpty()) {
                    messages.put(Constants.KEY_MB_LOA_DOES_NOT_EXIST, Constants.MSG_MB_LOA_DOES_NOT_EXIST);
                }
                // Check MB exists for given MBs
                validateMBExists(contractorBill, messages, contractorBillRequest.getRequestInfo());

                if (!messages.isEmpty())
                    throw new CustomException(messages);

                if (!letterOfAcceptanceResponse.getLetterOfAcceptances().isEmpty() && isNew) {
                    // Validate for Bill in Workflow
                    validateBillInWorkflow(contractorBill, letterOfAcceptanceResponse.getLetterOfAcceptances().get(0),
                            messages, contractorBillRequest.getRequestInfo());
                    // Only one final bill for Loa estimate and if final bill
                    // already created then don't allow to create any more bills
                    validateForFinalBill(contractorBill, letterOfAcceptanceResponse.getLetterOfAcceptances().get(0),
                            messages, contractorBillRequest.getRequestInfo());
                }
                // Work completion date is mandatory for final bill.
                if (contractorBill.getBillSubType() != null && !contractorBill.getBillSubType().isEmpty()
                        && contractorBill.getBillSubType().equalsIgnoreCase(Constants.BILL_SUB_TYPE_FINAL)
                        && contractorBill.getLetterOfAcceptanceEstimate().getWorkCompletionDate() == null) {
                    messages.put(Constants.KEY_CB_LOA_WORKCOMPLETION_DATE_NULL,
                            Constants.MSG_CB_LOA_WORKCOMPLETION_DATE_NULL);
                }

                Boolean loaEstimateExists = false;
                // For LOA estimate exists or not
                for (LetterOfAcceptance letterOfAcceptance : letterOfAcceptanceResponse.getLetterOfAcceptances()) {
                    for (LetterOfAcceptanceEstimate letterOfAcceptanceEstimate : letterOfAcceptance
                            .getLetterOfAcceptanceEstimates())
                        if (letterOfAcceptanceEstimate.getId()
                                .equalsIgnoreCase(contractorBill.getLetterOfAcceptanceEstimate().getId())) {
                            loaEstimateExists = true;
                            break;
                        }
                }
                if (!loaEstimateExists)
                    messages.put(Constants.KEY_CB_LOA_ESTIMATE_NOT_EXISTS, Constants.MSG_CB_LOA_ESTIMATE_NOT_EXISTS);

                // Validate for unique bill number
                if (StringUtils.isNotBlank(contractorBill.getBillNumber()))
                    validateUniqueBillNo(contractorBillRequest.getRequestInfo(), contractorBill, messages);

            }
        }

        if (!messages.isEmpty())
            throw new CustomException(messages);
    }

    private void validateBillInWorkflow(ContractorBill contractorBill, LetterOfAcceptance letterOfAcceptance,
            Map<String, String> messages, RequestInfo requestInfo) {
        List<ContractorBill> contractorBills = searchBillByLoaNumber(contractorBill, letterOfAcceptance, requestInfo);
        for (ContractorBill cb : contractorBills) {
            if (cb.getStatus() != null && !(cb.getStatus().equals(Constants.BILL_CANCELLED_STATUS)
                    || cb.getStatus().equals(Constants.BILL_APPROVED_STATUS)))
                messages.put(Constants.KEY_CB_IN_WORKFLOW, Constants.MSG_CB_IN_WORKFLOW);
        }
    }

    private void validateForFinalBill(ContractorBill contractorBill, LetterOfAcceptance letterOfAcceptance,
            Map<String, String> messages, RequestInfo requestInfo) {
        List<ContractorBill> contractorBills = searchBillByLoaNumber(contractorBill, letterOfAcceptance, requestInfo);
        for (ContractorBill cb : contractorBills) {
            if (cb.getStatus() != null && !(cb.getStatus().equals(Constants.BILL_CANCELLED_STATUS)
                    && cb.getBillType().equalsIgnoreCase(Constants.BILL_TYPE)
                    && cb.getBillSubType().equalsIgnoreCase(Constants.BILL_SUB_TYPE_FINAL)))
                messages.put(Constants.KEY_CB_TYPE_FINAL_EXISTS, Constants.MSG_CB_TYPE_FINAL_EXISTS);
        }
    }

    /**
     * @param contractorBill
     * @param letterOfAcceptance
     * @param requestInfo
     * @return
     */
    public List<ContractorBill> searchBillByLoaNumber(ContractorBill contractorBill,
            LetterOfAcceptance letterOfAcceptance, RequestInfo requestInfo) {
        ContractorBillSearchContract contractorBillSearchContract = new ContractorBillSearchContract();
        contractorBillSearchContract.setTenantId(contractorBill.getTenantId());
        contractorBillSearchContract.setLetterOfAcceptanceNumbers(Arrays.asList(letterOfAcceptance.getLoaNumber()));
        List<ContractorBill> contractorBills = contractorBillRepository
                .searchContractorBills(contractorBillSearchContract, requestInfo);
        return contractorBills;
    }

    private void validateDuplicateBillNumber(Map<String, String> messages,
            final ContractorBillRequest contractorBillRequest) {
        int size = contractorBillRequest.getContractorBills().size();
        boolean validBillNumbers = true;
        for (int i = 0; i <= size - 1; i++) {
            for (int j = i + 1; j <= size - 1; j++) {
                if (contractorBillRequest.getContractorBills().get(i).getBillNumber()
                        .equalsIgnoreCase(contractorBillRequest.getContractorBills().get(j).getBillNumber())) {
                    validBillNumbers = false;
                    break;
                }
            }
        }

        if (!validBillNumbers) {
            messages.put(Constants.KEY_DUPLICATE_CONTRACTORBILLNUMBER,
                    Constants.MESSAGE_DUPLICATE_CONTRACTORBILLNUMBER);
        }
    }

    private void validateUniqueBillNo(RequestInfo requestInfo, ContractorBill contractorBill,
            HashMap<String, String> messages) {

        ContractorBillSearchContract searchContract = new ContractorBillSearchContract();
        if (contractorBill.getId() != null)
            searchContract.setIds(Arrays.asList(contractorBill.getId()));
        searchContract.setBillNumbers(Arrays.asList(contractorBill.getBillNumber()));
        searchContract.setTenantId(contractorBill.getTenantId());
        List<ContractorBill> oldBill = contractorBillService.search(searchContract, requestInfo).getContractorBills();

        if (!oldBill.isEmpty())
            for (ContractorBill bill : oldBill) {
                // TODO : need to read bill status from mdms
                if (!contractorBill.getStatus().toString().equals(Constants.BILL_CANCELLED_STATUS.toString()))
                    messages.put(Constants.KEY_UNIQUE_CONTRACTORBILLNUMBER,
                            Constants.MESSAGE_UNIQUE_CONTRACTORBILLNUMBER);
            }
    }

    private void validateMBExists(ContractorBill contractorBill, Map<String, String> messages,
            RequestInfo requestInfo) {
        List<String> mbIds = new ArrayList<String>();
        for (MeasurementBookForContractorBill mbContractorBill : contractorBill.getMbForContractorBill()) {
            mbIds.add(mbContractorBill.getId());
        }
        List<MeasurementBook> measurementBooks = searchMeasurementBookByIds(contractorBill, requestInfo, mbIds);
        if (mbIds.size() != measurementBooks.size())
            messages.put(Constants.KEY_CB_MB_NOT_EXISTS, Constants.MSG_CB_MB_NOT_EXISTS);
    }

    /**
     * @param contractorBill
     * @param requestInfo
     * @param mbIds
     * @return
     */
    public List<MeasurementBook> searchMeasurementBookByIds(ContractorBill contractorBill, RequestInfo requestInfo,
            List<String> mbIds) {
        MeasurementBookSearchContract measurementBookSearchContract = new MeasurementBookSearchContract();
        measurementBookSearchContract.setTenantId(contractorBill.getTenantId());
        measurementBookSearchContract.setStatuses(Arrays.asList(MeasurementBookStatus.APPROVED.toString()));
        measurementBookSearchContract.setIds(mbIds);
        List<MeasurementBook> measurementBooks = measurementBookRepository
                .searchMeasurementBooks(measurementBookSearchContract, requestInfo);
        return measurementBooks;
    }

    private void validatePartRateMBs(ContractorBill contractorBill, Map<String, String> messages,
            RequestInfo requestInfo) {
        List<String> mbIds = new ArrayList<String>();
        List<ContractorBill> contractorBills = new ArrayList<ContractorBill>();
        MBForContractorBillSearchContract mbForContractorBillSearchContract;
        for (MeasurementBookForContractorBill mbContractorBill : contractorBill.getMbForContractorBill()) {
            mbIds.add(mbContractorBill.getId());
        }
        List<MeasurementBook> measurementBooks = searchMeasurementBookByIds(contractorBill, requestInfo, mbIds);
        if (measurementBooks != null && !measurementBooks.isEmpty()) {
            for (MeasurementBook measurementBook : measurementBooks) {
                mbForContractorBillSearchContract = new MBForContractorBillSearchContract();
                mbForContractorBillSearchContract.setTenantId(contractorBill.getTenantId());
                mbForContractorBillSearchContract.setMeasurementBook(measurementBook.getId()); 
                contractorBills = contractorBillRepository.searchContractorBillsByMb(mbForContractorBillSearchContract, requestInfo);
                for (MeasurementBookDetail detail : measurementBook.getMeasurementBookDetails()) {
                    
                }
            }
        }
        messages.put(Constants.KEY_CB_MB_NOT_EXISTS, Constants.MSG_CB_MB_NOT_EXISTS);
    }
    

}
