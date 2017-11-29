package org.egov.inv.domain.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.egov.common.Constants;
import org.egov.common.DomainService;
import org.egov.common.Pagination;
import org.egov.common.exception.CustomBindException;
import org.egov.common.exception.ErrorCode;
import org.egov.common.exception.InvalidDataException;
import org.egov.inv.model.*;
import org.egov.inv.model.PurchaseOrder.PurchaseTypeEnum;
import org.egov.inv.persistence.repository.PurchaseOrderJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PurchaseOrderService extends DomainService {

    @Autowired
    private PurchaseOrderJdbcRepository purchaseOrderRepository;

    @Autowired
    private PurchaseOrderDetailService purchaseOrderDetailService;

    @Value("${inv.purchaseorders.save.topic}")
    private String saveTopic;

    @Value("${inv.purchaseorders.save.key}")
    private String saveKey;

    @Value("${inv.purchaseorders.save.topic}")
    private String updateTopic;

    @Value("${inv.purchaseorders.save.key}")
    private String updateKey;

    @Value("${inv.purchaseorders.nonindent.save.topic}")
    private String saveNonIndentTopic;

    @Value("${inv.purchaseorders.nonindent.save.key}")
    private String saveNonIndentKey;

    @Value("${inv.purchaseorders.nonindent.save.topic}")
    private String updateNonIndentTopic;

    @Value("${inv.purchaseorders.nonindent.save.key}")
    private String updateNonIndentKey;


    @Transactional
    public PurchaseOrderResponse create(PurchaseOrderRequest purchaseOrderRequest) {

        try {
            List<PurchaseOrder> purchaseOrders = fetchRelated(purchaseOrderRequest.getPurchaseOrders());
            validate(purchaseOrders, Constants.ACTION_CREATE);
            List<String> sequenceNos = purchaseOrderRepository.getSequence(PurchaseOrder.class.getSimpleName(), purchaseOrders.size());
            int i = 0;
            for (PurchaseOrder purchaseOrder : purchaseOrders) {
                purchaseOrder.setId(sequenceNos.get(i));
                //move to id-gen with format <ULB short code>/<Store Code>/<fin. Year>/<serial No.>
                purchaseOrder.setPurchaseOrderNumber(sequenceNos.get(i));
                i++;
                int j = 0;
                purchaseOrder.setAuditDetails(getAuditDetails(purchaseOrderRequest.getRequestInfo(), Constants.ACTION_CREATE));
                List<String> detailSequenceNos = purchaseOrderRepository.getSequence(PurchaseOrderDetail.class.getSimpleName(), purchaseOrder.getPurchaseOrderDetails().size());
                for (PurchaseOrderDetail purchaseOrderDetail : purchaseOrder.getPurchaseOrderDetails()) {
                    purchaseOrderDetail.setId(detailSequenceNos.get(j));
                    purchaseOrderDetail.setTenantId(purchaseOrder.getTenantId());
                    j++;
                }
            }

            // TODO: ITERATE MULTIPLE PURCHASE ORDERS, BASED ON PURCHASE TYPE,
            // PUSH DATA TO KAFKA.

			/*if (purchaseOrders.size() > 0 && purchaseOrders.get(0).getPurchaseType() != null) {
                if (purchaseOrders.get(0).getPurchaseType().toString()
						.equalsIgnoreCase(PurchaseTypeEnum.INDENT.toString()))
					kafkaQue.send(saveTopic, saveKey, purchaseOrderRequest);
				else
					kafkaQue.send(saveTopic, saveKey, purchaseOrderRequest);
			}*/
            kafkaQue.send(saveTopic, saveKey, purchaseOrderRequest);
            PurchaseOrderResponse response = new PurchaseOrderResponse();
            response.setPurchaseOrders(purchaseOrderRequest.getPurchaseOrders());
            response.setResponseInfo(getResponseInfo(purchaseOrderRequest.getRequestInfo()));
            return response;
        } catch (CustomBindException e) {
            throw e;
        }

    }

    @Transactional
    public PurchaseOrderResponse update(PurchaseOrderRequest purchaseOrderRequest) {

        try {
            List<PurchaseOrder> purchaseOrder = fetchRelated(purchaseOrderRequest.getPurchaseOrders());
            validate(purchaseOrder, Constants.ACTION_UPDATE);

            // TODO: ITERATE MULTIPLE PURCHASE ORDERS, BASED ON PURCHASE TYPE,
            // PUSH DATA TO KAFKA.

            if (purchaseOrder.size() > 0 && purchaseOrder.get(0).getPurchaseType() != null) {
                if (purchaseOrder.get(0).getPurchaseType().toString()
                        .equalsIgnoreCase(PurchaseTypeEnum.INDENT.toString()))
                    kafkaQue.send(updateTopic, updateKey, purchaseOrderRequest);
                else
                    kafkaQue.send(updateTopic, updateKey, purchaseOrderRequest);
            }

            PurchaseOrderResponse response = new PurchaseOrderResponse();
            response.setPurchaseOrders(purchaseOrderRequest.getPurchaseOrders());
            response.setResponseInfo(getResponseInfo(purchaseOrderRequest.getRequestInfo()));
            return response;
        } catch (CustomBindException e) {
            throw e;
        }

    }


    public PurchaseOrderResponse search(PurchaseOrderSearch is) {
        PurchaseOrderResponse response = new PurchaseOrderResponse();
        Pagination<PurchaseOrder> search = purchaseOrderRepository.search(is);

        if (search.getPagedData().size() > 0) {
            for (PurchaseOrder purchaseOrder : search.getPagedData()) {
                PurchaseOrderDetailSearch purchaseOrderDetailSearch = new PurchaseOrderDetailSearch();
                purchaseOrderDetailSearch.setPurchaseOrder(purchaseOrder.getPurchaseOrderNumber());
                purchaseOrderDetailSearch.setTenantId(purchaseOrder.getTenantId());
                Pagination<PurchaseOrderDetail> detailPagination = purchaseOrderDetailService.search(purchaseOrderDetailSearch);
                purchaseOrder.setPurchaseOrderDetails(detailPagination.getPagedData().size() > 0 ? detailPagination.getPagedData() : Collections.EMPTY_LIST);
            }
        }

        response.setPurchaseOrders(search.getPagedData());
        response.setPage(getPage(search));
        return response;

    }

    private void validate(List<PurchaseOrder> pos, String method) {

        try {
            switch (method) {

                case Constants.ACTION_CREATE: {
                    if (pos == null) {
                        throw new InvalidDataException("purchaseOrders", ErrorCode.NOT_NULL.getCode(), null);
                    }
                }
                break;

            }
        } catch (IllegalArgumentException e) {

        }

    }

    public List<PurchaseOrder> fetchRelated(List<PurchaseOrder> pos) {
        for (PurchaseOrder po : pos) {
            // fetch related items

        }

        return pos;
    }


    public PurchaseOrderResponse preparePoFromIndents(PurchaseOrderRequest purchaseOrderRequest) {
        PurchaseOrderResponse response = new PurchaseOrderResponse();
        response.setResponseInfo(getResponseInfo(purchaseOrderRequest.getRequestInfo()));

        List<PurchaseOrder> purchaseOrders = fetchRelated(purchaseOrderRequest.getPurchaseOrders());
        validate(purchaseOrders, Constants.ACTION_SEARCH_INDENT_FOR_PO);
        List<PurchaseOrder> finalPurchaseOrders = new ArrayList<PurchaseOrder>();
        for (PurchaseOrder purchaseOrder : purchaseOrders) {

            //TODO: TEMPORARY DATA ADDED FOR TESTING PURPOSE.

            purchaseOrder.setAuditDetails(getAuditDetails(purchaseOrderRequest.getRequestInfo(), Constants.ACTION_CREATE));

            PurchaseOrderDetail purchaseOrderDetail = new PurchaseOrderDetail();
            purchaseOrderDetail.setTenantId(purchaseOrder.getTenantId());
            Material material1 = new Material();
            material1.setCode("NOS");
            Uom uom1 = new Uom();
            uom1.setCode("CM");
            purchaseOrderDetail.setMaterial(material1);
            purchaseOrderDetail.setUom(uom1);
            purchaseOrderDetail.setIndentNumber("MR1");

            PurchaseIndentDetail poIndentDetail = new PurchaseIndentDetail();
            poIndentDetail.setTenantId(purchaseOrder.getTenantId());
            poIndentDetail.setAuditDetails(getAuditDetails(purchaseOrderRequest.getRequestInfo(), Constants.ACTION_CREATE));
            poIndentDetail.setQuantity(BigDecimal.ONE);

            IndentDetail indentDetail = new IndentDetail();
            indentDetail.setId("1");
            poIndentDetail.setIndentDetail(indentDetail);

            PriceList priceList = new PriceList();
            priceList.setRateContractNumber("RC1");

            purchaseOrderDetail.setPriceList(priceList);
            purchaseOrderDetail.setUnitPrice(BigDecimal.TEN);
            List<PurchaseIndentDetail> poindentlist = new ArrayList<PurchaseIndentDetail>();
            poindentlist.add(poIndentDetail);

            purchaseOrderDetail.setPurchaseIndentDetails(poindentlist);
            purchaseOrder.addPurchaseOrderDetailsItem(purchaseOrderDetail);

            PurchaseOrderDetail purchaseOrderDetail1 = new PurchaseOrderDetail();
            purchaseOrderDetail1.setTenantId(purchaseOrder.getTenantId());
            Material material2 = new Material();
            material2.setCode("MAT3");
            Uom uom2 = new Uom();
            uom2.setCode("NOS");
            purchaseOrderDetail1.setMaterial(material2);
            purchaseOrderDetail1.setUom(uom2);
            purchaseOrderDetail1.setIndentNumber("MR2");

            PurchaseIndentDetail poIndentDetail1 = new PurchaseIndentDetail();
            poIndentDetail1.setTenantId(purchaseOrder.getTenantId());
            poIndentDetail1.setAuditDetails(getAuditDetails(purchaseOrderRequest.getRequestInfo(), Constants.ACTION_CREATE));
            poIndentDetail1.setQuantity(BigDecimal.TEN);

            IndentDetail indentDetail1 = new IndentDetail();
            indentDetail1.setId("2");
            poIndentDetail1.setIndentDetail(indentDetail1);

            PriceList priceList1 = new PriceList();
            priceList1.setRateContractNumber("RC2");

            purchaseOrderDetail1.setPriceList(priceList1);
            purchaseOrderDetail1.setUnitPrice(BigDecimal.valueOf(2));
            List<PurchaseIndentDetail> poindentlist1 = new ArrayList<PurchaseIndentDetail>();
            poindentlist1.add(poIndentDetail1);

            purchaseOrderDetail1.setPurchaseIndentDetails(poindentlist1);
            purchaseOrder.addPurchaseOrderDetailsItem(purchaseOrderDetail1);

            finalPurchaseOrders.add(purchaseOrder);
        }


        response.setPurchaseOrders(finalPurchaseOrders);


        // Iterate through each indent
        // supplier and rate type is required.
        // for each indent check available quantity to procure.
        // for each material, group together and build indent detail and po link
        // for each material, check rate contract and amount detail by supplier. If not found, skip that material.
        // Build Po reponse object and send back.
        return response;
    }

}