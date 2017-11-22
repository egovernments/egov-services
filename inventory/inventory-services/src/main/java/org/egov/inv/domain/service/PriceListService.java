package org.egov.inv.domain.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.egov.common.Constants;
import org.egov.common.DomainService;
import org.egov.common.Pagination;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.exception.CustomBindException;
import org.egov.common.exception.ErrorCode;
import org.egov.common.exception.InvalidDataException;
import org.egov.inv.model.PriceList;
import org.egov.inv.model.PriceListDetails;
import org.egov.inv.model.PriceListDetailsSearchRequest;
import org.egov.inv.model.PriceListRequest;
import org.egov.inv.model.PriceListResponse;
import org.egov.inv.model.PriceListSearchRequest;
import org.egov.inv.persistence.entity.PriceListEntity;
import org.egov.inv.persistence.repository.PriceListDetailJdbcRepository;
import org.egov.inv.persistence.repository.PriceListJdbcRepository;
import org.egov.inv.persistence.repository.PriceListRepository;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class PriceListService extends DomainService {


    public static final String UPDATE = "UPDATE";
    public static final String CREATE = "CREATE";
    
    @Value("${inv.pricelists.save.topic}")
    private String saveTopic;

    @Value("${inv.pricelists.save.key}")
    private String saveKey;

    @Value("${inv.pricelists.update.topic}")
    private String updateTopic;

    @Value("${inv.pricelists.update.key}")
    private String updateKey;
    
    @Autowired
    private PriceListRepository priceListRepository;

    @Autowired
    private PriceListJdbcRepository priceListJdbcRepository;
    
    @Autowired
    private PriceListDetailJdbcRepository priceListDetailsJdbcRepository;
    
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
    public static final String SEQ_PRICELIST = "seq_priceList";
    public static final String SEQ_PRICELISTDETAILS = "seq_pricelistdetails";
    
    public PriceListResponse save(PriceListRequest priceListRequest, String tenantId) {

        try {
        	
            List<String> priceListIdList = priceListJdbcRepository.getSequence(PriceList.class.getSimpleName(), priceListRequest.getPriceLists().size());
            validate(priceListRequest.getPriceLists(), Constants.ACTION_CREATE);
            priceListRequest.getPriceLists().forEach(priceList -> {
                priceList.setAuditDetails(mapAuditDetails(priceListRequest.getRequestInfo()));
                priceList.getPriceListDetails().forEach(priceListDetail -> {
                	priceListDetail.setAuditDetails(mapAuditDetails(priceListRequest.getRequestInfo()));
                	priceList.setRateContractNumber(priceList.getRateContractNumber().toUpperCase());
					priceList.setAgreementNumber(priceList.getAgreementNumber().toUpperCase());
					
					if(priceListDetail.getFromDate()==null){
						priceListDetail.setFromDate(priceList.getAgreementStartDate());
					}
					if(priceListDetail.getToDate()==null){
						priceListDetail.setToDate(priceList.getAgreementEndDate());
					}
					if(priceListDetail.getUom().getCode()!=null)
					{
						priceListDetail.setQuantity((priceListDetail.getUom().getConversionFactor()).doubleValue()*priceListDetail.getQuantity());
					}
                });
            });
            
            for (int i = 0; i <= priceListIdList.size() - 1; i++) {
            	PriceList priceList1 = priceListRequest.getPriceLists().get(i);
				if (!priceListJdbcRepository.uniqueCheck("rateContractNumber", new PriceListEntity().toEntity(priceList1))) {
				    throw new CustomException("inv.0011", "RateContract Number already exists " + priceList1.getRateContractNumber());
				}
                priceListRequest.getPriceLists().get(i)
                        .setId(priceListIdList.get(i).toString());
                
                if(priceListRequest.getPriceLists().get(i).getTenantId()==null){
                	priceListRequest.getPriceLists().get(i).setTenantId(tenantId);
                	for(PriceListDetails priceListDetail:priceListRequest.getPriceLists().get(i).getPriceListDetails()){
                		if(priceListDetail.getTenantId()==null){
                			priceListDetail.setTenantId(tenantId);
                		}
                	}
                }
                
                List<String> priceListDetailsIdList = priceListJdbcRepository.getSequence(PriceListDetails.class.getSimpleName(), priceListRequest.getPriceLists().get(i).getPriceListDetails().size());
                for(int j =0; j <= priceListDetailsIdList.size()-1; j++){
                	priceListRequest.getPriceLists().get(i)
    			            		.getPriceListDetails().get(j)
    			            		.setId(priceListDetailsIdList.get(j).toString());
                }
            }

            kafkaQue.send(saveTopic, saveKey, priceListRequest);

            PriceListResponse response = PriceListResponse.builder()
                    .priceLists(priceListRequest.getPriceLists())
                    .responseInfo(getResponseInfo(priceListRequest.getRequestInfo()))
                    .build();

            return response;
        } catch (CustomBindException e) {
            throw e;
        }
    }

	public PriceListResponse update(PriceListRequest priceListRequest,
			String tenantId) {

		try {
			validate(priceListRequest.getPriceLists(), Constants.ACTION_UPDATE);
			List<String> ids=new ArrayList<String>();
			priceListRequest.getPriceLists().stream().forEach(priceList -> {
								priceList.setAuditDetails(mapAuditDetailsForUpdate(priceListRequest.getRequestInfo()));
								priceList.setRateContractNumber(priceList.getRateContractNumber().toUpperCase());
								priceList.setAgreementNumber(priceList.getAgreementNumber().toUpperCase());
								if(priceList.getTenantId()==null){
									priceList.setTenantId(tenantId);
								}
								
								PriceListDetailsSearchRequest priceListDetailsSearchRequest = new PriceListDetailsSearchRequest();
								priceListDetailsSearchRequest.setPriceList(priceList.getId());
								priceListDetailsSearchRequest.setActive(true);
								priceListDetailsSearchRequest.setDeleted(false);
								
								List<PriceListDetails> oldPriceListDetails = priceListDetailsJdbcRepository.search(priceListDetailsSearchRequest).getPagedData();
								int actualOldCount = oldPriceListDetails.size();
								
								for(PriceListDetails priceListDetail:priceList.getPriceListDetails()){
									if(priceListDetail.getTenantId()==null){
										priceListDetail.setTenantId(tenantId);
									}
									if(priceListDetail.getUom().getCode()!=null)
									{
										priceListDetail.setQuantity((priceListDetail.getUom().getConversionFactor()).doubleValue()*priceListDetail.getQuantity());
									}
									Iterator<PriceListDetails> iter = oldPriceListDetails.iterator();
									while(iter.hasNext()){
										if(iter.next().getId().equals(priceListDetail.getId()))
											iter.remove();
									}
								}
								
								int removedIdsCount = oldPriceListDetails.size();
								int newIdsCount = priceList.getPriceListDetails().size() + removedIdsCount - actualOldCount;
								int newIdStartRange = actualOldCount-removedIdsCount;
								
								List<String> priceListDetailsIdList = priceListJdbcRepository.getSequence(PriceListDetails.class.getSimpleName(), newIdsCount);
								
								for(PriceListDetails pldl:priceList.getPriceListDetails()){
									if(pldl.getId()==null){
										pldl.setId(priceListDetailsIdList.get(0));
										pldl.setAuditDetails(mapAuditDetails(priceListRequest.getRequestInfo()));
										if(pldl.getFromDate()==null){
											pldl.setFromDate(priceList.getAgreementStartDate());
										}
										if(pldl.getToDate()==null){
											pldl.setToDate(priceList.getAgreementEndDate());
										}
										priceListDetailsIdList.remove(0);
									}
									ids.add(pldl.getId());
								}
								
//								for(PriceListDetails pld:oldPriceListDetails){
//									pld.setIsDeleted(true);
//									priceList.getPriceListDetails().add(pld);
//								}
							});

			kafkaQue.send(updateTopic, updateKey, priceListRequest);
			priceListDetailsJdbcRepository.markDeleted(ids,tenantId,"pricelistdetails","pricelist",priceListRequest.getPriceLists().get(0).getId());
			PriceListResponse response = new PriceListResponse();
			response.setResponseInfo(getResponseInfo(priceListRequest
					.getRequestInfo()));
			response.setPriceLists(priceListRequest.getPriceLists());
			
			return response;
		} catch (CustomBindException e) {
			throw e;
		}
	}

	public PriceListResponse search(
			PriceListSearchRequest priceListSearchRequest, RequestInfo requestInfo) {

		Pagination<PriceList> searchPriceLists = priceListRepository.search(priceListSearchRequest);
		
        PriceListResponse response = new PriceListResponse();
        response.setPriceLists(searchPriceLists.getPagedData().size() > 0 ? searchPriceLists.getPagedData() : Collections.emptyList());
		return response;
	}
	
	private void validate(List<PriceList> priceLists, String method) {

		try {

			switch (method) {

			case Constants.ACTION_CREATE: {
				if (priceLists == null) {
					throw new InvalidDataException("priceLists", ErrorCode.NOT_NULL.getCode(), null);
				}
			}
				break;

			case Constants.ACTION_UPDATE: {
				if (priceLists == null) {
					throw new InvalidDataException("priceLists", ErrorCode.NOT_NULL.getCode(), null);
				}
			}
				break;

			}
			
			Long currentMilllis = System.currentTimeMillis();
			
			for(PriceList pl : priceLists){
				if(Long.valueOf(pl.getAgreementDate()) > currentMilllis){
					throw new CustomException("agreementDate", "Agreement Date must be less than or equal to Today's date");
				}
				
				if(Long.valueOf(pl.getRateContractDate()) > currentMilllis){
					throw new CustomException("rateContractDate", "Rate Contract Date must be less than or equal to Today's date");
				}
				
				if(Long.valueOf(pl.getAgreementStartDate()) > currentMilllis){
					throw new CustomException("agreementStartDate", "Agreement Start Date must be less than or equal to Today's date");
				}
				
				if(Long.valueOf(pl.getAgreementEndDate()) < Long.valueOf(pl.getAgreementStartDate())){
					throw new CustomException("agreementEndDate", "Agreement End Date must be greater than or equal to Agreement start date");
				}
				
				for(PriceListDetails priceListDetail : pl.getPriceListDetails()){
					if(priceListDetail.getQuantity().doubleValue()<0){
						throw new CustomException("quantity", "Material "+ priceListDetail.getMaterial().getCode() + "'s quantity should be positive");
					}
				}
			}
			
//			if(priceListJdbcRepository.isDuplicateContract(priceLists)){
//				throw new CustomException("inv.0011", "A ratecontract already exists in the system for the given material in the specified time duration. Please select alternate duration for the contract.");
//			}
			
		} catch (IllegalArgumentException e) {

		}

	}
	
}
