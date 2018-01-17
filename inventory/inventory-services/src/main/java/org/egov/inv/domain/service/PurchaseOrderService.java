package org.egov.inv.domain.service;

import static org.springframework.util.StringUtils.isEmpty;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.egov.common.Constants;
import org.egov.common.DomainService;
import org.egov.common.MdmsRepository;
import org.egov.common.Pagination;
import org.egov.common.exception.CustomBindException;
import org.egov.common.exception.ErrorCode;
import org.egov.common.exception.InvalidDataException;
import org.egov.inv.domain.util.InventoryUtilities;
import org.egov.inv.model.Indent;
import org.egov.inv.model.IndentDetail;
import org.egov.inv.model.IndentResponse;
import org.egov.inv.model.IndentSearch;
import org.egov.inv.model.Material;
import org.egov.inv.model.PriceList;
import org.egov.inv.model.PriceListDetails;
import org.egov.inv.model.PriceListSearchRequest;
import org.egov.inv.model.PurchaseIndentDetail;
import org.egov.inv.model.PurchaseOrder;
import org.egov.inv.model.PurchaseOrder.PurchaseTypeEnum;
import org.egov.inv.model.PurchaseOrder.StatusEnum;
import org.egov.inv.model.PurchaseOrderDetail;
import org.egov.inv.model.PurchaseOrderDetailSearch;
import org.egov.inv.model.PurchaseOrderRequest;
import org.egov.inv.model.PurchaseOrderResponse;
import org.egov.inv.model.PurchaseOrderSearch;
import org.egov.inv.model.RequestInfo;
import org.egov.inv.model.Store;
import org.egov.inv.model.StoreGetRequest;
import org.egov.inv.model.Supplier;
import org.egov.inv.model.SupplierGetRequest;
import org.egov.inv.model.Uom;
import org.egov.inv.persistence.entity.IndentEntity;
import org.egov.inv.persistence.entity.PriceListEntity;
import org.egov.inv.persistence.repository.IndentJdbcRepository;
import org.egov.inv.persistence.repository.MaterialReceiptJdbcRepository;
import org.egov.inv.persistence.repository.PriceListJdbcRepository;
import org.egov.inv.persistence.repository.PurchaseOrderJdbcRepository;
import org.egov.inv.persistence.repository.StoreJdbcRepository;
import org.egov.inv.persistence.repository.SupplierJdbcRepository;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional(readOnly = true)
public class PurchaseOrderService extends DomainService {

    @Autowired
    private PurchaseOrderJdbcRepository purchaseOrderRepository;

    @Autowired
    private PurchaseOrderDetailService purchaseOrderDetailService;

    @Autowired
    private PriceListJdbcRepository priceListjdbcRepository;

    @Autowired
    private IndentJdbcRepository indentJdbcRepository;

    @Autowired
    private IndentService indentService;

    @Autowired
    private PriceListService priceListService;

    @Autowired
    private MaterialService materialService;
    
	@Autowired
	private StoreJdbcRepository storeJdbcRepository;

    @Autowired
    private UomService uomService;

    @Autowired
    SupplierJdbcRepository supplierJdbcRepository;

    @Value("${inv.purchaseorders.save.topic}")
    private String saveTopic;

    @Value("${inv.pricelists.podetail.required}")
    private Boolean priceListConfig;

    @Value("${inv.purchaseorders.save.key}")
    private String saveKey;

    @Value("${inv.purchaseorders.update.topic}")
    private String updateTopic;

    @Value("${inv.purchaseorders.update.key}")
    private String updateKey;

    @Value("${inv.purchaseorders.nonindent.save.topic}")
    private String saveNonIndentTopic;

    @Value("${inv.purchaseorders.nonindent.save.key}")
    private String saveNonIndentKey;

    @Value("${inv.purchaseorders.nonindent.update.topic}")
    private String updateNonIndentTopic;

    @Value("${inv.purchaseorders.nonindent.update.key}")
    private String updateNonIndentKey;

    @Value("${usetotal.indent.quantity}")
    private boolean isTotalIndentQuantityUsedInPo;

    @Autowired
    private MaterialReceiptJdbcRepository materialJdbcRepository;
    
    @Autowired
    private MdmsRepository mdmsRepository;

    private String INDENT_MULTIPLE = "Multiple";

    /**
     * @param purchaseOrderRequest
     * @param tenantId
     * @return validate store code
     * store mandatory in db
     */
    @Transactional
    public PurchaseOrderResponse create(PurchaseOrderRequest purchaseOrderRequest, String tenantId) {

        try {
        	
        	for(PurchaseOrder po:purchaseOrderRequest.getPurchaseOrders())
        	{
        		for(PurchaseOrderDetail purchaseOrderDetail : po.getPurchaseOrderDetails())
        		{
        			 Uom uom=getUom(tenantId, purchaseOrderDetail.getUom().getCode(), purchaseOrderRequest.getRequestInfo())	;
         		     purchaseOrderDetail.setUom(uom);
         		     populatePriceListDetails(purchaseOrderDetail);
        			 if (purchaseOrderDetail.getUserQuantity() != null) {
                         purchaseOrderDetail.setOrderQuantity(purchaseOrderDetail.getUserQuantity().multiply(purchaseOrderDetail.getUom().getConversionFactor()));
                     }
        		}
        	}
        	
            List<PurchaseOrder> purchaseOrders = purchaseOrderRequest.getPurchaseOrders();
            InvalidDataException errors = new InvalidDataException();
            validate(purchaseOrders, Constants.ACTION_CREATE, tenantId);
            List<String> sequenceNos = purchaseOrderRepository.getSequence(PurchaseOrder.class.getSimpleName(), purchaseOrders.size());
            int i = 0;
            for (PurchaseOrder purchaseOrder : purchaseOrders) {

            	BigDecimal totalAmount = BigDecimal.ZERO;
            	
            	for(PurchaseOrderDetail poDetail : purchaseOrder.getPurchaseOrderDetails()) {
            		totalAmount = totalAmount.add(poDetail.getOrderQuantity().multiply(poDetail.getUnitPrice()).add(totalAmount));
            	}
            	
            	purchaseOrder.setTotalAmount(totalAmount);
            	
                if (purchaseOrder.getAdvanceAmount() != null) {
                    if (purchaseOrder.getAdvanceAmount().compareTo(purchaseOrder.getTotalAmount()) > 0) {
                        errors.addDataError(ErrorCode.ADVAMT_GE_TOTAMT.getCode(), purchaseOrder.getAdvanceAmount() + " at serial no." + (purchaseOrders.indexOf(purchaseOrder) + 1));
                    }
                }
                
                if (purchaseOrder.getAdvancePercentage() != null) {
                	if (purchaseOrder.getAdvancePercentage().compareTo(new BigDecimal(100)) > 0) {
                        errors.addDataError(ErrorCode.ADVPCT_GE_HUN.getCode(), purchaseOrder.getAdvancePercentage() + " at serial no." + (purchaseOrders.indexOf(purchaseOrder) + 1));
                    }
                }
                
                if (purchaseOrder.getAdvanceAmount() != null && purchaseOrder.getAdvancePercentage() != null) {

                    if (purchaseOrder.getAdvancePercentage().compareTo(new BigDecimal(100)) > 0) {
                        errors.addDataError(ErrorCode.ADVPCT_GE_HUN.getCode(), purchaseOrder.getAdvancePercentage() + " at serial no." + (purchaseOrders.indexOf(purchaseOrder) + 1));
                    }

                    if (purchaseOrder.getAdvanceAmount().compareTo(purchaseOrder.getTotalAmount()) > 0) {
                        errors.addDataError(ErrorCode.ADVAMT_GE_TOTAMT.getCode(), purchaseOrder.getAdvanceAmount() + " at serial no." + (purchaseOrders.indexOf(purchaseOrder) + 1));
                    }

                    if ((purchaseOrder.getTotalAmount().multiply(purchaseOrder.getAdvancePercentage())).divide(new BigDecimal(100), RoundingMode.FLOOR).compareTo(purchaseOrder.getAdvanceAmount().divide(new BigDecimal(1), RoundingMode.FLOOR)) < 0) {
                        errors.addDataError(ErrorCode.ADVAMT_GE_TOTAMT.getCode(), purchaseOrder.getAdvancePercentage() + " at serial no." + (purchaseOrders.indexOf(purchaseOrder) + 1));
                    }

                }

                purchaseOrder.setStatus(StatusEnum.APPROVED);
                String purchaseOrderNumber = appendString(purchaseOrder);
                purchaseOrder.setId(sequenceNos.get(i));

                if (purchaseOrders.size() > 0 && purchaseOrders.get(0).getPurchaseType() != null) {
                    purchaseOrder.setPurchaseType(purchaseOrders.get(0).getPurchaseType());
                } else
                    purchaseOrder.setPurchaseType(PurchaseTypeEnum.INDENT);

                //TODO: move to id-gen with format <ULB short code>/<Store Code>/<fin. Year>/<serial No.>
                purchaseOrder.setPurchaseOrderNumber(purchaseOrderNumber);
                i++;
                int j = 0;
                purchaseOrder.setAuditDetails(getAuditDetails(purchaseOrderRequest.getRequestInfo(), Constants.ACTION_CREATE));
                List<String> detailSequenceNos = purchaseOrderRepository.getSequence(PurchaseOrderDetail.class.getSimpleName(), purchaseOrder.getPurchaseOrderDetails().size());

                for (PurchaseOrderDetail purchaseOrderDetail : purchaseOrder.getPurchaseOrderDetails()) {
                	
                	totalAmount = totalAmount.add(purchaseOrderDetail.getOrderQuantity().multiply(purchaseOrderDetail.getUnitPrice()).add(totalAmount));
                	
                    purchaseOrderDetail.setId(detailSequenceNos.get(j));
                    purchaseOrderDetail.setTenantId(purchaseOrder.getTenantId());

                    if (purchaseOrderDetail.getPurchaseIndentDetails() != null
                            && purchaseOrderDetail.getPurchaseIndentDetails().size() > 0) {
                        int k = 0;
                        List<String> poIndentDetailSequenceNos = purchaseOrderRepository.getSequence(
                                PurchaseIndentDetail.class.getSimpleName(),
                                purchaseOrderDetail.getPurchaseIndentDetails().size());

                        // Order quantity must be less than (tenderQuantity - usedQuantity) in case of tender
                        if (null != purchaseOrder.getRateType() && purchaseOrder.getRateType().toString().equals("One Time Tender"))
                            if (null != purchaseOrderDetail.getOrderQuantity() && purchaseOrderDetail.getOrderQuantity().compareTo(purchaseOrderDetail.getTenderQuantity().subtract(purchaseOrderDetail.getUsedQuantity())) > 0) {
                                errors.addDataError(ErrorCode.QTY_LE_SCND_ROW.getCode(), " at serial no." + purchaseOrder.getPurchaseOrderDetails().indexOf(purchaseOrderDetail));
                            }

                        for (PurchaseIndentDetail purchaseIndentDetail : purchaseOrderDetail
                                .getPurchaseIndentDetails()) {
                            purchaseIndentDetail.setId(poIndentDetailSequenceNos.get(k));
                            purchaseIndentDetail.setTenantId(purchaseOrder.getTenantId());
                            k++;
                        }

                    }

                    //Logic to split PODetail order quantity across multiple indentdetails starts
                    //TODO:below validation wroing fix
                    BigDecimal totalIndentQuantity = BigDecimal.ZERO;
                    if (purchaseOrderDetail.getPurchaseIndentDetails() != null) {
                        for (PurchaseIndentDetail purchaseIndentDetail : purchaseOrderDetail.getPurchaseIndentDetails()) {
                            totalIndentQuantity = totalIndentQuantity.add(purchaseIndentDetail.getIndentDetail().getIndentQuantity());
                        }

                        for (PurchaseIndentDetail purchaseIndentDetail : purchaseOrderDetail.getPurchaseIndentDetails()) {
                            if (totalIndentQuantity.compareTo(purchaseOrderDetail.getOrderQuantity()) >= 0) {
                                purchaseIndentDetail.getIndentDetail().setPoOrderedQuantity(purchaseIndentDetail.getIndentDetail().getIndentQuantity());
                            }
                        }
                    }
                    //Logic to split PODetail order quantity across multiple indentdetails ends

                    Object uom = mdmsRepository.fetchObject(tenantId, "common-masters", "Uom", "code", purchaseOrderDetail.getUom().getCode(), Uom.class);
                    purchaseOrderDetail.setUom((Uom) uom);
                    // purchaseOrderDetail.setUom(uomService.getUom(purchaseOrderDetail.getTenantId(), purchaseOrderDetail.getUom().getCode(),new RequestInfo()));
                   
                    if (purchaseOrderDetail.getIndentQuantity() != null) {
                        purchaseOrderDetail.setIndentQuantity(purchaseOrderDetail.getIndentQuantity().multiply(purchaseOrderDetail.getUom().getConversionFactor()));
                    }
                    if (purchaseOrderDetail.getTenderQuantity() != null) {
                        purchaseOrderDetail.setTenderQuantity(purchaseOrderDetail.getTenderQuantity().multiply(purchaseOrderDetail.getUom().getConversionFactor()));
                    }
                    if (purchaseOrderDetail.getUsedQuantity() != null) {
                        purchaseOrderDetail.setUsedQuantity(purchaseOrderDetail.getUsedQuantity().multiply(purchaseOrderDetail.getUom().getConversionFactor()));
                    }
                    if (purchaseOrderDetail.getUnitPrice() != null) {
                        purchaseOrderDetail.setUnitPrice(purchaseOrderDetail.getUnitPrice().divide(purchaseOrderDetail.getUom().getConversionFactor()));
                    }
                    j++;
                }

                purchaseOrder.setTotalAmount(totalAmount);
            }

            // TODO: ITERATE MULTIPLE PURCHASE ORDERS, BASED ON PURCHASE TYPE,
            // PUSH DATA TO KAFKA.

            if (errors.getValidationErrors().size() > 0)
                throw errors;
            
            if (purchaseOrders.size() > 0 && purchaseOrders.get(0).getPurchaseType() != null) {
                if (purchaseOrders.get(0).getPurchaseType().toString()
                        .equalsIgnoreCase(PurchaseTypeEnum.INDENT.toString()))
                    kafkaQue.send(saveTopic, saveKey, purchaseOrderRequest);
                else
                    kafkaQue.send(saveNonIndentTopic, saveNonIndentKey, purchaseOrderRequest);
            } else { //TODO: REMOVE BELOW, IF PURCHASE TYPE IS PROPER. oTHER WISE BY DEFAULT PASSING TO INDENT PURCHASE.
                kafkaQue.send(saveTopic, saveKey, purchaseOrderRequest);
            }
            PurchaseOrderResponse response = new PurchaseOrderResponse();
            response.setPurchaseOrders(purchaseOrderRequest.getPurchaseOrders());
            response.setResponseInfo(getResponseInfo(purchaseOrderRequest.getRequestInfo()));
            return response;
        } catch (CustomBindException e) {
            throw e;
        }

    }

    @Transactional
    public PurchaseOrderResponse update(PurchaseOrderRequest purchaseOrderRequest, String tenantId) {

        try {
            List<PurchaseOrder> purchaseOrder = purchaseOrderRequest.getPurchaseOrders();
            
            for(PurchaseOrder po:purchaseOrderRequest.getPurchaseOrders())
        	{
        		for(PurchaseOrderDetail purchaseOrderDetail : po.getPurchaseOrderDetails())
        		{
        		    Uom uom=getUom(tenantId, purchaseOrderDetail.getUom().getCode(), purchaseOrderRequest.getRequestInfo())	;
        		    purchaseOrderDetail.setUom(uom);
        		    populatePriceListDetails(purchaseOrderDetail);
        			 if (purchaseOrderDetail.getUserQuantity() != null) {
                         purchaseOrderDetail.setOrderQuantity(purchaseOrderDetail.getUserQuantity().multiply(purchaseOrderDetail.getUom().getConversionFactor()));
                     }
        		}
        	}
            validate(purchaseOrder, Constants.ACTION_UPDATE, tenantId);
            InvalidDataException errors = new InvalidDataException();

            for (PurchaseOrder eachPurchaseOrder : purchaseOrder) {
                //TODO: handle the reversal only the amount not entirly zero
                if (eachPurchaseOrder.getStatus().equals(PurchaseOrder.StatusEnum.fromValue("Rejected"))) {
                    for (PurchaseOrderDetail eachPurchaseOrderDetail : eachPurchaseOrder.getPurchaseOrderDetails()) {
                        for (PurchaseIndentDetail purchaseIndentDetail : eachPurchaseOrderDetail.getPurchaseIndentDetails()) {
                            purchaseIndentDetail.getIndentDetail().setPoOrderedQuantity(BigDecimal.ZERO);
                            purchaseIndentDetail.getIndentDetail().setIndentQuantity(BigDecimal.ZERO);
                        }
                    }
                } else {

                    if (eachPurchaseOrder.getPurchaseOrderNumber() == null) {
                        errors.addDataError(ErrorCode.NULL_VALUE.getCode(), eachPurchaseOrder.getPurchaseOrderNumber() + " at serial no." + (purchaseOrder.indexOf(eachPurchaseOrder) + 1));
                    }

                    if (eachPurchaseOrder.getAdvanceAmount() != null) {
                        if (eachPurchaseOrder.getAdvanceAmount().compareTo(eachPurchaseOrder.getTotalAmount()) > 0) {
                            errors.addDataError(ErrorCode.ADVAMT_GE_TOTAMT.getCode(), eachPurchaseOrder.getAdvanceAmount() + " at serial no." + (purchaseOrder.indexOf(eachPurchaseOrder) + 1));
                        }
                    }

                    BigDecimal totalAmount = BigDecimal.ZERO;
                    eachPurchaseOrder.setAuditDetails(getAuditDetails(purchaseOrderRequest.getRequestInfo(), Constants.ACTION_UPDATE));

                    for (PurchaseOrderDetail eachPurchaseOrderDetail : eachPurchaseOrder.getPurchaseOrderDetails()) {

                        //Logic to split PODetail order quantity across multiple indentdetails starts
                        BigDecimal totalIndentQuantity = BigDecimal.ZERO;

                        if (eachPurchaseOrderDetail.getPurchaseIndentDetails() != null)
                            for (PurchaseIndentDetail purchaseIndentDetail : eachPurchaseOrderDetail.getPurchaseIndentDetails()) {
                                totalIndentQuantity = totalIndentQuantity.add(purchaseIndentDetail.getIndentDetail().getIndentQuantity());
                            }

                        for (PurchaseIndentDetail purchaseIndentDetail : eachPurchaseOrderDetail.getPurchaseIndentDetails()) {
                            if (totalIndentQuantity.compareTo(eachPurchaseOrderDetail.getOrderQuantity()) >= 0) {
                                purchaseIndentDetail.getIndentDetail().setPoOrderedQuantity(eachPurchaseOrderDetail.getOrderQuantity());
                            }
                        }
                        //Logic to split PODetail order quantity across multiple indentdetails ends

                        //populating the below Uom to get the conversion factor for populating the line level details
                    /*    Object uom = mdmsRepository.fetchObject(tenantId, "common-masters", "Uom", "code", eachPurchaseOrderDetail.getUom().getCode(), Uom.class);
                        eachPurchaseOrderDetail.setUom((Uom) uom);*/

                       
                        if (eachPurchaseOrderDetail.getIndentQuantity() != null) {
                            eachPurchaseOrderDetail.setIndentQuantity(eachPurchaseOrderDetail.getIndentQuantity().multiply(eachPurchaseOrderDetail.getUom().getConversionFactor()));
                        }
                        if (eachPurchaseOrderDetail.getTenderQuantity() != null) {
                            eachPurchaseOrderDetail.setTenderQuantity(eachPurchaseOrderDetail.getTenderQuantity().multiply(eachPurchaseOrderDetail.getUom().getConversionFactor()));
                        }
                        if (eachPurchaseOrderDetail.getUsedQuantity() != null) {
                            eachPurchaseOrderDetail.setUsedQuantity(eachPurchaseOrderDetail.getUsedQuantity().multiply(eachPurchaseOrderDetail.getUom().getConversionFactor()));
                        }
                        if (eachPurchaseOrderDetail.getUnitPrice() != null) {
                            eachPurchaseOrderDetail.setUnitPrice(eachPurchaseOrderDetail.getUnitPrice().divide(eachPurchaseOrderDetail.getUom().getConversionFactor()));
                        }
                        totalAmount = totalAmount.add(eachPurchaseOrderDetail.getOrderQuantity().multiply(eachPurchaseOrderDetail.getUnitPrice()).add(totalAmount));
                    }
                    eachPurchaseOrder.setTotalAmount(totalAmount);
                }
            }

            // TODO: ITERATE MULTIPLE PURCHASE ORDERS, BASED ON PURCHASE TYPE,
            // PUSH DATA TO KAFKA.
            
            if (errors.getValidationErrors().size() > 0)
                throw errors;

            if (purchaseOrder.size() > 0 && purchaseOrder.get(0).getPurchaseType() != null) {
                if (purchaseOrder.get(0).getPurchaseType().toString()
                        .equalsIgnoreCase(PurchaseTypeEnum.INDENT.toString()))
                    kafkaQue.send(updateTopic, updateKey, purchaseOrderRequest);
                else
                    kafkaQue.send(updateNonIndentTopic, updateNonIndentKey, purchaseOrderRequest);
            } else
                kafkaQue.send(updateTopic, updateKey, purchaseOrderRequest); //TODO REMOVE THIS IF PUCHASE TYPE PRESENT.

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

    private void validate(List<PurchaseOrder> pos, String method, String tenantId) {
        InvalidDataException errors = new InvalidDataException();

        try {
            switch (method) {

                case Constants.ACTION_CREATE: {
                    if (pos == null) {
                        errors.addDataError(ErrorCode.NOT_NULL.getCode(), "purchaseOrders", null);
                    }
                }
                break;

                case Constants.ACTION_UPDATE: {
                    if (pos == null) {
                        errors.addDataError(ErrorCode.NOT_NULL.getCode(), "purchaseOrders", null);
                    }
                }
                break;

            }

            Long currentMilllis = System.currentTimeMillis();

            for(PurchaseOrder  purchaseOrder : pos)
            	if(purchaseOrder.getPurchaseType() == null)
            		errors.addDataError(ErrorCode.INVALID_PURCHASETYPE_VALUE.getCode(), "purchaseType", null);
            	
            for(PurchaseOrder  purchaseOrder : pos)
            	if(purchaseOrder.getPurchaseType() != null)
				if (!Arrays.stream(PurchaseOrder.PurchaseTypeEnum.values())
						.anyMatch((t) -> t.equals(PurchaseOrder.PurchaseTypeEnum.fromValue(purchaseOrder.getPurchaseType().toString())))) {
					errors.addDataError(ErrorCode.INVALID_PURCHASETYPE_VALUE.getCode(), "purchaseType", purchaseOrder.getPurchaseType().toString());
				}

            //Second check for validating if Indent is valid for PO Creation
            if(method.equals(Constants.ACTION_CREATE))
            	for(PurchaseOrder purchaseOrder : pos)
            	if(purchaseOrder.getPurchaseType() != null)
            	if (purchaseOrder.getPurchaseType().toString().equals("Indent"))
            	if(purchaseOrder.getIndentNumbers() != null)
                for (String indentNo : purchaseOrder.getIndentNumbers()) {
                    if (!purchaseOrderRepository.getIsIndentValidForPOCreate(indentNo)) {
                        errors.addDataError(ErrorCode.INVALID_INDENT_VALUE.getCode(), "indentNumber", indentNo);
                    }
                }
            	else
            		errors.addDataError(ErrorCode.NULL_VALUE.getCode(), "indentNumbers", null);
            
            //validate except during preparepofromindent
            if (!method.equals(Constants.ACTION_SEARCH_INDENT_FOR_PO))
                for (PurchaseOrder eachPurchaseOrder : pos) {
                    BigDecimal totalAmount = BigDecimal.ZERO;
                    int index = pos.indexOf(eachPurchaseOrder) + 1;
                    if(!isValidStore(tenantId, eachPurchaseOrder.getStore().getCode())) {
                    	errors.addDataError(ErrorCode.INVALID_ACTIVE_VALUE.getCode(),"Store "+ eachPurchaseOrder.getStore().getCode());
                    }
                    if (eachPurchaseOrder.getPurchaseOrderDate() > currentMilllis) {
                        String date = convertEpochtoDate(eachPurchaseOrder.getPurchaseOrderDate());
                        errors.addDataError(ErrorCode.PO_DATE_LE_TODAY.getCode(), date + " at serial no." + index);
                    }
                    if (null != eachPurchaseOrder.getExpectedDeliveryDate()) {
                        if (eachPurchaseOrder.getExpectedDeliveryDate() < eachPurchaseOrder.getPurchaseOrderDate()) {
                            String date = convertEpochtoDate(eachPurchaseOrder.getExpectedDeliveryDate());
                            errors.addDataError(ErrorCode.EXP_DATE_GE_PODATE.getCode(), date + " at serial no." + index);
                        }
                    }

                    if (eachPurchaseOrder.getAdvanceAmount() != null && eachPurchaseOrder.getAdvancePercentage() != null) {

                        if (eachPurchaseOrder.getAdvancePercentage().compareTo(new BigDecimal(100)) > 1) {
                            errors.addDataError(ErrorCode.ADVPCT_GE_HUN.getCode(), eachPurchaseOrder.getAdvancePercentage() + " at serial no." + (pos.indexOf(eachPurchaseOrder) + 1));
                        }

                    } else if (eachPurchaseOrder.getAdvancePercentage() != null) {
                        if (eachPurchaseOrder.getAdvancePercentage().compareTo(new BigDecimal(100)) > 1) {
                            errors.addDataError(ErrorCode.ADVPCT_GE_HUN.getCode(), eachPurchaseOrder.getAdvancePercentage() + " at serial no." + (pos.indexOf(eachPurchaseOrder) + 1));
                        }
                    }

                    String indentNumbers = "";
                    if (null != eachPurchaseOrder.getPurchaseOrderDetails())
                        for (PurchaseOrderDetail purchaseOrderDetail : eachPurchaseOrder.getPurchaseOrderDetails()) {

                            IndentEntity ie = IndentEntity.builder().build();

                            //Indent Number mandatory for each PurchaseOrder
                            if(eachPurchaseOrder.getPurchaseType() != null)
                            if (eachPurchaseOrder.getPurchaseType().toString().equals("Indent")) {
                        		if(purchaseOrderDetail.getIndentNumber() == null)
                        			errors.addDataError(ErrorCode.NOT_NULL.getCode(), "indentNumber", "null");
                            }

                            //indent reference validation
                            if(eachPurchaseOrder.getPurchaseType() != null)
                            if (eachPurchaseOrder.getPurchaseType().toString().equals("Indent")) {
                                if(purchaseOrderDetail.getIndentNumber() != null) {
	                            	ie = indentJdbcRepository.findById(IndentEntity.builder().indentNumber(purchaseOrderDetail.getIndentNumber()).tenantId(purchaseOrderDetail.getTenantId()).build());
	                                if (ie == null || ie.getId() == null)
	                                    errors.addDataError(ErrorCode.INVALID_REF_VALUE.getCode(), "IndentNumber", purchaseOrderDetail.getIndentNumber());
	                                indentNumbers += purchaseOrderDetail.getIndentNumber() + ",";
                                }
                            }

                            if(purchaseOrderDetail.getPriceList() == null || purchaseOrderDetail.getPriceList().getId() == null) {
                            	errors.addDataError(ErrorCode.NOT_NULL.getCode(), "rateContract", "null");                            	
                            }

                            PriceListEntity ple = PriceListEntity.builder().build();
                            
                            if(purchaseOrderDetail.getPriceList() != null)
                            	ple = priceListjdbcRepository.findById(PriceListEntity.builder().id(purchaseOrderDetail.getPriceList().getId()).tenantId(purchaseOrderDetail.getTenantId()).build());

                            //RateContract reference validation
                            if (ple == null || ple.getId() == null) {
                                errors.addDataError(ErrorCode.INVALID_REF_VALUE.getCode(), "priceList", null);
                            }
                        }
                    indentNumbers.replaceAll(",$", "");

                    IndentSearch is = IndentSearch.builder().ids(new ArrayList<String>(Arrays.asList(indentNumbers.split(",")))).tenantId(tenantId).build();
                    IndentResponse isr = indentService.search(is, new RequestInfo());

                    if(eachPurchaseOrder.getPurchaseType() != null)
                    if (eachPurchaseOrder.getPurchaseType().toString().equals("Indent"))
                        for (Indent in : isr.getIndents()) {
                            if (in.getIndentDate().compareTo(eachPurchaseOrder.getPurchaseOrderDate()) > 0) {
                                String date = convertEpochtoDate(eachPurchaseOrder.getPurchaseOrderDate());
                                errors.addDataError(ErrorCode.DATE1_LE_DATE2.getCode(), date + " at serial no." + pos.indexOf(eachPurchaseOrder));
                            }
                        }

                    //Allow only material which are part of the indent only for creating PO
                    if(eachPurchaseOrder.getPurchaseType() != null)
                    if (eachPurchaseOrder.getPurchaseType().toString().equals("Indent"))
                        for (PurchaseOrderDetail poDetail : eachPurchaseOrder.getPurchaseOrderDetails()) {
                            boolean materialPresent = false;
                            IndentSearch is1 = IndentSearch.builder().ids(new ArrayList<String>(Arrays.asList(poDetail.getIndentNumber()))).tenantId(tenantId).build();
                            IndentResponse isr1 = indentService.search(is1, new RequestInfo());

                            for (Indent ind : isr1.getIndents()) {
                                for (IndentDetail indDetail : ind.getIndentDetails()) {
                                    if (indDetail.getMaterial().getCode().equals(poDetail.getMaterial().getCode())) {
                                        materialPresent = true;
                                    }
                                }
                            }

                            if (!materialPresent) {
                                errors.addDataError(ErrorCode.MATERIAL_NOT_PART_OF_INDENT.getCode(), poDetail.getMaterial().getCode() + poDetail.getIndentNumber());
                            }
                        }

                    //Supplier reference validation
                    if(null != eachPurchaseOrder.getSupplier() && !isEmpty(eachPurchaseOrder.getSupplier().getCode()))
                    if(!isValidSupplier(tenantId, eachPurchaseOrder.getSupplier().getCode()))
                    	errors.addDataError(ErrorCode.INVALID_REF_VALUE.getCode(), "Supplier", eachPurchaseOrder.getSupplier().getCode());
                    
                    //RateType reference validation
                    if(eachPurchaseOrder.getRateType() != null)
                    if (!Arrays.stream(PriceList.RateTypeEnum.values()).anyMatch((t) -> t.equals(PriceList.RateTypeEnum.fromValue(eachPurchaseOrder.getRateType().toString())))) {
                        errors.addDataError(ErrorCode.INVALID_REF_VALUE.getCode(), "rateType", eachPurchaseOrder.getRateType().toString());
                    }

                    if(eachPurchaseOrder.getPurchaseOrderDetails() == null) {
                    	errors.addDataError(ErrorCode.NOT_NULL.getCode(), "purchaseOrderDetails", "null");
                    }
                    
                    if (null != eachPurchaseOrder.getPurchaseOrderDetails()) {
                        for (PurchaseOrderDetail poDetail : eachPurchaseOrder.getPurchaseOrderDetails()) {
                            int detailIndex = eachPurchaseOrder.getPurchaseOrderDetails().indexOf(poDetail) + 1;

                            //validating ratecontracts for each POLine
                            boolean isRateContractExist = false;
                            if (purchaseOrderRepository.isRateContractsExists(eachPurchaseOrder.getSupplier().getCode(), eachPurchaseOrder.getRateType().toString(), poDetail.getMaterial().getCode())) {
                                isRateContractExist = true;
                                break;
                            }
                            if (!isRateContractExist)
                                errors.addDataError(ErrorCode.OBJECT_NOT_FOUND_COMBINATION.getCode(), "rateContract", "Supplier " + eachPurchaseOrder.getSupplier().getCode(), "RateType" + eachPurchaseOrder.getRateType().toString() + " at row " + detailIndex);


                            //Validating the POLine price with that in the ratecontract
                            if (poDetail.getUnitPrice().compareTo(new BigDecimal(poDetail.getPriceList().getPriceListDetails().get(0).getRatePerUnit())) != 0) {
                                errors.addDataError(ErrorCode.UNITPRICE_EQ_PLDRATE.getCode(), "unitprice" + "ratecontractprice" + poDetail.getUnitPrice() + poDetail.getPriceList().getPriceListDetails().get(0).getRatePerUnit());
                            }


                            //validation of order quantity incase of tender
                            if(poDetail == null || poDetail.getTenderQuantity() != null) {
                            	if ((poDetail.getOrderQuantity().add(poDetail.getUsedQuantity())).compareTo(poDetail.getTenderQuantity()) > 0) {
                                    errors.addDataError(ErrorCode.ORDQTY_LE_TENDQTY.getCode(), poDetail.getOrderQuantity().toString() + " at serial no." + detailIndex);
                                }
                            } else {
                            	errors.addDataError(ErrorCode.NOT_NULL.getCode(), "tenderQuantity", "null");
                            }
                            

                            //Material reference validation
                            if (poDetail == null || null != poDetail.getMaterial()) {
                                if (validateMaterial(tenantId, poDetail.getMaterial())) {
                                    errors.addDataError(ErrorCode.MATERIAL_NAME_NOT_EXIST.getCode(), poDetail.getMaterial().getCode() + " at serial no." + detailIndex);
                                }
                            }

                            if (null != poDetail.getMaterial() && StringUtils.isEmpty(poDetail.getMaterial().getCode())) {
                                errors.addDataError(ErrorCode.MAT_DETAIL.getCode(), " at serial no." + detailIndex);
                            }
                            
                            if(poDetail == null || null == poDetail.getReceivedQuantity() || poDetail.getReceivedQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                            	errors.addDataError(ErrorCode.NOT_NULL.getCode(), "receivedQuantity", "null");
                            }

                            //ratecontract mandatory, then rate, price, quantity are mandatory at each line level
                            if (priceListConfig && (poDetail == null || null == poDetail.getPriceList().getId())) {
                                errors.addDataError(ErrorCode.RATE_CONTRACT.getCode(), " at serial no." + detailIndex);
                            }

                            if (priceListConfig && (poDetail == null || null == poDetail.getOrderQuantity() || poDetail.getOrderQuantity().compareTo(BigDecimal.ZERO) <= 0)) {
                                errors.addDataError(ErrorCode.NOT_NULL.getCode(), "orderQuantity", "null");
                            }

                            if (priceListConfig && (poDetail == null || null == poDetail.getUnitPrice() || poDetail.getUnitPrice().compareTo(BigDecimal.ZERO) <= 0)) {
                                errors.addDataError(ErrorCode.NOT_NULL.getCode(), "unitPrice", "null");
                            }

                            //validate the rates entered for creating PO with the one's in pricelist
                            if (poDetail != null && poDetail.getPriceList() != null && poDetail.getPriceList().getPriceListDetails() != null)
                                for (PriceListDetails pld : poDetail.getPriceList().getPriceListDetails()) {
                                    if (pld.getMaterial().getCode() != null && poDetail.getMaterial().getCode() != null)
                                        if (pld.getMaterial().getCode().equals(poDetail.getMaterial().getCode()) && pld.getRatePerUnit().compareTo(poDetail.getUnitPrice().doubleValue()) != 0) {
                                            errors.addDataError(ErrorCode.MAT_PRICELEST_CATGRY_MATCH.getCode(), poDetail.getMaterial().getCode(), poDetail.getUnitPrice().toString(), " at serial no." + detailIndex);
                                        }
                                }

                            if(eachPurchaseOrder.getPurchaseType() != null)
                            if (eachPurchaseOrder.getPurchaseType().toString().equals("Indent"))
                                if (poDetail != null && null != poDetail.getOrderQuantity() && null != poDetail.getIndentQuantity()) {
                                    int res = poDetail.getOrderQuantity().compareTo(poDetail.getIndentQuantity());
                                    if (res == 1) {
                                        errors.addDataError(ErrorCode.ORDQTY_LE_INDQTY.getCode(), poDetail.getIndentQuantity().toString() + " at serial no." + detailIndex);
                                    }
                                }
                            totalAmount = totalAmount.add(poDetail.getOrderQuantity().multiply(poDetail.getUnitPrice()).add(totalAmount));
                        }
                        eachPurchaseOrder.setTotalAmount(totalAmount);
                    } else
                        errors.addDataError(ErrorCode.NOT_NULL.getCode(), "purchaseOrdersDetail", null);
                }


        } catch (IllegalArgumentException e) {

        }
        if (errors.getValidationErrors().size() > 0)
            throw errors;

    }


    // GET INDENT OBJECTS BY PASSING ID'S
    // Iterate through each indent
    // supplier and rate type is required. -- mandatory fields.?????
    // for each indent check available quantity to procure.COMBINE
    // MATERIALS WHICH ARE NOT ISSUED. Build indent detail list.
    // for each material, group together and build indent detail and po
    // link . Save indent number at po detail level. ?
    // for each material, check rate contract and amount detail by
    // supplier. If not found, skip that material.
    // Get rate contract details for each line
    // Tender quantity and used quantity should be required.
    // Show unit rate based on rate contract.
    // Get available quantity for each material- ?
    // Send indent detail list for each row of po line.
    // Build Po reponse object and send back.

    public PurchaseOrderResponse preparePoFromIndents(PurchaseOrderRequest purchaseOrderRequest, String tenantId) {
        PurchaseOrderResponse response = new PurchaseOrderResponse();
        response.setResponseInfo(getResponseInfo(purchaseOrderRequest.getRequestInfo()));

        List<PurchaseOrder> purchaseOrders = purchaseOrderRequest.getPurchaseOrders();
        validate(purchaseOrders, Constants.ACTION_SEARCH_INDENT_FOR_PO, tenantId);
        List<PurchaseOrder> finalPurchaseOrders = new ArrayList<PurchaseOrder>();

        for (PurchaseOrder purchaseOrder : purchaseOrders) {
            InvalidDataException errors = new InvalidDataException();
            // TODO: validation of supplier, rate type and indents.

            IndentSearch indentSearch = new IndentSearch();
            indentSearch.setIds(purchaseOrder.getIndentNumbers());
            indentSearch.setTenantId(tenantId);
            IndentResponse indentResponse = indentService.search(indentSearch, purchaseOrderRequest.getRequestInfo());

            Map<String, PurchaseOrderDetail> purchaseOrderLines = new HashMap<String, PurchaseOrderDetail>();

            // configure to check whether full quantity of indent to be consider or quantity pending ( out of issue) used to create po.

            // convert all items into purchase uom by referring each indent.

            //Logic to check if indent lines are valid for PO Creation
            for (String indentNo : purchaseOrder.getIndentNumbers()) {
                if (!purchaseOrderRepository.getIsIndentValidForPOCreate(indentNo)) {
                    errors.addDataError(ErrorCode.INVALID_INDENT_VALUE.getCode(), "indentNumber", indentNo);
                }
            }

            for (Indent indent : indentResponse.getIndents()) {

                for (IndentDetail indentDetail : indent.getIndentDetails()) {
                    PurchaseOrderDetail purchaseOrderDetail = null;
                    BigDecimal pendingQty = BigDecimal.ZERO;

                    // Consider full indent quantity to be used in poorderquantity
                    if (!isTotalIndentQuantityUsedInPo) {
                        if (indentDetail != null && indentDetail.getIndentQuantity()
                                .compareTo(indentDetail.getPoOrderedQuantity() != null
                                        ? indentDetail.getPoOrderedQuantity() : BigDecimal.ZERO) > 0) {
                            pendingQty = indentDetail.getIndentQuantity()
                                    .subtract(indentDetail.getPoOrderedQuantity() != null
                                            ? indentDetail.getPoOrderedQuantity() : BigDecimal.ZERO);

                        }
                    } else {

                        if (indentDetail != null && (indentDetail.getIndentQuantity()
                                .subtract(indentDetail.getTotalProcessedQuantity() != null
                                        ? indentDetail.getTotalProcessedQuantity() : BigDecimal.ZERO)
                                .compareTo(indentDetail.getPoOrderedQuantity() != null
                                        ? indentDetail.getPoOrderedQuantity() : BigDecimal.ZERO) > 0)) {
                            pendingQty = indentDetail.getIndentQuantity()
                                    .subtract(indentDetail.getTotalProcessedQuantity() != null
                                            ? indentDetail.getTotalProcessedQuantity() : BigDecimal.ZERO)
                                    .subtract(indentDetail.getPoOrderedQuantity() != null
                                            ? indentDetail.getPoOrderedQuantity() : BigDecimal.ZERO);

                        }

                    }

                    if (pendingQty.compareTo(BigDecimal.ZERO) > 0) {

                    	if (purchaseOrderLines.get(indentDetail.getMaterial().getCode()) == null) {
                            purchaseOrderDetail = new PurchaseOrderDetail();
                            purchaseOrderDetail.setMaterial(indentDetail.getMaterial());
                            purchaseOrderDetail.setUom(indentDetail.getMaterial().getPurchaseUom());
                            purchaseOrderDetail.setIndentQuantity(InventoryUtilities.getQuantityInSelectedUom(pendingQty,
                                    indentDetail.getMaterial().getPurchaseUom().getConversionFactor()));

                            purchaseOrderDetail.setIndentNumber(indent.getIndentNumber());
                            purchaseOrderDetail.setTenantId(tenantId);
                            buildPurchaseOrderIndentDetail(purchaseOrderRequest, indentDetail,
                                    purchaseOrderDetail, pendingQty, tenantId);
                            purchaseOrderLines.put(indentDetail.getMaterial().getCode(), purchaseOrderDetail);
                            purchaseOrder.addPurchaseOrderDetailsItem(purchaseOrderDetail);
                        } else {
                            purchaseOrderDetail = purchaseOrderLines.get(indentDetail.getMaterial().getCode());
                            purchaseOrderDetail.setIndentQuantity(purchaseOrderDetail.getIndentQuantity().add(InventoryUtilities.getQuantityInSelectedUom(pendingQty,
                                    indentDetail.getMaterial().getPurchaseUom().getConversionFactor())));
                            purchaseOrderDetail.setIndentNumber(INDENT_MULTIPLE);
                            buildPurchaseOrderIndentDetail(purchaseOrderRequest, indentDetail,
                                    purchaseOrderDetail, pendingQty, tenantId);
                        }
                    }
                }

            }
            
            if (errors.getValidationErrors().size() > 0)
                throw errors;

            finalPurchaseOrders.add(purchaseOrder);
        }

        response.setPurchaseOrders(finalPurchaseOrders);

        return response;
    }

    public PurchaseOrderResponse getPOsForAdvanceRequisition(String tenantId) {
        PurchaseOrderResponse response = new PurchaseOrderResponse();
        Pagination<PurchaseOrder> search = purchaseOrderRepository.searchPOsForAdvanceRequisition(tenantId);
        response.setPurchaseOrders(search.getPagedData());
        response.setPage(getPage(search));
        return response;
    }
    
    public Boolean checkAllItemsSuppliedForPo(PurchaseOrderSearch purchaseOrderSearch) {

        Boolean flag = true;

        PurchaseOrderResponse search = search(purchaseOrderSearch);

        if (search.getPurchaseOrders().size() > 0) {
            List<PurchaseOrder> purchaseOrders = search.getPurchaseOrders();

            for (PurchaseOrder purchaseOrder : purchaseOrders) {
                for (PurchaseOrderDetail purchaseOrderDetail : purchaseOrder.getPurchaseOrderDetails()) {
                    if (null != purchaseOrderDetail.getReceivedQuantity() &&
                            purchaseOrderDetail.getOrderQuantity().compareTo(purchaseOrderDetail.getReceivedQuantity()) != 0) {
                        return false;
                    }
                }
            }
        } else
            throw new CustomException("po", "Purchase order not found");

        return flag;
    }


    public PurchaseOrderResponse getPOPendingToDeliver(PurchaseOrderSearch purchaseOrderSearch) {

        PurchaseOrderResponse search = search(purchaseOrderSearch);

        if (search.getPurchaseOrders().size() > 0) {
            List<PurchaseOrder> purchaseOrders = search.getPurchaseOrders();

            for (PurchaseOrder purchaseOrder : purchaseOrders) {
                for (PurchaseOrderDetail purchaseOrderDetail : purchaseOrder.getPurchaseOrderDetails()) {
                    if (purchaseOrderDetail.getOrderQuantity().compareTo(purchaseOrderDetail.getReceivedQuantity()) == 0) {
                        purchaseOrder.getPurchaseOrderDetails().remove(purchaseOrderDetail);
                    }
                }
            }
        } else
            throw new CustomException("po", "Purchase order not found");

        return search;
    }

    private boolean validateMaterial(String tenantId, Material material) {
        Material materialFromMdms = materialService.fetchMaterial(tenantId, material.getCode(), new org.egov.inv.model.RequestInfo());
        if (materialFromMdms == null) {
            return false;
        }
        if (material.getBaseUom().getCode() != null)
            if (!materialFromMdms.getBaseUom().getCode().equals(material.getBaseUom().getCode())) {
                return false;
            }
        return true;
    }
    
	private boolean isValidStore(String tenantId, String storeCode) {
		StoreGetRequest storeEntity = StoreGetRequest.builder()
											 .code(Collections.singletonList(storeCode))
										     .tenantId(tenantId)
										     .active(true)
											 .build();
		Pagination<Store> stores = storeJdbcRepository.search(storeEntity);
		if(stores.getPagedData().size() > 0)
		{
			return true;
		}
		return false;
	}
	
	private boolean isValidSupplier(String tenantId, String supplierCode) {
		 SupplierGetRequest supplierGetRequest = SupplierGetRequest.builder()
				 									.code(Collections.singletonList(supplierCode))
				 									.tenantId(tenantId)
				 									.active(true)
				 									.build();
         Pagination<Supplier> suppliers = supplierJdbcRepository.search(supplierGetRequest);
         if(suppliers.getPagedData().size() > 0)
        	 return true;
         return false;
	}

    private void buildPurchaseOrderIndentDetail(PurchaseOrderRequest purchaseOrderRequest,
                                                IndentDetail indentDetail, PurchaseOrderDetail purchaseOrderDetail, BigDecimal pendingQty, String tenantId) {
        PurchaseIndentDetail poIndentDetail = new PurchaseIndentDetail();
        poIndentDetail.setTenantId(tenantId);
        poIndentDetail.setAuditDetails(
                getAuditDetails(purchaseOrderRequest.getRequestInfo(), Constants.ACTION_CREATE));
        poIndentDetail.setQuantity(pendingQty);
        poIndentDetail.setIndentDetail(indentDetail);
        purchaseOrderDetail.addPurchaseIndentDetailsItem(poIndentDetail);
    }

    private String appendString(PurchaseOrder poOrder) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        String code = "PO/";
        int id = Integer.valueOf(materialJdbcRepository.getSequence(poOrder));
        String idgen = String.format("%05d", id);
        String purchaseOrderNumber = code + idgen + "/" + year;
        return purchaseOrderNumber;
    }

    private String convertEpochtoDate(Long date) {
        Date epoch = new Date(date);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String s2 = format.format(epoch);
        return s2;
    }
    
	private void populatePriceListDetails(PurchaseOrderDetail purchaseOrderDetail) {
		if (purchaseOrderDetail.getPriceList() != null) {
			if (!isEmpty(purchaseOrderDetail.getPriceList().getRateContractNumber())&& purchaseOrderDetail.getPriceList().getRateContractNumber() != null) {
				PriceListSearchRequest pls = PriceListSearchRequest.builder().rateContractNumber(purchaseOrderDetail.getPriceList().getRateContractNumber()).build();
				PriceList pl = priceListjdbcRepository.search(pls).getPagedData().get(0);
				for (Iterator<PriceListDetails> iterator = pl.getPriceListDetails().iterator(); iterator.hasNext();) {
					PriceListDetails pld = iterator.next();
					if (!pld.getMaterial().getCode().equals(purchaseOrderDetail.getMaterial().getCode())) {
						iterator.remove();
					}
				}
				purchaseOrderDetail.setPriceList(pl);
			}
		}
	}

}