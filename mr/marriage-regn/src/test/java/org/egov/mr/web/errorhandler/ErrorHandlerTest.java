package org.egov.mr.web.errorhandler;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.mr.model.RegistrationUnit;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

@RunWith(MockitoJUnitRunner.class)
public class ErrorHandlerTest {

	@InjectMocks
	private ErrorHandler errorHandler;

	@Mock
	private ErrorHandler errorHandlerNoArgsConstructor = new ErrorHandler();

	@Mock
	private RequestInfo requestInfo;

	@Mock
	private BindingResult bindingResult;

	@Mock
	private BindingResult bindingResultsForRequestInfo;

	@Mock
	private BindingResult bindingResultsForSearchCriteria;

	@Mock
	private RegistrationUnit registrationUnit;

	@Mock
	private ObjectError error = new ObjectError("tenantId", "nullObtained");

	@Test
	public void testReqForCreate() {

		ResponseEntity<?> errResEntity = errorHandler.handleBindingErrorsForCreate(getRequestInfo(), bindingResult);
		assertEquals(errResEntity, null);
	}

	@Test
	public void testReqAndCriteria() {
		ResponseEntity<?> errResEntity = errorHandler.handleBindingErrorsForSearch(getRequestInfo(),
				bindingResultsForRequestInfo, bindingResultsForSearchCriteria);
		assertEquals(errResEntity, null);
	}

	@Test
	public void testReqForUpdate() {
		ResponseEntity<?> errResEntity = errorHandler.handleBindingErrorsForUpdate(getRequestInfo(), bindingResult);
		assertEquals(errResEntity, null);
	}

	@Test
	public void testReqForMissingParameter() {
		ResponseEntity<?> errResEntity = errorHandler.getMissingParameterErrorResponse(bindingResult, getRequestInfo());
		assertTrue(errResEntity.getStatusCode().equals(HttpStatus.BAD_REQUEST));
	}

	@Test
	public void testReqForUnExpectedError() {
		ResponseEntity<?> errResEntity = errorHandler.getUnExpectedErrorResponse(bindingResult, getRequestInfo(),
				"NotSpecific");
		assertTrue(errResEntity.getStatusCode().equals(HttpStatus.BAD_REQUEST));
	}

	@Test
	public void testReqForUpdateErrorResponse() {
		ResponseEntity<?> errResEntity = errorHandler.getUpdateErrorResponse(bindingResult, getRequestInfo(),
				registrationUnit);
		assertTrue(errResEntity.getStatusCode().equals(HttpStatus.BAD_REQUEST));
	}

	private RequestInfo getRequestInfo() {
		requestInfo.setAction("ACTIVE");
		requestInfo.setApiId("ALLAPIS");
		requestInfo.setAuthToken("15AS-145625AS-AS7AS41XC4-AS4C1");
		requestInfo.setCorrelationId("X9SRG52H");
		requestInfo.setKey("requestKey");
		requestInfo.setDid("requestDeviceid");
		requestInfo.setMsgId("requestMsg");
		requestInfo.setTs(Long.valueOf("987456321"));
		requestInfo.setUserInfo(new User());
		requestInfo.setVer("requestVersion");
		return requestInfo;
	}
}
