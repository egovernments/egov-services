package org.egov.pgr.write.service;

import org.egov.pgr.common.entity.ReceivingMode;
import org.egov.pgr.common.repository.ReceivingModeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReceivingModeWriteService {

	private final ReceivingModeRepository receivingModeWriteRepository;

	@Autowired
	public ReceivingModeWriteService(final ReceivingModeRepository receivingModeWriteRepository) {
		this.receivingModeWriteRepository = receivingModeWriteRepository;
	}

	public ReceivingMode getReceivingModeByCode(String code) {
		return receivingModeWriteRepository.findByCode(code);
	}
}
