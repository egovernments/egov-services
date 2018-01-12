package org.egov.inv.domain.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.egov.common.Constants;
import org.egov.common.DomainService;
import org.egov.common.MdmsRepository;
import org.egov.common.Pagination;
import org.egov.common.exception.CustomBindException;
import org.egov.common.exception.ErrorCode;
import org.egov.common.exception.InvalidDataException;
import org.egov.inv.model.FinancialYear;
import org.egov.inv.model.MaterialIssue;
import org.egov.inv.model.MaterialIssue.MaterialIssueStatusEnum;
import org.egov.inv.model.MaterialIssueDetail;
import org.egov.inv.model.MaterialIssueResponse;
import org.egov.inv.model.MaterialIssueSearchContract;
import org.egov.inv.model.RequestInfo;
import org.egov.inv.model.Scrap;
import org.egov.inv.model.Scrap.ScrapStatusEnum;
import org.egov.inv.model.ScrapDetail;
import org.egov.inv.model.ScrapDetailSearch;
import org.egov.inv.model.ScrapRequest;
import org.egov.inv.model.ScrapResponse;
import org.egov.inv.model.ScrapSearch;
import org.egov.inv.model.Store;
import org.egov.inv.model.StoreGetRequest;
import org.egov.inv.model.Tenant;
import org.egov.inv.model.Uom;
import org.egov.inv.persistence.entity.MaterialIssueDetailEntity;
import org.egov.inv.persistence.entity.MaterialIssueEntity;
import org.egov.inv.persistence.repository.ScrapDetailJdbcRepository;
import org.egov.inv.persistence.repository.ScrapJdbcRepository;
import org.egov.inv.persistence.repository.StoreJdbcRepository;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONArray;

@Service
public class ScrapService extends DomainService{
	
	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;
	
	@Autowired
	private NonIndentMaterialIssueService nonIndentMaterialIssueService;
	
	@Autowired
	private ScrapJdbcRepository scrapJdbcRepository;
	
	@Autowired
	private MdmsRepository mdmsRepository;
	
	@Autowired
	private StoreJdbcRepository storeJdbcRepository;
	
	@Autowired
	private NumberGenerator numberGenerator;
	
	@Autowired
	private ScrapDetailJdbcRepository scrapDetailJdbcRepository;
	
	@Value("${inv.scrap.save.topic}")
	private String createTopic;
	
	@Value("${inv.scrap.update.topic}")
	private String updateTopic;
	
	public List<Scrap> create(ScrapRequest scrapReq, String tenantId) {
	try{
		fetchRelated(scrapReq,tenantId);
		List<Scrap> scrap = scrapReq.getScraps();
		validate(scrapReq.getScraps(), Constants.ACTION_CREATE,tenantId,scrapReq.getRequestInfo());
		
		scrap.forEach(scrapData -> {
				scrapData.setId(scrapJdbcRepository.getSequence("seq_scrap"));
				scrapData.setScrapNumber(getScrapNumber(scrapData, scrapReq.getRequestInfo(), tenantId));
				scrapData.setScrapStatus(ScrapStatusEnum.APPROVED);
			if (StringUtils.isEmpty(scrapData.getTenantId())) {
				scrapData.setTenantId(tenantId);
			}
				scrapData.getScrapDetails().forEach(scrapDetails -> {
				scrapDetails.setId(scrapJdbcRepository.getSequence("seq_scrapDetail"));
				scrapDetails.setTenantId(tenantId);
			});
		});
		materialIssueBackUpdate(scrapReq,tenantId);
				kafkaTemplate.send(createTopic, scrapReq);
		return scrapReq.getScraps();
		}catch(CustomBindException e) {
			throw e;
		}
	}
	
	 public List<Scrap> update(ScrapRequest scrapReq, String tenantId) {
		try{
			fetchRelated(scrapReq,tenantId);
			List<Scrap> scrap = scrapReq.getScraps();
			List<String> scrapDetailNumbers = new ArrayList<>();
			List<String> scrapNumbers = new ArrayList<>();
			validate(scrapReq.getScraps(), Constants.ACTION_UPDATE,tenantId,scrapReq.getRequestInfo());
			
			scrap.forEach(scrapData -> {
				if (StringUtils.isEmpty(scrapData.getTenantId())) {
						scrapData.setTenantId(tenantId);
				}
				if(StringUtils.isEmpty(scrapData.getId())){
					scrapData.setId(scrapJdbcRepository.getSequence("seq_scrap"));
				}
				scrapNumbers.add(scrapData.getScrapNumber());

				scrapData.getScrapDetails().forEach(scrapDetails -> {
					if(StringUtils.isEmpty(scrapDetails.getId())){
						scrapDetails.setId(scrapJdbcRepository.getSequence("seq_scrapDetail"));
					}
					if (StringUtils.isEmpty(scrapDetails.getTenantId())) {
						scrapDetails.setTenantId(tenantId);
					}
					scrapDetails.setScrapNumber(scrapData.getScrapNumber());
					scrapDetailNumbers.add(scrapDetails.getScrapNumber());

					scrapJdbcRepository.markDeleted(scrapDetailNumbers, tenantId, "scrapDetail", "scrapNumber", scrapDetails.getScrapNumber());

					scrapJdbcRepository.markDeleted(scrapNumbers, tenantId, "scrap", "scrapNumber", scrapData.getScrapNumber());

				});
			});
			materialIssueBackUpdate(scrapReq,tenantId);
			kafkaTemplate.send(updateTopic, scrapReq);
			return scrapReq.getScraps();
			}catch(CustomBindException e) {
				throw e;
			}
		}
	
	 public ScrapResponse search(ScrapSearch scrapSearch) {
		Pagination<Scrap> scrapPagination = scrapJdbcRepository.search(scrapSearch);
		if (scrapPagination.getPagedData().size() > 0) {
	            for (Scrap scrap : scrapPagination.getPagedData()) {
	                List<ScrapDetail> scrapDetail = getScrapDetails(scrap.getScrapNumber(), scrapSearch.getTenantId());
	                scrap.setScrapDetails(scrapDetail);
	            }
		}
		
		ScrapResponse response = new ScrapResponse();
		return response.responseInfo(null).scraps(scrapPagination.getPagedData().size() > 0
				? scrapPagination.getPagedData() : Collections.EMPTY_LIST);
	}
	
	 private void validate(List<Scrap> scrap, String method, String tenantId,RequestInfo info) {
		InvalidDataException errors = new InvalidDataException();

		try {
			switch (method) {
				case Constants.ACTION_CREATE: {
					if (scrap == null) {
						errors.addDataError(ErrorCode.NOT_NULL.getCode(),"scrap", null);
						} 
					}
					break;
	
				case Constants.ACTION_UPDATE: {
					if (scrap == null) {
						errors.addDataError(ErrorCode.NOT_NULL.getCode(),"scrap", null);
						}
					}
					break;
				}
			for(Scrap scrapData : scrap )
			{
				if(null == scrapData.getScrapDate()) {
					errors.addDataError(ErrorCode.NOT_NULL.getCode(),"scrap Date", null);
				}
					if(null == scrapData.getStore().getName() || scrapData.getStore().getName().isEmpty()){
						errors.addDataError(ErrorCode.NOT_NULL.getCode(),"Store Name", null);
					}
						if(null == scrapData.getStore().getCode() || scrapData.getStore().getName().isEmpty()){
							errors.addDataError(ErrorCode.NOT_NULL.getCode(),"Store Code", null);
						}else{
							if(validateStore(tenantId,scrapData)) {
								errors.addDataError(ErrorCode.INVALID_ACTIVE_VALUE.getCode(),"Store "+ scrapData.getStore().getCode());
							}
						}
							if(null == scrapData.getDescription() ||  scrapData.getDescription().isEmpty()) {
								errors.addDataError(ErrorCode.NOT_NULL.getCode(),"Description", null);
							}
				
				for(ScrapDetail scrapDetail : scrapData.getScrapDetails()){
					if(null == scrapDetail.getUserQuantity()) {
						errors.addDataError(ErrorCode.NOT_NULL.getCode(),"Scrap Quantity", null);
					}else
					{
						int scrapQty =scrapDetail.getUserQuantity().compareTo(BigDecimal.ZERO);
					if (scrapQty != 1) {
						errors.addDataError(ErrorCode.QUANTITY_GT_ZERO.getCode(),"Scrap Quantity "+scrapDetail.getUserQuantity());
					}
					}
					int scrapValue =scrapDetail.getScrapValue().compareTo(BigDecimal.ZERO);
					if(scrapValue!=1){
						errors.addDataError(ErrorCode.UNIT_PRICE_GT_ZERO.getCode());

					}
					BigDecimal remainQuantity= scrapDetail.getQuantity().subtract(scrapDetail.getIssueDetail().getScrapedQuantity());
					if((scrapDetail.getScrapQuantity().compareTo(remainQuantity)) == 1){
						errors.addDataError(ErrorCode.QTY1_LE_QTY2.getCode(),"Scrap Quantity  "+scrapDetail.getScrapQuantity(),"Available Issue Quantity " +remainQuantity);

					}
				}
			}
		}catch(IllegalArgumentException e) {
			throw e;
		}
		if (errors.getValidationErrors().size() > 0)
			throw errors;
	}
	
	 private String getScrapNumber(Scrap scrap, RequestInfo info,String tenantId) {
		InvalidDataException errors = new InvalidDataException();
		ObjectMapper mapper = new ObjectMapper();
		JSONArray tenantStr = mdmsRepository.getByCriteria(tenantId, "tenant", "tenants", "code",
				scrap.getTenantId(), info);

		Tenant tenant = mapper.convertValue(tenantStr.get(0), Tenant.class);
		if (tenant == null) {
			errors.addDataError(ErrorCode.CITY_CODE_NOT_AVAILABLE.getCode(), tenantId);
		}
		String finYearRange = "";
		JSONArray finYears = mdmsRepository.getByCriteria(tenantId, "egf-master", "FinancialYear", null, null, info);
		outer: for (int i = 0; i < finYears.size(); i++) {
			FinancialYear fin = mapper.convertValue(finYears.get(i), FinancialYear.class);
			if (getCurrentDate() >= fin.getStartingDate().getTime()  && getCurrentDate() <= fin.getEndingDate().getTime()) {
				finYearRange = fin.getFinYearRange();
				break outer;
			}
		}

		if (errors.getValidationErrors().size() > 0) {
			throw errors;
		}

		String seq = "SCRP/" + tenant.getCity().getCode() + "/" + finYearRange;
		return seq + "/" + numberGenerator.getNextNumber(seq, 5);
	}
	
	 private void fetchRelated(ScrapRequest request, String tenantId) {
		InvalidDataException errors = new InvalidDataException();
		List<ScrapDetail> scrapDetailList = new ArrayList<>();
		for (Scrap scrap : request.getScraps()) {
			for (ScrapDetail scrapDetails : scrap.getScrapDetails())
			{
		MaterialIssueSearchContract searchContract = MaterialIssueSearchContract.builder()
													.issuePurpose(MaterialIssue.IssuePurposeEnum.WRITEOFFORSCRAP.toString())
													.materialIssueStatus(MaterialIssueStatusEnum.APPROVED.toString())
													.scrapCreated(false)
													.tenantId(tenantId)
													.build();
													
		MaterialIssueResponse response = nonIndentMaterialIssueService.search(searchContract);
		if(response.getMaterialIssues().size() <= 0 )
		{
			errors.addDataError(ErrorCode.NO_DATA_FOUND.getCode(), "Scrap Process");

		}
		for(MaterialIssue issue : response.getMaterialIssues()){
			for(MaterialIssueDetail issueDetail : issue.getMaterialIssueDetails()) {
				ScrapDetail scrapDetail= new ScrapDetail();
				MaterialIssueDetail matIssueDetail =  new MaterialIssueDetail();
				matIssueDetail.setId(issueDetail.getId());
				matIssueDetail.setScrapedQuantity(issueDetail.getScrapedQuantity());
		if (issue == null )
			errors.addDataError(ErrorCode.NO_DATA_FOUND.getCode(), "Scrap Process", null);
		
		else
			scrapDetail.setId(scrapDetails.getId());
			scrapDetail.setIssueDetail(matIssueDetail);
			scrapDetail.setTenantId(tenantId); 
			scrapDetail.setUom(issueDetail.getUom());
			scrapDetail.setMaterial(issueDetail.getMaterial());
			scrapDetail.setExistingValue(issueDetail.getValue());
			scrapDetail.setQuantity(issueDetail.getQuantityIssued());
			
			if(scrapDetails.getUserQuantity() != null) {
				setConvertedScrapQuantity(tenantId, scrapDetails,issueDetail);
			}
			
			if(scrapDetails.getScrapValue() != null) {
				setConvertedScrapRate(tenantId, scrapDetails,issueDetail);
			}
			scrapDetail.setScrapQuantity(scrapDetails.getScrapQuantity());
			scrapDetail.setUserQuantity(scrapDetails.getUserQuantity());
			scrapDetail.setScrapReason(scrapDetail.getScrapReason());
			scrapDetail.setScrapValue(scrapDetails.getScrapValue());
			scrapDetailList.add(scrapDetail);
			
			}
		}
		scrap.setScrapDetails(scrapDetailList);
				}
		}
		if (errors.getValidationErrors().size() > 0)
			throw errors;
	}
	
	 private List<ScrapDetail> getScrapDetails(String scrapNumber, String tenantId) {
	        ScrapDetailSearch scrapDetailSearch = ScrapDetailSearch.builder()
	                .ScrapNumber(scrapNumber)
	                .tenantId(tenantId)
	                .build();
	        Pagination<ScrapDetail> scrapDetails = scrapDetailJdbcRepository.search(scrapDetailSearch);
	        return scrapDetails.getPagedData().size() > 0 ? scrapDetails.getPagedData() : Collections.EMPTY_LIST;
	    }
	 
	 private void setConvertedScrapRate(String tenantId, ScrapDetail detail,MaterialIssueDetail issueDetail) {
			Uom uom = (Uom) mdmsRepository.fetchObject(tenantId, "common-masters", "Uom",  "code", issueDetail.getUom().getCode(), Uom.class);
			issueDetail.setUom(uom);

			if (null != detail.getScrapValue() && null != uom.getConversionFactor()) {
				BigDecimal convertedRate = getSaveConvertedRate(detail.getScrapValue(),
						uom.getConversionFactor());
				detail.setScrapValue(convertedRate);
			}

		}
	 
	 private void setConvertedScrapQuantity(String tenantId, ScrapDetail detail,MaterialIssueDetail issueDetail) {
			InvalidDataException errors = new InvalidDataException();
			Uom uom = (Uom) mdmsRepository.fetchObject(tenantId, "common-masters", "Uom", "code", issueDetail.getUom().getCode(), Uom.class);
			issueDetail.setUom(uom);

			if (null != detail.getUserQuantity() && null != uom.getConversionFactor()) {
				BigDecimal convertedUserQuantity = getSaveConvertedQuantity(detail.getUserQuantity(),
						uom.getConversionFactor());
				detail.setScrapQuantity(convertedUserQuantity);
				int res =convertedUserQuantity.compareTo(issueDetail.getQuantityIssued());				
				if(res == 1) {
					errors.addDataError(ErrorCode.QTY1_LE_QTY2.getCode(), "Scrap Quantity ", "Issued Quantity ",null);

				}
			}
			
			if (errors.getValidationErrors().size() > 0)
				throw errors;
					
		}
	 
	 private boolean validateStore(String tenantId, Scrap scrap) {
			StoreGetRequest storeEntity = StoreGetRequest.builder()
												 .code(Collections.singletonList(scrap.getStore().getCode()))
											     .tenantId(tenantId)
											     .active(true)
												 .build();
			Pagination<Store> store =storeJdbcRepository.search(storeEntity);
			if(store.getPagedData().size() > 0) {
				return false;
			}
			return  true;
		}
	 
	 private Long getCurrentDate() {
	        return currentEpochWithoutTime() + (24 * 60 * 60) - 1;
	    }
	 
	 private void materialIssueBackUpdate(ScrapRequest request,String tenantId ){
		 
		 for(Scrap scrap : request.getScraps())
		 {
			 for(ScrapDetail detail: scrap.getScrapDetails()){
					HashMap<String, String> hashMaps = new HashMap<>();
					hashMaps.put("scrapedquantity", "scrapedquantity + " + detail.getScrapQuantity());
					
					scrapJdbcRepository.updateColumn(new MaterialIssueDetailEntity(),
							"materialissuedetail", hashMaps, " WHERE id = '"+ detail.getIssueDetail().getId() +"'  AND tenantid = '"+tenantId +"'");
					BigDecimal totalScrapedQuantity =detail.getIssueDetail().getScrapedQuantity().add(detail.getScrapQuantity());
					int res = detail.getQuantity().compareTo(totalScrapedQuantity);
					if( res == 0)
					{
						HashMap<String, String> hashMap = new HashMap<>();
						hashMap.put("scrapcreated", "true");
						scrapJdbcRepository.updateColumn(new MaterialIssueEntity(),
								"materialissue", hashMap, " WHERE issuenumber = (select materialissuenumber from materialissuedetail where id = '"+detail.getIssueDetail().getId() +"' AND tenantId = '"+ tenantId +"') "+" AND issuepurpose = 'WRITEOFFORSCRAP' AND tenantid = '" +tenantId +"'");
					}
			 	}
		 	}
	 	}
	}


