package org.egov.wcms.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.model.MeterStatus;
import org.egov.wcms.repository.builder.MeterStatusQueryBuilder;
import org.egov.wcms.repository.rowmapper.MeterStatusRowMapper;
import org.egov.wcms.service.CodeGeneratorService;
import org.egov.wcms.web.contract.MeterStatusGetRequest;
import org.egov.wcms.web.contract.MeterStatusReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MeterStatusRepository {
	public static final Logger logger = LoggerFactory.getLogger(MeterStatusRepository.class);

	@Autowired
	private ApplicationProperties applicationProperties;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	private MeterStatusQueryBuilder meterStatusQueryBuilder;

	@Autowired
	private CodeGeneratorService codeGeneratorService;

	@Autowired
	private MeterStatusRowMapper meterStatusRowMapper;

	public List<MeterStatus> pushMeterStatusCreateToQueue(MeterStatusReq meterStatusReq) {
		logger.info("Sending MeterStatusRequest to queue");
		List<MeterStatus> meterStatuses = meterStatusReq.getMeterStatus();
		for (MeterStatus meterStatus : meterStatuses) {
			meterStatus.setCode(codeGeneratorService.generate(MeterStatus.SEQ_METER_STATUS));
		}
		try {
			kafkaTemplate.send(applicationProperties.getCreateMeterStatusTopicName(), meterStatusReq);
		} catch (Exception e) {
			logger.error("Exception encountered:" + e);
		}
		return meterStatusReq.getMeterStatus();
	}

	public MeterStatusReq createMeterStatus(MeterStatusReq meterStatusRequest) {
		List<MeterStatus> meterStatuses = meterStatusRequest.getMeterStatus();
		List<Map<String, Object>> batchValues = new ArrayList<>(meterStatuses.size());
		for (MeterStatus meterStatus : meterStatuses) {
			batchValues.add(new MapSqlParameterSource("id", Long.valueOf(meterStatus.getCode()))
					.addValue("code", meterStatus.getCode()).addValue("status", meterStatus.getMeterStatus())
					.addValue("description", meterStatus.getDescription())
					.addValue("createdby", meterStatusRequest.getRequestInfo().getUserInfo().getId())
					.addValue("createddate", new Date().getTime())
					.addValue("lastmodifiedby", meterStatusRequest.getRequestInfo().getUserInfo().getId())
					.addValue("lastmodifieddate", new Date().getTime()).addValue("tenantId", meterStatus.getTenantId())
					.getValues());
		}
		namedParameterJdbcTemplate.batchUpdate(meterStatusQueryBuilder.getCreateMeterStatusQuery(),
				batchValues.toArray(new Map[meterStatuses.size()]));
		return meterStatusRequest;

	}

	public List<MeterStatus> pushMeterStatusUpdateToQueue(MeterStatusReq meterStatusRequest) {
		logger.info("Sending MeterStatusRequest to queue");
		try {
			kafkaTemplate.send(applicationProperties.getUpdateMeterStatusTopicName(), meterStatusRequest);
		} catch (Exception e) {
			logger.error("Exception encountered:" + e);
		}
		return meterStatusRequest.getMeterStatus();
	}

	public MeterStatusReq updateMeterStatus(MeterStatusReq meterStatusRequest) {
		List<MeterStatus> meterStatuses = meterStatusRequest.getMeterStatus();
		List<Map<String, Object>> batchValues = new ArrayList<>(meterStatuses.size());
		for (MeterStatus meterStatus : meterStatuses) {
			batchValues.add(new MapSqlParameterSource("status", meterStatus.getMeterStatus())
					.addValue("description", meterStatus.getDescription())
					.addValue("lastmodifiedby", meterStatusRequest.getRequestInfo().getUserInfo().getId())
					.addValue("lastmodifieddate", new Date().getTime()).addValue("code", meterStatus.getCode())
					.addValue("tenantId", meterStatus.getTenantId()).getValues());
		}
		namedParameterJdbcTemplate.batchUpdate(meterStatusQueryBuilder.getUpdateMeterStatusQuery(),
				batchValues.toArray(new Map[meterStatuses.size()]));
		return meterStatusRequest;
	}

	public List<MeterStatus> getMeterStatusByCriteria(MeterStatusGetRequest meterStatusGetRequest) {
		Map<String, Object> preparedStatementValues = new HashMap<>();
		String queryString = meterStatusQueryBuilder.getQuery(meterStatusGetRequest, preparedStatementValues);
		return namedParameterJdbcTemplate.query(queryString, preparedStatementValues, meterStatusRowMapper);
	}

}
