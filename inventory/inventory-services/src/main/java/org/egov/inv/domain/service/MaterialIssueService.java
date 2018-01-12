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
import org.egov.inv.domain.util.InventoryUtilities;
import org.egov.inv.model.Department;
import org.egov.inv.model.Fifo;
import org.egov.inv.model.FifoRequest;
import org.egov.inv.model.FifoResponse;
import org.egov.inv.model.Indent.IndentStatusEnum;
import org.egov.inv.model.IndentDetail;
import org.egov.inv.model.IndentResponse;
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
import org.egov.inv.persistence.entity.MaterialIssueDetailEntity;
import org.egov.inv.persistence.entity.MaterialIssueEntity;
import org.egov.inv.persistence.repository.IndentDetailJdbcRepository;
import org.egov.inv.persistence.repository.MaterialIssueDetailJdbcRepository;
import org.egov.inv.persistence.repository.MaterialIssueJdbcRepository;
import org.egov.inv.persistence.repository.MaterialIssuedFromReceiptJdbcRepository;
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
public class MaterialIssueService extends DomainService {
	
	private static final Logger LOG = LoggerFactory.getLogger(MaterialIssueService.class);

	@Autowired
	private MaterialIssueJdbcRepository materialIssueJdbcRepository;

	@Autowired
	private IndentService indentService;

	@Autowired
	private MdmsRepository mdmsRepository;

	@Autowired
	private StoreService storeService;

	@Autowired
	private MaterialIssueDetailJdbcRepository materialIssueDetailsJdbcRepository;

	@Autowired
	private MaterialIssuedFromReceiptJdbcRepository materialIssuedFromReceiptsJdbcRepository;

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
			validate(materialIssueRequest.getMaterialIssues(), Constants.ACTION_CREATE, type);
			List<String> sequenceNos = materialIssueJdbcRepository.getSequence(MaterialIssue.class.getSimpleName(),
					materialIssueRequest.getMaterialIssues().size());
			int i = 0;
			for (MaterialIssue materialIssue : materialIssueRequest.getMaterialIssues()) {
				String seqNo = sequenceNos.get(i);
				materialIssue.setId(seqNo);
				setMaterialIssueValues(materialIssue, seqNo, Constants.ACTION_CREATE, type);
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
						backUpdateIndentAdd(materialIssueDetail, materialIssue.getTenantId());
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

	private void backUpdateIndentAdd(MaterialIssueDetail materialIssueDetail, String tenantId) {
		HashMap<String, String> hashMap = new HashMap<>();
		hashMap.put("indentissuedquantity",
				"indentissuedquantity  + " + materialIssueDetail.getQuantityIssued().toString());
		materialIssueDetail.getIndentDetail().setTenantId(tenantId);
		materialIssueJdbcRepository.updateColumn(
				new IndentDetailEntity().toEntity(materialIssueDetail.getIndentDetail()), "indentdetail", hashMap,
				null);
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
			if (quantityIssued.compareTo(BigDecimal.valueOf(fifoEntity.getBalance())) >= 0) {
				materialIssuedFromReceipt
						.setQuantity(BigDecimal.valueOf(fifoEntity.getBalance()));
				unitRate = BigDecimal.valueOf(fifoEntity.getUnitRate())
						.multiply(BigDecimal.valueOf(fifoEntity.getBalance()))
						.add(unitRate);
				quantityIssued = quantityIssued
						.subtract(BigDecimal.valueOf(fifoEntity.getBalance()));
			} else {
				materialIssuedFromReceipt.setQuantity(quantityIssued);
				unitRate = quantityIssued
						.multiply(BigDecimal.valueOf(fifoEntity.getUnitRate())).add(unitRate);
				quantityIssued = BigDecimal.ZERO;
			}
			materialIssuedFromReceipts.add(materialIssuedFromReceipt);
			if (quantityIssued.compareTo(BigDecimal.ZERO) == 0)
				break;
		}
		materialIssueDetail.setMaterialIssuedFromReceipts(materialIssuedFromReceipts);
		return unitRate;
	}

	private void fetchRelated(MaterialIssueRequest materialIssueRequest, String action) {
		if (action.equals(Constants.ACTION_CREATE)) {
			for (MaterialIssue materialIssue : materialIssueRequest.getMaterialIssues()) {
				validateAndBuildMaterialIssueHeader(materialIssue);
				validateAndBuildMaterialIssueDetails(materialIssue);

			}
		} else {
			for (MaterialIssue materialIssue : materialIssueRequest.getMaterialIssues()) {

				validateAndBuildMaterialIssueHeader(materialIssue);
				validateAndBuildMaterialIssueDetails(materialIssue);

			}
		}

	}

	private void validateAndBuildMaterialIssueHeader(MaterialIssue materialIssue) {
		if (materialIssue.getFromStore() != null && StringUtils.isNotBlank(materialIssue.getFromStore().getCode())) {
			List<Store> stores = searchStoreByParameters(materialIssue.getFromStore().getCode(),
					materialIssue.getTenantId());
			if (stores.isEmpty())
				throw new CustomException("invalid.ref.value",
						"the field issuestore should have a valid value which exists in the system.");
			else
				materialIssue.setFromStore(stores.get(0));

		}
		if (materialIssue.getToStore() != null && StringUtils.isNotBlank(materialIssue.getToStore().getCode())) {
			List<Store> stores = searchStoreByParameters(materialIssue.getToStore().getCode(),
					materialIssue.getTenantId());
			if (stores.isEmpty())
				throw new CustomException("invalid.ref.value",
						"the field indentstore should have a valid value which exists in the system.");
			else
				materialIssue.setToStore(stores.get(0));
		}
	}

	private void validateAndBuildMaterialIssueDetails(MaterialIssue materialIssue) {
		if (!materialIssue.getMaterialIssueDetails().isEmpty()) {
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Material> materialMap = getMaterials(materialIssue.getTenantId(), mapper, new RequestInfo());
			Map<String, Uom> uomMap = getUoms(materialIssue.getTenantId(), mapper, new RequestInfo());

			for (MaterialIssueDetail materialIssueDetail : materialIssue.getMaterialIssueDetails()) {
				if (materialIssueDetail.getMaterial() != null
						&& StringUtils.isNotBlank(materialIssueDetail.getMaterial().getCode())) {
					if (materialMap.get(materialIssueDetail.getMaterial().getCode()) == null)
						throw new CustomException("invalid.ref.value",
								"the field material should have a valid value which exists in the system.");

					else
						materialIssueDetail.setMaterial(materialMap.get(materialIssueDetail.getMaterial().getCode()));
				}
				if (materialIssueDetail.getUom() != null
						&& StringUtils.isNotBlank(materialIssueDetail.getUom().getCode())) {
					if (uomMap.get(materialIssueDetail.getUom().getCode()) == null)
						throw new CustomException("invalid.ref.value",
								"the field uom should have a valid value which exists in the system.");

					else
						materialIssueDetail.setUom(uomMap.get(materialIssueDetail.getUom().getCode()));
				}
				if (materialIssueDetail.getIndentDetail() != null
						&& materialIssueDetail.getIndentDetail().getId() != null) {
					IndentDetailEntity entity = new IndentDetailEntity();
					entity.setId(materialIssueDetail.getIndentDetail().getId());
					entity.setTenantId(materialIssue.getTenantId());
					materialIssueDetail.setIndentDetail(indentDetailJdbcRepository.findById(entity) != null
							? indentDetailJdbcRepository.findById(entity).toDomain() : null);
				}
			}
		}
	}

	private List<Store> searchStoreByParameters(String storeCode,String tenantId) {
		StoreGetRequest storeGetRequest = new StoreGetRequest();
		storeGetRequest.setCode(Arrays.asList(storeCode));
		storeGetRequest.setTenantId(tenantId);
		return  storeService.search(storeGetRequest).getStores();
	}

	private void setMaterialIssueValues(MaterialIssue materialIssue, String seqNo, String action, String type) {
		if (type.equals(IssueTypeEnum.INDENTISSUE.toString()))
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
						errors.addDataError(ErrorCode.DATE_LE_CURRENTDATE.getCode(), "issueDate", convertEpochtoDate(materialIssue.getIssueDate()));
					}
					if(materialIssue.getIndent() != null &&  StringUtils.isNotBlank(materialIssue.getIndent().getIndentNumber() ))
					{
						BigDecimal totalIndentQuantity = BigDecimal.ZERO;

						IndentEntity indentEntity = new IndentEntity();
						indentEntity.setIndentNumber(materialIssue.getIndent().getIndentNumber());
						indentEntity.setTenantId(materialIssue.getTenantId());
						Object indenttEntity = materialIssueJdbcRepository.findById(indentEntity,
								"IndentEntity");
					
					   if(indenttEntity!=null) {
							IndentEntity indentEntityfromDb = (IndentEntity) indenttEntity;
							if (indentEntityfromDb != null) {
								if(!indentEntityfromDb.getIndentStatus().equals(IndentStatusEnum.APPROVED.toString()))
									errors.addDataError(ErrorCode.INDENT_NOT_APPROVED.getCode(), materialIssue.getIndent().getIndentNumber());
							}
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
							}
						//todo: if all items already issued?
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

							// user entered value copied into quantity issued
							// column. Use the same for validation, create and
							// update. 
	
							materialIssueDetail.setQuantityIssued(InventoryUtilities.getQuantityInBaseUom(
									materialIssueDetail.getUserQuantityIssued(),materialIssueDetail.getUom().getConversionFactor() ));
							if (materialIssueDetail.getQuantityIssued().compareTo(BigDecimal.ZERO) <= 0)
								errors.addDataError(ErrorCode.QUANTITY_GT_ZERO.getCode(), "quantityIssued",
										String.valueOf(i), materialIssueDetail.getQuantityIssued().toString());
							if(materialIssueDetail.getMaterial() != null && materialIssueDetail.getMaterial().getScrapable() != null)
							if (materialIssueDetail.getMaterial().getScrapable())
								errors.addDataError(ErrorCode.DONT_ALLOW_SCRAP_MATERIALS.getCode(), String.valueOf(i));

							BigDecimal balanceQuantity;
                            LOG.info("calculating balance quantity");
							balanceQuantity = getBalanceQuantityByStoreByMaterialAndIssueDate(materialIssue.getFromStore(),materialIssueDetail.getMaterial(),
									materialIssue.getIssueDate(), materialIssue.getTenantId());
							if (StringUtils.isNotBlank(balanceQuantity.toString())) {
								if (balanceQuantity.compareTo(BigDecimal.ZERO) <= 0)
									errors.addDataError(ErrorCode.QUANTITY_GT_ZERO.getCode(), "balanceQuantity",
											String.valueOf(i), balanceQuantity.toString());
								if (materialIssueDetail.getQuantityIssued()
										.compareTo(balanceQuantity) > 0) {
									errors.addDataError(ErrorCode.QUANTITY1_LTE_QUANTITY2.getCode(), "quantityIssued",
											"balanceQuantity",InventoryUtilities.getQuantityInSelectedUom(materialIssueDetail.getQuantityIssued(),materialIssueDetail.getUom().getConversionFactor())
											.toString(),
										  InventoryUtilities.getQuantityInSelectedUom(balanceQuantity,materialIssueDetail.getUom().getConversionFactor())
											.toString(), String.valueOf(i));
								}
							}
							if(materialIssueDetail.getIndentDetail() != null){
								if(materialIssueDetail.getIndentDetail().getIndentQuantity() != null && materialIssueDetail.getIndentDetail().getIndentIssuedQuantity() != null)
						 materialIssueDetail.setPendingIndentQuantity(materialIssueDetail.getIndentDetail().getIndentQuantity().subtract(materialIssueDetail.getIndentDetail().getIndentIssuedQuantity()));
							}
								if (materialIssueDetail.getQuantityIssued()
										.compareTo(materialIssueDetail.getPendingIndentQuantity()) > 0)
									errors.addDataError(ErrorCode.QUANTITY1_LTE_QUANTITY2.getCode(), "quantityIssued",
											"indentQuantity", InventoryUtilities.getQuantityInSelectedUom(materialIssueDetail.getQuantityIssued(),materialIssueDetail.getUom().getConversionFactor())
											.toString(),
											InventoryUtilities.getQuantityInSelectedUom(materialIssueDetail.getPendingIndentQuantity(),materialIssueDetail.getUom().getConversionFactor()).toString(),
											String.valueOf(i));
								if (materialIssueDetail.getPendingIndentQuantity()
										.compareTo(BigDecimal.ZERO) <= 0)
									errors.addDataError(ErrorCode.QUANTITY_GT_ZERO.getCode(), "indentQuantity",
											String.valueOf(i),
											InventoryUtilities.getQuantityInSelectedUom(materialIssueDetail.getPendingIndentQuantity(),materialIssueDetail.getUom().getConversionFactor()).toString());
								i++;	
						}
							
						}
						validateDuplicateMaterials(materialIssue.getMaterialIssueDetails(), materialIssue.getTenantId(),
								errors);
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

							// user entered value copied into quantity issued
							// column. Use the same for validation, create and
							// update.

							materialIssueDetail.setQuantityIssued(InventoryUtilities.getQuantityInBaseUom(
									materialIssueDetail.getUserQuantityIssued(),materialIssueDetail.getUom().getConversionFactor() ));
							
							if (materialIssueDetail.getQuantityIssued().compareTo(BigDecimal.ZERO) <= 0)
								errors.addDataError(ErrorCode.QUANTITY_GT_ZERO.getCode(), "quantityIssued",
										String.valueOf(i), materialIssueDetail.getQuantityIssued().toString());
							if(materialIssueDetail.getMaterial() != null && materialIssueDetail.getMaterial().getScrapable() != null)
							if (materialIssueDetail.getMaterial().getScrapable())
								errors.addDataError(ErrorCode.DONT_ALLOW_SCRAP_MATERIALS.getCode(), String.valueOf(i));
							FifoRequest fifoRequest = new FifoRequest();
							Fifo fifo = new Fifo();
							fifo.setStore(materialIssue.getFromStore());
							fifo.setMaterial(materialIssueDetail.getMaterial());
							fifo.setIssueDate(materialIssue.getIssueDate());
							fifo.setTenantId(materialIssue.getTenantId());
							fifoRequest.setFifo(fifo);
						
						MaterialIssueSearchContract searchContract = new MaterialIssueSearchContract();
						searchContract.setIssueNoteNumber(materialIssue.getIssueNumber());
						searchContract.setTenantId(materialIssue.getTenantId());
						Pagination<MaterialIssueDetail> listOfPagedMaterialIssueDetails = materialIssueDetailsJdbcRepository.search(materialIssue.getIssueNumber(), materialIssue.getTenantId(), null);
						List<MaterialIssueDetail> listOfMaterialIssueDetails = new ArrayList<>();
						BigDecimal quantityIssued = BigDecimal.ZERO;
						if(listOfPagedMaterialIssueDetails != null)
						 listOfMaterialIssueDetails = listOfPagedMaterialIssueDetails.getPagedData();
						for(MaterialIssueDetail materialIssDetail :listOfMaterialIssueDetails)
						{
							if(materialIssDetail.getId().equals(materialIssueDetail.getId())){
								quantityIssued = materialIssDetail.getQuantityIssued();
							break;
							}
								
						}
						BigDecimal balQuantity = BigDecimal.ZERO;
						FifoResponse fifoResponse = materialIssueReceiptFifoLogic.getTotalStockAsPerMaterial(fifoRequest);
						if(fifoResponse != null)
							balQuantity = fifoResponse.getStock();
						//TODO: CHECK THIS LOGIC. 
						BigDecimal balanceQuantity = balQuantity.add(quantityIssued);
							if (StringUtils.isNotBlank(balanceQuantity.toString())) {
								if (balanceQuantity.compareTo(BigDecimal.ZERO) <= 0)
									errors.addDataError(ErrorCode.QUANTITY_GT_ZERO.getCode(), "balanceQuantity",
											String.valueOf(i), balanceQuantity.toString());
								if (materialIssueDetail.getQuantityIssued()
										.compareTo(balanceQuantity) > 0) {
									errors.addDataError(ErrorCode.QUANTITY1_LTE_QUANTITY2.getCode(), "quantityIssued",
											"balanceQuantity", materialIssueDetail.getQuantityIssued().toString(),
											balanceQuantity.toString(), String.valueOf(i));
								}
							}
							if(materialIssueDetail.getIndentDetail() != null){
								if(materialIssueDetail.getIndentDetail().getIndentQuantity() != null && materialIssueDetail.getIndentDetail().getIndentIssuedQuantity() != null)
						 materialIssueDetail.setPendingIndentQuantity(materialIssueDetail.getIndentDetail().getIndentQuantity().subtract(materialIssueDetail.getIndentDetail().getIndentIssuedQuantity()));
							}
						BigDecimal actualIndentQuantity =	quantityIssued.add(materialIssueDetail.getPendingIndentQuantity());
								if (materialIssueDetail.getQuantityIssued()
										.compareTo(actualIndentQuantity) > 0)
									errors.addDataError(ErrorCode.QUANTITY1_LTE_QUANTITY2.getCode(), "quantityIssued",
											"indentQuantity", materialIssueDetail.getQuantityIssued().toString(),
											actualIndentQuantity.toString(),
											String.valueOf(i));
								if (actualIndentQuantity
										.compareTo(BigDecimal.ZERO) <= 0)
									errors.addDataError(ErrorCode.QUANTITY_GT_ZERO.getCode(), "indentQuantity",
											String.valueOf(i),
											actualIndentQuantity.toString());
							
							i++;
						}
						validateDuplicateMaterials(materialIssue.getMaterialIssueDetails(), materialIssue.getTenantId(),
								errors);
					}
					break;
				}
			}}catch(

	IllegalArgumentException e)
	{
	}if(errors.getValidationErrors().size()>0)throw errors;

	}

	private BigDecimal getBalanceQuantityByStoreByMaterialAndIssueDate(Store store,Material material,
			 Long issueDate, String tenantId) {
		BigDecimal balanceQuantity = BigDecimal.ZERO;
        LOG.info("store :" + store + "material :" + material + "issueDate :" + issueDate + "tenantId :" + tenantId);
		FifoRequest fifoRequest = new FifoRequest();
		Fifo fifo = new Fifo();
		fifo.setStore(store);
		fifo.setMaterial(material);
		fifo.setIssueDate(issueDate);
		fifo.setTenantId(tenantId);

		fifoRequest.setFifo(fifo);
		FifoResponse fifoResponse = materialIssueReceiptFifoLogic.getTotalStockAsPerMaterial(fifoRequest);
		if (fifoResponse != null)
			balanceQuantity = fifoResponse.getStock();
		return balanceQuantity;
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
			
			//Search old issue object.
			MaterialIssueSearchContract searchContract = new MaterialIssueSearchContract();
			searchContract.setIssueNoteNumber(materialIssue.getIssueNumber());
			searchContract.setTenantId(materialIssue.getTenantId());
			MaterialIssueResponse issueResponse = search(searchContract, type);
			
			//Cancel record status as cancelled 
			if (materialIssue.getMaterialIssueStatus() != null){
				if (materialIssue.getMaterialIssueStatus().toString()
						.equals(MaterialIssueStatusEnum.CANCELED.toString())) {
					issueResponse.getMaterialIssues().get(0).setMaterialIssueStatus(MaterialIssueStatusEnum.CANCELED);
					updateStatusAsCancelled(tenantId, materialIssue);
					materialIssueRequest.setMaterialIssues(Arrays.asList(issueResponse.getMaterialIssues().get(0)));
				}
			}
			
			//legacy mifr updation
			List<MaterialIssueDetail> materialIssueDetails = issueResponse.getMaterialIssues().get(0)
					.getMaterialIssueDetails();
			List<String> materialIssuedFromReceiptsIds = new ArrayList<>();
			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Uom> uoms = getUoms(tenantId, objectMapper, new RequestInfo());
			for (MaterialIssueDetail materialIssueDetail : materialIssueDetails) {
				for (MaterialIssuedFromReceipt mifr : materialIssueDetail.getMaterialIssuedFromReceipts()) {
					BigDecimal quantity = InventoryUtilities.getQuantityInBaseUom(mifr.getQuantity(),
							uoms.get(materialIssueDetail.getUom().getCode()).getConversionFactor());
					mifr.setQuantity(quantity);
					materialIssuedFromReceiptsIds.add(mifr.getId());
					mifr.setStatus(false);
				}
			} 
			//Update Material issue and receipt status as false.
			materialIssuedFromReceiptsJdbcRepository.updateStatus(materialIssuedFromReceiptsIds,
					materialIssue.getTenantId());
			if (materialIssue.getMaterialIssueStatus().toString().equals(MaterialIssueStatusEnum.CANCELED.toString())){
				backUpdateIndentInCaseOfCancellation(materialIssueDetails, materialIssue.getTenantId());
				//break;
			}else {
			setMaterialIssueValues(materialIssue, null, Constants.ACTION_UPDATE, type);
			materialIssue
					.setAuditDetails(getAuditDetails(materialIssueRequest.getRequestInfo(), Constants.ACTION_UPDATE));
			BigDecimal totalIssueValue = BigDecimal.ZERO;
			List<String> materialIssueDetailsIds = new ArrayList<>();
			for (MaterialIssueDetail materialIssueDetail : materialIssue.getMaterialIssueDetails()) {
				backUpdatingIndentForInsertionAndUpdation(materialIssueDetail, materialIssue.getTenantId(), type);
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
			}
			backUpdatingIndentForDeletionCase(materialIssueDetailsIds,materialIssue.getIssueNumber(),materialIssue.getTenantId(), type);
			materialIssueDetailsJdbcRepository.markDeleted(materialIssueDetailsIds, tenantId, "materialissuedetail",
					"materialissuenumber", materialIssue.getIssueNumber());
			materialIssue.setTotalIssueValue(totalIssueValue);
			}
		}
		kafkaTemplate.send(updateTopic, updateKey, materialIssueRequest);
		MaterialIssueResponse response = new MaterialIssueResponse();
		response.setMaterialIssues(materialIssueRequest.getMaterialIssues());
		response.setResponseInfo(getResponseInfo(materialIssueRequest.getRequestInfo()));
		return response;
	}

	private void backUpdatingIndentForDeletionCase(List<String> materialIssueDetailsIds, String issueNumber, String tenantId, String type) {
		Pagination<MaterialIssueDetail> materialIssueDetails = materialIssueDetailsJdbcRepository
				.search(issueNumber, tenantId, type);
		List<MaterialIssueDetail> materialIssueDls = new ArrayList<>();
		if(materialIssueDetails != null)
		 materialIssueDls =	materialIssueDetails.getPagedData();
		int flag =0;
		List<MaterialIssueDetail> midetails = new ArrayList<>();
		for(MaterialIssueDetail mids : materialIssueDls)
		{
			for(String ids : materialIssueDetailsIds){
				if(mids.getId().equals(ids))
					flag = 1;
				if(flag == 1)
					break;
			}
			if(flag == 0)
				midetails.add(mids);
		}
		for(MaterialIssueDetail mid : midetails){
		backUpdateIndentMinus(mid, tenantId);
		}
		
	}

	private void backUpdatingIndentForInsertionAndUpdation(MaterialIssueDetail materialIssueDetail, String tenantId, String type) {
      if(StringUtils.isEmpty(materialIssueDetail.getId()))
    		  backUpdateIndentAdd(materialIssueDetail, tenantId);
      else if(StringUtils.isNotEmpty(materialIssueDetail.getId())){
    	  MaterialIssueDetailEntity materialIssueDetailEntity = new MaterialIssueDetailEntity();
    	  MaterialIssueDetail materialIssueDet = new MaterialIssueDetail();
    	  materialIssueDetailEntity.setId(materialIssueDetail.getId());
    	  materialIssueDetailEntity.setTenantId(tenantId);
    	  MaterialIssueDetailEntity materialIssueDetEntity = materialIssueDetailsJdbcRepository.findById(materialIssueDetailEntity, "MaterialIssueDetailEntity");
    	  if(materialIssueDetEntity != null)
    		  materialIssueDet = materialIssueDetEntity.toDomain(type);
    	  backUpdateIndentMinus(materialIssueDet, tenantId);
    	  backUpdateIndentAdd(materialIssueDetail, tenantId);
      }
	}

	private void backUpdateIndentMinus(MaterialIssueDetail materialIssueDet, String tenantId) {
		HashMap<String, String> hashMap = new HashMap<>();
		hashMap.put("indentissuedquantity",
				"indentissuedquantity  - " + materialIssueDet.getQuantityIssued().toString());
		materialIssueDet.getIndentDetail().setTenantId(tenantId);
		materialIssueJdbcRepository.updateColumn(
				new IndentDetailEntity().toEntity(materialIssueDet.getIndentDetail()), "indentdetail", hashMap,
				null);
		
	}

	private void updateStatusAsCancelled(String tenantId, MaterialIssue materialIssue) {
		materialIssueJdbcRepository.updateStatus(materialIssue.getIssueNumber(), "CANCELED",
				materialIssue.getTenantId());
	}

	private void backUpdateIndentInCaseOfCancellation(List<MaterialIssueDetail> materialIssueDetails, String tenantId) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Uom> uoms = getUoms(tenantId, mapper, new RequestInfo());
		for (MaterialIssueDetail mid : materialIssueDetails) {
			mid.setUom(uoms.get(mid.getUom().getCode()));
			HashMap<String, String> hashMap = new HashMap<>();
			hashMap.put("indentissuedquantity", "indentissuedquantity - " + mid.getQuantityIssued());
			mid.getIndentDetail().setTenantId(tenantId);
			materialIssueJdbcRepository.updateColumn(new IndentDetailEntity().toEntity(mid.getIndentDetail()),
					"indentdetail", hashMap, null);
		}
	}

public MaterialIssueResponse search(final MaterialIssueSearchContract searchContract, String type) {
		Pagination<MaterialIssue> materialIssues = materialIssueJdbcRepository.search(searchContract, type);
		if (materialIssues.getPagedData().size() > 0)
			for (MaterialIssue materialIssue : materialIssues.getPagedData()) {
				Pagination<MaterialIssueDetail> materialIssueDetails = materialIssueDetailsJdbcRepository
						.search(materialIssue.getIssueNumber(), materialIssue.getTenantId(), type);
				if (materialIssueDetails.getPagedData().size() > 0) {
					for (MaterialIssueDetail materialIssueDetail : materialIssueDetails.getPagedData()) {
						Pagination<MaterialIssuedFromReceipt> materialIssuedFromReceipts = materialIssuedFromReceiptsJdbcRepository
								.search(materialIssueDetail.getId(), materialIssueDetail.getTenantId());
						ObjectMapper mapper = new ObjectMapper();
						Map<String, Uom> uoms = getUoms(materialIssue.getTenantId(), mapper, new RequestInfo());
						if (materialIssueDetail.getUom() != null
								&& materialIssueDetail.getUom().getCode() != null)
						{
						for(MaterialIssuedFromReceipt mifr : materialIssuedFromReceipts.getPagedData()){
						BigDecimal quantity =	getSearchConvertedQuantity(mifr.getQuantity(), uoms.get(materialIssueDetail.getUom().getCode())
								.getConversionFactor());
						mifr.setQuantity(quantity);
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
				
				// Show error if indent not found
				IndentSearch indentSearch = new IndentSearch();
				indentSearch.setIndentNumber(materialIssue.getIndent().getIndentNumber());
				indentSearch.setTenantId(tenantId);
				IndentResponse indentResponse=indentService.search(indentSearch, new RequestInfo());
				if(indentResponse!=null && indentResponse.getIndents()!=null && indentResponse.getIndents().isEmpty())
					throw new CustomException(ErrorCode.INVALID_INDENTNUMBER_FOR_ISSUE.getCode(),
							ErrorCode.INVALID_INDENTNUMBER_FOR_ISSUE.getMessage());
				
				materialIssue.setIndent(indentResponse.getIndents().get(0));
				if (materialIssue.getIndent().getIssueStore() != null
						&& StringUtils.isNotEmpty(materialIssue.getIndent().getIssueStore().getCode())) {
					List<Store> stores = searchStoreByParameters(materialIssue.getIndent().getIssueStore().getCode(),materialIssue.getTenantId());
					
				if(!stores.isEmpty())
					{
						Store store =stores.get(0);
						if (stores!=null && stores.get(0)!= null && store.getDepartment() != null
								&& StringUtils.isNotBlank(store.getDepartment().getCode())) {
							Department department = departmentService.getDepartment(tenantId,
									store.getDepartment().getCode(), new RequestInfo());
							store.setDepartment(department);
						}
						materialIssue.getIndent().setIssueStore(store);
						materialIssue.setFromStore(store); //Adding indent issue store as material issue -> from store (Store issues material)
					}
				}
				if (materialIssue.getIndent().getIndentStore() != null
						&& StringUtils.isNotBlank(materialIssue.getIndent().getIndentStore().getCode()))
				{
					List<Store> stores = searchStoreByParameters(materialIssue.getIndent().getIndentStore().getCode(),materialIssue.getTenantId());
					if (!stores.isEmpty()){
						Store store =stores.get(0);
						if (stores!=null && stores.get(0)!= null && store.getDepartment() != null
								&& StringUtils.isNotBlank(store.getDepartment().getCode())) {
							Department department = departmentService.getDepartment(tenantId,
									store.getDepartment().getCode(), new RequestInfo());
							store.setDepartment(department);
						}
						materialIssue.getIndent().setIndentStore(store);
						materialIssue.setToStore(store);
					}
				}
						
						
				ObjectMapper mapper = new ObjectMapper();
				if (!materialIssue.getIndent().getIndentDetails().isEmpty()) {
					Map<String, Uom> uomMap = getUoms(tenantId, mapper, new RequestInfo());
					Map<String, Material> materialMap = getMaterials(tenantId, mapper, new RequestInfo());
					List<MaterialIssueDetail> materialIssueDetail = new ArrayList<>();
					
				//Fetch indent details where quantity issue is pending.
					for (IndentDetail indentDetail : materialIssue.getIndent().getIndentDetails()) {
						
						// Show total indent required quantity.
						BigDecimal indentBalanceQuantity = InventoryUtilities.getQuantityInSelectedUom(
								indentDetail.getIndentQuantity()
										.subtract(indentDetail.getIndentIssuedQuantity() != null
												? indentDetail.getIndentIssuedQuantity() : BigDecimal.ZERO),
								uomMap.get(indentDetail.getUom().getCode()).getConversionFactor());
				
						if (indentBalanceQuantity.compareTo(BigDecimal.ZERO) > 0) {

							MaterialIssueDetail materialIssueDet = new MaterialIssueDetail();

							materialIssueDet.setPendingIndentQuantity(InventoryUtilities.getQuantityInSelectedUom(
									indentDetail.getIndentQuantity().subtract(indentDetail.getIndentIssuedQuantity()),
									uomMap.get(indentDetail.getUom().getCode()).getConversionFactor()));

							if (indentDetail.getMaterial() != null
									&& StringUtils.isNotBlank(indentDetail.getMaterial().getCode())) {
								indentDetail.setMaterial(materialMap.get(indentDetail.getMaterial().getCode()));
								materialIssueDet.setMaterial(materialMap.get(indentDetail.getMaterial().getCode()));
							}
							if (indentDetail.getUom() != null
									&& StringUtils.isNotBlank(indentDetail.getUom().getCode())) {
								indentDetail.setUom(uomMap.get(indentDetail.getUom().getCode()));
								materialIssueDet.setUom(uomMap.get(indentDetail.getUom().getCode()));

								// Show available quantity in UI. Converted to
								// Unit of measurement selected.
								materialIssueDet.setBalanceQuantity(InventoryUtilities.getQuantityInSelectedUom(
										getBalanceQuantityByStoreByMaterialAndIssueDate(
												materialIssue.getIndent().getIssueStore(),
												materialIssueDet.getMaterial(),
												(materialIssue.getIssueDate() != null ? materialIssue.getIssueDate()
														: currentEpochWithoutTime()),
												materialIssue.getTenantId()),
										materialIssueDet.getUom().getConversionFactor()));

							}

							materialIssueDet.setIndentDetail(indentDetail);
							materialIssueDetail.add(materialIssueDet);
						}
					}
					
					if (materialIssueDetail.isEmpty())
						throw new CustomException(ErrorCode.NO_ITEMS_TO_ISSUE.getCode(),
								ErrorCode.NO_ITEMS_TO_ISSUE.getMessage());
					
					materialIssue.setMaterialIssueDetails(materialIssueDetail);	
				}
			}else
				throw new CustomException(ErrorCode.ATLEAST_ONEINDENT_REQUIRE_ISSUE.getCode(),
						ErrorCode.ATLEAST_ONEINDENT_REQUIRE_ISSUE.getMessage());
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



