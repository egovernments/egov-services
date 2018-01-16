package org.egov.inv.domain.service;

import org.egov.common.Constants;
import org.egov.common.DomainService;
import org.egov.common.Pagination;
import org.egov.common.exception.ErrorCode;
import org.egov.common.exception.InvalidDataException;
import org.egov.inv.model.*;
import org.egov.inv.persistence.repository.MaterialReceiptJdbcRepository;
import org.egov.inv.persistence.repository.SupplierBillAdvanceAdjusmentJdbcRepository;
import org.egov.inv.persistence.repository.SupplierBillJdbcRepository;
import org.egov.inv.persistence.repository.SupplierBillReceiptJdbcRepository;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SupplierBillService extends DomainService {

    @Value("${inv.supplierbill.save.topic}")
    private String saveTopic;

    @Value("${inv.supplierbill.save.key}")
    private String savekey;

    @Autowired
    private SupplierBillJdbcRepository supplierBillJdbcRepository;

    @Autowired
    private SupplierBillReceiptJdbcRepository supplierBillReceiptJdbcRepository;

    @Autowired
    private SupplierBillAdvanceAdjusmentJdbcRepository supplierBillAdvanceAdjusmentJdbcRepository;

    @Autowired
    private MaterialReceiptJdbcRepository materialReceiptJdbcRepository;

    @Autowired
    private SupplierAdvanceRequisitionService supplierAdvanceRequisitionService;

    public SupplierBillResponse create(SupplierBillRequest supplierBillRequest, String tenantId) {

        fetchRelated(supplierBillRequest, tenantId);

        List<SupplierBill> supplierBillList = supplierBillRequest.getSupplierBills();

        validate(supplierBillList, tenantId, Constants.ACTION_CREATE);

        AuditDetails auditDetails = getAuditDetails(supplierBillRequest.getRequestInfo(), Constants.ACTION_CREATE);

        for (SupplierBill supplierBill : supplierBillList) {
            //Set Supplier Bill Id
            supplierBill.setId(supplierBillJdbcRepository.getSequence("seq_supplierbill"));

            for (SupplierBillReceipt supplierBillReceipt : supplierBill.getSupplierBillReceipts()) {
                //Set Supplier Bill Receipt Id
                supplierBillReceipt.setId(supplierBillJdbcRepository.getSequence("seq_supplierbillreceipt"));
                supplierBillReceipt.setSupplierBill(supplierBill.getId());
                supplierBillReceipt.setAuditDetails(auditDetails);

            }

            for (SupplierBillAdvanceAdjustment supplierBillAdvanceAdjustment : supplierBill.getSupplierBillAdvanceAdjustments()) {
                //Set Supplier Bill Advance Adjustment Id
                supplierBillAdvanceAdjustment.setId(supplierBillJdbcRepository.getSequence("seq_supplierbilladvanceadjustment"));
                supplierBillAdvanceAdjustment.supplierBill(supplierBill.getId());

                List<SupplierAdvanceRequisition> supplierAdvanceRequisitions = createSupplierAdvanceRequisition(supplierBillAdvanceAdjustment).getSupplierAdvanceRequisitions();

                for (SupplierAdvanceRequisition supplierAdvanceRequisition : supplierAdvanceRequisitions) {
                    supplierBillAdvanceAdjustment.setSupplierAdvanceRequisition(supplierAdvanceRequisition);
                }
            }

            supplierBill.setAuditDetails(auditDetails);


        }

        kafkaQue.send(saveTopic, savekey, supplierBillRequest);

        SupplierBillResponse supplierBillResponse = new SupplierBillResponse();
        return supplierBillResponse.
                responseInfo(null)
                .supplierBills(supplierBillRequest.getSupplierBills());
    }


    public SupplierBillResponse update(SupplierBillRequest supplierBillRequest, String tenantId) {

        fetchRelated(supplierBillRequest, tenantId);

        List<SupplierBill> supplierBillList = supplierBillRequest.getSupplierBills();

        validate(supplierBillList, tenantId, Constants.ACTION_UPDATE);

        AuditDetails auditDetails = getAuditDetails(supplierBillRequest.getRequestInfo(), Constants.ACTION_UPDATE);

        for (SupplierBill supplierBill : supplierBillList) {

            for (SupplierBillReceipt supplierBillReceipt : supplierBill.getSupplierBillReceipts()) {
                supplierBillReceipt.setAuditDetails(auditDetails);
            }

            supplierBill.setAuditDetails(auditDetails);

        }

        kafkaQue.send(saveTopic, savekey, supplierBillRequest);

        SupplierBillResponse supplierBillResponse = new SupplierBillResponse();
        return supplierBillResponse.
                responseInfo(null)
                .supplierBills(supplierBillRequest.getSupplierBills());
    }

    public SupplierBillResponse search(SupplierBillSearch supplierBillSearch) {

        SupplierBillResponse supplierBillResponse = new SupplierBillResponse();

        Pagination<SupplierBill> supplierBills = supplierBillJdbcRepository.search(supplierBillSearch);

        if (supplierBills.getPagedData().size() > 0) {
            for (SupplierBill supplierBill : supplierBills.getPagedData()) {

                //fetch supplier bill receipts
                SupplierBillReceiptSearch supplierBillReceiptSearch = SupplierBillReceiptSearch.builder()
                        .supplierBill(supplierBill.getId())
                        .build();

                Pagination<SupplierBillReceipt> billReceiptPagination = supplierBillReceiptJdbcRepository.search(supplierBillReceiptSearch);

                if (billReceiptPagination.getPagedData().size() > 0) {
                    supplierBill.setSupplierBillReceipts(billReceiptPagination.getPagedData());
                } else {
                    supplierBill.setSupplierBillReceipts(Collections.EMPTY_LIST);
                }

                //fetch supplier bill advance adjustments
                SupplierBillAdvanceAdjustmentSearch supplierBillAdvanceAdjustmentSearch = SupplierBillAdvanceAdjustmentSearch.builder()
                        .supplierBill(supplierBill.getId())
                        .build();

                Pagination<SupplierBillAdvanceAdjustment> advanceAdjustmentPagination = supplierBillAdvanceAdjusmentJdbcRepository.search(supplierBillAdvanceAdjustmentSearch);

                if (advanceAdjustmentPagination.getPagedData().size() > 0) {

                    supplierBill.setSupplierBillAdvanceAdjustments(advanceAdjustmentPagination.getPagedData());

                } else {
                    supplierBill.setSupplierBillAdvanceAdjustments(Collections.EMPTY_LIST);
                }

            }
            return supplierBillResponse.
                    responseInfo(null)
                    .supplierBills(supplierBills.getPagedData());

        } else return supplierBillResponse.
                responseInfo(null)
                .supplierBills(Collections.EMPTY_LIST);
    }

    private void validate(List<SupplierBill> supplierBills, String tenantId, String method) {
        InvalidDataException errors = new InvalidDataException();
        int i = 0;
        try {
            switch (method) {

                case Constants.ACTION_CREATE: {
                    if (supplierBills == null) {
                        throw new InvalidDataException("Supplier Bill", ErrorCode.NOT_NULL.getCode(), null);
                    }
                }
                break;

                case Constants.ACTION_UPDATE: {
                    if (supplierBills == null) {
                        throw new InvalidDataException("Supplier Bill", ErrorCode.NOT_NULL.getCode(), null);
                    }
                }

                break;
            }

            validateSameStoreSB(supplierBills, errors, i);

            for (SupplierBill supplierBill : supplierBills) {

            }

        } catch (IllegalArgumentException e) {

        }

        if (errors.getValidationErrors().size() > 0) {
            throw errors;
        }
    }

    private void validateSameStoreSB(List<SupplierBill> supplierBills, InvalidDataException errors, int i) {
        for (SupplierBill supplierBill : supplierBills) {

            long storeDistinctCount = supplierBill.getSupplierBillReceipts().
                    stream()
                    .map(supplierBillReceipt ->
                            supplierBillReceipt.getMaterialReceipt().getReceivingStore().getCode())
                    .distinct()
                    .count();

            if (storeDistinctCount > 1) {
                errors.addDataError(ErrorCode.OBJECT_DOESNT_BELONG.getCode(), "Material Receipts", "store");
            }

            i++;
        }
    }

    private void fetchRelated(SupplierBillRequest supplierBillRequest, String tenantId) {

        List<SupplierBill> supplierBills = supplierBillRequest.getSupplierBills();

        for (SupplierBill supplierBill : supplierBills) {
            for (SupplierBillReceipt supplierBillReceipt : supplierBill.getSupplierBillReceipts()) {
                MaterialReceiptSearch materialReceiptSearch = MaterialReceiptSearch.builder()
                        .mrnNumber(Collections.singletonList(supplierBillReceipt.getMaterialReceipt().getMrnNumber()))
                        .tenantId(tenantId)
                        .receiptType(Collections.singletonList(MaterialReceipt.ReceiptTypeEnum.PURCHASE_RECEIPT.toString()))
                        .mrnStatus(Collections.singletonList(MaterialReceipt.MrnStatusEnum.APPROVED.toString()))
                        .build();

                Pagination<MaterialReceipt> receiptPagination = materialReceiptJdbcRepository.search(materialReceiptSearch);

                if (receiptPagination.getPagedData().size() > 0) {
                    supplierBillReceipt.setMaterialReceipt(receiptPagination.getPagedData().get(0));
                } else {
                    throw new CustomException("Material Receipt", "Material receipt " + supplierBillReceipt.getMaterialReceipt().getMrnNumber() + " is not found");
                }
            }
        }

    }

    private SupplierAdvanceRequisitionResponse createSupplierAdvanceRequisition(SupplierBillAdvanceAdjustment supplierBillAdvanceAdjustment) {
        SupplierAdvanceRequisitionRequest supplierAdvanceRequisitionRequest = new SupplierAdvanceRequisitionRequest();
        supplierAdvanceRequisitionRequest.setRequestInfo(new RequestInfo());
        supplierAdvanceRequisitionRequest.setSupplierAdvanceRequisitions(Collections.singletonList(supplierBillAdvanceAdjustment.getSupplierAdvanceRequisition()));
        return supplierAdvanceRequisitionService.create(supplierAdvanceRequisitionRequest);
    }

}
