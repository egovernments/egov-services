package org.egov.inv.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.Constants;
import org.egov.common.DomainService;
import org.egov.common.MdmsRepository;
import org.egov.common.Pagination;
import org.egov.common.exception.ErrorCode;
import org.egov.common.exception.InvalidDataException;
import org.egov.inv.model.*;
import org.egov.inv.persistence.entity.MaterialReceiptEntity;
import org.egov.inv.persistence.entity.PurchaseOrderDetailEntity;
import org.egov.inv.persistence.entity.PurchaseOrderEntity;
import org.egov.inv.persistence.repository.*;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.isEmpty;

@Service
public class SupplierBillService extends DomainService {

    public static final String NET_PAYABLE_GLCODE = "3501001";
    public static final String SUPPLIER_BILL = "Supplier Bill";
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
    private MaterialReceiptService materialReceiptService;

    @Autowired
    private SupplierAdvanceRequisitionService supplierAdvanceRequisitionService;

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    private PurchaseOrderDetailJdbcRepository purchaseOrderDetailJdbcRepository;

    @Autowired
    private BillRestRepository billRestRepository;

    @Autowired
    private MdmsRepository mdmsRepository;

    @Autowired
    private MaterialStoreMappingJdbcRepository materialStoreMappingJdbcRepository;

    @Autowired
    private MaterialTypeStoreJdbcRepository materialTypeStoreJdbcRepository;

    public SupplierBillResponse create(SupplierBillRequest supplierBillRequest, String tenantId) {

        fetchRelated(supplierBillRequest, tenantId);

        List<SupplierBill> supplierBillList = supplierBillRequest.getSupplierBills();

        validate(supplierBillList, tenantId, Constants.ACTION_CREATE);

        AuditDetails auditDetails = getAuditDetails(supplierBillRequest.getRequestInfo(), Constants.ACTION_CREATE);

        List<CoaAmountGroup> coaAmountGroups = new ArrayList<>();

        //fetch material from mdms
        HashMap<String, Material> materialFromMdms = getMaterialFromMdms(tenantId);

        for (SupplierBill supplierBill : supplierBillList) {

            supplierBill.setAuditDetails(auditDetails);

            //Set Supplier Bill Id
            supplierBill.setId(supplierBillJdbcRepository.getSequence("seq_supplierbill"));

            for (SupplierBillReceipt supplierBillReceipt : supplierBill.getSupplierBillReceipts()) {
                //Set Supplier Bill Receipt Id
                supplierBillReceipt.setId(supplierBillJdbcRepository.getSequence("seq_supplierbillreceipt"));
                supplierBillReceipt.setSupplierBill(supplierBill.getId());
                supplierBillReceipt.setAuditDetails(auditDetails);
                backUpdatePo(tenantId, supplierBillReceipt.getMaterialReceipt());

                List<MaterialReceiptDetail> receiptDetails = supplierBillReceipt.getMaterialReceipt().getReceiptDetails();

                for (MaterialReceiptDetail materialReceiptDetail : receiptDetails) {

                    String materialCode = materialReceiptDetail.getMaterial().getCode();
                    String glCode = getGlCodeForMaterial(tenantId, materialFromMdms, supplierBillReceipt, materialCode);

                    //build glCode and amount object for supplier bill register
                    coaAmountGroups = buildGroupCoa(materialReceiptDetail.getAcceptedQty(), materialReceiptDetail.getUnitRate(), glCode);
                }
            }

            for (SupplierBillAdvanceAdjustment supplierBillAdvanceAdjustment : supplierBill.getSupplierBillAdvanceAdjustments()) {
                //Set Supplier Bill Advance Adjustment Id
                supplierBillAdvanceAdjustment.setId(supplierBillJdbcRepository.getSequence("seq_supplierbilladvanceadjustment"));
                supplierBillAdvanceAdjustment.supplierBill(supplierBill.getId());

                List<SupplierAdvanceRequisition> supplierAdvanceRequisitions = supplierAdvanceRequisitionService.create(buildSupplierAdvanceRequisition(supplierBillAdvanceAdjustment)).getSupplierAdvanceRequisitions();

                for (SupplierAdvanceRequisition supplierAdvanceRequisition : supplierAdvanceRequisitions) {
                    supplierBillAdvanceAdjustment.setSupplierAdvanceRequisition(supplierAdvanceRequisition);
                }
            }

            //Group chart of account and sum amount
            Map<String, BigDecimal> amount = coaAmountGroups.stream()
                    .collect(Collectors.groupingBy(coa -> coa.getChartofAccount(),
                            Collectors.reducing(BigDecimal.ZERO, CoaAmountGroup::getAmount,
                                    BigDecimal::add)));

            //build bill register object
            BillRegister billRegister = buildBillRegister(tenantId, supplierBill.getBillDetails(), amount);

            BillRegisterResponse billRegisterResponse = createBill(billRegister);

            //set bill register number to supplier bill
            for (BillRegister register : billRegisterResponse.getBillRegisters()) {
                supplierBill.billId(register.getBillNumber());
            }

        }

        kafkaQue.send(saveTopic, savekey, supplierBillRequest);

        SupplierBillResponse supplierBillResponse = new SupplierBillResponse();
        return supplierBillResponse.
                responseInfo(null)
                .supplierBills(supplierBillRequest.getSupplierBills());
    }


    private BillRegisterResponse createBill(BillRegister billRegister) {

        return billRestRepository.createBillRegister(Collections.singletonList(billRegister));
    }

    private BillRegister buildBillRegister(String tenantId, List<BillDetail> billDetails, Map<String, BigDecimal> coaAmount) {
        BigDecimal billAmount = BigDecimal.ZERO;
        Iterator iterator = coaAmount.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry pair = (Map.Entry) iterator.next();
            String glCode = pair.getKey().toString();
            BigDecimal amount = (BigDecimal) pair.getValue();

            BillDetail billDetail = BillDetail.builder().
                    chartOfAccount(new ChartOfAccount()
                            .glcode(glCode))
                    .debitAmount(amount)
                    .creditAmount(BigDecimal.ZERO)
                    .build();

            billAmount = billAmount.add(amount);

            billDetails.add(billDetail);
        }

        BigDecimal debitAmount = BigDecimal.ZERO;
        BigDecimal creditAmount = BigDecimal.ZERO;
        for (BillDetail billDetail : billDetails) {
            debitAmount = debitAmount.add(billDetail.getDebitAmount());
            creditAmount = creditAmount.add(billDetail.getCreditAmount());
        }

        //Net Payable
        BillDetail detail = BillDetail.builder()
                .chartOfAccount(new ChartOfAccount()
                        .glcode(NET_PAYABLE_GLCODE))
                .debitAmount(BigDecimal.ZERO)
                .creditAmount(debitAmount.subtract(creditAmount))
                .build();

        billDetails.add(detail);

        double billAmountInDouble = billAmount.doubleValue();

        return BillRegister.builder()
                .billDetails(billDetails)
                .tenantId(tenantId)
                .billDate(getCurrentDate())
                .billAmount(billAmountInDouble)
                .billType(SUPPLIER_BILL)
                .passedAmount(billAmountInDouble)
                .build();
    }


    public SupplierBillResponse update(SupplierBillRequest supplierBillRequest, String tenantId) {

        fetchRelated(supplierBillRequest, tenantId);

        List<SupplierBill> supplierBillList = supplierBillRequest.getSupplierBills();

        validate(supplierBillList, tenantId, Constants.ACTION_UPDATE);

        AuditDetails auditDetails = getAuditDetails(supplierBillRequest.getRequestInfo(), Constants.ACTION_UPDATE);

        for (SupplierBill supplierBill : supplierBillList) {

            for (SupplierBillReceipt supplierBillReceipt : supplierBill.getSupplierBillReceipts()) {
                supplierBillReceipt.setAuditDetails(auditDetails);
                if (supplierBill.getSupplierBillStatus().equalsIgnoreCase("CANCELLED")) {
                    backUpdateReceipt(supplierBillReceipt);
                    backUpdatePoCancelled(tenantId, supplierBillReceipt.getMaterialReceipt());
                } else {
                    backUpdatePo(tenantId, supplierBillReceipt.getMaterialReceipt());
                }
            }

            supplierBill.setAuditDetails(auditDetails);

            for (SupplierBillAdvanceAdjustment supplierBillAdvanceAdjustment : supplierBill.getSupplierBillAdvanceAdjustments()) {
                supplierBillAdvanceAdjustment.supplierBill(supplierBill.getId());

                List<SupplierAdvanceRequisition> supplierAdvanceRequisitions = supplierAdvanceRequisitionService.update(buildSupplierAdvanceRequisition(supplierBillAdvanceAdjustment)).getSupplierAdvanceRequisitions();

                for (SupplierAdvanceRequisition supplierAdvanceRequisition : supplierAdvanceRequisitions) {
                    supplierBillAdvanceAdjustment.setSupplierAdvanceRequisition(supplierAdvanceRequisition);
                }
            }

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
                //append tenantId
                if (!isEmpty(supplierBillReceipt.getTenantId())) {
                    supplierBillReceipt.setTenantId(tenantId);
                }

                //fetch material receipt
                MaterialReceiptSearch materialReceiptSearch = MaterialReceiptSearch.builder()
                        .mrnNumber(Collections.singletonList(supplierBillReceipt.getMaterialReceipt().getMrnNumber()))
                        .tenantId(tenantId)
                        .receiptType(Collections.singletonList(MaterialReceipt.ReceiptTypeEnum.PURCHASE_RECEIPT.toString()))
                        .mrnStatus(Collections.singletonList(MaterialReceipt.MrnStatusEnum.APPROVED.toString()))
                        .build();

                Pagination<MaterialReceipt> receiptPagination = materialReceiptService.search(materialReceiptSearch);

                if (receiptPagination.getPagedData().size() > 0) {
                    for (MaterialReceipt materialReceipt : receiptPagination.getPagedData()) {
                        supplierBillReceipt.setMaterialReceipt(materialReceipt);
                        for (MaterialReceiptDetail materialReceiptDetail : materialReceipt.getReceiptDetails()) {
                            //fetch purchase order details
                            PurchaseOrderDetailEntity purchaseOrderDetailEntity = new PurchaseOrderDetailEntity();
                            purchaseOrderDetailEntity.setId(materialReceiptDetail.getPurchaseOrderDetail().getId());
                            purchaseOrderDetailEntity.setTenantId(tenantId);
                            PurchaseOrderDetailEntity orderDetailEntity = purchaseOrderDetailJdbcRepository.findById(purchaseOrderDetailEntity);
                            if (orderDetailEntity != null) {
                                materialReceiptDetail.setPurchaseOrderDetail(orderDetailEntity.toDomain());
                            } else {
                                throw new CustomException("Purchase Order ", String.format("%s is not found", materialReceiptDetail.getPurchaseOrderDetail().getPurchaseOrderNumber()));
                            }
                        }
                    }
                } else {
                    throw new CustomException("Material Receipt", "Material receipt " + supplierBillReceipt.getMaterialReceipt().getMrnNumber() + " is not found");
                }
            }

        }

    }

    private SupplierAdvanceRequisitionRequest buildSupplierAdvanceRequisition(SupplierBillAdvanceAdjustment supplierBillAdvanceAdjustment) {
        SupplierAdvanceRequisitionRequest supplierAdvanceRequisitionRequest = new SupplierAdvanceRequisitionRequest();
        supplierAdvanceRequisitionRequest.setRequestInfo(new RequestInfo());
        supplierAdvanceRequisitionRequest.setSupplierAdvanceRequisitions(Collections.singletonList(supplierBillAdvanceAdjustment.getSupplierAdvanceRequisition()));
        return supplierAdvanceRequisitionRequest;
    }

    private void backUpdatePo(String tenantId, MaterialReceipt materialReceipt) {
        for (MaterialReceiptDetail materialReceiptDetail : materialReceipt.getReceiptDetails()) {

            PurchaseOrderSearch purchaseOrderSearch = new PurchaseOrderSearch();
            purchaseOrderSearch.setPurchaseOrderNumber(materialReceiptDetail.getPurchaseOrderDetail().getPurchaseOrderNumber());
            purchaseOrderSearch.setTenantId(tenantId);
            purchaseOrderSearch.setSearchPoAdvReq(false);
            if (purchaseOrderService.checkAllItemsSuppliedForPo(purchaseOrderSearch))
                supplierBillJdbcRepository.updateColumn(new PurchaseOrderEntity(), "purchaseorder", new HashMap<>(), "status = 'Fulfilled and Paid'"
                        + " where purchaseordernumber = '" + materialReceiptDetail.getPurchaseOrderDetail().getPurchaseOrderNumber() + "' and tenantid = '" + tenantId + "'");
        }
    }


    private void backUpdatePoCancelled(String tenantId, MaterialReceipt materialReceipt) {
        for (MaterialReceiptDetail materialReceiptDetail : materialReceipt.getReceiptDetails()) {
            supplierBillJdbcRepository.updateColumn(new PurchaseOrderEntity(), "purchaseorder", new HashMap<>(), "status = 'Rejected'"
                    + " where purchaseordernumber = '" + materialReceiptDetail.getPurchaseOrderDetail().getPurchaseOrderNumber() + "' and tenantid = '" + tenantId + "'");
        }
    }

    private void backUpdateReceipt(SupplierBillReceipt supplierBillReceipt) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("supplierbillpaid", "false");
        hashMap.put("mrnstatus", "'CANCELED'");
        supplierBillJdbcRepository.updateColumn(new MaterialReceiptEntity().toEntity(supplierBillReceipt.getMaterialReceipt()), "materialreceipt", hashMap, null);
    }

    public List<CoaAmountGroup> getGroupedCoAAmounts(List<String> mrnNumbers, String tenantId) {

        List<CoaAmountGroup> groupedCOAAmounts = supplierBillJdbcRepository.getGroupedCOAAmounts(mrnNumbers, tenantId);

        return groupedCOAAmounts.size() > 0 ? groupedCOAAmounts : Collections.EMPTY_LIST;
    }

    public Long getCurrentDate() {
        return currentEpochWithoutTime();
    }


    private HashMap<String, Material> getMaterialFromMdms(String tenantId) {

        List<Object> objectList = mdmsRepository.fetchObjectList(tenantId, "inventory", "Material", null, null, Material.class);

        HashMap<String, Material> materialHashMap = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        if (objectList != null && objectList.size() > 0) {
            for (Object object : objectList) {
                Material material = mapper.convertValue(object, Material.class);
                materialHashMap.put(material.getCode(), material);
            }
        }
        return materialHashMap;
    }

    private List<CoaAmountGroup> buildGroupCoa(BigDecimal acceptedQty, BigDecimal unitRate, String glCode) {
        List<CoaAmountGroup> coaAmountGroups = new ArrayList<>();

        BigDecimal amount;

        amount = acceptedQty.multiply(unitRate);

        CoaAmountGroup coaAmountGroup = CoaAmountGroup.builder()
                .amount(amount)
                .chartofAccount(glCode)
                .build();
        coaAmountGroups.add(coaAmountGroup);

        return coaAmountGroups;
    }

    private String getGlCodeForMaterial(String tenantId, HashMap<String, Material> materialFromMdms, SupplierBillReceipt supplierBillReceipt, String materialCode) {
        Material material = materialFromMdms.get(materialCode);
        String glCode = null;

        if (null != material && (null == material.getExpenseAccount() ||
                (null != material.getExpenseAccount() && isEmpty(material.getExpenseAccount().getGlcode())))) {
            MaterialStoreMappingSearch storeMappingSearch = MaterialStoreMappingSearch.builder()
                    .material(materialCode)
                    .store(supplierBillReceipt.getMaterialReceipt().getReceivingStore().getCode())
                    .tenantId(tenantId)
                    .build();
            Pagination<MaterialStoreMapping> storeMappingPagination = materialStoreMappingJdbcRepository.search(storeMappingSearch);

            if (storeMappingPagination.getPagedData().size() > 0) {
                for (MaterialStoreMapping materialStoreMapping : storeMappingPagination.getPagedData()) {
                    glCode = materialStoreMapping.getChartofAccount().getGlcode();
                }
            } else {
                MaterialTypeStoreMappingSearch typeStoreMappingSearch = MaterialTypeStoreMappingSearch.builder()
                        .materialType(material.getMaterialType().getCode())
                        .store(supplierBillReceipt.getMaterialReceipt().getReceivingStore().getCode())
                        .tenantId(tenantId)
                        .build();
                Pagination<MaterialTypeStoreMapping> typeStoreMappingPagination = materialTypeStoreJdbcRepository.search(typeStoreMappingSearch);

                if (typeStoreMappingPagination.getPagedData().size() > 0) {
                    for (MaterialTypeStoreMapping materialTypeStoreMapping : typeStoreMappingPagination.getPagedData()) {
                        glCode = materialTypeStoreMapping.getChartofAccount().getGlcode();
                    }
                } else {
                    throw new CustomException("Material", "Chart of account not defined for material");
                }
            }
        } else {
            if (null != material && null != material.getExpenseAccount()
                    && !isEmpty(material.getExpenseAccount().getGlcode())) {
                glCode = material.getExpenseAccount().getGlcode();
            } else {
                throw new CustomException("Material", "Material not found for material code : " + materialCode);
            }
        }
        return glCode;
    }
}
