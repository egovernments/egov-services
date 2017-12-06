package org.egov.inv.domain.service;

import org.egov.common.Constants;
import org.egov.common.DomainService;
import org.egov.common.Pagination;
import org.egov.inv.model.RequestInfo;
import org.egov.common.exception.ErrorCode;
import org.egov.common.exception.InvalidDataException;
import org.egov.inv.model.*;
import org.egov.inv.persistence.repository.ReceiptNoteRepository;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
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

                setUomAndQuantity(tenantId, materialReceiptDetail);

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
                logAwareKafkaTemplate.send(cancelReceiptPOTopic, cancelReceiptPOTopicKey, materialReceiptRequest);
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

        materialReceiptDetail.getReceiptDetailsAddnInfo().forEach(
                materialReceiptDetailAddnlInfo -> {
                    materialReceiptDetailAddnlInfo.setId(receiptNoteRepository.getSequence("seq_materialreceiptdetailaddnlinfo"));
                    if (isEmpty(materialReceiptDetailAddnlInfo.getTenantId())) {
                        materialReceiptDetailAddnlInfo.setTenantId(tenantId);
                    }
                }
        );
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


    private void validate(List<MaterialReceipt> materialReceipts, String tenantId, String method) {
        try {
            switch (method) {

                case Constants.ACTION_CREATE: {
                    if (materialReceipts == null) {
                        throw new InvalidDataException("materialreceipt", ErrorCode.NOT_NULL.getCode(), null);
                    } else {
                        for (MaterialReceipt materialReceipt : materialReceipts) {
                            validateMaterialReceipt(materialReceipt, tenantId);
                        }
                    }
                }

                break;

                case Constants.ACTION_UPDATE: {
                    if (materialReceipts == null) {
                        throw new InvalidDataException("materialreceipt", ErrorCode.NOT_NULL.getCode(), null);
                    } else {
                        for (MaterialReceipt materialReceipt : materialReceipts) {
                            validateMaterialReceipt(materialReceipt, tenantId);
                        }
                    }
                }

                break;
            }
        } catch (
                IllegalArgumentException e)

        {

        }

    }

    private void validateMaterialReceipt(MaterialReceipt materialReceipt, String tenantId) {

        if (null != materialReceipt.getReceivingStore() && !isEmpty(materialReceipt.getReceivingStore().getCode())) {
            validateStore(materialReceipt.getReceivingStore().getCode(), tenantId);
        }

        if (null != materialReceipt.getSupplierBillDate() && materialReceipt.getSupplierBillDate() > getCurrentDate()) {
            throw new CustomException("inv.0026", "Supplier bill date must be less than or equal to current date");
        }

        if (null != materialReceipt.getChallanDate() && materialReceipt.getChallanDate() > getCurrentDate()) {
            throw new CustomException("inv.0028", "Challan date must be less than or equal to current date");
        }

        if (null != materialReceipt.getReceiptDate() && materialReceipt.getReceiptDate() > getCurrentDate()) {
            throw new CustomException("inv.0029", "Receipt date must be less than or equal to current date");
        }

        if (null != materialReceipt.getSupplier() && !isEmpty(materialReceipt.getSupplier().getCode())) {
            validateSupplier(materialReceipt, tenantId);
        }

        validateMaterialReceiptDetail(materialReceipt, tenantId);

    }

    private Long getCurrentDate() {
        return currentEpochWithoutTime() + (24 * 60 * 60) - 1;
    }

    private void validateMaterialReceiptDetail(MaterialReceipt materialReceipt, String tenantId) {
        validateDuplicateMaterialDetails(materialReceipt.getReceiptDetails());
        for (MaterialReceiptDetail materialReceiptDetail : materialReceipt.getReceiptDetails()) {
            if (materialReceipt.getReceiptType().toString().equalsIgnoreCase(MaterialReceipt.ReceiptTypeEnum.PURCHASE_RECEIPT.toString())) {
                validatePurchaseOrder(materialReceiptDetail, materialReceipt.getReceivingStore().getCode(),
                        materialReceipt.getReceiptDate(), materialReceipt.getSupplier().getCode(), tenantId);
            }
            validateMaterial(materialReceiptDetail, tenantId);
            validateQuantity(materialReceiptDetail);
            if (materialReceiptDetail.getReceiptDetailsAddnInfo().size() > 0) {
                validateDetailsAddnInfo(materialReceiptDetail.getReceiptDetailsAddnInfo(), tenantId);
            }
        }
    }

    private void validateStore(String storeCode, String tenantId) {
        StoreGetRequest storeGetRequest = StoreGetRequest.builder()
                .code(Collections.singletonList(storeCode))
                .tenantId(tenantId)
                .build();

        StoreResponse storeResponse = storeService.search(storeGetRequest);
        if (storeResponse.getStores().size() == 0) {
            throw new CustomException("inv.0025", "Store not found");
        }
    }

    private void validateSupplier(MaterialReceipt materialReceipt, String tenantId) {
        SupplierGetRequest supplierGetRequest = SupplierGetRequest.builder()
                .code(Collections.singletonList(materialReceipt.getSupplier().getCode()))
                .tenantId(tenantId)
                .active(true)
                .build();
        SupplierResponse suppliers = supplierService.search(supplierGetRequest);
        if (suppliers.getSuppliers().size() == 0) {
            throw new CustomException("inv.0030", "Supplier not found or inactive");

        }
    }

    private void validateQuantity(MaterialReceiptDetail materialReceiptDetail) {


        if (isEmpty(materialReceiptDetail.getReceivedQty())) {
            throw new CustomException("inv.0023", "Received quantity is required");
        }

        if (!isEmpty(materialReceiptDetail.getReceivedQty()) && materialReceiptDetail.getReceivedQty().doubleValue() <= 0) {
            throw new CustomException("inv.0024", "Received quantity should be greater than zero");
        }

        if (!isEmpty(materialReceiptDetail.getAcceptedQty()) && materialReceiptDetail.getAcceptedQty().doubleValue() <= 0) {
            throw new CustomException("inv.0025", "Accepted quantity should be greater than zero");
        }

    }

    private void validateMaterial(MaterialReceiptDetail receiptDetail, String tenantId) {

        if (null != receiptDetail.getMaterial()) {
            Material material = materialService.fetchMaterial(tenantId, receiptDetail.getMaterial().getCode(), new RequestInfo());

            for (MaterialReceiptDetailAddnlinfo addnlinfo : receiptDetail.getReceiptDetailsAddnInfo()) {
                if (true == material.getLotControl() && isEmpty(addnlinfo.getLotNo())) {
                    throw new CustomException("inv.0020", "Lot number is required");
                }

                if (true == material.getShelfLifeControl() && (isEmpty(addnlinfo.getExpiryDate()) ||
                        (!isEmpty(addnlinfo.getExpiryDate()) && !(addnlinfo.getExpiryDate().doubleValue() > 0)))) {
                    throw new CustomException("inv.0021", "Expiry date is required");
                }
            }
        } else
            throw new CustomException("inv.0022", "material is not present");
    }

    private void validateDetailsAddnInfo(List<MaterialReceiptDetailAddnlinfo> materialReceiptDetailAddnlinfos, String tenantId) {
        Long currentDate = currentEpochWithoutTime() + (24 * 60 * 60) - 1;

        for (MaterialReceiptDetailAddnlinfo addnlinfo : materialReceiptDetailAddnlinfos) {
            {
                if (null != addnlinfo.getExpiryDate()
                        && currentDate > addnlinfo.getExpiryDate()) {
                    throw new CustomException("inv.0023", "Expiry date must be greater than today's date");
                }
            }
        }
    }

    private void validateDuplicateMaterialDetails(List<MaterialReceiptDetail> materialReceiptDetails) {
        HashSet<String> hashSet = new HashSet<>();
        for (MaterialReceiptDetail materialReceiptDetail : materialReceiptDetails) {
            if (false == hashSet.add(materialReceiptDetail.getPurchaseOrderDetail().getId() + "-" + materialReceiptDetail.getMaterial().getCode())) {
                throw new CustomException("inv.0015", materialReceiptDetail.getPurchaseOrderDetail().getId() +
                        " and " + materialReceiptDetail.getMaterial().getCode() + " combination is already entered");
            }
        }
    }


    private void validatePurchaseOrder(MaterialReceiptDetail materialReceiptDetail, String store, Long receiptDate, String supplier, String tenantId) {

        if (null != materialReceiptDetail.getPurchaseOrderDetail()) {
            PurchaseOrderDetailSearch purchaseOrderDetailSearch = new PurchaseOrderDetailSearch();
            purchaseOrderDetailSearch.setTenantId(tenantId);
            purchaseOrderDetailSearch.setIds(Collections.singletonList(materialReceiptDetail.getPurchaseOrderDetail().getId()));

            Pagination<PurchaseOrderDetail> purchaseOrderDetails = purchaseOrderDetailService.search(purchaseOrderDetailSearch);

            if (purchaseOrderDetails.getPagedData().size() > 0) {
                for (PurchaseOrderDetail purchaseOrderDetail : purchaseOrderDetails.getPagedData()) {

                    if (null != materialReceiptDetail.getReceivedQty() &&
                            materialReceiptDetail.getReceivedQty().longValue() > purchaseOrderDetail.getOrderQuantity().longValue()) {
                        throw new CustomException("inv.0031", "Received quantity should be less than order quantity");
                    }

                    if (null != materialReceiptDetail.getReceivedQty() &&
                            materialReceiptDetail.getReceivedQty().longValue() > purchaseOrderDetail.getReceivedQuantity().longValue()) {
                        throw new CustomException("inv.0032", "Received quantity should be not be greater than receieved quatity");
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
                                throw new CustomException("inv.00027", "Receipt Date must be greater than purchase order date");
                            }

                            if (!isEmpty(supplier) && !isEmpty(purchaseOrder.getSupplier().getCode())
                                    && !supplier.equalsIgnoreCase(purchaseOrder.getSupplier().getCode())) {
                                throw new CustomException("inv.0029", "Supplier doesn't match the purchase order supplier");
                            }
                        }
                    } else
                        throw new CustomException("inv.0016", "purchase order - " + materialReceiptDetail.getPurchaseOrderDetail().getId() +
                                " is not present");
                }
            } else
                throw new CustomException("inv.0017", "purchase order - " + materialReceiptDetail.getPurchaseOrderDetail().getId() +
                        " is not present");
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


}
