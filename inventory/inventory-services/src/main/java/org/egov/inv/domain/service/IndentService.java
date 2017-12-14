package org.egov.inv.domain.service;

import static org.springframework.util.StringUtils.isEmpty;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.Constants;
import org.egov.common.DomainService;
import org.egov.common.MdmsRepository;
import org.egov.common.Pagination;
import org.egov.common.exception.CustomBindException;
import org.egov.common.exception.ErrorCode;
import org.egov.common.exception.InvalidDataException;
import org.egov.inv.api.AssetRepository;
import org.egov.inv.domain.util.InventoryUtilities;
import org.egov.inv.model.FinancialYear;
import org.egov.inv.model.Indent;
import org.egov.inv.model.Indent.IndentPurposeEnum;
import org.egov.inv.model.Indent.IndentStatusEnum;
import org.egov.inv.model.Indent.IndentTypeEnum;
import org.egov.inv.model.IndentDetail;
import org.egov.inv.model.IndentRequest;
import org.egov.inv.model.IndentResponse;
import org.egov.inv.model.IndentSearch;
import org.egov.inv.model.RequestInfo;
import org.egov.inv.model.Tenant;
import org.egov.inv.model.Uom;
import org.egov.inv.persistence.entity.IndentDetailEntity;
import org.egov.inv.persistence.entity.IndentEntity;
import org.egov.inv.persistence.repository.IndentDetailJdbcRepository;
import org.egov.inv.persistence.repository.IndentJdbcRepository;
import org.egov.inv.persistence.repository.StoreJdbcRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONArray;
/**
 * 
 * @author mani
 * In search for PO show only records which 
 *   1. If appconfig is to consider whole indentquantiy return podetails where qty-(poordered+issued) >0
 *   2. If not return podetails qty-poordered
 *   3. Maintain seperate field for ui quantity
 *   4. all qty in base uom
 *   5. give api for status change
 *   6. give api for force close 
 *   7. Consider the status closed for not to show in issue or po
 *    
 */
@Service
@Transactional(readOnly = true)
public class IndentService extends DomainService {
	/*
	 * @Autowired private StoreRepository storeRepository;
	 * 
	 * @Autowired private DepartmentRepository departmentRepository;
	 */

	@Autowired
	  private RestTemplate restTemplate;
	@Autowired
	private IndentJdbcRepository indentRepository;

	@Value("${inv.indents.save.topic}")
	private String saveTopic;

	@Value("${inv.indents.save.key}")
	private String saveKey;

	@Value("${inv.indents.update.topic}")
	private String updateTopic;

	@Value("${inv.indents.update.key}")
	private String updateKey;

	@Autowired
	private IndentDetailJdbcRepository indentDetailJdbcRepository;

	@Autowired
	private StoreJdbcRepository storeJdbcRepository;
	@Autowired
	private MdmsRepository mdmsRepository;

	@Value("${inv.indents.addquantity.topic}")
	private String addQuantityTopic;

	@Value("${inv.indents.subtractquantity.topic}")
	private String subtractQuantityTopic;

	@Value("${inv.indents.addquantity.key}")
	private String addQuantitykey;

	@Value("${inv.indents.subtractquantity.key}")
	private String subtractQuantityKey;

	@Autowired
	private NumberGenerator numberGenerator;
	@Autowired
	private AssetRepository assetRepository;

	private static final Logger LOG = LoggerFactory.getLogger(IndentService.class);

	@Transactional
	public IndentResponse create(IndentRequest indentRequest) {

		try {
			indentRequest = fetchRelated(indentRequest);
			validate(indentRequest.getIndents(), Constants.ACTION_CREATE);
			List<String> sequenceNos = indentRepository.getSequence(Indent.class.getSimpleName(),
					indentRequest.getIndents().size());
			int i = 0;
			for (Indent b : indentRequest.getIndents()) {
				b.setId(sequenceNos.get(i));
				// move to id-gen with format <ULB short code>/<Store
				// Code>/<fin. Year>/<serial No.>
				b.setIndentNumber(getIndentNumber(b, indentRequest.getRequestInfo()));
				i++;
				int j = 0;
				// TO-DO : when workflow implemented change this to created
				b.setIndentStatus(IndentStatusEnum.APPROVED);
				b.setAuditDetails(getAuditDetails(indentRequest.getRequestInfo(), Constants.ACTION_CREATE));

				List<String> detailSequenceNos = indentRepository.getSequence(IndentDetail.class.getSimpleName(),
						b.getIndentDetails().size());
				setUpdatedQuantities(b);
				for (IndentDetail d : b.getIndentDetails()) {
					// save quantity in base uom
					BigDecimal quantityInBaseUom = InventoryUtilities.getQuantityInBaseUom(d.getIndentQuantity(),
							d.getUom().getConversionFactor());
					d.setIndentQuantity(quantityInBaseUom);
					d.setId(detailSequenceNos.get(j));
					d.setTenantId(b.getTenantId());
					j++;
				}
			}
			kafkaQue.send(saveTopic, saveKey, indentRequest);
			IndentResponse response = new IndentResponse();
			response.setIndents(indentRequest.getIndents());
			response.setResponseInfo(getResponseInfo(indentRequest.getRequestInfo()));
			return response;
		} catch (Exception e) {
			throw e;
		}

	}

	private String getIndentNumber(Indent b, RequestInfo info) {
		InvalidDataException errors = new InvalidDataException();
		ObjectMapper mapper = new ObjectMapper();
		JSONArray tenantStr = mdmsRepository.getByCriteria(b.getTenantId(), "tenant", "tenants", "code",
				b.getTenantId(), info);

		Tenant tenant = mapper.convertValue(tenantStr.get(0), Tenant.class);
		if (tenant == null) {
			errors.addDataError(ErrorCode.CITY_CODE_NOT_AVAILABLE.getCode(), b.getTenantId());
		}
		String finYearRange = "";
		JSONArray finYears = mdmsRepository.getByCriteria("default", "egf-master", "financialYears", null, null, info);
		outer: for (int i = 0; i < finYears.size(); i++) {
			FinancialYear fin = mapper.convertValue(finYears.get(i), FinancialYear.class);
			LOG.info("Indentdate" + b.getIndentDate());
			LOG.info("FinYear start date:" + fin.getStartingDate());
			if (b.getIndentDate() >= fin.getStartingDate().getTime()
					&& b.getIndentDate() <= fin.getEndingDate().getTime()) {
				finYearRange = fin.getFinYearRange();
				break outer;
			}
		}

		if (finYearRange.isEmpty()) {
			errors.addDataError(ErrorCode.FIN_YEAR_NOT_EXIST.getCode(), b.getIndentDate().toString());
		}
		if (errors.getValidationErrors().size() > 0) {
			throw errors;
		}

		String seq = "IND/" + tenant.getCity().getCode() + "/" + b.getIssueStore().getCode() + "/" + finYearRange;
		return seq + "/" + numberGenerator.getNextNumber(seq, 5);
	}

	@Transactional
	public IndentResponse update(IndentRequest indentRequest) {

		try {
			String tenantId = "";
			indentRequest = fetchRelated(indentRequest);

			String indentNumber = "";
			List<String> ids = new ArrayList<String>();
			validate(indentRequest.getIndents(), Constants.ACTION_CREATE);
			for (Indent b : indentRequest.getIndents()) {
				b.setIndentType(IndentTypeEnum.INDENTNOTE);
				if (!isEmpty(b.getAction())) {
					updateIndentQuantity(b);
				}
				int j = 0;
				if (!indentNumber.isEmpty()) {
					indentNumber = b.getIndentNumber();
				}
				b.setAuditDetails(getAuditDetails(indentRequest.getRequestInfo(), Constants.ACTION_UPDATE));
				for (IndentDetail d : b.getIndentDetails()) {
					if (d.getId() == null)
						d.setId(indentRepository.getSequence(IndentDetail.class.getSimpleName(), 1).get(0));
					ids.add(d.getId());
					BigDecimal quantityInBaseUom = InventoryUtilities.getQuantityInBaseUom(d.getIndentQuantity(),
							d.getUom().getConversionFactor());
					d.setIndentQuantity(quantityInBaseUom);
					d.setTenantId(b.getTenantId());
					if (tenantId.isEmpty())
						tenantId = b.getTenantId();
					j++;
				}
			}

			kafkaQue.send(saveTopic, saveKey, indentRequest);
			indentDetailJdbcRepository.markDeleted(ids, tenantId, "indentdetail", "indentNumber", indentNumber);
			IndentResponse response = new IndentResponse();
			response.setIndents(indentRequest.getIndents());
			response.setResponseInfo(getResponseInfo(indentRequest.getRequestInfo()));
			return response;
		} catch (CustomBindException e) {
			throw e;
		}

	}

	public IndentResponse search(IndentSearch is, RequestInfo info) {

		ObjectMapper mapper = new ObjectMapper();
		Map<String, Uom> uomMap = getUoms(is.getTenantId(), mapper, info);
		IndentResponse response = new IndentResponse();
		Pagination<Indent> search = indentRepository.search(is);
		if (!search.getPagedData().isEmpty()) {
			List<String> indentNumbers = new ArrayList<>();
			for (Indent indent : search.getPagedData()) {
				indentNumbers.add(indent.getIndentNumber());
			}

			List<IndentDetailEntity> indentDetails = indentDetailJdbcRepository.find(indentNumbers, is.getTenantId(),is.getSearchPurpose());

			if(indentDetails!=null )
			{
			IndentDetail detail = null;
			for (Indent indent : search.getPagedData()) {
				for (IndentDetailEntity detailEntity : indentDetails) {
					if (indent.getIndentNumber().equalsIgnoreCase(detailEntity.getIndentNumber())) {
						detail = detailEntity.toDomain();
						detail.setIndentQuantity(InventoryUtilities.getQuantityInSelectedUom(detail.getIndentQuantity(),
								uomMap.get(detail.getUom().getCode()).getConversionFactor()));
						indent.addIndentDetailsItem(detail);
					}
				}
			}
			}
		}
		response.setIndents(search.getPagedData());
		response.setPage(getPage(search));
		return response;

	}

	private void updateIndentQuantity(Indent indent) {
		setUpdatedQuantities(indent);
		if (!isEmpty(indent.getAction()) && "Add".equalsIgnoreCase(indent.getAction())) {
			kafkaQue.send(addQuantityTopic, addQuantitykey, indent);
		}
		if (!isEmpty(indent.getAction()) && "Subtract".equalsIgnoreCase(indent.getAction())) {
			kafkaQue.send(subtractQuantityTopic, subtractQuantityKey, indent);
		}
	}

	private Indent setUpdatedQuantities(Indent indent) {
		for (IndentDetail indentDetail : indent.getIndentDetails()) {
			if (isEmpty(indentDetail.getIndentIssuedQuantity())) {
				indentDetail.setIndentIssuedQuantity(BigDecimal.valueOf(0L));
			}
			if (isEmpty(indentDetail.getInterstoreRequestQuantity())) {
				indentDetail.setInterstoreRequestQuantity(BigDecimal.valueOf(0L));
			}
			if (isEmpty(indentDetail.getPoOrderedQuantity())) {
				indentDetail.setPoOrderedQuantity(BigDecimal.valueOf(0L));
			}
			if (isEmpty(indentDetail.getTotalProcessedQuantity())) {
				indentDetail.setTotalProcessedQuantity(BigDecimal.valueOf(0L));
			}
		}
		return indent;
	}

	private void validate(List<Indent> indents, String method) {
		InvalidDataException errors = new InvalidDataException();
		try {
			Long currentDate = currentEpochWithoutTime();
			currentDate = currentDate + (24 * 60 * 60 * 1000) - 1;
			LOG.info("CurrentDate is " + toDateStr(currentDate));

			Long ll = new Date().getTime();
			switch (method) {
			case Constants.ACTION_UPDATE: {
				if (indents == null) {
					errors.addDataError(ErrorCode.NOT_NULL.getCode(), "indents", "null");
				}

				for (Indent indent : indents) {
					IndentEntity entity = indentRepository.findById(new IndentEntity().toEntity(indent));
					if (entity.getIndentStatus().equalsIgnoreCase(IndentStatusEnum.CREATED.name())) {
						continue;
					} else {
						errors.addDataError(ErrorCode.UPDATE_NOT_ALLOWED.getCode(), "indent", entity.getIndentStatus(),
								indent.getIndentNumber());
					}

				}
				if(errors.getValidationErrors().size()>0)
					break;

			}

			case Constants.ACTION_CREATE: {
				if (indents == null) {
					errors.addDataError(ErrorCode.NOT_NULL.getCode(), "indents", "null");
				}
				for (Indent indent : indents) {
					LOG.info("-----------------------------------------------------------");
					LOG.info("CurrentDate is " + toDateStr(currentDate));
					LOG.info("indentDate is " + toDateStr(indent.getIndentDate()));
					LOG.info("now is " + toDateStr(ll));
					LOG.info("compare  " + ll.compareTo(currentDate));
					LOG.info("compare  " + ll.compareTo(currentDate));
					LOG.info("-----------------------------------------------------------");

					if (indent.getIndentDate().compareTo(currentDate) >= 0) {
						String indentDate = convertEpochtoDate(indent.getIndentDate());
						errors.addDataError(ErrorCode.DATE_LE_CURRENTDATE.getCode(), "indentDate",indentDate);
					}
					// commeneted as of now to support the past dated entries
					/*
					 * if(indent.getExpectedDeliveryDate().compareTo(
					 * currentDate) < 0) {
					 * errors.addDataError(ErrorCode.DATE_GE_CURRENTDATE.getCode
					 * (),
					 * "expectedDeliveryDate",indent.getExpectedDeliveryDate().
					 * toString()); }
					 */
					if (indent.getExpectedDeliveryDate() != null
							&& indent.getIndentDate().compareTo(indent.getExpectedDeliveryDate()) > 0) {
						LOG.info("expectedDeliveryDate=" + toDateStr(indent.getExpectedDeliveryDate()));
						LOG.info("indentDate=" + toDateStr(indent.getIndentDate()));
						String expectedDeliveryDate = convertEpochtoDate(indent.getExpectedDeliveryDate());
						String indentDate = convertEpochtoDate(indent.getIndentDate());
						errors.addDataError(ErrorCode.DATE1_GE_DATE2.getCode(), "expectedDeliveryDate", "indentDate",
								expectedDeliveryDate, indentDate);
					}
					List<String> materialCodes = new ArrayList<>();
					int i = 0;
					for (IndentDetail detail : indent.getIndentDetails()) {
						++i;
						if (materialCodes.isEmpty()) {

							materialCodes.add(detail.getMaterial().getCode());

						} else {
							if (materialCodes.indexOf(detail.getMaterial().getCode()) == -1) {
								materialCodes.add(detail.getMaterial().getCode());
							}

							else {
								errors.addDataError(ErrorCode.REPEATED_VALUE.getCode(), "material",
										detail.getMaterial().getCode(), " at serial no. " + i + " and "
												+ (materialCodes.indexOf(detail.getMaterial().getCode()) + 1));
							}
						}

						if (indent.getIndentPurpose().equals(IndentPurposeEnum.CAPITAL)) {
							if (detail.getProjectCode() == null || detail.getProjectCode().getCode() == null)
								errors.addDataError(ErrorCode.MANDATORY_BASED_ON.getCode(), "projectCode",
										"indentPurpose=Capital", "at serail no. " + i);
						}
						if (indent.getIndentPurpose().equals(IndentPurposeEnum.REPAIRSANDMAINTENANCE)) {
							if (detail.getAsset() == null || detail.getAsset().getCode() == null)
								errors.addDataError(ErrorCode.MANDATORY_BASED_ON.getCode(), "assetCode",
										"indentPurpose=Repairs and Maintenance", "at seraill no. " + i);
						}

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

	public IndentRequest fetchRelated(IndentRequest indentRequest) {
		String tenantId = indentRequest.getIndents().get(0).getTenantId();
		ObjectMapper mapper = new ObjectMapper();

		RequestInfo requestInfo = indentRequest.getRequestInfo();

		Map<String, Uom> uomMap = getUoms(tenantId, mapper, requestInfo);

		for (Indent indent : indentRequest.getIndents()) {

			// fetch related items

			
//			  if (indent.getIssueStore() != null) {
//			 indent.getIssueStore().setTenantId(tenantId); Store issueStore =
//			 (Store)
//			   storeJdbcRepository.findById(indent.getIssueStore(),"StoreEntity"
//			 ); if (issueStore == null) { throw new
//			   InvalidDataException("issueStore", "issueStore.invalid",
//			  " Invalid issueStore"); } indent.setIssueStore(issueStore); } if
//			  (indent.getIndentStore() != null) {
//			  indent.getIndentStore().setTenantId(tenantId); Store indentStore
//			  = (Store) storeJdbcRepository.findById(indent.getIndentStore(),
//			  "StoreEntity"); if (indentStore == null) { throw new
//			  InvalidDataException("indentStore", "indentStore.invalid",
//			  " Invalid indentStore"); } indent.setIndentStore(indentStore); }
//			 
			for (IndentDetail detail : indent.getIndentDetails()) {
				detail.setUom(uomMap.get(detail.getUom().getCode()));
				/*if(detail.getAsset().getCode()!=null)
				{
					Asset a=assetRepository.findByCode(detail.getAsset(),indentRequest.getRequestInfo());
					detail.setAsset(a);
				}*/
				
				
			}

			/*if (indent.getDepartment() != null) {
				Department department = restTemplate.postForEntity("", indentRequest.getRequestInfo(), Department.class);
				if (department == null) {
					throw new InvalidDataException("department", "department.invalid", " Invalid department");
				}
				indent.setDepartment(department);
			}*/

		}

		return indentRequest;
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
	 private String convertEpochtoDate(Long date)
	 {
		 Date epoch = new Date(date);
		 SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		 String s2 = format.format(epoch);
		 return s2;
	 }
}