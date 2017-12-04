package org.egov.inv.domain.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
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
import org.egov.inv.model.IndentDetail;
import org.egov.inv.model.IndentResponse;
import org.egov.inv.model.IndentSearch;
import org.egov.inv.model.Material;
import org.egov.inv.model.MaterialIssue;
import org.egov.inv.model.MaterialIssueDetail;
import org.egov.inv.model.MaterialIssueRequest;
import org.egov.inv.model.MaterialIssueResponse;
import org.egov.inv.model.MaterialIssueSearchContract;
import org.egov.inv.model.MaterialIssuedFromReceipt;
import org.egov.inv.model.RequestInfo;
import org.egov.inv.model.Store;
import org.egov.inv.model.StoreGetRequest;
import org.egov.inv.model.Uom;
import org.egov.inv.persistence.entity.MaterialIssueEntity;
import org.egov.inv.persistence.repository.IndentJdbcRepository;
import org.egov.inv.persistence.repository.MaterialIssueDetailsJdbcRepository;
import org.egov.inv.persistence.repository.MaterialIssueJdbcRepository;
import org.egov.inv.persistence.repository.MaterialIssuedFromReceiptsJdbcRepository;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONArray;

@Service
public class MaterialIssuesService extends DomainService {

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

	public MaterialIssueResponse create(final MaterialIssueRequest materialIssueRequest) {
		try {
			validate(materialIssueRequest.getMaterialIssues(), Constants.ACTION_CREATE);
			List<String> sequenceNos = materialIssueJdbcRepository.getSequence(MaterialIssue.class.getSimpleName(),
					materialIssueRequest.getMaterialIssues().size());
			int i = 0;
			for (MaterialIssue materialIssue : materialIssueRequest.getMaterialIssues()) {
				String seqNo = sequenceNos.get(i);
				materialIssue.setId(seqNo);
				int year = Calendar.getInstance().get(Calendar.YEAR);
				materialIssue.setIssueNumber("MRIN-" + String.valueOf(year) + "-" + seqNo);
				materialIssue.setAuditDetails(mapAuditDetails(materialIssueRequest.getRequestInfo()));
				i++;
				int j = 0;
				if (!materialIssue.getMaterialIssueDetails().isEmpty()) {
					List<String> detailSequenceNos = materialIssueDetailsJdbcRepository.getSequence(
							MaterialIssueDetail.class.getSimpleName(), materialIssue.getMaterialIssueDetails().size());
					for (MaterialIssueDetail materialIssueDetail : materialIssue.getMaterialIssueDetails()) {
						materialIssueDetail.setId(detailSequenceNos.get(j));
						materialIssueDetail.setTenantId(materialIssue.getTenantId());
						convertToUom(materialIssueDetail);
						j++;
						int k = 0;
						List<String> materialIssuedFromReceiptsSeqNos = materialIssuedFromReceiptsJdbcRepository
								.getSequence(MaterialIssuedFromReceipt.class.getSimpleName(),
										materialIssueDetail.getMaterialIssuedFromReceipts().size());
						for (MaterialIssuedFromReceipt materialIssuedFromReceipts : materialIssueDetail
								.getMaterialIssuedFromReceipts()) {
							materialIssuedFromReceipts.setId(materialIssuedFromReceiptsSeqNos.get(k));
							materialIssuedFromReceipts.setTenantId(materialIssueDetail.getTenantId());
							k++;
						}
					}
				}
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

	private void convertToUom(MaterialIssueDetail materialIssueDetail) {
		Double quantityIssued = 0d;
		if (materialIssueDetail.getIndentDetail() != null && materialIssueDetail.getIndentDetail().getUom() != null)
			materialIssueDetail.setUom(materialIssueDetail.getIndentDetail().getUom());
		Uom uom = getUom(materialIssueDetail.getTenantId(), materialIssueDetail.getUom().getCode(), new RequestInfo());
		if (materialIssueDetail.getQuantityIssued() != null)
			quantityIssued = getSaveConvertedQuantity(
					Double.valueOf(materialIssueDetail.getQuantityIssued().toString()),
					Double.valueOf(uom.getConversionFactor().toString()));
		materialIssueDetail.setQuantityIssued(BigDecimal.valueOf(quantityIssued));
	}

	private void validate(List<MaterialIssue> materialIssues, String method) {
		try {
			switch (method) {
			case "create":
				if (materialIssues == null) {
					throw new InvalidDataException("materialIssues", ErrorCode.NOT_NULL.getCode(), null);
				}
				for (MaterialIssue materialIssue : materialIssues) {
					if (!materialIssueJdbcRepository.uniqueCheck("",
							new MaterialIssueEntity().toEntity(materialIssue))) {

					}
				}
				break;
			case "update":
				for (MaterialIssue materialIssue : materialIssues) {
					if (StringUtils.isEmpty(materialIssue.getIssueNumber()))
						throw new CustomBindException("Issue Number is not provided");
				}
			default:

			}
		} catch (IllegalArgumentException e) {
		}

	}

	public MaterialIssueResponse update(final MaterialIssueRequest materialIssueRequest, String tenantId) {
		validate(materialIssueRequest.getMaterialIssues(), Constants.ACTION_UPDATE);
		List<MaterialIssue> materialIssues = materialIssueRequest.getMaterialIssues();
		for (MaterialIssue materialIssue : materialIssues) {
			List<String> materialIssueDetailsIds = new ArrayList<>();
			if (StringUtils.isEmpty(materialIssue.getTenantId()))
				materialIssue.setTenantId(tenantId);
			materialIssue
					.setAuditDetails(getAuditDetails(materialIssueRequest.getRequestInfo(), Constants.ACTION_UPDATE));
			for (MaterialIssueDetail materialIssueDetail : materialIssue.getMaterialIssueDetails()) {
				List<String> materialIssuedFromReceiptsIds = new ArrayList<>();
				if (StringUtils.isEmpty(materialIssueDetail.getTenantId()))
					materialIssueDetail.setTenantId(tenantId);
				convertToUom(materialIssueDetail);
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

	public MaterialIssueResponse search(final MaterialIssueSearchContract searchContract) {
		Pagination<MaterialIssue> materialIssues = materialIssueJdbcRepository.search(searchContract);
		if (materialIssues.getPagedData().size() > 0)
			for (MaterialIssue materialIssue : materialIssues.getPagedData()) {
				Pagination<MaterialIssueDetail> materialIssueDetails = materialIssueDetailsJdbcRepository
						.search(materialIssue.getIssueNumber(), materialIssue.getTenantId());

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

	public MaterialIssueResponse prepareMIFromIndents(MaterialIssueRequest materialIssueRequest, String tenantId) {
		for (MaterialIssue materialIssue : materialIssueRequest.getMaterialIssues()) {
			
			if (materialIssue.getIndent() != null
					&& StringUtils.isNotBlank(materialIssue.getIndent().getIndentNumber())) {
				IndentSearch indentSearch = new IndentSearch();
				indentSearch.setIndentNumber(materialIssue.getIndent().getIndentNumber());
				indentSearch.setTenantId(tenantId);
				materialIssue.setIndent(indentService.search(indentSearch, new RequestInfo()).getIndents().get(0));
				if (materialIssue.getIndent().getIssueStore() != null && StringUtils.isNotEmpty(materialIssue.getIndent().getIssueStore().getCode()))
				{
					StoreGetRequest storeGetRequest =StoreGetRequest.builder().code(Arrays.asList(materialIssue.getIndent().getIssueStore().getCode())).tenantId(tenantId).build();
					Store store = storeService.search(storeGetRequest).getStores().get(0);
					if(store != null && store.getDepartment() != null && StringUtils.isNotBlank(store.getDepartment().getCode())){
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
						/*	Material material = materialService.fetchMaterial(tenantId,
									indentDetail.getMaterial().getCode(), new RequestInfo());*/
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
		JSONArray responseJSONArray = mdmsRepository.getByCriteria(tenantId, "inventory",
                "Material", null, null, requestInfo);
		Map<String, Material> materialMap = new HashMap<>();

		if (responseJSONArray != null && responseJSONArray.size() > 0) {
			for (int i = 0; i < responseJSONArray.size(); i++) {
				Material material = mapper.convertValue(responseJSONArray.get(i), Material.class);
				materialMap.put(material.getCode(), material);
			}

		}
		return materialMap;
	}


}
