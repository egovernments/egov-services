package org.egov.inv.domain.service;

import org.egov.common.Constants;
import org.egov.common.DomainService;
import org.egov.common.MdmsRepository;
import org.egov.common.Pagination;
import org.egov.common.exception.ErrorCode;
import org.egov.common.exception.InvalidDataException;
import org.egov.inv.model.*;
import org.egov.inv.persistence.entity.PurchaseOrderDetailEntity;
import org.egov.inv.persistence.entity.PurchaseOrderEntity;
import org.egov.inv.persistence.repository.PurchaseOrderDetailJdbcRepository;
import org.egov.inv.persistence.repository.PurchaseOrderJdbcRepository;
import org.egov.inv.persistence.repository.ReceiptNoteRepository;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.util.StringUtils.isEmpty;

@Service
public class ReceiptNoteService extends DomainService {

    @Autowired
    private LogAwareKafkaTemplate<String, Object> logAwareKafkaTemplate;

    @Value("${inv.materialreceiptnote.save.topic}")
    private String createTopic;

    @Value("${inv.materialreceiptnote.update.topic}")
    private String updateTopic;

    @Value("${inv.materialreceiptnote.save.key}")
    private String createTopicKey;

    @Value("${inv.materialreceiptnote.update.topic}")
    private String updateTopicKey;

    @Value("${inv.purchaseorders.cancelreceipt.key}")
    private String cancelReceiptPOTopicKey;

    @Value("${inv.purchaseorders.cancelreceipt.topic}")
    private String cancelReceiptPOTopic;


    @Autowired
    private MaterialReceiptService materialReceiptService;

    @Autowired
    private ReceiptNoteRepository receiptNoteRepository;

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    private PurchaseOrderDetailService purchaseOrderDetailService;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private PurchaseOrderDetailJdbcRepository purchaseOrderDetailJdbcRepository;

    @Autowired
    private MdmsRepository mdmsRepository;

    @Autowired
    private PurchaseOrderJdbcRepository purchaseOrderJdbcRepository;


    public MaterialReceiptResponse create(MaterialReceiptRequest materialReceiptRequest, String tenantId) {
        List<MaterialReceipt> materialReceipts = materialReceiptRequest.getMaterialReceipt();

        validate(materialReceipts, tenantId, Constants.ACTION_CREATE);

        materialReceipts.forEach(materialReceipt ->
        {
            materialReceipt.setId(receiptNoteRepository.getSequence("seq_materialreceipt"));
            materialReceipt.setMrnNumber(appendString(materialReceipt));
            materialReceipt.setMrnStatus(MaterialReceipt.MrnStatusEnum.CREATED);
            materialReceipt.setAuditDetails(getAuditDetails(materialReceiptRequest.getRequestInfo(), tenantId));
            if (StringUtils.isEmpty(materialReceipt.getTenantId())) {
                materialReceipt.setTenantId(tenantId);
            }

            materialReceipt.getReceiptDetails().forEach(materialReceiptDetail -> {
                setMaterialDetails(tenantId, materialReceiptDetail);
            });
        });

        logAwareKafkaTemplate.send(createTopic, createTopicKey, materialReceiptRequest);

        MaterialReceiptResponse materialReceiptResponse = new MaterialReceiptResponse();

        return materialReceiptResponse.responseInfo(null)
                .materialReceipt(materialReceipts);
    }

    public MaterialReceiptResponse update(MaterialReceiptRequest materialReceiptRequest, String tenantId) {
        List<MaterialReceipt> materialReceipts = materialReceiptRequest.getMaterialReceipt();
        validate(materialReceipts, tenantId, Constants.ACTION_UPDATE);

        List<String> materialReceiptDetailIds = new ArrayList<>();
        List<String> materialReceiptDetailAddlnInfoIds = new ArrayList<>();
        materialReceipts.forEach(materialReceipt ->
        {
            if (StringUtils.isEmpty(materialReceipt.getTenantId())) {
                materialReceipt.setTenantId(tenantId);
            }

            materialReceipt.getReceiptDetails().forEach(materialReceiptDetail -> {
                if (isEmpty(materialReceiptDetail.getTenantId())) {
                    materialReceiptDetail.setTenantId(tenantId);
                }

                if (isEmpty(materialReceiptDetail.getId())) {
                    setMaterialDetails(tenantId, materialReceiptDetail);
                }

                materialReceiptDetailIds.add(materialReceiptDetail.getId());

                materialReceiptDetail.getReceiptDetailsAddnInfo().forEach(
                        materialReceiptDetailAddnlInfo -> {
                            materialReceiptDetailAddlnInfoIds.add(materialReceiptDetailAddnlInfo.getId());

                            if (isEmpty(materialReceiptDetailAddnlInfo.getTenantId())) {
                                materialReceiptDetailAddnlInfo.setTenantId(tenantId);
                            }
                        }
                );
                receiptNoteRepository.markDeleted(materialReceiptDetailAddlnInfoIds, tenantId, "materialreceiptdetailaddnlinfo", "receiptdetailid", materialReceiptDetail.getId());

                receiptNoteRepository.markDeleted(materialReceiptDetailIds, tenantId, "materialreceiptdetail", "mrnNumber", materialReceipt.getMrnNumber());

            });

            if (MaterialReceipt.MrnStatusEnum.CANCELED.toString().equalsIgnoreCase(materialReceipt.getMrnStatus().toString())) {
                for (MaterialReceiptDetail materialReceiptDetail : materialReceipt.getReceiptDetails()) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("receivedquantity", "receivedquantity - " + materialReceiptDetail.getAcceptedQty());
                    materialReceiptDetail.getPurchaseOrderDetail().setTenantId(tenantId);
                    receiptNoteRepository.updateColumn(new PurchaseOrderDetailEntity().toEntity(materialReceiptDetail.getPurchaseOrderDetail()), "purchaseorderdetail", hashMap, null);

                    receiptNoteRepository.updateColumn(new PurchaseOrderEntity(), "purchaseorder", new HashMap<>(), "status = (case when status = 'RECEIPTED' then 'APPROVED' ELSE status end)"
                            + " where purchaseordernumber = (select purchaseorder from purchaseorderdetail where id = '"
                            + materialReceiptDetail.getPurchaseOrderDetail().getId() + "') and tenantid = '" + tenantId + "'");
                }
            }

            if (MaterialReceipt.ReceiptTypeEnum.PURCHASE_RECEIPT.toString().equalsIgnoreCase(materialReceipt.getReceiptType().toString())) {
                for (MaterialReceiptDetail materialReceiptDetail : materialReceipt.getReceiptDetails()) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("acceptedquantity", "acceptedquantity + " + materialReceiptDetail.getAcceptedQty());
                    materialReceiptDetail.getPurchaseOrderDetail().setTenantId(tenantId);
                    receiptNoteRepository.updateColumn(new PurchaseOrderDetailEntity().toEntity(materialReceiptDetail.getPurchaseOrderDetail()), "purchaseorderdetail", hashMap, null);

                    PurchaseOrderDetailEntity purchaseOrderDetailEntity = new PurchaseOrderDetailEntity();
                    purchaseOrderDetailEntity.setId(materialReceiptDetail.getPurchaseOrderDetail().getId());
                    purchaseOrderDetailEntity.setTenantId(tenantId);
                    PurchaseOrderDetailEntity orderDetailEntity = purchaseOrderDetailJdbcRepository.findById(purchaseOrderDetailEntity);

                    PurchaseOrderSearch purchaseOrderSearch = new PurchaseOrderSearch();
                    purchaseOrderSearch.setPurchaseOrderNumber(orderDetailEntity.getPurchaseOrder());
                    purchaseOrderSearch.setTenantId(tenantId);
                    if (purchaseOrderService.checkAllItemsSuppliedForPo(purchaseOrderSearch))
                        receiptNoteRepository.updateColumn(new PurchaseOrderEntity(), "purchaseorder", new HashMap<>(), "status = (case when status = 'RECEIPTED' then 'APPROVED' ELSE status end)"
                                + " where purchaseordernumber = " + orderDetailEntity.getOrderNumber() + "') and tenantid = '" + tenantId + "'");
                    ;
                }
            }
        });

        logAwareKafkaTemplate.send(updateTopic, updateTopicKey, materialReceiptRequest);

        MaterialReceiptResponse materialReceiptResponse = new MaterialReceiptResponse();

        return materialReceiptResponse.responseInfo(null)
                .materialReceipt(materialReceipts);
    }


    public MaterialReceiptResponse search(MaterialReceiptSearch materialReceiptSearch) {
        Pagination<MaterialReceipt> materialReceiptPagination = materialReceiptService.search(materialReceiptSearch);
        MaterialReceiptResponse response = new MaterialReceiptResponse();
        return response
                .responseInfo(null)
                .materialReceipt(materialReceiptPagination.getPagedData().size() > 0 ? materialReceiptPagination.getPagedData() : Collections.EMPTY_LIST);
    }

    private void setMaterialDetails(String tenantId, MaterialReceiptDetail materialReceiptDetail) {
        materialReceiptDetail.setId(receiptNoteRepository.getSequence("seq_materialreceiptdetail"));
        if (isEmpty(materialReceiptDetail.getTenantId())) {
            materialReceiptDetail.setTenantId(tenantId);
        }

        setUomAndQuantity(tenantId, materialReceiptDetail);
        convertRate(tenantId, materialReceiptDetail);

        Material material = materialService.fetchMaterial(tenantId, materialReceiptDetail.getMaterial().getCode(), new RequestInfo());
        if (false == material.getSerialNumber() && false == material.getShelfLifeControl() && false == material.getLotControl()) {
            materialReceiptDetail.setReceiptDetailsAddnInfo(Collections.EMPTY_LIST);
        } else {
            materialReceiptDetail.getReceiptDetailsAddnInfo().forEach(
                    materialReceiptDetailAddnlInfo -> {
                        materialReceiptDetailAddnlInfo.setId(receiptNoteRepository.getSequence("seq_materialreceiptdetailaddnlinfo"));
                        if (isEmpty(materialReceiptDetailAddnlInfo.getTenantId())) {
                            materialReceiptDetailAddnlInfo.setTenantId(tenantId);
                        }
                    }
            );
        }
    }

    private void setUomAndQuantity(String tenantId, MaterialReceiptDetail materialReceiptDetail) {
        Uom uom = getUom(tenantId, materialReceiptDetail.getUom().getCode(), new RequestInfo());
        materialReceiptDetail.setUom(uom);

        if (null != materialReceiptDetail.getAcceptedQty() && null != uom.getConversionFactor()) {
            Double convertedReceivedQuantity = getSaveConvertedQuantity(materialReceiptDetail.getReceivedQty().doubleValue(), uom.getConversionFactor().doubleValue());
            materialReceiptDetail.setReceivedQty(BigDecimal.valueOf(convertedReceivedQuantity));
        }

        if (null != materialReceiptDetail.getAcceptedQty() && null != uom.getConversionFactor()) {
            Double convertedAcceptedQuantity = getSaveConvertedQuantity(materialReceiptDetail.getAcceptedQty().doubleValue(), uom.getConversionFactor().doubleValue());
            materialReceiptDetail.setAcceptedQty(BigDecimal.valueOf(convertedAcceptedQuantity));
        }
    }


    private void convertRate(String tenantId, MaterialReceiptDetail detail) {
        Uom uom = getUom(tenantId, detail.getUom().getCode(), new org.egov.inv.model.RequestInfo());
        detail.setUom(uom);

        if (null != detail.getUnitRate() && null != uom.getConversionFactor()) {
            Double convertedRate = getSaveConvertedRate(detail.getUnitRate().doubleValue(),
                    uom.getConversionFactor().doubleValue());
            detail.setUnitRate((BigDecimal.valueOf(convertedRate)));
        }

    }

    private void validate(List<MaterialReceipt> materialReceipts, String tenantId, String method) {
        InvalidDataException errors = new InvalidDataException();

        try {
            switch (method) {

                case Constants.ACTION_CREATE: {
                    if (materialReceipts == null) {
                        throw new InvalidDataException("materialreceipt", ErrorCode.NOT_NULL.getCode(), null);
                    } else {
                        for (MaterialReceipt materialReceipt : materialReceipts) {
                            validateMaterialReceipt(materialReceipt, tenantId, errors);
                        }
                    }
                }

                break;

                case Constants.ACTION_UPDATE: {
                    if (materialReceipts == null) {
                        throw new InvalidDataException("materialreceipt", ErrorCode.NOT_NULL.getCode(), null);
                    } else {
                        for (MaterialReceipt materialReceipt : materialReceipts) {
                            validateMaterialReceipt(materialReceipt, tenantId, errors);
                        }
                    }
                }

                break;
            }
        } catch (IllegalArgumentException e) {
        }
        if (errors.getValidationErrors().size() > 0)
            throw errors;

    }

    private void validateMaterialReceipt(MaterialReceipt materialReceipt, String tenantId, InvalidDataException errors) {

        if (null != materialReceipt.getReceivingStore() && !isEmpty(materialReceipt.getReceivingStore().getCode())) {
            validateStore(materialReceipt.getReceivingStore().getCode(), tenantId, errors);
        }

        if (null != materialReceipt.getSupplierBillDate() && materialReceipt.getSupplierBillDate() > getCurrentDate()) {
            String date = convertEpochtoDate(materialReceipt.getSupplierBillDate());
            errors.addDataError(ErrorCode.DATE_LE_CURRENTDATE.getCode(), "Supplier bill date ", date);
        }

        if (null != materialReceipt.getChallanDate() && materialReceipt.getChallanDate() > getCurrentDate()) {
            String date = convertEpochtoDate(materialReceipt.getChallanDate());
            errors.addDataError(ErrorCode.DATE_LE_CURRENTDATE.getCode(), "Challan date ", date);
        }

        if (null != materialReceipt.getReceiptDate() && materialReceipt.getReceiptDate() > getCurrentDate()) {
            String date = convertEpochtoDate(materialReceipt.getReceiptDate());
            errors.addDataError(ErrorCode.DATE_LE_CURRENTDATE.getCode(), "Receipt date ", date);
        }

        if (null != materialReceipt.getSupplier() && !isEmpty(materialReceipt.getSupplier().getCode())) {
            validateSupplier(materialReceipt, tenantId, errors);
        }

        validateMaterialReceiptDetail(materialReceipt, tenantId, errors);

    }

    private Long getCurrentDate() {
        return currentEpochWithoutTime() + (24 * 60 * 60 * 1000) - 1;
    }

    private void validateMaterialReceiptDetail(MaterialReceipt materialReceipt, String tenantId, InvalidDataException errors) {
        int i = 0;
        validateDuplicateMaterialDetails(materialReceipt.getReceiptDetails(), errors);
        for (MaterialReceiptDetail materialReceiptDetail : materialReceipt.getReceiptDetails()) {
            i++;
            if (materialReceipt.getReceiptType().toString().equalsIgnoreCase(MaterialReceipt.ReceiptTypeEnum.PURCHASE_RECEIPT.toString())) {
                validatePurchaseOrder(materialReceiptDetail, materialReceipt.getReceivingStore().getCode(),
                        materialReceipt.getReceiptDate(), materialReceipt.getSupplier().getCode(), tenantId, i, errors);
            }
            validateMaterial(materialReceiptDetail, tenantId, i, errors);
            validateUom(materialReceiptDetail.getUom(), tenantId, i, errors);
            validateQuantity(materialReceiptDetail, i, errors);
            if (materialReceiptDetail.getReceiptDetailsAddnInfo().size() > 0) {
                validateDetailsAddnInfo(materialReceiptDetail.getReceiptDetailsAddnInfo(), materialReceiptDetail.getAcceptedQty().longValue(), tenantId, i, errors);
            }
        }
    }

    private void validateStore(String storeCode, String tenantId, InvalidDataException errors) {
        StoreGetRequest storeGetRequest = StoreGetRequest.builder()
                .code(Collections.singletonList(storeCode))
                .tenantId(tenantId)
                .build();

        StoreResponse storeResponse = storeService.search(storeGetRequest);
        if (storeResponse.getStores().size() == 0) {
            errors.addDataError(ErrorCode.STORE_NOT_EXIST.getCode(), storeCode);
        }
    }

    private void validateSupplier(MaterialReceipt materialReceipt, String tenantId, InvalidDataException errors) {
        SupplierGetRequest supplierGetRequest = SupplierGetRequest.builder()
                .code(Collections.singletonList(materialReceipt.getSupplier().getCode()))
                .tenantId(tenantId)
                .active(true)
                .build();
        SupplierResponse suppliers = supplierService.search(supplierGetRequest);
        if (suppliers.getSuppliers().size() == 0) {
            errors.addDataError(ErrorCode.SUPPLIER_NOT_EXIST.getCode(), materialReceipt.getSupplier().getCode());

        }
    }


    private void validateUom(Uom uom, String tenantId, int i, InvalidDataException errors) {
        if (null != uom && !isEmpty(uom.getCode())) {
            mdmsRepository.fetchObject(tenantId, "common-masters", "Uom", "code", uom.getCode(), Uom.class);
        } else
            errors.addDataError(ErrorCode.OBJECT_NOT_FOUND_ROW.getCode(), "UOM ", uom.getCode(), String.valueOf(i));

    }

    private void validateQuantity(MaterialReceiptDetail materialReceiptDetail, int i, InvalidDataException errors) {


        if (isEmpty(materialReceiptDetail.getReceivedQty())) {
            errors.addDataError(ErrorCode.MANDATORY_VALUE_MISSING.getCode(), "Received Quantity is Required at row " + i);
        }

        if (!isEmpty(materialReceiptDetail.getReceivedQty()) && materialReceiptDetail.getReceivedQty().doubleValue() <= 0) {
            errors.addDataError(ErrorCode.QTY_GTR_ROW.getCode(), "Received Quantity", String.valueOf(i));
        }

        if (isEmpty(materialReceiptDetail.getAcceptedQty())) {
            errors.addDataError(ErrorCode.MANDATORY_VALUE_MISSINGROW.getCode(), "Accepted Quantity ", String.valueOf(i));
        }

        if (!isEmpty(materialReceiptDetail.getAcceptedQty()) && materialReceiptDetail.getAcceptedQty().doubleValue() <= 0) {
            errors.addDataError(ErrorCode.QTY_GTR_ROW.getCode(), "Accepted Quantity ", String.valueOf(i));
        }

        if (materialReceiptDetail.getAcceptedQty().longValue() > materialReceiptDetail.getReceivedQty().longValue()) {
            errors.addDataError(ErrorCode.QTY_LE_SCND_ROW.getCode(), "Accepted Quantity ", "Received Quantity", String.valueOf(i));
        }
    }

    private void validateMaterial(MaterialReceiptDetail receiptDetail, String tenantId, int i, InvalidDataException errors) {
        if (null != receiptDetail.getMaterial() && !isEmpty(receiptDetail.getMaterial().getCode())) {
            Material material = materialService.fetchMaterial(tenantId, receiptDetail.getMaterial().getCode(), new RequestInfo());

            for (MaterialReceiptDetailAddnlinfo addnlinfo : receiptDetail.getReceiptDetailsAddnInfo()) {
                if (true == material.getLotControl() && isEmpty(addnlinfo.getLotNo())) {
                    errors.addDataError(ErrorCode.LOT_NO_NOT_EXIST.getCode(), addnlinfo.getLotNo() + " at serial no." + i);
                }

                if (true == material.getShelfLifeControl() && (isEmpty(addnlinfo.getExpiryDate()) ||
                        (!isEmpty(addnlinfo.getExpiryDate()) && !(addnlinfo.getExpiryDate().doubleValue() > 0)))) {
                    errors.addDataError(ErrorCode.EXP_DATE_NOT_EXIST.getCode(), addnlinfo.getExpiryDate() + " at serial no." + i);
                    if (true == material.getSerialNumber() && isEmpty(addnlinfo.getSerialNo())) {
                        errors.addDataError(ErrorCode.MANDATORY_VALUE_MISSINGROW.getCode(), "Serial number ", String.valueOf(i));
                    }
                }


            }
        } else
            errors.addDataError(ErrorCode.MANDATORY_VALUE_MISSINGROW.getCode(), "Material ", String.valueOf(i));

    }

    private void validateDetailsAddnInfo(List<MaterialReceiptDetailAddnlinfo> materialReceiptDetailAddnlinfos, Long acceptedQuantity, String tenantId, int i, InvalidDataException errors) {
        Long currentDate = currentEpochWithoutTime() + (24 * 60 * 60) - 1;
        Long totalQuantity = 0L;
        for (MaterialReceiptDetailAddnlinfo addnlinfo : materialReceiptDetailAddnlinfos) {
            {
                if (null != addnlinfo.getQuantity()) {
                    totalQuantity = totalQuantity + addnlinfo.getQuantity().longValue();
                }

                if (null != addnlinfo.getExpiryDate()
                        && currentDate > addnlinfo.getExpiryDate()) {
                    String date = convertEpochtoDate(addnlinfo.getExpiryDate());
                    errors.addDataError(ErrorCode.DATE_LE_CURRENTDATE.getCode(), "Expiry date ", date);

                }
            }
        }

        if (totalQuantity.longValue() != acceptedQuantity.longValue()) {
            errors.addDataError(ErrorCode.FIELD_DOESNT_MATCH.getCode(), "Accepted Quantity", "Sum of quantity of additional details");
        }
    }

    private void validateDuplicateMaterialDetails(List<MaterialReceiptDetail> materialReceiptDetails, InvalidDataException errors) {
        HashSet<String> hashSet = new HashSet<>();
        int i = 0;
        for (MaterialReceiptDetail materialReceiptDetail : materialReceiptDetails) {
            i++;
            if (false == hashSet.add(materialReceiptDetail.getPurchaseOrderDetail().getId() + "-" + materialReceiptDetail.getMaterial().getCode())) {
                errors.addDataError(ErrorCode.COMBINATION_EXISTS_ROW.getCode(), "Purchase order", materialReceiptDetail.getPurchaseOrderDetail().getId().toString()
                        , "material", materialReceiptDetail.getMaterial().getCode().toString(), String.valueOf(i));
            }
        }
    }

    private void validatePurchaseOrder(MaterialReceiptDetail materialReceiptDetail, String store, Long receiptDate, String supplier, String tenantId, int i, InvalidDataException errors) {

        if (null != materialReceiptDetail.getPurchaseOrderDetail()) {
            PurchaseOrderDetailSearch purchaseOrderDetailSearch = new PurchaseOrderDetailSearch();
            purchaseOrderDetailSearch.setTenantId(tenantId);
            purchaseOrderDetailSearch.setIds(Collections.singletonList(materialReceiptDetail.getPurchaseOrderDetail().getId()));

            Pagination<PurchaseOrderDetail> purchaseOrderDetails = purchaseOrderDetailService.search(purchaseOrderDetailSearch);

            if (purchaseOrderDetails.getPagedData().size() > 0) {
                for (PurchaseOrderDetail purchaseOrderDetail : purchaseOrderDetails.getPagedData()) {

                    if (null != materialReceiptDetail.getReceivedQty() &&
                            materialReceiptDetail.getReceivedQty().longValue() > purchaseOrderDetail.getOrderQuantity().longValue()) {
                        errors.addDataError(ErrorCode.RCVED_QTY_LS_ODRQTY.getCode(), String.valueOf(i));
                    }

                    if (null != materialReceiptDetail.getReceivedQty() &&
                            materialReceiptDetail.getReceivedQty().longValue() > purchaseOrderDetail.getReceivedQuantity().longValue()) {
                        errors.addDataError(ErrorCode.RCVED_QTY_LS_PORCVEDATY.getCode(), String.valueOf(i));
                    }

                    PurchaseOrderSearch purchaseOrderSearch = new PurchaseOrderSearch();
                    purchaseOrderSearch.setPurchaseOrderNumber(purchaseOrderDetail.getPurchaseOrderNumber());
                    purchaseOrderSearch.setSupplier(supplier);
                    purchaseOrderSearch.setStore(store);
                    purchaseOrderSearch.setPurchaseOrderDate(receiptDate);
                    purchaseOrderSearch.setTenantId(purchaseOrderDetail.getTenantId());

                    PurchaseOrderResponse purchaseOrders = purchaseOrderService.search(purchaseOrderSearch);
                    if (purchaseOrders.getPurchaseOrders().size() > 0) {
                        for (PurchaseOrder purchaseOrder : purchaseOrders.getPurchaseOrders()) {
                            if (null != purchaseOrder.getPurchaseOrderDate()
                                    && purchaseOrder.getPurchaseOrderDate() > receiptDate) {
                                errors.addDataError(ErrorCode.DATE1_GT_DATE2ROW.getCode(), "Receipt Date ", "purchase order date ", String.valueOf(i));
                            }

                            if (!isEmpty(supplier) && !isEmpty(purchaseOrder.getSupplier().getCode())
                                    && !supplier.equalsIgnoreCase(purchaseOrder.getSupplier().getCode())) {

                                errors.addDataError(ErrorCode.MATCH_TWO_FIELDS.getCode(), "Supplier ", "purchase order supplier ", String.valueOf(i));
                            }
                        }
                    } else
                        errors.addDataError(ErrorCode.FIELD_NOT_EXIST.getCode(), "purchase order", materialReceiptDetail.getPurchaseOrderDetail().getId(), String.valueOf(i));
                }
            } else
                errors.addDataError(ErrorCode.FIELD_NOT_EXIST.getCode(), "purchase order", materialReceiptDetail.getPurchaseOrderDetail().getId(), String.valueOf(i));
        }
    }


    private PurchaseOrderResponse getPurchaseOrderResponse(PurchaseOrderSearch purchaseOrderSearch) {
        return purchaseOrderService.search(purchaseOrderSearch);
    }

    private String appendString(MaterialReceipt materialReceipt) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        String code = "MRN/";
        int id = Integer.valueOf(receiptNoteRepository.getSequence(materialReceipt));
        String idgen = String.format("%05d", id);
        String mrnNumber = code + idgen + "/" + year;
        return mrnNumber;
    }

    private String convertEpochtoDate(Long date) {
        Date epoch = new Date(date);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String s2 = format.format(epoch);
        return s2;
    }

}
