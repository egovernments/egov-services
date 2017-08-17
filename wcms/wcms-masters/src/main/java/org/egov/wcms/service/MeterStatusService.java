package org.egov.wcms.service;

import java.util.List;

import org.egov.wcms.model.MeterStatus;
import org.egov.wcms.repository.MeterStatusRepository;
import org.egov.wcms.web.contract.MeterStatusGetRequest;
import org.egov.wcms.web.contract.MeterStatusReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MeterStatusService {
	@Autowired
	private MeterStatusRepository meterStatusRepository;

	public static final Logger logger = LoggerFactory.getLogger(MeterStatusService.class);

	public List<MeterStatus> pushCreateMeterStatusToQueue(MeterStatusReq meterStatusReq) {
		logger.info("MeterStatusRequest :" + meterStatusReq);
		return meterStatusRepository.pushMeterStatusCreateToQueue(meterStatusReq);

	}

	public MeterStatusReq createMeterStatus(MeterStatusReq meterStatusRequest) {
		return meterStatusRepository.createMeterStatus(meterStatusRequest);
	}

	public List<MeterStatus> pushUpdateMeterStatusToQueue(MeterStatusReq meterStatusRequest) {
		logger.info("MeterStatusRequest :" + meterStatusRequest);
		return meterStatusRepository.pushMeterStatusUpdateToQueue(meterStatusRequest);
	}

	public MeterStatusReq updateMeterStatus(MeterStatusReq meterStatusRequest) {
		return meterStatusRepository.updateMeterStatus(meterStatusRequest);
	}

	public List<MeterStatus> getMeterStatus(MeterStatusGetRequest meterStatusGetRequest) {
		return meterStatusRepository.getMeterStatusByCriteria(meterStatusGetRequest);

	}

}
