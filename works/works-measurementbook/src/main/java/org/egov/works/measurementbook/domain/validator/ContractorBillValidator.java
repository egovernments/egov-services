package org.egov.works.measurementbook.domain.validator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.tracer.model.CustomException;
import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.commons.utils.CommonUtils;
import org.egov.works.measurementbook.config.Constants;
import org.egov.works.measurementbook.domain.repository.BillRegisterRepository;
import org.egov.works.measurementbook.domain.repository.ContractorBillRepository;
import org.egov.works.measurementbook.domain.repository.LetterOfAcceptanceRepository;
import org.egov.works.measurementbook.domain.repository.MeasurementBookRepository;
import org.egov.works.measurementbook.domain.service.ContractorBillService;
import org.egov.works.measurementbook.utils.MeasurementBookUtils;
import org.egov.works.measurementbook.web.contract.BillRegister;
import org.egov.works.measurementbook.web.contract.BillRegisterRequest;
import org.egov.works.measurementbook.web.contract.BillRegisterResponse;
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
import org.egov.works.measurementbook.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.minidev.json.JSONArray;

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
    
    @Autowired
    private MeasurementBookUtils measurementBookUtils;
    
    @Autowired
    private BillRegisterRepository billRegisterRepository;

    public void validateContractorBill(final ContractorBillRequest contractorBillRequest, Boolean isNew) {

        HashMap<String, String> messages = new HashMap<>();
        
        if (contractorBillRequest.getContractorBills().size() > 1) {
            validateDuplicateBillNumber(messages, contractorBillRequest);
        }
        
        if(isNew)
            createUpdateBillRegister(contractorBillRequest);
        
        // Validate duplicate bill numbers with in request
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
                if(contractorBill.getMbForContractorBill() != null && contractorBill.getMbForContractorBill().isEmpty())
                    messages.put(Constants.KEY_BILL_MB_DETAILS_REQUIRED, Constants.MSG_BILL_MB_DETAILS_REQUIRED);
                 else
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
                        && contractorBill.getWorkCompletionDate() == null) {
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

                // Validate for Part Rate MB's. Max two bill's allowed for Part
                // Rate Mb's
                validatePartRateMBs(contractorBill, messages, contractorBillRequest.getRequestInfo());

            }
            if(contractorBill.getId() != null)
                validateUpdateStatus(contractorBill, contractorBillRequest.getRequestInfo(), messages);
        }

        if (!messages.isEmpty())
            throw new CustomException(messages);
    }

    private void createUpdateBillRegister(final ContractorBillRequest contractorBillRequest) {
        BillRegisterRequest billRegisterRequest = new BillRegisterRequest();
        List<BillRegister> billRegisters = new ArrayList<>();
        BillRegister billRegister = null;
        for(ContractorBill bill : contractorBillRequest.getContractorBills()) {
            billRegister = prepareBillRegisterObject(bill);
            billRegisters.add(billRegister);
        }

        billRegisterRequest.setBillRegisters(billRegisters);
        billRegisterRequest.setRequestInfo(contractorBillRequest.getRequestInfo());
        BillRegisterResponse billRegisterResponse =  billRegisterRepository.createUpdateBillRegister(billRegisterRequest, false);
        
        for(BillRegister billRegister2: billRegisterResponse.getBillRegisters()) {
            for (final ContractorBill contractorBill : contractorBillRequest.getContractorBills()) {
                if(contractorBill.getBillNumber().equalsIgnoreCase(billRegister2.getBillNumber())) {
                    if(StringUtils.isBlank(contractorBill.getId()))
                            contractorBill.setId(new CommonUtils().getUUID());
                }
            }
        }
    }

    private BillRegister prepareBillRegisterObject(ContractorBill bill) {
        BillRegister billRegister;
        billRegister = new BillRegister();
        billRegister.setTenantId(bill.getTenantId());
        billRegister.setBillType(bill.getBillType());
        billRegister.setBillAmount(bill.getBillAmount());
        billRegister.setBillDate(bill.getBillDate());
        billRegister.setBillDetails(bill.getBillDetails());
        billRegister.setBillNumber(bill.getBillNumber());
        billRegister.setBillSubType(bill.getBillSubType());
        billRegister.setBudgetAppropriationNo(bill.getBudgetAppropriationNo());
        billRegister.budgetCheckRequired(billRegister.getBudgetCheckRequired());
        billRegister.setDepartment(billRegister.getDepartment());
        billRegister.setDescription(bill.getDescription());
        billRegister.setDivision(bill.getDivision());
        billRegister.setFunction(bill.getFunction());
        billRegister.setFunctionary(bill.getFunctionary());
        billRegister.setFund(bill.getFund());
        billRegister.setFundsource(bill.getFundsource());
        billRegister.setModuleName(bill.getModuleName());
        billRegister.setPartyBillDate(bill.getPartyBillDate());
        billRegister.setPartyBillNumber(bill.getPartyBillNumber());
        billRegister.setPassedAmount(bill.getPassedAmount());
        billRegister.setScheme(bill.getScheme());
        billRegister.setSourcePath(bill.getSourcePath());
        billRegister.setStatus(bill.getStatus());
        billRegister.setSubScheme(bill.getSubScheme());
        return billRegister;
    }
    
    private void validateUpdateStatus(ContractorBill contractorBill, RequestInfo requestInfo, Map<String, String> messages) {
            if(contractorBill.getId() != null) {
                List<ContractorBill> contractorBills = searchContractorBillById(contractorBill, requestInfo);
                List<String> fieldsNamesList = null;
                List<String> fieldsValuesList = null;
                if(contractorBills != null && !contractorBills.isEmpty()) {
                    String status = contractorBills.get(0).getStatus().getCode();
                    if (status.equals(Constants.STATUS_CANCELLED) || status.equals(Constants.STATUS_APPROVED)) {
                        messages.put(Constants.KEY_CB_CANNOT_UPDATE_STATUS, Constants.MSG_CB_CANNOT_UPDATE_STATUS);
                    } else if((status.equals(Constants.STATUS_REJECTED) && !contractorBill.getStatus().getCode().equals(Constants.STATUS_RESUBMITTED)) ||
                            (status.equals(Constants.STATUS_RESUBMITTED) && !(contractorBill.getStatus().toString().equals(Constants.STATUS_CHECKED) ||
                                    contractorBill.getStatus().getCode().equals(Constants.STATUS_CANCELLED)) )) {
                        messages.put(Constants.KEY_CB_INVALID_STATUS, Constants.MSG_CB_INVALID_STATUS);
                    } else if (!contractorBill.getStatus().getCode().equals(Constants.STATUS_REJECTED)) {
                        fieldsNamesList = new ArrayList<>(Arrays.asList(CommonConstants.CODE,CommonConstants.MODULE_TYPE));
                        fieldsValuesList = new ArrayList<>(Arrays.asList(contractorBill.getStatus().getCode().toUpperCase(), CommonConstants.CONTRACTORBILL));
                        JSONArray statusRequestArray = measurementBookUtils.getMDMSData(CommonConstants.WORKS_STATUS_APPCONFIG, fieldsNamesList,
                                fieldsValuesList, contractorBill.getTenantId(), requestInfo,
                                CommonConstants.MODULENAME_WORKS);
                        fieldsNamesList = new ArrayList<>(Arrays.asList(CommonConstants.CODE,CommonConstants.MODULE_TYPE));
                        fieldsValuesList = new ArrayList<>(Arrays.asList(status.toUpperCase(),CommonConstants.CONTRACTORBILL));
                        JSONArray dBStatusArray = measurementBookUtils.getMDMSData(CommonConstants.WORKS_STATUS_APPCONFIG, fieldsNamesList,
                                fieldsValuesList, contractorBill.getTenantId(), requestInfo,
                                CommonConstants.MODULENAME_WORKS);
                        if (statusRequestArray != null && !statusRequestArray.isEmpty() && dBStatusArray != null && !dBStatusArray.isEmpty()) {
                            Map<String, Object> jsonMapRequest = (Map<String, Object>) statusRequestArray.get(0);
                            Map<String, Object> jsonMapDB = (Map<String, Object>) dBStatusArray.get(0);
                            Integer requestStatusOrderNumber = (Integer) jsonMapRequest.get("orderNumber");
                            Integer dbtStatusOrderNumber = (Integer) jsonMapDB.get("orderNumber");
                            if (requestStatusOrderNumber - dbtStatusOrderNumber != 1) {
                                messages.put(Constants.KEY_CB_INVALID_STATUS, Constants.MSG_CB_INVALID_STATUS);
                            }
                        }
                    }
                }

            }
        }   

    private List<ContractorBill> searchContractorBillById(ContractorBill contractorBill, final RequestInfo requestInfo) {
        ContractorBillSearchContract contractorBillSearchContract = ContractorBillSearchContract.builder()
                .tenantId(contractorBill.getTenantId()).ids(Arrays.asList(contractorBill.getId())).build();
        return contractorBillRepository.searchContractorBills(contractorBillSearchContract,requestInfo);
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
                if (!contractorBill.getStatus().getCode().equals(Constants.BILL_CANCELLED_STATUS.toString()))
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
        if (measurementBooks != null && mbIds.size() != measurementBooks.size())
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
        measurementBookSearchContract.setStatuses(Arrays.asList(CommonConstants.STATUS_APPROVED));
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
        Boolean isPartRateMb = Boolean.FALSE;
        for (MeasurementBookForContractorBill mbContractorBill : contractorBill.getMbForContractorBill()) {
            mbIds.add(mbContractorBill.getId());
        }
        List<MeasurementBook> measurementBooks = searchMeasurementBookByIds(contractorBill, requestInfo, mbIds);
        if (measurementBooks != null && !measurementBooks.isEmpty()) {
            for (MeasurementBook measurementBook : measurementBooks) {
                for (MeasurementBookDetail detail : measurementBook.getMeasurementBookDetails()) {
                    if (detail != null & detail.getPartRate().compareTo(BigDecimal.ZERO) == 1) {
                        isPartRateMb = Boolean.TRUE;
                        break;
                    }
                }
                mbForContractorBillSearchContract = new MBForContractorBillSearchContract();
                mbForContractorBillSearchContract.setTenantId(contractorBill.getTenantId());
                mbForContractorBillSearchContract.setMeasurementBook(measurementBook.getId());
                contractorBills = contractorBillRepository.searchContractorBillsByMb(mbForContractorBillSearchContract,
                        requestInfo);
                if (isPartRateMb && contractorBills != null && contractorBills.size() == 2) {
                    messages.put(Constants.KEY_CB_PARTRATE_MB_MAX_EXISTS, Constants.MSG_CB_PARTRATE_MB_MAX_EXISTS);
                } else if (!isPartRateMb && contractorBills != null && !contractorBills.isEmpty()) {
                    messages.put(Constants.KEY_CB_REDUCEDRATE_MB_MAX_EXISTS,
                            Constants.MSG_CB_REDUCEDRATE_MB_MAX_EXISTS);
                }
            }
        }

    }

}
