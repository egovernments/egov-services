package org.egov.inv.domain.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import org.egov.common.Constants;
import org.egov.common.DomainService;
import org.egov.common.MdmsRepository;
import org.egov.common.Pagination;
import org.egov.common.exception.CustomBindException;
import org.egov.common.exception.ErrorCode;
import org.egov.common.exception.InvalidDataException;
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
import org.egov.inv.model.Uom;
import org.egov.inv.persistence.repository.ScrapDetailJdbcRepository;
import org.egov.inv.persistence.repository.ScrapJdbcRepository;
import org.egov.inv.persistence.repository.StoreJdbcRepository;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
				scrapData.setScrapNumber(appendString(scrapData));
				scrapData.setScrapStatus(ScrapStatusEnum.APPROVED);
			if (StringUtils.isEmpty(scrapData.getTenantId())) {
				scrapData.setTenantId(tenantId);
			}
			scrapData.getScrapDetails().forEach(scrapDetails -> {
				scrapDetails.setId(scrapJdbcRepository.getSequence("seq_scrapDetail"));
				scrapDetails.setTenantId(tenantId);
			});
		});
		
				kafkaTemplate.send(createTopic, scrapReq);
		return scrapReq.getScraps();
		}catch(CustomBindException e) {
			throw e;
		}
	}
	
	public List<Scrap> update(ScrapRequest scrapReq, String tenantId) {
		try{
			List<Scrap> scrap = scrapReq.getScraps();
			validate(scrapReq.getScraps(), Constants.ACTION_UPDATE,tenantId,scrapReq.getRequestInfo());
			
			scrap.forEach(scrapData -> {
				if (StringUtils.isEmpty(scrapData.getTenantId())) {
						scrapData.setTenantId(tenantId);
				}
				scrapData.getScrapDetails().forEach(scrapDetails -> {
					if (StringUtils.isEmpty(scrapDetails.getTenantId())) {
						scrapDetails.setTenantId(tenantId);
					}
				});
			});
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
	                List<ScrapDetail> scrapDetail = getScrapDetail(scrap.getScrapNumber(), scrapSearch.getTenantId());
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
				
				for(ScrapDetail scrapdetail : scrapData.getScrapDetails()){
					if(null == scrapdetail.getUserQuantity()) {
						errors.addDataError(ErrorCode.NOT_NULL.getCode(),"Scrap Quantity", null);
					}else
					{
						int scrapQty =scrapdetail.getUserQuantity().compareTo(BigDecimal.ZERO);
					if (scrapQty != 1) {
						errors.addDataError(ErrorCode.QUANTITY_GT_ZERO.getCode(),"Scrap Quantity "+scrapdetail.getUserQuantity());
					}
					}
					int scrapValue =scrapdetail.getScrapValue().compareTo(BigDecimal.ZERO);
					if(scrapValue!=1){
						errors.addDataError(ErrorCode.UNIT_PRICE_GT_ZERO.getCode());

					}
				}
			}
		}catch(IllegalArgumentException e) {
			throw e;
		}
		if (errors.getValidationErrors().size() > 0)
			throw errors;
	}
	
	private String appendString(Scrap scrapReq) {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		String code = "SCRP/";
		int id = Integer.valueOf(scrapJdbcRepository.getSequence(scrapReq));
		String idgen = String.format("%05d", id);
		String scrapNumber = code + idgen + "/" + year;
		return scrapNumber;
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
													.tenantId(tenantId)
													.build();
													
		MaterialIssueResponse response = nonIndentMaterialIssueService.search(searchContract);
		for(MaterialIssue issue : response.getMaterialIssues()){
			for(MaterialIssueDetail detail : issue.getMaterialIssueDetails()) {
				ScrapDetail scrapDetail= new ScrapDetail();
		if (issue == null )
			errors.addDataError(ErrorCode.DOESNT_MATCH.getCode(), "issuePurpose", null);
		
		else
			scrapDetail.setTenantId(tenantId); 
			scrapDetail.setUom(detail.getUom());
			scrapDetail.setMaterial(detail.getMaterial());
			scrapDetail.setExistingValue(detail.getValue());
			scrapDetail.setQuantity(detail.getQuantityIssued());
			
			if(scrapDetails.getUserQuantity() != null) {
				setConvertedScrapQuantity(tenantId, scrapDetails,detail);
			}
			
			if(scrapDetails.getScrapValue() != null) {
				setConvertedScrapRate(tenantId, scrapDetails,detail);
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
	
	 private List<ScrapDetail> getScrapDetail(String scrapNumber, String tenantId) {
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
	}


