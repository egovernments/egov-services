package org.egov.collection.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.collection.config.ApplicationProperties;
import org.egov.collection.config.CollectionServiceConstants;
import org.egov.collection.exception.CustomException;
import org.egov.collection.model.IdGenRequestInfo;
import org.egov.collection.model.IdRequest;
import org.egov.collection.model.IdRequestWrapper;
import org.egov.common.contract.request.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.jayway.jsonpath.JsonPath;

@Service
public class IdGenRepository {
	
    @Autowired
    public RestTemplate restTemplate;
    
	@Autowired
	private ApplicationProperties applicationProperties;
	
	public static final Logger LOGGER = LoggerFactory.getLogger(IdGenRepository.class);
	
	public String generateReceiptNumber(RequestInfo requestInfo, String tenantId) {
		LOGGER.info("Generating receipt number for the receipt.");

		StringBuilder builder = new StringBuilder();
		String hostname = applicationProperties.getIdGenServiceHost();
		String baseUri = applicationProperties.getIdGeneration();
		builder.append(hostname).append(baseUri);

		LOGGER.info("URI being hit: " + builder.toString());

		IdRequestWrapper idRequestWrapper = new IdRequestWrapper();
		IdGenRequestInfo idGenReq = new IdGenRequestInfo();

		// Because idGen Svc uses a slightly different form of requestInfo

		idGenReq.setAction(requestInfo.getAction());
		idGenReq.setApiId(requestInfo.getApiId());
		idGenReq.setAuthToken(requestInfo.getAuthToken());
		idGenReq.setCorrelationId(requestInfo.getCorrelationId());
		idGenReq.setDid(requestInfo.getDid());
		idGenReq.setKey(requestInfo.getKey());
		idGenReq.setMsgId(requestInfo.getMsgId());
        //idGenReq.setRequesterId(requestInfo.getRequesterId());
		idGenReq.setTs(requestInfo.getTs().getTime()); 
		idGenReq.setUserInfo(requestInfo.getUserInfo());
		idGenReq.setVer(requestInfo.getVer());

		IdRequest idRequest = new IdRequest();
		idRequest.setIdName(CollectionServiceConstants.COLL_ID_NAME);
		idRequest.setTenantId(tenantId);
		idRequest.setFormat(CollectionServiceConstants.COLL_ID_FORMAT);

		List<IdRequest> idRequests = new ArrayList<>();
		idRequests.add(idRequest);

		idRequestWrapper.setIdGenRequestInfo(idGenReq);
		idRequestWrapper.setIdRequests(idRequests);
		Object response = null;
		
		LOGGER.info("Request for idgen rcptno: " + idRequestWrapper.toString());


		try {
			response = restTemplate.postForObject(builder.toString(),
					idRequestWrapper, Object.class);
		} catch (Exception e) {
			throw new CustomException(Long.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.toString()),
					CollectionServiceConstants.RCPTNO_EXCEPTION_MSG, CollectionServiceConstants.RCPTNO_EXCEPTION_DESC);

		}
		LOGGER.info("Response from id gen service: " + response.toString());

		return JsonPath.read(response, "$.idResponses[0].id");
	}

    public String generateTransactionNumber(RequestInfo requestInfo, String tenantId) {
        LOGGER.info("Generating transaction number for the receipt.");

        StringBuilder builder = new StringBuilder();
        String hostname = applicationProperties.getIdGenServiceHost();
        String baseUri = applicationProperties.getIdGeneration();
        builder.append(hostname).append(baseUri);

        LOGGER.info("URI being hit: " + builder.toString());

        IdRequestWrapper idRequestWrapper = new IdRequestWrapper();

        IdGenRequestInfo idGenReq = new IdGenRequestInfo();

        // Because idGen Svc uses a slightly different form of requestInfo

        idGenReq.setAction(requestInfo.getAction());
        idGenReq.setApiId(requestInfo.getApiId());
        idGenReq.setAuthToken(requestInfo.getAuthToken());
        idGenReq.setCorrelationId(requestInfo.getCorrelationId());
        idGenReq.setDid(requestInfo.getDid());
        idGenReq.setKey(requestInfo.getKey());
        idGenReq.setMsgId(requestInfo.getMsgId());
        //idGenReq.setRequesterId(requestInfo.getRequesterId());
        idGenReq.setTs(requestInfo.getTs().getTime()); 
        idGenReq.setUserInfo(requestInfo.getUserInfo());
        idGenReq.setVer(requestInfo.getVer());
        IdRequest idRequest = new IdRequest();
        idRequest.setIdName(CollectionServiceConstants.COLL_TRANSACTION_ID_NAME);
        idRequest.setTenantId(tenantId);
        String splitTenant = tenantId.contains(".") ? tenantId.split("\\.")[1] : tenantId; 
        idRequest.setFormat(CollectionServiceConstants.COLL_TRANSACTION_FORMAT.replace("{tenant}", splitTenant));

        List<IdRequest> idRequests = new ArrayList<>();
        idRequests.add(idRequest);

        idRequestWrapper.setIdGenRequestInfo(idGenReq);
        idRequestWrapper.setIdRequests(idRequests);
        Object response = null;
        
		LOGGER.info("Request for idgen transactionId: " + idRequestWrapper.toString());

        try {
            response = restTemplate.postForObject(builder.toString(),
                    idRequestWrapper, Object.class);
        } catch (Exception e) {
            throw new CustomException(Long.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.toString()),
                    CollectionServiceConstants.TRANSACTIONNO_EXCEPTION_MSG, CollectionServiceConstants.TRANSACTIONNO_EXCEPTION_DESC);

        }
        LOGGER.info("Response from id gen service: " + response.toString());

        return JsonPath.read(response, "$.idResponses[0].id");
    }

    public String generateRemittanceNumber(RequestInfo requestInfo, String tenantId) {
        LOGGER.info("Generating transaction number for the receipt.");

        StringBuilder builder = new StringBuilder();
        String hostname = applicationProperties.getIdGenServiceHost();
        String baseUri = applicationProperties.getIdGeneration();
        builder.append(hostname).append(baseUri);

        LOGGER.info("URI being hit: " + builder.toString());

        IdRequestWrapper idRequestWrapper = new IdRequestWrapper();

        IdGenRequestInfo idGenReq = new IdGenRequestInfo();

        // Because idGen Svc uses a slightly different form of requestInfo

        idGenReq.setAction(requestInfo.getAction());
        idGenReq.setApiId(requestInfo.getApiId());
        idGenReq.setAuthToken(requestInfo.getAuthToken());
        idGenReq.setCorrelationId(requestInfo.getCorrelationId());
        idGenReq.setDid(requestInfo.getDid());
        idGenReq.setKey(requestInfo.getKey());
        idGenReq.setMsgId(requestInfo.getMsgId());
        //idGenReq.setRequesterId(requestInfo.getRequesterId());
        idGenReq.setTs(requestInfo.getTs().getTime());
        idGenReq.setUserInfo(requestInfo.getUserInfo());
        idGenReq.setVer(requestInfo.getVer());
        IdRequest idRequest = new IdRequest();
        idRequest.setIdName(CollectionServiceConstants.COLL_REMITTENACE_ID_NAME);
        idRequest.setTenantId(tenantId);
        idRequest.setFormat(CollectionServiceConstants.COLL_REMITTENACE_ID_FORMAT);

        List<IdRequest> idRequests = new ArrayList<>();
        idRequests.add(idRequest);

        idRequestWrapper.setIdGenRequestInfo(idGenReq);
        idRequestWrapper.setIdRequests(idRequests);
        Object response = null;

        LOGGER.info("Request for idgen remittance Id: " + idRequestWrapper.toString());

        try {
            response = restTemplate.postForObject(builder.toString(),
                    idRequestWrapper, Object.class);
        } catch (Exception e) {
            throw new CustomException(Long.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.toString()),
                    CollectionServiceConstants.REMITTANCENO_EXCEPTION_MSG, CollectionServiceConstants.REMITTANCENO_EXCEPTION_DESC);

        }
        LOGGER.info("Response from id gen service: " + response.toString());

        return JsonPath.read(response, "$.idResponses[0].id");
    }
}
