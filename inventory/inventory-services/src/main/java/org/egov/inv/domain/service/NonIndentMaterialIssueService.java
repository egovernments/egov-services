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
import org.egov.inv.model.Material;
import org.egov.inv.model.MaterialIssue;
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
import org.egov.inv.model.MaterialIssue.IssuePurposeEnum;
import org.egov.inv.model.MaterialIssue.IssueTypeEnum;
import org.egov.inv.model.MaterialIssue.MaterialIssueStatusEnum;
import org.egov.inv.persistence.entity.FifoEntity;
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
public class NonIndentMaterialIssueService extends DomainService {

	private static final Logger LOG = LoggerFactory.getLogger(NonIndentMaterialIssueService.class);

	@Autowired
	private MaterialIssueJdbcRepository materialIssueJdbcRepository;

	@Autowired
	private MaterialIssueDetailsJdbcRepository materialIssueDetailsJdbcRepository;

	@Autowired
	private MaterialIssuedFromReceiptsJdbcRepository materialIssuedFromReceiptsJdbcRepository;

	@Autowired
	private MdmsRepository mdmsRepository;
	
	@Autowired
	private MaterialIssueReceiptFifoLogic materialIssueReceiptFifoLogic;

	@Autowired
	private StoreService storeService;

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

	public MaterialIssueResponse create(MaterialIssueRequest nonIndentIssueRequest) {
		try {
			fetchRelated(nonIndentIssueRequest);
			validate(nonIndentIssueRequest.getMaterialIssues(), Constants.ACTION_CREATE);
			List<String> sequenceNos = materialIssueJdbcRepository.getSequence(MaterialIssue.class.getSimpleName(),
					nonIndentIssueRequest.getMaterialIssues().size());
			int i = 0;
			for (MaterialIssue materialIssue : nonIndentIssueRequest.getMaterialIssues()) {
				String seqNo = sequenceNos.get(i);
				materialIssue.setId(seqNo);
				setMaterialIssueValues(materialIssue, seqNo, Constants.ACTION_CREATE);
				materialIssue.setAuditDetails(mapAuditDetails(nonIndentIssueRequest.getRequestInfo()));
				i++;
				int j = 0;
				if (!materialIssue.getMaterialIssueDetails().isEmpty()) {
					List<String> detailSequenceNos = materialIssueDetailsJdbcRepository.getSequence(
							MaterialIssueDetail.class.getSimpleName(), materialIssue.getMaterialIssueDetails().size());
					ObjectMapper mapper = new ObjectMapper();
					Map<String, Uom> uomMap = getUoms(materialIssue.getTenantId(), mapper, new RequestInfo());
					for (MaterialIssueDetail materialIssueDetail : materialIssue.getMaterialIssueDetails()) {
						materialIssueDetail.setId(detailSequenceNos.get(j));
						materialIssueDetail.setTenantId(materialIssue.getTenantId());
						BigDecimal unitRate =getMaterialIssuedFromReceiptData(materialIssue.getFromStore(),
								materialIssueDetail.getMaterial(), materialIssue.getIssueDate(),
								materialIssue.getTenantId(), materialIssueDetail,materialIssue);
						materialIssueDetail.setValue(unitRate);
						convertToUom(materialIssueDetail, uomMap);
						j++;
						}
					}
				}
	
			kafkaTemplate.send(createTopic, createKey, nonIndentIssueRequest);
			MaterialIssueResponse response = new MaterialIssueResponse();
			response.setMaterialIssues(nonIndentIssueRequest.getMaterialIssues());
			response.setResponseInfo(getResponseInfo(nonIndentIssueRequest.getRequestInfo()));
			return response;
		} catch (CustomBindException e) {
			throw e;
		}
	}

	private BigDecimal getMaterialIssuedFromReceiptData(Store store, Material material, Long issueDate, String tenantId,
			MaterialIssueDetail materialIssueDetail, MaterialIssue materialIssue) {
		List<MaterialIssuedFromReceipt> materialIssuedFromReceipts = new ArrayList<>();
		List<FifoEntity> listOfFifoEntity = new ArrayList<> ();
		if(materialIssue.getIssuePurpose().equals(IssuePurposeEnum.WRITEOFFORSCRAP)){
			listOfFifoEntity = materialIssueReceiptFifoLogic.implementFifoLogic(store, material, issueDate,
					tenantId);
		}
		else{
			listOfFifoEntity = materialIssueReceiptFifoLogic.implementFifoLogicForReturnMaterial(store, material, issueDate, tenantId, materialIssueDetail.getMrnNumber());
		}
			int k = 0;
			List<String> materialIssuedFromReceiptsSeqNos = materialIssuedFromReceiptsJdbcRepository
					.getSequence(MaterialIssuedFromReceipt.class.getSimpleName(), listOfFifoEntity.size());
			BigDecimal unitRate = BigDecimal.ZERO;
			BigDecimal quantityIssued = materialIssueDetail.getQuantityIssued();
			for (FifoEntity fifoEntity : listOfFifoEntity) {
				MaterialIssuedFromReceipt materialIssuedFromReceipt = new MaterialIssuedFromReceipt();
				MaterialReceiptDetail materialReceiptDetail = new MaterialReceiptDetail();
				materialReceiptDetail.setId(fifoEntity.getReceiptDetailId());
				materialIssuedFromReceipt.setMaterialReceiptDetail(materialReceiptDetail);
				materialIssuedFromReceipt.setMaterialReceiptDetailAddnlinfoId(fifoEntity.getReceiptDetailAddnInfoId());
				materialIssuedFromReceipt.setId(materialIssuedFromReceiptsSeqNos.get(k));
				materialIssuedFromReceipt.setTenantId(materialIssueDetail.getTenantId());
				materialIssuedFromReceipt.setStatus(true);
				materialIssuedFromReceipt.setMaterialReceiptId(fifoEntity.getReceiptId());
				if (quantityIssued.compareTo(BigDecimal.valueOf(getSearchConvertedQuantity(fifoEntity.getBalance(),Double.valueOf(materialIssueDetail.getUom().getConversionFactor() .toString())))) >= 0) {
					materialIssuedFromReceipt.setQuantity(BigDecimal.valueOf(getSearchConvertedQuantity(fifoEntity.getBalance(),Double.valueOf(materialIssueDetail.getUom().getConversionFactor() .toString()))));
					unitRate = BigDecimal.valueOf(fifoEntity.getUnitRate())
							.multiply(BigDecimal.valueOf(getSearchConvertedQuantity(fifoEntity.getBalance(),Double.valueOf(materialIssueDetail.getUom().getConversionFactor() .toString())))).add(unitRate);
					quantityIssued = quantityIssued.subtract(BigDecimal.valueOf(getSearchConvertedQuantity(fifoEntity.getBalance(),Double.valueOf(materialIssueDetail.getUom().getConversionFactor() .toString()))));
				} else {
					materialIssuedFromReceipt.setQuantity(quantityIssued);
					unitRate = quantityIssued.multiply(BigDecimal.valueOf(fifoEntity.getUnitRate())).add(unitRate);
					quantityIssued = BigDecimal.ZERO;
				}
				k++;
				materialIssuedFromReceipts.add(materialIssuedFromReceipt);
				if (quantityIssued.equals(BigDecimal.ZERO))
					break;
			}
			materialIssueDetail.setMaterialIssuedFromReceipts(materialIssuedFromReceipts);
			return unitRate;

	
			
	}

	private void fetchRelated(MaterialIssueRequest nonIndentIssueRequest) {
		for (MaterialIssue materialIssue : nonIndentIssueRequest.getMaterialIssues()) {
		if (materialIssue.getFromStore() != null
				&& StringUtils.isNotBlank(materialIssue.getFromStore().getCode())) {
			StoreGetRequest storeGetRequest = new StoreGetRequest();
			storeGetRequest.setCode(Arrays.asList(materialIssue.getFromStore().getCode()));
			storeGetRequest.setTenantId(materialIssue.getTenantId());
			List<Store> stores =storeService.search(storeGetRequest).getStores();
			if (stores.isEmpty())
				throw new CustomException("invalid.ref.value", "the field fromstore should have a valid value which exists in the system.");
			else
				materialIssue.setFromStore(stores.get(0));
				
		}
		if (materialIssue.getToStore() != null
				&& StringUtils.isNotBlank(materialIssue.getToStore().getCode())) {
			StoreGetRequest storeGetRequest = new StoreGetRequest();
			storeGetRequest.setCode(Arrays.asList(materialIssue.getToStore().getCode()));
			storeGetRequest.setTenantId(materialIssue.getTenantId());
			List<Store> stores =storeService.search(storeGetRequest).getStores();
			if (stores.isEmpty())
				throw new CustomException("invalid.ref.value", "the field tostore should have a valid value which exists in the system.");
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
				throw new CustomException("invalid.ref.value", "the field material should have a valid value which exists in the system.");
					else
						materialIssueDetail.setMaterial(materialMap.get(materialIssueDetail.getMaterial().getCode()));
				}
				if (materialIssueDetail.getUom() != null
						&& StringUtils.isNotBlank(materialIssueDetail.getUom().getCode())) {
					if (uomMap.get(materialIssueDetail.getUom().getCode()) == null)
				throw new CustomException("invalid.ref.value", "the field uom should have a valid value which exists in the system.");
					else
						materialIssueDetail.setUom(uomMap.get(materialIssueDetail.getUom().getCode()));
				}
			}
		}
		}
	}

	private void validate(List<MaterialIssue> materialIssues, String method) {
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
						errors.addDataError(ErrorCode.DATE_LE_CURRENTDATE.getCode(), "issueDate",date);
					}
					if (!materialIssue.getMaterialIssueDetails().isEmpty()) {
						for (MaterialIssueDetail materialIssueDetail : materialIssue.getMaterialIssueDetails()) {
							if (materialIssueDetail.getQuantityIssued().compareTo(BigDecimal.ZERO) <= 0)
								errors.addDataError(ErrorCode.QUANTITY_GT_ZERO.getCode(), "quantityIssued",
										materialIssueDetail.getQuantityIssued().toString());
							if(materialIssueDetail.getBalanceQuantity() != null)
							if (StringUtils.isNotBlank(materialIssueDetail.getBalanceQuantity().toString())) {
								if (materialIssueDetail.getBalanceQuantity().compareTo(BigDecimal.ZERO) <= 0)
									errors.addDataError(ErrorCode.QUANTITY_GT_ZERO.getCode(), "balanceQuantity",
											materialIssueDetail.getBalanceQuantity().toString());
								if (materialIssueDetail.getQuantityIssued()
										.compareTo(materialIssueDetail.getBalanceQuantity()) > 0) {
									errors.addDataError(ErrorCode.QUANTITY1_LTE_QUANTITY2.getCode(), "quantityIssued",
											"balanceQuantity", materialIssueDetail.getQuantityIssued().toString(),
											materialIssueDetail.getBalanceQuantity().toString());
								}
							}
							if(materialIssue.getIssuePurpose() != null)
							if(materialIssue.getIssuePurpose().equals(IssuePurposeEnum.WRITEOFFORSCRAP)){
								if(materialIssueDetail.getMaterial() != null && materialIssueDetail.getMaterial().getScrapable() != null){
								if(!materialIssueDetail.getMaterial().getScrapable())
									errors.addDataError(ErrorCode.ALLOW_SCRAP_MATERIALS.getCode(), IssuePurposeEnum.WRITEOFFORSCRAP.toString());
								}
							}
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
						errors.addDataError(ErrorCode.DATE_LE_CURRENTDATE.getCode(), "issueDate",date);
					}
					if (!materialIssue.getMaterialIssueDetails().isEmpty()) {
						for (MaterialIssueDetail materialIssueDetail : materialIssue.getMaterialIssueDetails()) {
							if (materialIssueDetail.getQuantityIssued().compareTo(BigDecimal.ZERO) <= 0)
								errors.addDataError(ErrorCode.QUANTITY_GT_ZERO.getCode(), "quantityIssued",
										materialIssueDetail.getQuantityIssued().toString());
							if(materialIssueDetail.getBalanceQuantity() != null)
							if (StringUtils.isNotBlank(materialIssueDetail.getBalanceQuantity().toString())) {
								if (materialIssueDetail.getBalanceQuantity().compareTo(BigDecimal.ZERO) <= 0)
									errors.addDataError(ErrorCode.QUANTITY_GT_ZERO.getCode(), "balanceQuantity",
											materialIssueDetail.getBalanceQuantity().toString());
								if (materialIssueDetail.getQuantityIssued()
										.compareTo(materialIssueDetail.getBalanceQuantity()) > 0) {
									errors.addDataError(ErrorCode.QUANTITY1_LTE_QUANTITY2.getCode(), "quantityIssued",
											"balanceQuantity", materialIssueDetail.getQuantityIssued().toString(),
											materialIssueDetail.getBalanceQuantity().toString());
								}
							}
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

	private void setMaterialIssueValues(MaterialIssue materialIssue, String seqNo, String action) {
		materialIssue.setIssueType(IssueTypeEnum.NONINDENTISSUE);
		if (action.equals(Constants.ACTION_CREATE)) {
			int year = Calendar.getInstance().get(Calendar.YEAR);
			materialIssue.setIssueNumber("MRIN-" + String.valueOf(year) + "-" + seqNo);
			materialIssue.setMaterialIssueStatus(MaterialIssueStatusEnum.CREATED);
		}

	}

	private void convertToUom(MaterialIssueDetail materialIssueDetail, Map<String, Uom> uomMap) {
		Double quantityIssued = 0d;
		Double quantity = 0d;
		if (materialIssueDetail.getUom() != null && uomMap.get(materialIssueDetail.getUom().getCode()) != null) {
			if (materialIssueDetail.getQuantityIssued() != null)
				quantityIssued = getSaveConvertedQuantity(
						Double.valueOf(materialIssueDetail.getQuantityIssued().toString()), Double.valueOf(
								uomMap.get(materialIssueDetail.getUom().getCode()).getConversionFactor().toString()));
			materialIssueDetail.setQuantityIssued(BigDecimal.valueOf(quantityIssued));
			if(!materialIssueDetail.getMaterialIssuedFromReceipts().isEmpty())
			for(MaterialIssuedFromReceipt mifr : materialIssueDetail.getMaterialIssuedFromReceipts()){
				if(mifr.getQuantity() != null && materialIssueDetail.getUom().getConversionFactor() != null)
				quantity = getSaveConvertedQuantity(Double.valueOf(mifr.getQuantity().toString()), Double.valueOf(materialIssueDetail.getUom().getConversionFactor().toString()));
				mifr.setQuantity(BigDecimal.valueOf(quantity));
			}
		}
	}

	public MaterialIssueResponse update(final MaterialIssueRequest materialIssueRequest, String tenantId) {
		validate(materialIssueRequest.getMaterialIssues(), Constants.ACTION_UPDATE);
		List<MaterialIssue> materialIssues = materialIssueRequest.getMaterialIssues();
		for (MaterialIssue materialIssue : materialIssues) {
			List<String> materialIssueDetailsIds = new ArrayList<>();
			if (StringUtils.isEmpty(materialIssue.getTenantId()))
				materialIssue.setTenantId(tenantId);
			setMaterialIssueValues(materialIssue, null, Constants.ACTION_UPDATE);
			materialIssue
					.setAuditDetails(getAuditDetails(materialIssueRequest.getRequestInfo(), Constants.ACTION_UPDATE));
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Uom> uomMap = getUoms(materialIssue.getTenantId(), mapper, new RequestInfo());
			for (MaterialIssueDetail materialIssueDetail : materialIssue.getMaterialIssueDetails()) {
				List<String> materialIssuedFromReceiptsIds = new ArrayList<>();
				if (StringUtils.isEmpty(materialIssueDetail.getTenantId()))
					materialIssueDetail.setTenantId(tenantId);
				convertToUom(materialIssueDetail, uomMap);
				if (StringUtils.isEmpty(materialIssueDetail.getId()))
					materialIssueDetail.setId(materialIssueDetailsJdbcRepository
							.getSequence(MaterialIssueDetail.class.getSimpleName(), 1).get(0));
				materialIssueDetailsIds.add(materialIssueDetail.getId());
				for (MaterialIssuedFromReceipt mifr : materialIssueDetail.getMaterialIssuedFromReceipts()) {
					if (StringUtils.isEmpty(mifr.getTenantId()))
						mifr.setTenantId(tenantId);
					if (StringUtils.isEmpty(mifr.getId()))
						mifr.setId(materialIssuedFromReceiptsJdbcRepository
								.getSequence(MaterialIssuedFromReceipt.class.getSimpleName(), 1).get(0));
					materialIssuedFromReceiptsIds.add(mifr.getId());
				}
				materialIssuedFromReceiptsJdbcRepository.markDeleted(materialIssuedFromReceiptsIds, tenantId,
						"materialissuedfromreceipt", "issuedetailid", materialIssueDetail.getId());
			}
			materialIssueDetailsJdbcRepository.markDeleted(materialIssueDetailsIds, tenantId, "materialissuedetail",
					"materialissuenumber", materialIssue.getIssueNumber());

		}
		kafkaTemplate.send(updateTopic, updateKey, materialIssueRequest);
		MaterialIssueResponse response = new MaterialIssueResponse();
		response.setMaterialIssues(materialIssueRequest.getMaterialIssues());
		response.setResponseInfo(getResponseInfo(materialIssueRequest.getRequestInfo()));
		return response;
	}

	public MaterialIssueResponse search(MaterialIssueSearchContract searchContract) {
		Pagination<MaterialIssue> materialIssues = materialIssueJdbcRepository.search(searchContract,
				IssueTypeEnum.NONINDENTISSUE.toString());
		if (materialIssues.getPagedData().size() > 0)
			for (MaterialIssue materialIssue : materialIssues.getPagedData()) {
				Pagination<MaterialIssueDetail> materialIssueDetails = materialIssueDetailsJdbcRepository.search(
						materialIssue.getIssueNumber(), materialIssue.getTenantId(),
						IssueTypeEnum.NONINDENTISSUE.toString());
				if (materialIssueDetails.getPagedData().size() > 0) {
					for (MaterialIssueDetail materialIssueDetail : materialIssueDetails.getPagedData()) {
						Uom uom = null;
						if (materialIssueDetail.getUom() != null && materialIssueDetail.getUom().getCode() != null)
							uom = getUom(materialIssueDetail.getTenantId(), materialIssueDetail.getUom().getCode(),
									new RequestInfo());
						Double quantityIssued = getSearchConvertedQuantity(
								Double.valueOf(materialIssueDetail.getQuantityIssued().toString()),
								Double.valueOf(uom.getConversionFactor().toString()));
						materialIssueDetail.setQuantityIssued(BigDecimal.valueOf(quantityIssued));
					}
					materialIssue.setMaterialIssueDetails(materialIssueDetails.getPagedData());
				}
			}

		MaterialIssueResponse materialIssueResponse = new MaterialIssueResponse();
		materialIssueResponse.setMaterialIssues(materialIssues.getPagedData());
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
	private String convertEpochtoDate(Long date)
	 {
		 Date epoch = new Date(date);
		 SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		 String s2 = format.format(epoch);
		 return s2;
	 }
}
