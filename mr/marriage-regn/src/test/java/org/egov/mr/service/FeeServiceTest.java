package org.egov.mr.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.mr.config.PropertiesManager;
import org.egov.mr.model.Fee;
import org.egov.mr.repository.FeeRepository;
import org.egov.mr.util.SequenceIdGenService;
import org.egov.mr.web.contract.FeeCriteria;
import org.egov.mr.web.contract.FeeRequest;
import org.egov.mr.web.contract.FeeResponse;
import org.egov.mr.web.contract.ResponseInfoFactory;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.kafka.support.SendResult;

@RunWith(MockitoJUnitRunner.class)
public class FeeServiceTest {

	@Mock
	private ResponseInfoFactory responseInfoFactory;

	@Mock
	private FeeRepository feeRepository;

	@Mock
	private SequenceIdGenService sequenceGenUtil;

	@Mock
	private PropertiesManager propertiesManager;

	@Mock
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	@InjectMocks
	private FeeService feeService;

	@Test
	public void testShouldGetFee() {

		FeeResponse feeResponse = FeeResponse.builder().fees(getFees()).build();

		when(feeRepository.findForCriteria(Matchers.any(FeeCriteria.class))).thenReturn(getFees());

		FeeCriteria feeCriteria = FeeCriteria.builder().tenantId("default").feeCriteria("2017-2018")
				.fromDate(123456789l).toDate(23456789l).build();

		assertEquals(feeResponse, feeService.getFee(feeCriteria, new RequestInfo()));

	}

	@Test
	public void testShouldCreateAsync() {

		FeeResponse feeResponse = FeeResponse.builder().fees(getFees()).build();

		FeeRequest feeRequest = FeeRequest.builder().fees(getFees()).requestInfo(new RequestInfo()).build();
		List<String> ids = new ArrayList<>();
		ids.add("1");
		when(sequenceGenUtil.getIds(Matchers.any(int.class), Matchers.any(String.class))).thenReturn(ids);

		when(propertiesManager.getCreateFeeTopicName()).thenReturn("egov-create");

		when(kafkaTemplate.send(Matchers.any(String.class), Matchers.any(Object.class)))
				.thenReturn(new SendResult<>(null, null));

		assertTrue(feeResponse.toString().equals(feeService.createAsync(feeRequest).toString()));

	}

	@Test
	public void testShouldUpdateAsync() {

		FeeResponse feeResponse = FeeResponse.builder().fees(getFees()).build();

		FeeRequest feeRequest = FeeRequest.builder().fees(getFees()).requestInfo(new RequestInfo()).build();

		when(propertiesManager.getUpdateFeeTopicName()).thenReturn("egov-update");

		when(kafkaTemplate.send(Matchers.any(String.class), Matchers.any(Object.class)))
				.thenReturn(new SendResult<>(null, null));

		assertTrue(feeResponse.toString().equals(feeService.updateAsync(feeRequest).toString()));

	}

	@Test
	public void testShouldCreateFee() {
		doNothing().when(feeRepository).createFee(any(FeeRequest.class));
		feeService.createFee(FeeRequest.builder().fees(getFees()).build());
	}

	@Test
	public void testShouldUpdateFee() {
		doNothing().when(feeRepository).updateFee(any(FeeRequest.class));
		feeService.updateFee(FeeRequest.builder().fees(getFees()).build());
	}

	private List<Fee> getFees() {
		Fee fee = Fee.builder().id("1").tenantId("default").fee(new BigDecimal("10")).feeCriteria("2017-2018")
				.fromDate(123456789l).toDate(23456789l).build();
		List<Fee> fees = new ArrayList<>();
		fees.add(fee);

		return fees;
	}
}
