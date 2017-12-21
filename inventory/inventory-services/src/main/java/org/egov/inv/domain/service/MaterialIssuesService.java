package org.egov.inv.domain.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.Constants;
import org.egov.common.DomainService;
import org.egov.common.MdmsRepository;
import org.egov.common.Pagination;
import org.egov.common.exception.CustomBindException;
import org.egov.common.exception.ErrorCode;
import org.egov.common.exception.InvalidDataException;
import org.egov.inv.model.Department;
import org.egov.inv.model.Indent.IndentStatusEnum;
import org.egov.inv.model.IndentDetail;
import org.egov.inv.model.IndentSearch;
import org.egov.inv.model.Material;
import org.egov.inv.model.MaterialIssue;
import org.egov.inv.model.MaterialIssue.IssueTypeEnum;
import org.egov.inv.model.MaterialIssue.MaterialIssueStatusEnum;
import org.egov.inv.model.MaterialIssueDetail;
import org.egov.inv.model.MaterialIssueRequest;
import org.egov.inv.model.MaterialIssueResponse;
import org.egov.inv.model.MaterialIssueSearchContract;
import org.egov.inv.model.MaterialIssuedFromReceipt;
import org.egov.inv.model.MaterialReceiptDetail;
import org.egov.inv.model.RequestInfo;
import org.egov.inv.model.Store;
import org.egov.inv.model.StoreGetRequest;
import org.egov.inv.model.Uom;
import org.egov.inv.persistence.entity.FifoEntity;
import org.egov.inv.persistence.entity.IndentDetailEntity;
import org.egov.inv.persistence.entity.IndentEntity;
import org.egov.inv.persistence.entity.MaterialIssueEntity;
import org.egov.inv.persistence.repository.IndentDetailJdbcRepository;
import org.egov.inv.persistence.repository.MaterialIssueDetailsJdbcRepository;
import org.egov.inv.persistence.repository.MaterialIssueJdbcRepository;
import org.egov.inv.persistence.repository.MaterialIssuedFromReceiptsJdbcRepository;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.tracer.model.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONArray;

@Service
public class MaterialIssuesService extends DomainService {

	private static final Logger LOG = LoggerFactory.getLogger(MaterialIssuesService.class);

	@Autowired
	private MaterialIssueJdbcRepository materialIssueJdbcRepository;

	@Autowired
	private IndentService indentService;

	@Autowired
	private MdmsRepository mdmsRepository;

	@Autowired
	private StoreService storeService;

	@Autowired
	private MaterialIssueDetailsJdbcRepository materialIssueDetailsJdbcRepository;

	@Autowired
	private MaterialIssuedFromReceiptsJdbcRepository materialIssuedFromReceiptsJdbcRepository;

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private MaterialIssueReceiptFifoLogic materialIssueReceiptFifoLogic;
	
	@Autowired
	private IndentDetailJdbcRepository indentDetailJdbcRepository;

	@Value("${inv.issues.save.topic}")
	private String createTopic;

	@Value("${inv.issues.save.key}")
	private String createKey;

	@Value("${inv.issues.update.topic}")
	private String updateTopic;

	@Value("${inv.issues.update.key}")
	private String updateKey;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	public MaterialIssueResponse create(final MaterialIssueRequest materialIssueRequest, String type) {
		try {
			fetchRelated(materialIssueRequest, Constants.ACTION_CREATE);
			validate(materialIssueRequest.getMaterialIssues(), Constants.ACTION_CREATE , type);
			List<String> sequenceNos = materialIssueJdbcRepository.getSequence(MaterialIssue.class.getSimpleName(),
					materialIssueRequest.getMaterialIssues().size());
			int i = 0;
			for (MaterialIssue materialIssue : materialIssueRequest.getMaterialIssues()) {
				String seqNo = sequenceNos.get(i);
				materialIssue.setId(seqNo);
				setMaterialIssueValues(materialIssue, seqNo, Constants.ACTION_CREATE ,type);
				materialIssue.setAuditDetails(mapAuditDetails(materialIssueRequest.getRequestInfo()));
				i++;
				int j = 0;
				BigDecimal totalIssueValue = BigDecimal.ZERO;
				if (!materialIssue.getMaterialIssueDetails().isEmpty()) {
					List<String> detailSequenceNos = materialIssueDetailsJdbcRepository.getSequence(
							MaterialIssueDetail.class.getSimpleName(), materialIssue.getMaterialIssueDetails().size());
					for (MaterialIssueDetail materialIssueDetail : materialIssue.getMaterialIssueDetails()) {
						materialIssueDetail.setId(detailSequenceNos.get(j));
						materialIssueDetail.setTenantId(materialIssue.getTenantId());
						BigDecimal value = getMaterialIssuedFromReceiptData(materialIssue.getFromStore(),
								materialIssueDetail.getMaterial(), materialIssue.getIssueDate(),
								materialIssue.getTenantId(), materialIssueDetail);
						totalIssueValue = totalIssueValue.add(value);
						materialIssueDetail.setValue(value);
						convertToUom(materialIssueDetail);
						backUpdateIndent(materialIssueDetail, materialIssue.getTenantId());
						j++;
					}
				}
				materialIssue.setTotalIssueValue(totalIssueValue);
			}
			kafkaTemplate.send(createTopic, createKey, materialIssueRequest);
			MaterialIssueResponse response = new MaterialIssueResponse();
			response.setMaterialIssues(materialIssueRequest.getMaterialIssues());
			response.setResponseInfo(getResponseInfo(materialIssueRequest.getRequestInfo()));
			return response;
		} catch (CustomBindException e) {
			throw e;
		}
	}

	private void backUpdateIndent(MaterialIssueDetail materialIssueDetail, String tenantId) {
		 HashMap<String, String> hashMap = new HashMap<>();
         hashMap.put("indentissuedquantity", "indentissuedquantity  + " + materialIssueDetail.getQuantityIssued().toString());
         materialIssueDetail.getIndentDetail().setTenantId(tenantId);
         materialIssueJdbcRepository.updateColumn(new IndentDetailEntity().toEntity(materialIssueDetail.getIndentDetail()), "indentdetail", hashMap, null);
	}

	private BigDecimal getMaterialIssuedFromReceiptData(Store store, Material material, Long issueDate, String tenantId,
			MaterialIssueDetail materialIssueDetail) {
		List<MaterialIssuedFromReceipt> materialIssuedFromReceipts = new ArrayList<>();
		List<FifoEntity> listOfFifoEntity = materialIssueReceiptFifoLogic.implementFifoLogic(store, material, issueDate,
				tenantId);

		BigDecimal unitRate = BigDecimal.ZERO;
		BigDecimal quantityIssued = materialIssueDetail.getQuantityIssued();
		for (FifoEntity fifoEntity : listOfFifoEntity) {
			MaterialIssuedFromReceipt materialIssuedFromReceipt = new MaterialIssuedFromReceipt();
			MaterialReceiptDetail materialReceiptDetail = new MaterialReceiptDetail();
			materialReceiptDetail.setId(fifoEntity.getReceiptDetailId());
			materialIssuedFromReceipt.setMaterialReceiptDetail(materialReceiptDetail);
			materialIssuedFromReceipt.setMaterialReceiptDetailAddnlinfoId(fifoEntity.getReceiptDetailAddnInfoId());
			materialIssuedFromReceipt.setId(materialIssuedFromReceiptsJdbcRepository
					.getSequence(MaterialIssuedFromReceipt.class.getSimpleName(), 1).get(0));
			materialIssuedFromReceipt.setTenantId(materialIssueDetail.getTenantId());
			materialIssuedFromReceipt.setStatus(true);
			materialIssuedFromReceipt.setMaterialReceiptId(fifoEntity.getReceiptId());
			if (quantityIssued.compareTo(BigDecimal.valueOf(getSearchConvertedQuantity(fifoEntity.getBalance(),
					Double.valueOf(materialIssueDetail.getUom().getConversionFactor().toString())))) >= 0) {
				materialIssuedFromReceipt
						.setQuantity(BigDecimal.valueOf(getSearchConvertedQuantity(fifoEntity.getBalance(),
								Double.valueOf(materialIssueDetail.getUom().getConversionFactor().toString()))));
				unitRate = BigDecimal.valueOf(getSearchConvertedRate(fifoEntity.getUnitRate(),
						Double.valueOf(materialIssueDetail.getUom().getConversionFactor().toString())))
						.multiply(BigDecimal.valueOf(getSearchConvertedQuantity(fifoEntity.getBalance(),
								Double.valueOf(materialIssueDetail.getUom().getConversionFactor().toString()))))
						.add(unitRate);
				quantityIssued = quantityIssued
						.subtract(BigDecimal.valueOf(getSearchConvertedQuantity(fifoEntity.getBalance(),
								Double.valueOf(materialIssueDetail.getUom().getConversionFactor().toString()))));
			} else {
				materialIssuedFromReceipt.setQuantity(quantityIssued);
				unitRate = quantityIssued
						.multiply(BigDecimal.valueOf(getSearchConvertedRate(fifoEntity.getUnitRate(),
								Double.valueOf(materialIssueDetail.getUom().getConversionFactor().toString())))).add(unitRate);
				quantityIssued = BigDecimal.ZERO;
			}
			materialIssuedFromReceipts.add(materialIssuedFromReceipt);
			if (quantityIssued.equals(BigDecimal.ZERO))
				break;
		}
		materialIssueDetail.setMaterialIssuedFromReceipts(materialIssuedFromReceipts);
		return unitRate.multiply(materialIssueDetail.getQuantityIssued());
	}

	private void fetchRelated(MaterialIssueRequest materialIssueRequest, String action) {
        if(action.equals(Constants.ACTION_CREATE)){
		for (MaterialIssue materialIssue : materialIssueRequest.getMaterialIssues()) {

			if (materialIssue.getIndent().getIssueStore() != null
					&& StringUtils.isNotBlank(materialIssue.getIndent().getIssueStore().getCode())) {
				StoreGetRequest storeGetRequest = new StoreGetRequest();
				storeGetRequest.setCode(Arrays.asList(materialIssue.getIndent().getIssueStore().getCode()));
				storeGetRequest.setTenantId(materialIssue.getTenantId());
				List<Store> stores = storeService.search(storeGetRequest).getStores();
				if (stores.isEmpty())
					throw new CustomException("invalid.ref.value",
							"the field issuestore should have a valid value which exists in the system.");
				else
					materialIssue.setFromStore(stores.get(0));

			}
			if (materialIssue.getIndent().getIndentStore() != null
					&& StringUtils.isNotBlank(materialIssue.getIndent().getIndentStore().getCode())) {
				StoreGetRequest storeGetRequest = new StoreGetRequest();
				storeGetRequest.setCode(Arrays.asList(materialIssue.getIndent().getIndentStore().getCode()));
				storeGetRequest.setTenantId(materialIssue.getTenantId());
				List<Store> stores = storeService.search(storeGetRequest).getStores();
				if (stores.isEmpty())
					throw new CustomException("invalid.ref.value",
							"the field indentstore should have a valid value which exists in the system.");
				else
					materialIssue.setToStore(stores.get(0));
			}
			if (!materialIssue.getMaterialIssueDetails().isEmpty()) {
				ObjectMapper mapper = new ObjectMapper();
				Map<String, Material> materialMap = getMaterials(materialIssue.getTenantId(), mapper,
						new RequestInfo());
				Map<String, Uom> uomMap = getUoms(materialIssue.getTenantId(), mapper, new RequestInfo());
				for (MaterialIssueDetail materialIssueDetail : materialIssue.getMaterialIssueDetails()) {
					if (materialIssueDetail.getIndentDetail().getMaterial() != null
							&& StringUtils.isNotBlank(materialIssueDetail.getIndentDetail().getMaterial().getCode())) {
						if (materialMap.get(materialIssueDetail.getIndentDetail().getMaterial().getCode()) == null)
							throw new CustomException("invalid.ref.value",
									"the field material should have a valid value which exists in the system.");

						else
							materialIssueDetail.setMaterial(
									materialMap.get(materialIssueDetail.getIndentDetail().getMaterial().getCode()));
					}
					if (materialIssueDetail.getIndentDetail().getUom() != null
							&& StringUtils.isNotBlank(materialIssueDetail.getIndentDetail().getUom().getCode())) {
						if (uomMap.get(materialIssueDetail.getIndentDetail().getUom().getCode()) == null)
							throw new CustomException("invalid.ref.value",
									"the field uom should have a valid value which exists in the system.");

						else
							materialIssueDetail
									.setUom(uomMap.get(materialIssueDetail.getIndentDetail().getUom().getCode()));
					}
				}
			}

		}
        }
        else{
        	for (MaterialIssue materialIssue : materialIssueRequest.getMaterialIssues()) {

    			if (materialIssue.getFromStore() != null
    					&& StringUtils.isNotBlank(materialIssue.getFromStore().getCode())) {
    				StoreGetRequest storeGetRequest = new StoreGetRequest();
    				storeGetRequest.setCode(Arrays.asList(materialIssue.getFromStore().getCode()));
    				storeGetRequest.setTenantId(materialIssue.getTenantId());
    				List<Store> stores = storeService.search(storeGetRequest).getStores();
    				if (stores.isEmpty())
    					throw new CustomException("invalid.ref.value",
    							"the field issuestore should have a valid value which exists in the system.");
    				else
    					materialIssue.setFromStore(stores.get(0));

    			}
    			if (materialIssue.getToStore() != null
    					&& StringUtils.isNotBlank(materialIssue.getToStore().getCode())) {
    				StoreGetRequest storeGetRequest = new StoreGetRequest();
    				storeGetRequest.setCode(Arrays.asList(materialIssue.getToStore().getCode()));
    				storeGetRequest.setTenantId(materialIssue.getTenantId());
    				List<Store> stores = storeService.search(storeGetRequest).getStores();
    				if (stores.isEmpty())
    					throw new CustomException("invalid.ref.value",
    							"the field indentstore should have a valid value which exists in the system.");
    				else
    					materialIssue.setToStore(stores.get(0));
    			}
    			if (!materialIssue.getMaterialIssueDetails().isEmpty()) {
    				ObjectMapper mapper = new ObjectMapper();
    				Map<String, Material> materialMap = getMaterials(materialIssue.getTenantId(), mapper,
    						new RequestInfo());
    				Map<String, Uom> uomMap = getUoms(materialIssue.getTenantId(), mapper, new RequestInfo());
    				for (MaterialIssueDetail materialIssueDetail : materialIssue.getMaterialIssueDetails()) {
    					if (materialIssueDetail.getMaterial() != null
    							&& StringUtils.isNotBlank(materialIssueDetail.getMaterial().getCode())) {
    						if (materialMap.get(materialIssueDetail.getMaterial().getCode()) == null)
    							throw new CustomException("invalid.ref.value",
    									"the field material should have a valid value which exists in the system.");

    						else
    							materialIssueDetail.setMaterial(
    									materialMap.get(materialIssueDetail.getMaterial().getCode()));
    					}
    					if (materialIssueDetail.getUom() != null
    							&& StringUtils.isNotBlank(materialIssueDetail.getUom().getCode())) {
    						if (uomMap.get(materialIssueDetail.getUom().getCode()) == null)
    							throw new CustomException("invalid.ref.value",
    									"the field uom should have a valid value which exists in the system.");

    						else
    							materialIssueDetail
    									.setUom(uomMap.get(materialIssueDetail.getUom().getCode()));
    					}
    				}
    			}

    		}
        }

	}

	private void setMaterialIssueValues(MaterialIssue materialIssue, String seqNo, String action, String type) {
		if(type .equals(IssueTypeEnum.INDENTISSUE.toString()))
		materialIssue.setIssueType(IssueTypeEnum.INDENTISSUE);
		else
			materialIssue.setIssueType(IssueTypeEnum.MATERIALOUTWARD);
		if (StringUtils.isNotBlank(materialIssue.getIndent().getIndentCreatedBy()))
			materialIssue.setIssuedToEmployee(materialIssue.getIndent().getIndentCreatedBy());
		if (StringUtils.isNotBlank(materialIssue.getIndent().getDesignation()))
			materialIssue.setIssuedToDesignation(materialIssue.getIndent().getDesignation());
		if (action.equals(Constants.ACTION_CREATE)) {
			int year = Calendar.getInstance().get(Calendar.YEAR);
			materialIssue.setIssueNumber("MRIN-" + String.valueOf(year) + "-" + seqNo);
			materialIssue.setMaterialIssueStatus(MaterialIssueStatusEnum.CREATED);
		}
	}

	private void convertToUom(MaterialIssueDetail materialIssueDetail) {
		Double quantityIssued = 0d;
		Double quantity = 0d;
		if (materialIssueDetail.getUom() != null && StringUtils.isNotEmpty(materialIssueDetail.getUom().getCode())) {
			if (materialIssueDetail.getQuantityIssued() != null)
				quantityIssued = getSaveConvertedQuantity(
						Double.valueOf(materialIssueDetail.getQuantityIssued().toString()),
						Double.valueOf(materialIssueDetail.getUom().getConversionFactor().toString()));
			materialIssueDetail.setQuantityIssued(BigDecimal.valueOf(quantityIssued));
			if (!materialIssueDetail.getMaterialIssuedFromReceipts().isEmpty())
				for (MaterialIssuedFromReceipt mifr : materialIssueDetail.getMaterialIssuedFromReceipts()) {
					if (mifr.getQuantity() != null && materialIssueDetail.getUom().getConversionFactor() != null)
						quantity = getSaveConvertedQuantity(Double.valueOf(mifr.getQuantity().toString()),
								Double.valueOf(materialIssueDetail.getUom().getConversionFactor().toString()));
					mifr.setQuantity(BigDecimal.valueOf(quantity));
				}
		}
	}

	private void validate(List<MaterialIssue> materialIssues, String method, String type) {
		InvalidDataException errors = new InvalidDataException();
		try {
			Long currentDate = currentEpochWithoutTime();
			currentDate = currentDate + (24 * 60 * 60 * 1000) - 1;
			LOG.info("CurrentDate is " + toDateStr(currentDate));
			switch (method) {
			case "create":
				if (materialIssues == null) {
					errors.addDataError(ErrorCode.NOT_NULL.getCode(), "materialIssues", "null");
				}
				for (MaterialIssue materialIssue : materialIssues) {
					if (materialIssue.getIssueDate().compareTo(currentDate) > 0) {
						String date = convertEpochtoDate(materialIssue.getIssueDate());
						errors.addDataError(ErrorCode.DATE_LE_CURRENTDATE.getCode(), "issueDate", date);
					}
					if(materialIssue.getIndent() != null &&  StringUtils.isNotBlank(materialIssue.getIndent().getIndentNumber() ))
					{
						IndentEntity indentEntity = new IndentEntity();
						indentEntity.setIndentNumber(materialIssue.getIndent().getIndentNumber());
						indentEntity.setTenantId(materialIssue.getTenantId());
						Object indenttEntity = materialIssueJdbcRepository.findById(indentEntity,
								"IndentEntity");
						IndentEntity indentEntityfromDb = (IndentEntity) indenttEntity;
						if (indentEntityfromDb != null) {
							if(!indentEntityfromDb.getIndentStatus().equals(IndentStatusEnum.APPROVED.toString()))
								errors.addDataError(ErrorCode.INDENT_NOT_APPROVED.getCode(), materialIssue.getIndent().getIndentNumber());
						}
						BigDecimal totalIndentQuantity = BigDecimal.ZERO;
						List <IndentDetailEntity> indentDetailEntity =indentDetailJdbcRepository.find(Arrays.asList(materialIssue.getIndent().getIndentNumber()), materialIssue.getTenantId(), null);
				if(!indentDetailEntity.isEmpty())
				{
					for(IndentDetailEntity indentDetailsEntity :indentDetailEntity){
						BigDecimal quantity =  BigDecimal.ZERO;
						IndentDetail indentDetail = indentDetailsEntity.toDomain();
						if(indentDetail.getIndentQuantity() != null && indentDetail.getIndentIssuedQuantity() != null)
						 quantity =	indentDetail.getIndentQuantity().subtract(indentDetail.getIndentIssuedQuantity());
						totalIndentQuantity = totalIndentQuantity.add(quantity);
					}
				}
				if(totalIndentQuantity.compareTo(BigDecimal.ZERO) == 0)
					errors.addDataError(ErrorCode.NO_ITEMS_TO_ISSUE.getCode());
					}
					if(type.equals(IssueTypeEnum.MATERIALOUTWARD.toString()))
					{
						if(materialIssue.getToStore() == null) 
							errors.addDataError(ErrorCode.MANDATORY_BASED_ON.getCode(), "toStore", "issueType - Transfer Outward", "");
							else{
								if(StringUtils.isBlank(materialIssue.getToStore().getCode()))
							errors.addDataError(ErrorCode.MANDATORY_BASED_ON.getCode(), "toStore", "issueType - Transfer Outward", "");
							}
						if(materialIssue.getFromStore() == null) 
							errors.addDataError(ErrorCode.MANDATORY_BASED_ON.getCode(), "fromStore", "issueType - Transfer Outward", "");
							else{
								if(StringUtils.isBlank(materialIssue.getFromStore().getCode()))
							errors.addDataError(ErrorCode.MANDATORY_BASED_ON.getCode(), "fromStore", "issueType - Transfer Outward", "");
							}
						if(materialIssue.getFromStore() != null  &&
								materialIssue.getFromStore().getActive() != null){
						if(!materialIssue.getFromStore().getActive())
							errors.addDataError(ErrorCode.ACTIVE_STORES_ALLOWED.getCode(), "fromStore");
						}
						if(materialIssue.getToStore() != null &&
								materialIssue.getToStore().getActive() != null){
							if(!materialIssue.getToStore().getActive())
								errors.addDataError(ErrorCode.ACTIVE_STORES_ALLOWED.getCode(), "toStore");
							}
					}
					
					if (!materialIssue.getMaterialIssueDetails().isEmpty()) {
						int i = 1;
						for (MaterialIssueDetail materialIssueDetail : materialIssue.getMaterialIssueDetails()) {
							if (materialIssueDetail.getQuantityIssued().compareTo(BigDecimal.ZERO) <= 0)
								errors.addDataError(ErrorCode.QUANTITY_GT_ZERO.getCode(), "quantityIssued",
										String.valueOf(i), materialIssueDetail.getQuantityIssued().toString());
							if(materialIssueDetail.getMaterial() != null && materialIssueDetail.getMaterial().getScrapable() != null)
							if (materialIssueDetail.getMaterial().getScrapable())
								errors.addDataError(ErrorCode.DONT_ALLOW_SCRAP_MATERIALS.getCode(), String.valueOf(i));
							if (StringUtils.isNotBlank(materialIssueDetail.getBalanceQuantity().toString())) {
								if (materialIssueDetail.getBalanceQuantity().compareTo(BigDecimal.ZERO) <= 0)
									errors.addDataError(ErrorCode.QUANTITY_GT_ZERO.getCode(), "balanceQuantity",
											String.valueOf(i), materialIssueDetail.getBalanceQuantity().toString());
								if (materialIssueDetail.getQuantityIssued()
										.compareTo(materialIssueDetail.getBalanceQuantity()) > 0) {
									errors.addDataError(ErrorCode.QUANTITY1_LTE_QUANTITY2.getCode(), "quantityIssued",
											"balanceQuantity", materialIssueDetail.getQuantityIssued().toString(),
											materialIssueDetail.getBalanceQuantity().toString(), String.valueOf(i));
								}
							}
							if (materialIssueDetail.getIndentDetail() != null) {
								if (materialIssueDetail.getQuantityIssued()
										.compareTo(materialIssueDetail.getIndentDetail().getIndentQuantity()) > 0)
									errors.addDataError(ErrorCode.QUANTITY1_LTE_QUANTITY2.getCode(), "quantityIssued",
											"indentQuantity", materialIssueDetail.getQuantityIssued().toString(),
											materialIssueDetail.getIndentDetail().getIndentQuantity().toString(),
											String.valueOf(i));
								if (materialIssueDetail.getIndentDetail().getIndentQuantity()
										.compareTo(BigDecimal.ZERO) <= 0)
									errors.addDataError(ErrorCode.QUANTITY_GT_ZERO.getCode(), "indentQuantity",
											String.valueOf(i),
											materialIssueDetail.getIndentDetail().getIndentQuantity().toString());
							}
							i++;
						}
						validateDuplicateMaterials(materialIssue.getMaterialIssueDetails(), materialIssue.getTenantId(),
								errors);
					}
				}

				break;
			case "update":
				for (MaterialIssue materialIssue : materialIssues) {
					if (StringUtils.isEmpty(materialIssue.getIssueNumber()))
						errors.addDataError(ErrorCode.NOT_NULL.getCode(), "issueNumber", "null");
					if (materialIssue.getIssueDate().compareTo(currentDate) > 0) {
						String date = convertEpochtoDate(materialIssue.getIssueDate());
						errors.addDataError(ErrorCode.DATE_LE_CURRENTDATE.getCode(), "issueDate", date);
					}
				    if(type != null)
					if(type.equals(IssueTypeEnum.MATERIALOUTWARD.toString()))
					{
						if(materialIssue.getToStore() == null) 
							errors.addDataError(ErrorCode.MANDATORY_BASED_ON.getCode(), "toStore", "issueType - Transfer Outward", "");
							else{
								if(StringUtils.isBlank(materialIssue.getToStore().getCode()))
							errors.addDataError(ErrorCode.MANDATORY_BASED_ON.getCode(), "toStore", "issueType - Transfer Outward", "");
							}
						if(materialIssue.getFromStore() == null) 
							errors.addDataError(ErrorCode.MANDATORY_BASED_ON.getCode(), "fromStore", "issueType - Transfer Outward", "");
							else{
								if(StringUtils.isBlank(materialIssue.getFromStore().getCode()))
							errors.addDataError(ErrorCode.MANDATORY_BASED_ON.getCode(), "fromStore", "issueType - Transfer Outward", "");
							}
						if(materialIssue.getFromStore() != null  &&
								materialIssue.getFromStore().getActive() != null){
						if(!materialIssue.getFromStore().getActive())
							errors.addDataError(ErrorCode.ACTIVE_STORES_ALLOWED.getCode(), "fromStore");
						}
						if(materialIssue.getToStore() != null &&
								materialIssue.getToStore().getActive()){
							if(!materialIssue.getToStore().getActive())
								errors.addDataError(ErrorCode.ACTIVE_STORES_ALLOWED.getCode(), "toStore");
							}
					}
					if (materialIssue.getIssueNumber() != null) {
						MaterialIssueEntity materialIssueEntity = new MaterialIssueEntity();
						materialIssueEntity.setIssueNumber(materialIssue.getIssueNumber());
						materialIssueEntity.setTenantId(materialIssue.getTenantId());
						Object issueEntity = materialIssueJdbcRepository.findById(materialIssueEntity,
								"MaterialIssueEntity");
						MaterialIssueEntity issueEntityfromDb = (MaterialIssueEntity) issueEntity;
						if (issueEntityfromDb != null) {
							if (materialIssue.getIssueType() != null) {
								if (!issueEntityfromDb.getIssueType().equals(materialIssue.getIssueType()))
									errors.addDataError(ErrorCode.NOT_ALLOWED_TO_UPDATE.getCode(), "issueType",
											"MaterialIssue");
							}
							if (materialIssue.getSupplier() != null && materialIssue.getSupplier().getCode() != null) {
								if (!issueEntityfromDb.getSupplier().equals(materialIssue.getSupplier().getCode()))
									errors.addDataError(ErrorCode.NOT_ALLOWED_TO_UPDATE.getCode(), "supplier",
											"MaterialIssue");
							}
						}
						if (materialIssue.getIssueDate() != null) {
							if (!issueEntityfromDb.getIssueDate().equals(materialIssue.getIssueDate()))
								errors.addDataError(ErrorCode.NOT_ALLOWED_TO_UPDATE.getCode(), "issueDate",
										"MaterialIssue");
						}
					}
					if (!materialIssue.getMaterialIssueDetails().isEmpty()) {
						int i = 1;
						for (MaterialIssueDetail materialIssueDetail : materialIssue.getMaterialIssueDetails()) {
							if (materialIssueDetail.getQuantityIssued().compareTo(BigDecimal.ZERO) <= 0)
								errors.addDataError(ErrorCode.QUANTITY_GT_ZERO.getCode(), "quantityIssued",
										String.valueOf(i), materialIssueDetail.getQuantityIssued().toString());
							if(materialIssueDetail.getMaterial() != null && materialIssueDetail.getMaterial().getScrapable() != null)
							if (materialIssueDetail.getMaterial().getScrapable())
								errors.addDataError(ErrorCode.DONT_ALLOW_SCRAP_MATERIALS.getCode(), String.valueOf(i));
							if (StringUtils.isNotBlank(materialIssueDetail.getBalanceQuantity().toString())) {
								if (materialIssueDetail.getBalanceQuantity().compareTo(BigDecimal.ZERO) <= 0)
									errors.addDataError(ErrorCode.QUANTITY_GT_ZERO.getCode(), "balanceQuantity",
											String.valueOf(i), materialIssueDetail.getBalanceQuantity().toString());
								if (materialIssueDetail.getQuantityIssued()
										.compareTo(materialIssueDetail.getBalanceQuantity()) > 0) {
									errors.addDataError(ErrorCode.QUANTITY1_LTE_QUANTITY2.getCode(), "quantityIssued",
											"balanceQuantity", materialIssueDetail.getQuantityIssued().toString(),
											materialIssueDetail.getBalanceQuantity().toString(), String.valueOf(i));
								}
							}
							if (materialIssueDetail.getIndentDetail() != null) {
								if (materialIssueDetail.getQuantityIssued()
										.compareTo(materialIssueDetail.getIndentDetail().getIndentQuantity()) > 0)
									errors.addDataError(ErrorCode.QUANTITY1_LTE_QUANTITY2.getCode(), "quantityIssued",
											"indentQuantity", materialIssueDetail.getQuantityIssued().toString(),
											materialIssueDetail.getIndentDetail().getIndentQuantity().toString(),
											String.valueOf(i));
								if (materialIssueDetail.getIndentDetail().getIndentQuantity()
										.compareTo(BigDecimal.ZERO) <= 0)
									errors.addDataError(ErrorCode.QUANTITY_GT_ZERO.getCode(), "indentQuantity",
											String.valueOf(i),
											materialIssueDetail.getIndentDetail().getIndentQuantity().toString());
							}
							i++;
						}
						validateDuplicateMaterials(materialIssue.getMaterialIssueDetails(), materialIssue.getTenantId(),
								errors);
					}
					break;
				}
			}
		} catch (IllegalArgumentException e) {
		}
		if (errors.getValidationErrors().size() > 0)
			throw errors;

	}
	
	private void validateDuplicateMaterials(List<MaterialIssueDetail> materialIssueDetails, String tenantId,
			InvalidDataException errors) {
		HashSet<Material> setMaterial = new HashSet<Material>();

		for (MaterialIssueDetail issueDetail : materialIssueDetails) {
			Material material = new Material();
			material.setCode(issueDetail.getMaterial().getCode());
			material.setTenantId(tenantId);
			if (!setMaterial.add(material)) {
				errors.addDataError(ErrorCode.REPEATED_VALUE.getCode(), "material", material.getCode(), "");
			}
		}
	}

	public MaterialIssueResponse update(final MaterialIssueRequest materialIssueRequest, String tenantId, String type) {
		fetchRelated(materialIssueRequest, Constants.ACTION_UPDATE);
		validate(materialIssueRequest.getMaterialIssues(), Constants.ACTION_UPDATE, type);
		List<MaterialIssue> materialIssues = materialIssueRequest.getMaterialIssues();
		for (MaterialIssue materialIssue : materialIssues) {
			if (StringUtils.isEmpty(materialIssue.getTenantId()))
				materialIssue.setTenantId(tenantId);
			MaterialIssueSearchContract searchContract = new MaterialIssueSearchContract();
			searchContract.setIssueNoteNumber(materialIssue.getIssueNumber());
			searchContract.setTenantId(materialIssue.getTenantId());
			MaterialIssueResponse issueResponse = search(searchContract,type);
			if(materialIssue.getMaterialIssueStatus() != null )
			if(materialIssue.getMaterialIssueStatus().toString().equals(MaterialIssueStatusEnum.CANCELED.toString())){
				issueResponse.getMaterialIssues().get(0).setMaterialIssueStatus(MaterialIssueStatusEnum.CANCELED);
				updateStatusAsCancelled(tenantId, materialIssue);
				materialIssueRequest.setMaterialIssues(Arrays.asList(issueResponse.getMaterialIssues().get(0)));
				}
			List<MaterialIssueDetail> materialIssueDetails = issueResponse.getMaterialIssues().get(0)
					.getMaterialIssueDetails();
			List<String> materialIssuedFromReceiptsIds = new ArrayList<>();
			ObjectMapper objectMapper = new ObjectMapper();
			Map<String,Uom> uoms = getUoms(tenantId, objectMapper, new RequestInfo());
			for (MaterialIssueDetail materialIssueDetail : materialIssueDetails) {
				Double quantityIssued = getSaveConvertedQuantity(Double.valueOf(materialIssueDetail.getQuantityIssued().toString()),
						Double.valueOf(uoms.get(materialIssueDetail.getUom().getCode()).getConversionFactor().toString()));
				materialIssueDetail.setQuantityIssued(BigDecimal.valueOf(quantityIssued));
				for (MaterialIssuedFromReceipt mifr : materialIssueDetail.getMaterialIssuedFromReceipts()) {
					Double quantity = getSaveConvertedQuantity(Double.valueOf(mifr.getQuantity().toString()),
							Double.valueOf(uoms.get(materialIssueDetail.getUom().getCode()).getConversionFactor().toString()));
					mifr.setQuantity(BigDecimal.valueOf(quantity));
					materialIssuedFromReceiptsIds.add(mifr.getId());
					mifr.setStatus(false);
				}
			}
			materialIssuedFromReceiptsJdbcRepository.updateStatus(materialIssuedFromReceiptsIds,
					materialIssue.getTenantId());
			
			if(materialIssue.getMaterialIssueStatus().toString().equals(MaterialIssueStatusEnum.CANCELED.toString()))
				backUpdateIndentInCaseOfUpdate(materialIssueDetails, materialIssue.getTenantId());
			if( materialIssue.getMaterialIssueStatus().toString().equals(MaterialIssueStatusEnum.CANCELED.toString()))
				break;
			setMaterialIssueValues(materialIssue, null, Constants.ACTION_UPDATE,type);
			materialIssue
					.setAuditDetails(getAuditDetails(materialIssueRequest.getRequestInfo(), Constants.ACTION_UPDATE));
			BigDecimal totalIssueValue = BigDecimal.ZERO;
			List<String> materialIssueDetailsIds = new ArrayList<>();
			for (MaterialIssueDetail materialIssueDetail : materialIssue.getMaterialIssueDetails()) {
				if (StringUtils.isEmpty(materialIssueDetail.getTenantId()))
					materialIssueDetail.setTenantId(tenantId);
				if (StringUtils.isEmpty(materialIssueDetail.getId()))
					materialIssueDetail.setId(materialIssueDetailsJdbcRepository
							.getSequence(MaterialIssueDetail.class.getSimpleName(), 1).get(0));
				materialIssueDetailsIds.add(materialIssueDetail.getId());
				BigDecimal value = getMaterialIssuedFromReceiptData(materialIssue.getFromStore(),
						materialIssueDetail.getMaterial(), materialIssue.getIssueDate(), materialIssue.getTenantId(),
						materialIssueDetail);
				totalIssueValue = totalIssueValue.add(value);
				materialIssueDetail.setValue(value);
				convertToUom(materialIssueDetail);
			}
			materialIssueDetailsJdbcRepository.markDeleted(materialIssueDetailsIds, tenantId, "materialissuedetail",
					"materialissuenumber", materialIssue.getIssueNumber());
			materialIssue.setTotalIssueValue(totalIssueValue);
		}
		kafkaTemplate.send(updateTopic, updateKey, materialIssueRequest);
		MaterialIssueResponse response = new MaterialIssueResponse();
		response.setMaterialIssues(materialIssueRequest.getMaterialIssues());
		response.setResponseInfo(getResponseInfo(materialIssueRequest.getRequestInfo()));
		return response;
	}

	private void updateStatusAsCancelled(String tenantId, MaterialIssue materialIssue) {
         materialIssueJdbcRepository.updateStatus(materialIssue.getIssueNumber(), materialIssue.getTenantId());		
	}

	private void backUpdateIndentInCaseOfUpdate(List<MaterialIssueDetail> materialIssueDetails, String tenantId) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String ,Uom> uoms = getUoms(tenantId, mapper, new RequestInfo());
		for(MaterialIssueDetail mid :materialIssueDetails){
		mid.setUom(uoms.get(mid.getUom().getCode()));
			HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("indentissuedquantity", "indentissuedquantity - " +
            		mid.getQuantityIssued());
            mid.getIndentDetail().setTenantId(tenantId);
            materialIssueJdbcRepository.updateColumn(new IndentDetailEntity().toEntity(mid.getIndentDetail()), "indentdetail", hashMap, null);
		}
	}

	

	public MaterialIssueResponse search(final MaterialIssueSearchContract searchContract, String type) {
		Pagination<MaterialIssue> materialIssues = materialIssueJdbcRepository.search(searchContract,
				type);
		if (materialIssues.getPagedData().size() > 0)
			for (MaterialIssue materialIssue : materialIssues.getPagedData()) {
				Pagination<MaterialIssueDetail> materialIssueDetails = materialIssueDetailsJdbcRepository.search(
						materialIssue.getIssueNumber(), materialIssue.getTenantId(),
						type);
				if (materialIssueDetails.getPagedData().size() > 0) {
					for (MaterialIssueDetail materialIssueDetail : materialIssueDetails.getPagedData()) {
				Pagination<MaterialIssuedFromReceipt> materialIssuedFromReceipts = materialIssuedFromReceiptsJdbcRepository.search(materialIssueDetail.getId(), materialIssueDetail.getTenantId());
						ObjectMapper mapper = new ObjectMapper();
						Map<String, Uom> uoms = getUoms(materialIssue.getTenantId(), mapper, new RequestInfo());
						if (materialIssueDetail.getUom() != null
								&& materialIssueDetail.getUom().getCode() != null)
						{
						Double quantityIssued = getSearchConvertedQuantity(
								Double.valueOf(materialIssueDetail.getQuantityIssued().toString()),
								Double.valueOf(uoms.get(materialIssueDetail.getUom().getCode())
										.getConversionFactor().toString()));
						materialIssueDetail.setQuantityIssued(BigDecimal.valueOf(quantityIssued));
						for(MaterialIssuedFromReceipt mifr : materialIssuedFromReceipts.getPagedData()){
						Double quantity =	getSearchConvertedQuantity(Double.valueOf(mifr.getQuantity().toString()), Double.valueOf(uoms.get(materialIssueDetail.getUom().getCode())
									.getConversionFactor().toString()));
						mifr.setQuantity(BigDecimal.valueOf(quantity));
						}
						}
						materialIssueDetail.setMaterialIssuedFromReceipts(materialIssuedFromReceipts.getPagedData());
					}
					materialIssue.setMaterialIssueDetails(materialIssueDetails.getPagedData());
				}
			}
		MaterialIssueResponse materialIssueResponse = new MaterialIssueResponse();
		materialIssueResponse.setMaterialIssues(materialIssues.getPagedData());
		return materialIssueResponse;
	}

	public MaterialIssueResponse prepareMIFromIndents(MaterialIssueRequest materialIssueRequest, String tenantId) {
		for (MaterialIssue materialIssue : materialIssueRequest.getMaterialIssues()) {
			if (materialIssue.getIndent() != null
					&& StringUtils.isNotBlank(materialIssue.getIndent().getIndentNumber())) {
				IndentSearch indentSearch = new IndentSearch();
				indentSearch.setIndentNumber(materialIssue.getIndent().getIndentNumber());
				indentSearch.setTenantId(tenantId);
				materialIssue.setIndent(indentService.search(indentSearch, new RequestInfo()).getIndents().get(0));
				if (materialIssue.getIndent().getIssueStore() != null
						&& StringUtils.isNotEmpty(materialIssue.getIndent().getIssueStore().getCode())) {
					StoreGetRequest storeGetRequest = StoreGetRequest.builder()
							.code(Arrays.asList(materialIssue.getIndent().getIssueStore().getCode())).tenantId(tenantId)
							.build();
					Store store = storeService.search(storeGetRequest).getStores().get(0);
					if (store != null && store.getDepartment() != null
							&& StringUtils.isNotBlank(store.getDepartment().getCode())) {
						Department department = departmentService.getDepartment(tenantId,
								store.getDepartment().getCode(), new RequestInfo());
						store.setDepartment(department);
					}
					materialIssue.getIndent().setIssueStore(store);
				}
				ObjectMapper mapper = new ObjectMapper();
				if (!materialIssue.getIndent().getIndentDetails().isEmpty()) {
					Map<String, Uom> uomMap = getUoms(tenantId, mapper, new RequestInfo());
					Map<String, Material> materialMap = getMaterials(tenantId, mapper, new RequestInfo());
					List<MaterialIssueDetail> materialIssueDetail = new ArrayList<>();
					for (IndentDetail indentDetail : materialIssue.getIndent().getIndentDetails()) {
						MaterialIssueDetail materialIssueDet = new MaterialIssueDetail();
						if (indentDetail.getMaterial() != null
								&& StringUtils.isNotBlank(indentDetail.getMaterial().getCode())) {
							indentDetail.setMaterial(materialMap.get(indentDetail.getMaterial().getCode()));
						}
						if (indentDetail.getUom() != null && StringUtils.isNotBlank(indentDetail.getUom().getCode())) {
							indentDetail.setUom(uomMap.get(indentDetail.getUom().getCode()));
						}
						materialIssueDet.setIndentDetail(indentDetail);
						materialIssueDetail.add(materialIssueDet);
					}
					materialIssue.setMaterialIssueDetails(materialIssueDetail);
				}
			}
		}
		MaterialIssueResponse materialIssueResponse = new MaterialIssueResponse();
		materialIssueResponse.setMaterialIssues(materialIssueRequest.getMaterialIssues());
		return materialIssueResponse;
	}

	private Map<String, Uom> getUoms(String tenantId, final ObjectMapper mapper, RequestInfo requestInfo) {
		JSONArray responseJSONArray = mdmsRepository.getByCriteria(tenantId, "common-masters", "Uom", null, null,
				requestInfo);
		Map<String, Uom> uomMap = new HashMap<>();

		if (responseJSONArray != null && responseJSONArray.size() > 0) {
			for (int i = 0; i < responseJSONArray.size(); i++) {
				Uom uom = mapper.convertValue(responseJSONArray.get(i), Uom.class);
				uomMap.put(uom.getCode(), uom);
			}

		}
		return uomMap;
	}

	private Map<String, Material> getMaterials(String tenantId, final ObjectMapper mapper, RequestInfo requestInfo) {
		JSONArray responseJSONArray = mdmsRepository.getByCriteria(tenantId, "inventory", "Material", null, null,
				requestInfo);
		Map<String, Material> materialMap = new HashMap<>();

		if (responseJSONArray != null && responseJSONArray.size() > 0) {
			for (int i = 0; i < responseJSONArray.size(); i++) {
				Material material = mapper.convertValue(responseJSONArray.get(i), Material.class);
				materialMap.put(material.getCode(), material);
			}

		}
		return materialMap;
	}

	private String convertEpochtoDate(Long date) {
		Date epoch = new Date(date);
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		String s2 = format.format(epoch);
		return s2;
	}
}
