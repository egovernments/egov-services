package org.egov.receipt.consumer.repository;

import java.lang.reflect.Field;
import java.util.Map;

import org.egov.mdms.service.TokenService;
import org.egov.receipt.consumer.model.RequestInfo;
import org.egov.receipt.custom.exception.VoucherCustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.HttpClientErrorException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

@Repository
@Slf4j
public class ServiceRequestRepository {
		
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private TokenService tokenService;
	private int callCounter = 0;
	/**
	 * Fetches results from searcher framework based on the uri and request that define what is to be searched.
	 * 
	 * @param requestInfo
	 * @param serviceReqSearchCriteria
	 * @return Object
	 * @author atique
	 */
	public Object fetchResult(StringBuilder uri, Object request, String tenantId) throws VoucherCustomException{
		ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		Object response = null;
		
		try {
			response = restTemplate.postForObject(uri.toString(), request, Map.class);
		}catch(HttpClientErrorException e) {
			// callCounter is to avoid the recursive call on multiple unauthorized access.
			if(e.getStatusCode().equals(HttpStatus.UNAUTHORIZED) && callCounter == 0){
				log.error("Unauthorized accessed : Retrying with SYSTEM auth token.");
				response = this.retryHttpCallOnUnauthorizedAccess(uri, request, tenantId);
			}else if(e.getStatusCode().equals(HttpStatus.UNAUTHORIZED)){
				// initializing to zero for next request call.
				callCounter = 0;
				log.error("Unauthorized accessed : Failed to access even after retrying with SYSTEM auth token : ",e);
				throw new VoucherCustomException("FAILED","Failed to access uri : "+uri+" even after retrying with SYSTEM auth token");
			}
		}catch(Exception e) {
			// initializing to zero for next request call.
			callCounter = 0;
			log.error("Exception while fetching from searcher: ",e);
			throw new VoucherCustomException("FAILED","Exception while fetching from searcher.");
		}
		return response;
	}
	
	private Object retryHttpCallOnUnauthorizedAccess(StringBuilder uri, Object request, String tenantId) throws VoucherCustomException{
		RequestInfo requestInfo = null;
		Class<?> clazz = request.getClass();
	    Field field = ReflectionUtils.findField(clazz, "requestInfo");
	    try {
			if(field != null){
				ReflectionUtils.makeAccessible(field);
				requestInfo = (RequestInfo) field.get(request);
				requestInfo.setAuthToken(tokenService.generateAdminToken(tenantId));
				ReflectionUtils.setField(field, request, requestInfo);
				field = ReflectionUtils.findField(clazz, "requestInfo");
				callCounter++;
				return this.fetchResult(uri, request, tenantId);				
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			log.error(e.getMessage());
		}
	    return null;
	}
}
